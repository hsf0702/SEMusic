package com.se.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.se.music.R;
import com.se.music.activity.PlayMusicActivity;
import com.se.music.service.MusicPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:47
 * 描述：底部音乐快捷控制栏
 * 备注：备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class QuickControlsFragment extends BaseFragment {

    @BindView(R.id.playbar_img)
    SimpleDraweeView mAlbum;

    @BindView(R.id.rl_layout)
    RelativeLayout relativeLayout = null;

    @BindView(R.id.playbar_info)
    TextView mPlaybarInfo;

    @BindView(R.id.playbar_singer)
    TextView mPlaybarSinger;

    @BindView(R.id.play_list)
    ImageView mPlayList;

    @BindView(R.id.control)
    ImageView mControl;

    @BindView(R.id.play_next)
    ImageView mPlayNext;

    public static QuickControlsFragment newInstance() {
        return new QuickControlsFragment();
    }

    @OnClick(R.id.rl_layout)
    void rlLayout() {
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    @OnClick(R.id.play_list)
    void playList() {
    }

    @OnClick(R.id.control)
    void control() {
        MusicPlayer.Companion.playOrPause();
    }

    @OnClick(R.id.play_next)
    void playNext() {
        MusicPlayer.Companion.nextPlay();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.view_quick_controls, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void updateFragment() {
        mPlaybarInfo.setText(MusicPlayer.Companion.getTrackName());
        mPlaybarSinger.setText(MusicPlayer.Companion.getArtistName());
        if (MusicPlayer.Companion.getIsPlaying()) {
            mControl.setImageResource(R.drawable.playbar_btn_pause);
        } else {
            mControl.setImageResource(R.drawable.playbar_btn_play);
        }
        if (MusicPlayer.Companion.getAlbumPic() != null) {
            mAlbum.setImageURI(MusicPlayer.Companion.getAlbumPic());
        }
    }

    @Override
    public void updatePlayInfo() {
        updateFragment();
    }
}

