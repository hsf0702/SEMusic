package com.past.music.online.listener

import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager

/**
 * Created by gaojin on 2018/1/1.
 */
interface OnLineRefreshListener {
    fun refresh(fragmentManager: FragmentManager?, loaderManager: LoaderManager)
}