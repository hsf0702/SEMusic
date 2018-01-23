package com.past.music.online

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.Loader
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.activity.WebViewActivity
import com.past.music.online.view.RecommendSongListBlock
import com.past.music.online.listener.OnLineRefreshListener
import com.past.music.online.model.HallModel
import com.past.music.pastmusic.R
import com.past.music.retrofit.MusicRetrofit
import com.past.music.retrofit.callback.CallLoaderCallbacks
import com.past.music.utils.GlideImageLoader
import com.past.music.utils.IdUtils
import com.youth.banner.Banner
import com.youth.banner.listener.OnBannerListener
import retrofit2.Call


class MusicFragment : Fragment(), OnBannerListener {

    //View
    private var banner: Banner? = null
    private var recommendBlock: RecommendSongListBlock? = null

    private val images = ArrayList<String>()
    private var bannerList: List<HallModel.Data.Slider>? = null
    private var refreshList: MutableList<OnLineRefreshListener>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        banner = view.findViewById(R.id.banner)
        recommendBlock = view.findViewById(R.id.online_recommend_block)

        refreshList = ArrayList()
        refreshList!!.add(recommendBlock!!)

        loaderManager.initLoader(IdUtils.GET_MUSIC_HALL, null, buildHallCallBack())

        initBlocks()
    }

    private fun initBlocks() {
        refreshList?.forEach { it.refresh(fragmentManager, loaderManager) }
    }

    private fun initBanner() {
        banner?.setImageLoader(GlideImageLoader())
        banner?.setImages(images)

        //banner设置方法全部调用完毕时最后调用
        banner?.start()

        banner?.setOnBannerListener(this)
    }

    override fun OnBannerClick(position: Int) {
        WebViewActivity.startWebViewActivity(context, "", bannerList?.get(position)?.linkUrl)
    }


    /**
     * 请求轮播图片
     */
    private fun buildHallCallBack(): CallLoaderCallbacks<HallModel> {
        return object : CallLoaderCallbacks<HallModel>(context!!) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<HallModel> {
                return MusicRetrofit.getInstance().getMusicHall()
            }

            override fun onSuccess(loader: Loader<*>, data: HallModel) {
                data.data?.slider?.forEach { it.picUrl?.let { it1 -> images.add(it1) } }
                bannerList = data.data?.slider
                initBanner()
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("MusicFragment", throwable.toString())
            }
        }
    }


    companion object {
        fun newInstance(): MusicFragment {
            return MusicFragment()
        }
    }
}