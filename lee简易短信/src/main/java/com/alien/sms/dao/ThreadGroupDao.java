package com.alien.sms.dao;

import com.alien.sms.global.Constant;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class ThreadGroupDao {
	public static boolean isInGroup(ContentResolver cr,int thread_id) {
		return cr.query(Constant.URI.THREAD_GROUP, null, "thread_id = "+thread_id, null, null).moveToNext();
	}
	public static String getGroupByThreadId(ContentResolver cr,int thread_id){
		Cursor cursor = cr.query(Constant.URI.THREAD_GROUP, new String[]{"group_id"}, "thread_id = "+thread_id, null, null);
		cursor.moveToFirst();
		int group_id = cursor.getInt(0);
		cursor.close();
		Cursor cursor2 = cr.query(Constant.URI.GROUP, new String[]{"name"}, "_id = "+group_id, null, null);
		if(!cursor2.moveToFirst()){
			deleteThreadGroupRelation(cr, group_id);
			return null;
		}else{
			String group_name = cursor2.getString(0);
			cursor2.close();
			return group_name;
		}
	}
	public static boolean deleteInGroup(ContentResolver cr,int thread_id){
		//查询获得group——id以便更改thread_count。
		Cursor cursor = cr.query(Constant.URI.THREAD_GROUP, null, "thread_id = "+thread_id, null, null);
		cursor.moveToFirst();
		int group_id = cursor.getInt(1);
		cursor.close();
		
		int num = cr.delete(Constant.URI.THREAD_GROUP_DELETE, "thread_id = "+thread_id, null);
		boolean isSuccess = (num != 0);
		if(isSuccess){
			GroupDao.changeThreadCount(cr , group_id , GroupDao.getThreadCount(cr, group_id)-1);
		}
		return isSuccess;
	}
	
	public static boolean insertThead(ContentResolver cr, int thread_id, int group_id) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues();
		values.put("group_id", group_id);
		values.put("thread_id", thread_id);
		Uri uri = cr.insert(Constant.URI.THREAD_GROUP_INSERT, values);
		boolean isSuccess = (uri != null);
		if (isSuccess) {
			GroupDao.changeThreadCount(cr, group_id, GroupDao.getThreadCount(cr, group_id)+1);
		}
		return isSuccess;
	}
	public static void deleteThreadGroupRelation(ContentResolver cr, int group_id) {
		// TODO Auto-generated method stub
		cr.delete(Constant.URI.THREAD_GROUP_DELETE, "group_id ="+group_id, null);
	}
	public static Cursor findAllThreadByGroup(ContentResolver cr, int groupId) {
		// TODO Auto-generated method stub
		return cr.query(Constant.URI.THREAD_GROUP, new String[]{"thread_id"}, "group_id = "+groupId, null, null);
	}
}
