package com.alienleeh.familychat.Observer;

import android.os.Handler;

import com.alienleeh.familychat.utils.ApplicationUtils;
import com.alienleeh.familychat.utils.LogUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlienLeeH on 2016/6/26.
 */
public class SyncDataStatusObserver {
    private static final int TIME_OUT_SECONDS = 10;

    private Handler uiHandler;

    private Runnable timeoutRunnable;
    /**
     * 状态
     */
    private LoginSyncStatus syncStatus = LoginSyncStatus.NO_BEGIN;

    private List<Observer<Void>> observers = new ArrayList<>();

    /**
     * 注销时清除状态&监听
     */
    public void reset() {
        syncStatus = LoginSyncStatus.NO_BEGIN;
        observers.clear();
    }


    private static SyncDataStatusObserver syncDataStatusObserver;
    public static synchronized SyncDataStatusObserver getInstance() {
        if (syncDataStatusObserver == null){
            syncDataStatusObserver = new SyncDataStatusObserver();
        }
        return syncDataStatusObserver;
    }

    public void registerLoginSyncDataStatus(boolean b) {
        NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(new Observer<LoginSyncStatus>() {
            @Override
            public void onEvent(LoginSyncStatus loginSyncStatus) {
                syncStatus = loginSyncStatus;
            }
        }, b);
    }

    /**
     * 监听登录后同步数据完成事件，缓存构建完成后自动取消监听
     * 调用时机：登录成功后
     *
     * @param observer 观察者
     * @return 返回true表示数据同步已经完成或者不进行同步，返回false表示正在同步数据
     */
    public boolean observeSyncDataCompletedEvent(Observer<Void> observer) {
        if (syncStatus == LoginSyncStatus.NO_BEGIN || syncStatus == LoginSyncStatus.SYNC_COMPLETED) {
            /*
            * NO_BEGIN 如果登录后未开始同步数据，那么可能是自动登录的情况:
            * PUSH进程已经登录同步数据完成了，此时UI进程启动后并不知道，这里直接视为同步完成
            */
            return true;
        }

        // 正在同步
        if (!observers.contains(observer)) {
            observers.add(observer);
        }

        // 超时定时器
        if (uiHandler == null) {
            uiHandler = new Handler(ApplicationUtils.getContext().getMainLooper());
        }

        if (timeoutRunnable == null) {
            timeoutRunnable = new Runnable() {
                @Override
                public void run() {
                    // 如果超时还处于开始同步的状态，模拟结束
                    if (syncStatus == LoginSyncStatus.BEGIN_SYNC) {
                        onLoginSyncDataCompleted(true);
                    }
                }
            };
        }

        uiHandler.removeCallbacks(timeoutRunnable);
        uiHandler.postDelayed(timeoutRunnable, TIME_OUT_SECONDS * 1000);

        return false;
    }

    /**
     * 登录同步数据完成处理
     */
    private void onLoginSyncDataCompleted(boolean timeout) {
        // 移除超时任务（有可能完成包到来的时候，超时任务都还没创建）
        LogUtils.e(this,"onLoginSyncDataCompleted,timeout:"+timeout);
        if (timeoutRunnable != null) {
            uiHandler.removeCallbacks(timeoutRunnable);
        }

        // 通知上层
        for (Observer<Void> o : observers) {
            o.onEvent(null);
        }

        // 重置状态
        reset();
    }

}
