package com.replon.www.grace_thehealthapp.Calories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.ContentsDataDisplay;
import com.replon.www.grace_thehealthapp.Utility.DataDisplayAdapter;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class ShowAllDataCalories extends AppCompatActivity {

    public static final String TAG = "ShowAllDataCalories";
    ImageView back;

    private RecyclerView recyclerView;
    private DataDisplayAdapter mAdapter;
    private ArrayList<ContentsDataDisplay> dataList;

    BarChart chart ;
    ArrayList<BarEntry> BARENTRY ;
    ArrayList<String> BarEntryLabels ;
    BarDataSet Bardataset ;
    BarData BARDATA ;

    DatabaseHelperCalories myDb;
    private String str_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), ShowAllDataCalories.this);
        setContentView(R.layout.activity_show_all_data_calories);

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

        addValuesToBarEntry();
        addValuesToBarEntryLabels();

        Bardataset = new BarDataSet(BARENTRY, "Calories");

        BARDATA = new BarData(Bardataset);
        BARDATA.setBarWidth(0.6f);

        Bardataset.setColors(getColor(R.color.darkPurple));
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




    }

    private void init(){

        back = (ImageView)findViewById(R.id.back);

        chart = findViewById(R.id.barChart);

        recyclerView = findViewById(R.id.calories_recycler_view);
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases

        dataList = new ArrayList<>();
        BARENTRY = new ArrayList<>();
        BarEntryLabels = new ArrayList<>();

        myDb = new DatabaseHelperCalories(getApplicationContext());
        mAdapter = new DataDisplayAdapter(this,dataList);
        recyclerView.setAdapter(mAdapter);


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
                            res2.getInt(2),
                            "Calories",
                            str_date));

                    Log.i(TAG, buffer2.toString());

                }
            }

        Collections.reverse(dataList);
        mAdapter.notifyDataSetChanged();


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
