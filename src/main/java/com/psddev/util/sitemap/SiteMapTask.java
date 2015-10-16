package com.psddev.util.sitemap;

import com.psddev.util.task.Job;
import com.psddev.util.task.JobSettings;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SiteMapTask extends Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(SiteMapTask.class);

    private SiteMapConfig getConfig() {
        return SiteMapConfig.getDefault();
    }

    @Override
    protected JobSettings getJobSettings() {
        SiteMapConfig config = getConfig();
        return config != null ? config.getJobSettings() : null;
    }

    @Override
    protected void doRepeatingTask(DateTime runTime) throws Exception {

        SiteMapConfig config = getConfig();

        // Make sure the config is not null.
        if (config != null) {

            // Find the last time site map was run
            SiteMapLastRun lastRun = SiteMapLastRun.getInstance();
            DateTime lastStart = lastRun.getStartDate();

            if (!lastRun.isRunning() && runTime.isAfter(lastStart)) {
                doGenerateSiteMaps(config, lastRun);

                SiteMapSummary.getSummaries(); // Kick off Lazy / Periodic Cache
                return;
            }

        } else {
            LOGGER.warn("Site Map Task will never run! No Config is set!");
        }

        skipRunCount();
    }

    /**
     * Saves the {@code LastRun} object with the current time as generates the sitemaps.
     * @param lastRun The {@code LastRun} object
     */
    private void doGenerateSiteMaps(SiteMapConfig config, SiteMapLastRun lastRun) {

        if (lastRun == null) {
            lastRun = SiteMapLastRun.getInstance();
        }

        SiteMapUtils.generateSiteMaps(config, lastRun);
    }
}
