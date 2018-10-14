package com.se.music;

/**
 * Author: gaojin
 * Time: 2018/10/14 下午9:06
 */

public class Test {

    public TrackmatchesBean trackmatches;

    public static class TrackmatchesBean {

        public java.util.List<TrackBean> track;

        public static class TrackBean {

            public String name;
            public String artist;
            public String url;
            public String streamable;
            public String listeners;
            public String mbid;
            public java.util.List<ImageBean> image;

            public static class ImageBean {
            }
        }
    }
}
