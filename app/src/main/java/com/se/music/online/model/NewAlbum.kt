package com.se.music.online.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by gaojin on 2018/1/28.
 */
class NewAlbum : Serializable {
    /**
     * data : {"size":2,"song_list":[{"action":{"alert":100002,"icons":8060,"msgdown":0,"msgfav":0,"msgid":14,"msgpay":6,"msgshare":0,"switch":17413891},"album":{"id":3889202,"mid":"001AiGTr4ZG1OK","name":"隐隐作秀","subtitle":"","time_public":"2018-01-25","title":"隐隐作秀"},"bpm":0,"data_type":0,"file":{"media_mid":"003Tz8UT3hwEfi","size_128mp3":3051300,"size_192aac":4612917,"size_192ogg":4132519,"size_24aac":593615,"size_320mp3":7627953,"size_48aac":1164195,"size_96aac":2328938,"size_ape":19288519,"size_dts":0,"size_flac":19236194,"size_try":0,"try_begin":0,"try_end":0},"fnote":4009,"genre":1,"id":212914098,"index_album":1,"index_cd":0,"interval":190,"isonly":1,"ksong":{"id":3262715,"mid":"001YIgul246MJD"},"label":"0","language":0,"mid":"003Tz8UT3hwEfi","modify_stamp":0,"mv":{"id":0,"name":"","title":"","vid":""},"name":"隐隐作秀","pay":{"pay_down":1,"pay_month":1,"pay_play":0,"pay_status":0,"price_album":0,"price_track":200,"time_free":0},"singer":[{"id":199515,"mid":"0003ZpE43ypssl","name":"张碧晨","title":"张碧晨","type":1,"uin":0}],"status":0,"subtitle":"","time_public":"2018-01-25","title":"隐隐作秀","trace":"","type":0,"url":"http://stream2.qqmusic.qq.com/224914098.wma","version":0,"volume":{"gain":-5.336,"lra":11.996,"peak":0.956}},{"action":{"alert":11,"icons":4680,"msgdown":0,"msgfav":0,"msgid":0,"msgpay":0,"msgshare":0,"switch":603923},"album":{"id":3894532,"mid":"002f1OAc3dqJd6","name":"一步一年新","subtitle":"","time_public":"2018-01-26","title":"一步一年新"},"bpm":0,"data_type":0,"file":{"media_mid":"003P3ToV1O8I8s","size_128mp3":3108982,"size_192aac":4700502,"size_192ogg":4377429,"size_24aac":602209,"size_320mp3":7772153,"size_48aac":1182588,"size_96aac":2364648,"size_ape":24071989,"size_dts":0,"size_flac":24138332,"size_try":0,"try_begin":0,"try_end":0},"fnote":0,"genre":1,"id":212955501,"index_album":1,"index_cd":0,"interval":194,"isonly":0,"ksong":{"id":3267142,"mid":"0017KJw63N46zm"},"label":"0","language":0,"mid":"003P3ToV1O8I8s","modify_stamp":0,"mv":{"id":0,"name":"","title":"","vid":""},"name":"一步一年新","pay":{"pay_down":0,"pay_month":0,"pay_play":0,"pay_status":0,"price_album":0,"price_track":0,"time_free":0},"singer":[{"id":12111,"mid":"004YXxql1sSr2o","name":"金志文","title":"金志文","type":0,"uin":0}],"status":31,"subtitle":"","time_public":"2018-01-26","title":"一步一年新","trace":"","type":0,"url":"http://stream2.qqmusic.qq.com/224955501.wma","version":0,"volume":{"gain":-10.082,"lra":4.261,"peak":1}}],"type":1,"type_info":[{"id":1,"report":"10_0_0_1_10001_1","title":"内地"},{"id":2,"report":"10_0_0_2_10001_1","title":"港台"},{"id":3,"report":"10_0_0_3_10001_1","title":"欧美"},{"id":4,"report":"10_0_0_4_10001_1","title":"日本"},{"id":5,"report":"10_0_0_5_10001_1","title":"韩国"}]}
     * code : 0
     */

    var data: DataBean? = null
    var code: Int = 0

