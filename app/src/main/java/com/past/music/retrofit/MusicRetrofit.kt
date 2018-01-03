package com.past.music.retrofit

import com.past.music.online.model.HallModel
import com.past.music.online.model.RecommendListModel
import com.past.music.online.model.SingerModel
import com.past.music.online.params.CommonParams
import com.past.music.utils.GsonFactory
import retrofit2.Call
import retrofit2.Retrofit


/**
 * Created by gaojin on 2017/12/18.
 */
class MusicRetrofit private constructor() {

    val API_BASE_C_URL = "https://c.y.qq.com/"
    val API_BASE_U_URL = "https://u.y.qq.com/"

    private val baseCRetrofit: Retrofit
    private val baseURetrofit: Retrofit

    init {
        baseCRetrofit = RetrofitFactory.getInstance(API_BASE_C_URL)
        baseURetrofit = RetrofitFactory.getInstance(API_BASE_U_URL)
    }

    fun getMusicHall(): Call<HallModel> {
        return baseCRetrofit.create(RetrofitService::class.java).getMusicHallService()
    }

    fun getRecommendList(): Call<RecommendListModel> {
        return baseURetrofit.create(RetrofitService::class.java)
                .getRecommendList(GsonFactory.getInstance().toJson(CommonParams()))
    }

    fun getSinger(pagesize: Int, pagenum: Int): Call<SingerModel> {
        val map = hashMapOf("channel" to "singer", "key" to "all_all_all", "page" to "list", "format" to "jsonp")
        return baseCRetrofit.create(RetrofitService::class.java).getSinger(map, pagesize, pagenum)
    }

    companion object {
        @Volatile private var sMusicRetrofit: MusicRetrofit? = null

        fun getInstance(): MusicRetrofit {
            if (null == sMusicRetrofit) {
                synchronized(Retrofit::class.java) {
                    if (null == sMusicRetrofit) {
                        sMusicRetrofit = MusicRetrofit()
                    }
                }
            }
            return sMusicRetrofit!!
        }
    }

}