package com.alienleeh.familychat.ui.activity;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.UserInfoEditAdapter;
import com.alienleeh.familychat.base.BaseActivity;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class EditInfoActivity extends BaseActivity{

    private TextView cancel_editinfo;
    private TextView commit_editinfo;
    private ListView lv_head_editinfo;
    private ListView lv_mid_editinfo;
    private ListView lv_end_editinfo;

    @Override
    public void initView() {
        setContentView(R.layout.activity_edit_userinfo);

        lv_head_editinfo = (ListView) findViewById(R.id.lv_head_editinfo);
        lv_mid_editinfo = (ListView) findViewById(R.id.lv_mid_editinfo);
        lv_end_editinfo = (ListView) findViewById(R.id.lv_end_editinfo);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_editinfo);
            cancel_editinfo = (TextView) findViewById(R.id.cancel_editinfo);
            commit_editinfo = (TextView) findViewById(R.id.commit_editinfo);
        }
    }

    @Override
    public void initListener() {
        cancel_editinfo.setOnClickListener(this);
        commit_editinfo.setOnClickListener(this);
    }

    @Override
    public void initData() {
        UserInfoEditAdapter adapter1 = new UserInfoEditAdapter(this, Constant.UserInfo.HEAD);
        UserInfoEditAdapter adapter2 = new UserInfoEditAdapter(this, Constant.UserInfo.MIDDLE);
        UserInfoEditAdapter adapter3 = new UserInfoEditAdapter(this, Constant.UserInfo.sign);
        lv_head_editinfo.setAdapter(adapter1);
        lv_mid_editinfo.setAdapter(adapter2);
        lv_end_editinfo.setAdapter(adapter3);
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.cancel_editinfo:
                finish();
                break;
            case R.id.commit_editinfo:

                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
