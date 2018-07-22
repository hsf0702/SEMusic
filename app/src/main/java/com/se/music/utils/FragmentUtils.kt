package com.se.music.utils

import android.support.v4.app.Fragment
import com.se.music.R
import com.se.music.base.BaseActivity
import com.se.music.base.mvp.MvpPage

/**
 *Author: gaojin
 *Time: 2018/5/27 下午11:52
 * Fragment跳转工具
 */

fun startFragment(current: Fragment, target: Fragment, tag: String) {
    val ft = current.fragmentManager!!.beginTransaction()
    ft.setCustomAnimations(R.anim.slide_right_in
            , R.anim.slide_left_out
            , R.anim.slide_left_in
            , R.anim.slide_right_out)
            .hide((current.context as BaseActivity).supportFragmentManager.findFragmentByTag(tag))
            .addToBackStack(null)
            .add(R.id.se_main_content, target)
            .commit()
}

fun startFragment(current: MvpPage, target: Fragment, tag: String) {
    if (current is Fragment) {
        startFragment(current as Fragment, target, tag)
    }
}