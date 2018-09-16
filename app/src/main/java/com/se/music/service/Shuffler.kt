package com.se.music.service

import java.util.*

/**
 *Author: gaojin
 *Time: 2018/9/16 下午7:46
 *随机播放算法
 */

class Shuffler {
    private val mHistoryOfNumbers = LinkedList<Int>()
    private val mPreviousNumbers = TreeSet<Int>()
    private val mRandom = Random()
    private var mPrevious: Int = 0

    fun nextInt(interval: Int): Int {
        var next: Int
        do {
            next = mRandom.nextInt(interval)
        } while (next == mPrevious && interval > 1
                && !mPreviousNumbers.contains(Integer.valueOf(next)))
        mPrevious = next
        mHistoryOfNumbers.add(mPrevious)
        mPreviousNumbers.add(mPrevious)
        cleanUpHistory()
        return next
    }

    private fun cleanUpHistory() {
        if (!mHistoryOfNumbers.isEmpty() && mHistoryOfNumbers.size >= MediaService.MAX_HISTORY_SIZE) {
            for (i in 0 until Math.max(1, MediaService.MAX_HISTORY_SIZE / 2)) {
                mPreviousNumbers.remove(mHistoryOfNumbers.removeFirst())
            }
        }
    }
}