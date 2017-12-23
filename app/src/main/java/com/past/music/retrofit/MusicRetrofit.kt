package com.past.music.retrofit

import com.past.music.online.model.HallModel
import retrofit2.Call
import retrofit2.Retrofit

/**
 * Created by gaojin on 2017/12/18.
 */
class MusicRetrofit private constructor() {

    val API_BASE_URL = "https://c.y.qq.com/"

    private val baseRetrofit: Retrofit

    init {
        baseRetrofit = RetrofitFactory.getInstance(API_BASE_URL)
    }

    fun getMusicHall(): Call<HallModel> {
        return baseRetrofit.create(RetrofitService::class.java).getMusicHallService()
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