    class DataBean {
        /**
         * size : 2
         * song_list : [{"action":{"alert":100002,"icons":8060,"msgdown":0,"msgfav":0,"msgid":14,"msgpay":6,"msgshare":0,"switch":17413891},"album":{"id":3889202,"mid":"001AiGTr4ZG1OK","name":"隐隐作秀","subtitle":"","time_public":"2018-01-25","title":"隐隐作秀"},"bpm":0,"data_type":0,"file":{"media_mid":"003Tz8UT3hwEfi","size_128mp3":3051300,"size_192aac":4612917,"size_192ogg":4132519,"size_24aac":593615,"size_320mp3":7627953,"size_48aac":1164195,"size_96aac":2328938,"size_ape":19288519,"size_dts":0,"size_flac":19236194,"size_try":0,"try_begin":0,"try_end":0},"fnote":4009,"genre":1,"id":212914098,"index_album":1,"index_cd":0,"interval":190,"isonly":1,"ksong":{"id":3262715,"mid":"001YIgul246MJD"},"label":"0","language":0,"mid":"003Tz8UT3hwEfi","modify_stamp":0,"mv":{"id":0,"name":"","title":"","vid":""},"name":"隐隐作秀","pay":{"pay_down":1,"pay_month":1,"pay_play":0,"pay_status":0,"price_album":0,"price_track":200,"time_free":0},"singer":[{"id":199515,"mid":"0003ZpE43ypssl","name":"张碧晨","title":"张碧晨","type":1,"uin":0}],"status":0,"subtitle":"","time_public":"2018-01-25","title":"隐隐作秀","trace":"","type":0,"url":"http://stream2.qqmusic.qq.com/224914098.wma","version":0,"volume":{"gain":-5.336,"lra":11.996,"peak":0.956}},{"action":{"alert":11,"icons":4680,"msgdown":0,"msgfav":0,"msgid":0,"msgpay":0,"msgshare":0,"switch":603923},"album":{"id":3894532,"mid":"002f1OAc3dqJd6","name":"一步一年新","subtitle":"","time_public":"2018-01-26","title":"一步一年新"},"bpm":0,"data_type":0,"file":{"media_mid":"003P3ToV1O8I8s","size_128mp3":3108982,"size_192aac":4700502,"size_192ogg":4377429,"size_24aac":602209,"size_320mp3":7772153,"size_48aac":1182588,"size_96aac":2364648,"size_ape":24071989,"size_dts":0,"size_flac":24138332,"size_try":0,"try_begin":0,"try_end":0},"fnote":0,"genre":1,"id":212955501,"index_album":1,"index_cd":0,"interval":194,"isonly":0,"ksong":{"id":3267142,"mid":"0017KJw63N46zm"},"label":"0","language":0,"mid":"003P3ToV1O8I8s","modify_stamp":0,"mv":{"id":0,"name":"","title":"","vid":""},"name":"一步一年新","pay":{"pay_down":0,"pay_month":0,"pay_play":0,"pay_status":0,"price_album":0,"price_track":0,"time_free":0},"singer":[{"id":12111,"mid":"004YXxql1sSr2o","name":"金志文","title":"金志文","type":0,"uin":0}],"status":31,"subtitle":"","time_public":"2018-01-26","title":"一步一年新","trace":"","type":0,"url":"http://stream2.qqmusic.qq.com/224955501.wma","version":0,"volume":{"gain":-10.082,"lra":4.261,"peak":1}}]
         * type : 1
         * type_info : [{"id":1,"report":"10_0_0_1_10001_1","title":"内地"},{"id":2,"report":"10_0_0_2_10001_1","title":"港台"},{"id":3,"report":"10_0_0_3_10001_1","title":"欧美"},{"id":4,"report":"10_0_0_4_10001_1","title":"日本"},{"id":5,"report":"10_0_0_5_10001_1","title":"韩国"}]
         */

        var size: Int = 0
        var type: Int = 0
        var song_list: List<SongListBean>? = null
        var type_info: List<TypeInfoBean>? = null

        class SongListBean {
            /**
             * action : {"alert":100002,"icons":8060,"msgdown":0,"msgfav":0,"msgid":14,"msgpay":6,"msgshare":0,"switch":17413891}
             * album : {"id":3889202,"mid":"001AiGTr4ZG1OK","name":"隐隐作秀","subtitle":"","time_public":"2018-01-25","title":"隐隐作秀"}
             * bpm : 0
             * data_type : 0
             * file : {"media_mid":"003Tz8UT3hwEfi","size_128mp3":3051300,"size_192aac":4612917,"size_192ogg":4132519,"size_24aac":593615,"size_320mp3":7627953,"size_48aac":1164195,"size_96aac":2328938,"size_ape":19288519,"size_dts":0,"size_flac":19236194,"size_try":0,"try_begin":0,"try_end":0}
             * fnote : 4009
             * genre : 1
             * id : 212914098
             * index_album : 1
             * index_cd : 0
             * interval : 190
             * isonly : 1
             * ksong : {"id":3262715,"mid":"001YIgul246MJD"}
             * label : 0
             * language : 0
             * mid : 003Tz8UT3hwEfi
             * modify_stamp : 0
             * mv : {"id":0,"name":"","title":"","vid":""}
             * name : 隐隐作秀
             * pay : {"pay_down":1,"pay_month":1,"pay_play":0,"pay_status":0,"price_album":0,"price_track":200,"time_free":0}
             * singer : [{"id":199515,"mid":"0003ZpE43ypssl","name":"张碧晨","title":"张碧晨","type":1,"uin":0}]
             * status : 0
             * subtitle :
             * time_public : 2018-01-25
             * title : 隐隐作秀
             * trace :
             * type : 0
             * url : http://stream2.qqmusic.qq.com/224914098.wma
             * version : 0
             * volume : {"gain":-5.336,"lra":11.996,"peak":0.956}
             */

