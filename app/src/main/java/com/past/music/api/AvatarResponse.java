package com.past.music.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * =======================================================
 * 作者：GaoJin
 * 日期：2017/4/23 13:56
 * 描述：
 * 备注：
 * =======================================================
 */

public class AvatarResponse {
    /**
     * artist : {"name":"Avril Lavigne","mbid":"0103c1cc-4a09-4a5d-a344-56ad99a77193","url":"https://www.last.fm/music/Avril+Lavigne","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/c1ab57067340d25c6a3eee9b123ed265.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/c1ab57067340d25c6a3eee9b123ed265.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/c1ab57067340d25c6a3eee9b123ed265.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/c1ab57067340d25c6a3eee9b123ed265.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/c1ab57067340d25c6a3eee9b123ed265.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/c1ab57067340d25c6a3eee9b123ed265.png","size":""}],"streamable":"0","ontour":"0","stats":{"listeners":"2473374","playcount":"105306470"},"similar":{"artist":[{"name":"Hilary Duff","url":"https://www.last.fm/music/Hilary+Duff","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/76b974982c1fe8ec757b1f2e91d0bc75.png","size":""}]},{"name":"Ashley Tisdale","url":"https://www.last.fm/music/Ashley+Tisdale","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/45045b3575424417c4239d1fe02f5a29.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/45045b3575424417c4239d1fe02f5a29.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/45045b3575424417c4239d1fe02f5a29.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/45045b3575424417c4239d1fe02f5a29.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/45045b3575424417c4239d1fe02f5a29.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/45045b3575424417c4239d1fe02f5a29.png","size":""}]},{"name":"Demi Lovato","url":"https://www.last.fm/music/Demi+Lovato","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/b874a9ac728300a8bfa3f3707a685a45.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/b874a9ac728300a8bfa3f3707a685a45.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/b874a9ac728300a8bfa3f3707a685a45.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/b874a9ac728300a8bfa3f3707a685a45.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/b874a9ac728300a8bfa3f3707a685a45.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/b874a9ac728300a8bfa3f3707a685a45.png","size":""}]},{"name":"Kelly Clarkson","url":"https://www.last.fm/music/Kelly+Clarkson","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/24b0baf777be4d14cd382606a98cc490.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/24b0baf777be4d14cd382606a98cc490.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/24b0baf777be4d14cd382606a98cc490.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/24b0baf777be4d14cd382606a98cc490.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/24b0baf777be4d14cd382606a98cc490.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/24b0baf777be4d14cd382606a98cc490.png","size":""}]},{"name":"Taylor Swift","url":"https://www.last.fm/music/Taylor+Swift","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/93f95ff8e6d2b836215c574ce142483a.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/93f95ff8e6d2b836215c574ce142483a.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/93f95ff8e6d2b836215c574ce142483a.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/93f95ff8e6d2b836215c574ce142483a.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/93f95ff8e6d2b836215c574ce142483a.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/93f95ff8e6d2b836215c574ce142483a.png","size":""}]}]},"tags":{"tag":[{"name":"pop","url":"https://www.last.fm/tag/pop"},{"name":"pop rock","url":"https://www.last.fm/tag/pop+rock"},{"name":"rock","url":"https://www.last.fm/tag/rock"},{"name":"female vocalists","url":"https://www.last.fm/tag/female+vocalists"},{"name":"Canadian","url":"https://www.last.fm/tag/Canadian"}]},"bio":{"links":{"link":{"#text":"","rel":"original","href":"https://last.fm/music/Avril+Lavigne/+wiki"}},"published":"10 Feb 2006, 19:36","summary":"Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut <a href=\"https://www.last.fm/music/Avril+Lavigne\">Read more on Last.fm<\/a>","content":"Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut, Lavigne's honors notably are 8 Grammy award nominations as well as 8 Juno awards from 24 nominations. Lavigne is one of the top-selling artists releasing albums in the US, with over 11 million copies certified by the RIAA. To date, Lavigne has had 8 Top 20 Billboard Hot 100 hits, including the best-selling single in 2007, Girlfriend, which was released as the lead single from her third studio album, The Best Damn Thing. Lavigne has released 5 Top 5 Billboard 200 studio albums: Let Go (2002), Under My Skin (2004), The Best Damn Thing (2007), Goodbye Lullaby (2011), and Avril Lavigne (2013). As of April 2013, Lavigne has sold more than 30 million albums and 50 million singles worldwide.\nPersonal Life - in July 2013, Lavigne married Nickelback and fellow Canadian, Chad Kroeger. The couple met in July 2012 while working on Lavigne's single \"Here's To Never Growing Up\" and Kroeger proposed 1 month later. On Sept 3, 2015, after 2 years of marriage, the couple announced that they were separating. <a href=\"https://www.last.fm/music/Avril+Lavigne\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}}
     */

