package com.se.music.online.model

import com.google.gson.annotations.SerializedName

/**
 * Created by gaojin on 2018/1/28.
 */
class NewSong {
    /**
     * data : {"album_list":[{"album":{"id":3767136,"mid":"001f5qLB0VIsA2","name":"8090","subtitle":"","time_public":"2017-12-14","title":"8090"},"author":[{"id":1327288,"mid":"001xruwk2A0Vw7","name":"赵方婧","title":"赵方婧","type":1,"uin":0}]},{"album":{"id":3767110,"mid":"001dsBel46bkC8","name":"好聚不好散","subtitle":"","time_public":"2017-12-14","title":"好聚不好散"},"author":[{"id":1278829,"mid":"000EwSOA2udDjs","name":"孙晨","title":"孙晨","type":0,"uin":0}]}],"category_info":[{"id":-1,"title":"全部"},{"id":0,"title":"专辑"},{"id":11,"title":"EP"},{"id":10,"title":"Single"},{"id":1,"title":"演唱会"},{"id":3,"title":"动漫"},{"id":4,"title":"游戏"}],"company_info":[{"id":-1,"title":"全部"},{"id":35,"title":"环球唱片"},{"id":5,"title":"索尼音乐"},{"id":3,"title":"华纳唱片"},{"id":1360,"title":"少城时代"},{"id":2,"title":"英皇唱片"},{"id":373,"title":"金牌大风"},{"id":10,"title":"福茂唱片"},{"id":7913,"title":"梦响强音"},{"id":20,"title":"华谊兄弟"},{"id":1020,"title":"乐华圆娱"},{"id":2597,"title":"梦响当然"},{"id":12692,"title":"时代峰峻"},{"id":1110,"title":"通力时代"},{"id":1167,"title":"简单快乐"},{"id":6,"title":"滚石唱片"},{"id":357,"title":"相信音乐"},{"id":63,"title":"海蝶音乐"},{"id":24,"title":"太合麦田"},{"id":1892,"title":"华宇世博"},{"id":53,"title":"摩登天空"},{"id":146,"title":"亚神音乐"}],"genre_info":[{"id":0,"title":"全部"},{"id":1,"title":"流行"},{"id":2,"title":"古典"},{"id":3,"title":"爵士"},{"id":36,"title":"摇滚"},{"id":22,"title":"电子"},{"id":27,"title":"拉丁"},{"id":21,"title":"轻音乐"},{"id":39,"title":"世界音乐"},{"id":34,"title":"嘻哈"},{"id":37,"title":"原声"},{"id":19,"title":"乡村"},{"id":20,"title":"舞曲"},{"id":33,"title":"R&B"},{"id":23,"title":"民谣"},{"id":28,"title":"金属"}],"size":17400,"type":1,"type_info":[{"id":1,"report":"","title":"内地"},{"id":2,"report":"","title":"港台"},{"id":3,"report":"","title":"欧美"},{"id":4,"report":"","title":"日本"},{"id":5,"report":"","title":"韩国"},{"id":6,"report":"","title":"其他"}],"year_info":[{"id":1,"title":"全部"},{"id":14,"title":"2017"},{"id":7,"title":"2016"},{"id":8,"title":"2015"},{"id":9,"title":"2014"},{"id":15,"title":"2013"},{"id":16,"title":"2012"},{"id":2,"title":"一零年代"},{"id":3,"title":"零零年代"},{"id":4,"title":"九十年代"},{"id":5,"title":"八十年代"},{"id":6,"title":"七十年代"},{"id":13,"title":"六十年代"}]}
     * code : 0
     */

    var data: DataBean? = null
    var code: Int = 0

