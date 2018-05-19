package com.se.music.utils.database

import android.net.Uri

/**
 *Author: gaojin
 *Time: 2018/5/19 下午10:51
 */

class DataBaseMetaData {
    companion object {
        val DATABASE_NAME = "SEMusic.db"
        val DATABASE_VERSION = 1
    }

    class SongList {
        companion object {
            val AUTHORITIES = "com.se.music.SongListContentProvider"
            val TABLE_NAME = "SongListTable"
            val CONTENT_URI = Uri.parse("content://$AUTHORITIES/$TABLE_NAME")
        }
    }
}