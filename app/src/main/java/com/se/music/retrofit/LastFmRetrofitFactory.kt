package com.se.music.retrofit

import com.se.music.retrofit.base.ConverterDataInterceptor
import com.se.music.retrofit.base.SeGsonConverterFactory
import com.se.music.retrofit.interceptor.LastFmCommonInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 *Author: gaojin
 *Time: 2018/6/3 下午5:38
 */

class LastFmRetrofitFactory {
    companion object {
        private val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LastFmCommonInterceptor())
                .build()!!

        fun getInstance(baseUrl: String): Retrofit {
            return Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(SeGsonConverterFactory.create()
                            .addConvertIntercepter(ConverterDataInterceptor())) //设置数据解析器
                    .client(okHttpClient)
                    .build()
        }
    }
}