package com.past.music.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.past.music.api.HotListResponse;
import com.past.music.pastmusic.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 19:51
 * 描述：
 * 备注：
 * =======================================================
 */
public class SongListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean> mList;

    public SongListAdapter(Context context) {
        this.mContext = context;
    }

    public void updateList(List<HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecommendListHolder(LayoutInflater.from(mContext).inflate(R.layout.recommend_song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((RecommendListHolder) holder).onBind(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    class RecommendListHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recommend_img_singer)
        SimpleDraweeView mImgSong;

        @BindView(R.id.recommend_singer_list_title)
        TextView mTitle;

        @BindView(R.id.recommend_singer_list_info)
        TextView mInfo;

        @BindView(R.id.rl_recommend_singer_item)
        RelativeLayout relativeLayout;


        public RecommendListHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(final HotListResponse.ShowapiResBodyBean.PagebeanBean.SonglistBean songlistBean) {
            mTitle.setText(songlistBean.getSongname());
            mTitle.setTextColor(0xffffffff);
            relativeLayout.setBackgroundResource(R.color.test);
            mInfo.setText(songlistBean.getSingername() + " • " + "专辑");
            mImgSong.setImageURI(songlistBean.getAlbumpic_big());
        }
    }
}
