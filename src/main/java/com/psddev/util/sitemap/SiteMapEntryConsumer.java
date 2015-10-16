package com.psddev.util.sitemap;

import com.psddev.dari.util.AsyncConsumer;
import com.psddev.dari.util.AsyncQueue;
import com.psddev.dari.util.ObjectUtils;

import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

final class SiteMapEntryConsumer extends AsyncConsumer<SiteMapEntry> {

    private static final String EXECUTOR_NAME = "Site Map Executor";

    private int maxEntriesCount;
    private int maxEntriesFileSize;
    private String siteUrl;
    private Collection<String> crossSiteUrls;
    private List<SiteMap> siteMaps = new ArrayList<>();
    private CountDownLatch latch;

    private int index = 0;
    private int entriesCount = 0;
    private int entriesFileSize = 0;
    private boolean hasImages = false;
    private boolean hasVideos = false;
    private boolean hasNews = false;
    private List<Object> urls = new ArrayList<>();

    SiteMapEntryConsumer(AsyncQueue<SiteMapEntry> entriesQueue,
                                 Integer maxEntriesCount,
                                 Integer maxEntriesFileSize,
                                 String siteUrl,
                                 Collection<String> crossSiteUrls,
                                 CountDownLatch latch) {

        super(EXECUTOR_NAME, entriesQueue);
        this.maxEntriesCount = maxEntriesCount;
        this.maxEntriesFileSize = maxEntriesFileSize;
        this.siteUrl = siteUrl;
        this.crossSiteUrls = crossSiteUrls;
        this.latch = latch;
    }

    public List<SiteMap> getSiteMaps() {
        return siteMaps;
    }

    public Collection<String> getCrossSiteUrls() {
        return crossSiteUrls;
    }

    @Override
    protected void consume(SiteMapEntry siteMapEntry) throws Exception {

        Map<String, Object> entryJsonObject = SiteMapUtils.toXmlJsonMap(siteMapEntry);

        boolean limitReached = false;
        entriesCount++;
        entriesFileSize += SiteMapUtils.getXmlLengthForJsonMap(ImmutableMap.of("url", entryJsonObject));

        //check to see if this entry will fit in current sitemap
        //if so, update siteMap data (images, videos, news) and add to url list
        if (entriesCount <= maxEntriesCount && entriesFileSize <= maxEntriesFileSize) {

            if (!hasImages && !ObjectUtils.isBlank(siteMapEntry.getImages())) {
                hasImages = true;
            }
            if (!hasVideos && !ObjectUtils.isBlank(siteMapEntry.getVideos())) {
                hasVideos = true;
            }
            if (!hasNews && !ObjectUtils.isBlank(siteMapEntry.getNews())) {
                hasNews = true;
            }

            urls.add(entryJsonObject);

            // if not, this item needs to begin a new sitemap
        } else {

            limitReached = true;

            // If the queue is closed, go ahead and add ONE more item
            if (getInput().isClosed()) {
                urls.add(entryJsonObject);

                // else re-add item to queue
            } else {
                getInput().add(siteMapEntry);
            }
        }

        // create the current sitemap if the size limit has been reached
        if (limitReached) {
            createCurrentSiteMap();
        }
    }

    @Override
    protected void finished() {

        // Create and save the last sitemap;
        if (urls.size() > 0) {
            createCurrentSiteMap();
        }

        latch.countDown();
    }

    private void createCurrentSiteMap() {

        Map<String, Object> siteMapRootMap = new LinkedHashMap<>();
        Map<String, Object> siteMapMap = new LinkedHashMap<>();

        siteMapMap.put("@" + Constants.SITE_MAP_XMLNS, Constants.SITE_MAP_XMLNS_URL);
        siteMapMap.put("@" + Constants.SITE_MAP_XSI, Constants.SITE_MAP_XSI_URL);
        siteMapMap.put("@" + Constants.SITE_MAP_XSI_LOC, Constants.SITE_MAP_XSI_LOC_URL);
        if (hasImages) {
            siteMapMap.put("@" + Constants.SITE_MAP_XMLNS_IMAGE, Constants.SITE_MAP_XMLNS_IMAGE_URL);
        }
        if (hasVideos) {
            siteMapMap.put("@" + Constants.SITE_MAP_XMLNS_VIDEO, Constants.SITE_MAP_XMLNS_VIDEO_URL);
        }
        if (hasNews) {
            siteMapMap.put("@" + Constants.SITE_MAP_XMLNS_NEWS, Constants.SITE_MAP_XMLNS_NEWS_URL);
        }

        siteMapMap.put("url", urls);

        siteMapRootMap.put("urlset", siteMapMap);

        SiteMap siteMap = new SiteMap();

        siteMap.setSiteMapUrl(siteUrl);
        if (crossSiteUrls != null) {
            siteMap.setCrossSiteUrls(new HashSet<>(crossSiteUrls));
        }
        siteMap.setSiteMapIndex(++index);
        siteMap.setLastModified(new Date());

        siteMap.setXml(SiteMapUtils.getSiteMapXml(siteMapRootMap, false));

        //clear that sitemap's XML data and save add it to the list of sitemaps for the index
        siteMap.clearTransientXml();

        siteMaps.add(siteMap);

        // cleanup / reset

        hasImages = false;
        hasVideos = false;
        hasNews = false;

        urls = new ArrayList<>();

        entriesCount = 0;
        entriesFileSize = 0;
    }
}
