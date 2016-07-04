package com.alienleeh.familychat.helper;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.widget.CursorAdapter;

public class SimQueryHandler extends AsyncQueryHandler {

	public SimQueryHandler(ContentResolver cr) {
		super(cr);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @param token
	 * @param cookie
     * @param cursor
	 *
     */

	protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
		// TODO Auto-generated method stub
		super.onQueryComplete(token, cookie, cursor);
		if (cookie != null && cookie instanceof CursorAdapter) {
			((CursorAdapter) cookie).changeCursor(cursor);
		}
	}
}
