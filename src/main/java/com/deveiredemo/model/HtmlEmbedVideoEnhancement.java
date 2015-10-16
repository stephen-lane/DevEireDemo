package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.IframeView;

@ViewMapping(IframeView.FromHtmlEmbedVideoEnhancement.class)

@Recordable.DisplayName("HTML Embed Video Enhancement Overrides")
public class HtmlEmbedVideoEnhancement extends Reference {

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
    public HtmlEmbedVideo getObject() {
        Object video = super.getObject();
        if (video instanceof HtmlEmbedVideo) {
            return (HtmlEmbedVideo) video;
        } else {
            return null;
        }
    }
}
