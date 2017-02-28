package com.past.music.fragment.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.past.music.fragment.MusicFragment;
import com.past.music.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

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

    private List<String> tabNames = new ArrayList<>();

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabNames.add("我的");
        tabNames.add("音乐馆");
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MineFragment.newInstance("", "");
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

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {

        return tabNames.get(position % 2);
    }
}
