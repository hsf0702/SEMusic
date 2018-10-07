package com.se.music.block

import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.se.music.R
import com.se.music.service.MediaService
import com.se.music.service.MusicPlayer
import com.se.music.singleton.HandlerSingleton
import com.se.music.utils.ms2Minute

/**
 *Author: gaojin
 *Time: 2018/9/27 下午5:37
 * 播放音乐底部操作区域
 */
class PlayingBottomBlock : LinearLayout, View.OnClickListener, SeekBar.OnSeekBarChangeListener, ViewBlockAction {

    private lateinit var repeatMode: ImageView
    private lateinit var preSong: ImageView
    private lateinit var centerControl: ImageView
    private lateinit var nextSong: ImageView
    private lateinit var songList: ImageView
    private lateinit var favorite: ImageView
    private lateinit var download: ImageView
    private lateinit var share: ImageView
    private lateinit var comment: ImageView

    private lateinit var seekTimePlayed: TextView
    private lateinit var seekTotalTime: TextView
    private lateinit var seekBar: AppCompatSeekBar

    private val handler = HandlerSingleton.instance

    private var duration: Long = 0

    private val updateSeekBarRunnable = object : Runnable {
        override fun run() {
            if (duration in 1..627080715) {
                val position = MusicPlayer.position()
                seekBar.progress = (1000 * position / duration).toInt()
                seekTimePlayed.text = position.ms2Minute()
            }
            if (MusicPlayer.isPlaying()) {
                seekBar.postDelayed(this, 200)
            } else {
                seekBar.removeCallbacks(this)
            }
        }
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = VERTICAL
        View.inflate(context, R.layout.block_playing_bottom, this)

        repeatMode = findViewById(R.id.repeat_mode)
        preSong = findViewById(R.id.pre_song)
        centerControl = findViewById(R.id.center_control)
        nextSong = findViewById(R.id.next_song)
        songList = findViewById(R.id.song_list)

        favorite = findViewById(R.id.playing_favorite)
        download = findViewById(R.id.playing_download)
        share = findViewById(R.id.playing_share)
        comment = findViewById(R.id.playing_comment)

        seekTimePlayed = findViewById(R.id.seek_current_time)
        seekTotalTime = findViewById(R.id.seek_all_time)
        seekBar = findViewById(R.id.player_seek_bar)

        seekBar.isIndeterminate = false
        seekBar.max = 1000
        seekBar.progress = 1

        seekBar.setOnSeekBarChangeListener(this)
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

    /**
     * 更新模块
     */
    override fun updateBlock() {
        updateRepeatStatus()
        updatePlayingStatus()
        setSeekBar()
    }

    /**
     * 切换歌曲
     */
    override fun musicChanged() {
        setSeekBar()
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        seekTimePlayed.text = progress.toLong().ms2Minute()
        val i = progress * MusicPlayer.duration() / 1000
        if (fromUser) {
            MusicPlayer.seek(i)
            seekTimePlayed.text = i.ms2Minute()
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
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
        if (MusicPlayer.isPlaying()) {
            centerControl.setImageResource(R.drawable.default_player_btn_pause_selector)
            handler.post(updateSeekBarRunnable)
        } else {
            centerControl.setImageResource(R.drawable.default_player_btn_play_selector)
            handler.removeCallbacks(updateSeekBarRunnable)
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

    private fun setSeekBar() {
        duration = MusicPlayer.duration()
        seekTotalTime.text = duration.ms2Minute()
        seekTimePlayed.text = MusicPlayer.position().ms2Minute()
        seekBar.progress = (1000 * MusicPlayer.position() / MusicPlayer.duration()).toInt()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        seekBar.removeCallbacks(updateSeekBarRunnable)
    }

}