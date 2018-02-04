package com.past.music.kmvp

import android.app.Activity
import android.content.Context
import android.support.annotation.IdRes
import android.view.View
import android.view.ViewGroup
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberFunctions

/**
 * Created by gaojin on 2018/2/4.
 */
abstract class KBaseView(private var presenter: KMvpPresenter, private val viewId: Int) : KMvpView {

    private var view: View? = null

    protected abstract fun createView(): View

    protected fun getActivity(): Activity? {
        return presenter.getActivity()
    }

    override fun setPresenter(presenter: KMvpPresenter) {
        this.presenter = presenter
    }

    protected fun getContext(): Context? {
        return getActivity()
    }

    protected fun <D : Any> dispatchData(data: D) {
        presenter.onViewChanged(getId(), data)
    }

    override fun <D : Any> onDataChanged(data: D) {
        initView()
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

    override fun getView(): View {
        initView()
        return view!!
    }

    override fun getId(): Int {
        return viewId
    }

    protected fun initView() {
        if (getActivity() == null) {
            return
        }
        if (view == null) {
            view = createView()
            replace(viewId, view)
        }
    }

    protected fun replace(@IdRes targetId: Int, source: View?) {
        val target: View = getActivity()!!.findViewById(targetId)
        replace(target, source)
    }


    protected fun replace(target: View?, source: View?) {
        if (target == null) {
            return
        }
        if (source == null) {
            return
        }
        val viewParent = target.parent

        if (viewParent != null && viewParent is ViewGroup) {
            val index = viewParent.indexOfChild(target)
            viewParent.removeViewInLayout(target)

            val id = target.id
            source.id = id

            val layoutParams = target.layoutParams
            if (layoutParams != null) {
                viewParent.addView(source, index, layoutParams)
            } else {
                viewParent.addView(source, index)
            }
        } else {
            throw IllegalStateException("ViewStub must have a non-null ViewGroup viewParent")
        }

    }
}