package com.alien.sms.bean;

import android.database.Cursor;

public class Conversation {
	private String snippet;
	private String threadId;
	private String msgCount;
	private String address;
	private long date;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public static Conversation createFromCursor(Cursor cursor) {
		Conversation conversation = new Conversation();
		conversation.setSnippet(cursor.getString(cursor.getColumnIndex("snippet")));
		conversation.setThreadId(cursor.getString(cursor.getColumnIndex("_id")));
		conversation.setMsgCount(cursor.getString(cursor.getColumnIndex("msg_count")));
		conversation.setAddress(cursor.getString(cursor.getColumnIndex("address")));
		conversation.setDate(cursor.getLong(cursor.getColumnIndex("date")));
		return conversation;
	}
	public String getSnippet() {
		return snippet;
	}
	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getMsgCount() {
		return msgCount;
	}
	public void setMsgCount(String msgCount) {
		this.msgCount = msgCount;
	}
	
}
