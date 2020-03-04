package com.replon.www.grace_thehealthapp.Water;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.PostWebView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Math.round;


public class WaterActivity extends AppCompatActivity {

    public static final String TAG = "WaterActivity";
    ImageView back;
    ProgressBar progressBar;
    ImageView add_water, minus_water;
    TextView water_progress, water_goal,glass_value_text;

    TextView reference_link;
    FrameLayout show_all_data, preferences;


    DatabaseHelperWater myDb;

    //Default values

    int target_glasses = 8;
    int glasses_drank = 0;
    int total_quantity_drank = 0;
    int glass_size =200;

    boolean data_with_date_found;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), WaterActivity.this);
        setContentView(R.layout.activity_water);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getColor(R.color.darkBlue));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        water_progress.setText(String.valueOf(glasses_drank));
        water_goal.setText("of "+String.valueOf(target_glasses)+" glasses");
        progressBar.setMax(target_glasses);
        progressBar.setProgress(glasses_drank,true);


        add_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glasses_drank+=1;
                progressBar.setProgress(glasses_drank,true);
                water_progress.setText(String.valueOf(glasses_drank));

                total_quantity_drank = total_quantity_drank + glass_size;

                Log.i(TAG,"Total quantity drank is" + total_quantity_drank);
                updateData(target_glasses,glasses_drank,glass_size,total_quantity_drank);
            }

        });
        minus_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                glasses_drank-=1;
                if (glasses_drank<0) {
                    glasses_drank = 0;
                }
                water_progress.setText(String.valueOf(glasses_drank));
                progressBar.setProgress(glasses_drank,true);

                total_quantity_drank = total_quantity_drank - glass_size;
                if(total_quantity_drank < 0){
                    total_quantity_drank = 0;
                }

                Log.i(TAG,"Total quantity drank is" + total_quantity_drank);


                updateData(target_glasses,glasses_drank,glass_size,total_quantity_drank);

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
                Intent intent = new Intent(getApplicationContext(), WaterPreferencesActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
            }
        });

        show_all_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShowAllDataWater.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);

            }
        });




        viewData();

    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        myDb=new DatabaseHelperWater(this);
        progressBar = findViewById(R.id.water_progress_bar);


        glass_value_text=(TextView)findViewById(R.id.glass_value_text);
        water_progress = (TextView) findViewById(R.id.water_progress);
        water_goal = (TextView) findViewById(R.id.water_goal);
        reference_link = (TextView) findViewById(R.id.reference_link_text);

        add_water = (ImageView) findViewById(R.id.image_add_water);
        minus_water = (ImageView) findViewById(R.id.image_minus_water);

        show_all_data = (FrameLayout) findViewById(R.id.show_all_data_frame);
        preferences = (FrameLayout) findViewById(R.id.preferences_frame);


    }


    private void updateData(int target_glasses, int glasses_drank,int glass_size,int total_quantity_drank) {

        if(!data_with_date_found){
            boolean isInserted= myDb.insertData(target_glasses,glasses_drank,glass_size,total_quantity_drank);

            if(isInserted){
                Log.i(TAG,"Water goal saved");
                data_with_date_found=true;
            }else{
                Log.i(TAG,"Unable to save water goal");

            }
        }else{
            boolean isUpdated= myDb.updateData(target_glasses,glasses_drank,glass_size,total_quantity_drank);

            if(isUpdated){
                Log.i(TAG,"Water goal updated");
            }else{
                Log.i(TAG,"Unable to update water goal");

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

                    glass_value_text.setText(res.getInt(3) + " ml");

                    buffer.append("ID: " +res.getString(0)+"\n" );
                    buffer.append("target_glasses: " +res.getInt(1)+"\n");
                    buffer.append("glasses_drank: "+res.getString(2)+"\n");
                    buffer.append("glass_size " +res.getInt(3)+"\n");
                    buffer.append("total_quantity_drank " +res.getInt(4)+"\n\n");

                    glasses_drank = res.getInt(2);
                    target_glasses = res.getInt(1);
                    total_quantity_drank=res.getInt(4);
                    glass_size = res.getInt(3);
                }
                Log.i(TAG,buffer.toString());




                water_progress.setText(String.valueOf(glasses_drank));
                progressBar.setProgress(glasses_drank,true);
                water_goal.setText("of "+String.valueOf(target_glasses)+" glasses");
                progressBar.setMax(target_glasses);



            }

            Cursor res2 = myDb.getAllData();
            if(res2.getCount()==0){
                Log.i(TAG,"There is no stored data");


            }else{


                while(res2.moveToNext()){

                    buffer.append("ID: " +res2.getString(0)+"\n" );
                    buffer.append("target_glasses: " +res2.getInt(1)+"\n");
                    buffer.append("glasses_drank: "+res2.getString(2)+"\n");
                    buffer.append("glass_size " +res2.getInt(3)+"\n");
                    buffer.append("total_quantity_drank " +res2.getInt(4)+"\n\n");

                    target_glasses=res2.getInt(1);
                    glass_size =res2.getInt(3);
                    total_quantity_drank = res2.getInt(4);


                }
                Log.i(TAG,"Glasses quantity " + glass_size);

                water_goal.setText("of "+String.valueOf(target_glasses)+" glasses");
                Log.i(TAG,"Goal in glass 1 is " + target_glasses);
                progressBar.setMax(target_glasses);

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
