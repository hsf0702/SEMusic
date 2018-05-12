package com.se.music.kmvp

/**
 * Created by gaojin on 2018/2/4.
 * MVP - Model
 */
interface KMvpModel {
    fun setPresenter(presenter: KMvpPresenter)

    fun <D : Any> onDataChanged(data: D)

    fun load()

    fun getId(): Int
}