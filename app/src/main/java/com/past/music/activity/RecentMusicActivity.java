package com.past.music.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.past.music.dialog.SongOperationDialog;
import com.past.music.entity.MusicEntity;
import com.past.music.log.MyLog;
import com.past.music.pastmusic.R;
import com.past.music.provider.RecentStore;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;
import com.past.music.utils.MConstants;
import com.past.music.utils.MusicUtils;
import com.past.music.utils.recent.Song;
import com.past.music.utils.recent.SongLoader;
import com.past.music.utils.recent.TopTracksLoader;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecentMusicActivity extends ToolBarActivity {

    public static final String TAG = "RecentMusicActivity";

    @BindView(R.id.recent_music_recycle)
    RecyclerView mRecyclerView;

    private Adapter mAdapter;
    private List<Song> mList;
    private RecentStore recentStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("最近播放");

        recentStore = RecentStore.getInstance(this);

        TopTracksLoader recentloader = new TopTracksLoader(this, TopTracksLoader.QueryType.RecentSongs);
        List<Song> recentsongs = SongLoader.getSongsForCursor(TopTracksLoader.getCursor());
        int songCountInt = recentsongs.size();
        mList = recentsongs;
        MyLog.i(TAG, mList.size() + "");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);

        new loadSongs().execute("");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_recent_music;
    }

    //异步加载recyclerview界面
    private class loadSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            mAdapter = new Adapter(mList);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            mRecyclerView.setAdapter(mAdapter);
        }

        @Override
        protected void onPreExecute() {

        }
    }

    public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        public static final int HEAD_LAYOUT = 0X01;
        public static final int CONTENT_LAYOUT = 0X02;
        private Handler handler;
        SongOperationDialog songOperationDialog;

        private List<Song> mList;

        public Adapter(List<Song> list) {
            if (list == null) {
                throw new IllegalArgumentException("model Data must not be null");
            }
            mList = list;
        }

        //更新adpter的数据
        public void updateDataSet(List<Song> list) {
            this.mList = list;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == HEAD_LAYOUT) {
                return new CommonItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.common_item, parent, false));
            } else {
                return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_musci_common_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof CommonItemViewHolder) {

            } else {
                ((ListItemViewHolder) holder).onBindData(mList.get(position - 1), position);
            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() + 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return HEAD_LAYOUT;
            } else {
                return CONTENT_LAYOUT;
            }
        }

        class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            @BindView(R.id.play_all_number)
            TextView textView;

            @BindView(R.id.select)
            ImageView select;

            @OnClick(R.id.select)
            void select() {
            }

            CommonItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
            }


            @Override
            public void onClick(View v) {
                HandlerUtil.getInstance(RecentMusicActivity.this).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        HashMap<Long, MusicEntity> infos = new HashMap();
                        for (int i = 0; i < mList.size(); i++) {
                            MusicEntity info = MusicUtils.getMusicInfo(RecentMusicActivity.this, mList.get(i).id);
                            list[i] = info.songId;
                            info.islocal = true;
                            info.albumData = MusicUtils.getAlbumArtUri(info.albumId) + "";
                            infos.put(list[i], info);
                        }
                        MusicPlayer.playAll(infos, list, 0, false);
                    }
                }, 70);
            }
        }

        class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.music_name)
            TextView mMusicName;

            @BindView(R.id.music_info)
            TextView mMusicInfo;

            @OnClick(R.id.viewpager_list_button)
            void setmListButton() {
                MusicEntity musicEntity = MusicUtils.getMusicInfo(RecentMusicActivity.this, mList.get(getAdapterPosition() - 1).id);
                songOperationDialog = new SongOperationDialog(RecentMusicActivity.this, musicEntity, MConstants.MUSICOVERFLOW);
                songOperationDialog.show();
            }

            ListItemViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                HandlerUtil.getInstance(RecentMusicActivity.this).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        HashMap<Long, MusicEntity> infos = new HashMap();
                        for (int i = 0; i < mList.size(); i++) {
                            MusicEntity info = MusicUtils.getMusicInfo(RecentMusicActivity.this, mList.get(i).id);
                            list[i] = info.songId;
                            info.islocal = true;
                            info.albumData = MusicUtils.getAlbumArtUri(info.albumId) + "";
                            infos.put(list[i], info);
                        }
                        if (getAdapterPosition() > 0)
                            MusicPlayer.playAll(infos, list, getAdapterPosition() - 1, false);
                    }
                }, 70);
            }

            public void onBindData(Song song, int position) {
                mMusicName.setText(song.title);
                mMusicInfo.setText(song.artistName);
            }
        }
    }
}
