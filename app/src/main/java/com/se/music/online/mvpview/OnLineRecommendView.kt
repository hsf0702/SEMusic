package com.se.music.online.mvpview

import android.support.annotation.Keep
import android.view.View
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.online.model.RecommendListModel
import com.se.music.online.view.RecommendSongListBlock

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