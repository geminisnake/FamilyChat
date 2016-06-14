package com.alienleeh.familychat.helper;

import com.alienleeh.familychat.Observer.UserInfoObservable;
import com.alienleeh.familychat.utils.ApplicationUtils;

import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/3.
 */
public class UserInfoHelper {
    private static UserInfoObservable userInfoObservable;
    /**
     * 注册用户资料变化观察者。<br>
     *     注意：不再观察时(如Activity destroy后)，要unregister，否则会造成资源泄露
     * @param observer 观察者
     */
    public static void registerObserver(UserInfoObservable.UserInfoObserver observer) {
        if (userInfoObservable == null) {
            userInfoObservable = new UserInfoObservable(ApplicationUtils.getContext());
        }
        userInfoObservable.registerObserver(observer);
    }
    public static void unRegisterObserver(UserInfoObservable.UserInfoObserver observer) {
        if (userInfoObservable != null) {
            userInfoObservable.unRegisterObserver(observer);
        }
    }
    /**
     * 当用户资料发生改动时，请调用此接口，通知更新UI
     * @param accounts 有用户信息改动的帐号列表
     */
    public static void notifyChanged(List<String> accounts) {
        if (userInfoObservable != null) {
            userInfoObservable.notifyObservers(accounts);
        }
    }
}
