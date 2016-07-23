package com.alien.sms.bean;

import android.database.Cursor;

public class ConversationDetail {
		private String msgBody;
		private int TYPE;
		private long date;
		private long _id;
		public long get_id() {
			return _id;
		}
		public void set_id(long _id) {
			this._id = _id;
		}
		public static ConversationDetail createFromCursor(Cursor cursor){
			ConversationDetail detail = new ConversationDetail();
			detail.setMsgBody(cursor.getString(cursor.getColumnIndex("body")));
			detail.set_id(cursor.getLong(cursor.getColumnIndex("_id")));
			detail.setTYPE(cursor.getInt(cursor.getColumnIndex("type")));
			detail.setDate(cursor.getLong(cursor.getColumnIndex("date")));
			return detail;			
		}
		public long getDate() {
			return date;
		}
		public void setDate(long date) {
			this.date = date;
		}
		public String getMsgBody() {
			return msgBody;
		}
		public void setMsgBody(String msgBody) {
			this.msgBody = msgBody;
		}
		public int getTYPE() {
			return TYPE;
		}
		public void setTYPE(int tYPE) {
			TYPE = tYPE;
		}	
}

