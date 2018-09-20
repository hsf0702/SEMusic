package com.se.music.subpage.mine.local

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.BaseFragment
import com.se.music.entity.MusicEntity
import com.se.music.provider.metadata.infoMusic
import com.se.music.provider.metadata.localMusicUri
import com.se.music.provider.metadata.songSelection
import com.se.music.utils.QUERY_LOCAL_SONG
import com.se.music.singleton.SharePreferencesUtils
import com.se.music.utils.parseCursorToMusicEntityList


/**
 * Created by gaojin on 2018/2/28.
 */
class LocalSongFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: MusicListAdapter
    private val musicList: ArrayList<MusicEntity> = ArrayList()

    companion object {
        fun newInstance(): LocalSongFragment {
            return LocalSongFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRecyclerView = inflater.inflate(R.layout.fragment_local_recycler_view, container, false) as RecyclerView
        return mRecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = MusicListAdapter(context!!, musicList)

        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = adapter
        loaderManager.initLoader(QUERY_LOCAL_SONG, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!
                , localMusicUri
                , infoMusic
                , songSelection.toString()
                , null
                , SharePreferencesUtils.instance.getSongSortOrder())
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        parseCursorToMusicEntityList(QUERY_LOCAL_SONG, data, musicList)
        adapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}