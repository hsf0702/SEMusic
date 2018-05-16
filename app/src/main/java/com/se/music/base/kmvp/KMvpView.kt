package com.se.music.base.kmvp

import android.view.View

/**
 * Created by gaojin on 2018/2/4.
 * MVP - View 接口定义
 */
interface KMvpView {
    fun setPresenter(presenter: KMvpPresenter)

    fun <D : Any> onDataChanged(data: D)

    fun <D : Any> onDataChanged(data: D, view: View)

    fun getView(): View

    fun getId(): Int
}