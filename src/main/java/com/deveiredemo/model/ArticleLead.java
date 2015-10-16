package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class ArticleLead extends Content {

    private Image image;

    private String captionOverride;

    private boolean isSuperLead;

    public Image getImage() {
        return image;
    }

    public String getCaptionOverride() {
        return captionOverride;
    }

    public boolean isSuperLead() {
        return isSuperLead;
    }
}
