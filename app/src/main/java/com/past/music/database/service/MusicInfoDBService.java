package com.past.music.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.past.music.database.PastMusicDBHelper;
import com.past.music.database.entity.MusicInfoCache;
import com.past.music.entity.MusicEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/15 16:58
 * 描述：
 * 备注：
 * =======================================================
 */
public class MusicInfoDBService {
    PastMusicDBHelper pastMusicDBHelper;

    public MusicInfoDBService(Context context) {
        pastMusicDBHelper = new PastMusicDBHelper(context);
    }

    public synchronized void insert(MusicEntity musicEntity, String songListId) {
        SQLiteDatabase db = pastMusicDBHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicInfoCache.ID, UUID.randomUUID().toString());
        contentValues.put(MusicInfoCache.NAME, musicEntity.getMusicName());
        contentValues.put(MusicInfoCache.ALBUM_ID, musicEntity.getAlbumId());
        contentValues.put(MusicInfoCache.ALBUM_NAME, musicEntity.getAlbumName());
        contentValues.put(MusicInfoCache.ALBUM_PIC, musicEntity.getAlbumPic());
        contentValues.put(MusicInfoCache.ARTIST_ID, musicEntity.getArtistId());
        contentValues.put(MusicInfoCache.ARTIST_NAME, musicEntity.getArtist());
        contentValues.put(MusicInfoCache.SONG_ID, musicEntity.getSongId());
        if (musicEntity.islocal()) {
            contentValues.put(MusicInfoCache.IS_LOCAL, 0);
        } else {
            contentValues.put(MusicInfoCache.IS_LOCAL, 1);
        }
        contentValues.put(MusicInfoCache.SONG_LIST_ID, songListId);
        db.insert(PastMusicDBHelper.MUSICINFO_TABLE, null, contentValues);
    }


    public synchronized List<MusicEntity> query(String songListId) {
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        List<MusicEntity> list = new ArrayList<>();
        String sql = "select * from " + PastMusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            MusicEntity musicEntity = new MusicEntity();
            musicEntity.setSongId(Long.parseLong(cursor.getString(0)));
            musicEntity.setMusicName(cursor.getString(2));
            musicEntity.setAlbumId(cursor.getInt(3));
            musicEntity.setAlbumName(cursor.getString(4));
            musicEntity.setAlbumPic(cursor.getString(5));
            musicEntity.setArtistId(Long.parseLong(cursor.getString(6)));
            musicEntity.setArtist(cursor.getString(7));
            int a = cursor.getInt(9);
            if (a == 0) {
                musicEntity.setIslocal(true);
            } else {
                musicEntity.setIslocal(false);
            }
            list.add(musicEntity);
        }
        while (cursor.moveToNext()) {
            MusicEntity musicEntity = new MusicEntity();
            musicEntity.setSongId(Long.parseLong(cursor.getString(0)));
            musicEntity.setMusicName(cursor.getString(2));
            musicEntity.setAlbumId(cursor.getInt(3));
            musicEntity.setAlbumName(cursor.getString(4));
            musicEntity.setAlbumPic(cursor.getString(5));
            musicEntity.setArtistId(Long.parseLong(cursor.getString(6)));
            musicEntity.setArtist(cursor.getString(7));
            int a = cursor.getInt(9);
            if (a == 0) {
                musicEntity.setIslocal(true);
            } else {
                musicEntity.setIslocal(false);
            }
            list.add(musicEntity);
        }
        return list;
    }

    public synchronized String haveSong(String songListId) {
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        List<MusicEntity> list = new ArrayList<>();
        String sql = "select * from " + PastMusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getString(5) == null) {
                return "empty";
            } else {
                return cursor.getString(5);
            }

        } else {
            return null;
        }
    }

    public synchronized MusicEntity firstEntity(String songListId) {
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        String sql = "select * from " + PastMusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'";
        MusicEntity musicEntity = null;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            musicEntity = new MusicEntity();
            musicEntity.setSongId(Long.parseLong(cursor.getString(0)));
            musicEntity.setMusicName(cursor.getString(2));
            musicEntity.setAlbumId(cursor.getInt(3));
            musicEntity.setAlbumName(cursor.getString(4));
            musicEntity.setAlbumPic(cursor.getString(5));
            musicEntity.setArtistId(Long.parseLong(cursor.getString(6)));
            musicEntity.setArtist(cursor.getString(7));
            int a = cursor.getInt(9);
            if (a == 0) {
                musicEntity.setIslocal(true);
            } else {
                musicEntity.setIslocal(false);
            }
        }
        return musicEntity;
    }


    public synchronized String getLocalCount(String songListId) {
        int total = 0;
        int local = 0;
        SQLiteDatabase db = pastMusicDBHelper.getReadableDatabase();
        List<MusicEntity> list = new ArrayList<>();
        String sql = "select * from " + PastMusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            total = cursor.getCount();
            cursor.moveToFirst();
            int a = cursor.getInt(9);
            if (a == 0) {
                local++;
            }
        }

        while (cursor.moveToNext()) {
            int a = cursor.getInt(9);
            if (a == 0) {
                local++;
            }
        }
        return total + "首，" + local + "首已下载";
    }
}
