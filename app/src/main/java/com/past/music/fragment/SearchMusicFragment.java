package com.past.music.fragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.past.music.MyApplication;
import com.past.music.api.MusicDetailInfo;
import com.past.music.api.SearchSongInfo;
import com.past.music.download.BMA;
import com.past.music.download.Down;
import com.past.music.entity.MusicEntity;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/10 22:30
 * 描述：
 * 备注：
 * =======================================================
 */
public class SearchMusicFragment extends KtAttachFragment {

    private MusicAdapter mAdapter;
    private ArrayList<SearchSongInfo> songInfos;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;


    public static SearchMusicFragment newInstance(ArrayList<SearchSongInfo> list) {
        SearchMusicFragment fragment = new SearchMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("searchMusic", list);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recylerview, container, false);
        if (getArguments() != null) {
            songInfos = getArguments().getParcelableArrayList("searchMusic");
        }

        recyclerView = view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getMContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MusicAdapter(songInfos);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        return view;
    }


    public class MusicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        final static int FIRST_ITEM = 0;
        final static int ITEM = 1;
        private ArrayList<SearchSongInfo> mList;

        public MusicAdapter(ArrayList<SearchSongInfo> list) {
//            if (list == null) {
//                throw new IllegalArgumentException("model Data must not be null");
//            }
            mList = list;
        }

        //更新adpter的数据
        public void updateDataSet(ArrayList<SearchSongInfo> list) {
            this.mList = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            return new ListItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_musci_common_item, viewGroup, false));
        }

//        //判断布局类型
//        @Override
//        public int getItemViewType(int position) {
//            return position == FIRST_ITEM ? FIRST_ITEM : ITEM;
//
//        }

        //将数据与界面进行绑定
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SearchSongInfo model = mList.get(position);
            if (holder instanceof ListItemViewHolder) {

                ((ListItemViewHolder) holder).mainTitle.setText(model.getTitle());
                ((ListItemViewHolder) holder).title.setText(model.getAuthor());

            }
        }

        @Override
        public int getItemCount() {
            return (null != mList ? mList.size() : 0);
        }


//        public class CommonItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//            TextView textView;
//            ImageView select;
//
//            CommonItemViewHolder(View view) {
//                super(view);
//                this.textView = (TextView) view.findViewById(R.id.play_all_number);
//                this.select = (ImageView) view.findViewById(R.id.select);
//                view.setOnClickListener(this);
//            }
//
//            public void onClick(View v) {
//
//
//            }
//
//        }


        public class ListItemViewHolder extends RecyclerView.ViewHolder {
            //ViewHolder
            ImageView moreOverflow, playState;
            TextView mainTitle, title;

            ListItemViewHolder(View view) {
                super(view);
                this.mainTitle = view.findViewById(R.id.music_name);
                this.title = view.findViewById(R.id.music_info);
                this.moreOverflow = view.findViewById(R.id.viewpager_list_button);

                moreOverflow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SearchSongInfo model = mList.get(getAdapterPosition());
                        new AlertDialog.Builder(getMContext()).setTitle("要下载音乐吗").
                                setPositiveButton(getMContext().getString(R.string.sure), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Down.downMusic(MyApplication.mContext, model.getSong_id() + "", model.getTitle(), model.getAuthor());
                                        dialog.dismiss();
                                    }
                                }).
                                setNegativeButton(getMContext().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                });
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final SearchSongInfo model = mList.get(getAdapterPosition());
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... params) {
                                MusicEntity musicInfo = new MusicEntity();
                                try {
                                    MusicDetailInfo info = null;
                                    JsonObject jsonObject = HttpUtil.getResposeJsonObject(BMA.Song.songBaseInfo(model.getSong_id()))
                                            .get("result").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();
                                    info = MyApplication.gsonInstance().fromJson(jsonObject, MusicDetailInfo.class);
                                    musicInfo.albumData = info.getPic_small();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }


                                musicInfo.songId = Integer.parseInt(model.getSong_id());
                                musicInfo.musicName = model.getTitle();
                                musicInfo.artist = model.getAuthor();
                                musicInfo.islocal = false;
                                musicInfo.albumName = model.getAlbum_title();
                                musicInfo.albumId = Integer.parseInt(model.getAlbum_id());
                                musicInfo.artistId = Integer.parseInt(model.getArtist_id());
                                musicInfo.lrc = model.getLrclink();

                                HashMap<Long, MusicEntity> infos = new HashMap<Long, MusicEntity>();
                                long[] list = new long[1];
                                list[0] = musicInfo.songId;
                                infos.put(list[0], musicInfo);
                                MusicPlayer.playAll(infos, list, 0, false);
                                return null;
                            }
                        }.execute();
                    }
                });

            }


        }
    }


}
