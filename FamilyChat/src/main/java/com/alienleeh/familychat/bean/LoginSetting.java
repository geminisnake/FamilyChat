package com.alienleeh.familychat.bean;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class LoginSetting {
    private static LoginSetting loginSetting = null;
    private String defaultAccount;
    private String password;
    private  boolean isRemember;
    private boolean autoLogin;

    public String getDefaultAccount() {
        return defaultAccount;
    }

    public String getPassword() {
        return password;
    }

    public boolean isRemember() {
        return isRemember;
    }

    public boolean isAutoLogin() {
        return autoLogin;
    }

    private LoginSetting(String defaultAccount, String password, boolean isRemember, boolean autoLogin) {
        this.defaultAccount = defaultAccount;
        this.password = password;
        this.isRemember = isRemember;
        this.autoLogin = autoLogin;
    }


    public static LoginSetting getInstance(String defaultAccount, String password, boolean isRemember, boolean autoLogin) {
        if (loginSetting == null){
            loginSetting = new LoginSetting(defaultAccount,password,isRemember,autoLogin);
        }else {
            loginSetting.defaultAccount = defaultAccount;
            loginSetting.password = password;
            loginSetting.isRemember = isRemember;
            loginSetting.autoLogin = autoLogin;
        }
        return loginSetting;
    }
}
