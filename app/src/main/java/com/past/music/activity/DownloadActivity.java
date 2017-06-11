package com.past.music.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.past.music.fragment.DownFragment;
import com.past.music.fragment.DownMusicFragment;
import com.past.music.pastmusic.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DownloadActivity extends ToolBarActivity {
    private List<String> Tnames = new ArrayList<>();
    FragmentAdapter mAdapter = null;

    @BindView(R.id.local_tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.local_view_pager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("下载歌曲");

        Tnames.add("已下载");
        Tnames.add("正在下载");
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        FragmentManager fm = getSupportFragmentManager();
        mAdapter = new FragmentAdapter(fm);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return DownMusicFragment.newInstance("/storage/emulated/0/pastmusic", false, null);
            } else {
                return DownFragment.newInstance();
            }

        }

        @Override
        public int getCount() {
            return 2;
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return Tnames.get(position % 2);
        }
    }
}
