package com.se.music.common

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *Author: gaojin
 *Time: 2018/5/26 下午9:35
 * Common Entity 集合
 */

/**
 * 专辑bean
 */
data class AlbumEntity(var albumName: String
                       , var albumId: Int = -1
                       , var numberOfSongs: Int
                       , var albumArt: String
                       , var albumArtist: String
                       , var albumSort: String)

/**
 * 歌手bean
 */
data class ArtistEntity(var artistName: String
                        , var numberOfTracks: Int
                        , var artistId: Long
                        , var artistSort: String)

/**
 * 文件夹bean
 */
data class FolderEntity(var folderName: String
                        , var folderPath: String
                        , var folderSort: String) {
    var folderCount: Int = 0
}

@Parcelize
data class MusicEntity(var songId: Long = -1
                       , var musicName: String
                       , var artist: String
                       , var albumId: Long = -1
                       , var albumData: String?
                       , var albumPic: String?
                       , var duration: Long
                       , var albumName: String?
                       , var artistId: Long
                       , var data: String?
                       , var size: Long
                       , var folder: String?
                       , var lrc: String?
                       , var sort: String?
        /**
         * 0表示没有收藏 1表示收藏
         */
                       , var favorite: Int = 0
                       , var islocal: Boolean) : Parcelable {
}

data class SongListEntity(var id: String
                          , var name: String
                          , var createTime: String) {
    var count: Int = 0
    var creator: String? = null
    var listPic: String? = null
    var info: String? = null
}

data class OverFlowItem(var title: String   //信息标题
                        , var avatar: Int = 0)   // 图片ID