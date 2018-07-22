package com.se.music.utils

import com.se.music.provider.metadata.LAST_FM_IMAGE_LARGE
import com.se.music.provider.metadata.LAST_FM_IMAGE_MEDIUM
import com.se.music.provider.metadata.LAST_FM_IMAGE_MEGA

/**
 *Author: gaojin
 *Time: 2018/7/8 下午3:13
 */

fun getImageId(imageUrl: String): String {
    val regex = Regex("[a-z0-9]*(?=.png)")
    val result = regex.find(imageUrl)
    return if (result == null) {
        "null"
    } else {
        regex.find(imageUrl)!!.value
    }
}

fun getMediumImageUrl(imageId: String): String {
    return String.format(LAST_FM_IMAGE_MEDIUM, imageId)
}

fun getLargeImageUrl(imageId: String): String {
    return String.format(LAST_FM_IMAGE_LARGE, imageId)
}

fun getMegaImageUrl(imageId: String): String {
    return String.format(LAST_FM_IMAGE_MEGA, imageId)
}