package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

@ToolUi.Referenceable(via = HtmlEmbedVideoEnhancement.class)

@Recordable.DisplayName("Video (HTML Embed)")
@Recordable.PreviewField("thumbnail")
public class HtmlEmbedVideo extends Content implements Video {

    @ToolUi.Note(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    private String name;

    private String title;

    @ToolUi.RichText
    private String description;

    @ToolUi.CodeType("html")
    private String embedCode;

    private StorageItem thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public void setEmbedCode(String embedCode) {
        this.embedCode = embedCode;
    }

    @Override
    public StorageItem getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(StorageItem thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public boolean isPlayable() {
        return !StringUtils.isBlank(getEmbedCode());
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        // TODO: Waiting on designs for video page.
        return null;
    }

    // --- Promotable Support ---

    @Override
    public String getPromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public Image getPromotableImageFallback() {

        StorageItem thumbnail = getThumbnail();
        if (thumbnail != null) {

            Image image = new Image();
            image.setFile(thumbnail);
            return image;
        }

        return null;
    }

    // --- UbikContent support ---

    @Override
    public StorageItem getCardImage() {
        return getThumbnail();
    }
}
