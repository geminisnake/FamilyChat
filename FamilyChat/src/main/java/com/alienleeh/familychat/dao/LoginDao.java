package com.alienleeh.familychat.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.alienleeh.familychat.Global.Constant;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class LoginDao {
    public static boolean hasBeenRegist(ContentResolver contentResolver, String accountId) {
        Cursor cursor = contentResolver.query(Constant.URI.ACCOUNTINFO_QUERY,null,"account_id = '"+accountId+"'",null,null);
        return cursor != null? cursor.moveToFirst() : false;
    }

    public static boolean registThisAccount(ContentResolver contentResolver, String accountId, String password) {
        ContentValues values = new ContentValues();
        values.put("account_id",accountId);
        values.put("password",password);
        Uri uri = contentResolver.insert(Constant.URI.ACCOUNTINFO_INSERT,values);
        return uri != null;
    }

    public static boolean judgeCorrectPassword(ContentResolver contentResolver, String accountId, String password) {
        Cursor cursor = contentResolver.query(Constant.URI.ACCOUNTINFO_QUERY,new String[]{"password"},"account_id ='"+accountId+"'",null,null);
        if (cursor.moveToFirst())
            return cursor.getString(0).equals(password);
        else
            throw new RuntimeException("错误错误");
    }
}
