package com.alienleeh.familychat.utils;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by AlienLeeH on 2016/6/22.
 */
public class ActivityUtils {
    public static void startActivity(Activity actA, Class activityClass, boolean b) {
        Intent it = new Intent(actA,activityClass);
        actA.startActivity(it);
        if (b){
            actA.finish();
        }
    }

    public static void startActivity(Activity actA, Class activityClass) {
        startActivity(actA,activityClass,false);
    }
}
