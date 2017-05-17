package com.past.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.past.music.database.entity.ImageCache;
import com.past.music.database.entity.MusicInfoCache;
import com.past.music.database.entity.SongListCache;

/**
 * Created by gaojin on 2017/4/11.
 */

public class PastMusicDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "PastMusic.db";
    public static final String IMAGE_TABLE = "ImageCacheTable";
    public static final String SONGLIST_TABLE = "SongListTable";
    public static final String MUSICINFO_TABLE = "MusicInfoTable";
    private static final int DB_VERSION = 1;

    private static final String IMAGE_TABLE_CREATE = "create table " + IMAGE_TABLE +
            " (" + ImageCache.ID + " integer primary key autoincrement, " +
            ImageCache.NAME + " text not null, " +
            ImageCache.URL + " text not null);";

    public static final String SONGLIST_TABLE_CREATE = "create table " + SONGLIST_TABLE +
            " (" + SongListCache.ID + " varchar(128), " +
            SongListCache.NAME + " varchar(50) NOT NULL," +
            SongListCache.COUNT + " int," +
            SongListCache.CREATOR + " varchar(20)," +
            SongListCache.CREATE_TIME + " varchar(30) NOT NULL," +
            SongListCache.LIST_PIC + " varchar(50)," +
            SongListCache.INFO + " varchar(50)," +
            "PRIMARY KEY (" + SongListCache.ID + ")" +
            ");";

    public static final String MUSICINFO_TABLE_CREATE = "create table " + MUSICINFO_TABLE +
            " (" + MusicInfoCache.ID + " varchar(128), " +
            MusicInfoCache.SONG_LIST_ID + " varchar(50) NOT NULL," +
            MusicInfoCache.NAME + " varchar(50) NOT NULL," +
            MusicInfoCache.ALBUM_ID + " varchar(20)," +
            MusicInfoCache.ALBUM_NAME + " varchar(20)," +
            MusicInfoCache.ALBUM_PIC + " varchar(50)," +
            MusicInfoCache.ARTIST_ID + " varchar(20) NOT NULL," +
            MusicInfoCache.ARTIST_NAME + " varchar(10) NOT NULL," +
            MusicInfoCache.PATH + " varchar(20)," +
            MusicInfoCache.IS_LOCAL + " int," +
            "PRIMARY KEY (" + MusicInfoCache.ID + ")" +
            ");";

    public PastMusicDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(IMAGE_TABLE_CREATE);
        db.execSQL(SONGLIST_TABLE_CREATE);
        db.execSQL(MUSICINFO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SONGLIST_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + MUSICINFO_TABLE);
        onCreate(db);
    }
}
