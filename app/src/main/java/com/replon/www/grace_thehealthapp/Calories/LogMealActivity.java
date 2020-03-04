package com.replon.www.grace_thehealthapp.Calories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.SocialMedia.CommunityFeedActivity;
import com.replon.www.grace_thehealthapp.Utility.ContentsDataDisplay;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DataDisplayAdapter;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class LogMealActivity extends AppCompatActivity {

    public static final String TAG = "LogMealActivity";
    ImageView back;

    Button btn_save;

    private EditText et_food_name,et_food_calories;
    DatabaseHelperCalories myDb;
    Boolean data_with_date_found;
    int target_calories=2000,calories_consumed=0;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), LogMealActivity.this);

        setContentView(R.layout.activity_log_meal);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkYellow));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();
        viewData();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(et_food_calories.getText().toString().trim().isEmpty() || et_food_name.getText().toString().isEmpty()){
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "One or more Fields is Empty",
                            R.drawable.ic_error,
                            R.color.darkYellow,
                            "Dismiss",
                            LogMealActivity.this);
                }else{
                    calories_consumed=Integer.parseInt(et_food_calories.getText().toString().trim()) + calories_consumed;
                    saveData(target_calories,calories_consumed,et_food_name.getText().toString().trim());
                }

            }
        });


    }


    private void init(){

        back = (ImageView)findViewById(R.id.back);
        btn_save = (Button) findViewById(R.id.btn_save);

        myDb = new DatabaseHelperCalories(getApplicationContext());
        customDialog = new CustomDialog();
        et_food_calories = (EditText) findViewById(R.id.et_food_calories);
        et_food_name = (EditText) findViewById(R.id.et_food_name);




    }

    private void saveData(int target_calories, int calories_consumed,String food_name){
        if(!data_with_date_found){
            boolean isInserted= myDb.insertData(target_calories,calories_consumed,food_name);

            if(isInserted){
                Log.i(TAG,"Meal Logged");
                data_with_date_found=true;
                finish();
            }else{
                Log.i(TAG,"Unable to log meal");

            }
        }else{
            boolean isUpdated= myDb.updateData(target_calories,calories_consumed,food_name);

            if(isUpdated){
                Log.i(TAG,"Meal Logged");
                finish();
            }else{
                Log.i(TAG,"Unable to log meal");

            }
        }
    }

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
}
