package com.se.music.retrofit

import com.se.music.online.model.ExpressInfoModel
import com.se.music.online.model.HallModel
import com.se.music.online.model.RecommendListModel
import com.se.music.online.model.SingerModel
import com.se.music.subpage.entity.ArtistAvatar
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
    fun getNewSongInfo(@Query("data") data: String): Call<ExpressInfoModel>

    @GET("")
    fun getSingerAvatar(@Query("method") method: String, @Query("method") artist: String): Call<ArtistAvatar>
}