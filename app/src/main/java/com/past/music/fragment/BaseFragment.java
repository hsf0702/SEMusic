package com.past.music.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.past.music.activity.BaseActivity;
import com.past.music.activity.MusicStateListener;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:44
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class BaseFragment extends Fragment implements MusicStateListener {

    public Context mContext;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity) getActivity()).setMusicStateListenerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updatePlayInfo() {

    }

    @Override
    public void updateTime() {

    }

    @Override
    public void reloadAdapter() {

    }
}
