package com.se.music.online.newsong

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.Loader
import android.util.Log
import com.se.music.base.mvp.BaseModel
import com.se.music.base.mvp.MvpPresenter
import com.se.music.online.model.ExpressInfoModel
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/3/7.
 */
class OnLineNewSongModel(presenter: MvpPresenter, modelId: Int) : BaseModel<ExpressInfoModel>(presenter, modelId) {
    override fun reload() {
    }

    override fun load() {
        (getActivity() as FragmentActivity).supportLoaderManager.initLoader(getId(), null, buildExpressInfoCallBack())
    }

    private fun buildExpressInfoCallBack(): CallLoaderCallbacks<ExpressInfoModel> {
        return object : CallLoaderCallbacks<ExpressInfoModel>(getContext()!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<ExpressInfoModel> {
                return MusicRetrofit.getInstance().getNewSongInfo()
            }

            override fun onSuccess(loader: Loader<*>, data: ExpressInfoModel) {
                dispatchData(data)
            }


            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("OnLineNewSongModel", throwable.toString())
            }

        }
    }
}