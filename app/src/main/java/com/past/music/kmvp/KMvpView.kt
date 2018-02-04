package com.past.music.kmvp

import android.view.View

/**
 * Created by gaojin on 2018/2/4.
 * MVP - View 接口定义
 */
interface KMvpView {
    fun setPresenter(presenter: KMvpPresenter)

    fun <D:Any> onDataChanged(data: D)

    fun getView(): View

    fun getId(): Int
}