package com.se.music.mine

import android.database.Cursor
import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.kmvp.KBasePresenter
import com.se.music.base.kmvp.KMvpPage
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.mine.event.CollectEvent
import com.se.music.mine.event.CreateEvent
import com.se.music.mine.listname.MineSongListNameView
import com.se.music.mine.model.QuerySongListModel
import com.se.music.mine.operation.MineOperationView
import com.se.music.mine.personal.MinePersonalInfoView
import com.se.music.mine.root.MineAdapter
import com.se.music.utils.IdUtils

/**
 * Author: gaojin
 * Time: 2018/4/22 下午9:35
 * 首页【我的】Tab页面
 */
class MvpMineFragment : Fragment(), KMvpPage {

    private val presenter: KMvpPresenter = KBasePresenter(this)
    private var recyclerView: RecyclerView? = null
    private var adapter: MineAdapter? = null

    companion object {
        fun newInstance(): MvpMineFragment {
            return MvpMineFragment()
        }
    }

    override fun onPageError(exception: Exception) {
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.fragment_mine_mvp, container, false)
        recyclerView = rootView.findViewById(R.id.mine_recycler_view)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        adapter = MineAdapter(context!!)
        recyclerView!!.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.add(MinePersonalInfoView(presenter, R.id.mine_personal_info, adapter!!.header!!))
        presenter.add(MineOperationView(presenter, R.id.mine_fun_area, adapter!!.header!!))
        presenter.add(MineSongListNameView(presenter, R.id.mine_song_list_title, adapter!!.header!!))

        presenter.add(QuerySongListModel(presenter, IdUtils.QUERY_SONG_LIST))

        presenter.start(IdUtils.QUERY_SONG_LIST)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        presenter.reload(IdUtils.QUERY_SONG_LIST)
    }

    @Keep
    fun onViewChanged(id: Int, collectEvent: CollectEvent) {
        adapter!!.notifyDataSetChanged()
    }

    @Keep
    fun onViewChanged(id: Int, createEvent: CreateEvent) {
        adapter!!.notifyDataSetChanged()
    }

    @Keep
    fun onModelChanged(id: Int, cursor: Cursor) {
        adapter!!.cursor = cursor
        adapter!!.notifyItemRangeChanged(1, cursor.count)
        presenter.dispatchModelDataToView(id, cursor, R.id.mine_song_list_title)
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