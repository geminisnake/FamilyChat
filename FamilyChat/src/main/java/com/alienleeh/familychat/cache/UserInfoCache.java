package com.alienleeh.familychat.cache;

import android.text.TextUtils;

import com.alienleeh.familychat.manager.NIMClientManager;
import com.alienleeh.familychat.utils.LogUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.friend.model.Friend;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.UserServiceObserve;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by AlienLeeH on 2016/7/1.
 */
public class UserInfoCache {
    private static UserInfoCache userInfoCace = null;

    private Map<String,NimUserInfo> account2UserMap = new ConcurrentHashMap<>();

    private Map<String, List<RequestCallback<NimUserInfo>>> requestUserInfoMap = new ConcurrentHashMap<>();

    private UserInfoCache() {}

    public static synchronized UserInfoCache getInstance(){
        if (userInfoCace == null){
            userInfoCace = new UserInfoCache();
        }
        return userInfoCace;
    }


    public void buildCache() {
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getAllUserInfo();
        addOrUpdateUsers(users, false);
        LogUtils.i(this.toString(), "build NimUserInfoCache completed, users count = " + account2UserMap.size());
    }
    public void getUserInfoFromRomate(final String account, final RequestCallback<NimUserInfo> callback){
        if (TextUtils.isEmpty(account)){
            return;
        }
        if (requestUserInfoMap.containsKey(account)){
            if (callback != null){
                requestUserInfoMap.get(account).add(callback);
            }
            return;
        }else {
            List<RequestCallback<NimUserInfo>> callbackList = new ArrayList<>();
            if (callback != null){
                callbackList.add(callback);
            }
            requestUserInfoMap.put(account,callbackList);
        }
        List<String> accounts = new ArrayList<>(1);
        accounts.add(account);

        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallbackWrapper<List<NimUserInfo>>() {
            @Override
            public void onResult(int i, List<NimUserInfo> nimUserInfos, Throwable throwable) {
                if (throwable != null){
                    callback.onException(throwable);
                    return;
                }
                NimUserInfo user = null;
                boolean hasCallback = !requestUserInfoMap.get(account).isEmpty();
                if (i == ResponseCode.RES_SUCCESS && nimUserInfos != null && !nimUserInfos.isEmpty()){
                    user = nimUserInfos.get(0);
                }
                if (hasCallback){
                    List<RequestCallback<NimUserInfo>> cbs = requestUserInfoMap.get(account);
                    for (RequestCallback<NimUserInfo> cb : cbs){
                        if (i == ResponseCode.RES_SUCCESS){
                            cb.onSuccess(user);
                        }else {
                            cb.onFailed(i);
                        }
                    }
                }
                requestUserInfoMap.remove(account);
            }
        });
    }
    /**
     * 从云信服务器获取批量用户信息[异步]
     */
    public void getUserInfoFromRemote(List<String> accounts, final RequestCallback<List<NimUserInfo>> callback) {
        NIMClient.getService(UserService.class).fetchUserInfo(accounts).setCallback(new RequestCallback<List<NimUserInfo>>() {
            @Override
            public void onSuccess(List<NimUserInfo> users) {
                // 这里不需要更新缓存，由监听用户资料变更（添加）来更新缓存
                if (callback != null) {
                    callback.onSuccess(users);
                }
            }

            @Override
            public void onFailed(int code) {
                if (callback != null) {
                    callback.onFailed(code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                if (callback != null) {
                    callback.onException(exception);
                }
            }
        });
    }

    private void addOrUpdateUsers(final List<NimUserInfo> users, boolean notify) {
        if (users == null || users.isEmpty()) {
            return;
        }
        // update cache
        for (NimUserInfo u : users) {
            account2UserMap.put(u.getAccount(), u);
        }
        // log
        List<String> accounts = getAccounts(users);

        // 通知变更
        if (notify && accounts != null && !accounts.isEmpty()) {
            NIMClientManager.notifyUserInfoChanged(accounts); // 通知到UI组件
        }
    }


    public List<NimUserInfo> getAllUsersOfMyFriend() {
        List<String> accounts = FriendCache.getInstance().getMyFriendAccounts();
        List<NimUserInfo> users = new ArrayList<>();
        List<String> unknownAccounts = new ArrayList<>();
        for (String account : accounts) {
            if (hasUser(account)) {
                users.add(getUserInfo(account));
            } else {
                unknownAccounts.add(account);
            }
        }
        return users;
    }

    private boolean hasUser(String account) {
        return !(account == null || TextUtils.isEmpty(account)) && account2UserMap.containsKey(account);
    }

    public NimUserInfo getUserInfo(String account) {
        if (TextUtils.isEmpty(account) || account2UserMap == null) {

            return null;
        }

        return account2UserMap.get(account);
    }
   public NimUserInfo getMyInfo(){
       return account2UserMap.get(NIMClientManager.getAccount());
   }

    /**
     * 获取用户显示名称。
     * 若设置了备注名，则显示备注名。
     * 若没有设置备注名，用户有昵称则显示昵称，用户没有昵称则显示帐号。
     *
     * @param account 用户帐号
     * @return
     */
    public String getUserDisplayName(String account) {
        String alias = getAlias(account);
        if (!TextUtils.isEmpty(alias)) {
            return alias;
        }

        return getUserName(account);
    }

    private String getAlias(String account) {
        Friend friend = FriendCache.getInstance().getFriendByAccount(account);
        if (friend != null && !TextUtils.isEmpty(friend.getAlias())){
            return friend.getAlias();
        }
        return null;
    }

    public String getUserName(String account) {
        NimUserInfo userInfo = getUserInfo(account);
        if (userInfo != null && !TextUtils.isEmpty(userInfo.getName())) {
            return userInfo.getName();
        } else {
            return account;
        }
    }
    public String getAvatar(String account) {
        if (getUserInfo(account) != null){
            return getUserInfo(account).getAvatar();
        }
        return null;
    }
    private List<String> getAccounts(List<NimUserInfo> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }

        List<String> accounts = new ArrayList<>(users.size());
        for (NimUserInfo user : users) {
            accounts.add(user.getAccount());
        }

        return accounts;
    }

    public void clearCache() {
        account2UserMap.clear();
    }
    /**
     * 注册用户资料变更观察者
     */
    public void registerObservers(boolean register) {
        NIMClient.getService(UserServiceObserve.class).observeUserInfoUpdate(userInfoUpdateObserver,register);
    }

    private Observer<List<NimUserInfo>> userInfoUpdateObserver = new Observer<List<NimUserInfo>>() {
        @Override
        public void onEvent(List<NimUserInfo> nimUserInfos) {
            if (nimUserInfos == null || nimUserInfos.isEmpty()){
                return;

            }
            addOrUpdateUsers(nimUserInfos,true);
        }
    };
}
