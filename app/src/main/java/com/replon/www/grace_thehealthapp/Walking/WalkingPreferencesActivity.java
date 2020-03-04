package com.replon.www.grace_thehealthapp.Walking;

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

import com.replon.www.grace_thehealthapp.Calories.LogMealActivity;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.CustomDialog;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WalkingPreferencesActivity extends AppCompatActivity {


    public static final String TAG = "WalkingPreferencesActivity";
    ImageView back,save;

    EditText et_walking_goal;
    DatabaseHelperWalking myDb;
    int target_Steps=10000;
    int storedSteps=0;
    Boolean data_with_date_found;
    CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), WalkingPreferencesActivity.this);

        setContentView(R.layout.activity_walking_preferences);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkPurple));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        viewWalkingData();
        et_walking_goal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                save.setVisibility(View.VISIBLE);

                return false;
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_walking_goal.getText().toString().trim().isEmpty()) {
                    if (!data_with_date_found) {
                        boolean isInserted = myDb.insertData(storedSteps,Integer.parseInt(et_walking_goal.getText().toString().trim()));

                        if (isInserted) {
                            Log.i(TAG, "Walking Goal Updated");
                            finish();
                        } else {
                            Log.i(TAG, "Unable to update goal");

                        }
                    } else {
                        boolean isUpdated = myDb.updateData(storedSteps,Integer.parseInt(et_walking_goal.getText().toString().trim()));

                        if (isUpdated) {
                            Log.i(TAG, "Walking Goal Updated");
                            finish();
                        } else {
                            Log.i(TAG, "Unable to update goal");

                        }
                    }
                } else {
                    customDialog.showMessageOneOption(
                            "Oh Snap!",
                            "Gaol cannot be empty",
                            R.drawable.ic_error,
                            R.color.darkPurple,
                            "Dismiss",
                            WalkingPreferencesActivity.this);
                }
            }
        });
    }

    private void init() {
        back = (ImageView) findViewById(R.id.back);
        save = (ImageView) findViewById(R.id.save);
        save.setVisibility(View.GONE);

        et_walking_goal = (EditText) findViewById(R.id.et_walking_goal);
        myDb=new DatabaseHelperWalking(getApplicationContext());
        customDialog=new CustomDialog();

    }

    public void viewWalkingData() {

        Log.i(TAG,"ViewDate running");
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(date);

        StringBuffer buffer=new StringBuffer();
        if(myDb.getDataWithDate(formattedDate)!=null) {

            Cursor res = myDb.getDataWithDate(formattedDate);
            if (res.getCount() == 0) {
                Log.i(TAG, "There is no stored data");
                data_with_date_found = false;

                storedSteps=0;


            }
            else {
                data_with_date_found = true;
                while (res.moveToNext()) {


                    buffer.append("ID: " + res.getString(0) + "\n");
                    buffer.append("steps: " + res.getInt(1) + "\n");
                    buffer.append("target_steps: " + res.getInt(2) + "\n");

                    storedSteps = res.getInt(1);
                    target_Steps = res.getInt(2);

                }
                Log.i(TAG,"\n"+buffer.toString());

            }

            Cursor res2 = myDb.getAllData();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");


            }else{

                while(res2.moveToNext()){

                    target_Steps=res2.getInt(2);
                    et_walking_goal.setText(String.valueOf(target_Steps));

                }


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
