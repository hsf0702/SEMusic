package com.past.music.online.mvpview

import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.view.OnLineClassifyBlock

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