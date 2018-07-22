package com.se.music.mine.model

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.se.music.base.BaseActivity
import com.se.music.base.mvp.BaseModel
import com.se.music.base.mvp.MvpPresenter
import com.se.music.provider.metadata.SL_CONTENT_URI

/**
 *Author: gaojin
 *Time: 2018/5/21 下午10:22
 */

class QuerySongListModel(presenter: MvpPresenter, private val modelId: Int) : BaseModel<Cursor>(presenter, modelId), LoaderManager.LoaderCallbacks<Cursor> {

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val uri = SL_CONTENT_URI
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
}