package com.psddev.util.sitemap;

/**
 * How often a URL is updated.
 */
public enum SiteMapChangeFrequency {

    ALWAYS("always"),
    HOURLY("hourly"),
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly"),
    NEVER("never");

    private String display;

    private SiteMapChangeFrequency(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
