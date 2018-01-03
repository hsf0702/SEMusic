package com.past.music.online.block

import android.content.Context
import android.support.v4.app.FragmentManager
import android.support.v4.app.LoaderManager
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.past.music.activity.BaseActivity
import com.past.music.online.listener.OnLineRefreshListener
import com.past.music.online.subfragment.OnLineSingerFragment
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2017/12/30.
 */
class OnLineClassifyBlock : LinearLayout, OnLineRefreshListener, View.OnClickListener {
    override fun refresh(fragmentManager: FragmentManager?, loaderManager: LoaderManager) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var mContext: Context = context

    private var singer: View? = null
    private var rank: View? = null
    private var radio: View? = null
    private var list: View? = null
    private var mv: View? = null
    private var album: View? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.online_classify_block, this)
        val padding = mContext.resources.getDimensionPixelOffset(R.dimen.app_base_left_right_padding)
        setPadding(padding, 0, padding, 0)
        initView()
    }

    private fun initView() {
        singer = findViewById(R.id.classify_singer)
        rank = findViewById(R.id.classify_rank)
        radio = findViewById(R.id.classify_radio)
        list = findViewById(R.id.classify_song_list)
        mv = findViewById(R.id.classify_mv)
        album = findViewById(R.id.classify_album)

        singer!!.setOnClickListener(this)
        rank!!.setOnClickListener(this)
        radio!!.setOnClickListener(this)
        list!!.setOnClickListener(this)
        mv!!.setOnClickListener(this)
        album!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.classify_singer) {
            val ft = (mContext as BaseActivity).supportFragmentManager.beginTransaction()
            ft.addToBackStack(null)
            ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
            ft.add(R.id.content, OnLineSingerFragment.newInstance()).commit()
        }
    }
}