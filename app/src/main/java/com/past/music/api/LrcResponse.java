package com.past.music.api;

import com.past.music.api.base.BaseResponseQQApi;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/6/5 13:54
 * 描述：
 * 备注：
 * =======================================================
 */
public class LrcResponse extends BaseResponseQQApi {

    private ShowapiResBodyBean showapi_res_body;

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        private int ret_code;
        private String lyric;
        private String lyric_txt;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public String getLyric() {
            return lyric;
        }

        public void setLyric(String lyric) {
            this.lyric = lyric;
        }

        public String getLyric_txt() {
            return lyric_txt;
        }

        public void setLyric_txt(String lyric_txt) {
            this.lyric_txt = lyric_txt;
        }
    }
}
