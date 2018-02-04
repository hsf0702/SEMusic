package com.past.music.online.mvpmodel

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.Loader
import android.util.Log
import com.past.music.kmvp.KBaseModel
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.HallModel
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/2/4.
 */
class OnLineBannerModel(presenter: KMvpPresenter, modelId: Int) : KBaseModel<HallModel>(presenter, modelId) {
    override fun load() {
        (getActivity() as FragmentActivity).supportLoaderManager.initLoader(getId(), null, buildHallCallBack())
    }

    /**
     * 请求轮播图片
     */
    private fun buildHallCallBack(): CallLoaderCallbacks<HallModel> {
        return object : CallLoaderCallbacks<HallModel>(getContext()!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<HallModel> {
                return MusicRetrofit.getInstance().getMusicHall()
            }

            override fun onSuccess(loader: Loader<*>, data: HallModel) {

            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("MusicFragment", throwable.toString())
            }
        }
    }
}