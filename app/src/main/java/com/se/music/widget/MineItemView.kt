package com.se.music.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.se.music.pastmusic.R

/**
 * Created by gaojin on 2018/2/26.
 */
class MineItemView : LinearLayout {

    private var mImageView: ImageView? = null
    private var mItemName: TextView? = null
    private var mItemCount: TextView? = null

    private var count: Int = 0
    private var itemtext: String? = null
    private var icon: Drawable? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : this(context, attrs, defStyleAttr, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.item_view)
        count = typedArray.getInt(R.styleable.item_view_count, 151)
        itemtext = typedArray.getString(R.styleable.item_view_text)
        icon = typedArray.getDrawable(R.styleable.item_view_icon_view)
        typedArray.recycle()

        View.inflate(getContext(), R.layout.view_mine_item_view, this)

        mImageView = findViewById(R.id.img_item)
        mItemName = findViewById(R.id.tv_item_name)
        mItemCount = findViewById(R.id.tv_item_count)

        mImageView!!.setImageDrawable(icon)
        mItemName!!.text = itemtext
        mItemCount!!.text = count.toString()
    }

    fun setmImageView(icon: Drawable) {
        if (mImageView != null) {
            mImageView!!.setImageDrawable(icon)
        }
    }

    fun setmItemName(item_text: String) {
        if (mItemName != null) {
            mItemName!!.text = item_text
        }
    }

    fun setmItemCount(count: String) {
        if (mItemCount != null) {
            mItemCount!!.text = count
        }
    }
}