package com.se.music.subpage.playing

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.se.music.R
import com.se.music.subpage.entity.OtherVersionInfo
import com.se.music.subpage.entity.SimilarSongInfo
import com.se.music.utils.getImageId
import com.se.music.utils.getLargeImageUrl
import com.se.music.utils.loadUrl
import com.se.music.widget.ContentItemView

/**
 *Author: gaojin
 *Time: 2018/10/12 下午6:19
 */

class PlayingSongRelatedInfoBlock : LinearLayout {

    private lateinit var firstContainer: LinearLayout
    private lateinit var secondContainer: LinearLayout
    private lateinit var thirdContainer: LinearLayout

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        orientation = LinearLayout.VERTICAL
        View.inflate(context, R.layout.playing_song_related_info, this)
        firstContainer = findViewById(R.id.first_container)
        secondContainer = findViewById(R.id.second_container)
        thirdContainer = findViewById(R.id.third_container)
    }

    fun addOtherVersionInfo(otherVersionInfo: OtherVersionInfo) {
        firstContainer.removeAllViews()
        otherVersionInfo.track?.forEach { bean ->
            val itemView = generateItemView()
            itemView.title.text = bean.name
            itemView.subTitle.text = bean.artist
            bean.image?.run {
                val mid = get(0).imgUrl.getImageId()
                itemView.headImg.loadUrl(mid.getLargeImageUrl())
            }
            firstContainer.addView(itemView)
        }
    }

    fun addSimilarInfo(similarSongInfo: SimilarSongInfo) {
        secondContainer.removeAllViews()
        similarSongInfo.track?.forEach { bean ->
            val itemView = generateItemView()
            itemView.title.text = bean.name
            itemView.subTitle.text = bean.artist?.name
            bean.image?.run {
                val mid = get(0).imgUrl.getImageId()
                itemView.headImg.loadUrl(mid.getLargeImageUrl())
            }
            secondContainer.addView(itemView)
        }
    }

    private fun generateItemView(): ContentItemView {
        val itemView = ContentItemView(context)
        val itemHeight = resources.getDimensionPixelOffset(R.dimen.content_image_head)
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, itemHeight)
        params.bottomMargin = resources.getDimensionPixelOffset(R.dimen.content_item_bottom_margin)
        itemView.layoutParams = params
        return itemView
    }
}