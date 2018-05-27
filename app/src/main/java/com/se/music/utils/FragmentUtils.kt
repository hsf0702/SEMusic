package com.se.music.utils

import android.app.Activity
import android.support.v4.app.Fragment
import com.se.music.R
import com.se.music.base.BaseActivity

/**
 *Author: gaojin
 *Time: 2018/5/27 下午11:52
 * Fragment跳转工具
 */

fun jumpToFragment(activity: Activity, target: Fragment) {
    if (activity is BaseActivity) {
        val ft = activity.supportFragmentManager.beginTransaction()
        ft.addToBackStack(null)
        ft.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out)
        ft.add(R.id.content, target).commit()
    }
}