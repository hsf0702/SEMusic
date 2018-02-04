package com.past.music.kmvp

import android.app.Activity

/**
 * Created by gaojin on 2018/2/4.
 */
interface KMvpPage {
    fun getActivity(): Activity

    /**
     * 提供给一个页面的基础请求Model在error的时候调用
     *
     * @param exception 错误信息以Exception形式传给Presenter
     */
    fun onPageError(exception: Exception)
}