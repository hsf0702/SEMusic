package com.past.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.fragment.adapter.MainFragmentAdapter;
import com.past.music.pastmusic.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager = null;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout = null;
    private MainFragmentAdapter mAdapter = null;

    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        FragmentManager fm = getSupportFragmentManager();
        //初始化自定义适配器
        mAdapter = new MainFragmentAdapter(fm);
        //绑定自定义适配器
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }

    @Override
    protected void setStatusBar() {
        int mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor, BaseConfig.Alpha);
    }
}
