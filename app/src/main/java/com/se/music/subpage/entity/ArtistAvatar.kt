package com.se.music.subpage.entity

import com.google.gson.annotations.SerializedName

/**
 *Author: gaojin
 *Time: 2018/6/3 下午7:18
 */

class ArtistAvatar {
    private var artist: ArtistBeanX? = null
}

class ArtistBeanX {
    var name: String? = null
    var image: List<ImageBeanX>? = null
}

class ImageBeanX {
    @SerializedName("#text")
    var imgUrl: String? = null // FIXME check this code
}