            var action: ActionBean? = null
            var album: AlbumBean? = null
            var bpm: Int = 0
            var data_type: Int = 0
            var file: FileBean? = null
            var fnote: Int = 0
            var genre: Int = 0
            var id: Int = 0
            var index_album: Int = 0
            var index_cd: Int = 0
            var interval: Int = 0
            var isonly: Int = 0
            var ksong: KsongBean? = null
            var label: String? = null
            var language: Int = 0
            var mid: String? = null
            var modify_stamp: Int = 0
            var mv: MvBean? = null
            var name: String? = null
            var pay: PayBean? = null
            var status: Int = 0
            var subtitle: String? = null
            var time_public: String? = null
            var title: String? = null
            var trace: String? = null
            var type: Int = 0
            var url: String? = null
            var version: Int = 0
            var volume: VolumeBean? = null
            var singer: List<SingerBean>? = null

            class ActionBean {
                /**
                 * alert : 100002
                 * icons : 8060
                 * msgdown : 0
                 * msgfav : 0
                 * msgid : 14
                 * msgpay : 6
                 * msgshare : 0
                 * switch : 17413891
                 */

                var alert: Int = 0
                var icons: Int = 0
                var msgdown: Int = 0
                var msgfav: Int = 0
                var msgid: Int = 0
                var msgpay: Int = 0
                var msgshare: Int = 0
                @SerializedName("switch")
                var switchX: Int = 0
            }

            class AlbumBean {
                /**
                 * id : 3889202
                 * mid : 001AiGTr4ZG1OK
                 * name : 隐隐作秀
                 * subtitle :
                 * time_public : 2018-01-25
                 * title : 隐隐作秀
                 */

                var id: Int = 0
                var mid: String? = null
                var name: String? = null
                var subtitle: String? = null
                var time_public: String? = null
                var title: String? = null
            }

            class FileBean {
                /**
                 * media_mid : 003Tz8UT3hwEfi
                 * size_128mp3 : 3051300
                 * size_192aac : 4612917
                 * size_192ogg : 4132519
                 * size_24aac : 593615
                 * size_320mp3 : 7627953
                 * size_48aac : 1164195
                 * size_96aac : 2328938
                 * size_ape : 19288519
                 * size_dts : 0
                 * size_flac : 19236194
                 * size_try : 0
                 * try_begin : 0
                 * try_end : 0
                 */

                var media_mid: String? = null
                var size_128mp3: Int = 0
                var size_192aac: Int = 0
                var size_192ogg: Int = 0
                var size_24aac: Int = 0
                var size_320mp3: Int = 0
                var size_48aac: Int = 0
                var size_96aac: Int = 0
                var size_ape: Int = 0
                var size_dts: Int = 0
                var size_flac: Int = 0
                var size_try: Int = 0
                var try_begin: Int = 0
                var try_end: Int = 0
            }

            class KsongBean {
                /**
                 * id : 3262715
                 * mid : 001YIgul246MJD
                 */

                var id: Int = 0
                var mid: String? = null
            }

            class MvBean {
                /**
                 * id : 0
                 * name :
                 * title :
                 * vid :
                 */

                var id: Int = 0
                var name: String? = null
                var title: String? = null
                var vid: String? = null
            }

            class PayBean {
                /**
                 * pay_down : 1
                 * pay_month : 1
                 * pay_play : 0
                 * pay_status : 0
                 * price_album : 0
                 * price_track : 200
                 * time_free : 0
                 */

                var pay_down: Int = 0
                var pay_month: Int = 0
                var pay_play: Int = 0
                var pay_status: Int = 0
                var price_album: Int = 0
                var price_track: Int = 0
                var time_free: Int = 0
            }

            class VolumeBean {
                /**
                 * gain : -5.336
                 * lra : 11.996
                 * peak : 0.956
                 */

                var gain: Double = 0.toDouble()
                var lra: Double = 0.toDouble()
                var peak: Double = 0.toDouble()
            }

            class SingerBean {
                /**
                 * id : 199515
                 * mid : 0003ZpE43ypssl
                 * name : 张碧晨
                 * title : 张碧晨
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

        class TypeInfoBean {
            /**
             * id : 1
             * report : 10_0_0_1_10001_1
             * title : 内地
             */

            var id: Int = 0
            var report: String? = null
            var title: String? = null
        }
    }
}