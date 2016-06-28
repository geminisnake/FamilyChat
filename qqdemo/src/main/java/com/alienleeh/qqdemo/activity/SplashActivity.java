package com.alienleeh.qqdemo.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alienleeh.qqdemo.R;
import com.alienleeh.qqdemo.base.BaseActivity;
import com.alienleeh.qqdemo.global.Constant;

public class SplashActivity extends BaseActivity {

    public static final int DURATION_MILLIS = 3000;
    private RelativeLayout rl_splash;
    private boolean isEnter = false;
    private ImageView iv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        setContentView(R.layout.activity_splash);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f,1);
        alphaAnimation.setDuration(DURATION_MILLIS);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
        iv_splash = (ImageView) findViewById(R.id.iv_splash);
        iv_splash.setAnimation(alphaAnimation);
        alphaAnimation.start();
        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(DURATION_MILLIS);
                entryLogin();
            }
        }.start();

    }

    private synchronized void entryLogin() {
        if (isEnter){
            return;
        }
        isEnter = true;
        mHandler.sendEmptyMessage(Constant.MSG_SPLASH);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        entryLogin();
        return true;
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    protected void processClick(View v) {

    }
}
