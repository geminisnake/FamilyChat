package com.alienleeh.familychat.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.alienleeh.familychat.manager.ActivitiesFinisher;

/**
 * Created by AlienLeeH on 2016/6/16.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initActionBar();
        initListener();
        initData();
        ActivitiesFinisher.addActivity(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivitiesFinisher.removeActivity(this);
    }

    public abstract void initView();

    public abstract void initActionBar();

    public abstract void initListener();

    public abstract void initData();

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void onClick(View view) {
        processClick(view);
    }

    protected abstract void processClick(View view);


}
