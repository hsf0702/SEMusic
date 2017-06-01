package com.past.music.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.past.music.database.PastMusicDBHelper;
import com.past.music.database.entity.SongListCache;
import com.past.music.entity.SongListEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/15 16:57
 * 描述：
 * 备注：
 * =======================================================
 */
public class SongListDBService {
    PastMusicDBHelper pastMusicDBHelper;

    private static final String[] PROJECTION = new String[]{
            SongListCache.ID, SongListCache.NAME,
            SongListCache.COUNT, SongListCache.CREATOR,
            SongListCache.CREATE_TIME, SongListCache.LIST_PIC,
            SongListCache.INFO
    };

    public SongListDBService(Context context) {
        pastMusicDBHelper = new PastMusicDBHelper(context);
    }

    public synchronized void insert(String songListName, int count, String creator, String pic_url, String info) {
        SQLiteDatabase db = pastMusicDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongListCache.ID, UUID.randomUUID().toString());
        contentValues.put(SongListCache.NAME, songListName);
        contentValues.put(SongListCache.COUNT, count);
        contentValues.put(SongListCache.CREATOR, creator);
        contentValues.put(SongListCache.CREATE_TIME, System.currentTimeMillis());
        contentValues.put(SongListCache.LIST_PIC, pic_url);
        contentValues.put(SongListCache.INFO, info);
        db.insert(PastMusicDBHelper.SONGLIST_TABLE, null, contentValues);
    }

    public synchronized void insert(String songListName, String info) {
        SQLiteDatabase db = pastMusicDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SongListCache.ID, UUID.randomUUID().toString());
        contentValues.put(SongListCache.NAME, songListName);
        contentValues.put(SongListCache.CREATE_TIME, System.currentTimeMillis());
        contentValues.put(SongListCache.INFO, info);
        db.insert(PastMusicDBHelper.SONGLIST_TABLE, null, contentValues);
    }

    public synchronized void updatePic(String songListId, String pic) {
        SQLiteDatabase db = pastMusicDBHelper.getWritableDatabase();
        String sql = "update " + PastMusicDBHelper.SONGLIST_TABLE + " set " + SongListCache.LIST_PIC + " = '" + pic + "' where " + SongListCache.ID + " = '" + songListId + "'";
        db.execSQL(sql);
    }


    public synchronized List<SongListEntity> query() {
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        List<SongListEntity> list = new ArrayList<>();
        Cursor cursor = db.query(PastMusicDBHelper.SONGLIST_TABLE, null, null, null,
                null, null, SongListCache.CREATE_TIME);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            SongListEntity songListEntity = new SongListEntity();
            songListEntity.setId(cursor.getString(0));
            songListEntity.setName(cursor.getString(1));
            songListEntity.setCount(cursor.getInt(2));
            songListEntity.setCreator(cursor.getString(3));
            songListEntity.setCreate_time(cursor.getString(4));
            songListEntity.setList_pic(cursor.getString(5));
            songListEntity.setInfo(cursor.getString(6));
            list.add(songListEntity);
        }
        while (cursor.moveToNext()) {
            SongListEntity songListEntity = new SongListEntity();
            songListEntity.setId(cursor.getString(0));
            songListEntity.setName(cursor.getString(1));
            songListEntity.setCount(cursor.getInt(2));
            songListEntity.setCreator(cursor.getString(3));
            songListEntity.setCreate_time(cursor.getString(4));
            songListEntity.setList_pic(cursor.getString(5));
            songListEntity.setInfo(cursor.getString(6));
            list.add(songListEntity);
        }
        return list;
    }
}
