package com.past.music.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.past.music.pastmusic.R;

/**
 * Created by gaojin on 2017/3/25.
 */

public class MusicListDialog extends AlertDialog {

    private Context mContext;

    public MusicListDialog(@NonNull Context context) {
        super(context, R.style.style_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_play_list);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.bottom_dialog_in_style);
    }


    @Override
    public void show() {
        super.show();
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = (int) (dm.heightPixels * 0.6);
//        this.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
        this.getWindow().setLayout(width, height);
    }
}
