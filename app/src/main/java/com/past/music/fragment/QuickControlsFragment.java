package com.past.music.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.activity.PlayMusicActivity;
import com.past.music.pastmusic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:47
 * 版本：
 * 描述：底部音乐快捷控制栏
 * 备注：
 * =======================================================
 */
public class QuickControlsFragment extends BaseFragment {

    @BindView(R.id.playbar_img)
    SimpleDraweeView mAlbum;

    @BindView(R.id.linear)
    LinearLayout mLinear = null;

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

    @OnClick(R.id.linear)
    void linear() {
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        startActivityByY(intent, true);
    }

    @OnClick(R.id.play_list)
    void playList() {
        Toast.makeText(getContext(), "play_list", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.control)
    void control() {
        Toast.makeText(getContext(), "control", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.play_next)
    void playNext() {
        Toast.makeText(getContext(), "play_next", Toast.LENGTH_SHORT).show();
    }


    public static QuickControlsFragment newInstance() {
        QuickControlsFragment fragment = new QuickControlsFragment();
        return fragment;
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startAnimation(mAlbum);
    }

    public void startAnimation(View view) {
        Animation operatingAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        view.startAnimation(operatingAnim);
    }

    public void stopAnimation(View view) {
        view.clearAnimation();
    }

}

