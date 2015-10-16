package com.psddev.util.sitemap;

public enum SiteMapRelationship {

    ALLOW("allow"),
    DENY("deny");

    private String name;

    private SiteMapRelationship(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