    private ArtistBeanX artist;

    public ArtistBeanX getArtist() {
        return artist;
    }

    public void setArtist(ArtistBeanX artist) {
        this.artist = artist;
    }

    public static class ArtistBeanX {
        /**
         * name : Avril Lavigne
         * mbid : 0103c1cc-4a09-4a5d-a344-56ad99a77193
         * url : https://www.last.fm/music/Avril+Lavigne
         * image : [{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/c1ab57067340d25c6a3eee9b123ed265.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/c1ab57067340d25c6a3eee9b123ed265.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/c1ab57067340d25c6a3eee9b123ed265.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/c1ab57067340d25c6a3eee9b123ed265.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/c1ab57067340d25c6a3eee9b123ed265.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/c1ab57067340d25c6a3eee9b123ed265.png","size":""}]
         * streamable : 0
         * ontour : 0
         * stats : {"listeners":"2473374","playcount":"105306470"}
         * similar : {"artist":[{"name":"Hilary Duff","url":"https://www.last.fm/music/Hilary+Duff","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/76b974982c1fe8ec757b1f2e91d0bc75.png","size":""}]},{"name":"Ashley Tisdale","url":"https://www.last.fm/music/Ashley+Tisdale","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/45045b3575424417c4239d1fe02f5a29.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/45045b3575424417c4239d1fe02f5a29.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/45045b3575424417c4239d1fe02f5a29.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/45045b3575424417c4239d1fe02f5a29.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/45045b3575424417c4239d1fe02f5a29.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/45045b3575424417c4239d1fe02f5a29.png","size":""}]},{"name":"Demi Lovato","url":"https://www.last.fm/music/Demi+Lovato","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/b874a9ac728300a8bfa3f3707a685a45.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/b874a9ac728300a8bfa3f3707a685a45.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/b874a9ac728300a8bfa3f3707a685a45.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/b874a9ac728300a8bfa3f3707a685a45.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/b874a9ac728300a8bfa3f3707a685a45.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/b874a9ac728300a8bfa3f3707a685a45.png","size":""}]},{"name":"Kelly Clarkson","url":"https://www.last.fm/music/Kelly+Clarkson","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/24b0baf777be4d14cd382606a98cc490.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/24b0baf777be4d14cd382606a98cc490.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/24b0baf777be4d14cd382606a98cc490.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/24b0baf777be4d14cd382606a98cc490.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/24b0baf777be4d14cd382606a98cc490.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/24b0baf777be4d14cd382606a98cc490.png","size":""}]},{"name":"Taylor Swift","url":"https://www.last.fm/music/Taylor+Swift","image":[{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/93f95ff8e6d2b836215c574ce142483a.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/93f95ff8e6d2b836215c574ce142483a.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/93f95ff8e6d2b836215c574ce142483a.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/93f95ff8e6d2b836215c574ce142483a.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/93f95ff8e6d2b836215c574ce142483a.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/93f95ff8e6d2b836215c574ce142483a.png","size":""}]}]}
         * tags : {"tag":[{"name":"pop","url":"https://www.last.fm/tag/pop"},{"name":"pop rock","url":"https://www.last.fm/tag/pop+rock"},{"name":"rock","url":"https://www.last.fm/tag/rock"},{"name":"female vocalists","url":"https://www.last.fm/tag/female+vocalists"},{"name":"Canadian","url":"https://www.last.fm/tag/Canadian"}]}
         * bio : {"links":{"link":{"#text":"","rel":"original","href":"https://last.fm/music/Avril+Lavigne/+wiki"}},"published":"10 Feb 2006, 19:36","summary":"Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut <a href=\"https://www.last.fm/music/Avril+Lavigne\">Read more on Last.fm<\/a>","content":"Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut, Lavigne's honors notably are 8 Grammy award nominations as well as 8 Juno awards from 24 nominations. Lavigne is one of the top-selling artists releasing albums in the US, with over 11 million copies certified by the RIAA. To date, Lavigne has had 8 Top 20 Billboard Hot 100 hits, including the best-selling single in 2007, Girlfriend, which was released as the lead single from her third studio album, The Best Damn Thing. Lavigne has released 5 Top 5 Billboard 200 studio albums: Let Go (2002), Under My Skin (2004), The Best Damn Thing (2007), Goodbye Lullaby (2011), and Avril Lavigne (2013). As of April 2013, Lavigne has sold more than 30 million albums and 50 million singles worldwide.\nPersonal Life - in July 2013, Lavigne married Nickelback and fellow Canadian, Chad Kroeger. The couple met in July 2012 while working on Lavigne's single \"Here's To Never Growing Up\" and Kroeger proposed 1 month later. On Sept 3, 2015, after 2 years of marriage, the couple announced that they were separating. <a href=\"https://www.last.fm/music/Avril+Lavigne\">Read more on Last.fm<\/a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."}
         */

