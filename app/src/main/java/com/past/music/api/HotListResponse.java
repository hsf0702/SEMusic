package com.past.music.api;

import com.past.music.api.base.BaseResponseQQApi;

import java.util.List;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 13:57
 * 描述：
 * 备注：
 * =======================================================
 */
public class HotListResponse extends BaseResponseQQApi {
    private ShowapiResBodyBean showapi_res_body;

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    public static class ShowapiResBodyBean {
        private int ret_code;
        private PagebeanBean pagebean;

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public PagebeanBean getPagebean() {
            return pagebean;
        }

        public void setPagebean(PagebeanBean pagebean) {
            this.pagebean = pagebean;
        }

        public static class PagebeanBean {
            private int total_song_num;
            private int ret_code;
            private String update_time;
            private int color;
            private int cur_song_num;
            private int comment_num;
            private int code;
            private int currentPage;
            private int song_begin;
            private int totalpage;
            private List<SonglistBean> songlist;

            public int getTotal_song_num() {
                return total_song_num;
            }

            public void setTotal_song_num(int total_song_num) {
                this.total_song_num = total_song_num;
            }

            public int getRet_code() {
                return ret_code;
            }

            public void setRet_code(int ret_code) {
                this.ret_code = ret_code;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public int getColor() {
                return color;
            }

            public void setColor(int color) {
                this.color = color;
            }

            public int getCur_song_num() {
                return cur_song_num;
            }

            public void setCur_song_num(int cur_song_num) {
                this.cur_song_num = cur_song_num;
            }

            public int getComment_num() {
                return comment_num;
            }

            public void setComment_num(int comment_num) {
                this.comment_num = comment_num;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getSong_begin() {
                return song_begin;
            }

            public void setSong_begin(int song_begin) {
                this.song_begin = song_begin;
            }

            public int getTotalpage() {
                return totalpage;
            }

            public void setTotalpage(int totalpage) {
                this.totalpage = totalpage;
            }

            public List<SonglistBean> getSonglist() {
                return songlist;
            }

            public void setSonglist(List<SonglistBean> songlist) {
                this.songlist = songlist;
            }
        }
    }
}
