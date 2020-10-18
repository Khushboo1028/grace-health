package com.replon.www.grace_thehealthapp.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";
    RelativeLayout mainrel;

    Button btn_login;

    ProgressBar progressBar;

    private RequestQueue rq;

    EditText et_email,et_password;
    SharedPreferences preferences;

    TextView tv_new_user,tv_forgot_password;

    CustomDialog customDialog;

    UserDataStore userDataStore;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), LoginActivity.this);

        setContentView(R.layout.activity_login);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_purple_light));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        mainrel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(et_email.getText().toString().trim().isEmpty() || et_password.getText().toString().trim().isEmpty()){
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "Please enter all the details.",
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            LoginActivity.this);
                }else if (!isValidEmailAddress(et_email.getText().toString().trim())){
                    // SHOW MESSAGE THAT EMAIL IS INVALID
//                    Toast.makeText(getApplicationContext(),"INVALID EMAIL",Toast.LENGTH_SHORT).show();
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "Details you have entered are incorrect.",
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            LoginActivity.this);
                }else{
                    login(et_email.getText().toString().trim(),et_password.getText().toString());
                }


            }
        });



        tv_new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
            }
        });

        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });



    }


    private void init(){

        mainrel = (RelativeLayout) findViewById(R.id.mainrel);

        btn_login = (Button) findViewById(R.id.btn_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        tv_new_user = (TextView) findViewById(R.id.tv_new_user);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);


        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);

        preferences = getSharedPreferences(getString(R.string.SharedPref), Context.MODE_PRIVATE);

        customDialog = new CustomDialog();

        rq = Volley.newRequestQueue(this);

        userDataStore = new UserDataStore(LoginActivity.this);

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


    }

    private void login(String email, String password){

        progressBar.setVisibility(View.VISIBLE);

//        JSONObject paramJson = new JSONObject();
//
//        try {
//            paramJson.put("email", email);
//            paramJson.put("password", password);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }



//        String url = getString(R.string.BASE_URL)+"/login";

//        final JsonRequest request = new JsonRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//                try {
//
//                    progressBar.setVisibility(View.GONE);
//
//                    JSONObject data = response.getJSONObject("data");
//                    JSONObject headers = response.getJSONObject("headers");
//
//
//                    userDataStore.storeUserData(data);
//                    JSONObject userJSON = userDataStore.readUserData();
//
//                    try{
//                        if (userJSON.getString("auth_token")!=null){
//                            finish();
//                        }
//                    }catch (JSONException e){
//                        e.printStackTrace();
//                    }
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Log.i(TAG,"Error is " + e.getMessage());
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressBar.setVisibility(View.GONE);
//
//                JSONObject data = null;
//
//                try {
//                    String responseBody = new String(error.networkResponse.data, "utf-8");
//                    data = new JSONObject(responseBody);
//                    String message = data.optString("error");
//                    Log.i(TAG,"SOME ERROR "+message);
//                    customDialog.showMessageOneOption(
//                            "Oh Snap!",
//                            message,
//                            R.drawable.ic_error,
//                            R.color.login_purple_light,
//                            "Dismiss",
//                            LoginActivity.this);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//
//        })
        // OVERRIDE METHODS
//        {

//            @Override
//            public Map getHeaders() throws AuthFailureError {
//                HashMap headers = new HashMap();
//                headers.put("Content-Type", "application/json");
//                headers.put("x-auth", xauth_try);
//                return headers;
//            }
//        };

//        rq.add(request);

        //FIREBASE LOGIN CODE
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.i(TAG,"User logged in successfully");
                        progressBar.setVisibility(View.GONE);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,"User login Error occurred" + e.getLocalizedMessage());
                        customDialog.showMessageOneOption(
                            "Oh Snap!",
                            e.getLocalizedMessage(),
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            LoginActivity.this);
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }


    public static boolean isValidEmailAddress(String email) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }



    public class JsonRequest extends JsonObjectRequest {

        public JsonRequest(int method, String url, JSONObject jsonRequest, Response.Listener
                <JSONObject> listener, Response.ErrorListener errorListener) {
            super(method, url, jsonRequest, listener, errorListener);
        }

        @Override
        protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

                JSONObject jsonResponse = new JSONObject();
                jsonResponse.put("data", new JSONObject(jsonString));
                jsonResponse.put("headers", new JSONObject(response.headers));

                return Response.success(jsonResponse,
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException je) {
                return Response.error(new ParseError(je));
            }
        }
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }


    @Override
    protected void onStart() {
        super.onStart();
//        JSONObject userJSON = userDataStore.readUserData();
//        if (userJSON!=null){
//            if (userJSON.has("auth_token")){
//                try{
//                    if (!userJSON.getString("auth_token").equals("")){
//                        finish();
//                    }
//                }catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        }

    }
}
