package com.se.music.subpage.mine.local

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.app.LoaderManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.R
import com.se.music.entity.AlbumEntity
import com.se.music.utils.inflate
import com.se.music.utils.loadAlbumPic
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
        holder.albumPic.loadAlbumPic(context, albumEntity, loaderManager, R.drawable.default_album_avatar)
    }
}

class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val albumPic: ImageView = view.findViewById(R.id.local_album_pic)
    val albumName: TextView = view.findViewById(R.id.local_album_name)
    val albumSongCount: TextView = view.findViewById(R.id.local_album_number_of_tracks)
}