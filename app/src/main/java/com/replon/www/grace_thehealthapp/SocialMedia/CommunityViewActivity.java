package com.replon.www.grace_thehealthapp.SocialMedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.util.ArrayUtils;
import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityViewActivity extends AppCompatActivity {

    ImageView back;

    RecyclerView recyclerView;

    CommunityViewAdapter adapter;

    ArrayList<String> community_list;
    ArrayList<String> community_colors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), CommunityViewActivity.this);

        setContentView(R.layout.activity_community_view);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] comm = getResources().getStringArray(R.array.communities_list);
        String[] colors = getResources().getStringArray(R.array.community_colors);

        for (int i=1;i<comm.length;i++){
            community_list.add(comm[i]);
            community_colors.add(colors[i-1]);
        }

        adapter = new CommunityViewAdapter(this,community_list,community_colors);
        recyclerView.setAdapter(adapter);



    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);

        recyclerView = (RecyclerView) findViewById(R.id.community_recycler_view);

        community_list = new ArrayList<>();
        community_colors = new ArrayList<>();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
