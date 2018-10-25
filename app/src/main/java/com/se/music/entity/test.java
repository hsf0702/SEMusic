package com.se.music.entity;

import java.util.List;

/**
 * Author: gaojin
 * Time: 2018/10/23 下午10:15
 */

public class test {

    /**
     * query : 爱久见人心
     * pages : {"total":12,"rn_num":10}
     * lrcys_list : [{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/14950888/14950888.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/6269843bf5ee901ab5827646814fd6f5/267101782/267101782.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20221037/20221037.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20215777/20215777.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20525288/20525288.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20532103/20532103.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20675867/20675867.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20654568/20654568.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/14960340/14960340.lrc"},{"lrclink":"http://qukufile2.qianqian.com/data2/lrc/20534911/20534911.lrc"}]
     */

    public String query;
    public PagesBean pages;
    public List<LrcysListBean> lrcys_list;

    public static class PagesBean {
        /**
         * total : 12
         * rn_num : 10
         */

        public int total;
        public int rn_num;
    }

    public static class LrcysListBean {
        /**
         * lrclink : http://qukufile2.qianqian.com/data2/lrc/14950888/14950888.lrc
         */

        public String lrclink;
    }
}
