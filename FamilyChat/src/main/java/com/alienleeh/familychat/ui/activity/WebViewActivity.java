package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.mylibrary.customUI.ProgressCircle;

/**
 * Created by AlienLeeH on 2016/7/26..Hour:08
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class WebViewActivity extends BaseActivity{

    private static final String URL = "url";
    private ImageView back;
    private WebView webView;
    private ImageView main;
    private ImageView forward;
    private ProgressCircle progressCircle;
    private String url;

    @Override
    public void initView() {
        url = getIntent().getStringExtra(URL);
        setContentView(R.layout.activity_one_web);
        webView = (WebView) findViewById(R.id.web_webview);
        back = (ImageView) findViewById(R.id.back_webview);
        main = (ImageView) findViewById(R.id.main_webview);
        forward = (ImageView) findViewById(R.id.forward_webview);
        progressCircle = (ProgressCircle) findViewById(R.id.prc_webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (!progressCircle.isShown()){
                    progressCircle.setVisibility(View.VISIBLE);
                }
                progressCircle.setProgress(newProgress);
                if (newProgress == 100){
                    ApplicationUtils.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressCircle.setVisibility(View.GONE);
                        }
                    },200);
                }
            }
        });
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initListener() {
        main.setOnClickListener(this);
        back.setOnClickListener(this);
        forward.setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.back_webview:
                webView.goBack();
                break;
            case R.id.main_webview:
                showProgress();
                break;
            case R.id.forward_webview:
                webView.goForward();
                break;

        }

    }

    private void showProgress() {
        progressCircle.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0;i<=100;i++){
                    progressCircle.setProgress(i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressCircle.setVisibility(View.GONE);
                    }
                });
            }
        }).start();

    }

    public static void start(Context context, String url) {
        Intent intent = new Intent(context,WebViewActivity.class);
        intent.putExtra(URL,url);
        context.startActivity(intent);
    }
}
