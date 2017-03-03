package com.past.music.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.past.music.pastmusic.R;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/3/3 下午4:44
 * 版本：
 * 描述：
 * 备注：
 * =======================================================
 */
public class BaseFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    /**
     * X轴方向滑动打开Activity
     *
     * @param intent
     * @param isInFromRight
     */
    public final void startActivityByX(Intent intent, boolean isInFromRight) {
        this.startActivity(intent);
        if (isInFromRight) {
            getActivity().overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            getActivity().overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

    /**
     * Y轴方向滑动打开Activity
     *
     * @param intent
     * @param isInFromBottom
     */
    public final void startActivityByY(Intent intent, boolean isInFromBottom) {
        this.startActivity(intent);
        if (isInFromBottom) {
            getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
        } else {
            getActivity().overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
        }
    }
}
