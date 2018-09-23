package com.se.music.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.service.MediaService
import com.se.music.service.MusicPlayer
import com.se.music.utils.blurBitmap

/**
 *Author: gaojin
 *Time: 2018/8/7 下午7:48
 */

class PlayingActivity : BaseActivity(), View.OnClickListener {

    private lateinit var activityBg: View
    private lateinit var toolbar: Toolbar

    private lateinit var songTitle: TextView

    private lateinit var repeatMode: ImageView
    private lateinit var preSong: ImageView
    private lateinit var centerControl: ImageView
    private lateinit var nextSong: ImageView
    private lateinit var songList: ImageView
    private lateinit var favorite: ImageView
    private lateinit var download: ImageView
    private lateinit var share: ImageView
    private lateinit var comment: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        showQuickControl(false)
        toolbarInit()
        initView()

        Glide.with(this)
                .asBitmap()
                .load("https://y.gtimg.cn/music/photo_new/T002R300x300M0000003YzXZ0ssGal.jpg")
                .into(object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val blurBitmap = blurBitmap(this@PlayingActivity, resource, 120)
                        if (blurBitmap != null) {
                            activityBg.background = getAlphaDrawable(blurBitmap)
                        }
                    }
                })
    }

    override fun onResume() {
        super.onResume()
        songTitle.text = MusicPlayer.getTrackName()
        updateRepeatStatus()
        updatePlayingStatus()
    }

    private fun initView() {
        activityBg = findViewById(R.id.player_activity_bg)
        songTitle = findViewById(R.id.playing_song_title)

        repeatMode = findViewById(R.id.repeat_mode)
        preSong = findViewById(R.id.pre_song)
        centerControl = findViewById(R.id.center_control)
        nextSong = findViewById(R.id.next_song)
        songList = findViewById(R.id.song_list)

        favorite = findViewById(R.id.playing_favorite)
        download = findViewById(R.id.playing_download)
        share = findViewById(R.id.playing_share)
        comment = findViewById(R.id.playing_comment)

        repeatMode.setOnClickListener(this)
        preSong.setOnClickListener(this)
        centerControl.setOnClickListener(this)
        nextSong.setOnClickListener(this)
        songList.setOnClickListener(this)
        favorite.setOnClickListener(this)
        download.setOnClickListener(this)
        share.setOnClickListener(this)
        comment.setOnClickListener(this)
    }

    private fun toolbarInit() {
        toolbar = findViewById(R.id.playing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.repeat_mode -> {
                setRepeatStatus()
                updateRepeatStatus()
            }
            R.id.pre_song -> {
                MusicPlayer.previous()
            }
            R.id.center_control -> {
                MusicPlayer.playOrPause()
                updatePlayingStatus()
            }
            R.id.next_song -> MusicPlayer.nextPlay()
        }
    }

    private fun updatePlayingStatus() {
        if (MusicPlayer.getIsPlaying()) {
            centerControl.setImageResource(R.drawable.default_player_btn_pause_selector)
        } else {
            centerControl.setImageResource(R.drawable.default_player_btn_play_selector)
        }
    }

    private fun setRepeatStatus() {
        when (MusicPlayer.getRepeatMode()) {
            MediaService.REPEAT_ALL -> MusicPlayer.setRepeatMode(MediaService.REPEAT_CURRENT)
            MediaService.REPEAT_CURRENT -> MusicPlayer.setRepeatMode(MediaService.REPEAT_SHUFFLER)
            MediaService.REPEAT_SHUFFLER -> MusicPlayer.setRepeatMode(MediaService.REPEAT_ALL)
        }
    }

    private fun updateRepeatStatus() {
        when (MusicPlayer.getRepeatMode()) {
            MediaService.REPEAT_CURRENT -> repeatMode.setImageResource(R.drawable.player_btn_repeat_once)
            MediaService.REPEAT_ALL -> repeatMode.setImageResource(R.drawable.player_btn_repeat)
            MediaService.REPEAT_SHUFFLER -> repeatMode.setImageResource(R.drawable.player_btn_random_normal)
        }
    }

    override fun updateTrack() {
        super.updateTrack()
        songTitle.text = MusicPlayer.getTrackName()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_setting, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_setting -> Toast.makeText(this, "设置", Toast.LENGTH_SHORT).show()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }

    private fun getAlphaDrawable(bg: Bitmap): Drawable {
        val newBitmap = Bitmap.createBitmap(bg.copy(Bitmap.Config.ARGB_8888, true))
        val canvas = Canvas(newBitmap)
        val paint = Paint()
        paint.color = ContextCompat.getColor(this, R.color.hex_33000000)
        canvas.drawRect(0f, 0f, bg.width.toFloat(), bg.height.toFloat(), paint)
        canvas.save()
        canvas.restore()
        return BitmapDrawable(resources, newBitmap)
    }
}