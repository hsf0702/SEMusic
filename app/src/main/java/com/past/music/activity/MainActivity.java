package com.past.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.neu.gaojin.MyOkHttpClient;
import com.past.music.Config.BaseConfig;
import com.past.music.fragment.adapter.MainFragmentAdapter;
import com.past.music.pastmusic.R;
import com.past.music.utils.SysUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.img_start)
    ImageView mImgStart;

    @BindView(R.id.img_end)
    ImageView mImgEnd;

    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @OnClick(R.id.img_start)
    void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    private MainFragmentAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new MainFragmentAdapter(fm);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        int statusBarHeight = SysUtils.getStatusBarHeight(this);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.height += statusBarHeight;
        toolbar.setLayoutParams(layoutParams);

        MyOkHttpClient.getInstance(this).test();
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }

    @Override
    protected void setStatusBar() {
        int mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, mDrawerLayout, mStatusBarColor, BaseConfig.Alpha);
    }
}
