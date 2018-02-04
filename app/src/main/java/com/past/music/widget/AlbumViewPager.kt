package com.past.music.widget

import android.content.Context
import android.graphics.PointF
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 * Created by gaojin on 2018/2/4.
 */
class AlbumViewPager : ViewPager {

    var downPoint = PointF()
    var onSingleTouchListener: OnSingleTouchListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录按下时候的坐标
                downPoint.x = ev.x
                downPoint.y = ev.y
                if (this.childCount > 1) { //有内容，多于1个时
                    // 通知其父控件，现在进行的是本控件的操作，不允许拦截
                    parent.requestDisallowInterceptTouchEvent(true)
                }
            }
            MotionEvent.ACTION_UP ->
                // 在up时判断是否按下和松手的坐标为一个点
                if (PointF.length(ev.x - downPoint.x, ev.y - downPoint.y) < 5.0.toFloat()) {
                    onSingleTouch(this)
                    return true
                }
        }
        return super.onTouchEvent(ev)
    }

    fun onSingleTouch(v: View) {
        if (onSingleTouchListener != null) {
            onSingleTouchListener!!.onSingleTouch(v)
        }
    }

    interface OnSingleTouchListener {
        fun onSingleTouch(v: View)
    }
}