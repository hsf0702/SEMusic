package com.se.music.online.recommend

import android.view.View
import androidx.annotation.Keep
import com.se.music.base.mvp.BaseView
import com.se.music.base.mvp.MvpPresenter
import com.se.music.online.model.RecommendListModel

/**
 * Created by gaojin on 2018/3/6.
 * 每日歌曲推荐模块
 */
class OnLineRecommendView(presenter: MvpPresenter, viewId: Int) : BaseView(presenter, viewId) {

    private lateinit var recommendView: RecommendSongListBlock

    override fun createView(): View {
        recommendView = RecommendSongListBlock(getContext()!!)
        return recommendView
    }

    @Keep
    fun onDataChanged(data: RecommendListModel) {
        recommendView.dataChanged(data)
    }
}