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

public class NaturalDisasters extends AppCompatActivity {

    ImageView back, search;

    RecyclerView recyclerView;
    ArrayList<ContentsEmergencyData> dataList,naturalDisastersList;
    EmergencyDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), NaturalDisasters.this);
        setContentView(R.layout.activity_natural_disasters);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.naturalDisasterStartColor));
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
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(NaturalDisasters.this, sharedView, transitionName);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("dataList", dataList);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });

//        dataList.add(new ContentsEmergencyData("Artificial Respiration", "in First Aid", "LONG TEXT HERE", "artificial,respiration,throat,breathe,chest,nose,airway,breathing,blowing,mouth,resuscitation,air,mouth to mouth,inhale,exhale,revive,passage,oxygen"));
//        dataList.add(new ContentsEmergencyData("Cardiopulmonary resuscitation (CPR)", "in First Aid","LONG TEXT HERE", "resuscitation,cardiopulmonary,cpr,breathe,chest,mouth,breathing,compressions,cardiac,gasping,exhaustion,inhale,exhale,heart"));
//        dataList.add(new ContentsEmergencyData("Shock", "in First Aid","LONG TEXT HERE","shock,oxygen,spinal cord injury,aggressive behaviour,yawning,gasping,heart failure,shallow breathing,nausea,bleeding,septic,fear,severe bleeding,fast pulse,grey blue skin,restlessness,unresponsive"));

        dataList = this.getIntent().getExtras().getParcelableArrayList("dataList");

        for(ContentsEmergencyData s:dataList){
            if (s.getPlace().equals("3")){
                naturalDisastersList.add(s);
            }
        }

        adapter = new EmergencyDataAdapter(this, naturalDisastersList);
        recyclerView.setAdapter(adapter);
    }

    private void init(){
        back = (ImageView) findViewById(R.id.back);
        search = (ImageView) findViewById(R.id.search_img);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        dataList=new ArrayList<>();
        naturalDisastersList = new ArrayList<>();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
