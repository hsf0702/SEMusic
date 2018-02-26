package com.past.music.dialog;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;
import com.past.music.widget.DividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/8 19:29
 * 描述：
 * 备注：
 * =======================================================
 */
public class MusicQueueFragment extends DialogFragment {


    private ArrayList<MusicEntity> playlist = new ArrayList<>();
    private PlayListAdapter mAdapter = null;
    private Handler mHandler;
    private int currentlyPlayingPosition = 0;
    private MusicEntity musicEntity = null;
    private LinearLayoutManager layoutManager;
    public Context mContext;
    private boolean move = false;
    private int index = 0;


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

    public static MusicQueueFragment newInstance() {
        MusicQueueFragment fragment = new MusicQueueFragment();
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
        View view = inflater.inflate(R.layout.dialog_play_list, container);
        ButterKnife.bind(this, view);
        mHandler = HandlerUtil.getInstance(mContext);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity().getResources(), R.color.text_gray, R.dimen.divider_1, DividerItemDecoration.VERTICAL_LIST);
        mAdapter = new PlayListAdapter();
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.addOnScrollListener(new RecyclerViewListener());
        mRecyclerView.setAdapter(mAdapter);
        new LoadSongs().execute();
        return view;
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
            textView_count.setText("（" + playlist.size() + "首）");
            mAdapter.notifyDataSetChanged();
            for (int i = 0; i < playlist.size(); i++) {
                MusicEntity info = playlist.get(i);
                if (info != null && MusicPlayer.getCurrentAudioId() == info.getSongId()) {
                    moveToPosition(i);
                }
            }
        }
    }

    private void moveToPosition(int n) {
        index = n;
        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
        int firstItem = layoutManager.findFirstVisibleItemPosition();
        int lastItem = layoutManager.findLastVisibleItemPosition();
        //然后区分情况
        if (n <= firstItem) {
            //当要置顶的项在当前显示的第一个项的前面时
            mRecyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            //当要置顶的项已经在屏幕上显示时
            int top = mRecyclerView.getChildAt(n - firstItem).getTop();
            mRecyclerView.scrollBy(0, top);
        } else {
            //当要置顶的项在当前显示的最后一项的后面时
            mRecyclerView.scrollToPosition(n);
            //这里这个变量是用在RecyclerView滚动监听里面的
            move = true;
        }
    }

    class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (move) {
                move = false;
                int n = index - layoutManager.findFirstVisibleItemPosition();
                if (0 <= n && n < mRecyclerView.getChildCount()) {
                    int top = mRecyclerView.getChildAt(n).getTop();
                    mRecyclerView.scrollBy(0, top);
                }
            }
        }
    }


    class PlayListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.dialog_playqueue_item, parent, false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            musicEntity = playlist.get(position);
            if (MusicPlayer.getCurrentAudioId() == musicEntity.getSongId()) {
                currentlyPlayingPosition = position;
                ((ItemViewHolder) holder).onBindData(playlist.get(position), position, true);
            } else {
                ((ItemViewHolder) holder).onBindData(playlist.get(position), position, false);
            }

        }

        @Override
        public int getItemCount() {
            return playlist == null ? 0 : playlist.size();
        }

        class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.music_name)
            TextView mMusicName;

            @BindView(R.id.music_singer)
            TextView mMusicSinger;

            @BindView(R.id.view_line)
            View viewLine;

            @BindView(R.id.img_dance)
            ImageView imageView;


            public ItemViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                itemView.setOnClickListener(this);
            }

            public void onBindData(MusicEntity musicEntity, int position, boolean isPlaying) {
                if (isPlaying) {
                    mMusicName.setTextColor(mContext.getResources().getColor(R.color.medium_sea_green));
                    mMusicSinger.setTextColor(mContext.getResources().getColor(R.color.medium_sea_green));
                    viewLine.setBackgroundResource(R.color.medium_sea_green);
                    mMusicName.setText(musicEntity.getMusicName());
                    mMusicSinger.setText(musicEntity.getArtist());
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    mMusicName.setTextColor(mContext.getResources().getColor(R.color.white));
                    mMusicSinger.setTextColor(mContext.getResources().getColor(R.color.text_gray));
                    viewLine.setBackgroundResource(R.color.text_gray);
                    mMusicName.setText(musicEntity.getMusicName());
                    mMusicSinger.setText(musicEntity.getArtist());
                    imageView.setVisibility(View.GONE);
                }

            }

            @Override
            public void onClick(View v) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final int position = getAdapterPosition();
                        if (position == -1) {
                            return;
                        } else {
                            long[] ids = new long[1];
                            ids[0] = playlist.get(position).songId;
                            MusicPlayer.setQueue(position);
                            notifyItemChanged(currentlyPlayingPosition);
                            notifyItemChanged(position);
                        }
                    }
                }, 70);
            }
        }
    }
}
