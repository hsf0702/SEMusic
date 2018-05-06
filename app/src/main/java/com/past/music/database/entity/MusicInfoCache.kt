package com.past.music.database.entity

/**
 * Author: gaojin
 * Time: 2018/5/6 下午4:28
 */
class MusicInfoCache {
    companion object {
        val SONG_LIST_ID = "_song_list_id"
        /*Data Field*/
        val ID = "_id"
        val SONG_ID = "_song_id"
        val NAME = "_name"
        val ALBUM_ID = "_album_id"
        val ALBUM_NAME = "_album_name"
        val ALBUM_PIC = "_album_pic"
        val ARTIST_ID = "_artist_id"
        val ARTIST_NAME = "_artist_name"
        val PATH = "_PATH"
        //0代表本地  1代表不是本地
        val IS_LOCAL = "_is_local"
        //0代表不喜欢  1代表喜欢
        val IS_LOVE = "_is_love"
    }
}