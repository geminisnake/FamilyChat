package com.alien.sms.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class GroupDatabaseOpenHelper extends SQLiteOpenHelper {
	private static GroupDatabaseOpenHelper instance;
	public static GroupDatabaseOpenHelper getInstance(Context context){
		if (instance == null) {
			instance = new GroupDatabaseOpenHelper(context, "group.db", null, 1);
		}
		return instance;
	}
	private GroupDatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table groups(" +
				"_id integer primary key autoincrement,"+
				"name varchar," +
				"create_date integer," +
				"thread_count integer)");
		db.execSQL("create table thread_group(" +
				"_id integer primary key autoincrement," +
				"group_id integer," +
				"thread_id integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}
