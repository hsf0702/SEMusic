package com.se.music.utils

import android.content.Context
import android.os.Bundle
import android.support.v4.content.Loader
import android.widget.ImageView
import com.se.music.R
import com.se.music.entity.Album
import com.se.music.entity.AlbumEntity
import com.se.music.provider.database.provider.ImageStore
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import retrofit2.Call

/**
 *Author: gaojin
 *Time: 2018/9/30 下午5:33
 */

fun buildAlbumCallBacks(context: Context, imageView: ImageView, albumEntity: AlbumEntity): CallLoaderCallbacks<Album> {
    return object : CallLoaderCallbacks<Album>(context) {
        override fun onCreateCall(id: Int, args: Bundle?): Call<Album> {
            return MusicRetrofit.instance.getAlbumInfo(albumEntity.albumArtist, albumEntity.albumName)
        }

        override fun onSuccess(loader: Loader<*>, data: Album) {
            if (data.image != null && data.image!!.isNotEmpty()) {
                val imageId = data.image!![0].imageUrl!!.getImageId()
                imageView.loadUrl(imageId.getMegaImageUrl(), R.drawable.default_album_avatar)
                //添加图片缓存
                ImageStore.instance.addImage(albumEntity.albumKey, imageId)
            } else {
                imageView.setImageResource(R.drawable.default_album_avatar)
            }
        }

        override fun onFailure(loader: Loader<*>, throwable: Throwable) {
        }
    }
}