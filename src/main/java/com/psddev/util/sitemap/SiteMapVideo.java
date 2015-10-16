package com.psddev.util.sitemap;

import java.util.Date;
import java.util.List;

public class SiteMapVideo implements ComplexElement {

    @XmlField(path = "video:thumbnail_loc")
    private String thumbnailLoc;

    @XmlField(path = "video:title")
    private String title;

    @XmlField(path = "video:description")
    private String description;

    @XmlField(path = "video:content_loc")
    private String contentLoc;

    @XmlField(path = "video:player_loc/$")
    private String playerLoc;

    @XmlField(path = "video:player_loc/@allow_embed")
    private Boolean playerLocAllowEmbed;

    @XmlField(path = "video:player_loc/@autoplay")
    private String playerLocAutoplay;

    @XmlField(path = "video:duration")
    private Long duration;

    @XmlField(path = "video:expiration_date", transformer = LongDateFieldTransformer.class)
    private Date expirationDate;

    @XmlField(path = "video:rating")
    private Double rating;

    @XmlField(path = "video:view_count")
    private Long viewCount;

    @XmlField(path = "video:publication_date", transformer = LongDateFieldTransformer.class)
    private Date publicationDate;

    @XmlField(path = "video:tag")
    private List<String> tags;

    @XmlField(path = "video:category")
    private String category;

    @XmlField(path = "video:family_friendly")
    private Boolean familyFriendly;

    @XmlField(path = "video:restriction/$")
    private String restriction;

    @XmlField(path = "video:restriction/@relationship")
    private SiteMapRelationship restrictionRelationship;

    @XmlField(path = "video:gallery_loc/$")
    private String galleryLoc;

    @XmlField(path = "video:gallery_loc/@title")
    private String galleryLocTitle;

    @XmlField(path = "video:price/$")
    private Double price;

    @XmlField(path = "video:price/@currency")
    private String priceCurrency;

    @XmlField(path = "video:requires_subscription")
    private Boolean requiresSubscription;

    @XmlField(path = "video:uploader/$")
    private String uploader;

    @XmlField(path = "video:uploader/@info")
    private String uploaderInfo;

    @XmlField(path = "video:live")
    private Boolean live;

    public void setThumbnailLoc(String thumbnailLoc) {
        this.thumbnailLoc = thumbnailLoc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContentLoc(String contentLoc) {
        this.contentLoc = contentLoc;
    }

    public void setPlayerLoc(String playerLoc) {
        this.playerLoc = playerLoc;
    }

    public void setPlayerLocAllowEmbed(Boolean playerLocAllowEmbed) {
        this.playerLocAllowEmbed = playerLocAllowEmbed;
    }

    public void setPlayerLocAutoplay(String playerLocAutoplay) {
        this.playerLocAutoplay = playerLocAutoplay;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setFamilyFriendly(Boolean familyFriendly) {
        this.familyFriendly = familyFriendly;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public void setRestrictionRelationship(SiteMapRelationship restrictionRelationship) {
        this.restrictionRelationship = restrictionRelationship;
    }

    public void setGalleryLoc(String galleryLoc) {
        this.galleryLoc = galleryLoc;
    }

    public void setGalleryLocTitle(String galleryLocTitle) {
        this.galleryLocTitle = galleryLocTitle;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }

    public void setRequiresSubscription(Boolean requiresSubscription) {
        this.requiresSubscription = requiresSubscription;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public void setUploaderInfo(String uploaderInfo) {
        this.uploaderInfo = uploaderInfo;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }
}
