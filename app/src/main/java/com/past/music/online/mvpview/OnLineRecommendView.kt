package com.past.music.online.mvpview

import android.support.annotation.Keep
import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.RecommendListModel
import com.past.music.online.view.RecommendSongListBlock

/**
 * Created by gaojin on 2018/3/6.
 * 每日歌曲推荐模块
 */
class OnLineRecommendView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId) {

    private var recommendView: RecommendSongListBlock? = null

    override fun createView(): View {
        recommendView = RecommendSongListBlock(getContext()!!)
        return recommendView!!
    }

    @Keep
    fun onDataChanged(data: RecommendListModel) {
        recommendView!!.dataChanged(data)
    }
}