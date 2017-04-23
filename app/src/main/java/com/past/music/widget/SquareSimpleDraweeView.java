package com.past.music.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/22 22:40
 * 描述：
 * 备注：
 * =======================================================
 */
public class SquareSimpleDraweeView extends SimpleDraweeView {

    public SquareSimpleDraweeView(Context context) {
        this(context, null);
    }

    public SquareSimpleDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}
