package com.alienleeh.familychat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.alienleeh.familychat.Global.Constant;
import com.alienleeh.familychat.bean.LoginSetting;

/**
 * sharepreference操作工具类
 * Created by AlienLeeH on 2016/6/17.
 */
public class SharePreferenceUtils {


    public static LoginSetting loadSetting() {
        SharedPreferences preferences =
                ApplicationUtils.getContext().getSharedPreferences(Constant.CACHE.SHAREPRE_LOGIN,Context.MODE_PRIVATE);
        String defaultAccount = preferences.getString("account","");
        String password = preferences.getString("password","");
        boolean isRemember = preferences.getBoolean("isRemember",false);
        boolean autoLogin = preferences.getBoolean("autoLogin",false);
        return LoginSetting.getInstance(defaultAccount,password,isRemember,autoLogin);
    }

    public static void saveLoginSetting(String accountId, String password, boolean isRemember, boolean autoLogin) {
        SharedPreferences.Editor editor =
                ApplicationUtils.getContext().getSharedPreferences(Constant.CACHE.SHAREPRE_LOGIN,Context.MODE_PRIVATE).edit();
        editor.putString("account",accountId);
        editor.putBoolean("isRemember",isRemember);
        editor.putBoolean("autoLogin",autoLogin);
        if (isRemember){
            editor.putString("password",password);
        }
        editor.apply();
    }
}

