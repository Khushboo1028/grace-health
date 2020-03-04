package com.replon.www.grace_thehealthapp.Calories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.PostWebView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CaloriesActivity extends AppCompatActivity {

    public static final String TAG = "CaloriesActivity";
    ImageView back;

    TextView tv_calories,tv_calories_goal;

    TextView reference_link;

    Button btn_log_meal;

    RelativeLayout show_all_data,preferences;
    DatabaseHelperCalories myDb;
    Boolean data_with_date_found;

int calories_consumed=0,target_calories=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), CaloriesActivity.this);
        setContentView(R.layout.activity_calories);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkYellow));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        reference_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostWebView.class);
                intent.putExtra("post_url",getString(R.string.mayo_clinic_link));
                startActivity(intent);
            }
        });

        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaloriesPreferencesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

        show_all_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowAllDataCalories.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);

            }
        });

        btn_log_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogMealActivity.class);
                startActivity(intent);
            }
        });


    }

    private void init(){

        back = (ImageView)findViewById(R.id.back);

        tv_calories = (TextView) findViewById(R.id.tv_calories);
        tv_calories_goal = (TextView) findViewById(R.id.calories_goal);

        reference_link = (TextView) findViewById(R.id.reference_link_text);

        show_all_data = (RelativeLayout) findViewById(R.id.show_data_rel);
        preferences = (RelativeLayout) findViewById(R.id.preferences_rel);

        btn_log_meal = (Button) findViewById(R.id.btn_log_meal);

        myDb=new DatabaseHelperCalories(getApplicationContext());
        tv_calories.setText(String.valueOf(calories_consumed));
        tv_calories_goal.setText("Of " + String.valueOf(target_calories) + " Calories");    }

    private void viewData() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);
        Log.i(TAG,"date is " +formattedDate);

        StringBuffer buffer=new StringBuffer();
        if(myDb.getDataWithDate(formattedDate)!=null){

            Cursor res = myDb.getDataWithDate(formattedDate);
            if(res.getCount()==0){
                Log.i(TAG,"There is no stored data");
                data_with_date_found=false;

            }else{
                data_with_date_found=true;
                while(res.moveToNext()){

                    buffer.append("ID:(Date) " +res.getString(0)+"\n" );
                    buffer.append("target_calories: " +res.getInt(1)+"\n");
                    buffer.append("calories_consumed: "+res.getString(2)+"\n\n");

                    calories_consumed=res.getInt(2);
                    target_calories=res.getInt(1);

                    tv_calories.setText(String.valueOf(calories_consumed));
                    tv_calories_goal.setText(String.valueOf(target_calories));


                }
                Log.i(TAG,buffer.toString());


            }

            Cursor res2 = myDb.getAllData();
            StringBuffer buffer2=new StringBuffer();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");


            }else{

                while(res2.moveToNext()){
                    buffer2.append("ID:(Date) " +res2.getString(0)+"\n" );
                    buffer2.append("target_calories: " +res2.getInt(1)+"\n");
                    buffer2.append("calories_consumed: "+res2.getString(2)+"\n\n");

                    target_calories=res2.getInt(1);
                    tv_calories_goal.setText("Of " + String.valueOf(target_calories) + " Calories");

                }

                Log.i(TAG,buffer2.toString());

            }
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewData();
    }
}
