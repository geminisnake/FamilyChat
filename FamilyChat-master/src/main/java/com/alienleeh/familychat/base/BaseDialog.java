package com.alienleeh.familychat.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public abstract class BaseDialog extends android.app.AlertDialog implements View.OnClickListener{

    protected BaseDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

    public abstract void processClick(View v);
}
