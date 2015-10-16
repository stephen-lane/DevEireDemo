package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.GalleryGridView;

//@ViewMapping(GalleryModuleView.FromGalleryEnhancement.class)
@ViewMapping(GalleryGridView.FromGalleryEnhancement.class)

@Recordable.DisplayName("Gallery Enhancement Overrides")
public class GalleryEnhancement extends Reference {

    private String headlineOverride;

    @ToolUi.RichText
    private String descriptionOverride;

    private GalleryEnhancementStyle galleryStyle = GalleryEnhancementStyle.LOW_RES;

    public String getHeadlineOverride() {
        if (headlineOverride == null) {
            return getGallery().getPromotableData().getTitle();
        }
        return headlineOverride;
    }

    public String getDescriptionOverride() {
        if (descriptionOverride == null) {
            return getGallery().getPromotableData().getDescription();
        }
        return descriptionOverride;
    }

    public GalleryEnhancementStyle getGalleryStyle() {
        if (galleryStyle == null) {
            galleryStyle = GalleryEnhancementStyle.LOW_RES;
        }
        return galleryStyle;
    }

    public Gallery getGallery() {
        Object object = getObject();
        return object instanceof Gallery ? (Gallery) object : null;
    }
}
