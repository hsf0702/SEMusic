package com.past.music.activity;

import android.os.Bundle;

import com.past.music.database.service.SongListDBService;
import com.past.music.pastmusic.R;

public class DownloadActivity extends ToolBarActivity {

    SongListDBService songListDBService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("下载歌曲");
        songListDBService = new SongListDBService(this);
        songListDBService.insert("跑步", 100, "高进", "http://i.gtimg.cn/music/photo/mid_album_300/A/m/001cZASs2rAdAm.jpg", "二哥如果");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_download;
    }
}
