package com.se.music.provider

import android.net.Uri

/**
 *Author: gaojin
 *Time: 2018/5/19 下午10:51
 */

class MetaData {
    companion object {
        val DATABASE_NAME = "SEMusic.db"
        val DATABASE_VERSION = 1
    }

    class SongList {
        companion object {
            val AUTHORITIES = "com.se.music.SongListContentProvider"
            val TABLE_NAME = "SongListTable"
            val CONTENT_URI = Uri.parse("content://$AUTHORITIES/$TABLE_NAME")

            /*Data Field*/
            val ID = "_id"
            val NAME = "_name"
            val COUNT = "_count"
            val CREATOR = "_creator"
            val CREATE_TIME = "_create_time"
            val PIC = "_pic"
            val INFO = "_info"

            /*Index*/
            val ID_INDEX = 0
            val NAME_INDEX = 1
            val COUNT_INDEX = 2
            val CREATOR_INDEX = 3
            val CREATE_TIME_INDEX = 4
            val PIC_INDEX = 5
            val INFO_INDEX = 6
        }
    }
}