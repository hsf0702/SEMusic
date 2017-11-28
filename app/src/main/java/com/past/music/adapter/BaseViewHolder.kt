package com.past.music.adapter

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by gaojin on 2017/11/20.
 */
abstract class BaseViewHolder<in T> constructor(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun onBind(t: T, position: Int)
}