package com.se.music.utils

/**
 * Created by gaojin on 2018/3/6.
 */
interface MConstants {
    companion object {
        val MUSIC_COUNT_CHANGED = "com.past.music.musiccountchanged"
        val PLAYLIST_ITEM_MOVED = "com.past.music.moved"
        val PLAYLIST_COUNT_CHANGED = "com.past.music.playlistcountchanged"
        val EMPTY_LIST = "com.past.music.emptyplaylist"
        val PACKAGE = "com.past.music"

        const val MUSICOVERFLOW = 0
        const val ARTISTOVERFLOW = 1
        const val ALBUMOVERFLOW = 2
        const val FOLDEROVERFLOW = 3

        //歌手和专辑列表点击都会进入MyMusic 此时要传递参数表明是从哪里进入的
        val START_FROM_ARTIST = 1
        val START_FROM_ALBUM = 2
        val START_FROM_LOCAL = 3
        val START_FROM_FOLDER = 4
        val START_FROM_FAVORITE = 5

        val FAV_PLAYLIST = 10
    }
}