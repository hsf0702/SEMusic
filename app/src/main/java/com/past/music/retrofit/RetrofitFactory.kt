package com.past.music.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by gaojin on 2017/12/18.
 */
class RetrofitFactory {
    companion object {
        fun getInstance(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                    .build()!!
        }
    }
}