package com.jikexueyuan.todaydolt;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by dej on 2016/9/25.
 */
public class EventProvider extends ContentProvider {

    // 设置一个Uri
    public static final Uri URI = Uri.parse("content://com.jikexueyuan.eventprovider");
    // 声明一个SQLiteDatabase来作为存储事件的数据对象
    SQLiteDatabase dbEvent;

    @Override
    public boolean onCreate() {
        dbEvent = getContext().openOrCreateDatabase("myenents.db3", Context.MODE_PRIVATE, null);
        dbEvent.execSQL("CREATE TABLE IF NOT EXISTS events("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "time TEXT NOT NULL,"
                + "content TEXT NOT NULL"
                + ")");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = dbEvent.query("events",null,null,null,null,null,null);
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
        dbEvent.insert("events", "_id", values);
        getContext().getContentResolver().notifyChange(URI, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        dbEvent.delete("events", "_id=?", selectionArgs);
        getContext().getContentResolver().notifyChange(URI, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
