package com.alienleeh.qqdemo.utils;

import android.content.Intent;

import com.alienleeh.qqdemo.base.BaseActivity;

/**
 * Created by AlienLeeH on 2016/6/21.
 */
public class ActivityUtils {
    public static void startActivity(BaseActivity ActivityA, Class classB){
        startActivity(ActivityA,classB,false);
    }

    public static void startActivity(BaseActivity ActivityA, Class classB, boolean b) {
        Intent it = new Intent(ActivityA,classB);
        ActivityA.startActivity(it);
        if (b){
            ActivityA.finish();
        }
    }
}
