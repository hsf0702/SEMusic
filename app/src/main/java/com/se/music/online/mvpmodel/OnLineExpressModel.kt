package com.se.music.online.mvpmodel

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.Loader
import android.util.Log
import com.se.music.base.kmvp.KBaseModel
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.online.model.ExpressInfoModel
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/3/7.
 */
class OnLineExpressModel(presenter: KMvpPresenter, modelId: Int) : KBaseModel<ExpressInfoModel>(presenter, modelId) {
    override fun load() {
        (getActivity() as FragmentActivity).supportLoaderManager.initLoader(getId(), null, buildExpressInfoCallBack())
    }

    private fun buildExpressInfoCallBack(): CallLoaderCallbacks<ExpressInfoModel> {
        return object : CallLoaderCallbacks<ExpressInfoModel>(getContext()!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<ExpressInfoModel> {
                return MusicRetrofit.getInstance().getExpressInfo()
            }

            override fun onSuccess(loader: Loader<*>, data: ExpressInfoModel) {
                dispatchData(data)
            }


            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("RecommendSongListBlock", throwable.toString())
            }

        }
    }
}