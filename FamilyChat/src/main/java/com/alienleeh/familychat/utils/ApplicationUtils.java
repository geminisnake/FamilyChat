package com.alienleeh.familychat.utils;

import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;

import com.alienleeh.familychat.Global.MyApplication;

/**
 * Created by AlienLeeH on 2016/6/23.
 */
public class ApplicationUtils {
    public  static Context getContext(){
        return MyApplication.getContext();
    }
    public  static Handler getHandler(){
        return MyApplication.getHandler();
    }
    public  static int getMainThreadId(){
        return MyApplication.getMainThreadId();
    }
    public  static int dp2px(float dip){
        float midu = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * midu + 0.5f);
    }
    public static float px2dp(int px){
        float midu = getContext().getResources().getDisplayMetrics().density;
        return px / midu;
    }

    public static int getWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getWidth();
    }
    public static int getHeight() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        return manager.getDefaultDisplay().getHeight();
    }
}
