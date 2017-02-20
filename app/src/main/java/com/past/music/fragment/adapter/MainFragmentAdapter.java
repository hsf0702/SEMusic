package com.past.music.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.past.music.fragment.MusicFragment;
import com.past.music.fragment.NetEasyFragment;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/2/20 下午12:16
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class MainFragmentAdapter extends FragmentPagerAdapter {

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return NetEasyFragment.newInstance("", "");
            case 1:
                return MusicFragment.newInstance("", "");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
