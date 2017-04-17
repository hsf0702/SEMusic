package com.past.music.fragment;

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
import com.neu.gaojin.MyOkHttpClient;
import com.neu.gaojin.response.BaseSuccessCallback;
import com.past.music.MyApplication;
import com.past.music.activity.PlayMusicActivity;
import com.past.music.api.AvatarRequest;
import com.past.music.api.AvatarResponse;
import com.past.music.dialog.MusicQueueFragment;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;

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

    private static final String TAG = "QuickControlsFragment";

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

    @OnClick(R.id.rl_layout)
    void rlLayout() {
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
    }

    @OnClick(R.id.play_list)
    void playList() {
        MusicQueueFragment.newInstance().show(getFragmentManager(), "music_queue");
    }

    @OnClick(R.id.control)
    void control() {
        MusicPlayer.playOrPause();
        MyLog.i(TAG, "control");
    }

    @OnClick(R.id.play_next)
    void playNext() {
        MusicPlayer.nextPlay();
    }


    public static QuickControlsFragment newInstance() {
        return new QuickControlsFragment();
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
        mPlaybarInfo.setText(MusicPlayer.getTrackName());
        mPlaybarSinger.setText(MusicPlayer.getArtistName());
        if (MusicPlayer.getIsPlaying()) {
            mControl.setImageResource(R.drawable.playbar_btn_pause);
        } else {
            mControl.setImageResource(R.drawable.playbar_btn_play);
        }
        if (MusicPlayer.getArtistName() != null) {
            AvatarRequest avatarRequest = new AvatarRequest();
            avatarRequest.setArtist(MusicPlayer.getArtistName().replace(";", " "));
            if (MyApplication.dbService.query(MusicPlayer.getArtistName().replace(";", "")) == null) {
                MyOkHttpClient.getInstance(getContext()).sendNet(avatarRequest, new BaseSuccessCallback<AvatarResponse>() {
                    @Override
                    public void onSuccess(int statusCode, final AvatarResponse response) {
                        MyLog.i("onSuccess", statusCode + "");
                        MyApplication.dbService.insert(MusicPlayer.getArtistName().replace(";", ""), response.getArtist().getImage().get(2).get_$Text112());
                        mAlbum.setImageURI(response.getArtist().getImage().get(2).get_$Text112());
                    }
                });
            } else {
                mAlbum.setImageURI(MyApplication.dbService.query(MusicPlayer.getArtistName().replace(";", "")));
            }
        }
    }

    @Override
    public void updatePlayInfo() {
        updateFragment();
    }
}

