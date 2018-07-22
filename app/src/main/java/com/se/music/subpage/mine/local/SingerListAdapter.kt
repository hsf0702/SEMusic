package com.se.music.subpage.mine.local

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.GlideApp
import com.se.music.R
import com.se.music.entity.ArtistEntity
import com.se.music.provider.database.provider.ImageStore
import com.se.music.retrofit.MusicRetrofit
import com.se.music.retrofit.callback.CallLoaderCallbacks
import com.se.music.entity.Artist
import com.se.music.utils.IdUtils
import com.se.music.utils.getImageId
import com.se.music.utils.getMediumImageUrl
import retrofit2.Call
import java.util.*

/**
 *Author: gaojin
 *Time: 2018/7/5 下午9:23
 */

class SingerListAdapter constructor(private val context: Context, private val list: ArrayList<ArtistEntity>, private val loaderManager: LoaderManager) : RecyclerView.Adapter<SingerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        return SingerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.mine_local_singer_item, parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SingerViewHolder, position: Int) {
        val artistEntity = list[position]
        holder.singerName.text = artistEntity.artistName
        holder.singerSongCount.text = "${artistEntity.numberOfTracks}首"
        if (artistEntity.imageId == null
                || artistEntity.imageId!!.isEmpty()) {
            loaderManager.initLoader(IdUtils.generateLoaderId(), null, buildArtistCallBacks(holder, position))
        } else {
            setAvatarImage(artistEntity.imageId!!, holder.singerAvatar)
        }
    }

    private fun buildArtistCallBacks(holder: SingerViewHolder, position: Int): CallLoaderCallbacks<Artist> {
        return object : CallLoaderCallbacks<Artist>(context) {
            override fun onCreateCall(id: Int, args: Bundle?): Call<Artist> {
                return MusicRetrofit.instance.getSingAvatar(list[position].artistName)
            }

            override fun onSuccess(loader: Loader<*>, data: Artist) {
                if (data.image != null && data.image!!.isNotEmpty()) {
                    val imageId = getImageId(data.image!![0].imageUrl!!)
                    setAvatarImage(imageId, holder.singerAvatar)
                    list[position].imageId = imageId
                    //添加图片缓存
                    ImageStore.instance.addImage(list[position].artistKey, imageId)
                } else {
                    holder.singerAvatar.setImageResource(R.drawable.default_singer_avatar)
                }
            }

            override fun onFailure(loader: Loader<*>, throwable: Throwable) {
                Log.e("SingerListAdapter", throwable.toString())
            }
        }
    }

    fun setAvatarImage(imageId: String, avatar: ImageView) {
        GlideApp.with(context)
                .load(getMediumImageUrl(imageId))
                .placeholder(R.drawable.default_singer_avatar)
                .into(avatar)
    }
}

class SingerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val singerAvatar: ImageView = view.findViewById(R.id.local_singer_avatar)
    val singerName: TextView = view.findViewById(R.id.local_singer_name)
    val singerSongCount: TextView = view.findViewById(R.id.local_singer_number_of_tracks)
}