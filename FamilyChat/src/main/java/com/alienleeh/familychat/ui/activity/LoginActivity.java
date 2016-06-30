package com.alienleeh.familychat.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alienleeh.familychat.R;
import com.alienleeh.familychat.base.BaseActivity;
import com.alienleeh.familychat.bean.LoginSetting;
import com.alienleeh.familychat.manager.ActivitiesFinisher;
import com.alienleeh.familychat.manager.DataCacheManager;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.ui.dialog.LoadingDialog;
import com.alienleeh.familychat.utils.ActivityUtils;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.NetWork.ContactHttpDao;
import com.alienleeh.familychat.utils.SharePreferenceUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * 登录界面
 * Created by AlienLeeH on 2016/6/17.
 */
public class LoginActivity extends BaseActivity{

    private AutoCompleteTextView act_account;
    private EditText et_password;
    private View login_form;
    private View login_progress;
    private Button bt_login;
    private Button bt_register;
    private CheckBox cb_login_auto;
    private CheckBox cb_login_remember;
    private Intent receivedIt;
    private AbortableFuture<LoginInfo> loginRequest;
    private LinearLayout ll_trans_register;
    private LinearLayout ll_trans_login;

    private static final float Distance = ApplicationUtils.getWidth() + 20;
    private ActionBar actionBar;
    private Button bt_confirm_register;
    private Button bt_back_register;
    private EditText et_nickname;
    private EditText et_confirm_password;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.custom_menu1,menu);
        menu.findItem(R.id.menu_it_autosearch).setVisible(false);
        menu.findItem(R.id.menu_it_addToDesk).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_power_off:
                ActivitiesFinisher.finishAll();
                break;
            default:
                break;
        }
        return true;
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        act_account = (AutoCompleteTextView) findViewById(R.id.act_account);

        et_password = (EditText) findViewById(R.id.et_password);
        login_form = findViewById(R.id.login_form);
        login_progress = findViewById(R.id.login_progress);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_register = (Button) findViewById(R.id.bt_register);
        cb_login_remember = (CheckBox) findViewById(R.id.cb_login_remember);
        cb_login_auto = (CheckBox) findViewById(R.id.cb_login_auto);

        ll_trans_register = (LinearLayout) findViewById(R.id.ll_trans_register);
        ll_trans_login = (LinearLayout) findViewById(R.id.ll_trans_login);
        ll_trans_register.setTranslationX(-Distance);


        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        bt_confirm_register = (Button) findViewById(R.id.bt_confirm_register);
        bt_back_register = (Button) findViewById(R.id.bt_back_register);
        receivedIt = getIntent();
    }

    @Override
    public void initActionBar() {
        actionBar = getSupportActionBar();
    }

    @Override
    public void initListener() {
        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
        bt_confirm_register.setOnClickListener(this);
        bt_back_register.setOnClickListener(this);
    }

    @Override
    public void initData() {
        initBySharePre();
    }

    private void initBySharePre() {
        LoginSetting loginSetting = SharePreferenceUtils.loadSetting();
        act_account.setText(loginSetting.getDefaultAccount());
        cb_login_remember.setChecked(loginSetting.isRemember());
        cb_login_auto.setChecked(loginSetting.isAutoLogin());
        if (loginSetting.isRemember()){
            et_password.setText(loginSetting.getPassword());
        }
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()){
            case R.id.bt_login:
                attemptLogin(false);
                break;
            case R.id.bt_register:
                registermode(true);
                break;
            case R.id.bt_back_register:
                registermode(false);
                break;
            case R.id.bt_confirm_register:
                attemptRegist();
                break;
        }
    }

    private void registermode(boolean registerMode) {
        if (registerMode){
            ll_trans_login.animate().translationX(Distance).setDuration(500);
            ApplicationUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ll_trans_register.animate().translationX(0).setDuration(500);
                    actionBar.setTitle(R.string.button_register);
                }
            },250);
        }else {
            ll_trans_register.animate().translationX(-Distance).setDuration(500);
            ApplicationUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ll_trans_login.animate().translationX(0).setDuration(500);
                    actionBar.setTitle(R.string.app_name);
                }
            },250);
        }
    }

    private void attemptRegist() {
        String accountId = act_account.getText().toString().toLowerCase();
        String password = et_password.getText().toString();

        if (!judgeInputUnValid(accountId,password)){
            if (!et_confirm_password.getText().toString().equals(password)){
                et_confirm_password.setError("密码不一致");
                et_confirm_password.requestFocus();
            }else {
                ContactHttpDao.getInstance().register(accountId, password, et_nickname.getText().toString()
                        , new ContactHttpDao.ContactHttpCallback() {
                            @Override
                            public void onSuccess(Object o) {
                                ToastUtils.showToast(LoginActivity.this,"注册成功");
                            }

                            @Override
                            public void onFailed(int code, String errorMsg) {
                                ToastUtils.showToast(LoginActivity.this,"错误:code="+code+"...error:"+errorMsg);
                            }
                        });
            }
        }
    }

    public void onLoginDone(){
        loginRequest = null;
    }
    /**
     * 尝试登陆
     * @param auto 传入是否是自动登陆的布尔参数，用以确定是否还需要保存登陆数据
     */
    private void attemptLogin(final boolean auto) {

        final String accountId = act_account.getText().toString().toLowerCase();
        final String mpassword = et_password.getText().toString();
        boolean cancel = judgeInputUnValid(accountId,mpassword);
        if (!cancel){
            final LoadingDialog loginDialog = new LoadingDialog(this);
            loginDialog.showDialog("登录中",false,false);
            LoginInfo info = new LoginInfo(accountId,mpassword);

            loginRequest = NIMClient.getService(AuthService.class).login(info);

            RequestCallback<LoginInfo> callback = new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    if (!auto){
                        SharePreferenceUtils.saveLoginSetting(
                                accountId,mpassword,cb_login_remember.isChecked(),cb_login_auto.isChecked());
                    }
                    DataCacheManager.buildDataCacheAsync();
                    NIMClientManager.setAccount(accountId);
                    loginDialog.disappear();
                    ActivityUtils.startActivity(LoginActivity.this,MainActivity.class,true);
                }

                @Override
                public void onFailed(int code) {
                    loginDialog.disappear();
                    onLoginDone();
                    if (code == 302 || code == 404) {
                        Toast.makeText(LoginActivity.this, R.string.string_orpassword_wrong, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onException(Throwable throwable) {
                    loginDialog.disappear();
                    Toast.makeText(LoginActivity.this, R.string.login_exception, Toast.LENGTH_LONG).show();
                    onLoginDone();
                }
            };
            loginRequest.setCallback(callback);
        }
    }


    private boolean judgeInputUnValid(String accountId,String password) {
        View focusView =null;
        boolean cancel = true;
        act_account.setError(null);
        et_password.setError(null);
        if (TextUtils.isEmpty(accountId)){
            act_account.setError("请输入账号！");
            focusView = act_account;
        }else if (!isValid(accountId)){
            act_account.setError("输入长度必须在5-12");
            focusView = act_account;
        }else if (TextUtils.isEmpty(password)) {
            et_password.setError("您还未输入密码！");
            focusView = et_password;
        }else if (!isValid(password)){
            et_password.setError("输入长度必须在5-12");
            focusView = et_password;
        }else {
            cancel = false;
        }
        if (cancel){
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean isValid(String accountId) {
        return accountId.length() > 4 && accountId.length() < 13;
    }

    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            login_form.setVisibility(show ? View.GONE : View.VISIBLE);
            login_form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            login_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            login_form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
