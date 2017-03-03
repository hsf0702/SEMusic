package com.past.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;
import com.past.music.BaseActivity;
import com.past.music.fragment.adapter.MainFragmentAdapter;
import com.past.music.pastmusic.R;
import com.past.music.widget.CircleImageView;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager = null;
    private TabLayout mTabLayout = null;
    private MainFragmentAdapter mAdapter = null;
    private CircleImageView mCircleImageView = null;
    private RelativeLayout mRlBottomPlay = null;

    private int mStatusBarColor;
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCircleImageView = (CircleImageView) findViewById(R.id.circle_image);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        mRlBottomPlay = (RelativeLayout) findViewById(R.id.rl_bottom);
        initListener();

        FragmentManager fm = getSupportFragmentManager();
        //初始化自定义适配器
        mAdapter = new MainFragmentAdapter(fm);
        //绑定自定义适配器
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            mCircleImageView.startAnimation(operatingAnim);
        }
    }

    private void initListener() {
        mRlBottomPlay.setOnClickListener(this);
    }

    @Override
    protected void setStatusBar() {
        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor, 0);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_bottom:
                intent = new Intent(this, PlayMusicActivity.class);
                startActivityByY(intent);
                break;
        }
    }
}
