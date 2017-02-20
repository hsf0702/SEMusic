package com.past.music.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.jaeger.library.StatusBarUtil;
import com.past.music.BaseActivity;
import com.past.music.fragment.adapter.MainFragmentAdapter;
import com.past.music.pastmusic.R;
import com.past.music.widget.CircleImageView;

public class MainActivity extends BaseActivity {

    //权限申请标志码
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0x01;

    private DrawerLayout mDrawerLayout;

    private ViewPager mViewPager = null;
    private MainFragmentAdapter mAdapter = null;
    private CircleImageView mCircleImageView = null;

    private int mStatusBarColor;
    private int mAlpha = StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCircleImageView = (CircleImageView) findViewById(R.id.circle_image);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        FragmentManager fm = getSupportFragmentManager();
        //初始化自定义适配器
        mAdapter = new MainFragmentAdapter(fm);
        //绑定自定义适配器
        mViewPager.setAdapter(mAdapter);

        Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        if (operatingAnim != null) {
            mCircleImageView.startAnimation(operatingAnim);
        }
    }

    @Override
    protected void setStatusBar() {
        mStatusBarColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColorForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor, 0);
    }
}
