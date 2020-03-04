package com.replon.www.grace_thehealthapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

public class SignUpActivity extends AppCompatActivity {

    TextView tv_already_sign_in;

    Button btn_create;

    EditText et_name, et_email, et_password, et_confirm_password;

    CustomDialog customDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), SignUpActivity.this);

        setContentView(R.layout.activity_sign_up);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.signup_end_color));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        tv_already_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name,email,password,confirm_password;
                name = et_name.getText().toString().trim();
                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                confirm_password = et_confirm_password.getText().toString().trim();


                if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
                    customDialog.showMessageOneOption("Oh Snap!",
                            "Looks like there are incomplete fields",
                            R.drawable.ic_error,
                            R.color.violet,
                            "Dismiss",
                            SignUpActivity.this);
                }else if(!isValidEmailAddress(email)){
                    et_email.selectAll();
                    customDialog.showMessageOneOption("Oh Snap!",
                            "Please enter a correct email",
                            R.drawable.ic_error,
                            R.color.violet,
                            "Dismiss",
                            SignUpActivity.this);
                }else if(!password.equals(confirm_password)){
                    et_password.selectAll();
                    customDialog.showMessageOneOption("Oh Snap!",
                            "Passwords don't match.",
                            R.drawable.ic_error,
                            R.color.violet,
                            "Dismiss",
                            SignUpActivity.this);
                }else{
                    finishAffinity();
                    Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
                    intent.putExtra("name",name);
                    intent.putExtra("email",email);
                    intent.putExtra("password",password);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
                }

            }
        });
    }

    private void init(){

        tv_already_sign_in = (TextView) findViewById(R.id.tv_already_sign_in);

        btn_create = (Button) findViewById(R.id.btn_create);

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);

        customDialog = new CustomDialog();



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
    }
}
