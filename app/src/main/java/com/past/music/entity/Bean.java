package com.past.music.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by gaojin on 2018/1/3.
 */

public class Bean {

    public NewAlbumBean new_album;
    public NewSongBean new_song;
    public int code;
    public long ts;

    public static class NewAlbumBean {

        public DataBean data;
        public int code;
        public static class DataBean {

            public int size;
            public int type;
            public List<AlbumListBean> album_list;

            public static class AlbumListBean {

                public AlbumBean album;
                public List<AuthorBean> author;

                public static class AlbumBean {

                    public int id;
                    public String mid;
                    public String name;
                    public String subtitle;
                    public String time_public;
                    public String title;
                }

                public static class AuthorBean {

                    public int id;
                    public String mid;
                    public String name;
                    public String title;
                    public int type;
                    public int uin;
                }
            }
        }
    }

    public static class NewSongBean {

        public DataBeanX data;
        public int code;

        public static class DataBeanX {

            public int size;
            public int type;
            public List<SongListBean> song_list;

            public static class SongListBean {

                public ActionBean action;
                public AlbumBeanX album;
                public int bpm;
                public int data_type;
                public FileBean file;
                public int fnote;
                public int genre;
                public int id;
                public int index_album;
                public int index_cd;
                public int interval;
                public int isonly;
                public KsongBean ksong;
                public String label;
                public int language;
                public String mid;
                public int modify_stamp;
                public MvBean mv;
                public String name;
                public PayBean pay;
                public int status;
                public String subtitle;
                public String time_public;
                public String title;
                public String trace;
                public int type;
                public String url;
                public int version;
                public VolumeBean volume;
                public List<SingerBean> singer;

                public static class ActionBean {

                    public int alert;
                    public int icons;
                    public int msgdown;
                    public int msgfav;
                    public int msgid;
                    public int msgpay;
                    public int msgshare;
                    @SerializedName("switch")
                    public int switchX;
                }

                public static class AlbumBeanX {

                    public int id;
                    public String mid;
                    public String name;
                    public String subtitle;
                    public String time_public;
                    public String title;
                }

                public static class FileBean {

                    public String media_mid;
                    public int size_128mp3;
                    public int size_192aac;
                    public int size_192ogg;
                    public int size_24aac;
                    public int size_320mp3;
                    public int size_48aac;
                    public int size_96aac;
                    public int size_ape;
                    public int size_dts;
                    public int size_flac;
                    public int size_try;
                    public int try_begin;
                    public int try_end;
                }

                public static class KsongBean {
                    public int id;
                    public String mid;
                }

                public static class MvBean {
                    public int id;
                    public String name;
                    public String title;
                    public String vid;
                }

                public static class PayBean {
                    public int pay_down;
                    public int pay_month;
                    public int pay_play;
                    public int pay_status;
                    public int price_album;
                    public int price_track;
                    public int time_free;
                }

                public static class VolumeBean {
                    public double gain;
                    public double lra;
                    public int peak;
                }

                public static class SingerBean {
                    public int id;
                    public String mid;
                    public String name;
                    public String title;
                    public int type;
                    public int uin;
                }
            }
        }
    }
}
