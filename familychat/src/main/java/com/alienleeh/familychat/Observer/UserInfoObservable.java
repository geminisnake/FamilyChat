package com.alienleeh.familychat.Observer;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/7/3.
 */
public class UserInfoObservable {
    private List<UserInfoObserver> observers = new ArrayList<>();
    private Handler uiHandler;

    public UserInfoObservable(Context context) {
        uiHandler = new Handler(context.getMainLooper());
    }

    synchronized public void registerObserver(UserInfoObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }
    synchronized public void unRegisterObserver(UserInfoObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }
    synchronized public void notifyObservers(final List<String> accounts){
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                for (UserInfoObserver observer : observers){
                    observer.onUserInfoChanged(accounts);
                }
            }
        });
    }


    public interface UserInfoObserver {
        void onUserInfoChanged(List<String> accounts);
    }
}
