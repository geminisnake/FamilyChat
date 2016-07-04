package com.alienleeh.familychat.emotions.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.alienleeh.familychat.utils.ApplicationUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by AlienLeeH on 2016/7/8.
 */
public class SpanStringUtils {

    public static SpannableString getEmotionContent(boolean scale, final Context context, final TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);

        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);

        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            String path = null;

            int whichtype = -1;
            for (Integer type:EmotionUtils.ALL_EMOTION) {
                if (EmotionUtils.getEmotionMap(type).get(key) != null){
                    path = EmotionUtils.getImgByName(type,key);
                    whichtype = type;
                    break;
                }
            }
            if (path != null) {
                // 压缩表情图片
                AssetManager assetManager = ApplicationUtils.getContext().getAssets();
                try {
                    InputStream stream = assetManager.open(path);
                    int size = 0;
                    if (scale & whichtype != 1){
                        size = (int) tv.getTextSize()*55/10;
                    }else {
                        size = (int) tv.getTextSize()*18/10;
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    ImageSpan span = new ImageSpan(context, scaleBitmap);
                    spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return spannableString;
    }

    public static SpannableString getEmotionContent(final Context context, final TextView tv, String source) {
        SpannableString spannableString = new SpannableString(source);
        String regexEmotion = "\\[([\u4e00-\u9fa5\\w])+\\]";
        Pattern patternEmotion = Pattern.compile(regexEmotion);
        Matcher matcherEmotion = patternEmotion.matcher(spannableString);
        while (matcherEmotion.find()) {
            // 获取匹配到的具体字符
            String key = matcherEmotion.group();
            // 匹配字符串的开始位置
            int start = matcherEmotion.start();
            // 利用表情名字获取到对应的图片
            String path = null;

            for (Integer type:EmotionUtils.ALL_EMOTION) {
                if (EmotionUtils.getEmotionMap(type).get(key) != null){
                    path = EmotionUtils.getImgByName(type,key);
                    break;
                }
            }
            if (path != null) {
                // 压缩表情图片
                AssetManager assetManager = ApplicationUtils.getContext().getAssets();
                try {
                    InputStream stream = assetManager.open(path);
                    int size = (int) tv.getTextSize()*18/10;
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    Bitmap scaleBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    ImageSpan span = new ImageSpan(context, scaleBitmap);
                    spannableString.setSpan(span, start, start + key.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return spannableString;
    }
}