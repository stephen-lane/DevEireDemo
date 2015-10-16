package com.psddev.util.sitemap;

import com.psddev.dari.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * <a href="https://support.google.com/news/publisher/answer/74288?hl=en">
 * Google News Sitemap
 * </a>
 */
public class SiteMapNews implements ComplexElement {

    @XmlField(path = "news:publication/news:name")
    private String name;

    @XmlField(path = "news:publication/news:language")
    private String language;

    @XmlField(path = "news:access")
    private String access;

    @XmlField(path = "news:genres", transformer = CsvStringListFieldTransformer.class)
    private String genres;

    @XmlField(path = "news:publication_date")
    private Date publicationDate;

    @XmlField(path = "news:title")
    private String title;

    @XmlField(path = "news:geo_locations")
    private String geoLocations;

    @XmlField(path = "news:keywords", transformer = CsvStringListFieldTransformer.class)
    private String keywords;

    @XmlField(path = "news:stock_tickers")
    private String stockTickers;

    public SiteMapNews() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGenres(List<String> genres) {
        this.genres = StringUtils.join(genres, ", ");
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGeoLocations(String geoLocations) {
        this.geoLocations = geoLocations;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = StringUtils.join(keywords, ", ");
    }

    public void setStockTickers(List<String> stockTickers) {
        this.stockTickers = StringUtils.join(stockTickers, ", ");
    }
}
