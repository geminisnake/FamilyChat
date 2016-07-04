package com.alienleeh.familychat.manager;

import android.content.Context;
import android.text.TextUtils;

import com.alienleeh.familychat.helper.ImageLoaderHelper;
import com.alienleeh.familychat.helper.SystemMsgHelper;
import com.alienleeh.familychat.helper.UserInfoHelper;
import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.ToastUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public final class NIMClientManager {


    private static String account;

    private static ImageLoaderHelper imageLoaderHelper;

    private static Context context;

    public static void initOnlineStatusObserver() {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            @Override
            public void onEvent(StatusCode statusCode) {
                switch (statusCode){
                    case PWD_ERROR:
                        ToastUtils.showToast(ApplicationUtils.getContext(),"账号或密码错误");
                        break;
                    case LOGINED:
                        ToastUtils.showToast(ApplicationUtils.getContext(),"登陆成功");
                        break;
                    case KICKOUT:
                    case KICK_BY_OTHER_CLIENT:

                        break;
                }

            }
        },true);
    }

    public static void init(Context context) {
        NIMClientManager.context = context;
        initOnlineStatusObserver();
        if (!TextUtils.isEmpty(account)){
            DataCacheManager.buildDataCacheAsync();
        }
        SystemMsgHelper.registerSystemMsgReceived(true);
        NIMClientManager.imageLoaderHelper = new ImageLoaderHelper(NIMClientManager.context,null);
        DataCacheManager.observeSDKDataChanged(true);
    }

    public static void setAccount(String defaultAccount) {
        account = defaultAccount;
    }
    public static String getAccount() {
        return account;
    }

    public static void clearCache() {
        DataCacheManager.clearDataCache();
    }

    public static void notifyUserInfoChanged(List<String> accounts) {
        UserInfoHelper.notifyChanged(accounts);
    }
}
