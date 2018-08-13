package com.example.capture.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库 流量存储
 */
public class SQL  extends SQLiteOpenHelper {

    public SQL(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //数据流量，手机应用表
        String traffic="create table  IF NOT EXISTS traffic(name text primary key , Rx Integer,RxCompany text ,Tx Integer, TxCompany text)";
        String perSetting="create table  IF NOT EXISTS perSetting(name text primary key , per Integer)";
        db.execSQL(traffic);
        db.execSQL(perSetting);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
