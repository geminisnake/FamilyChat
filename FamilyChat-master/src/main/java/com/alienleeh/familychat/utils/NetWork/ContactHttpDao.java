package com.alienleeh.familychat.utils.NetWork;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    public static final String HEADER_KEY_APP_KEY = "AppKey";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_USER_Nonce = "Nonce";
    public static final String HEADER_CURTIME = "CurTime";
    public static final String HEADER_CheckSUM = "CheckSum";
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

    private ContactHttpDao(){
        NimHttpClient.getInstance().init();
    }

    public void register(String accountId, String password, String nick, final ContactHttpCallback callback) {
        String url = MyServers.API_SERVER_TEST;
//        password = MD5.getStringMD5(password);暂时不要加密，自己测试麻烦
        String appKey = readAppKey();

        String appSecret = "7ba3793dceef";
        String nonce =  "9527";
        String curTime = String.valueOf((new Date()).getTime() / 1000L);
        String checkSum = CheckSumBuilder.getCheckSum(appSecret, nonce ,curTime);
        try {
            URLEncoder.encode(nick,"UTF-8");
        }catch (UnsupportedEncodingException e) {
            LogUtils.e("UnsupportedEncodingException:",e.toString());
        }

        Map<String, String> headers = new HashMap<>(1);

        //JSON头
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
        headers.put(HEADER_USER_Nonce, nonce);
        headers.put(HEADER_CURTIME,curTime);
        headers.put(HEADER_CheckSUM,checkSum);
        headers.put(HEADER_KEY_APP_KEY, appKey);


        //body
        List<NameValuePair> info = new ArrayList<>();
        info.add(new BasicNameValuePair("accid",accountId));
        info.add(new BasicNameValuePair("token",password));
        if (!TextUtils.isEmpty(nick)){
            info.add(new BasicNameValuePair("name",nick));
        }

        NimHttpClient.getInstance().execute(url,headers,info,new NimHttpClient.NimHttpCallback(){
            @Override
            public void onResponse(String response, int code, String msg) {
                if (code != 0){
                    LogUtils.e(this,"注册失败，code = "+code);
                    if (callback != null){
                        callback.onFailed(code,msg);
                    }
                    return;
                }

                try {
                    JSONObject object = JSON.parseObject(response);
                    int resCode = object.getIntValue("code");
                    if (resCode == RESULT_CODE_SUCCESS){
                        if (callback != null){
                            callback.onSuccess(null);
                        }
                    }else {
                        LogUtils.e(this,response);
                        callback.onFailed(resCode,object.getString("info"));
                    }
                }catch (JSONException e){
                    callback.onFailed(-1,e.getMessage());
                }
            }
        });

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
