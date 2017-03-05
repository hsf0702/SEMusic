package com.past.music.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.pastmusic.R;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/5 下午7:05
 * 描述：带有Toolbar的Activity抽象基类
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */

public abstract class ToolBarActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mToolbar = (Toolbar) findViewById(R.id.base_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.back_white);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected abstract int getLayoutId();


    @Override
    protected void setStatusBar() {
        int mColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mColor, BaseConfig.Alpha);
    }
}
