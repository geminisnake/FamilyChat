package com.alienleeh.familychat.ui.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.adapter.GridViewAdapter;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.helper.UserUpdateHelper;
import com.alienleeh.familychat.ui.dialog.EasyDialog;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import com.alienleeh.mylibrary.customUI.CircleImageView;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class SetAvatarActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private GridView gv1_avatar_acti;
    private CircleImageView avataractivity_set_default;
    private LinearLayout actionbar_setavatar_iconback;
    private ImageView actionbar_setavatar_icondone;
    private int currentPosition = -1;
    private GridViewAdapter adapter;
    private Button bt_set_avatar_reset;
    private String defaultPath;
    private Button bt_set_avatar_custom;


    @Override
    public void initView() {
        setContentView(R.layout.activity_setup_avatar);
        gv1_avatar_acti = (GridView) findViewById(R.id.gv1_avatar_acti);
        avataractivity_set_default = (CircleImageView) findViewById(R.id.avataractivity_set_default);
        bt_set_avatar_reset = (Button) findViewById(R.id.bt_set_avatar_reset);
        bt_set_avatar_custom = (Button) findViewById(R.id.bt_set_avatar_custom);
        setAnimation();
    }

    private void setAnimation() {
        ImageView jiantou_animation_set_avatar = (ImageView) findViewById(R.id.jiantou_animation_set_avatar);
        TranslateAnimation animation = new TranslateAnimation(0,0,0,45);
        animation.setDuration(1200);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        Interpolator interpolator = new AccelerateInterpolator(1.2f);
        animation.setInterpolator(interpolator);
        jiantou_animation_set_avatar.startAnimation(animation);
    }

    @Override
    public void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.actionbar_setavatar_acti);
            actionbar_setavatar_iconback = (LinearLayout) findViewById(R.id.actionbar_setavatar_iconback);
            actionbar_setavatar_icondone = (ImageView) findViewById(R.id.actionbar_setavatar_icondone);
        }
    }

    @Override
    public void initListener() {
        gv1_avatar_acti.setOnItemClickListener(this);
        bt_set_avatar_reset.setOnClickListener(this);
        bt_set_avatar_custom.setOnClickListener(this);
        actionbar_setavatar_iconback.setOnClickListener(this);
        actionbar_setavatar_icondone.setOnClickListener(this);
    }

    @Override
    public void initData() {
        adapter = new GridViewAdapter(this);
        gv1_avatar_acti.setAdapter(adapter);
        parseIntent();
        ImageLoaderHelper.displayAvatarList(defaultPath,avataractivity_set_default);
    }

    private void parseIntent() {
        Intent intent = getIntent();
        defaultPath = intent.hasExtra("extra1")?
                intent.getStringExtra("extra1") : "defaultavatar.png";
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.avataractivity_set_default:

                break;
            case R.id.actionbar_setavatar_iconback:
                finish();
                break;
            case R.id.actionbar_setavatar_icondone:

                updateUserAvatarPath();
                break;
            case R.id.bt_set_avatar_reset:
                if (currentPosition != -1){
                    currentPosition = -1;
                }
                ImageLoaderHelper.displayAvatarList(defaultPath,avataractivity_set_default);
                break;

        }
    }

    private void updateUserAvatarPath() {
        if (currentPosition == -1){
            ToastUtils.showToast(this,"头像并没有改变！currentP:"+currentPosition);
            return;
        }
        UserUpdateHelper.update(UserInfoFieldEnum.AVATAR,adapter.getItem(currentPosition),avatarCallback);
    }

    private RequestCallbackWrapper<Void> avatarCallback = new RequestCallbackWrapper<Void>() {
        @Override
        public void onResult(int i, Void aVoid, Throwable throwable) {
            int[] dialogRes = {R.layout.dialog_easy1,R.id.text_easy_dialog,R.id.bt_easy_dialog};
            if (i == 200){
                EasyDialog.showDialog(SetAvatarActivity.this,dialogRes,"头像设置成功.",false);
            }
            else {
                EasyDialog.showDialog(SetAvatarActivity.this,dialogRes,"头像设置失败！:",false);
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPosition = position;
        ImageLoaderHelper.displayAvatarList(adapter.getItem(position),avataractivity_set_default);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
