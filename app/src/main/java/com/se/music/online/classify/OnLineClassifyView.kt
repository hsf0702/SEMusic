package com.se.music.online.classify

import android.view.View
import com.se.music.base.mvp.BaseView
import com.se.music.base.mvp.MvpPresenter

/**
 * Created by gaojin on 2018/3/6.
 */
class OnLineClassifyView(presenter: MvpPresenter, viewId: Int) : BaseView(presenter, viewId) {

    init {
        initView()
    }

    override fun createView(): View {
        return OnLineClassifyBlock(getContext()!!)
    }
}