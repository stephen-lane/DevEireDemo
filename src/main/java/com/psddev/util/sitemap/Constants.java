package com.psddev.util.sitemap;

final class Constants {

    private Constants() {
    }

    static final String SITE_MAP_XMLNS =                "xmlns";
    static final String SITE_MAP_XMLNS_URL =            "http://www.sitemaps.org/schemas/sitemap/0.9";

    static final String SITE_MAP_XSI =                  "xmlns:xsi";
    static final String SITE_MAP_XSI_URL =              "http://www.w3.org/2001/XMLSchema-instance";

    static final String SITE_MAP_XSI_LOC =              "xsi:schemaLocation";
    static final String SITE_MAP_XSI_LOC_URL =          "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd";
    static final String SITE_MAP_INDEX_XSI_LOC_URL =    "http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemapindex.xsd";

    static final String SITE_MAP_XMLNS_IMAGE =          "xmlns:image";
    static final String SITE_MAP_XMLNS_IMAGE_URL =      "http://www.google.com/schemas/sitemap-image/1.1";

    static final String SITE_MAP_XMLNS_VIDEO =          "xmlns:video";
    static final String SITE_MAP_XMLNS_VIDEO_URL =      "http://www.google.com/schemas/sitemap-video/1.1";

    static final String SITE_MAP_XMLNS_NEWS =           "xmlns:news";
    static final String SITE_MAP_XMLNS_NEWS_URL =       "http://www.google.com/schemas/sitemap-news/0.9";

    static final String SITE_MAP_INDEX_ELEMENT = "sitemapindex";
    static final String SITE_MAP_INDEX_SITEMAP_ELEMENT = "sitemap";
    static final String SITE_MAP_INDEX_LOC_ELEMENT = "loc";
    static final String SITE_MAP_INDEX_LASTMOD_ELEMENT = "lastmod";
}
