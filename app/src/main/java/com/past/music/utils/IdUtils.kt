package com.past.music.utils

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by gaojin on 2017/12/23.
 */
class IdUtils {
    companion object {
        private val integer = AtomicInteger(0)

        /**
         * loaderId默认从0开始，每次自增1
         *
         * @return loaderId
         */
        fun generateLoaderId(): Int {
            return integer.getAndIncrement()
        }

        val GET_MUSIC_HALL = generateLoaderId()
    }
}