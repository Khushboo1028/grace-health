package com.replon.www.grace_thehealthapp.Utility;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.replon.www.grace_thehealthapp.R;

import static android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE;

public class VideoPlayerActivity extends AppCompatActivity {

    VideoView videoView;
    MediaController mediaController;
    ProgressBar progressBar;

    String url;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        //changing statusbar color
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getColor(R.color.black));
            window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        }

        init();

        url = getIntent().getStringExtra("url");


        mediaController.setAnchorView(videoView);

//        uri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/payment-test-9d386.appspot.com/o/video.mp4?alt=media&token=6cef629f-d99b-441a-8e73-75d996488578");
        uri = Uri.parse(url);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);
        videoView.requestFocus();





        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {

                if(MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START == what){
                    progressBar.setVisibility(View.GONE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_START == what) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                if (MediaPlayer.MEDIA_INFO_BUFFERING_END == what) {
                    progressBar.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });




        videoView.start();
    }

    private void init(){
        videoView = (VideoView) findViewById(R.id.videoView);

        mediaController = new MediaController(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }


    public String getScreenOrientation(Context context){
        final int screenOrientation = ((WindowManager)  context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (screenOrientation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                return "LANDSCAPE";
            default:
                return "PORTRAIT";

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
