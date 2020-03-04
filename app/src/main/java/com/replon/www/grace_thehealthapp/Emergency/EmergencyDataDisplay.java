package com.replon.www.grace_thehealthapp.Emergency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.replon.www.grace_thehealthapp.R;
import com.replon.www.grace_thehealthapp.Utility.DefaultTextConfig;
import com.replon.www.grace_thehealthapp.Utility.VideoPlayerActivity;

public class EmergencyDataDisplay extends AppCompatActivity {

    ImageView back,video_img;
    TextView tv_heading;
    RelativeLayout topRel;
    WebView webView;

    String heading,place,url,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), EmergencyDataDisplay.this);
        setContentView(R.layout.activity_emergency_data_display);

        init();
        heading = getIntent().getStringExtra("heading");
        place = getIntent().getStringExtra("place");
        description = getIntent().getStringExtra("description");
        url = getIntent().getStringExtra("url");
        setBackground();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_heading.setText(heading);

//        webView.setBackgroundColor(Color.parseColor("#F2F2F2"));
//        webView.loadDataWithBaseURL(null, description, "text/html", "utf-8", null);
        webView.loadUrl("file:///android_asset/"+description+".html");



        video_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VideoPlayerActivity.class);
                intent.putExtra("url",url);
                startActivity(intent);
            }
        });
    }


    private void init(){
        back = (ImageView) findViewById(R.id.back);
        video_img = (ImageView) findViewById(R.id.video_img);

        tv_heading = (TextView) findViewById(R.id.tv_heading);

        topRel = (RelativeLayout) findViewById(R.id.topRel);

        webView = (WebView) findViewById(R.id.webView);
        heading="";
        place="";
        url="";
        description="";


    }


    private void setBackground(){





        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            if(place.equalsIgnoreCase("1")){
                window.setStatusBarColor(getColor(R.color.firstAidStartColor));
                topRel.setBackground(getDrawable(R.drawable.first_aid_gradient));
            }else if (place.equalsIgnoreCase("2")){
                window.setStatusBarColor(getColor(R.color.safetyTipsStartColor));
                topRel.setBackground(getDrawable(R.drawable.safety_tips_gradient));
            }else if (place.equalsIgnoreCase("3")){
                window.setStatusBarColor(getColor(R.color.naturalDisasterStartColor));
                topRel.setBackground(getDrawable(R.drawable.natural_disasters_gradient));
            }

            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.enter_finish_activity,R.anim.exit_finish_activity);
    }
}
