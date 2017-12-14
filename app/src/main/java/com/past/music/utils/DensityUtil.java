package com.past.music.utils;

import android.content.Context;

/**
 * Created by gaojin on 2017/3/25.
 */

public class DensityUtil {
    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
