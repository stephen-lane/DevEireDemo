package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.util.TextUtils;
import com.deveiredemo.view.CaptionView;

@ViewMapping(CaptionView.FromImageEnhancement.class)

@Recordable.DisplayName("Image Enhancement Overrides")
public class ImageEnhancement extends Reference {

    @ToolUi.Hidden
    @ToolUi.Note("Not yet supported...")
    @ToolUi.Heading("Image Metadata Overrides")
    private Link link;

    @ToolUi.RichText
    private String captionOverride;

    private String creditOverride;

    @ToolUi.Heading("Image Sizing Overrides")
    @ToolUi.NoteHtml("These settings only apply when the enhancement is "
            + "right or left aligned. You can NOT constrain the dimensions "
            + "when the enhancement is set to full.")
    private Integer width;

    private Integer height;

    private boolean maintainAspectRatio;

    public Link getLink() {
        return link;
    }

    public String getCaptionOverride() {
        if (ReferentialTextUtils.isBlank(captionOverride)) {
            Image image = getObject();
            if (image != null) {
                return image.getCaption();
            }
        }
        return TextUtils.cleanPlaceholderText(captionOverride);
    }

    public String getCreditOverride() {
        if (StringUtils.isBlank(creditOverride)) {
            Image image = getObject();
            if (image != null) {
                return image.getCredit();
            }
        }
        return TextUtils.cleanPlaceholderText(creditOverride);
    }

    public Integer getWidth() {
        if (width != null && width <= 0) {
            width = null;
        }
        return width;
    }

    public Integer getHeight() {
        if (height != null && height <= 0) {
            height = null;
        }
        return height;
    }

    public boolean isMaintainAspectRatio() {
        return maintainAspectRatio;
    }

    @Override
    public Image getObject() {
        Object object = super.getObject();
        if (object instanceof Image) {
            return (Image) object;
        } else {
            return null;
        }
    }
}
