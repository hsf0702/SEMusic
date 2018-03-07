package com.past.music.online.mvpmodel

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.Loader
import android.util.Log
import com.past.music.kmvp.KBaseModel
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.RecommendListModel
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/3/6.
 */
class OnLineRecommendModel(presenter: KMvpPresenter, modelId: Int) : KBaseModel<RecommendListModel>(presenter, modelId) {
    override fun load() {
        (getActivity() as FragmentActivity).supportLoaderManager.initLoader(getId(), null, buildRecommendListCallBack())
    }

    private fun buildRecommendListCallBack(): CallLoaderCallbacks<RecommendListModel> {
        return object : CallLoaderCallbacks<RecommendListModel>(getContext()!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<RecommendListModel> {
                return MusicRetrofit.getInstance().getRecommendList()
            }

            override fun onSuccess(loader: Loader<*>, data: RecommendListModel) {
                dispatchData(data)
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("OnLineRecommendModel", throwable.toString())
            }
        }
    }
}