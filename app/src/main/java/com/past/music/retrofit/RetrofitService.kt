package com.past.music.retrofit

import com.past.music.online.model.ExpressInfoModel
import com.past.music.online.model.HallModel
import com.past.music.online.model.RecommendListModel
import com.past.music.online.model.SingerModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * Created by gaojin on 2017/12/18.
 */
interface RetrofitService {

    @GET("musichall/fcgi-bin/fcg_yqqhomepagerecommend.fcg")
    fun getMusicHallService(): Call<HallModel>

    @GET("cgi-bin/musicu.fcg")
    fun getRecommendList(@Query("data") data: String): Call<RecommendListModel>

    @GET("v8/fcg-bin/v8.fcg")
    fun getSinger(@QueryMap map: Map<String, String>, @Query("pagesize") pagesize: Int, @Query("pagenum") pagenum: Int): Call<SingerModel>

    @GET("cgi-bin/musicu.fcg")
    fun getExpressInfo(@Query("data") data: String): Call<ExpressInfoModel>
}