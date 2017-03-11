package com.past.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.past.music.fragment.adapter.LocalFragmentAdapter;
import com.past.music.pastmusic.R;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;

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

        int localCount = MusicUtils.queryMusic(this, MConstants.START_FROM_LOCAL).size();
        int singerCount = MusicUtils.queryArtist(this).size();
        int albumCount = MusicUtils.queryAlbums(this).size();
        mTabNames.add("歌曲 " + localCount);
        mTabNames.add("歌手 " + singerCount);
        mTabNames.add("专辑 " + albumCount);
        mTabNames.add("文件夹 2");

        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new LocalFragmentAdapter(fm, mTabNames);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_local_music;
    }
}
