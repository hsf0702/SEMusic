package com.past.music.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.past.music.database.PastMusicDBHelper;
import com.past.music.database.entity.ImageCache;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/28 15:39
 * 描述：
 * 备注：
 * =======================================================
 */

public class DBService {
    PastMusicDBHelper pastMusicDBHelper;

    public DBService(Context context) {
        pastMusicDBHelper = new PastMusicDBHelper(context);
    }

    public synchronized void insert(String artistName, String imageUrl) {
        SQLiteDatabase db = pastMusicDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ImageCache.NAME, artistName);
        contentValues.put(ImageCache.URL, imageUrl);
        db.insert(PastMusicDBHelper.DB_TABLE, null, contentValues);
    }

    public synchronized String query(String artistName) {
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        Cursor cursor = db.query(PastMusicDBHelper.DB_TABLE, new String[]{ImageCache.URL}, ImageCache.NAME + "=?", new String[]{artistName}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(0);
        } else {
            return null;
        }
    }
}
