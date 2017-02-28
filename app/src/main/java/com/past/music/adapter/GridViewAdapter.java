package com.past.music.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.past.music.api.Test;
import com.past.music.widget.IconView;

import java.util.List;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/2/28 下午10:50
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class GridViewAdapter extends BaseAdapter {

    private List<Test.ShowapiResBodyBean.PagebeanBean.SonglistBean> mData;
    private Context mContext = null;

    public GridViewAdapter(Context context, List<Test.ShowapiResBodyBean.PagebeanBean.SonglistBean> list) {
        mData = list;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IconView iconView = null;
        if (convertView == null) {
            iconView = new IconView(mContext);
        } else {
            iconView = (IconView) convertView;
        }

        iconView.setLayoutParams(new GridView.LayoutParams(200, 200));
        iconView.setmHotPic(mData.get(position).getAlbumpic_big());
        iconView.setmTvDes(mData.get(position).getSongname());
        return iconView;
    }
}
