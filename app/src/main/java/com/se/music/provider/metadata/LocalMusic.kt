package com.se.music.provider.metadata

/**
 *Author: gaojin
 *Time: 2018/5/27 下午7:04
 */

//歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
const val START_FROM_ARTIST = 1
const val START_FROM_ALBUM = 2
const val START_FROM_LOCAL = 3
const val START_FROM_FOLDER = 4
const val START_FROM_FAVORITE = 5