package com.past.music.activity;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.past.music.fragment.QuickControlsFragment;
import com.past.music.pastmusic.R;

import butterknife.ButterKnife;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:38
 * 版本：
 * 描述：Activity基类
 * 备注：
 * =======================================================
 */
public class BaseActivity extends AppCompatActivity {

    private QuickControlsFragment fragment; //底部播放控制栏

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
    }


    /**
     * @param show 显示或关闭底部播放控制栏
     */
    protected void showQuickControl(boolean show) {
//        Log.d(TAG, MusicPlayer.getQueue().length + "");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (show) {
            if (fragment == null) {
                fragment = QuickControlsFragment.newInstance();
                ft.add(R.id.bottom_container, fragment).commitAllowingStateLoss();
            } else {
                ft.show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (fragment != null)
                ft.hide(fragment).commitAllowingStateLoss();
        }
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
            this.overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        } else {
            this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
            this.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
        } else {
            this.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
        }
    }

}
