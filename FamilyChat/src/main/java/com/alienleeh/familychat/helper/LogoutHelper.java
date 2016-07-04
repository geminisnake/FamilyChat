package com.alienleeh.familychat.helper;

import com.alienleeh.familychat.Observer.SyncDataStatusObserver;
import com.alienleeh.familychat.manager.NIMClientManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;

/**
 * Created by AlienLeeH on 2016/7/3.
 */
public class LogoutHelper {
    public static void logout() {
        // 清理缓存&注销监听&清除状态
        NIMClientManager.clearCache();
        SyncDataStatusObserver.getInstance().reset();
        SystemMsgHelper.registerSystemMsgReceived(false);
        NIMClient.getService(AuthService.class).logout();
    }
}
