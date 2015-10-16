package com.psddev.util.sitemap;

import java.util.Date;
import java.util.List;

public class SiteMapEntry implements ComplexElement {

    @XmlField(path = "loc", required = true)
    private String permalink;

    @XmlField(path = "lastmod")
    private Date updateDate;

    @XmlField(path = "changefreq")
    private SiteMapChangeFrequency changeFrequency;

    @XmlField(path = "priority")
    private Double priority;

    @XmlField(path = "image:image")
    private List<SiteMapImage> images;

    @XmlField(path = "video:video")
    private List<SiteMapVideo> videos;

    @XmlField(path = "news:news")
    private List<SiteMapNews> news;

    public SiteMapEntry() {
    }

    String getPermalink() {
        return permalink;
    }

    /** The FULL permalink for this content, including the protocol, domain,
     *  and optionally the port. Required. */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    SiteMapChangeFrequency getChangeFrequency() {
        return changeFrequency;
    }

    public void setChangeFrequency(SiteMapChangeFrequency changeFrequency) {
        this.changeFrequency = changeFrequency;
    }

    Double getPriority() {
        return priority;
    }

    public void setPriority(Double priority) {
        this.priority = priority;
    }

    List<SiteMapImage> getImages() {
        return images;
    }

    public void setImages(List<SiteMapImage> images) {
        this.images = images;
    }

    List<SiteMapVideo> getVideos() {
        return videos;
    }

    public void setVideos(List<SiteMapVideo> videos) {
        this.videos = videos;
    }

    List<SiteMapNews> getNews() {
        return news;
    }

    public void setNews(List<SiteMapNews> news) {
        this.news = news;
    }
}
