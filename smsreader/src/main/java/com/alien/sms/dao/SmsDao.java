package com.alien.sms.dao;

import java.util.List;

import com.alien.sms.global.Constant;
import com.alien.sms.receiver.SendSmsReceiver;

import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

public class SmsDao {

	public static void sendSms(String address, String sendingMessage,Context context) {
		SmsManager manager = SmsManager.getDefault();
		List<String> smss = manager.divideMessage(sendingMessage);
		Intent intent = new Intent(SendSmsReceiver.ACTION_SEND_SMS);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		for (String string : smss) {
			manager.sendTextMessage(address,null,string, pendingIntent, null);
			insertSms(context,string,address);
		}
	}

	private static void insertSms(Context context, String string,String address) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("body", string);
		values.put("address", address);
		values.put("type", Constant.MSG_TYPE.SENT);
		context.getContentResolver().insert(Constant.URI.SMS, values);
	}
}
