package com.alienleeh.mylibrary.test;

import android.content.Context;

/**
 * Created by AlienLeeH on 2016/7/25..Hour:13
 * Email:alienleeh@foxmail.com
 * Description:
 */
public class Utils {
    public static float dp2px(Context mContext, int dp) {
        float midu = mContext.getResources().getDisplayMetrics().density;

        return dp * midu;
    }
}
