package com.se.music.utils

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.v4.app.LoaderManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.se.music.GlideApp
import com.se.music.entity.AlbumEntity

/**
 *Author: gaojin
 *Time: 2018/9/2 下午5:20
 */

fun ViewGroup.inflate(layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun ImageView.loadUrl(url: String?) {
    GlideApp.with(context)
            .load(url)
            .into(this)
}

fun ImageView.loadUrl(url: String?, @DrawableRes drawableRes: Int) {
    GlideApp.with(context)
            .load(url)
            .placeholder(drawableRes)
            .into(this)
}

fun ImageView.loadAlbumPic(context: Context, albumEntity: AlbumEntity, loaderManager: LoaderManager, @DrawableRes drawableRes: Int) {
    if (albumEntity.imageUrl.isEmpty()
            && !albumEntity.albumArtist.isEmpty()
            && albumEntity.albumName.isEmpty()) {
        loaderManager.initLoader(generateLoaderId(), null, buildAlbumCallBacks(context, this, albumEntity))
    } else {
        this.loadUrl(albumEntity.imageUrl.getMegaImageUrl(), drawableRes)
    }
}