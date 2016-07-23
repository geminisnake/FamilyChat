package com.alien.sms.receiver;

import com.alien.sms.utils.ToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SendSmsReceiver extends BroadcastReceiver {
	public static final String ACTION_SEND_SMS = "com.alien.sms";
	@Override
	public void onReceive(Context context, Intent intent) {
		int code = getResultCode();
		if (code == Activity.RESULT_OK) {
			ToastUtils.showToast(context, "发送成功");
		}else
			ToastUtils.showToast(context, "发送失败");
	}
}
