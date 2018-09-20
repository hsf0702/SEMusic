package com.se.music.utils

import android.provider.MediaStore

/**
 *Author: gaojin
 *Time: 2018/9/20 下午11:22
 */

/**
 * ========ARTIST=========
 */
/* Artist sort order A-Z */
val ARTIST_A_Z = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER

/* Artist sort order Z-A */
val ARTIST_Z_A = ARTIST_A_Z + " DESC"

/* Artist sort order number of songs */
val ARTIST_NUMBER_OF_SONGS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS + " DESC"

/* Artist sort order number of albums */
val ARTIST_NUMBER_OF_ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS + " DESC"

/**
 * ========ALBUM=========
 */
/* Album sort order A-Z */
val ALBUM_A_Z = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER

/* Album sort order Z-A */
val ALBUM_Z_A = ALBUM_A_Z + " DESC"

/* Album sort order songs */
val ALBUM_NUMBER_OF_SONGS = MediaStore.Audio.Albums.NUMBER_OF_SONGS + " DESC"

/* Album sort order artist */
val ALBUM_ARTIST = MediaStore.Audio.Albums.ARTIST

/* Album sort order year */
val ALBUM_YEAR = MediaStore.Audio.Albums.FIRST_YEAR + " DESC"


/**
 * ========SONG=========
 */
/* Song sort order A-Z */
val SONG_A_Z = MediaStore.Audio.Media.DEFAULT_SORT_ORDER

/* Song sort order Z-A */
val SONG_Z_A = SONG_A_Z + " DESC"

/* Song sort order artist */
val SONG_ARTIST = MediaStore.Audio.Media.ARTIST

/* Song sort order album */
val SONG_ALBUM = MediaStore.Audio.Media.ALBUM

/* Song sort order year */
val SONG_YEAR = MediaStore.Audio.Media.YEAR + " DESC"

/* Song sort order duration */
val SONG_DURATION = MediaStore.Audio.Media.DURATION + " DESC"

/* Song sort order date */
val SONG_DATE = MediaStore.Audio.Media.DATE_ADDED + " DESC"

/* Song sort order filename */
val SONG_FILENAME = MediaStore.Audio.Media.DATA