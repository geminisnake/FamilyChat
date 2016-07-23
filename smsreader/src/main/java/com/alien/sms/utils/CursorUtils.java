package com.alien.sms.utils;

import android.database.Cursor;
import android.util.Log;

public class CursorUtils {
	public static void printCursor(Cursor cursor){
		LogUtils.e(cursor, "һ����"+cursor.getCount()+"������");
		cursor.moveToPrevious();
		while (cursor.moveToNext()) {
			for (int i = 0; i < cursor.getColumnCount(); i++) {
				String name = cursor.getColumnName(i);
				String content = cursor.getString(i);
				LogUtils.e(cursor, name+"::::"+content);
			}
			Log.e("���Ƿָ���", "����������������");
		}
	}
}
