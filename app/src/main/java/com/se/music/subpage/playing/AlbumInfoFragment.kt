package com.se.music.subpage.playing

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.TextView
import com.se.music.R
import com.se.music.base.BaseFragment
import com.se.music.service.MusicPlayer
import com.se.music.utils.getMegaImageUrl
import com.se.music.utils.loadUrl
import com.se.music.widget.CircleImageView

/**
 *Author: gaojin
 *Time: 2018/10/7 下午7:49
 */

class AlbumInfoFragment : BaseFragment() {
    companion object {
        fun newInstance(): AlbumInfoFragment {
            return AlbumInfoFragment()
        }
    }

    private lateinit var albumView: CircleImageView
    private lateinit var artistName: TextView
    private lateinit var circleAnim: ObjectAnimator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_playing_album_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        albumView = view.findViewById(R.id.album_pic)
        artistName = view.findViewById(R.id.artist_name)

        circleAnim = ObjectAnimator.ofFloat(albumView, "rotation", 0.toFloat(), 360.toFloat())
        circleAnim.interpolator = LinearInterpolator()
        circleAnim.repeatCount = -1
        circleAnim.duration = 30000
        circleAnim.start()

        artistName.text = MusicPlayer.getArtistName()
        if (MusicPlayer.getAlbumPic().isEmpty()) {
            albumView.setImageResource(R.drawable.player_albumcover_default)
        } else {
            albumView.loadUrl(MusicPlayer.getAlbumPic().getMegaImageUrl(), R.drawable.player_albumcover_default)
        }
    }

    override fun musciChanged() {
        circleAnim.start()
        setAnimation()
        artistName.text = MusicPlayer.getArtistName()
        if (MusicPlayer.getAlbumPic().isEmpty()) {
            albumView.setImageResource(R.drawable.player_albumcover_default)
        } else {
            albumView.loadUrl(MusicPlayer.getAlbumPic().getMegaImageUrl(), R.drawable.player_albumcover_default)
        }
    }

    private fun setAnimation() {
        if (MusicPlayer.isPlaying()) {
            if (circleAnim.isPaused) {
                circleAnim.resume()
            }
        } else {
            if (circleAnim.isRunning) {
                circleAnim.pause()
            }
        }
    }

    override fun updatePlayInfo() {
        setAnimation()
    }
}