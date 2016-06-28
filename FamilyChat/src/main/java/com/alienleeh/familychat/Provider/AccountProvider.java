package com.alienleeh.familychat.Provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.alienleeh.familychat.dao.AccountInfoOpenHelper;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class AccountProvider extends ContentProvider{
    private AccountInfoOpenHelper helper;
    private SQLiteDatabase database;
    private static final String AUTHORITY = "com.alienleeh.familychat.provider";
    public static final Uri BASE_URI = Uri.parse("content://"+AUTHORITY);

    public static final int USERINFO_QUERY = 0;
    public static final int USERINFO_INSERT = 1;
    public static final int USERINFO_UPDATE = 2;
    public static final int USERINFO_DELETE = 3;

    private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        matcher.addURI(AUTHORITY,"userinfo/query",USERINFO_QUERY);
        matcher.addURI(AUTHORITY,"userinfo/insert",USERINFO_INSERT);
        matcher.addURI(AUTHORITY,"userinfo/update",USERINFO_UPDATE);
        matcher.addURI(AUTHORITY,"userinfo/delete",USERINFO_DELETE);
    }
    public boolean onCreate() {
        helper = AccountInfoOpenHelper.getInstance(getContext());
        database = helper.getWritableDatabase();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = null;
        switch (matcher.match(uri)){
            case USERINFO_QUERY:
                cursor = database.query("UserInfo",projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("无法识别的Uri"+uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri uriReturn = null;
        switch (matcher.match(uri)){
            case USERINFO_INSERT:
                long rowId = database.insert("UserInfo",null,values);
                uriReturn = Uri.withAppendedPath(BASE_URI,"/userinfo/"+rowId);
                break;
        }
        return uriReturn;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)){
            case USERINFO_DELETE:
                num = database.delete("UserInfo",selection,selectionArgs);
                break;
        }
        return num;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int num = 0;
        switch (matcher.match(uri)){
            case USERINFO_UPDATE:
                num = database.update("UserInfo",values,selection,selectionArgs);
                break;
        }
        return 0;
    }
}
