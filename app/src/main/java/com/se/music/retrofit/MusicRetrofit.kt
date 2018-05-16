package com.se.music.retrofit

import com.se.music.online.model.ExpressInfoModel
import com.se.music.online.model.HallModel
import com.se.music.online.model.RecommendListModel
import com.se.music.online.model.SingerModel
import com.se.music.online.params.CommonPostParams
import com.se.music.online.params.ExpressPostParams
import com.se.music.utils.singleton.GsonFactory
import retrofit2.Call
import retrofit2.Retrofit


/**
 * Created by gaojin on 2017/12/18.
 */
class MusicRetrofit private constructor() {

    val API_BASE_C_URL = "http://c.y.qq.com/"
    val API_BASE_U_URL = "http://u.y.qq.com/"

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
        val params = CommonPostParams()
        params.recomPlaylist.method = "get_hot_recommend"
        params.recomPlaylist.module = "playlist.HotRecommendServer"
        params.recomPlaylist.param.async = 1
        params.recomPlaylist.param.cmd = 2
        return baseURetrofit.create(RetrofitService::class.java)
                .getRecommendList(GsonFactory.getInstance().toJson(params))
    }

    fun getSinger(pagesize: Int, pagenum: Int): Call<SingerModel> {
        val map = hashMapOf("channel" to "singer", "key" to "all_all_all", "page" to "list", "format" to "jsonp")
        return baseCRetrofit.create(RetrofitService::class.java).getSinger(map, pagesize, pagenum)
    }

    fun getExpressInfo(): Call<ExpressInfoModel> {

        val expressPostParams = ExpressPostParams()
        expressPostParams.new_album.method = "GetNewSong"
        expressPostParams.new_album.module = "QQMusic.MusichallServer"
        expressPostParams.new_album.param.sort = 1
        expressPostParams.new_album.param.start = 0
        expressPostParams.new_album.param.end = 0

        expressPostParams.new_song.method = "GetNewAlbum"
        expressPostParams.new_song.module = "QQMusic.MusichallServer"
        expressPostParams.new_song.param.sort = 1
        expressPostParams.new_song.param.start = 0
        expressPostParams.new_song.param.end = 1

        return baseURetrofit.create(RetrofitService::class.java)
                .getExpressInfo(GsonFactory.getInstance().toJson(expressPostParams))
    }

    companion object {
        @Volatile
        private var sMusicRetrofit: MusicRetrofit? = null

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