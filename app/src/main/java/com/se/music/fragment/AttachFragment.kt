package com.se.music.fragment

import android.app.Activity
import android.support.v4.app.Fragment

/**
 * Created by gaojin on 2017/11/28.
 */
open class AttachFragment : Fragment() {
    var mContext: Activity? = null

    override fun onAttach(activity: Activity?) {
        super.onAttach(activity)
        this.mContext = activity
    }
}