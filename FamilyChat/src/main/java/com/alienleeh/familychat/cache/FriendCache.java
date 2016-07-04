package com.alienleeh.familychat.cache;

import android.text.TextUtils;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.friend.FriendServiceObserve;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Created by AlienLeeH on 2016/6/25.
 */
public class FriendCache {
    private static FriendCache friendCache;

    private Set<String> friendAccountSet = new CopyOnWriteArraySet<>();

    private Map<String, Friend> friendMap = new ConcurrentHashMap<>();

    private List<FriendDataChangedObserver> friendObservers = new ArrayList<>();


    private FriendCache() {
    }

    public static synchronized FriendCache getInstance(){
        if (friendCache == null){
            friendCache = new FriendCache();
        }
        return friendCache;
    }

    public void buildCache() {
        // 获取我所有的好友关系
        List<Friend> friends = NIMClient.getService(FriendService.class).getFriends();
        for (Friend f : friends) {
            friendMap.put(f.getAccount(), f);
        }

        //获取账号
        List<String> accounts = NIMClient.getService(FriendService.class).getFriendAccounts();
        if (accounts == null || accounts.isEmpty()){
            return;
        }

        friendAccountSet.addAll(accounts);
    }

    public void clearCache() {
        //清除缓存
        friendMap.clear();
        friendAccountSet.clear();
    }

    /**
     * 好友查询
     */
    public List<String> getMyFriendAccounts() {
        List<String> accounts = new ArrayList<>(friendAccountSet.size());
        accounts.addAll(friendAccountSet);
        return accounts;
    }
    public Friend getFriendByAccount(String account){
        if (TextUtils.isEmpty(account)){
            return null;
        }
        return friendMap.get(account);
    }
    public int getMyFriendCounts() {
        return friendAccountSet.size();
    }

    public boolean isMyFriend(String account) {
        return friendAccountSet.contains(account);
    }



    /*
    缓存监听SDK通知
     */
    public void registerObservers(boolean register) {
        NIMClient.getService(FriendServiceObserve.class).observeFriendChangedNotify(friendChangedNotifyObserver, register);
    }

    private Observer<FriendChangedNotify> friendChangedNotifyObserver = new Observer<FriendChangedNotify>() {
        @Override
        public void onEvent(FriendChangedNotify friendChangedNotify) {
            List<Friend> addOrUpdateFriends = friendChangedNotify.getAddedOrUpdatedFriends();
            List<String> deleteFriends = friendChangedNotify.getDeletedFriends();

            List<String> friendAccounts = new ArrayList<>(addOrUpdateFriends.size());
            String account;
            for (Friend f : addOrUpdateFriends){
                account = f.getAccount();
                friendMap.put(account,f);
                friendAccounts.add(account);
            }

            if (!friendAccounts.isEmpty()){
                friendAccountSet.addAll(friendAccounts);
                //通知好友关系更新
                for (FriendDataChangedObserver o : friendObservers){
                    o.onAddedOrUpdatedFriends(friendAccounts);
                }
            }

            if (!deleteFriends.isEmpty()){
                friendAccountSet.removeAll(deleteFriends);
                for (String f : deleteFriends){
                    friendMap.remove(f);
                }
                //通知有好友关系删除
                for (FriendDataChangedObserver o : friendObservers){
                    o.onDeletedFriends(friendAccounts);
                }
            }
        }
    };

    /**
     * APP监听缓存
     */
    public void registerFriendDataChangedObserver(FriendDataChangedObserver o, boolean register) {
        if (o == null) {
            return;
        }

        if (register) {
            if (!friendObservers.contains(o)) {
                friendObservers.add(o);
            }
        } else {
            friendObservers.remove(o);
        }
    }


    /*
    *
    *程序监听缓存观察者接口
    * */
    public interface FriendDataChangedObserver {
        void onAddedOrUpdatedFriends(List<String> accounts);

        void onDeletedFriends(List<String> accounts);

        void onAddUserToBlackList(List<String> account);

        void onRemoveUserFromBlackList(List<String> account);
    }

}
