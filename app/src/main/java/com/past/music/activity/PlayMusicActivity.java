package com.past.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.service.PlayerService;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayMusicActivity extends BaseActivity {
    private Mp3Info mMp3Info = null;

    @BindView(R.id.playing_next)
    ImageView mNext;

    @BindView(R.id.playing_pre)
    ImageView mPre;

    @BindView(R.id.playing_play)
    ImageView mPlay;

    @OnClick(R.id.playing_next)
    void next() {

    }

    @OnClick(R.id.playing_pre)
    void pre() {

    }

    @OnClick(R.id.playing_play)
    void play() {
        Intent intent = new Intent();
        intent.putExtra(BaseConfig.URL, mMp3Info.getUrl());
        intent.putExtra(BaseConfig.MSG, BaseConfig.PlayerMsg.PLAY_MSG);
        intent.setClass(this, PlayerService.class);
        startService(intent);       //启动服务
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucent(this);
    }
}
