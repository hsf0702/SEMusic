package com.se.music.utils.database.provider

import android.content.ContentValues
import com.se.music.common.MusicEntity
import com.se.music.provider.MusicDBHelper
import com.se.music.utils.database.entity.MusicInfoCache
import java.util.*

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:59
 */
class MusicInfoDBService {

    companion object {
        val instance: MusicInfoDBService by lazy { MusicInfoDBService() }
    }

    @Synchronized
    fun insert(musicEntity: MusicEntity, songListId: String) {
        val db = MusicDBHelper.instance.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MusicInfoCache.ID, UUID.randomUUID().toString())
        contentValues.put(MusicInfoCache.NAME, musicEntity.musicName)
        contentValues.put(MusicInfoCache.ALBUM_ID, musicEntity.albumId)
        contentValues.put(MusicInfoCache.ALBUM_NAME, musicEntity.albumName)
        contentValues.put(MusicInfoCache.ALBUM_PIC, musicEntity.albumPic)
        contentValues.put(MusicInfoCache.ARTIST_ID, musicEntity.artistId)
        contentValues.put(MusicInfoCache.ARTIST_NAME, musicEntity.artist)
        contentValues.put(MusicInfoCache.SONG_ID, musicEntity.songId)
        if (musicEntity.islocal) {
            contentValues.put(MusicInfoCache.IS_LOCAL, 0)
        } else {
            contentValues.put(MusicInfoCache.IS_LOCAL, 1)
        }
        contentValues.put(MusicInfoCache.SONG_LIST_ID, songListId)
        db.insert(MusicDBHelper.MUSICINFO_TABLE, null, contentValues)
    }

    @Synchronized
    fun haveSong(songListId: String): String? {
        val db = MusicDBHelper.instance.readableDatabase
        val sql = "select * from " + MusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'"
        val cursor = db.rawQuery(sql, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            return if (cursor.getString(5) == null) {
                "empty"
            } else {
                cursor.getString(5)
            }

        } else {
            return null
        }
    }


    @Synchronized
    fun getLocalCount(songListId: String): String {
        var total = 0
        var local = 0
        val db = MusicDBHelper.instance.readableDatabase
        val sql = "select * from " + MusicDBHelper.MUSICINFO_TABLE + " where " + MusicInfoCache.SONG_LIST_ID + " = '" + songListId + "'"
        val cursor = db.rawQuery(sql, null)
        if (cursor.count > 0) {
            total = cursor.count
            cursor.moveToFirst()
            val a = cursor.getInt(9)
            if (a == 0) {
                local++
            }
        }

        while (cursor.moveToNext()) {
            val a = cursor.getInt(9)
            if (a == 0) {
                local++
            }
        }
        return total.toString() + "首，" + local + "首已下载"
    }
}