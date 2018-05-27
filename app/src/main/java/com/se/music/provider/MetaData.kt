package com.se.music.provider

import android.net.Uri

/**
 *Author: gaojin
 *Time: 2018/5/27 下午2:19
 */

const val DATABASE_NAME = "SEMusic.db"
const val DATABASE_VERSION = 1

object SongList {
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

object LocalMusic {
    //歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
    val START_FROM_ARTIST = 1
    val START_FROM_ALBUM = 2
    val START_FROM_LOCAL = 3
    val START_FROM_FOLDER = 4
    val START_FROM_FAVORITE = 5
}