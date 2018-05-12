package com.se.music.utils

import java.util.*

/**
 * Created by gaojin on 2017/12/23.
 * 集合工具类
 */
class CollectionUtils {
    companion object {

        fun <T> isEmpty(list: List<T>?): Boolean {
            return list == null || list.isEmpty()
        }

        fun <T> inArray(t: T?, list: List<T>): Boolean {
            return if (t != null && !isEmpty(list)) list.contains(t) else false
        }

        fun <T> addAll(list: MutableList<T>, vararg ts: T) {
            val newList = Arrays.asList(*ts)
            list.addAll(newList)
        }

        fun <T> size(list: List<T>?): Int {
            return list?.size ?: 0
        }
    }
}