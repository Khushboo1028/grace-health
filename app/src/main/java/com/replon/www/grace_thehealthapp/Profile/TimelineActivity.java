package com.replon.www.grace_thehealthapp.Profile;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.util.ArrayList;

public class TimelineActivity extends AppCompatActivity {

    ImageView back;
    RecyclerView recyclerView;
    TimelineAdapter adapter;
    ArrayList<ContentsTimeline> timelineList;

    Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), TimelineActivity.this);
        setContentView(R.layout.activity_timeline);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        timelineList.add(new ContentsTimeline("Dec 2019","","",0));
        timelineList.add(new ContentsTimeline("","Mon, Dec 12", "Sick", 1));
        timelineList.add(new ContentsTimeline("","Sat, Dec 9", "Hospital", 2));
        timelineList.add(new ContentsTimeline("","Tue, Dec 1", "Check Up", 1));

        timelineList.add(new ContentsTimeline("Nov 2019","","",0));
        timelineList.add(new ContentsTimeline("","Wed, Nov 29", "Sick", 1));
        timelineList.add(new ContentsTimeline("","Fri, Nov 8", "Hospital", 2));
        timelineList.add(new ContentsTimeline("","Mon, Nov 2", "Check Up", 1));
        timelineList.add(new ContentsTimeline("Start","","",0));

        adapter = new TimelineAdapter(TimelineActivity.this, timelineList);
        recyclerView.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddRecordActivity.class);
                startActivity(intent);
            }
        });


    }


    private void init(){
        back = (ImageView)findViewById(R.id.back);

        btn_add = (Button) findViewById(R.id.btn_add);

        timelineList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
