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
import com.se.music.entity.ArtistEntity
import com.se.music.provider.metadata.info_artist
import com.se.music.provider.metadata.localSingerUri
import com.se.music.provider.metadata.singerSelection
import com.se.music.utils.IdUtils
import com.se.music.utils.parseCursorToArtistEntityList

/**
 *Author: gaojin
 *Time: 2018/5/31 下午11:32
 */

class LocalSingerFragment : BaseFragment(), LoaderManager.LoaderCallbacks<Cursor> {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var adapter: SingerListAdapter
    private val list = ArrayList<ArtistEntity>()

    companion object {
        fun newInstance(): LocalSingerFragment {
            return LocalSingerFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mRecyclerView = inflater.inflate(R.layout.fragment_local_recycler_view, container, false) as RecyclerView
        return mRecyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = SingerListAdapter(context!!, list, loaderManager)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.adapter = adapter
        loaderManager.initLoader(IdUtils.QUERY_LOCAL_SINGER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(context!!
                , localSingerUri
                , info_artist
                , singerSelection.toString()
                , null
                , null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        parseCursorToArtistEntityList(IdUtils.QUERY_LOCAL_SINGER, data, list)
        adapter.notifyDataSetChanged()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {}

}