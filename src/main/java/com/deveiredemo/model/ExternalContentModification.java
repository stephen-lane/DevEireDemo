package com.deveiredemo.model;

import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Modification;

import com.deveiredemo.view.IframeView;

@ViewMapping(IframeView.FromExternalContent.class)

public class ExternalContentModification extends Modification<ExternalContent> {

    @ToolUi.DisplayFirst
    private String title;

    @ToolUi.DisplayLast
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
}
