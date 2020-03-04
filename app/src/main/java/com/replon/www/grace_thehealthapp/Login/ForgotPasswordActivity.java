package com.replon.www.grace_thehealthapp.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ForgotPasswordActivity extends AppCompatActivity {

    public static final String TAG = "ForgotPassActivity";
    ImageView back;

    RelativeLayout mainrel;

    TextView tv_forgot_password;
    EditText et_email;

    Button btn_continue;
    CustomDialog customDialog;
    private RequestQueue rq;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), ForgotPasswordActivity.this);

        setContentView(R.layout.activity_forgot_password);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.login_purple_light));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mainrel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                InputMethodManager imm =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_email.getText().toString().trim().isEmpty()){
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "Please enter all the details.",
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            ForgotPasswordActivity.this);
                }else if (!isValidEmailAddress(et_email.getText().toString().trim())){
                    // SHOW MESSAGE THAT EMAIL IS INVALID
//                    Toast.makeText(getApplicationContext(),"INVALID EMAIL",Toast.LENGTH_SHORT).show();
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "Details you have entered are incorrect.",
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            ForgotPasswordActivity.this);
                }else{
                    forgot_password(et_email.getText().toString().trim());
                }
            }
        });

    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        mainrel = (RelativeLayout) findViewById(R.id.mainrel);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);

        et_email = (EditText) findViewById(R.id.et_email);

        btn_continue = (Button) findViewById(R.id.btn_continue);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        customDialog = new CustomDialog();

        rq = Volley.newRequestQueue(this);

    }

    private void forgot_password(String email){

        progressBar.setVisibility(View.VISIBLE);

        String url = getString(R.string.BASE_URL)+"/forgot-password";

        JSONObject paramJson = new JSONObject();

        try {
            paramJson.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final JsonRequest jsonRequest = new JsonRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{

                    JSONObject data = response.getJSONObject("data");

                    if (data.getString("success").equalsIgnoreCase("successfully sent password")){
                        Toast.makeText(getApplicationContext(),"Email sent successfully",Toast.LENGTH_LONG).show();
                        finish();
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_LONG).show();
                    Log.i(TAG,"ERROR IS "+ e.getMessage());
                    progressBar.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_LONG).show();
                Log.i(TAG,"ERROR IS "+ error.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });

        rq.add(jsonRequest);

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






    public static boolean isValidEmailAddress(String email) {
        boolean stricterFilter = true;
        String stricterFilterString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";
        String laxString = ".+@.+\\.[A-Za-z]{2}[A-Za-z]*";
        String emailRegex = stricterFilter ? stricterFilterString : laxString;
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(emailRegex);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.exit_start_activity,R.anim.exit_finish_activity);
    }
}
