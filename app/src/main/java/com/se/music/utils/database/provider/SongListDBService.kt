package com.se.music.utils.database.provider

import android.content.ContentValues
import com.se.music.common.entity.SongListEntity
import com.se.music.utils.database.MusicDBHelper
import com.se.music.utils.database.entity.SongListCache
import java.util.*

/**
 * Author: gaojin
 * Time: 2018/5/6 下午3:02
 */
class SongListDBService {

    companion object {
        val instance: SongListDBService by lazy { SongListDBService() }
    }

    @Synchronized
    fun insert(songListName: String, count: Int, creator: String, pic_url: String, info: String) {
        val db = MusicDBHelper.instance.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SongListCache.ID, UUID.randomUUID().toString())
        contentValues.put(SongListCache.NAME, songListName)
        contentValues.put(SongListCache.COUNT, count)
        contentValues.put(SongListCache.CREATOR, creator)
        contentValues.put(SongListCache.CREATE_TIME, System.currentTimeMillis())
        contentValues.put(SongListCache.LIST_PIC, pic_url)
        contentValues.put(SongListCache.INFO, info)
        db.insert(MusicDBHelper.SONGLIST_TABLE, null, contentValues)
    }

    @Synchronized
    fun insert(songListName: String, info: String) {
        val db = MusicDBHelper.instance.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SongListCache.ID, UUID.randomUUID().toString())
        contentValues.put(SongListCache.NAME, songListName)
        contentValues.put(SongListCache.CREATE_TIME, System.currentTimeMillis())
        contentValues.put(SongListCache.INFO, info)
        db.insert(MusicDBHelper.SONGLIST_TABLE, null, contentValues)
    }

    @Synchronized
    fun updatePic(songListId: String, pic: String) {
        val db = MusicDBHelper.instance.writableDatabase
        val sql = "update " + MusicDBHelper.SONGLIST_TABLE + " set " + SongListCache.LIST_PIC + " = '" + pic + "' where " + SongListCache.ID + " = '" + songListId + "'"
        db.execSQL(sql)
    }


    @Synchronized
    fun query(): List<SongListEntity> {
        val db = MusicDBHelper.instance.readableDatabase
        val list = ArrayList<SongListEntity>()
        val cursor = db.query(MusicDBHelper.SONGLIST_TABLE, null, null, null, null, null, SongListCache.CREATE_TIME)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val songListEntity = SongListEntity()
            songListEntity.id = cursor.getString(0)
            songListEntity.name = cursor.getString(1)
            songListEntity.count = cursor.getInt(2)
            songListEntity.creator = cursor.getString(3)
            songListEntity.createTime = cursor.getString(4)
            songListEntity.listPic = cursor.getString(5)
            songListEntity.info = cursor.getString(6)
            list.add(songListEntity)
        }
        while (cursor.moveToNext()) {
            val songListEntity = SongListEntity()
            songListEntity.id = cursor.getString(0)
            songListEntity.name = cursor.getString(1)
            songListEntity.count = cursor.getInt(2)
            songListEntity.creator = cursor.getString(3)
            songListEntity.createTime = cursor.getString(4)
            songListEntity.listPic = cursor.getString(5)
            songListEntity.info = cursor.getString(6)
            list.add(songListEntity)
        }
        return list
    }
}