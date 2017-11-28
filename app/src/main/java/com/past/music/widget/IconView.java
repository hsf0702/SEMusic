package com.past.music.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.past.music.pastmusic.R;
import com.past.music.utils.DensityUtil;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/2/28 下午10:14
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class IconView extends RelativeLayout {

    private Context mContext;
    private SquareSimpleDraweeView mHotPic;
    private TextView mTvDes;
    private int width;

    public IconView(Context context) {
        this(context, null);
    }

    public IconView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth() + DensityUtil.sp2px(mContext, 24));
    }

    private void initView() {
        View.inflate(getContext(), R.layout.view_icon_view, this);
        mHotPic = findViewById(R.id.img_hot_pic);
        mTvDes = findViewById(R.id.tv_describe);
    }

    public void setmHotPic(String pic) {
        Uri uri = Uri.parse(pic);
        mHotPic.setImageURI(uri);
    }

    public void setmTvDes(String des) {
        if (mTvDes != null) {
            mTvDes.setText(des);
        }
    }
}
