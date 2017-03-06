package com.past.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.ViewTreeObserver;

import com.past.music.fragment.adapter.LocalFragmentAdapter;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class LocalMusicActivity extends ToolBarActivity {

    private static final String TAG = "LocalMusicActivity";

    @BindView(R.id.local_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.local_view_pager)
    ViewPager mViewPager = null;

    LocalFragmentAdapter mAdapter = null;
    private List<String> mTabNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("本地歌曲");
        mTabNames.add("歌曲 151");
        mTabNames.add("歌手 67");
        mTabNames.add("专辑 95");
        mTabNames.add("文件夹 2");

        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new LocalFragmentAdapter(fm, mTabNames);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        final ViewTreeObserver vto = mToolbar.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mToolbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int height = mToolbar.getMeasuredHeight();
                int width = mToolbar.getMeasuredWidth();
                MyLog.i(TAG + "toolbar-height", height + "");
                MyLog.i(TAG + "toolbar-width", width + "");
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_music;
    }
}
