package com.se.music.subpage.mine.local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.R
import com.se.music.entity.Album
import com.se.music.entity.AlbumEntity
import com.se.music.provider.database.provider.ImageStore
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import com.se.music.utils.*
import retrofit2.Call
import java.util.*


/**
 *Author: gaojin
 *Time: 2018/7/9 下午8:17
 */

class AlbumListAdapter constructor(private val context: Context, private val list: ArrayList<AlbumEntity>
                                   , private val loaderManager: LoaderManager) : RecyclerView.Adapter<AlbumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(parent.inflate(R.layout.mine_local_album_item))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val albumEntity = list[position]
        holder.albumName.text = albumEntity.albumName
        holder.albumSongCount.text = "${albumEntity.numberOfSongs}首"
        if (list[position].imageId == null
                || list[position].imageId!!.isEmpty()) {
            loaderManager.initLoader(generateLoaderId(), null, buildAlbumCallBacks(holder, position))
        } else {
            holder.albumPic.loadUrl(getMegaImageUrl(albumEntity.imageId!!), R.drawable.default_album_avatar)
        }
    }

    private fun buildAlbumCallBacks(holder: AlbumViewHolder, position: Int): CallLoaderCallbacks<Album> {
        return object : CallLoaderCallbacks<Album>(context) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<Album> {
                return MusicRetrofit.instance.getAlbumInfo(list[position].albumArtist, list[position].albumName)
            }

            override fun onSuccess(loader: Loader<*>, data: Album) {
                if (data.image != null && data.image!!.isNotEmpty()) {
                    val imageId = getImageId(data.image!![0].imageUrl!!)
                    holder.albumPic.loadUrl(getMegaImageUrl(imageId), R.drawable.default_album_avatar)
                    list[position].imageId = imageId
                    //添加图片缓存
                    ImageStore.instance.addImage(list[position].albumKey, imageId)
                } else {
                    holder.albumPic.setImageResource(R.drawable.default_album_avatar)
                }
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
            }
        }
    }
}

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val albumPic: ImageView = view.findViewById(R.id.local_album_pic)
    val albumName: TextView = view.findViewById(R.id.local_album_name)
    val albumSongCount: TextView = view.findViewById(R.id.local_album_number_of_tracks)
}