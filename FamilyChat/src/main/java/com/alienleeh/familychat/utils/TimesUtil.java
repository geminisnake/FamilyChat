package com.alienleeh.familychat.utils;

import java.math.BigDecimal;

/**
 * Created by AlienLeeH on 2016/7/21..Hour:23
 * Email:alienleeh@foxmail.com
 * Description:时间工具类 初步功能转换时间
 */
public class TimesUtil {
    public static long getSecondsByMillSec(long duration) {
        long seconds = new BigDecimal((float)duration / 1000).setScale(0,BigDecimal.ROUND_HALF_UP).intValue();
        if (seconds == 0){
            seconds = 1;
        }
        return seconds;
    }
}
