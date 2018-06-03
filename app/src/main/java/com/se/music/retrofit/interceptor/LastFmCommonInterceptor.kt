package com.se.music.retrofit.interceptor

import com.se.music.retrofit.LAST_FM_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

/**
 *Author: gaojin
 *Time: 2018/6/3 下午5:17
 * 对LastFm的请求添加公用参数
 */

class LastFmCommonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldRequest = chain.request()
        val oldUrlBuilder = oldRequest.url()
                .newBuilder()
                .scheme(oldRequest.url().scheme())
                .host(oldRequest.url().host())
                .addQueryParameter("format", "json")
                .addQueryParameter("api_key", LAST_FM_API_KEY)

        val newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(oldUrlBuilder.build())
                .build()
        return chain.proceed(newRequest)
    }
}