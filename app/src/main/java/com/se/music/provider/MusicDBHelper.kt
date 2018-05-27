package com.se.music.provider

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.se.music.provider.metadata.*
import com.se.music.utils.database.entity.MusicInfoCache
import com.se.music.utils.database.provider.RecentStore
import com.se.music.utils.database.provider.SearchHistory
import com.se.music.utils.singleton.ApplicationSingleton

/**
 * Author: gaojin
 * Time: 2018/5/6 下午1:52
 */
class MusicDBHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        const val SONGLIST_TABLE = "SongListTable"
        const val MUSICINFO_TABLE = "MusicInfoTable"

        val instance: MusicDBHelper by lazy { MusicDBHelper() }
    }

    private val SONGLIST_TABLE_CREATE = "create table " + SONGLIST_TABLE +
            " (" + SL_ID + " varchar(128), " +
            SL_NAME + " varchar(50) NOT NULL," +
            SL_COUNT + " int," +
            SL_CREATOR + " varchar(20)," +
            SL_CREATE_TIME + " varchar(30) NOT NULL," +
            SL_PIC + " varchar(50)," +
            SL_INFO + " varchar(50)," +
            "PRIMARY KEY (" + SL_ID + ")" +
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

    constructor() : this(ApplicationSingleton.instance!!.applicationContext, DATABASE_NAME, null, DATABASE_VERSION)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SONGLIST_TABLE_CREATE)
        db.execSQL(MUSICINFO_TABLE_CREATE)
        RecentStore.instance.onCreate(db)
        SearchHistory.instance.onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $SONGLIST_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $MUSICINFO_TABLE")
        RecentStore.instance.onUpgrade(db, oldVersion, newVersion)
        SearchHistory.instance.onUpgrade(db, oldVersion, newVersion)
        onCreate(db)
    }
}