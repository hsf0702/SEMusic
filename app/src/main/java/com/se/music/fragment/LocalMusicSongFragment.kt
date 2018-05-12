package com.se.music.fragment

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.MusicApplication
import com.se.music.adapter.MusicListAdapter
import com.se.music.entity.MusicEntity
import com.se.music.pastmusic.R
import com.se.music.singleton.ApplicationSingleton
import com.se.music.utils.MConstants
import com.se.music.utils.MusicUtils


/**
 * Created by gaojin on 2018/2/28.
 */
class LocalMusicSongFragment : Fragment() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MusicListAdapter? = null
    var musicList: ArrayList<MusicEntity> = ArrayList()

    companion object {
        fun newInstance(): LocalMusicSongFragment {
            return LocalMusicSongFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_local_music, container, false)
        mAdapter = MusicListAdapter(context!!)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mRecyclerView = view.findViewById(R.id.local_music_recycle)
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.adapter = mAdapter
        refreshAdapter()

    }

    public fun refreshAdapter() {
        Log.e("gj", "refreshAdapter")
        object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String {
                musicList.clear()
                musicList.addAll(MusicUtils.queryMusic(ApplicationSingleton.instance!!, MConstants.START_FROM_LOCAL) as ArrayList)
                mAdapter!!.updateDataSet(musicList)
                return ""
            }

            override fun onPostExecute(result: String?) {
                mAdapter!!.notifyDataSetChanged()
            }
        }.execute()
    }
}