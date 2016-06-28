package com.alienleeh.familychat.utils;
/**
 *@author AlienLeeH
 */
import android.util.Log;

public class LogUtils {
	public static boolean isDebug = true;
	public static void e(Object tag,String msg){
		if (isDebug) {
			Log.e(tag.getClass().getSimpleName(), msg);
		}
	}
	
}
