package com.se.music.service

import android.provider.MediaStore

/**
 *Author: gaojin
 *Time: 2018/9/23 下午2:47
 */
const val ALBUM_PIC = "album_pic"
val PROJECTION = arrayOf("audio._id AS _id"
        , MediaStore.Audio.Media.ARTIST
        , MediaStore.Audio.Media.ALBUM
        , MediaStore.Audio.Media.TITLE
        , MediaStore.Audio.Media.DATA
        , MediaStore.Audio.Media.MIME_TYPE
        , MediaStore.Audio.Media.ALBUM_ID
        , MediaStore.Audio.Media.ARTIST_ID
        , ALBUM_PIC)