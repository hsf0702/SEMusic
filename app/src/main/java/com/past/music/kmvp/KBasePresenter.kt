package com.past.music.kmvp

import android.app.Activity
import android.os.Bundle

/**
 * Created by gaojin on 2018/2/4.
 */
class KBasePresenter : KMvpPresenter {
    override fun <D> onViewChanged(id: Int, data: D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <D> onModelChanged(id: Int, data: D) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(exception: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(view: KMvpView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun add(model: KMvpModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getView(viewId: Int): KMvpView {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActivity(): Activity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <D> dispatchModelDataToView(modelId: Int, data: D, vararg viewIds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <D> dispatchModelDataToModel(modelId: Int, data: D, vararg modelIds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <D> dispatchViewDataToView(viewId: Int, data: D, vararg viewIds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <D> dispatchViewDataToModel(viewId: Int, data: D, vararg modelIds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start(vararg modelIds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun load(vararg models: KMvpModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStart() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPause() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStop() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}