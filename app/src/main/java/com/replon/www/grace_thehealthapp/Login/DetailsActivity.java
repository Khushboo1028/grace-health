package com.replon.www.grace_thehealthapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.replon.www.grace_thehealthapp.MainActivity;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.UserDataStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailsActivity extends AppCompatActivity {


    public static final String TAG = "DetailsActivity";
    ImageView img_male,img_female;


    Calendar date;
    RelativeLayout dob_rel;
    TextView tv_dob, tv_weight, tv_height;

    Button btn_continue;

    SeekBar weight_seekbar,height_seekbar;

    String name,email,password,gender,dob,date_created;
    int height, weight;

    ProgressBar progressBar;

    CustomDialog customDialog;

    SharedPreferences preferences;

    UserDataStore userDataStore;

    private RequestQueue rq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), DetailsActivity.this);

        setContentView(R.layout.activity_details);


        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.signup_end_color));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");


        img_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_female.setImageAlpha(80);
                img_male.setImageAlpha(255);
                gender="male";


            }
        });

        img_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                img_male.setImageAlpha(80);
                img_female.setImageAlpha(255);
                gender="female";
            }
        });

        dob_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateAndTimePicker();
            }
        });

        weight_seekbar.setMax(250);
        weight_seekbar.setProgress(70);

        weight_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_weight.setText(progress + " kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        height_seekbar.setMax(250);
        height_seekbar.setProgress(165);

        height_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tv_height.setText(progress + " cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                height = height_seekbar.getProgress();
                weight = weight_seekbar.getProgress();
                dob = String.valueOf(date.getTimeInMillis());
                date_created = String.valueOf(Calendar.getInstance().getTimeInMillis());
//                Log.i(TAG, "DOB IS "+dob+"  GENDER is " + gender + "  CURRENT DATE IS " + date_created);
                signUp();
            }
        });



    }

    private void init(){

        img_female = (ImageView) findViewById(R.id.img_female);
        img_male = (ImageView) findViewById(R.id.img_male);

        img_female.setImageAlpha(80);
        gender="male";

        dob_rel = (RelativeLayout) findViewById(R.id.dob_rel);

        tv_dob = (TextView) findViewById(R.id.tv_dob);
        tv_weight = (TextView) findViewById(R.id.tv_weight);
        tv_height = (TextView) findViewById(R.id.tv_height);

        weight_seekbar = (SeekBar) findViewById(R.id.weight_seekbar);
        height_seekbar = (SeekBar) findViewById(R.id.height_seekbar);

        btn_continue = (Button) findViewById(R.id.btn_continue);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        preferences = getSharedPreferences(getString(R.string.SharedPref), Context.MODE_PRIVATE);
        userDataStore = new UserDataStore(DetailsActivity.this);

        customDialog = new CustomDialog();
        rq = Volley.newRequestQueue(this);


    }

    private void showDateAndTimePicker(){
        final Calendar currentDate = Calendar.getInstance();
        date = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(DetailsActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                date.set(year, monthOfYear, dayOfMonth);

                tv_dob.setText(new SimpleDateFormat("MMMM dd, yyyy").format(date.getTime()));
                Log.i(TAG,"Set date is "+date);



            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE));
        datePickerDialog.getDatePicker().setMaxDate(currentDate.getTimeInMillis());
        datePickerDialog.show();
    }


    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        String deviceOs = Build.VERSION.CODENAME;
        if (model.startsWith(manufacturer)) {
            return model;
        }
        return manufacturer + " " + model + " " + deviceOs;
    }


    private void signUp(){
        progressBar.setVisibility(View.VISIBLE);

        JSONObject paramJson = new JSONObject();

        try {
            paramJson.put("email", email);
            paramJson.put("password", password);
            paramJson.put("name",name);
            paramJson.put("gender",gender);
            paramJson.put("weight",weight);
            paramJson.put("height",height);
            paramJson.put("date_created",date_created);
            paramJson.put("dob",dob);
            paramJson.put("deviceType",getDeviceName());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = getString(R.string.BASE_URL)+"/signup";

        final JsonRequest request = new JsonRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    progressBar.setVisibility(View.GONE);

                    JSONObject data = response.getJSONObject("data");
                    JSONObject headers = response.getJSONObject("headers");

                    userDataStore.storeUserData(data);
                    JSONObject newOBJ = userDataStore.readUserData();

                    try{
                        if (newOBJ.getString("auth_token").equals("")){
                            Toast.makeText(getApplicationContext(),"Sign up failed. Try again",Toast.LENGTH_LONG).show();
                            finishAffinity();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);

                        }else{
                            finishAffinity();
                            Intent intent = new Intent(getApplicationContext(),ReadyActivity.class);
                            startActivity(intent);
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG,"Error is " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);

                JSONObject data = null;

                try {
                    String responseBody = new String(error.networkResponse.data, "utf-8");
                    data = new JSONObject(responseBody);
                    String message = data.optString("error");
                    Log.i(TAG,"SOME ERROR "+message);
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            message,
                            R.drawable.ic_error,
                            R.color.login_purple_light,
                            "Dismiss",
                            DetailsActivity.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }


        });

        rq.add(request);

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
}
