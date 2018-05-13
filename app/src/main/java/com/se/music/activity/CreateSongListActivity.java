package com.se.music.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.se.music.database.provider.SongListDBService;
import com.se.music.pastmusic.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateSongListActivity extends AppCompatActivity {

    String listName;
    String listInfo;

    @BindView(R.id.et_list_name)
    EditText nameInput;

    @BindView(R.id.et_list_info)
    EditText infoInput;

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @BindView(R.id.title)
    TextView mTitle;

    @OnClick(R.id.save)
    void save() {
        listName = nameInput.getText().toString();
        listInfo = infoInput.getText().toString();
        if (!TextUtils.isEmpty(listName)) {
            SongListDBService.Companion.getInstance().insert(listName, listInfo);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_song_list);
        setStatusBar();
        ButterKnife.bind(this);

        mTitle.setText("新建歌单");
        nameInput.setText("新建歌单");
        nameInput.setSelectAllOnFocus(true);
        nameInput.requestFocus();
    }

    protected void setStatusBar() {
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
