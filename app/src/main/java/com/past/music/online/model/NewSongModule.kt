package com.past.music.online.model

/**
 * Created by gaojin on 2018/1/6.
 */
class NewSongModule {
    var new_album: NewAlbum? = null
    var code: Int = 0
    var ts: Long = 0
}

class NewAlbumBean {
    var data: DataBean? = null
    var code: Int = 0
}

class DataBean {
    var size: Int = 0
    var type: Int = 0
    var album_list: List<AlbumListBean>? = null
}

class AlbumListBean {
    var album: AlbumBean? = null
    var author: List<AuthorBean>? = null
}

class AlbumBean {
    var id: Int = 0
    var mid: String? = null
    var name: String? = null
    var subtitle: String? = null
    var time_public: String? = null
    var title: String? = null
}

class AuthorBean {
    var id: Int = 0
    var mid: String? = null
    var name: String? = null
    var title: String? = null
    var type: Int = 0
    var uin: Int = 0
}