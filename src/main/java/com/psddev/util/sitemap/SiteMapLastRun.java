package com.psddev.util.sitemap;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Query;
import com.psddev.dari.db.Record;
import com.psddev.dari.db.Singleton;

import org.joda.time.DateTime;

/**
 * The saved object to store the last time the SiteMap task was ran.
 */
@ToolUi.Hidden
class SiteMapLastRun extends Record implements Singleton {

    private Long startDate;

    private Long lastDuration;

    private Integer lastSuccessfulSiteMapCount;

    private Long lastSuccessfulSiteMapEntryCount;

    private boolean isRunning;

    public DateTime getStartDate() {
        return (startDate == null ? new DateTime(0) : new DateTime(startDate));
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = (startDate == null ? null : startDate.getMillis());
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public Integer getLastSuccessfulSiteMapCount() {
        return lastSuccessfulSiteMapCount;
    }

    public void setLastSuccessfulSiteMapCount(Integer lastSuccessfulSiteMapCount) {
        this.lastSuccessfulSiteMapCount = lastSuccessfulSiteMapCount;
    }

    public Long getLastSuccessfulSiteMapEntryCount() {
        return lastSuccessfulSiteMapEntryCount;
    }

    public void setLastSuccessfulSiteMapEntryCount(Long lastSuccessfulSiteMapEntryCount) {
        this.lastSuccessfulSiteMapEntryCount = lastSuccessfulSiteMapEntryCount;
    }

    public void endRun(boolean successful, int siteMapCount, long siteMapEntryCount) {
        if (startDate != null && successful) {
            lastDuration = DateTime.now().getMillis() - startDate;
            lastSuccessfulSiteMapCount = siteMapCount > 0 ? siteMapCount : null;
            lastSuccessfulSiteMapEntryCount = siteMapEntryCount > 0L ? siteMapEntryCount : null;
        }

        isRunning = false;
        super.saveImmediately();
    }

    public String getLastDurationString() {

        if (lastDuration != null) {

            long remainder = lastDuration;

            StringBuilder duration = new StringBuilder();

            long second = 1000L;
            long minute = 60 * second;
            long hour = 60 * minute;

            if (hour > remainder) {

                duration.append(remainder / hour)
                        .append(" hours ");
                remainder = remainder % hour;
            }

            duration.append(remainder / minute)
                    .append(" minutes ");
            remainder = remainder % minute;

            duration.append(remainder / second)
                    .append(" seconds.");

            return duration.toString();
        }

        return null;
    }

    /**
     * @return Never {@code null}
     */
    static SiteMapLastRun getInstance() {
        SiteMapLastRun lastRun = Query.from(SiteMapLastRun.class).first();
        if (lastRun == null) {
            lastRun = new SiteMapLastRun();
        }
        return lastRun;
    }
}
