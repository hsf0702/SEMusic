package com.se.music.online.banner

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.Loader
import android.util.Log
import com.se.music.base.kmvp.KBaseModel
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.online.model.HallModel
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/2/4.
 */
class OnLineBannerModel(presenter: KMvpPresenter, modelId: Int) : KBaseModel<HallModel>(presenter, modelId) {
    override fun reload() {
    }

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
                dispatchData(data)
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("MusicFragment", throwable.toString())
            }
        }
    }


}