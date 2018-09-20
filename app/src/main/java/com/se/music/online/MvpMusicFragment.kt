package com.se.music.online

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.mvp.BasePresenter
import com.se.music.base.mvp.MvpPage
import com.se.music.base.mvp.MvpPresenter
import com.se.music.online.banner.OnLineBannerModel
import com.se.music.online.banner.OnLineBannerView
import com.se.music.online.classify.OnLineClassifyView
import com.se.music.online.model.ExpressInfoModel
import com.se.music.online.model.HallModel
import com.se.music.online.model.RecommendListModel
import com.se.music.online.newsong.OnLineNewSongModel
import com.se.music.online.newsong.OnLineNewSongView
import com.se.music.online.recommend.OnLineRecommendModel
import com.se.music.online.recommend.OnLineRecommendView
import com.se.music.utils.GET_EXPRESS_SONG
import com.se.music.utils.GET_MUSIC_HALL
import com.se.music.utils.GET_RECOMMEND_LIST

/**
 * Created by gaojin on 2018/2/4.
 * 音乐馆Fragment
 */
class MvpMusicFragment : Fragment(), MvpPage {

    private val presenter: MvpPresenter = BasePresenter(this)

    companion object {
        fun newInstance(): MvpMusicFragment {
            return MvpMusicFragment()
        }
    }

    override fun onPageError(exception: Exception) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_mvp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.add(OnLineBannerView(presenter, R.id.banner))
        presenter.add(OnLineBannerModel(presenter, GET_MUSIC_HALL))

        presenter.add(OnLineClassifyView(presenter, R.id.classify_view))

        presenter.add(OnLineRecommendView(presenter, R.id.online_recommend))
        presenter.add(OnLineRecommendModel(presenter, GET_RECOMMEND_LIST))

        presenter.add(OnLineNewSongView(presenter, R.id.online_express))
        presenter.add(OnLineNewSongModel(presenter, GET_EXPRESS_SONG))
        loadData()
    }

    private fun loadData() {
        presenter.start(GET_MUSIC_HALL
                , GET_RECOMMEND_LIST
                , GET_EXPRESS_SONG)
    }

    @Keep
    fun onModelChanged(id: Int, hallModel: HallModel) {
        presenter.dispatchModelDataToView(id, hallModel, R.id.banner)
    }

    @Keep
    fun onModelChanged(id: Int, recommendModel: RecommendListModel) {
        presenter.dispatchModelDataToView(id, recommendModel, R.id.online_recommend)
    }

    @Keep
    fun onModelChanged(id: Int, expressInfoModel: ExpressInfoModel) {
        presenter.dispatchModelDataToView(id, expressInfoModel, R.id.online_express)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}