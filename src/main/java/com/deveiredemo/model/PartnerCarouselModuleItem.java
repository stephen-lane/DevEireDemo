package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public class PartnerCarouselModuleItem extends Content {

    @Required
    private Image image;

    private Link link;

    public Link getLink() {
        return link;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String getLabel() {
        return image != null ? image.getLabel() : null;
    }
}
