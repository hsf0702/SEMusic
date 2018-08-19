package com.se.music.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.se.music.GlideApp
import com.se.music.R
import com.se.music.activity.PlayingActivity
import com.se.music.base.BaseFragment
import com.se.music.service.MusicPlayer

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

    lateinit var album: ImageView
    private lateinit var playBarSongName: TextView
    private lateinit var playBarSinger: TextView
    private lateinit var playList: ImageView
    lateinit var control: ImageView
    private lateinit var playNext: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.view_quick_controls, container, false)
        album = root.findViewById(R.id.play_bar_img)
        playBarSongName = root.findViewById(R.id.play_bar_song_name)
        playBarSinger = root.findViewById(R.id.play_bar_singer)
        playList = root.findViewById(R.id.play_list)
        control = root.findViewById(R.id.control)
        playNext = root.findViewById(R.id.play_next)

        root.setOnClickListener(this)
        playList.setOnClickListener(this)
        control.setOnClickListener(this)
        playNext.setOnClickListener(this)
        return root
    }

    override fun onResume() {
        super.onResume()
        updateFragment()
    }

    override fun onClick(v: View?) {
        when {
            v!!.id == R.id.play_list -> {
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
        playBarSongName.text = MusicPlayer.getTrackName()
        playBarSinger.text = MusicPlayer.getArtistName()
        if (MusicPlayer.getIsPlaying()) {
            control.setImageResource(R.drawable.playbar_btn_pause)
        } else {
            control.setImageResource(R.drawable.playbar_btn_play)
        }
        if (MusicPlayer.getAlbumPic() != null) {
            GlideApp.with(context!!)
                    .load(MusicPlayer.getAlbumPic())
                    .into(album)
        }
    }
}