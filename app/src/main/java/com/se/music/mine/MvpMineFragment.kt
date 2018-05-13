package com.se.music.mine

import android.os.Bundle
import android.support.annotation.Keep
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import com.se.music.database.provider.SongListDBService
import com.se.music.entity.SongListEntity
import com.se.music.kmvp.KBasePresenter
import com.se.music.kmvp.KMvpPage
import com.se.music.kmvp.KMvpPresenter
import com.se.music.mine.event.CollectEvent
import com.se.music.mine.event.CreateEvent
import com.se.music.mine.listname.MineSongListNameView
import com.se.music.mine.operation.MineOperationView
import com.se.music.mine.personal.MinePersonalInfoView
import com.se.music.mine.root.MineAdapter
import com.se.music.pastmusic.R

/**
 * Author: gaojin
 * Time: 2018/4/22 下午9:35
 * 首页【我的】Tab页面
 */
class MvpMineFragment : Fragment(), KMvpPage {

    private val presenter: KMvpPresenter = KBasePresenter(this)
    private var recyclerView: RecyclerView? = null
    private var adapter: MineAdapter? = null
    private var list = ArrayList<SongListEntity>()
    private var localSongList: List<SongListEntity>? = null
    private var collectedSongList: List<SongListEntity>? = null

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
        adapter = MineAdapter(context!!, list)
        recyclerView!!.adapter = adapter
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.add(MinePersonalInfoView(presenter, R.id.mine_personal_info, adapter!!.header!!))
        presenter.add(MineOperationView(presenter, R.id.mine_fun_area, adapter!!.header!!))
        presenter.add(MineSongListNameView(presenter, R.id.mine_song_list_title, adapter!!.header!!))
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        list.clear()
        localSongList = SongListDBService.instance.query()
        collectedSongList = SongListDBService.instance.query()
        list.addAll(localSongList!!)
        if (!list.isEmpty()) {
            adapter!!.notifyItemRangeChanged(1, list.size)
        }
    }

    @Keep
    fun onViewChanged(id: Int, collectEvent: CollectEvent) {
        list.clear()
        list.addAll(collectedSongList!!)
        adapter!!.notifyDataSetChanged()
    }

    @Keep
    fun onViewChanged(id: Int, createEvent: CreateEvent) {
        list.clear()
        list.addAll(localSongList!!)
        adapter!!.notifyDataSetChanged()
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