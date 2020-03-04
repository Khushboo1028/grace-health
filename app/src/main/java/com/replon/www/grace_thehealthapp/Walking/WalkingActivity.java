package com.replon.www.grace_thehealthapp.Walking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.ContentsDataDisplay;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.PostWebView;
import com.replon.www.grace_thehealthapp.Water.ShowAllDataWater;
import com.replon.www.grace_thehealthapp.Water.WaterPreferencesActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class WalkingActivity extends AppCompatActivity {

    public static final String TAG = "WalkingActivity";
    ImageView back;
    TextView steps_value_text,calories_value_text,tv_highest_steps,tv_date_highest,tv_lowest_steps,tv_date_lowest;

    TextView reference_link;
    DatabaseHelperWalking myDb;
    RelativeLayout show_all_data,preferences;


    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    private int dailyAverageSteps=0;
    private ArrayList<ContentsDataDisplay> dataList;
    private String str_date;

    double calories_burned = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), WalkingActivity.this);
        setContentView(R.layout.activity_walking);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkPurple));
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



            addValuesToBarEntry();
            addValuesToBarEntryLabels();

            Bardataset = new BarDataSet(BARENTRY, "Walking");

            BARDATA = new BarData(Bardataset);
            BARDATA.setBarWidth(0.6f);

            Bardataset.setColors(getColor(R.color.darkOrange));
            Bardataset.setValueTextColor(getColor(R.color.white));
            Bardataset.setValueTextSize(12f);
            chart.setDrawValueAboveBar(true);

            chart.setData(BARDATA);
            chart.getLegend().setEnabled(false);
            chart.setVisibleXRange(7f,7f);
            chart.setPinchZoom(false);
            chart.getDescription().setEnabled(false);

            chart.setFitBars(false);


            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setTextColor(getColor(R.color.white));
            xAxis.setTextSize(12f);
            IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(BarEntryLabels);
            xAxis.setGranularity(1f);
            xAxis.setValueFormatter(formatter);
            xAxis.setGridColor(getColor(R.color.white));
            xAxis.setLabelRotationAngle(-45f);
            xAxis.setDrawLabels(true);

            chart.getAxisRight().setEnabled(false);

            YAxis yAxis = chart.getAxisLeft();
            yAxis.setTextColor(getColor(R.color.white));
            yAxis.setTextSize(12f);
            yAxis.setDrawGridLines(false);
            yAxis.setAxisMinimum(0f);


            chart.animateY(1000);
            chart.invalidate();



