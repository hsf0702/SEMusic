package com.past.music;/**
 * Created by gaojin on 2017/1/26.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.past.music.pastmusic.R;

/**
 * =======================================================
 * 作者：gaojin
 * 日期：2017/1/26 上午12:38
 * 版本：
 * 描述：baseActivity
 * 备注：
 * =======================================================
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
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

    public final void startActivityByY(Intent intent) {
        this.startActivity(intent);
        this.overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_bottom);
    }

}
