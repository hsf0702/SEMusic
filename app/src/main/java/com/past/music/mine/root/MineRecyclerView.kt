package com.past.music.mine.root

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter

/**
 * Author: gaojin
 * Time: 2018/4/22 下午10:16
 */
class MineRecyclerView(presenter: KMvpPresenter, viewId: Int) : KBaseView(presenter, viewId) {

    private var mineRecyclerView: RecyclerView? = null
    private var adapter: MineAdapter? = null

    init {
        initView()
    }

    override fun createView(): View {
        mineRecyclerView = RecyclerView(getContext())
        mineRecyclerView!!.layoutManager = LinearLayoutManager(getContext())
        adapter = MineAdapter(getContext()!!)
        mineRecyclerView!!.adapter = adapter
        return mineRecyclerView!!
    }
}