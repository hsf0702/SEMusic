package com.se.music.main

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import com.se.music.R
import com.se.music.activity.PlayingActivity
import com.se.music.base.BaseFragment
import com.se.music.base.Null
import com.se.music.entity.AlbumEntity
import com.se.music.service.MusicPlayer
import com.se.music.utils.isContentEmpty
import com.se.music.utils.loadAlbumPic


/**
 *Author: gaojin
 *Time: 2018/5/23 下午10:32
 */

class QuickControlsFragment : BaseFragment(), View.OnClickListener {

    companion object {
        fun newInstance(): QuickControlsFragment {
            return QuickControlsFragment()
        }
    }

    private lateinit var album: ImageView
    private lateinit var playBarSongName: TextView
    private lateinit var playBarSinger: TextView
    private lateinit var playList: ImageView
    private lateinit var control: ImageView
    private lateinit var playNext: ImageView
    private lateinit var mLogan: TextView
    private lateinit var circleAnim: ObjectAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.view_quick_controls, container, false)
        album = root.findViewById(R.id.play_bar_img)
        playBarSongName = root.findViewById(R.id.play_bar_song_name)
        playBarSinger = root.findViewById(R.id.play_bar_singer)
        playList = root.findViewById(R.id.play_list)
        control = root.findViewById(R.id.control)
        playNext = root.findViewById(R.id.play_next)
        mLogan = root.findViewById(R.id.music_logan)

        val albumCenterPoint = resources.getDimension(R.dimen.bottom_fragment_album_size) / 2
        album.pivotX = albumCenterPoint
        album.pivotY = albumCenterPoint

        circleAnim = ObjectAnimator.ofFloat(album, "rotation", 0.toFloat(), 360.toFloat())
        circleAnim.interpolator = LinearInterpolator()
        circleAnim.repeatCount = -1
        circleAnim.duration = 12000
        circleAnim.start()

        root.setOnClickListener(this)
        root.elevation = 100.toFloat()

        playList.setOnClickListener(this)
        control.setOnClickListener(this)
        playNext.setOnClickListener(this)
        return root
    }

    override fun onResume() {
        super.onResume()
        updateFragment()
    }

    override fun onClick(v: View) {
        when {
            v.id == R.id.play_list -> {
            }
            v.id == R.id.control -> MusicPlayer.playOrPause()
            v.id == R.id.play_next -> MusicPlayer.nextPlay()
            else -> {
                val intent = Intent(activity, PlayingActivity::class.java)
                startActivity(intent)
                activity!!.overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out)
            }
        }
    }

    override fun updatePlayInfo() {
        updateFragment()
    }

    private fun updateFragment() {
        val albumEntity = AlbumEntity(MusicPlayer.getCurrentAlbumId()
                , MusicPlayer.getAlbumName()
                , 0
                , MusicPlayer.getArtistName()
                , Null)
        albumEntity.imageUrl = MusicPlayer.getAlbumPic()

        album.loadAlbumPic(context!!, albumEntity, loaderManager, R.drawable.player_albumcover_default)

        if (MusicPlayer.getTrackName().isContentEmpty() && MusicPlayer.getArtistName().isContentEmpty()) {
            playBarSongName.visibility = View.GONE
            playBarSinger.visibility = View.GONE
            album.visibility = View.GONE
            mLogan.visibility = View.VISIBLE
        } else {
            playBarSongName.visibility = View.VISIBLE
            playBarSinger.visibility = View.VISIBLE
            album.visibility = View.VISIBLE
            mLogan.visibility = View.GONE
            playBarSongName.text = MusicPlayer.getTrackName()
            playBarSinger.text = MusicPlayer.getArtistName()
        }

        if (MusicPlayer.isPlaying()) {
            control.setImageResource(R.drawable.playbar_btn_pause)
            circleAnim.resume()
        } else {
            control.setImageResource(R.drawable.playbar_btn_play)
            circleAnim.pause()
        }
    }
}