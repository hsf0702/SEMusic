package com.se.music.mine.personal

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import com.se.music.R
import com.se.music.base.kmvp.KBaseView
import com.se.music.base.kmvp.KMvpPresenter

/**
 * Author: gaojin
 * Time: 2018/5/6 下午7:32
 */
class MinePersonalInfoView(presenter: KMvpPresenter, viewId: Int, header: View) : KBaseView(presenter, viewId) {

    init {
        initView(header)
    }

    @SuppressLint("InflateParams")
    override fun createView(): View {
        return LayoutInflater.from(getContext()).inflate(R.layout.mine_head_layout, null)
    }
}