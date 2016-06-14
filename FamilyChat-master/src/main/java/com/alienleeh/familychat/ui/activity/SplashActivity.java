package com.alienleeh.familychat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.utils.ActivityUtils;
import com.netease.nimlib.sdk.NIMClient;

public class SplashActivity extends BaseActivity {

    public static final int DURATION_MILLIS = 3000;
    private RelativeLayout rl_splash = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_splash);
        rl_splash = (RelativeLayout) findViewById(R.id.rl_splash);
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f,1);
        alphaAnimation.setDuration(DURATION_MILLIS);
        alphaAnimation.setFillAfter(true);

        rl_splash.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                switch (NIMClient.getStatus()){
                    case LOGINED:
                        ActivityUtils.startActivity(SplashActivity.this,MainActivity.class);
                        break;
                    default:
                        Intent loginIt = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(loginIt);
                        break;
                }
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void processClick(View view) {

    }
}
