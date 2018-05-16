package com.se.music.base.kmvp

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.SparseArray
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions

/**
 * Created by gaojin on 2018/2/4.
 * MVP - Presenter实现
 */
class KBasePresenter(private var page: KMvpPage) : KMvpPresenter {

    private val mvpViewMap = SparseArray<KMvpView>()
    private val mvpModelMap = SparseArray<KMvpModel>()
    private val modelTypeMap = SparseArray<KType>()

    init {
        if (page !is Fragment && page !is android.app.Fragment && page !is Activity) {
            throw IllegalArgumentException("FoodMvpPage must be implemented by Activity or Fragment")
        }
    }

    override fun <D : Any> onViewChanged(id: Int, data: D) {
        if (data::class == Any::class) {
            throw IllegalArgumentException("Please don't dispatch data whose Class type is Any !!!")
        }

        val declaredMemberFunctions = page::class.declaredMemberFunctions
        for (item: KFunction<*> in declaredMemberFunctions) {
            if (item.name == "onViewChanged") {
                try {
                    item.call(page, id, data)
                } catch (e: IllegalArgumentException) {
                    continue
                } catch (e: InvocationTargetException) {
                    continue
                } catch (e: IllegalAccessException) {
                    continue
                }
                break
            }
        }
    }

    override fun <D : Any> onModelChanged(id: Int, data: D) {
        if (data::class == Any::class) {
            throw IllegalArgumentException("Please don't dispatch data whose Class type is Any !!!")
        }

        val declaredMemberFunctions = page::class.declaredMemberFunctions
        for (item: KFunction<*> in declaredMemberFunctions) {
            if (item.name == "onModelChanged") {
                try {
                    item.call(page, id, data)
                } catch (e: IllegalArgumentException) {
                    continue
                } catch (e: InvocationTargetException) {
                    continue
                } catch (e: IllegalAccessException) {
                    continue
                }
                break
            }
        }
    }

    override fun onError(exception: Exception) {
        page.onPageError(exception)
    }

    override fun add(view: KMvpView) {
        mvpViewMap.put(view.getId(), view)
    }

    override fun add(model: KMvpModel) {
        mvpModelMap.put(model.getId(), model)
    }

    override fun getView(viewId: Int): KMvpView? {
        return if (mvpViewMap.indexOfKey(viewId) >= 0) {
            mvpViewMap.get(viewId)
        } else null
    }

    override fun getActivity(): Activity? {
        return page.getActivity()
    }

    override fun <D : Any> dispatchModelDataToView(modelId: Int, data: D, vararg viewIds: Int) {
        for (id in viewIds) {
            mvpViewMap.get(id)?.onDataChanged(data)
        }
    }

    override fun <D : Any> dispatchModelDataToModel(modelId: Int, data: D, vararg modelIds: Int) {
        for (id in modelIds) {
            mvpModelMap.get(id)?.onDataChanged(data)
        }
    }

    override fun <D : Any> dispatchViewDataToView(viewId: Int, data: D, vararg viewIds: Int) {
        for (id in viewIds) {
            mvpViewMap.get(id)?.onDataChanged(data)
        }
    }

    override fun <D : Any> dispatchViewDataToModel(viewId: Int, data: D, vararg modelIds: Int) {
        for (id in modelIds) {
            mvpModelMap.get(id)?.onDataChanged(data)
        }
    }

    override fun start(vararg modelIds: Int) {
        modelIds
                .map { mvpModelMap.get(it) }
                .forEach { it?.load() }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        val onCreate = KMvpOnCreate()
        onCreate.savedInstanceState = savedInstanceState
        dispatchLifeCycle(onCreate)
    }

    override fun onStart() {
        dispatchLifeCycle(KMvpOnStart())
    }

    override fun onResume() {
        dispatchLifeCycle(KMvpOnResume())
    }

    override fun onPause() {
        dispatchLifeCycle(KMvpOnPause())
    }

    override fun onStop() {
        dispatchLifeCycle(KMvpOnStop())
    }

    override fun onDestroy() {
        dispatchLifeCycle(KMvpOnDestroy())
    }

    private fun dispatchLifeCycle(lifeCycle: KMvpLifeCycle) {
        (0 until mvpViewMap.size())
                .map { mvpViewMap.valueAt(it) }
                .forEach { it.onDataChanged(lifeCycle) }

        (0 until mvpModelMap.size())
                .map { mvpModelMap.valueAt(it) }
                .forEach { it.onDataChanged(lifeCycle) }
    }
}