package com.past.music.online.params

import java.io.Serializable

/**
 * Created by gaojin on 2018/1/1.
 */
class CommonParams : Serializable {
    /**
     * comm : {"ct":24}
     * recomPlaylist : {"method":"get_hot_recommend","param":{"async":1,"cmd":2},"module":"playlist.HotRecommendServer"}
     */

    var comm: Comm = Comm()
    var recomPlaylist: RecomPlaylist = RecomPlaylist()

    class Comm : Serializable {
        /**
         * ct : 24
         */
        var ct: Int = 24
    }

    class RecomPlaylist : Serializable {

        /**
         * method : get_hot_recommend
         * param : {"async":1,"cmd":2}
         * module : playlist.HotRecommendServer
         */

        var method: String = "get_hot_recommend"
        var param: ParamBean = ParamBean()
        var module: String? = "playlist.HotRecommendServer"
    }

    class ParamBean : Serializable {
        /**
         * async : 1
         * cmd : 2
         */
        var async: Int = 1
        var cmd: Int = 2
    }
}