package com.se.music.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.se.music.entity.MusicEntity;
import com.se.music.pastmusic.R;
import com.se.music.service.MusicPlayer;
import com.se.music.utils.HandlerUtil;
import com.se.music.utils.MConstants;
import com.se.music.utils.MusicUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wm on 2016/1/18.
 */
public class DownMusicFragment extends BaseFragment {
    private String folder_path;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private FolderDetailAdapter folderDetailAdapter;

    public static DownMusicFragment newInstance(String id, boolean useTransition, String transitionName) {
        DownMusicFragment fragment = new DownMusicFragment();
        Bundle args = new Bundle();
        args.putString("folder_path", id);
        args.putBoolean("transition", useTransition);
        if (useTransition)
            args.putString("transition_name", transitionName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folder_path = getArguments().getString("folder_path");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.down_music_recylerview, container, false);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(layoutManager);
        folderDetailAdapter = new FolderDetailAdapter(null);
        recyclerView.setAdapter(folderDetailAdapter);
        recyclerView.setHasFixedSize(true);
        setItemDecoration();
        reloadAdapter();
        return view;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        reloadAdapter();
    }

    //设置分割线
    private void setItemDecoration() {

    }

    //更新adapter界面
    public void reloadAdapter() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... unused) {
                boolean hasFolder = false;
                File file = new File(folder_path);
                if (!file.exists()) {
                    hasFolder = file.mkdirs();
                } else {
                    hasFolder = true;
                }
                if (hasFolder) {
                    List<MusicEntity> albumList = MusicUtils.Companion.queryMusic(getContext(), folder_path, MConstants.Companion.getSTART_FROM_FOLDER());
                    folderDetailAdapter.updateDataSet(albumList);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                folderDetailAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    class FolderDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        List<MusicEntity> mList;

        public FolderDetailAdapter(List<MusicEntity> musicInfos) {
            mList = musicInfos;
            //list.add(0,null);
        }

        //更新adpter的数据
        public void updateDataSet(List<MusicEntity> list) {
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            if (viewType == FIRST_ITEM) {
                return new CommonItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.common_item, viewGroup, false));
            } else {
                return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_musci_common_item, viewGroup, false));
            }
        }

        @Override
        public int getItemViewType(int position) {
            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder instanceof CommonItemViewHolder) {
                ((CommonItemViewHolder) holder).textView.setText("共" + mList.size() + "首");

                ((CommonItemViewHolder) holder).select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), SelectActivity.class);
//                        intent.putParcelableArrayListExtra("ids", (ArrayList) mList);
//                        getContext().startActivity(intent);
                    }
                });
            }
            if (holder instanceof ListItemViewHolder) {
                MusicEntity musicInfo = mList.get(position - 1);
                ((ListItemViewHolder) holder).mainTitle.setText(musicInfo.getMusicName());
                ((ListItemViewHolder) holder).title.setText(musicInfo.getArtist());
            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() + 1 : 0);
        }


        class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;
            ImageView select;

            CommonItemViewHolder(View view) {
                super(view);
                this.textView = view.findViewById(R.id.play_all_number);
                this.select = view.findViewById(R.id.select);
                view.setOnClickListener(this);
            }

            //播放文件夹
            @Override
            public void onClick(View v) {
                HandlerUtil.Companion.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        HashMap<Long, MusicEntity> infos = new HashMap();
                        for (int i = 0; i < mList.size(); i++) {
                            MusicEntity info = mList.get(i);
                            list[i] = info.getSongId();
                            info.setIslocal(true);
                            info.setAlbumData(MusicUtils.Companion.getAlbumArtUri(info.getAlbumId()) + "");
                            infos.put(list[i], mList.get(i));
                        }
                        MusicPlayer.Companion.playAll(infos, list, 0, false);
                    }
                }, 70);
            }


        }

        public class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //RecommendViewHolder
            ImageView moreOverflow;
            TextView mainTitle, title;

            ListItemViewHolder(View view) {
                super(view);
                this.mainTitle = view.findViewById(R.id.music_name);
                this.title = view.findViewById(R.id.music_info);
                this.moreOverflow = view.findViewById(R.id.viewpager_list_button);

                //设置弹出菜单
                moreOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
                view.setOnClickListener(this);

            }

            //播放歌曲
            @Override
            public void onClick(View v) {
                HandlerUtil.Companion.getInstance().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        long[] list = new long[mList.size()];
                        HashMap<Long, MusicEntity> infos = new HashMap();
                        for (int i = 0; i < mList.size(); i++) {
                            MusicEntity info = mList.get(i);
                            list[i] = info.getSongId();
                            info.setIslocal(true);
                            info.setAlbumData(MusicUtils.Companion.getAlbumArtUri(info.getAlbumId()) + "");
                            infos.put(list[i], mList.get(i));
                        }
                        if (getAdapterPosition() > 0)
                            MusicPlayer.Companion.playAll(infos, list, getAdapterPosition() - 1, false);
                    }
                }, 60);
            }

        }

    }
}
