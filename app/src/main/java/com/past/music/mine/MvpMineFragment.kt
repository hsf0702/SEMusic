package com.past.music.mine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.kmvp.KBasePresenter
import com.past.music.kmvp.KMvpPage
import com.past.music.kmvp.KMvpPresenter
import com.past.music.mine.root.MineRecyclerView
import com.past.music.pastmusic.R

/**
 * Author: gaojin
 * Time: 2018/4/22 下午9:35
 */
class MvpMineFragment : Fragment(), KMvpPage {

    private val presenter: KMvpPresenter = KBasePresenter(this)

    companion object {
        fun newInstance(): MvpMineFragment {
            return MvpMineFragment()
        }
    }

    override fun onPageError(exception: Exception) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mine_mvp, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.add(MineRecyclerView(presenter, R.id.mine_root_view))
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