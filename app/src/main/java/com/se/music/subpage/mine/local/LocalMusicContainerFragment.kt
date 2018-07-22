package com.se.music.subpage.mine.local


import android.database.Cursor
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.se.music.R
import com.se.music.base.BasePageFragment
import com.se.music.provider.metadata.*
import com.se.music.utils.IdUtils
import com.se.music.utils.SharePreferencesUtils

/**
 * Author: gaojin
 * Time: 2018/5/13 下午6:26
 */
class LocalMusicContainerFragment : BasePageFragment() {

    companion object {
        private const val mPosition = "mPosition"
        fun newInstance(position: Int): LocalMusicContainerFragment {
            val fragment = LocalMusicContainerFragment()
            val args = Bundle()
            args.putInt(mPosition, position)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mAdapter: LocalFragmentAdapter
    private var position: Int = 0
    private lateinit var mTabLayout: TabLayout
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            position = arguments!!.getInt(mPosition)
        }
    }

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val content = LayoutInflater.from(context).inflate(R.layout.fragment_local_music_kotlin, container, false)
        mTabLayout = content.findViewById(R.id.local_tab_layout)
        mViewPager = content.findViewById(R.id.local_view_pager)
        return content
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(context!!.getString(R.string.local_music_title))
        loaderManager.initLoader(IdUtils.QUERY_LOCAL_SONG, null, buildLocalMusicCallBack())
        loaderManager.initLoader(IdUtils.QUERY_LOCAL_SINGER, null, buildLocalSingerCallBack())
        loaderManager.initLoader(IdUtils.QUERY_LOCAL_ALBUM, null, buildLocalAlbumCallBack())
        loaderManager.initLoader(IdUtils.QUERY_FOLDER, null, buildFolderCallBack())
        mAdapter = LocalFragmentAdapter(fm, context!!)
        mViewPager.adapter = mAdapter
        mViewPager.currentItem = position
        mTabLayout.setupWithViewPager(mViewPager)
    }

    private fun buildLocalMusicCallBack(): LoaderManager.LoaderCallbacks<Cursor> {
        return object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                //TODO loading界面
                return CursorLoader(context!!, localMusicUri, infoMusic, songSelection.toString(), null, SharePreferencesUtils.instance.getSongSortOrder())
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                mAdapter.setTitle(0, context!!.getString(R.string.local_music_song, data.count))
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        }
    }

    private fun buildLocalSingerCallBack(): LoaderManager.LoaderCallbacks<Cursor> {
        return object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return CursorLoader(context!!, localSingerUri, info_artist, singerSelection.toString(), null, SharePreferencesUtils.instance.getArtistSortOrder())
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                mAdapter.setTitle(1, context!!.getString(R.string.local_music_singer, data.count))
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        }
    }

    private fun buildLocalAlbumCallBack(): LoaderManager.LoaderCallbacks<Cursor> {
        return object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return CursorLoader(context!!, localAlbumUri, info_album, albumSelection.toString(), null, SharePreferencesUtils.instance.getAlbumSortOrder())
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                mAdapter.setTitle(2, context!!.getString(R.string.local_music_album, data.count))
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        }
    }

    private fun buildFolderCallBack(): LoaderManager.LoaderCallbacks<Cursor> {
        return object : LoaderManager.LoaderCallbacks<Cursor> {
            override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
                return CursorLoader(context!!, folderUri, info_folder, folderSelection.toString(), null, null)
            }

            override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
                mAdapter.setTitle(3, context!!.getString(R.string.local_music_folder, data.count))
            }

            override fun onLoaderReset(loader: Loader<Cursor>) {
            }
        }
    }
}
