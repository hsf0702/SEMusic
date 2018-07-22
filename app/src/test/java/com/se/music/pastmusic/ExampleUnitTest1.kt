package com.se.music.pastmusic

import android.view.View
import org.junit.Test

/**
 *Author: gaojin
 *Time: 2018/7/22 下午5:58
 */

class ExampleUnitTest1 {
    @Test
    fun testColor() {
        val s = Integer.toHexString(4334209)
        println(s)
        val string = "111222333444555222"
        println("string.replace(\"222\",\"ddd\") = " + string.replace("222", "ddd"))
        println(View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}