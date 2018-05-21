package com.se.music.mine.model

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.se.music.base.BaseActivity
import com.se.music.base.kmvp.KBaseModel
import com.se.music.base.kmvp.KMvpPresenter
import com.se.music.utils.database.DataBaseMetaData

/**
 *Author: gaojin
 *Time: 2018/5/21 下午10:22
 */

class QuerySongListModel(presenter: KMvpPresenter, private val modelId: Int) : KBaseModel<Cursor>(presenter, modelId), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = DataBaseMetaData.SongList.CONTENT_URI
        return CursorLoader(getContext()!!, uri, null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {
            dispatchData(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }

    override fun load() {
        (getActivity() as BaseActivity).supportLoaderManager.initLoader(modelId, null, this)
    }

    override fun reload() {
        (getActivity() as BaseActivity).supportLoaderManager.restartLoader(modelId, null, this)
    }
}