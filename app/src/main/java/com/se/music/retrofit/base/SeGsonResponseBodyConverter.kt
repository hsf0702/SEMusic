package com.se.music.retrofit.base

import okhttp3.Response
import retrofit2.Converter

/**
 *Author: gaojin
 *Time: 2018/6/12 上午11:50
 */

class SeGsonResponseBodyConverter :Converter<Response,Any>{
    override fun convert(value: Response?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}