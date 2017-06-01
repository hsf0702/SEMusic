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

    private ArtistBeanX artist;

    public ArtistBeanX getArtist() {
        return artist;
    }

    public void setArtist(ArtistBeanX artist) {
        this.artist = artist;
    }

    public static class ArtistBeanX {

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
