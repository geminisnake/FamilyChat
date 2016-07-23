package com.alien.sms.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	public static void showToast(Context context,String text) {
		Toast.makeText(context, text, 0).show();
	}
}
