package com.se.music.activity

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.service.MusicPlayer
import com.se.music.subpage.playing.PlayerPagerAdapter
import com.se.music.subpage.playing.PlayingBottomBlock
import com.se.music.utils.blurBitmap
import com.se.music.utils.getMegaImageUrl
import com.se.music.widget.MultiButtonLayout
import com.se.music.widget.PlayingAlbumPageTransformer

/**
 *Author: gaojin
 *Time: 2018/8/7 下午7:48
 */

class PlayingActivity : BaseActivity(), ViewPager.OnPageChangeListener {

    private lateinit var activityBg: View
    private lateinit var toolbar: Toolbar

    private lateinit var songTitle: TextView
    private lateinit var viewPager: ViewPager
    private lateinit var multiButtonLayout: MultiButtonLayout
    private lateinit var playingBottomBlock: PlayingBottomBlock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        showQuickControl(false)
        toolbarInit()
        initView()
    }

    private fun initView() {
        activityBg = findViewById(R.id.player_activity_bg)
        songTitle = findViewById(R.id.playing_song_title)
        viewPager = findViewById(R.id.content_view_pager)
        multiButtonLayout = findViewById(R.id.playing_select_radio)
        playingBottomBlock = findViewById(R.id.playing_bottom_block)

        viewPager.adapter = PlayerPagerAdapter(supportFragmentManager)
        viewPager.setPageTransformer(false, PlayingAlbumPageTransformer())
        viewPager.addOnPageChangeListener(this)
        viewPager.currentItem = PlayerPagerAdapter.ALBUM_INFO
        viewPager.offscreenPageLimit = 2
    }

    override fun onResume() {
        super.onResume()
        songTitle.text = MusicPlayer.getTrackName()
        playingBottomBlock.updateBlock()
        setBg()
    }

    private fun toolbarInit() {
        toolbar = findViewById(R.id.playing_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
    }

    override fun musicChanged() {
        super.musicChanged()
        songTitle.text = MusicPlayer.getTrackName()
        playingBottomBlock.musicChanged()
        setBg()
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

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        multiButtonLayout.setSelectedChild(position)
        Log.e("gj", position.toString())
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out)
    }

    private fun setBg() {
        if (MusicPlayer.getAlbumPic().isEmpty()) {
            activityBg.background = getDrawable(R.drawable.player_background_real)
        } else {
            Glide.with(this)
                    .asBitmap()
                    .load(MusicPlayer.getAlbumPic().getMegaImageUrl())
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            val blurBitmap = blurBitmap(resource, 100)
                            if (blurBitmap != null) {
                                activityBg.background = getAlphaDrawable(blurBitmap)
                            }
                        }

                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            super.onLoadFailed(errorDrawable)
                            activityBg.background = getDrawable(R.drawable.player_background_real)
                        }
                    })
        }
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