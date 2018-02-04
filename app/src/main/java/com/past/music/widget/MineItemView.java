package com.past.music.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.past.music.pastmusic.R;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/27 下午8:28
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class MineItemView extends RelativeLayout {

    ImageView mImageView;
    TextView mItemName;
    TextView mItemCount;

    private int count;
    private String item_text;
    private Drawable icon;


    public MineItemView(Context context) {
        this(context, null);
    }

    public MineItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MineItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inial(context, attrs);
        initView();
    }

    private void inial(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.item_view);
        count = typedArray.getInt(R.styleable.item_view_count, 151);
        item_text = typedArray.getString(R.styleable.item_view_text);
        icon = typedArray.getDrawable(R.styleable.item_view_icon_view);
        typedArray.recycle();
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_mine_item_view, this);

        mImageView = findViewById(R.id.img_item);
        mItemName = findViewById(R.id.tv_item_name);
        mItemCount = findViewById(R.id.tv_item_count);

        mImageView.setImageDrawable(icon);
        mItemName.setText(item_text);
        mItemCount.setText(String.valueOf(count));
    }

    public void setmImageView(Drawable icon) {
        if (mImageView != null) {
            mImageView.setImageDrawable(icon);
        }
    }

    public void setmItemName(String item_text) {
        if (mItemName != null) {
            mItemName.setText(item_text);
        }
    }

    public void setmItemCount(String count) {
        if (mItemCount != null) {
            mItemCount.setText(count);
        }
    }
}
