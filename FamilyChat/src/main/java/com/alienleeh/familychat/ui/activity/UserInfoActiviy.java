package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.cache.UserInfoCache;
import com.alienleeh.familychat.customUI.MyActionBar;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.ui.dialog.LoadingDialog;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.constant.GenderEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

/**
 * Created by AlienLeeH on 2016/7/7.
 */
public class UserInfoActiviy extends BaseActivity{

    private TextView infoact_accid;
    private TextView infoact_nickname;
    private TextView infoact_displayname;
    private TextView infoact_birth;
    private TextView infoact_address;
    private TextView infoact_mb_num;
    private TextView infoact_signed;
    private TextView infoact_email;
    private Button infoact_bt1;
    private Button infoact_bt2;
    private ImageView infoact_gender;
    private ImageView infoact_avatar;
    private String accid;
    private LoadingDialog dialog;

    @Override
    public void initView() {
        setContentView(R.layout.activity_display_userinfo);
        infoact_accid = (TextView) findViewById(R.id.infoact_accid);
        infoact_nickname = (TextView) findViewById(R.id.infoact_nickname);
        infoact_displayname = (TextView) findViewById(R.id.infoact_displayname);
        infoact_birth = (TextView) findViewById(R.id.infoact_birth);
        infoact_address = (TextView) findViewById(R.id.infoact_address);
        infoact_mb_num = (TextView) findViewById(R.id.infoact_mb_num);
        infoact_email = (TextView) findViewById(R.id.infoact_email);
        infoact_signed = (TextView) findViewById(R.id.infoact_signed);
        infoact_bt1 = (Button) findViewById(R.id.infoact_bt1);
        infoact_bt2 = (Button) findViewById(R.id.infoact_bt2);
        infoact_avatar = (ImageView) findViewById(R.id.infoact_avatar);
        infoact_gender = (ImageView) findViewById(R.id.infoact_gender);
        dialog = new LoadingDialog(this);
        dialog.showDialog("加载中",false,false);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(new MyActionBar(this, "个人资料"));
        }
    }

    @Override
    public void initListener() {
        infoact_bt1.setOnClickListener(this);
        infoact_bt2.setOnClickListener(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        accid = intent.getStringExtra("userAcc");
        NimUserInfo userInfo = UserInfoCache.getInstance().getUserInfo(accid);
        if (userInfo != null){
            displayAllData(userInfo);
            dialog.disappear();
        }else {
            UserInfoCache.getInstance().getUserInfoFromRomate(accid, new RequestCallbackWrapper<NimUserInfo>() {
                @Override
                public void onResult(int i, final NimUserInfo asynuserInfo, Throwable throwable) {
                    if (asynuserInfo != null)
                    new Handler(getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            displayAllData(asynuserInfo);
                            dialog.disappear();
                        }
                    });
                }
            });
        }
    }

    private void displayAllData(NimUserInfo userInfo) {
        ImageLoaderHelper.displayAvatar(userInfo,infoact_avatar);
        infoact_gender.setVisibility(View.VISIBLE);
        if (userInfo.getGenderEnum() == GenderEnum.FEMALE){
            infoact_gender.setImageResource(R.drawable.female);
        }else if (userInfo.getGenderEnum() == GenderEnum.MALE){
            infoact_gender.setImageResource(R.drawable.male);
        }else {
            infoact_gender.setVisibility(View.GONE);
        }
        infoact_displayname.setText(TextUtils.isEmpty(userInfo.getName())?
                accid : userInfo.getName());
        infoact_accid.setText("亲聊账号:"+accid);
        infoact_birth.setText(TextUtils.isEmpty(userInfo.getBirthday())?
                "对方还未填写" : userInfo.getBirthday());
        infoact_mb_num.setText(TextUtils.isEmpty(userInfo.getMobile())?
                "对方还未填写" : userInfo.getMobile());
        infoact_address.setText(TextUtils.isEmpty(userInfo.getExtension())?
                "对方还未填写" : userInfo.getExtension());
        infoact_email.setText(TextUtils.isEmpty(userInfo.getEmail())?
                "对方还未填写" : userInfo.getEmail());
        infoact_signed.setText(TextUtils.isEmpty(userInfo.getSignature())?
                "对方还未填写" : userInfo.getSignature());
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.infoact_bt1:
                ConversationActivity.start(this,accid);
                break;
            case R.id.infoact_bt2:
                ToastUtils.showToast(this,"视频功能尚未开通");
                break;
        }
    }

    public static void start(Context context, String account) {
        Intent it = new Intent(context,UserInfoActiviy.class);
        it.putExtra("userAcc",account);
        context.startActivity(it);
    }
}
