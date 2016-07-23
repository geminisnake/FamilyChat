package com.alien.sms.dao;

import com.alien.sms.global.Constant;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

public class GroupDao {
	public static void updateGroupName(ContentResolver cr,String name,int _id){
		ContentValues values = new ContentValues();
		values.put("name", name);
		cr.update(Constant.URI.GROUP_UPDATE, values, "_id ="+_id, null);
	}
	public static void insertGroup(ContentResolver cr,String name){
		ContentValues values =new ContentValues();
		values.put("name", name);
		values.put("thread_count", 0);
		values.put("create_date", System.currentTimeMillis());
		cr.insert(Constant.URI.GROUP_INSERT, values);
	}
	public static void deleteGroup(ContentResolver cr,int _id){
		cr.delete(Constant.URI.GROUP_DELETE, "_id = "+_id, null);
		ThreadGroupDao.deleteThreadGroupRelation(cr,_id);
	}
	public static boolean hasGroup(ContentResolver cr){
		return cr.query(Constant.URI.GROUP, null, null, null, null).moveToFirst();
	}

	public static Cursor getGroupCursor(ContentResolver contentResolver) {
		// TODO Auto-generated method stub
		return contentResolver.query(Constant.URI.GROUP, new String[]{"_id","name"}, null, null, "name");
	}
	public static int getThreadCount(ContentResolver cr,int group_id){
		Cursor cursor = cr.query(Constant.URI.GROUP, new String[]{"thread_count"}, "_id = "+group_id, null, null);
		if(cursor.moveToFirst())
			return cursor.getInt(0);
		else
			throw new RuntimeException("查询不到该行数据，请校验传入的数group_id:"+group_id+"是否正确");
	}
	public static void changeThreadCount(ContentResolver cr, int group_id, int newCount){
		ContentValues values = new ContentValues();
		values.put("thread_count", newCount);
		cr.update(Constant.URI.GROUP_UPDATE, values, "_id = "+group_id, null);
	}
}
