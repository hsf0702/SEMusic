package com.se.music.mine.model

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import com.se.music.base.BaseActivity
import com.se.music.base.mvp.BaseModel
import com.se.music.base.mvp.MvpPresenter
import com.se.music.provider.metadata.START_FROM_ALBUM
import com.se.music.provider.metadata.START_FROM_ARTIST
import com.se.music.provider.metadata.START_FROM_LOCAL
import com.se.music.utils.SharePreferencesUtils

/**
 *Author: gaojin
 *Time: 2018/5/24 下午11:49
 */

class QueryLocalSongModel(presenter: MvpPresenter, private var modelId: Int, private var from: Int) : BaseModel<Cursor>(presenter, modelId)
        , LoaderManager.LoaderCallbacks<Cursor> {
    //用于检索本地文件
    private val mFilterSize = 1024 * 1024// 1MB
    private val mFilterDuration = 60 * 1000// 1分钟
    private val mUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI!!

    //查询数据库的列名称
    private val infoMusic = arrayOf(MediaStore.Audio.Media._ID          //音乐ID
            , MediaStore.Audio.Media.TITLE      //音乐的标题
            , MediaStore.Audio.Media.DATA       //日期
            , MediaStore.Audio.Media.ALBUM_ID   //专辑ID
            , MediaStore.Audio.Media.ALBUM      //专辑
            , MediaStore.Audio.Media.ARTIST     //艺术家
            , MediaStore.Audio.Media.ARTIST_ID  //艺术家ID
            , MediaStore.Audio.Media.DURATION   //音乐时长
            , MediaStore.Audio.Media.SIZE)     //音乐大小

    override fun load() {
        (getActivity() as BaseActivity).supportLoaderManager.initLoader(modelId, null, this)
    }

    override fun reload() {
        (getActivity() as BaseActivity).supportLoaderManager.restartLoader(modelId, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val select = StringBuilder(" 1=1 and title != ''")
        // 查询语句：检索出.mp3为后缀名，时长大于1分钟，文件大小大于1MB的媒体文件
        select.append(" and " + MediaStore.Audio.Media.SIZE + " > " + mFilterSize)
        select.append(" and " + MediaStore.Audio.Media.DURATION + " > " + mFilterDuration)

        return when (from) {
            START_FROM_LOCAL -> {
                CursorLoader(getContext()!!, mUri, infoMusic, select.toString(), null, SharePreferencesUtils.instance.getSongSortOrder())
            }
            START_FROM_ARTIST -> {
                select.append(" and " + MediaStore.Audio.Media.ARTIST_ID + " = " + id)
                CursorLoader(getContext()!!, mUri, infoMusic, select.toString(), null, SharePreferencesUtils.instance.getArtistSortOrder())
            }
            START_FROM_ALBUM -> {
                select.append(" and " + MediaStore.Audio.Media.ALBUM_ID + " = " + id)
                CursorLoader(getContext()!!, mUri, infoMusic, select.toString(), null, SharePreferencesUtils.instance.getAlbumSortOrder())
            }
            else -> {
                CursorLoader(getContext()!!, mUri, infoMusic, select.toString(), null, SharePreferencesUtils.instance.getSongSortOrder())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) {
            dispatchData(data)
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
    }
}