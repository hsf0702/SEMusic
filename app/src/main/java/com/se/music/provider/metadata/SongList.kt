package com.se.music.provider.metadata

import android.net.Uri

/**
 *Author: gaojin
 *Time: 2018/5/27 下午6:55
 */

const val SL_AUTHORITIES = "com.se.music.SongListContentProvider"
const val SL_TABLE_NAME = "SongListTable"
val SL_CONTENT_URI = Uri.parse("content://$SL_AUTHORITIES/$SL_TABLE_NAME")!!

/*Data Field*/
const val SL_ID = "_id"
const val SL_NAME = "_name"
const val SL_COUNT = "_count"
const val SL_CREATOR = "_creator"
const val SL_CREATE_TIME = "_create_time"
const val SL_PIC = "_pic"
const val SL_INFO = "_info"

/*Index*/
const val SL_ID_INDEX = 0
const val SL_NAME_INDEX = 1
const val SL_COUNT_INDEX = 2
const val SL_CREATOR_INDEX = 3
const val SL_CREATE_TIME_INDEX = 4
const val SL_PIC_INDEX = 5
const val SL_INFO_INDEX = 6