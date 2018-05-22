package com.se.music.mine.listtitle

import android.content.Intent
import android.database.Cursor
import android.support.annotation.Keep
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.se.music.R
import com.se.music.activity.CreateSongListActivity
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.mine.event.CollectEvent
import com.se.music.mine.event.CreateEvent

/**
 * Author: gaojin
 * Time: 2018/5/7 下午9:22
 */
class MineSongListTitleView(presenter: KMvpPresenter
                            , viewId: Int
                            , view: View) : KBaseView(presenter, viewId), View.OnClickListener {

    private var titleCreated: TextView? = null
    private var titleCollected: TextView? = null

    private var songListCount: TextView? = null
    private var addSongList: View? = null
    private var manageSongList: View? = null
    private var llCreateNewSongList: View? = null
    private var tvNoCollectedSongList: View? = null

    private var createSongListSize = 0
    private var collectedSongListSize = 0
    private var isCreateTab = true

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
        if (isCreateTab) {
            titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
            titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))
        }
    }

    private fun clickCreated() {
        if (isCreateTab) {
            return
        }
        titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
        titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))
        songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, createSongListSize)
        addSongList!!.visibility = View.VISIBLE
        tvNoCollectedSongList!!.visibility = View.GONE
        llCreateNewSongList!!.visibility = if (createSongListSize == 0) View.VISIBLE else View.GONE
        dispatchData(CreateEvent())
        isCreateTab = true
    }

    private fun clickCollected() {
        if (!isCreateTab) {
            return
        }
        titleCollected!!.setTextColor(getContext()!!.resources.getColor(R.color.light_black))
        titleCreated!!.setTextColor(getContext()!!.resources.getColor(R.color.gray))
        songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, collectedSongListSize)
        addSongList!!.visibility = View.GONE
        llCreateNewSongList!!.visibility = View.GONE
        tvNoCollectedSongList!!.visibility = if (collectedSongListSize == 0) View.VISIBLE else View.GONE
        dispatchData(CollectEvent())
        isCreateTab = false
    }

    private fun createNewSongList() {
        val intent = Intent(getContext(), CreateSongListActivity::class.java)
        getActivity()!!.startActivityForResult(intent, 0)
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

    @Keep
    fun onDataChanged(cursor: Cursor) {
        createSongListSize = cursor.count
        collectedSongListSize = cursor.count

        if (isCreateTab) {
            songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, createSongListSize)
            tvNoCollectedSongList!!.visibility = View.GONE
            llCreateNewSongList!!.visibility = if (createSongListSize == 0) View.VISIBLE else View.GONE
        } else {
            songListCount!!.text = getContext()!!.resources.getString(R.string.song_list_count, collectedSongListSize)
            llCreateNewSongList!!.visibility = View.GONE
            tvNoCollectedSongList!!.visibility = if (collectedSongListSize == 0) View.VISIBLE else View.GONE
        }
    }
}