        private String name;
        private String mbid;
        private String url;
        private String streamable;
        private String ontour;
        private StatsBean stats;
        private SimilarBean similar;
        private TagsBean tags;
        private BioBean bio;
        private List<ImageBeanX> image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMbid() {
            return mbid;
        }

        public void setMbid(String mbid) {
            this.mbid = mbid;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStreamable() {
            return streamable;
        }

        public void setStreamable(String streamable) {
            this.streamable = streamable;
        }

        public String getOntour() {
            return ontour;
        }

        public void setOntour(String ontour) {
            this.ontour = ontour;
        }

        public StatsBean getStats() {
            return stats;
        }

        public void setStats(StatsBean stats) {
            this.stats = stats;
        }

        public SimilarBean getSimilar() {
            return similar;
        }

        public void setSimilar(SimilarBean similar) {
            this.similar = similar;
        }

        public TagsBean getTags() {
            return tags;
        }

        public void setTags(TagsBean tags) {
            this.tags = tags;
        }

        public BioBean getBio() {
            return bio;
        }

        public void setBio(BioBean bio) {
            this.bio = bio;
        }

        public List<ImageBeanX> getImage() {
            return image;
        }

        public void setImage(List<ImageBeanX> image) {
            this.image = image;
        }

        public static class StatsBean {
            /**
             * listeners : 2473374
             * playcount : 105306470
             */

            private String listeners;
            private String playcount;

            public String getListeners() {
                return listeners;
            }

            public void setListeners(String listeners) {
                this.listeners = listeners;
            }

            public String getPlaycount() {
                return playcount;
            }

            public void setPlaycount(String playcount) {
                this.playcount = playcount;
            }
        }

        public static class SimilarBean {
            private List<ArtistBean> artist;

            public List<ArtistBean> getArtist() {
                return artist;
            }

            public void setArtist(List<ArtistBean> artist) {
                this.artist = artist;
            }

            public static class ArtistBean {
                /**
                 * name : Hilary Duff
                 * url : https://www.last.fm/music/Hilary+Duff
                 * image : [{"#text":"https://lastfm-img2.akamaized.net/i/u/34s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"small"},{"#text":"https://lastfm-img2.akamaized.net/i/u/64s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"medium"},{"#text":"https://lastfm-img2.akamaized.net/i/u/174s/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"large"},{"#text":"https://lastfm-img2.akamaized.net/i/u/300x300/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"extralarge"},{"#text":"https://lastfm-img2.akamaized.net/i/u/76b974982c1fe8ec757b1f2e91d0bc75.png","size":"mega"},{"#text":"https://lastfm-img2.akamaized.net/i/u/arQ/76b974982c1fe8ec757b1f2e91d0bc75.png","size":""}]
                 */

                private String name;
                private String url;
                private List<ImageBean> image;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public List<ImageBean> getImage() {
                    return image;
                }

                public void setImage(List<ImageBean> image) {
                    this.image = image;
                }

                public static class ImageBean {
                    @SerializedName("#text")
                    private String _$Text216; // FIXME check this code
                    private String size;

                    public String get_$Text216() {
                        return _$Text216;
                    }

                    public void set_$Text216(String _$Text216) {
                        this._$Text216 = _$Text216;
                    }

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
                    }
                }
            }
        }

