package com.past.music.online.params

import java.io.Serializable

/**
 * Created by gaojin on 2018/1/14.
 * 选项参数
 */
class ParamsBean : Serializable {

    var async: Int = 0
    var cmd: Int = 0
    var type: Int = 0
    var category: Int = 0
    var genre: Int = 0
    var year: Int = 0
    var company: Int = 0
    var sort: Int = 0
    var start: Int = 0
    var end: Int = 0
}