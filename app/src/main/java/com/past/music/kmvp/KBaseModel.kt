package com.past.music.kmvp

import android.app.Activity
import android.content.Context
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions

/**
 * Created by gaojin on 2018/2/4.
 * MVP - Model实现
 */
abstract class KBaseModel<in T : Any>(private var presenter: KMvpPresenter, private val modelId: Int) : KMvpModel {
    override fun setPresenter(presenter: KMvpPresenter) {
        this.presenter = presenter
    }

    override fun <D : Any> onDataChanged(data: D) {
        if (data::class == Any::class) {
            throw IllegalArgumentException("Please don't dispatch data whose Class type is Any !!!")
        }

        val dataClazz: KClass<*> = data::class
        this::class.declaredMemberFunctions.forEach {
            if ("onDataChanged" == it.name && dataClazz == it.typeParameters) {
                it.call(data)
            }
        }
    }

    override fun getId(): Int {
        return modelId
    }

    protected fun getActivity(): Activity? {
        return presenter.getActivity()
    }

    protected fun getContext(): Context? {
        return getActivity()
    }

    protected fun dispatchData(data: T) {
        presenter.onModelChanged(getId(), data)
    }
}