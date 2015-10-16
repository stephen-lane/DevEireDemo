package com.psddev.util.sitemap;

public class SiteMapImage implements ComplexElement {

    @XmlField(path = "image:loc", required = true)
    private String url;

    @XmlField(path = "image:title")
    private String title;

    @XmlField(path = "image:caption")
    private String caption;

    @XmlField(path = "image:geo_location")
    private String geoLocation;

    @XmlField(path = "image:license")
    private String license;

    public SiteMapImage() {
    }

    /** URL to the image. Required. */
    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
