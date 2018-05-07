package com.past.music.mine.personal

import android.view.LayoutInflater
import android.view.View
import com.past.music.kmvp.KBaseView
import com.past.music.kmvp.KMvpPresenter
import com.past.music.pastmusic.R

/**
 * Author: gaojin
 * Time: 2018/5/6 下午7:32
 */
class MinePersonalInfoView(presenter: KMvpPresenter, viewId: Int, header: View) : KBaseView(presenter, viewId) {

    init {
        initView(header)
    }

    override fun createView(): View {
        return LayoutInflater.from(getContext()).inflate(R.layout.mine_head_layout, null)
    }
}