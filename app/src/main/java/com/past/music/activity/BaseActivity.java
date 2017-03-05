package com.past.music.activity;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.past.music.fragment.QuickControlsFragment;
import com.past.music.pastmusic.IMediaAidlInterface;
import com.past.music.pastmusic.R;
import com.past.music.service.MusicPlayer;

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
public class BaseActivity extends AppCompatActivity implements ServiceConnection {

    private MusicPlayer.ServiceToken mToken;
    public static IMediaAidlInterface mService = null;
    private QuickControlsFragment fragment; //底部播放控制栏

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mToken = MusicPlayer.bindToService(this, this);
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

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
    }

    public void unbindService() {
        if (mToken != null) {
            MusicPlayer.unbindFromService(mToken);
            mToken = null;
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = IMediaAidlInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mService = null;
    }
}
