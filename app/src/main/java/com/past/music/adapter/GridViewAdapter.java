package com.past.music.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.past.music.lastfmapi.Test;
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
    private int width;

    public GridViewAdapter(Context context, List<Test.ShowapiResBodyBean.PagebeanBean.SonglistBean> list) {
        mData = list;
        this.mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
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
            convertView = new IconView(mContext);
            iconView = (IconView) convertView;
        } else {
            iconView = (IconView) convertView;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width / 3, width / 3 + 20);
        iconView.setLayoutParams(layoutParams);
        if (position == 0 || position == 3) {
            iconView.setPadding(0, 0, 5, 0);
        }
        if (position == 2 || position == 5) {
            iconView.setPadding(5, 0, 0, 0);
        }
        iconView.setmHotPic(mData.get(position).getAlbumpic_big());
        iconView.setmTvDes(mData.get(position).getSongname());
        return convertView;
    }
}
