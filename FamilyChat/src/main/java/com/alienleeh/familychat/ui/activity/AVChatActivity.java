package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by AlienLeeH on 2016/6/29.
 */
public class AVChatActivity {

    public static void start(Context context, String account, int callType, int source) {
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.putExtra(KEY_ACCOUNT, account);
        intent.putExtra(KEY_IN_CALLING, false);
        intent.putExtra(KEY_CALL_TYPE, callType);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }

    /**
     * 来电跳转
     *
     */
    public static void launch(Context context, AVChatData config, int source) {
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_CALL_CONFIG, config);
        intent.putExtra(KEY_IN_CALLING, true);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }
}
