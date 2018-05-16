package com.se.music.online.mvpview

import android.view.View
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.online.view.OnLineClassifyBlock

/**
 * Created by gaojin on 2018/3/6.
 */
class OnLineClassifyView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId) {

    init {
        initView()
    }

    override fun createView(): View {
        return OnLineClassifyBlock(getContext()!!)
    }
}