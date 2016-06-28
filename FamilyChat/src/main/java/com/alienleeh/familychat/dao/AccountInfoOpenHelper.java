package com.alienleeh.familychat.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AlienLeeH on 2016/6/17.
 */
public class AccountInfoOpenHelper extends SQLiteOpenHelper{
    private static AccountInfoOpenHelper instance;
    public static AccountInfoOpenHelper getInstance(Context context){
        if (instance == null)
            instance = new AccountInfoOpenHelper(context,"accountinfo.db",null,1);
        return instance;
    }
    private AccountInfoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table UserInfo(" +
            "_id integer primary key autoincrement," +
                "account_id varchar," +
                "password varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
