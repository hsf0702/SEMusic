package com.past.music.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;

import java.util.ArrayList;

public class PlayListInfoActivity extends ToolBarActivity {

    private static final String TITLE = "title";
    private static final String MUSIC_LIST = "music_list";


    public static void startActivity(Context context, String title, ArrayList<MusicEntity> list) {
        Intent intent = new Intent(context, PlayListInfoActivity.class);
        intent.putExtra(TITLE, title);
        intent.putParcelableArrayListExtra(MUSIC_LIST, list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_play_list_info;
    }
}
