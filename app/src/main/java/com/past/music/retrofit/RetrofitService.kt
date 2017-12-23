package com.past.music.retrofit

import com.past.music.online.model.HallModel
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by gaojin on 2017/12/18.
 */
interface RetrofitService {

    @GET("musichall/fcgi-bin/fcg_yqqhomepagerecommend.fcg")
    fun getMusicHallService(): Call<HallModel>
}