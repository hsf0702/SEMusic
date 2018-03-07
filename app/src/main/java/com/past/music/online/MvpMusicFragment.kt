package com.past.music.online

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.kmvp.KBasePresenter
import com.past.music.kmvp.KMvpPage
import com.past.music.kmvp.KMvpPresenter
import com.past.music.online.model.ExpressInfoModel
import com.past.music.online.model.HallModel
import com.past.music.online.model.RecommendListModel
import com.past.music.online.mvpmodel.OnLineBannerModel
import com.past.music.online.mvpmodel.OnLineExpressModel
import com.past.music.online.mvpmodel.OnLineRecommendModel
import com.past.music.online.mvpview.OnLineBannerView
import com.past.music.online.mvpview.OnLineClassifyView
import com.past.music.online.mvpview.OnLineExpressView
import com.past.music.online.mvpview.OnLineRecommendView
import com.past.music.pastmusic.R
import com.past.music.utils.IdUtils

/**
 * Created by gaojin on 2018/2/4.
 * 音乐馆Fragment
 */
class MvpMusicFragment : Fragment(), KMvpPage {

    private val presenter: KMvpPresenter = KBasePresenter(this)

    companion object {
        fun newInstance(): MvpMusicFragment {
            return MvpMusicFragment()
        }
    }

    override fun onPageError(exception: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_music_mvp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.add(OnLineBannerView(presenter, R.id.banner))
        presenter.add(OnLineBannerModel(presenter, IdUtils.GET_MUSIC_HALL))

        presenter.add(OnLineClassifyView(presenter, R.id.classify_view))

        presenter.add(OnLineRecommendView(presenter, R.id.online_recommend))
        presenter.add(OnLineRecommendModel(presenter, IdUtils.GET_RECOMMEND_LIST))

        presenter.add(OnLineExpressView(presenter, R.id.online_express))
        presenter.add(OnLineExpressModel(presenter, IdUtils.GET_EXPRESS_SONG))
        loadData()
    }

    private fun loadData() {
        presenter.start(IdUtils.GET_MUSIC_HALL
                , IdUtils.GET_RECOMMEND_LIST
                , IdUtils.GET_EXPRESS_SONG)
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