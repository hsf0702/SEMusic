package com.past.music.kmvp

import android.app.Activity
import android.os.Bundle

/**
 * Created by gaojin on 2018/2/4.
 * MVP - Presenter
 */
interface KMvpPresenter {
    /**
     * view变化的回调
     *
     * @param data View变化带来的影响结果
     * @param <D>
    </D> */
    fun <D> onViewChanged(id: Int, data: D)

    /**
     * 数据请求回调
     *
     * @param data 数据请求返回结果
     * @param <D>
    </D> */
    fun <D> onModelChanged(id: Int, data: D)

    /**
     * 提供给一个页面的基础请求Model在error的时候调用
     *
     * @param exception 错误信息以Exception形式传给Presenter
     */
    fun onError(exception: Exception)

    fun add(view: KMvpView)

    fun add(model: KMvpModel)

    fun getView(viewId: Int): KMvpView

    fun getActivity(): Activity

    fun <D> dispatchModelDataToView(modelId: Int, data: D, vararg viewIds: Int)

    fun <D> dispatchModelDataToModel(modelId: Int, data: D, vararg modelIds: Int)

    fun <D> dispatchViewDataToView(viewId: Int, data: D, vararg viewIds: Int)

    fun <D> dispatchViewDataToModel(viewId: Int, data: D, vararg modelIds: Int)

    fun start(vararg modelIds: Int)

    @Deprecated("")
    fun load(vararg models: KMvpModel)


    fun onCreate(savedInstanceState: Bundle)

    fun onStart()

    fun onResume()

    fun onPause()

    fun onStop()

    fun onDestroy()
}