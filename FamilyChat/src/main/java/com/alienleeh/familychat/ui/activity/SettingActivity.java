package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.manager.NIMClientManager;

/**
 * Created by AlienLeeH<br/>  on 2016/7/27..Hour:03<br/>
 * Email:alienleeh@foxmail.com<br/>
 * Description:设置界面
 */
public class SettingActivity extends BaseActivity{

    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private View view5;
    private View view7;
    private View view6;

    @Override
    public void initView() {
        String accid = NIMClientManager.getAccount();
        setContentView(R.layout.activity_setting);
        view1 = findViewById(R.id.msg_notify_setting_acti);
        view2 = findViewById(R.id.mode_tingtong_setting_acti);
        view3 = findViewById(R.id.not_disturb_setting_acti);
        view4 = findViewById(R.id.net_rec_setting_acti);
        view5 = findViewById(R.id.filter_notify_seting_acti);
        view6 = findViewById(R.id.clear_cachemsg_setting_acti);
        view7 = findViewById(R.id.about_me_setting_acti);
    }

    @Override
    public void initActionBar() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        ((TextView)view1.findViewById(R.id.func_name_switch_bt)).setText("消息提醒");
        ((TextView)view2.findViewById(R.id.func_name_switch_bt)).setText("听筒模式");
        ((TextView)view3.findViewById(R.id.func_name_switch_bt)).setText("免打扰");
        ((TextView)view4.findViewById(R.id.func_name_switch_bt)).setText("网络通话服务器录制音频");
        ((TextView)view5.findViewById(R.id.func_name_switch_bt)).setText("过滤通知");
        ((TextView)view6.findViewById(R.id.func_name_switch_bt)).setText("清空聊天记录");
        ((TextView)view7.findViewById(R.id.func_name_switch_bt)).setText("关于");

    }

    @Override
    protected void processClick(View view) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context,SettingActivity.class);
        context.startActivity(intent);
    }
}
