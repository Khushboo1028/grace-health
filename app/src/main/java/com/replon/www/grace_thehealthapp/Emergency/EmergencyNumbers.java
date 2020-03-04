package com.replon.www.grace_thehealthapp.Emergency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;

import java.util.ArrayList;

public class EmergencyNumbers extends AppCompatActivity {

    ImageView back, search;
    RecyclerView recyclerView;
    EmergencyNumbersAdapter adapter;
    ArrayList<ContentsEmergencyData> numbersList,dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), EmergencyNumbers.this);
        setContentView(R.layout.activity_emergency_numbers);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#DD2476"));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                View sharedView = search;
                String transitionName = "search";
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(EmergencyNumbers.this, sharedView, transitionName);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });

        dataList = this.getIntent().getExtras().getParcelableArrayList("dataList");

        for(ContentsEmergencyData s:dataList){
            if (s.getPlace().equals("0")){
                numbersList.add(s);
            }
        }

        adapter = new EmergencyNumbersAdapter(this,numbersList);
        recyclerView.setAdapter(adapter);

    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search_img);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        numbersList = new ArrayList<>();
        dataList = new ArrayList<>();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
