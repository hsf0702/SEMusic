package com.past.music.widget;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.pastmusic.R;

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
    private SimpleDraweeView mHotPic;
    private TextView mTvDes;

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

    private void initView() {
        View.inflate(getContext(), R.layout.view_icon_view, this);
        mHotPic = (SimpleDraweeView) findViewById(R.id.img_hot_pic);
        mTvDes = (TextView) findViewById(R.id.tv_describe);
    }

    public void setmHotPic(String pic) {
        Uri uri = Uri.parse(pic);
        mHotPic.setImageURI(uri);
    }

    public void setmTvDes(String des) {
        mTvDes.setText(des);
    }
}