    class DataBean {
        /**
         * album_list : [{"album":{"id":3767136,"mid":"001f5qLB0VIsA2","name":"8090","subtitle":"","time_public":"2017-12-14","title":"8090"},"author":[{"id":1327288,"mid":"001xruwk2A0Vw7","name":"赵方婧","title":"赵方婧","type":1,"uin":0}]},{"album":{"id":3767110,"mid":"001dsBel46bkC8","name":"好聚不好散","subtitle":"","time_public":"2017-12-14","title":"好聚不好散"},"author":[{"id":1278829,"mid":"000EwSOA2udDjs","name":"孙晨","title":"孙晨","type":0,"uin":0}]}]
         * category_info : [{"id":-1,"title":"全部"},{"id":0,"title":"专辑"},{"id":11,"title":"EP"},{"id":10,"title":"Single"},{"id":1,"title":"演唱会"},{"id":3,"title":"动漫"},{"id":4,"title":"游戏"}]
         * company_info : [{"id":-1,"title":"全部"},{"id":35,"title":"环球唱片"},{"id":5,"title":"索尼音乐"},{"id":3,"title":"华纳唱片"},{"id":1360,"title":"少城时代"},{"id":2,"title":"英皇唱片"},{"id":373,"title":"金牌大风"},{"id":10,"title":"福茂唱片"},{"id":7913,"title":"梦响强音"},{"id":20,"title":"华谊兄弟"},{"id":1020,"title":"乐华圆娱"},{"id":2597,"title":"梦响当然"},{"id":12692,"title":"时代峰峻"},{"id":1110,"title":"通力时代"},{"id":1167,"title":"简单快乐"},{"id":6,"title":"滚石唱片"},{"id":357,"title":"相信音乐"},{"id":63,"title":"海蝶音乐"},{"id":24,"title":"太合麦田"},{"id":1892,"title":"华宇世博"},{"id":53,"title":"摩登天空"},{"id":146,"title":"亚神音乐"}]
         * genre_info : [{"id":0,"title":"全部"},{"id":1,"title":"流行"},{"id":2,"title":"古典"},{"id":3,"title":"爵士"},{"id":36,"title":"摇滚"},{"id":22,"title":"电子"},{"id":27,"title":"拉丁"},{"id":21,"title":"轻音乐"},{"id":39,"title":"世界音乐"},{"id":34,"title":"嘻哈"},{"id":37,"title":"原声"},{"id":19,"title":"乡村"},{"id":20,"title":"舞曲"},{"id":33,"title":"R&B"},{"id":23,"title":"民谣"},{"id":28,"title":"金属"}]
         * size : 17400
         * type : 1
         * type_info : [{"id":1,"report":"","title":"内地"},{"id":2,"report":"","title":"港台"},{"id":3,"report":"","title":"欧美"},{"id":4,"report":"","title":"日本"},{"id":5,"report":"","title":"韩国"},{"id":6,"report":"","title":"其他"}]
         * year_info : [{"id":1,"title":"全部"},{"id":14,"title":"2017"},{"id":7,"title":"2016"},{"id":8,"title":"2015"},{"id":9,"title":"2014"},{"id":15,"title":"2013"},{"id":16,"title":"2012"},{"id":2,"title":"一零年代"},{"id":3,"title":"零零年代"},{"id":4,"title":"九十年代"},{"id":5,"title":"八十年代"},{"id":6,"title":"七十年代"},{"id":13,"title":"六十年代"}]
         */

        var size: Int = 0
        var type: Int = 0
        var album_list: List<AlbumListBean>? = null
        var category_info: List<CategoryInfoBean>? = null
        var company_info: List<CompanyInfoBean>? = null
        var genre_info: List<GenreInfoBean>? = null
        var type_info: List<TypeInfoBean>? = null
        var year_info: List<YearInfoBean>? = null

        class AlbumListBean {
            /**
             * album : {"id":3767136,"mid":"001f5qLB0VIsA2","name":"8090","subtitle":"","time_public":"2017-12-14","title":"8090"}
             * author : [{"id":1327288,"mid":"001xruwk2A0Vw7","name":"赵方婧","title":"赵方婧","type":1,"uin":0}]
             */

            var album: AlbumBean? = null
            var author: List<AuthorBean>? = null

            class AlbumBean {
                /**
                 * id : 3767136
                 * mid : 001f5qLB0VIsA2
                 * name : 8090
                 * subtitle :
                 * time_public : 2017-12-14
                 * title : 8090
                 */

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var subtitle: String? = null
                var time_public: String? = null
                var title: String? = null
            }

            class AuthorBean {
                /**
                 * id : 1327288
                 * mid : 001xruwk2A0Vw7
                 * name : 赵方婧
                 * title : 赵方婧
                 * type : 1
                 * uin : 0
                 */

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var title: String? = null
                var type: Int = 0
                var uin: Int = 0
            }
        }

        class CategoryInfoBean {
            /**
             * id : -1
             * title : 全部
             */

            var id: Int = 0
            var title: String? = null
        }

        class CompanyInfoBean {
            /**
             * id : -1
             * title : 全部
             */

            var id: Int = 0
            var title: String? = null
        }

        class GenreInfoBean {
            /**
             * id : 0
             * title : 全部
             */

            var id: Int = 0
            var title: String? = null
        }

        class TypeInfoBean {
            /**
             * id : 1
             * report :
             * title : 内地
             */

            var id: Int = 0
            var report: String? = null
            var title: String? = null
        }

        class YearInfoBean {
            /**
             * id : 1
             * title : 全部
             */

            var id: Int = 0
            var title: String? = null
        }
    }
}