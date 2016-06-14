package com.alienleeh.familychat.ui.activity;

import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by AlienLeeH on 2016/6/28.
 */
public class EditInfoActivity extends BaseActivity{

    private TextView cancel_editinfo;
    private TextView commit_editinfo;
    private View nick;
    private View gender;
    private View birth;
    private View age;
    private View address;
    private View phone;
    private View email;
    private View shortcut;
    private List<View> allInclude= new ArrayList<>();
    private EditText nicket;
    private EditText shortcutet;
    private EditText emailet;
    private EditText phoneet;
    private EditText addresset;
    private EditText ageet;
    private EditText birthet;
    private EditText genderet;

    @Override
    public void initView() {
        setContentView(R.layout.activity_edit_userinfo);
        nick = findViewById(R.id.include_userifo_nick);
        gender = findViewById(R.id.include_userifo_gender);
        birth = findViewById(R.id.include_userifo_birth);
        age = findViewById(R.id.include_userifo_age);
        address = findViewById(R.id.include_userifo_address);
        phone = findViewById(R.id.include_userifo_phone);
        email = findViewById(R.id.include_userifo_email);
        shortcut = findViewById(R.id.include_userifo_shortcut);
        allInclude.add(nick);
        allInclude.add(gender);
        allInclude.add(birth);
        allInclude.add(age);
        allInclude.add(address);
        allInclude.add(phone);
        allInclude.add(email);
        allInclude.add(shortcut);
        initEditText();
    }

    private void initEditText() {
        nicket = (EditText) nick.findViewById(R.id.et_body_userinfo);
        genderet = (EditText) gender.findViewById(R.id.et_body_userinfo);
        birthet = (EditText) birth.findViewById(R.id.et_body_userinfo);
        ageet = (EditText) age.findViewById(R.id.et_body_userinfo);
        addresset = (EditText) address.findViewById(R.id.et_body_userinfo);
        phoneet = (EditText) phone.findViewById(R.id.et_body_userinfo);
        emailet = (EditText) email.findViewById(R.id.et_body_userinfo);
        shortcutet = (EditText) shortcut.findViewById(R.id.et_body_userinfo);
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
        for (int i = 0;i < allInclude.size();i++){
            String s = Constant.UserInfo.UPDATE_LIST[i];
            ((TextView)allInclude.get(i).findViewById(R.id.tv_head_userinfo)).setText(s);
            if (s.equals("性别") | s.equals("生日") | s.equals("所在地")){
                (allInclude.get(i).findViewById(R.id.iv_icon_userinfo)).setVisibility(View.VISIBLE);
            }
        }
        nicket.setInputType(InputType.TYPE_CLASS_TEXT);
        nicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 12){
                    nicket.setError("长度超过限制");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        genderet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())){
                    if (!(s.toString().equals("男") | s.toString().equals("女") | s.toString().equals("人妖"))){
                        genderet.setError("请输入性别“男”或“女”");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        phoneet.setInputType(InputType.TYPE_CLASS_PHONE);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        birthet.setInputType(InputType.TYPE_DATETIME_VARIATION_DATE);
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.cancel_editinfo:
                finish();
                break;
            case R.id.commit_editinfo:
                if (chekValid()){
                    attemptUpdate();
                }else {
                    ToastUtils.showToast(this,"未知错误");
                }
                break;
        }
    }
    //尝试上传修改资料，获取各edittext输入信息，非空即传
    private void attemptUpdate() {
        Map<UserInfoFieldEnum,Object> fields = new HashMap<>();
        if (!TextUtils.isEmpty(nicket.getText())){
            fields.put(UserInfoFieldEnum.Name,nicket.getText().toString());
        }
        if (!TextUtils.isEmpty(genderet.getText())){
            if (genderet.getText().toString().equals("男")){
                fields.put(UserInfoFieldEnum.GENDER, 1);
            }else {
                fields.put(UserInfoFieldEnum.GENDER,2);
            }
        }else {
            fields.put(UserInfoFieldEnum.GENDER,0);
        }
        if (!TextUtils.isEmpty(birthet.getText())){
            fields.put(UserInfoFieldEnum.BIRTHDAY,birthet.getText().toString());
        }
        if (!TextUtils.isEmpty(addresset.getText())){
            fields.put(UserInfoFieldEnum.EXTEND,addresset.getText().toString());
        }
        if (!TextUtils.isEmpty(phoneet.getText())){
            fields.put(UserInfoFieldEnum.MOBILE,phoneet.getText().toString());
        }
        if (!TextUtils.isEmpty(emailet.getText())){
            fields.put(UserInfoFieldEnum.EMAIL,emailet.getText().toString());
        }
        if (!TextUtils.isEmpty(shortcutet.getText())){
            fields.put(UserInfoFieldEnum.SIGNATURE,shortcutet.getText().toString());
        }
        NIMClient.getService(UserService.class).updateUserInfo(fields).setCallback(new RequestCallbackWrapper<Void>() {
            @Override
            public void onResult(int i, Void aVoid, Throwable throwable) {
                if (i == 200){
                    ToastUtils.showToast(EditInfoActivity.this,"更新成功");
                }else {
                    ToastUtils.showToast(EditInfoActivity.this,"修改失败。。"+aVoid+"..."+throwable);
                }
            }
        });

    }

    private boolean chekValid() {
        boolean valid = false;
        if (nicket.getText().toString().length()>12){
            nicket.setError("长度超过限制");
            nicket.requestFocus();
        }else if (!TextUtils.isEmpty(genderet.getText()) & !genderet.getText().toString().equals("男") & !genderet.getText().toString().equals("女")){
            genderet.setError("性别非法");
            genderet.requestFocus();
        }else if (!TextUtils.isEmpty(phoneet.getText()) & (phoneet.getText().length() > 11 | phoneet.getText().length() < 7)){
            phoneet.setError("请输入正确的电话号码");
            phoneet.requestFocus();
        }else if (!TextUtils.isEmpty(emailet.getText()) & !emailet.getText().toString().contains("@")){
            emailet.setError("请输入正确的email");
        }else {
            valid = true;
        }
        return valid;
    }

    private Map<UserInfoFieldEnum,Object> getInfoEnum() {
        Map<UserInfoFieldEnum,Object> fieldEnum= new HashMap<>();
        return fieldEnum;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
