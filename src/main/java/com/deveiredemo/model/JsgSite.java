package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.Recordable;

@Recordable.DisplayName("J. Spieth Golf Site")
public class JsgSite extends Application {

    private String websiteName;

    private String websiteTwitterHandle;

    private String facebookAppId;

    private String googleAnalyticsKey;

    // --- background jobs ---

    @ToolUi.Tab("Jobs")
    private String defaultJobHostOrIpAddress;

    @ToolUi.Tab("Jobs")
    private BackgroundJob siteMap;

    public String getWebsiteName() {
        return websiteName;
    }

    public String getWebsiteTwitterHandle() {
        return websiteTwitterHandle;
    }

    public String getFacebookAppId() {
        return facebookAppId;
    }

    public double getDynamicQueryTimeout() {
        return 3;
    }

    public static JsgSite getInstance() {
        return Application.Static.getInstance(JsgSite.class);
    }

    public String getGoogleAnalyticsKey() {
        return googleAnalyticsKey;
    }

    public String getDefaultJobHostOrIpAddress() {
        return defaultJobHostOrIpAddress;
    }

    public BackgroundJob getSiteMap() {
        return siteMap;
    }
}
