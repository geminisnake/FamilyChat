package com.alienleeh.familychat.manager;

import com.alienleeh.familychat.MySingleThreadExecutor;
import com.alienleeh.familychat.cache.FriendCache;

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
    }

    private static void clearDataCache() {
        FriendCache.getInstance().clearCache();
    }
}
