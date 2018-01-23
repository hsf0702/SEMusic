package com.past.music.online.view

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.past.music.online.listener.OnLineRefreshListener
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2018/1/6.
 */
class NewSongExpressBlock : LinearLayout, OnLineRefreshListener {
    override fun refresh(fragmentManager: FragmentManager?, loaderManager: LoaderManager) {
    }

    private var recommendRecycleView: RecyclerView? = null
    private var iconEnter: View? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.online_new_song_express_block, this)
        initView()
    }

    private fun initView() {
        iconEnter = findViewById(R.id.new_song_express_icon_enter)
        recommendRecycleView = findViewById(R.id.new_song_express_recycle_view)
        recommendRecycleView!!.layoutManager = LinearLayoutManager(context)
    }
}