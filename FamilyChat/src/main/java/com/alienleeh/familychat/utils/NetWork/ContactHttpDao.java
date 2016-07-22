package com.alienleeh.familychat.utils.NetWork;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;
import com.alienleeh.familychat.utils.string.MD5;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class ContactHttpDao {
    // code
    private static final int RESULT_CODE_SUCCESS = 200;

    // api
    private static final String API_NAME_REGISTER = "createDemoUser";

    // header
    private static final String HEADER_KEY_APP_KEY = "appkey";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_USER_AGENT = "User-Agent";

    // request
    private static final String REQUEST_USER_NAME = "username";
    private static final String REQUEST_NICK_NAME = "nickname";
    private static final String REQUEST_PASSWORD = "password";

    // result
    private static final String RESULT_KEY_RES = "res";
    private static final String RESULT_KEY_ERROR_MSG = "errmsg";
    private static ContactHttpDao instance = null;

    public static synchronized ContactHttpDao getInstance(){
        if (instance == null){
            instance = new ContactHttpDao();
        }
        return instance;
    }

    public void register(String accountId, String password, String nick, ContactHttpCallback callback) {
        String url = MyServers.apiServer()
        password = MD5.getStringMD5(password);
        String appKey = readAppKey();
        try {
            URLEncoder.encode(nick,"UTF-8");
        }catch (UnsupportedEncodingException e) {
            LogUtils.e("UnsupportedEncodingException:",e.toString());
        }

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
        headers.put(HEADER_USER_AGENT, "nim_demo_android");
        headers.put(HEADER_KEY_APP_KEY, appKey);

    }
    public interface ContactHttpCallback<T>{
        void onSuccess(T t);
        void onFailed(int code,String errorMsg);
    }

    private String readAppKey() {
        try {
            ApplicationInfo appInfo = ApplicationUtils.getContext().getPackageManager()
                    .getApplicationInfo(ApplicationUtils.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
