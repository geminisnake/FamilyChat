package com.alienleeh.familychat.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class ActivityUtils {
    public static void startActivity(Activity actA, Class activityClass, boolean b,String extra) {
        Intent it = new Intent(actA,activityClass);
        if (extra != null & !TextUtils.isEmpty(extra)){
            it.putExtra("extra1",extra);
        }
        actA.startActivity(it);
        if (b){
            actA.finish();
        }
    }
    public static void startActivity(Activity actA,Class activityClass, boolean finish){
        startActivity(actA,activityClass,finish,null);
    }
    public static void startActivity(Activity actA,Class activityClass, String extra){
        startActivity(actA,activityClass,false,extra);
    }
    public static void startActivity(Activity actA, Class activityClass) {
        startActivity(actA,activityClass,false,null);
    }
}
