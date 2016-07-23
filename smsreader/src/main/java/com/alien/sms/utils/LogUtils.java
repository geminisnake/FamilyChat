package com.alien.sms.utils;
import android.content.Context;
/**
 *@author Administrator
 */
import android.util.Log;

public class LogUtils {
	public static boolean isDebug = true;
	public static void e(Object tag,String msg){
		if (isDebug) {
			Log.e(tag.getClass().getSimpleName(), msg);
		}
	}
	public  static Context getContext(){
        return MyApplication.getContext();
    }
	public  static int dp2px(float dip){
		
        float midu = getContext().getResources().getDisplayMetrics().density;
        return (int)(dip * midu + 0.5f);
    }
    public static float px2dp(int px){
        float midu = getContext().getResources().getDisplayMetrics().density;
        return px / midu;
    }
	
}
