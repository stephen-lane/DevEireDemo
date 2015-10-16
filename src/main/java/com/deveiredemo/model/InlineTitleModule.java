package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.SectionInlineTitleView;

@ViewMapping(SectionInlineTitleView.FromInlineTitleModule.class)

@Recordable.DisplayName("Module (Inline Title)")
public class InlineTitleModule extends Module {

    private String title;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getLabel() {
        return getTitle();
    }
}
