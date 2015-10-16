package com.deveiredemo.model;

public enum SocialLinkIcon {

    FACEBOOK("Facebook"),
    TWITTER("Twitter"),
    PINTEREST("Pinterest"),
    LINKED_IN("LinkedIn"),
    INSTAGRAM("Instagram"),
    GOOGLE_PLUS("Google+"),
    EMAIL("Email");

    private String label;

    private SocialLinkIcon(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
