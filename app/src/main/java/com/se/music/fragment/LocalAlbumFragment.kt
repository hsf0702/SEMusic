package com.se.music.fragment

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
import com.se.music.adapter.AlbumListAdapter
import com.se.music.base.BaseFragment
import com.se.music.entity.AlbumEntity
import com.se.music.provider.metadata.albumSelection
import com.se.music.provider.metadata.info_album
import com.se.music.provider.metadata.localAlbumUri
import com.se.music.utils.QUERY_LOCAL_ALBUM
import com.se.music.utils.parseCursorToAlbumEntityList

/**
 *Author: gaojin
 *Time: 2018/7/8 下午5:21
 */

class LocalAlbumFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: AlbumListAdapter
    private val list = ArrayList<AlbumEntity>()

    companion object {
        fun newInstance(): LocalAlbumFragment {
            return LocalAlbumFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRecyclerView = inflater.inflate(R.layout.fragment_local_recycler_view, container, false) as RecyclerView
        return mRecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = AlbumListAdapter(context!!, list, loaderManager)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = adapter
        loaderManager.initLoader(QUERY_LOCAL_ALBUM, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!
                , localAlbumUri
                , info_album
                , albumSelection.toString()
                , null
                , null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        parseCursorToAlbumEntityList(QUERY_LOCAL_ALBUM, data, list)
        adapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}