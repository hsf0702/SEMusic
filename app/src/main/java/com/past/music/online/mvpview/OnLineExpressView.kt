package com.past.music.online.mvpview

import android.support.annotation.Keep
import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.ExpressInfoModel
import com.past.music.online.view.NewSongExpressBlock

/**
 * Created by gaojin on 2018/3/7.
 * 新歌速递
 */
class OnLineExpressView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId) {

    private var expressView: NewSongExpressBlock? = null

    override fun createView(): View {
        expressView = NewSongExpressBlock(getContext()!!)
        return expressView!!
    }

    @Keep
    fun onDataChanged(data: ExpressInfoModel) {
        expressView!!.dataChanged(data)
    }

}