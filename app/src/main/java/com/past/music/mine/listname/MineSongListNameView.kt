package com.past.music.mine.listname

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.past.music.activity.CreateSongListActivity
import com.past.music.entity.SongListEntity
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.pastmusic.R

/**
 * Author: gaojin
 * Time: 2018/5/7 下午9:22
 */
class MineSongListNameView(presenter: KMvpPresenter
                           , viewId: Int
                           , view: View
                           , private var list: List<SongListEntity>) : KBaseView(presenter, viewId), View.OnClickListener {

    private var titleCreated: TextView? = null
    private var titleCollected: TextView? = null

    private var songListCount: TextView? = null
    private var addSongList: View? = null
    private var manageSongList: View? = null
    private var llCreateNewSongList: View? = null
    private var tvNoCollectedSongList: View? = null

    init {
        initView(view)
    }

    override fun createView(): View {
        val rootView = LayoutInflater.from(getContext()).inflate(R.layout.mine_song_list_title_layout, null)

        titleCreated = rootView.findViewById(R.id.tv_title_created)
        titleCollected = rootView.findViewById(R.id.tv_title_collected)
        songListCount = rootView.findViewById(R.id.song_list_count)
        addSongList = rootView.findViewById(R.id.add_song_list)
        manageSongList = rootView.findViewById(R.id.manage_song_list)
        llCreateNewSongList = rootView.findViewById(R.id.ll_create_new_song_list)
        tvNoCollectedSongList = rootView.findViewById(R.id.no_collected_song_list)

        titleCreated!!.setOnClickListener(this)
        titleCollected!!.setOnClickListener(this)
        addSongList!!.setOnClickListener(this)
        manageSongList!!.setOnClickListener(this)
        llCreateNewSongList!!.setOnClickListener(this)

        bindView()
        return rootView
    }

    private fun bindView() {
        titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
        titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))

        if (list.isEmpty()) {
            songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, 0)
            llCreateNewSongList!!.visibility = View.VISIBLE
            tvNoCollectedSongList!!.visibility = View.GONE
        } else {
            songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, list.size)
            llCreateNewSongList!!.visibility = View.GONE
        }
    }

    private fun clickCreated() {
        titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
        titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))
        tvNoCollectedSongList!!.visibility = View.GONE
        addSongList!!.visibility = View.VISIBLE
        if (list.isEmpty()) {
            llCreateNewSongList!!.visibility = View.VISIBLE
        }
    }

    private fun clickCollected() {
        titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
        titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))
        llCreateNewSongList!!.visibility = View.GONE
        addSongList!!.visibility = View.GONE
        tvNoCollectedSongList!!.visibility = View.VISIBLE
    }

    private fun createNewSongList() {
        val intent = Intent(getContext(), CreateSongListActivity::class.java)
        getActivity()!!.startActivity(intent)
        getActivity()!!.overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_title_created -> clickCreated()
            R.id.tv_title_collected -> clickCollected()
            R.id.ll_create_new_song_list -> createNewSongList()
            R.id.add_song_list -> createNewSongList()
        }
    }

}