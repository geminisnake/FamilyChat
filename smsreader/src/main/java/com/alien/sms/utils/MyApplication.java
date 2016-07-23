package com.alien.sms.utils;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class MyApplication extends Application{
    public static Context getContext() {
        return context;
    }

    public static Handler getHandler() {
        return handler;
    }

    public static int getMainThreadId() {
        return mainThreadId;
    }

    private static Context context;
    private static Handler handler;
    private static int mainThreadId;

    @Override
    public void onCreate() {
        context = getApplicationContext();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    default:
                        super.handleMessage(msg);
                }
            }
        };
        mainThreadId = android.os.Process.myTid();

        super.onCreate();
    }	
}
