package com.se.music.common

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

data class MusicEntity(var songId: Long = -1
                       , var albumName: String
                       , var albumId: Int = -1) {
    /**
     * 数据库中音乐文件的列名
     */

    var albumData: String? = null        //
    var albumPic: String? = null
    var duration: Int = 0
    var musicName: String? = null
    var artist: String? = null
    var artistId: Long = 0
    var data: String? = null
    var folder: String? = null
    var lrc: String? = null
    var islocal: Boolean = false
    var sort: String? = null

    var size: Int = 0
    /**
     * 0表示没有收藏 1表示收藏
     */
    var favorite = 0
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