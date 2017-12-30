package com.past.music.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.past.music.pastmusic.R

/**
 * Created by gaojin on 2017/12/31.
 */
class RecommendItemView : LinearLayout {
    private var itemHeader: AutoHeightImageView? = null
    private var descriptionView: TextView? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun init() {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.online_recommend_item_view, this)
        initView()
    }

    private fun initView() {
        itemHeader = findViewById(R.id.recommend_item_head)
        descriptionView = findViewById(R.id.song_list_description)
    }

    fun setImageView(url: String): RecommendItemView {
        Glide.with(context).load(url).into(itemHeader)
        return this
    }

    fun setDescription(description: String): RecommendItemView {
        descriptionView!!.text = description
        return this
    }

}