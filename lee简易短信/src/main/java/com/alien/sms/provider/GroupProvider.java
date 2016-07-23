package com.alien.sms.provider;

import com.alien.sms.dao.GroupDatabaseOpenHelper;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class GroupProvider extends ContentProvider {

	private GroupDatabaseOpenHelper helper;
	private SQLiteDatabase database;
	private static final String authority = "com.alien.sms";
	public static final Uri BASE_URI = Uri.parse("content://"+authority);
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final String TABLE_THREAD_GROUP = "thread_group";
	public static final int CODE_GROUP_INSERT = 0;
	public static final int CODE_GROUP = 1;
	public static final int CODE_GROUP_UPDATE = 2;
	public static final int CODE_GROUP_DELETE = 3;
	
	public static final int CODE_THREAD_GROUP_INSERT = 4;
	public static final int CODE_THREAD_GROUP = 5;
	public static final int CODE_THREAD_GROUP_UPDATE = 6;
	public static final int CODE_THREAD_GROUP_DELETE = 7;
	

	static{
		matcher.addURI(authority, "groups/insert", CODE_GROUP_INSERT);
		matcher.addURI(authority, "groups", CODE_GROUP);
		matcher.addURI(authority, "groups/update", CODE_GROUP_UPDATE);
		matcher.addURI(authority, "groups/delete", CODE_GROUP_DELETE);
		
		matcher.addURI(authority, "thread_groups/insert", CODE_THREAD_GROUP_INSERT);
		matcher.addURI(authority, "thread_groups", CODE_THREAD_GROUP);
		matcher.addURI(authority, "thread_groups/update", CODE_THREAD_GROUP_UPDATE);
		matcher.addURI(authority, "thread_groups/delete", CODE_THREAD_GROUP_DELETE);
	}
	@Override
	public boolean onCreate() {
		helper = GroupDatabaseOpenHelper.getInstance(getContext());
		database = helper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		switch (matcher.match(uri)) {
		case CODE_GROUP:
			cursor = database.query("groups", projection, selection, selectionArgs, null, null, sortOrder);
			break;
		case CODE_THREAD_GROUP:
			cursor = database.query(TABLE_THREAD_GROUP, projection, selection, selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("无法识别的uri:"+uri);
		}
		cursor.setNotificationUri(getContext().getContentResolver(), BASE_URI);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// 插入！
		Uri u = null;
		switch (matcher.match(uri)) {
		case CODE_GROUP_INSERT:
			long rowId = database.insert("groups", null, values);
			if (rowId == -1) 
				return null;
			else
				u = Uri.parse("content://"+authority+"/groups/"+rowId);
			break;
		case CODE_THREAD_GROUP_INSERT:
			rowId = database.insert(TABLE_THREAD_GROUP, null, values);
			if(rowId == -1)
				return null;
			else
				u = Uri.withAppendedPath(BASE_URI, "/thread_group/"+rowId);
			break;
		default:
			throw new IllegalArgumentException("无法识别的Uri:"+uri);
		}
		getContext().getContentResolver().notifyChange(BASE_URI, null);
		return u;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		//删除
		int num = 0;
		switch (matcher.match(uri)) {
		case CODE_GROUP_DELETE:
			num = database.delete("groups", selection, selectionArgs);
			break;
		case CODE_THREAD_GROUP_DELETE:
			num = database.delete(TABLE_THREAD_GROUP, selection, selectionArgs);	
			break;
		default:
			throw new IllegalArgumentException("无法识别的uri"+uri);
		}
		getContext().getContentResolver().notifyChange(BASE_URI, null);
		return num;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		//改动
		int num;
		switch (matcher.match(uri)) {
		case CODE_GROUP_UPDATE:
			num=database.update("groups", values, selection, selectionArgs);
			break;
		case CODE_THREAD_GROUP_UPDATE:
			num = database.update(TABLE_THREAD_GROUP, values, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("无法识别的Uri"+uri);
		}
		getContext().getContentResolver().notifyChange(BASE_URI, null);
		return num;
	}
}
