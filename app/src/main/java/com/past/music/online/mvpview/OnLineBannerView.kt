package com.past.music.online.mvpview

import android.annotation.SuppressLint
import android.support.annotation.Keep
import android.view.LayoutInflater
import android.view.View
import com.past.music.activity.WebViewActivity
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.HallModel
import com.past.music.pastmusic.R
import com.past.music.utils.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener

/**
 * Created by gaojin on 2018/2/4.
 * Banner模块
 */
class OnLineBannerView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId), OnBannerListener {

    private var banner: Banner = LayoutInflater.from(getContext()!!).inflate(R.layout.view_banner, null) as Banner
    private val images = ArrayList<String>()
    private var bannerList: List<HallModel.Data.Slider>? = null

    @SuppressLint("InflateParams")
    override fun createView(): View {
        return banner
    }

    @Keep
    fun onDataChanged(data: HallModel) {
        data.data?.slider?.forEach { it.picUrl?.let { it1 -> images.add(it1) } }
        bannerList = data.data?.slider
        initBanner()
    }

    private fun initBanner() {
        banner.setImageLoader(GlideImageLoader())
        banner.setImages(images)
        banner.setOnBannerListener(this)
        //banner设置方法全部调用完毕时最后调用
        banner.start()
    }

    override fun OnBannerClick(position: Int) {
        WebViewActivity.startWebViewActivity(getContext(), "", bannerList?.get(position)?.linkUrl)

    }
}