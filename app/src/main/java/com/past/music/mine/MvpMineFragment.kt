package com.past.music.mine

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.past.music.database.provider.SongListDBService
import com.past.music.entity.SongListEntity
import com.past.music.kmvp.KBasePresenter
import com.past.music.kmvp.KMvpPage
import com.past.music.kmvp.KMvpPresenter
import com.past.music.mine.listname.MineSongListNameView
import com.past.music.mine.operation.MineOperationView
import com.past.music.mine.personal.MinePersonalInfoView
import com.past.music.mine.root.MineAdapter
import com.past.music.pastmusic.R

/**
 * Author: gaojin
 * Time: 2018/4/22 下午9:35
 */
class MvpMineFragment : Fragment(), KMvpPage {

    private val presenter: KMvpPresenter = KBasePresenter(this)
    private var recyclerView: RecyclerView? = null
    private var adapter: MineAdapter? = null
    private var list = ArrayList<SongListEntity>()

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
        presenter.add(MineSongListNameView(presenter, R.id.mine_song_list_title, adapter!!.header!!, list))
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
        list.addAll(SongListDBService.instance.query())
        if (!list.isEmpty()) {
            adapter!!.notifyItemRangeChanged(1, list.size)
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