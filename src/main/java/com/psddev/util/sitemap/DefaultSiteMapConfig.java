package com.psddev.util.sitemap;

import com.psddev.util.task.JobSettings;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

import java.util.Map;
import java.util.Set;

// Default config for SiteMap.Task when no other class implements SiteMapConfig.
// Runs on the first of every month.
class DefaultSiteMapConfig implements SiteMapConfig {

    static final DefaultSiteMapConfig INSTANCE = new DefaultSiteMapConfig();

    @Override
    public JobSettings getJobSettings() {
        return new JobSettings() {

            @Override
            public boolean isAllowedToRun() {
                return true;
            }

            // Beginning of each month.
            @Override
            public DateTime calculateRunTime(DateTime currentTime) {
                return currentTime.property(DateTimeFieldType.monthOfYear()).roundFloorCopy();
            }

            @Override
            public String getLabel() {
                return "Default Job Settings";
            }
        };
    }

    @Override
    public Map<String, Set<String>> getSiteMapCrossSiteUrls() {
        return null;
    }

    // 50,000
    @Override
    public Integer getMaximumSiteMapEntries() {
        return 50000;
    }

    // 10 MB
    @Override
    public Integer getMaximumSiteMapFileSize() {
        return 1024 * 1024 * 10;
    }

    // 3
    @Override
    public int getMaxAttempts() {
        return 3;
    }

    @Override
    public void initialize(String settingsKey, Map<String, Object> settings) {
        // nothing to do.
    }
}
