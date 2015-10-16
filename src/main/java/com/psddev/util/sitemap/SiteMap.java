package com.psddev.util.sitemap;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Record;

import com.google.common.io.BaseEncoding;
import com.psddev.util.CompressionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;

/**
 * Metadata around a site map.
 */
@ToolUi.Hidden
public class SiteMap extends Record {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteMap.class);

    @Indexed
    @ToolUi.ReadOnly
    private String siteMapUrl;

    @Indexed
    @ToolUi.ReadOnly
    private Integer siteMapIndex;

    @ToolUi.ReadOnly
    private Set<String> crossSiteUrls;

    @ToolUi.ReadOnly
    private List<SiteMap> siteMaps;

    @ToolUi.ReadOnly
    private Date lastModified;

    @ToolUi.Hidden
    private String data;

    @ToolUi.Hidden
    private boolean isCompressed;

    private transient String xml;

    public String getSiteMapUrl() {
        return siteMapUrl;
    }

    void setSiteMapUrl(String siteMapUrl) {
        this.siteMapUrl = siteMapUrl;
    }

    public Integer getSiteMapIndex() {
        return siteMapIndex;
    }

    void setSiteMapIndex(Integer siteMapIndex) {
        this.siteMapIndex = siteMapIndex;
    }

    /** Returns a set of all the cross site URLs associated with this sitemap. */
    public Set<String> getCrossSiteUrls() {

        if (crossSiteUrls != null) {
            return new HashSet<>(crossSiteUrls);
        } else {
            return new HashSet<>();
        }
    }

    void setCrossSiteUrls(Set<String> crossSiteUrls) {
        this.crossSiteUrls = crossSiteUrls;
    }

    /** Returns true if this SiteMap is an index file for a list of SiteMaps. */
    public boolean isIndex() {
        return siteMaps == null;
    }

    /** Returns a list of all the sitemaps represented by this site map index,
     *  or null if this is not a sitemap index. */
    public List<SiteMap> getSiteMaps() {
        if (siteMaps != null && !siteMaps.isEmpty()) {
            return new ArrayList<>(siteMaps);
        } else {
            return null;
        }
    }

    void setSiteMaps(List<SiteMap> siteMaps) {
        this.siteMaps = siteMaps;
    }

    public Date getLastModified() {
        return lastModified;
    }

    void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getXml() {

        if (xml == null) {

            if (data != null) {

                if (isCompressed) {
                    try {
                        xml = new String(CompressionUtils.decompress(BaseEncoding.base64().decode(data)), StandardCharsets.UTF_8);

                    } catch (DataFormatException | IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                } else {
                    xml = data;
                }
            }
        }

        return xml;
    }

    void setXml(String xml) {

        if (xml != null) {
            try {
                data = BaseEncoding.base64().encode(CompressionUtils.compress(xml.getBytes(StandardCharsets.UTF_8)));
                isCompressed = true;
                this.xml = xml;

            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);

                data = xml;
                isCompressed = false;
                this.xml = xml;
            }

        } else {
            data = null;
            isCompressed = false;
            this.xml = null;
        }
    }

    void clearTransientXml() {
        this.xml = null;
    }

    @Override
    public String getLabel() {
        return siteMapUrl + "/sitemap" + (siteMapIndex == null ? "" : siteMapIndex) + ".xml";
    }
}
