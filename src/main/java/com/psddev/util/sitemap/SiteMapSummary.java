package com.psddev.util.sitemap;

import com.psddev.dari.db.Query;
import com.psddev.dari.util.Lazy;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PeriodicValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Class to get site map information without the large cost of reading the
 * compressed xml data.
 */
public final class SiteMapSummary {

    private String siteMapUrl;

    private String siteMapBaseUrl;

    private Integer siteMapIndex;

    private Date lastModified;

    private String siteMapId;

    private SiteMapSummary(SiteMap siteMap) {
        this.siteMapUrl = siteMap.getLabel();
        this.siteMapBaseUrl = siteMap.getSiteMapUrl();
        this.siteMapIndex = siteMap.getSiteMapIndex();
        this.lastModified = siteMap.getLastModified();
        this.siteMapId = siteMap.getId().toString();
    }

    public String getSiteMapUrl() {
        return siteMapUrl;
    }

    public String getSiteMapBaseUrl() {
        return siteMapBaseUrl;
    }

    public Integer getSiteMapIndex() {
        return siteMapIndex;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getSiteMapId() {
        return siteMapId;
    }

    public static List<SiteMapSummary> getSummaries() {

        PeriodicValue<List<SiteMapSummary>> periodicValue = SUMMARIES.get();
        if (periodicValue != null) {
            return periodicValue.get();
        }

        return null;
    }

    private static final Lazy<PeriodicValue<List<SiteMapSummary>>> SUMMARIES = new Lazy<PeriodicValue<List<SiteMapSummary>>>() {

        @Override
        protected PeriodicValue<List<SiteMapSummary>> create() throws Exception {
            return new PeriodicValue<List<SiteMapSummary>>(0, 60 * 60) { // update every hour

                @Override
                protected List<SiteMapSummary> update() {

                    List<SiteMapSummary> summaries = new ArrayList<>();

                    for (SiteMap siteMapReference : Query.from(SiteMap.class).referenceOnly().iterable(0)) {

                        UUID id = siteMapReference.getId();

                        if (id != null) {
                            // Resolve only one at a time, since these can be very large.
                            SiteMap siteMap = Query.from(SiteMap.class).where("_id = ?", id).first();
                            if (siteMap != null) {
                                summaries.add(new SiteMapSummary(siteMap));
                            }
                        }

                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                    }

                    Collections.sort(summaries, (s1, s2) -> {

                        // Neither should ever be null, but just in case...
                        if (s1 == null) {
                            return s2 == null ? 0 : 1;
                        } else if (s2 == null) {
                            return -1;
                        }

                        // First compare base urls
                        int baseUrlComparison = ObjectUtils.compare(s1.getSiteMapBaseUrl(), s2.getSiteMapBaseUrl(), true);

                        // then compare sitemap indexes, null means index so it goes first.
                        return baseUrlComparison == 0
                                ? ObjectUtils.compare(s1.getSiteMapIndex(), s2.getSiteMapIndex(), false)
                                : baseUrlComparison;
                    });

                    return summaries;
                }
            };
        }
    };
}
