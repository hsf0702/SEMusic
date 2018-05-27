package com.se.music.provider.metadata

import android.provider.MediaStore

/**
 *Author: gaojin
 *Time: 2018/5/28 上午1:00
 */

val localAlbumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI!!

val info_album = arrayOf(MediaStore.Audio.Albums._ID
        , MediaStore.Audio.Albums.ALBUM_ART
        , MediaStore.Audio.Albums.ALBUM
        , MediaStore.Audio.Albums.NUMBER_OF_SONGS
        , MediaStore.Audio.Albums.ARTIST)

val albumSelection = StringBuilder(MediaStore.Audio.Albums._ID
        + " in (select distinct " + MediaStore.Audio.Media.ALBUM_ID
        + " from audio_meta where (1=1)")
        .append(" and " + MediaStore.Audio.Media.SIZE + " > " + mFilterSize)
        .append(" and " + MediaStore.Audio.Media.DURATION + " > " + mFilterDuration)
        .append(" )")!!


