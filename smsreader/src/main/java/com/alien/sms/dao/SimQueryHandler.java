package com.alien.sms.dao;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

public class SimQueryHandler extends AsyncQueryHandler {

	public SimQueryHandler(ContentResolver cr) {
		super(cr);
		// TODO Auto-generated constructor stub
	}
/*
 * 查询完成时回调
 */
	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		// TODO Auto-generated method stub
		super.onQueryComplete(token, cookie, cursor);
//		CursorUtils.printCursor(cursor);
		if (cookie != null && cookie instanceof CursorAdapter) {
			((CursorAdapter) cookie).changeCursor(cursor);
		}
	}
}
