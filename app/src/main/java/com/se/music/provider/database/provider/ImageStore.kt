package com.se.music.provider.database.provider

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.se.music.provider.MusicDBHelper

/**
 *Author: gaojin
 *Time: 2018/7/8 下午5:34
 * 图片缓存管理
 */
class ImageStore {
    companion object {
        private const val MAX_ITEMS_IN_DB = 100

        const val IMAGE_KEY = "key"
        const val IMAGE_VALUE = "image_value"
        private const val IMAGE_TABLE_CREATE = "create table " + MusicDBHelper.IMAGE_TABLE +
                " (" + IMAGE_KEY + " varchar(128), " +
                IMAGE_VALUE + " varchar(50)," +
                "PRIMARY KEY (" + IMAGE_KEY + ")" +
                ");"
        val instance: ImageStore by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { ImageStore() }
    }

    fun onCreate(db: SQLiteDatabase) {
        db.execSQL(IMAGE_TABLE_CREATE)
    }

    fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${MusicDBHelper.IMAGE_TABLE}")
    }

    /**
     * 添加图片缓存
     */
    @Synchronized
    fun addImage(key: String, imageValue: String) {
        val database = MusicDBHelper.instance.writableDatabase
        database.beginTransaction()
        try {
            val value = ContentValues(2)
            value.put(IMAGE_KEY, key)
            value.put(IMAGE_VALUE, imageValue)
            database.insert(MusicDBHelper.IMAGE_TABLE, null, value)
            var oldest: Cursor? = null
            try {
                oldest = database.query(MusicDBHelper.IMAGE_TABLE
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null
                        , null)

                if (oldest.count > MAX_ITEMS_IN_DB) {
                    //删除第一个
                    oldest.moveToPosition(0)
                    val cachedKey = oldest.getString(0)
                    database.delete(MusicDBHelper.IMAGE_TABLE, "$IMAGE_KEY=$cachedKey", null)
                }
            } finally {
                oldest?.close()
            }
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
        }
    }

    /**
     * 查询图片缓存
     */
    @Synchronized
    fun query(key: String?): String? {
        val database = MusicDBHelper.instance.writableDatabase
        database.beginTransaction()
        var imageValue: String? = null
        try {
            val sql = "SELECT $IMAGE_VALUE FROM ${MusicDBHelper.IMAGE_TABLE} WHERE key='$key'"
            val cursor = database.rawQuery(sql, null)
            if (cursor.moveToFirst()) {
                imageValue = cursor.getString(0)
            }
            cursor.close()
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
        }
        return imageValue
    }

    /**
     * 删除图片缓存
     */
    @Synchronized
    fun delete(key: String) {
        val database = MusicDBHelper.instance.writableDatabase
        database.beginTransaction()
        try {
            database.delete(MusicDBHelper.IMAGE_TABLE, "$IMAGE_KEY=$key", null)
        } finally {
            database.setTransactionSuccessful()
            database.endTransaction()
        }
    }
}