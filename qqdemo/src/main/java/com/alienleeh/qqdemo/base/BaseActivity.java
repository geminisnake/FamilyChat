package com.alienleeh.qqdemo.base;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alienleeh.qqdemo.activity.LoginActivity;
import com.alienleeh.qqdemo.global.Constant;
import com.alienleeh.qqdemo.utils.ActivityUtils;

/**
 * Created by AlienLeeH on 2016/6/21.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Handler mHandler = new Handler(){
        private Context context;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Constant.MSG_SPLASH:
                    ActivityUtils.startActivity(BaseActivity.this,LoginActivity.class,true);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }
    public abstract void initView();
    public abstract void initListener();
    public abstract void initData();

    @Override
    public void onClick(View v) {
        processClick(v);
    }

    protected abstract void processClick(View v);
}
