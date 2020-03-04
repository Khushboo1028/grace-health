package com.replon.www.grace_thehealthapp.Utility;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.replon.www.grace_thehealthapp.R;
import com.victor.loading.rotate.RotateLoading;

public class PostWebView extends AppCompatActivity {

    private ImageView cancel, view_in_browser;
    private WebView webView;
    private String post_url;
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DefaultTextConfig defaultTextConfig = new DefaultTextConfig();
        defaultTextConfig.adjustFontScale(getResources().getConfiguration(), PostWebView.this);
        setContentView(R.layout.activity_post_web_view);

        cancel = (ImageView) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        view_in_browser = (ImageView) findViewById(R.id.view_in_browser);
        view_in_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post_url));
                finish();
                startActivity(intent);
            }
        });

        post_url = getIntent().getStringExtra("post_url");
        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoading) ;

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(post_url);

        webView.setWebViewClient(new WebViewClient(){
            @Override public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                rotateLoading.start();
//                mProgressBar.setVisibility(ProgressBar.VISIBLE);
                webView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                rotateLoading.stop();
//                mProgressBar.setVisibility(ProgressBar.GONE);
                webView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                rotateLoading.stop();
                webView.loadUrl("file:///android_asset/error.html");
            }
        });



    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
