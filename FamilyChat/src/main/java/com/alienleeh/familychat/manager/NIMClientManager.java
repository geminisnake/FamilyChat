package com.alienleeh.familychat.manager;

import android.text.TextUtils;

import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class NIMClientManager {
    private static String account;
    public static void initOnlineStatusObserver() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {
                ToastUtils.showToast(ApplicationUtils.getContext(),"在线状态:"+statusCode);
            }
        },true);
    }

    public static void init() {
        initOnlineStatusObserver();
        if (!TextUtils.isEmpty(account)){
            DataCacheManager.buildDataCacheAsync();
        }
    }

    public static void setAccount(String defaultAccount) {
        account = defaultAccount;
    }
}
