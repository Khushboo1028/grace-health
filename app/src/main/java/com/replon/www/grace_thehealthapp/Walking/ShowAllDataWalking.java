package com.replon.www.grace_thehealthapp.Walking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

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

public class ShowAllDataWalking extends AppCompatActivity {

    public static final String TAG = "ShowAllDataWalking";
    ImageView back;

    private RecyclerView recyclerView;
    private DataDisplayAdapter mAdapter;
    private ArrayList<ContentsDataDisplay> dataList;
    private String str_date;
    private DatabaseHelperWalking myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), ShowAllDataWalking.this);
        setContentView(R.layout.activity_show_all_data_walking);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.darkPurple));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mAdapter = new DataDisplayAdapter(this,dataList);
        recyclerView.setAdapter(mAdapter);
        viewData();


    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        recyclerView = findViewById(R.id.walking_recycler_view);
        recyclerView.setHasFixedSize(true); //so it doesn't matter if element size increases or decreases

        dataList = new ArrayList<>();
        myDb=new DatabaseHelperWalking(getApplicationContext());

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
                        res2.getInt(1),
                        "Steps",
                        str_date));

                Log.i(TAG, buffer2.toString());


            }
            mAdapter.notifyDataSetChanged();
        }

        Collections.reverse(dataList);




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
