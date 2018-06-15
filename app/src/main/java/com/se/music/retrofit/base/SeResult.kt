package com.se.music.retrofit.base

import com.google.gson.annotations.SerializedName

/**
 *Author: gaojin
 *Time: 2018/6/12 上午9:46
 */

class SeResult<T> : BaseSeResult() {
    @SerializedName("data")
    var data: T? = null
}