package com.se.music.retrofit

import com.se.music.online.model.ExpressInfoModel
import com.se.music.online.model.HallModel
import com.se.music.online.model.RecommendListModel
import com.se.music.online.model.SingerModel
import com.se.music.online.params.CommonPostParams
import com.se.music.online.params.ExpressPostParams
import com.se.music.subpage.entity.ArtistAvatar
import com.se.music.utils.singleton.GsonFactory
import retrofit2.Call
import retrofit2.Retrofit


/**
 * Created by gaojin on 2017/12/18.
 */
class MusicRetrofit private constructor() {

    companion object {
        val instance: MusicRetrofit by lazy { MusicRetrofit() }
    }

    val API_BASE_C_URL = "http://c.y.qq.com/"
    val API_BASE_U_URL = "http://u.y.qq.com/"
    val API_LAST_FM_URL = "http://ws.audioscrobbler.com/2.0/"

    private val baseCRetrofit: Retrofit = QQRetrofitFactory.getInstance(API_BASE_C_URL)
    private val baseURetrofit: Retrofit = QQRetrofitFactory.getInstance(API_BASE_U_URL)
    private val baseLastFmRetrofit: Retrofit = LastFmRetrofitFactory.getInstance(API_LAST_FM_URL)

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
                .getRecommendList(GsonFactory.instance.toJson(params))
    }

    fun getSinger(pagesize: Int, pagenum: Int): Call<SingerModel> {
        val map = hashMapOf("channel" to "singer", "key" to "all_all_all", "page" to "list", "format" to "jsonp")
        return baseCRetrofit.create(RetrofitService::class.java).getSinger(map, pagesize, pagenum)
    }

    fun getNewSongInfo(): Call<ExpressInfoModel> {

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
                .getNewSongInfo(GsonFactory.instance.toJson(expressPostParams))
    }

    fun getSingAvatar(artist: String): Call<ArtistAvatar> {
        return baseLastFmRetrofit.create(RetrofitService::class.java)
                .getSingerAvatar("artist.getinfo", artist)
    }
}