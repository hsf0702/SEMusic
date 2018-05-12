package com.se.music.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.se.music.database.entity.ImageCache
import com.se.music.database.entity.MusicInfoCache
import com.se.music.database.entity.SongListCache
import com.se.music.database.provider.RecentStore
import com.se.music.database.provider.SearchHistory
import com.se.music.singleton.ApplicationSingleton

/**
 * Author: gaojin
 * Time: 2018/5/6 下午1:52
 */
class MusicDBHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    companion object {
        const val DB_NAME = "PastMusic.db"
        const val IMAGE_TABLE = "ImageCacheTable"
        const val SONGLIST_TABLE = "SongListTable"
        const val MUSICINFO_TABLE = "MusicInfoTable"
        private const val DB_VERSION = 1

        val instance: MusicDBHelper by lazy { MusicDBHelper() }
    }

    private val IMAGE_TABLE_CREATE = "create table " + IMAGE_TABLE +
            " (" + ImageCache.ID + " integer primary key autoincrement, " +
            ImageCache.NAME + " text not null, " +
            ImageCache.URL + " text not null);"

    private val SONGLIST_TABLE_CREATE = "create table " + SONGLIST_TABLE +
            " (" + SongListCache.ID + " varchar(128), " +
            SongListCache.NAME + " varchar(50) NOT NULL," +
            SongListCache.COUNT + " int," +
            SongListCache.CREATOR + " varchar(20)," +
            SongListCache.CREATE_TIME + " varchar(30) NOT NULL," +
            SongListCache.LIST_PIC + " varchar(50)," +
            SongListCache.INFO + " varchar(50)," +
            "PRIMARY KEY (" + SongListCache.ID + ")" +
            ");"

    private val MUSICINFO_TABLE_CREATE = "create table " + MUSICINFO_TABLE +
            " (" + MusicInfoCache.SONG_ID + " varchar(128), " +
            MusicInfoCache.SONG_LIST_ID + " varchar(50) NOT NULL," +
            MusicInfoCache.NAME + " varchar(50) NOT NULL," +
            MusicInfoCache.ALBUM_ID + " varchar(20)," +
            MusicInfoCache.ALBUM_NAME + " varchar(20)," +
            MusicInfoCache.ALBUM_PIC + " varchar(50)," +
            MusicInfoCache.ARTIST_ID + " varchar(20) NOT NULL," +
            MusicInfoCache.ARTIST_NAME + " varchar(10) NOT NULL," +
            MusicInfoCache.PATH + " varchar(20)," +
            MusicInfoCache.IS_LOCAL + " int," +
            MusicInfoCache.ID + " varchar(128)," +
            MusicInfoCache.IS_LOVE + " int," +
            "PRIMARY KEY (" + MusicInfoCache.ID + ")" +
            ");"

    constructor() : this(ApplicationSingleton.instance!!.applicationContext, DB_NAME, null, DB_VERSION)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(IMAGE_TABLE_CREATE)
        db.execSQL(SONGLIST_TABLE_CREATE)
        db.execSQL(MUSICINFO_TABLE_CREATE)
        RecentStore.instance.onCreate(db)
        SearchHistory.instance.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $IMAGE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SONGLIST_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $MUSICINFO_TABLE")
        RecentStore.instance.onUpgrade(db, oldVersion, newVersion)
        SearchHistory.instance.onUpgrade(db, oldVersion, newVersion)
        onCreate(db)
    }
}