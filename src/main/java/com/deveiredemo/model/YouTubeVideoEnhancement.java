package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.IframeView;

@ViewMapping(IframeView.FromYouTubeVideoEnhancement.class)

@Recordable.DisplayName("YouTube Video Enhancement Overrides")
public class YouTubeVideoEnhancement extends Reference {

    private String title;

    private boolean hideOnMobile;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isHideOnMobile() {
        return hideOnMobile;
    }

    public void setHideOnMobile(boolean hideOnMobile) {
        this.hideOnMobile = hideOnMobile;
    }

    @Override
    public YouTubeVideo getObject() {
        Object video = super.getObject();
        if (video instanceof YouTubeVideo) {
            return (YouTubeVideo) video;
        } else {
            return null;
        }
    }
}
