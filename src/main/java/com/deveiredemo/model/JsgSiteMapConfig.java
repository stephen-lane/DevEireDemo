package com.deveiredemo.model;

import com.psddev.cms.tool.CmsTool;
import com.psddev.dari.db.Application;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.Settings;
import com.psddev.util.SettingsOverride;
import com.psddev.util.sitemap.SiteMapConfig;
import com.psddev.util.sitemap.SiteMapGenerator;
import com.psddev.util.task.JobSettings;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The configuration settings for the sitemap.xml generation task.
 */
public class JsgSiteMapConfig implements SiteMapConfig,
                                         SettingsOverride {

    @Override
    public JobSettings getJobSettings() {
        return JsgSite.getInstance().getSiteMap();
    }

    @Override
    public Map<String, Set<String>> getSiteMapCrossSiteUrls() {
        Map<String, Set<String>> siteMapCrossSiteUrls = new HashMap<>();
        String siteUrl = Application.Static.getInstance(CmsTool.class).getDefaultSiteUrl();
        if (siteUrl != null) {
            siteMapCrossSiteUrls.put(siteUrl, Collections.singleton(siteUrl));
        }
        return siteMapCrossSiteUrls;
    }

    @Override
    public Integer getMaximumSiteMapEntries() {
        return 45000;
    }

    @Override
    public Integer getMaximumSiteMapFileSize() {
        return (int) (1024 * 1024 * 9.5);
    }

    @Override
    public int getMaxAttempts() {
        return 3;
    }

    @Override
    public void initialize(String settingsKey, Map<String, Object> settings) {
        // nothing to do.
    }

    @Override
    public Map<String, Object> getOverrideSettings() {
        Map<String, Object> overrides = new HashMap<>();

        if (Settings.get(String.class, DEFAULT_SITE_MAP_CONFIG_SETTING) == null) {

            CollectionUtils.putByPath(overrides, DEFAULT_SITE_MAP_CONFIG_SETTING, Constants.APP_KEY);

            CollectionUtils.putByPath(overrides,
                    SiteMapConfig.SETTING_PREFIX + "/" + Constants.APP_KEY,
                    ImmutableMap.of("class", JsgSiteMapConfig.class.getName()));

            // misc settings
            CollectionUtils.putByPath(overrides, SiteMapGenerator.QUERY_FETCH_SIZE_OPTION, 400);
            CollectionUtils.putByPath(overrides, SiteMapGenerator.QUERY_USE_JDBC_FETCH_SIZE_OPTION, Boolean.FALSE);
        }

        return overrides;
    }
}
