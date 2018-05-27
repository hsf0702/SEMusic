package com.se.music.mine

import android.content.Intent
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
import com.se.music.base.mvp.BasePresenter
import com.se.music.base.mvp.MvpPage
import com.se.music.base.mvp.MvpPresenter
import com.se.music.common.SongListEntity
import com.se.music.mine.event.CollectEvent
import com.se.music.mine.event.CreateEvent
import com.se.music.mine.listtitle.MineSongListTitleView
import com.se.music.mine.model.QueryLocalSongModel
import com.se.music.mine.model.QuerySongListModel
import com.se.music.mine.operation.MineOperationView
import com.se.music.mine.personal.MinePersonalInfoView
import com.se.music.mine.root.MineAdapter
import com.se.music.provider.metadata.START_FROM_LOCAL
import com.se.music.subpage.mine.CreateSongListActivity
import com.se.music.utils.IdUtils
import com.se.music.utils.parseCursorToSongList

/**
 * Author: gaojin
 * Time: 2018/4/22 下午9:35
 * 首页【我的】Tab页面
 */
class MvpMineFragment : Fragment(), MvpPage {
    private val presenter: MvpPresenter = BasePresenter(this)
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MineAdapter
    private val list = ArrayList<SongListEntity>()

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
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = MineAdapter(context!!, list)
        recyclerView.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.add(MinePersonalInfoView(presenter, R.id.mine_personal_info, adapter.header!!))
        presenter.add(MineOperationView(presenter, R.id.mine_fun_area, adapter.header!!))
        presenter.add(MineSongListTitleView(presenter, R.id.mine_song_list_title, adapter.header!!))

        presenter.add(QuerySongListModel(presenter, IdUtils.QUERY_SONG_LIST))
        presenter.add(QueryLocalSongModel(presenter, IdUtils.QUERY_LOCAL_SONG, START_FROM_LOCAL))

        presenter.start(IdUtils.QUERY_SONG_LIST
                , IdUtils.QUERY_LOCAL_SONG)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == CreateSongListActivity.resultCode) {
//            presenter.reload(IdUtils.QUERY_SONG_LIST)
        }
    }

    @Keep
    fun onViewChanged(id: Int, collectEvent: CollectEvent) {
        adapter.notifyDataSetChanged()
    }

    @Keep
    fun onViewChanged(id: Int, createEvent: CreateEvent) {
        adapter.notifyDataSetChanged()
    }

    @Keep
    fun onModelChanged(id: Int, cursor: Cursor) {
        if (id == IdUtils.QUERY_SONG_LIST) {
            list.addAll(parseCursorToSongList(id, cursor))
            if (!list.isEmpty()) {
                adapter.notifyItemRangeChanged(1, list.size)
            }
            presenter.dispatchModelDataToView(id, cursor, R.id.mine_song_list_title)
        } else if (id == IdUtils.QUERY_LOCAL_SONG) {
            presenter.dispatchModelDataToView(id, cursor, R.id.mine_fun_area)
        }
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