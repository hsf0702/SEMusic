package com.past.music.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.past.music.database.ImageCacheDBHelper;
import com.past.music.database.entity.ImageCache;

/**
 * Created by gaojin on 2017/4/11.
 */

public class DBService {
    ImageCacheDBHelper imageCacheDBHelper;

    public DBService(Context context) {
        imageCacheDBHelper = new ImageCacheDBHelper(context);
    }

    public synchronized void insert(String artistName, String imageUrl) {
        SQLiteDatabase db = imageCacheDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ImageCache.NAME, artistName);
        contentValues.put(ImageCache.URL, imageUrl);
        db.insert(ImageCacheDBHelper.DB_TABLE, null, contentValues);
    }

    public synchronized String query(String artistName) {
        SQLiteDatabase db = imageCacheDBHelper.getReadableDatabase();
        Cursor cursor = db.query(ImageCacheDBHelper.DB_TABLE, new String[]{ImageCache.URL}, ImageCache.NAME + "=?", new String[]{artistName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else {
            return null;
        }
    }
}
