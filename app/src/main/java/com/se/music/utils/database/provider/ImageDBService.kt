package com.se.music.utils.database.provider

import android.content.ContentValues
import com.se.music.utils.database.MusicDBHelper
import com.se.music.utils.database.entity.ImageCache

/**
 * Author: gaojin
 * Time: 2018/5/6 下午2:57
 */
class ImageDBService {
    companion object {
        val instance: ImageDBService by lazy { ImageDBService() }
    }

    @Synchronized
    fun insert(artistName: String, imageUrl: String) {
        val db = MusicDBHelper.instance.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ImageCache.NAME, artistName)
        contentValues.put(ImageCache.URL, imageUrl)
        db.insert(MusicDBHelper.IMAGE_TABLE, null, contentValues)
    }

    @Synchronized
    fun query(artistName: String): String? {
        val db = MusicDBHelper.instance.readableDatabase
        val cursor = db.query(MusicDBHelper.IMAGE_TABLE, arrayOf(ImageCache.URL), ImageCache.NAME + "=?", arrayOf(artistName), null, null, null)
        return if (cursor.count > 0) {
            cursor.moveToFirst()
            cursor.getString(0)
        } else {
            null
        }
    }
}