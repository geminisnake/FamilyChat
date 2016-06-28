package com.alienleeh.familychat.cache;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class FriendCache {
    private static FriendCache friendCache;
    private List<String> accounts = new ArrayList<String>();
    private List<NimUserInfo> FriendsInfo = new ArrayList<NimUserInfo>();

    private FriendCache() {
    }

    public static synchronized FriendCache getInstance(){
        if (friendCache == null){
            friendCache = new FriendCache();
        }
        return friendCache;
    }

    public void buildCache() {
        accounts = NIMClient.getService(FriendService.class).getFriendAccounts();
        FriendsInfo = NIMClient.getService(UserService.class).getUserInfoList(accounts);
    }

    public void clearCache() {
        accounts.clear();
        FriendsInfo.clear();
    }

    public List<NimUserInfo> getFriendData() {
        return FriendsInfo;
    }
}
