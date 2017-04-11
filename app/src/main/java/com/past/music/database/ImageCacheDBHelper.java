package com.past.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.past.music.database.entity.ImageCache;

/**
 * Created by gaojin on 2017/4/11.
 */

public class ImageCacheDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "ImageCache.db";
    public static final String DB_TABLE = "ImageCacheTable";
    private static final int DB_VERSION = 1;

    private static final String DB_CREATE = "create table " + DB_TABLE +
            " (" + ImageCache.ID + " integer primary key autoincrement, " +
            ImageCache.NAME + " text not null, " +
            ImageCache.URL + " text not null);";

    public ImageCacheDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
        onCreate(db);
    }
}
