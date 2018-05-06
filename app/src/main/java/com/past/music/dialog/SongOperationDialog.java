package com.past.music.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.past.music.activity.SelectSongListActivity;
import com.past.music.adapter.MusicFlowAdapter;
import com.past.music.adapter.OverFlowAdapter;
import com.past.music.entity.MusicEntity;
import com.past.music.entity.OverFlowItem;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;
import com.past.music.utils.HandlerUtil;
import com.past.music.utils.MConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/5/17 21:09
 * 描述：
 * 备注：
 * =======================================================
 */
public class SongOperationDialog extends AlertDialog {

    private double heightPercent;
    private Context mContext;
    private MusicFlowAdapter muaicflowAdapter;
    private OverFlowAdapter commonAdapter;
    private int type;
    private String id;
    private String musicName, artist, albumId, albumName;
    private long playlistId = -1;
    private Handler mHandler;
    private MusicEntity adapterMusicEntity;
    private List<OverFlowItem> mlistInfo = new ArrayList<>();  //声明一个list，动态存储要显示的信息
    private ArrayList<MusicEntity> list = null;


    @BindView(R.id.pop_list_title)
    TextView topTitle;

    @BindView(R.id.pop_list)
    RecyclerView recyclerView;

    public SongOperationDialog(Context context) {
        super(context, R.style.style_dialog);
        this.mContext = context;
    }

    public SongOperationDialog(Context context, MusicEntity musicEntity, int type) {
        super(context, R.style.style_dialog);
        this.mContext = context;
        adapterMusicEntity = musicEntity;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_song_operation);
        ButterKnife.bind(this);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        mHandler = HandlerUtil.Companion.getInstance();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);
        getList();
        setClick();
    }

    private void getList() {
        if (type == MConstants.MUSICOVERFLOW) {
            if (adapterMusicEntity == null) {
                adapterMusicEntity = new MusicEntity();
            }
            artist = adapterMusicEntity.artist;
            albumId = adapterMusicEntity.albumId + "";
            albumName = adapterMusicEntity.albumName;
            musicName = adapterMusicEntity.musicName;
            topTitle.setText("歌曲：" + " " + musicName);
            heightPercent = 0.6;
            setMusicInfo();
            muaicflowAdapter = new MusicFlowAdapter(mContext, mlistInfo, adapterMusicEntity);

        } else {
//            switch (type) {
//                case MConstants.ARTISTOVERFLOW:
//                    String artist = id;
//                    list = MusicUtils.queryMusic(mContext, artist, MConstants.START_FROM_ARTIST);
//                    topTitle.setText("歌曲：" + " " + list.get(0).artist);
//                    break;
//                case MConstants.ALBUMOVERFLOW:
//                    String albumId = id;
//                    list = MusicUtils.queryMusic(mContext, albumId, MConstants.START_FROM_ALBUM);
//                    topTitle.setText("专辑：" + " " + list.get(0).albumName);
//                    break;
//                case MConstants.FOLDEROVERFLOW:
//                    String folder = id;
//                    list = MusicUtils.queryMusic(mContext, folder, MConstants.START_FROM_FOLDER);
//                    topTitle.setText("文件夹：" + " " + folder);
//                    break;
//            }
            setCommonInfo();
            heightPercent = 0.3;
            commonAdapter = new OverFlowAdapter(mContext, mlistInfo);

        }
    }

    private void setClick() {
        if (muaicflowAdapter != null) {
            muaicflowAdapter.setOnItemClickListener(new MusicFlowAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, String data) {
                    switch (Integer.parseInt(data)) {
                        case 0:
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (adapterMusicEntity.songId == MusicPlayer.Companion.getCurrentAudioId())
                                        return;

                                    long[] ids = new long[1];
                                    ids[0] = adapterMusicEntity.songId;
                                    HashMap<Long, MusicEntity> map = new HashMap<Long, MusicEntity>();
                                    map.put(ids[0], adapterMusicEntity);
//                                    MusicPlayer.Companion.playNext(mContext, map, ids);
                                }
                            }, 100);

                            dismiss();
                            break;
                        case 1:
                            SelectSongListActivity.startActivity(mContext, adapterMusicEntity);
                            dismiss();
                            break;
                        case 2:
                            break;
                        case 3:
                            break;
                        case 4:
                            break;
                        case 5:
                            break;
                        case 6:
                            break;
                        case 7:
                            break;
                        default:
                            break;
                    }
                }
            });
            recyclerView.setAdapter(muaicflowAdapter);
            return;
        }
        commonAdapter.setOnItemClickListener(new OverFlowAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                switch (Integer.parseInt(data)) {
                    case 0:
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                HashMap<Long, MusicEntity> infos = new HashMap<Long, MusicEntity>();
                                int len = list.size();
                                long[] listid = new long[len];
                                for (int i = 0; i < len; i++) {
                                    MusicEntity info = list.get(i);
                                    listid[i] = info.songId;
                                    infos.put(listid[i], info);
                                }

                                MusicPlayer.Companion.playAll(infos, listid, 0, false);
                            }
                        }, 60);
                        dismiss();
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        recyclerView.setAdapter(commonAdapter);
    }

    //设置音乐overflow条目
    private void setMusicInfo() {
        //设置mlistInfo，listview要显示的内容
        setInfo("下一首播放", R.drawable.lay_icn_next);
        setInfo("收藏到歌单", R.drawable.lay_icn_fav);
        setInfo("分享", R.drawable.lay_icn_share);
        setInfo("删除", R.drawable.lay_icn_delete);
        setInfo("歌手：" + artist, R.drawable.lay_icn_artist);
        setInfo("专辑：" + albumName, R.drawable.lay_icn_alb);
        setInfo("设为铃声", R.drawable.lay_icn_ring);
        setInfo("查看歌曲信息", R.drawable.lay_icn_document);
    }

    //设置专辑，艺术家，文件夹overflow条目
    private void setCommonInfo() {
        setInfo("播放", R.drawable.lay_icn_play);
        setInfo("收藏到歌单", R.drawable.lay_icn_fav);
        setInfo("删除", R.drawable.lay_icn_delete);
    }

    //为info设置数据，并放入mlistInfo
    public void setInfo(String title, int id) {
        // mlistInfo.clear();
        OverFlowItem information = new OverFlowItem();
        information.setTitle(title);
        information.setAvatar(id);
        mlistInfo.add(information); //将新的info对象加入到信息列表中
    }

    @Override
    public void show() {
        super.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        this.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
    }
}
