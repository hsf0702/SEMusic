package com.se.music.provider.metadata

import android.content.ContentUris
import android.net.Uri

/**
 *Author: gaojin
 *Time: 2018/5/27 下午2:19
 */

const val DATABASE_NAME = "SEMusic.db"
const val DATABASE_VERSION = 1

fun getAlbumArtUri(albumId: Long): Uri {
    return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId)
}