        public static class TagsBean {
            private List<TagBean> tag;

            public List<TagBean> getTag() {
                return tag;
            }

            public void setTag(List<TagBean> tag) {
                this.tag = tag;
            }

            public static class TagBean {
                /**
                 * name : pop
                 * url : https://www.last.fm/tag/pop
                 */

                private String name;
                private String url;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }

        public static class BioBean {
            /**
             * links : {"link":{"#text":"","rel":"original","href":"https://last.fm/music/Avril+Lavigne/+wiki"}}
             * published : 10 Feb 2006, 19:36
             * summary : Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut <a href="https://www.last.fm/music/Avril+Lavigne">Read more on Last.fm</a>
             * content : Avril Lavigne (born Avril Ramona Lavigne, September 27, 1984 in Belleville, Ontario, Canada), is a Juno award-winning and Grammy-award nominated singer, songwriter. In 2002, Lavigne reached mainstream success after her debut single, Complicated, and her debut album Let Go, gained lots of media attention and honourably earned her reputation of a 'skater punk' persona and 'pop punk princess' when she was only 17 years old. Since her professional debut, Lavigne's honors notably are 8 Grammy award nominations as well as 8 Juno awards from 24 nominations. Lavigne is one of the top-selling artists releasing albums in the US, with over 11 million copies certified by the RIAA. To date, Lavigne has had 8 Top 20 Billboard Hot 100 hits, including the best-selling single in 2007, Girlfriend, which was released as the lead single from her third studio album, The Best Damn Thing. Lavigne has released 5 Top 5 Billboard 200 studio albums: Let Go (2002), Under My Skin (2004), The Best Damn Thing (2007), Goodbye Lullaby (2011), and Avril Lavigne (2013). As of April 2013, Lavigne has sold more than 30 million albums and 50 million singles worldwide.
             * Personal Life - in July 2013, Lavigne married Nickelback and fellow Canadian, Chad Kroeger. The couple met in July 2012 while working on Lavigne's single "Here's To Never Growing Up" and Kroeger proposed 1 month later. On Sept 3, 2015, after 2 years of marriage, the couple announced that they were separating. <a href="https://www.last.fm/music/Avril+Lavigne">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply.
             */

            private LinksBean links;
            private String published;
            private String summary;
            private String content;

            public LinksBean getLinks() {
                return links;
            }

            public void setLinks(LinksBean links) {
                this.links = links;
            }

            public String getPublished() {
                return published;
            }

            public void setPublished(String published) {
                this.published = published;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public static class LinksBean {
                /**
                 * link : {"#text":"","rel":"original","href":"https://last.fm/music/Avril+Lavigne/+wiki"}
                 */

                private LinkBean link;

                public LinkBean getLink() {
                    return link;
                }

                public void setLink(LinkBean link) {
                    this.link = link;
                }

                public static class LinkBean {
                    @SerializedName("#text")
                    private String _$Text318; // FIXME check this code
                    private String rel;
                    private String href;

                    public String get_$Text318() {
                        return _$Text318;
                    }

                    public void set_$Text318(String _$Text318) {
                        this._$Text318 = _$Text318;
                    }

                    public String getRel() {
                        return rel;
                    }

                    public void setRel(String rel) {
                        this.rel = rel;
                    }

                    public String getHref() {
                        return href;
                    }

                    public void setHref(String href) {
                        this.href = href;
                    }
                }
            }
        }

        public static class ImageBeanX {
            @SerializedName("#text")
            private String _$Text112; // FIXME check this code
            private String size;

            public String get_$Text112() {
                return _$Text112;
            }

            public void set_$Text112(String _$Text112) {
                this._$Text112 = _$Text112;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }
        }
    }
}
