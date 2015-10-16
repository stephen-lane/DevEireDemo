package com.deveiredemo.model;

import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.util.TextUtils;
import com.deveiredemo.view.GalleryFullscreenSlideView;
import com.deveiredemo.view.ListPromoView;

import com.psddev.aod.UbikContent;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

@Recordable.Embedded
@Recordable.PreviewField("image/file")

@ViewMapping(GalleryFullscreenSlideView.FromGallerySlide.class)
@ViewMapping(ListPromoView.FromGallerySlideAsEnhancement.class)

public class GallerySlide extends Content implements UbikContent {

    @ToolUi.BulkUpload
    private Image image;

    @ToolUi.Placeholder(dynamicText = "${content.titleOverrideFallback}", editable = true)
    private String titleOverride;

    @ToolUi.RichText
    @ToolUi.Placeholder(dynamicText = "${content.captionOverrideFallback}", editable = true)
    private String captionOverride;

    public Image getImage() {
        return image;
    }

    public String getTitleOverride() {
        if (StringUtils.isBlank(titleOverride)) {
            return getTitleOverrideFallback();
        }
        return TextUtils.cleanPlaceholderText(titleOverride);
    }

    public String getCaptionOverride() {
        if (ReferentialTextUtils.isBlank(captionOverride)) {
            return getCaptionOverrideFallback();
        }
        return TextUtils.cleanPlaceholderText(captionOverride);
    }

    public String getTitleOverrideFallback() {
        return image != null ? image.getTitle() : null;
    }

    public String getCaptionOverrideFallback() {
        return image != null ? image.getCaption() : null;
    }

    @Override
    public String getLabel() {
        return getTitleOverride();
    }

    // --- UbikContent support ---

    @Override
    public String getCardTitle() {
        return getImage() != null ? getImage().getName() : null;
    }

    @Override
    public StorageItem getCardImage() {
        return getImage() != null ? getImage().getFile() : null;
    }

    @Override
    public String getCardBlurb() {
        return ObjectUtils.firstNonBlank(getCaptionOverride(), getImage() != null ? getImage().getCaption() : null);
    }
}
