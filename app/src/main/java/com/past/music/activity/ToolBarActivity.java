package com.past.music.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.pastmusic.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/5 下午7:05
 * 描述：带有Toolbar的Activity抽象基类
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */

public abstract class ToolBarActivity extends BaseActivity {

    @BindView(R.id.title)
    protected TextView mTitle;

    @BindView(R.id.back)
    protected ImageView mBack;

    @BindView(R.id.base_toolbar)
    RelativeLayout relativeLayout;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
    }

    protected abstract int getLayoutId();

    @Override
    public void setTitle(CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    protected void setStatusBar() {
        int mColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mColor, BaseConfig.Alpha);
    }
}
