package com.past.music.adapter;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.past.music.entity.Mp3Info;
import com.past.music.pastmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/4 下午3:17
 * 描述：
 * 备注：Copyright © 2010-2017. gaojin All rights reserved.
 * =======================================================
 */
public class MyContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEAD_LAYOUT = 0X01;
    private static final int FUNC_LAYOUT = 0X02;
    private static final int FAVOR_TITLE_LAYOUT = 0X03;
    private static final int FAVOR_ITEM_LAYOUT = 0X04;

    private List<Mp3Info> mList = new ArrayList<>();
    private Context mContext = null;

    public MyContentAdapter(Context context) {
        this.mContext = context;
    }

    private OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, Mp3Info mp3Info, int position);
    }

    public void setListItem(List<Mp3Info> listItem) {
        mList.clear();
        mList.addAll(listItem);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == HEAD_LAYOUT) {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_head_layout, parent, false));
        } else if (viewType == FUNC_LAYOUT) {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_func_layout, parent, false));
        } else if (viewType == FAVOR_TITLE_LAYOUT) {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_title_layout, parent, false));
        } else {
            return new HeadLayoutHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mine_favor_item_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mList.size() + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_LAYOUT;
        } else if (position == 1) {
            return FUNC_LAYOUT;
        } else if (position == 2) {
            return FAVOR_TITLE_LAYOUT;
        } else {
            return FAVOR_ITEM_LAYOUT;
        }
    }

    class HeadLayoutHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.mine_head_avatar)
//        CircleImageView mHeadAvatar;
//
//        @BindView(R.id.mine_head_listen_time)
//        TextView mListenTime;
//
//        @BindView(R.id.mine_head_listen_vip)
//        TextView mListenVip;
//
//        @BindView(R.id.mine_head_user_name)
//        TextView mUserName;

        public HeadLayoutHolder(View itemView) {
            super(itemView);
//            ButterKnife.bind(this, itemView);
        }
    }
}
