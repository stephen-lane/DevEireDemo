package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.StringUtils;

public class ExternalLink extends Link {

    @Required
    @ToolUi.Note("Please include protocol, e.g. \"http://\"")
    @ToolUi.Placeholder(value = "http://", editable = true)
    private String url;

    private LinkTarget target = LinkTarget._blank;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Object getHref() {
        return getUrl();
    }

    @Override
    public LinkTarget getTarget() {
        return target;
    }

    @Override
    public void beforeSave() {
        if (url != null) {
            url = StringUtils.trimAndCollapseWhitespaces(url);
        }
    }
}
