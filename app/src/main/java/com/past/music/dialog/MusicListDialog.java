package com.past.music.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gaojin on 2017/3/25.
 */

public class MusicListDialog extends AlertDialog {

    private Context mContext;

    private ArrayList<MusicEntity> playlist = new ArrayList<>();
    private PlayListAdapter mAdapter = null;
    private RecyclerView.ItemDecoration itemDecoration;

    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_close)
    TextView textView_close;

    @BindView(R.id.tv_count)
    TextView textView_count;

    @OnClick(R.id.tv_close)
    void close() {
        dismiss();
    }


    public MusicListDialog(@NonNull Context context) {
        super(context, R.style.style_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_list);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_in_style);
        ButterKnife.bind(this);
        itemDecoration = new DividerItemDecoration(mContext.getResources(), R.color.text_gray, R.dimen.divider_1, DividerItemDecoration.VERTICAL_LIST);
        mAdapter = new PlayListAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        new LoadSongs().execute();
    }


    @Override
    public void show() {
        super.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = (int) (dm.heightPixels * 0.6);
        this.getWindow().setLayout(width, height);
    }

    //异步加载recyclerview界面
    private class LoadSongs extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<Long, MusicEntity> play = MusicPlayer.getPlayinfos();
            if (play != null && play.size() > 0) {
                long[] queue = MusicPlayer.getQueue();
                int len = queue.length;
                for (int i = 0; i < len; i++) {
                    playlist.add(play.get(queue[i]));
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mAdapter.notifyDataSetChanged();
            textView_count.setText("（" + playlist.size() + "首）");
        }
    }


    class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dialog_playqueue_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ItemViewHolder) holder).onBindData(playlist.get(position), position);
        }

        @Override
        public int getItemCount() {
            return playlist.size();
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.music_name)
        TextView mMusicName;

        @BindView(R.id.music_singer)
        TextView mMusicSinger;

        @BindView(R.id.recycle_item)
        LinearLayout mRecycleItem;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBindData(MusicEntity musicEntity, int position) {
            mMusicName.setText(musicEntity.getMusicName());
            mMusicSinger.setText(musicEntity.getArtist());
            mRecycleItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
