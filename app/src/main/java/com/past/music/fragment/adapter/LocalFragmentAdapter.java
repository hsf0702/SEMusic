package com.past.music.fragment.adapter;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.past.music.fragment.LocalMusicFragment;
import com.past.music.fragment.MusicFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/6 下午1:13
 * 描述：
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
@SuppressLint("ValidFragment")
public class LocalFragmentAdapter extends FragmentPagerAdapter {
    private List<String> tabNames = new ArrayList<>();

    public LocalFragmentAdapter(FragmentManager fm, List<String> list) {
        super(fm);
        this.tabNames = list;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return LocalMusicFragment.newInstance("", "");
            case 1:
                return MusicFragment.newInstance("", "");
            case 2:
                return MusicFragment.newInstance("", "");
            case 3:
                return MusicFragment.newInstance("", "");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return tabNames.get(position % 4);
    }
}
