package com.se.music.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.widget.SeekBar
import com.se.music.pastmusic.R

/**
 * Created by gaojin on 2018/2/4.
 */
class PlayerSeekBar : SeekBar {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var drawLoading = false
    private var degree = 0
    private val mMatrix = Matrix()
    private val loading = BitmapFactory.decodeResource(resources, R.drawable.play_plybar_btn_loading)
    private var drawable: Drawable? = null

    fun setLoading(loading: Boolean) {
        if (loading) {
            drawLoading = true
            invalidate()
        } else {
            drawLoading = false
        }
    }

    override fun setThumb(thumb: Drawable) {
        var localRect: Rect? = null
        if (drawable != null) {
            localRect = drawable!!.bounds
        }
        super.setThumb(drawable)
        drawable = thumb
        if (localRect != null && drawable != null) {
            drawable!!.bounds = localRect
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    override fun getThumb(): Drawable {
        return if (Build.VERSION.SDK_INT >= 16) {
            super.getThumb()
        } else drawable!!
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (drawLoading) {
            canvas.save()
            degree = (degree + 3.0f).toInt()
            degree %= 360
            mMatrix.reset()
            mMatrix.postRotate(degree.toFloat(), (loading.width / 2).toFloat(), (loading.height / 2).toFloat())
            canvas.translate((paddingLeft + thumb.bounds.left + drawable!!.intrinsicWidth / 2 - loading.width / 2 - thumbOffset).toFloat(), (paddingTop + thumb.bounds.top + drawable!!.intrinsicHeight / 2 - loading.height / 2).toFloat())
            canvas.drawBitmap(loading, mMatrix, null)
            canvas.restore()
            invalidate()
        }

    }
}