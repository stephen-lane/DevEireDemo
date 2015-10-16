package com.psddev.util.sitemap;

import com.psddev.dari.util.Settings;
import com.psddev.dari.util.SettingsBackedObject;
import com.psddev.util.task.JobSettings;

import java.util.Map;
import java.util.Set;

/**
 * Global configuration for how the site map should be generated.
 */
public interface SiteMapConfig extends SettingsBackedObject {

    /** Setting key for all localization configurations. */
    public static final String SETTING_PREFIX = "cms/siteMap/config";

    /** Setting key for default localization environment name. */
    public static final String DEFAULT_SITE_MAP_CONFIG_SETTING = "cms/siteMap/defaultConfig";

    /**
     * Gets the Settings which determine if this task is allowed to run
     * and determines frequency of runs.
     *
     * @return The applicable JobSettings.
     */
    JobSettings getJobSettings();

    /**
     * An optional mapping of site URLs to the registered domains associated
     * with that site URL that describes how the site maps should be
     * generated. All Domains to be included in sitemaps must included in this Map.
     */
    Map<String, Set<String>> getSiteMapCrossSiteUrls();

    /**
     * The maximum number of entries in any given sitemap xml. If null is
     * returned, defaults to 50,000.
     *
     * @return the maximum number of entries.
     */
    Integer getMaximumSiteMapEntries();

    /**
     * The maximum file size (in bytes) of any given sitemap xml. If null
     * is returned, defaults to 10,485,760 (10MB).
     *
     * @return the maximum file size.
     */
    Integer getMaximumSiteMapFileSize();

    /**
     * @return The number of attempts to try site map generation after exceptions are thrown.
     */
    int getMaxAttempts();

    /** Returns the localization environment with the given {@code name}. */
    public static SiteMapConfig getInstance(String name) {
        return SiteMapUtils.SITE_MAP_CONFIG_INSTANCES.getUnchecked(name);
    }

    /** Returns the default localization environment. */
    public static SiteMapConfig getDefault() {
        String defaultSetting = Settings.get(String.class, DEFAULT_SITE_MAP_CONFIG_SETTING);
        if (defaultSetting != null) {
            return getInstance(defaultSetting);
        } else {
            return DefaultSiteMapConfig.INSTANCE;
        }
    }
}
