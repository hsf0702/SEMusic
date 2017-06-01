package com.past.music.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.jaeger.library.StatusBarUtil;
import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayMusicActivity extends BaseActivity {
    private Mp3Info mMp3Info = null;

//    @BindView(R.id.playing_next)
//    ImageView mNext;
//
//    @BindView(R.id.playing_pre)
//    ImageView mPre;

    @BindView(R.id.playing_play)
    ImageView mPlay;

    @OnClick(R.id.playing_next)
    void next() {
        MusicPlayer.nextPlay();
    }

    @OnClick(R.id.playing_pre)
    void pre() {
        MusicPlayer.previous(PlayMusicActivity.this, true);
    }

    @OnClick(R.id.playing_play)
    void play() {
        if (MusicPlayer.getIsPlaying()) {
            mPlay.setImageResource(R.drawable.play_rdi_btn_pause);
        } else {
            mPlay.setImageResource(R.drawable.play_rdi_btn_play);
        }
        if (MusicPlayer.getQueueSize() != 0) {
            MusicPlayer.playOrPause();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        showQuickControl(false);
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

    @Override
    protected void showQuickControl(boolean show) {
    }

    @Override
    public void baseUpdatePlayInfo() {
        if (MusicPlayer.getIsPlaying()) {
            mPlay.setImageResource(R.drawable.play_rdi_btn_pause);
        } else {
            mPlay.setImageResource(R.drawable.play_rdi_btn_play);
        }
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
