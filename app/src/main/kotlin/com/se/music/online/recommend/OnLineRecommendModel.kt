package com.se.music.online.recommend

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.loader.content.Loader
import com.se.music.base.mvp.BaseModel
import com.se.music.base.mvp.MvpPresenter
import com.se.music.online.model.RecommendListModel
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 * Created by gaojin on 2018/3/6.
 */
class OnLineRecommendModel(presenter: MvpPresenter, modelId: Int) : BaseModel<RecommendListModel>(presenter, modelId) {

    override fun load() {
        (getActivity() as FragmentActivity).supportLoaderManager.initLoader(getId(), null, buildRecommendListCallBack())
    }

    private fun buildRecommendListCallBack(): CallLoaderCallbacks<RecommendListModel> {
        return object : CallLoaderCallbacks<RecommendListModel>(getContext()!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<RecommendListModel> {
                return MusicRetrofit.instance.getRecommendList()
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