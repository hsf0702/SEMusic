package com.past.music.online.mvpview

import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.youth.banner.Banner

/**
 * Created by gaojin on 2018/2/4.
 * Banner模块
 */
class OnLineBannerView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId) {
    private var banner: Banner? = null

    override fun createView(): View {
        banner = Banner(getContext())
        return banner!!
    }
}