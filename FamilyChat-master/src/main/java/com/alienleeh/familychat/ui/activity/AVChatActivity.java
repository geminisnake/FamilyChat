package com.alienleeh.familychat.ui.activity;

import android.content.Context;
import android.content.Intent;

import com.netease.nimlib.sdk.avchat.model.AVChatData;

/**
 * Created by AlienLeeH on 2016/6/29.
 */

public class AVChatActivity {
    private static final String KEY_IN_CALLING = "KEY_IN_CALLING";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    private static final String KEY_CALL_TYPE = "KEY_CALL_TYPE";
    private static final String KEY_SOURCE = "source";
    private static final String KEY_CALL_AVCHAT_DATA = "KEY_CALL_AVCHAT_DATA";
    /**
     * 来自广播
     */
    public static final int FROM_BROADCASTRECEIVER = 0;
    /**
     * 来自发起方
     */
    public static final int FROM_INTERNAL = 1;
    /**
     * 来自通知栏
     */
    public static final int FROM_NOTIFICATION = 2;
    /**
     * 未知的入口
     */
    public static final int FROM_UNKNOWN = -1;
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
    public static void launch(Context context, AVChatData data, int source) {
        Intent intent = new Intent();
        intent.setClass(context, AVChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(KEY_CALL_AVCHAT_DATA, data);
        intent.putExtra(KEY_IN_CALLING, true);
        intent.putExtra(KEY_SOURCE, source);
        context.startActivity(intent);
    }
}