//            tv_highest_steps.setText("12345");  // SET HIGHEST OF ALL STEPS HERE
//            tv_date_highest.setText("11 Mar, 2020");  //SET HIGHEST DATE HERE
//
//            tv_lowest_steps.setText("1234");  //SET LOWEST OF ALL STEPS HERE
//            tv_date_lowest.setText("10 Apr, 2019"); //SET LOWEST DATE HERE


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
                    Intent intent = new Intent(getApplicationContext(), WalkingPreferencesActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);
                }
            });

            show_all_data.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), ShowAllDataWalking.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_start_activity,R.anim.exit_start_activity);

                }
            });

    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        steps_value_text = (TextView) findViewById(R.id.steps_value_text);
        calories_value_text = (TextView) findViewById(R.id.calories_value_text);
        tv_highest_steps =(TextView) findViewById(R.id.tv_highest_steps);
        tv_date_highest = (TextView) findViewById(R.id.tv_date_highest);
        tv_lowest_steps = (TextView) findViewById(R.id.tv_lowest_steps);
        tv_date_lowest = (TextView) findViewById(R.id.tv_date_lowest);
        reference_link = (TextView) findViewById(R.id.reference_link_text);


        dataList = new ArrayList<>();
        chart = findViewById(R.id.barChart);

        show_all_data = (RelativeLayout) findViewById(R.id.show_data_rel);
        preferences = (RelativeLayout) findViewById(R.id.preferences_rel);

        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();

        myDb=new DatabaseHelperWalking(getApplicationContext());

        steps_value_text.setText(String.valueOf(dailyAverageSteps));

        String cals = String.format("%.2f",calories_burned);
        calories_value_text.setText(cals);



    }


    public void addValuesToBarEntry() {

        for (int i = 0; i < dataList.size(); i++) {

            BARENTRY.add(new BarEntry(i, dataList.get(i).getAmount()));

        }
//
//        BARENTRY.add(new BarEntry(1, 1));
//        BARENTRY.add(new BarEntry(2, 2));
//        BARENTRY.add(new BarEntry(3, 3));
//        BARENTRY.add(new BarEntry(4, 4));
//        BARENTRY.add(new BarEntry(5, 5));
//        BARENTRY.add(new BarEntry(6, 6));
//        BARENTRY.add(new BarEntry(7, 7));
//        BARENTRY.add(new BarEntry(8, 8));
//        BARENTRY.add(new BarEntry(9, 9));
//        BARENTRY.add(new BarEntry(10, 10));
//        BARENTRY.add(new BarEntry(11, 11));
    }

    public void addValuesToBarEntryLabels() {

        for (int i = 0; i < dataList.size(); i++) {

            BarEntryLabels.add(dataList.get(i).getDate());

        }

//        BarEntryLabels.add("January");
//        BarEntryLabels.add("February");
//        BarEntryLabels.add("March");
//        BarEntryLabels.add("April");
//        BarEntryLabels.add("May");
//        BarEntryLabels.add("June");
//        BarEntryLabels.add("July");
//        BarEntryLabels.add("August");
//        BarEntryLabels.add("September");
//        BarEntryLabels.add("October");
//        BarEntryLabels.add("November");
//        BarEntryLabels.add("December");
    }

    private void viewData() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String today = df.format(date);
        Log.i(TAG,"date is " +today);

        StringBuffer buffer=new StringBuffer();
        dataList.clear();


        Cursor res2 = myDb.getAllData();
        StringBuffer buffer2=new StringBuffer();
        if(res2.getCount()==0){
            Log.i(TAG,"There is no stored data");


        }else {

            while (res2.moveToNext()) {
                if (today.equals(res2.getString(0))) {
                    str_date = "Today";
                    calories_burned = res2.getInt(1) * 0.55;
                    calories_value_text.setText(String.valueOf(calories_burned));
                } else {
                    str_date = res2.getString(0);
                    SimpleDateFormat sfd_viewFormat = new SimpleDateFormat("MMMM d, yyyy");
                    try {
                        Date date1 = sfd_viewFormat.parse(str_date);
                        str_date = sfd_viewFormat.format(date1);
                        Log.i(TAG, str_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                dataList.add(new ContentsDataDisplay(
                        res2.getInt(1),
                        "Calories",
                        str_date));

                Log.i(TAG, buffer2.toString());


            }
        }

        Collections.reverse(dataList);


        int sum=0;
        int min=10000000;
        int max=0;
        int pos_high=0, pos_low=0;
        for(int i=0 ; i<dataList.size();i++){
            sum=sum+dataList.get(i).getAmount();
        }
        if(dataList.size()!=0){
            dailyAverageSteps=sum/dataList.size();
        }else{
            dailyAverageSteps=0;
        }

        steps_value_text.setText(String.valueOf(dailyAverageSteps));

        if(dataList.size()!=0) {
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getAmount() < min) {
                    min = dataList.get(i).getAmount();
                    pos_low=i;

                }

                if (dataList.get(i).getAmount() > max) {
                    max = dataList.get(i).getAmount();
                    pos_high=i;
                }
            }
            tv_highest_steps.setText(String.valueOf(max));
            tv_lowest_steps.setText(String.valueOf(min));
            tv_date_highest.setText(dataList.get(pos_high).getDate());
            tv_date_lowest.setText(dataList.get(pos_low).getDate());


        }else{
            tv_date_highest.setText("");
            tv_date_lowest.setText("");
            tv_highest_steps.setText("");
            tv_lowest_steps.setText("");

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
