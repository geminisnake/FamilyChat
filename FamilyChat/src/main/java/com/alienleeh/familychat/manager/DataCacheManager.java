package com.alienleeh.familychat.manager;

import com.alienleeh.familychat.MySingleThreadExecutor;
import com.alienleeh.familychat.cache.FriendCache;
import com.alienleeh.familychat.cache.UserInfoCache;

/**
 * Created by AlienLeeH on 2016/6/26.
 */
public class DataCacheManager {
    public static void buildDataCacheAsync() {
        MySingleThreadExecutor.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                buildDataCache();
            }
        });
    }

    private static void buildDataCache() {
        clearDataCache();
        FriendCache.getInstance().buildCache();
        UserInfoCache.getInstance().buildCache();
    }

    public static void clearDataCache() {
        FriendCache.getInstance().clearCache();
        UserInfoCache.getInstance().clearCache();
    }

    public static void observeSDKDataChanged(boolean register) {
        FriendCache.getInstance().registerObservers(register);
        UserInfoCache.getInstance().registerObservers(register);
    }
}
