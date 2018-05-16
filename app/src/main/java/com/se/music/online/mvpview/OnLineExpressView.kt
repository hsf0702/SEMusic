package com.se.music.online.mvpview

import android.support.annotation.Keep
import android.view.View
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.online.model.ExpressInfoModel
import com.se.music.online.view.NewSongExpressBlock

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