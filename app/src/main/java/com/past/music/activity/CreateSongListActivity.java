package com.past.music.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.past.music.Config.BaseConfig;
import com.past.music.MyApplication;
import com.past.music.pastmusic.R;

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
            MyApplication.songListDBService.insert(listName, listInfo);
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
        int mColor = getResources().getColor(R.color.colorPrimary);
        StatusBarUtil.setColor(this, mColor, BaseConfig.Alpha);
    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_up_in, R.anim.push_down_out);
    }
}
