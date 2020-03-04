package com.replon.www.grace_thehealthapp.Calories;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CaloriesPreferencesActivity extends AppCompatActivity {

    public static final String TAG = "CaloriesPreferencesActivity";
    ImageView back,save;
    EditText et_calories_goal;
    DatabaseHelperCalories myDb;
    Boolean data_with_date_found;
    int target_calories=2000,calories_consumed=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calories_preferences);

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

        et_calories_goal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                save.setVisibility(View.VISIBLE);

                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!data_with_date_found){
                    boolean isInserted= myDb.insertData(Integer.parseInt(et_calories_goal.getText().toString().trim()),calories_consumed,"");

                    if(isInserted){
                        Log.i(TAG,"Meal Logged");
                        data_with_date_found=true;
                        finish();
                    }else{
                        Log.i(TAG,"Unable to log meal");

                    }
                }else{
                    boolean isUpdated= myDb.updateData(Integer.parseInt(et_calories_goal.getText().toString().trim()),calories_consumed,"");

                    if(isUpdated){
                        Log.i(TAG,"Meal Logged");
                        finish();
                    }else{
                        Log.i(TAG,"Unable to log meal");

                    }
                }
            }
        });

    }

    private void init() {

        back = (ImageView) findViewById(R.id.back);
        save = (ImageView) findViewById(R.id.save);
        save.setVisibility(View.GONE);

        myDb=new DatabaseHelperCalories(getApplicationContext());
        et_calories_goal = (EditText) findViewById(R.id.et_calories_goal);
        et_calories_goal.setText(String.valueOf(target_calories));

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


                    target_calories=res.getInt(1);
                    calories_consumed=res.getInt(2);
                    et_calories_goal.setText(String.valueOf(target_calories));

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
                    et_calories_goal.setText(String.valueOf(target_calories));

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
