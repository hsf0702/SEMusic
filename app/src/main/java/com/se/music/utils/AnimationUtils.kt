package com.se.music.utils

import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.animation.Interpolator

/**
 * Author: gaojin
 * Time: 2018/5/6 下午5:28
 */
class AnimationUtils {
    companion object {
        val FAST_OUT_SLOW_IN_INTERPOLATOR: Interpolator = FastOutSlowInInterpolator()

        fun lerp(startValue: Int, endValue: Int, fraction: Float): Int {
            return startValue + Math.round(fraction * (endValue - startValue))
        }
    }
}