package com.se.music.subpage.mine.local

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.common.MusicEntity
import com.se.music.provider.metadata.infoMusic
import com.se.music.provider.metadata.localMusicUri
import com.se.music.provider.metadata.songSelection
import com.se.music.utils.IdUtils
import com.se.music.utils.SharePreferencesUtils
import com.se.music.utils.parseCursorToMusicEntityList


/**
 * Created by gaojin on 2018/2/28.
 */
class LocalSongFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MusicListAdapter
    var musicList: ArrayList<MusicEntity> = ArrayList()

    companion object {
        fun newInstance(): LocalSongFragment {
            return LocalSongFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_local_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mAdapter = MusicListAdapter(context!!, musicList)
        mRecyclerView = view.findViewById(R.id.local_music_recycle)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = mAdapter

        loaderManager.initLoader(IdUtils.QUERY_LOCAL_SONG, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!, localMusicUri, infoMusic, songSelection.toString(), null, SharePreferencesUtils.instance.getSongSortOrder())
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        musicList.addAll(parseCursorToMusicEntityList(IdUtils.QUERY_LOCAL_SONG, data))
        mAdapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}