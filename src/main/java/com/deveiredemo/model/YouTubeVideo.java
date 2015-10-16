package com.deveiredemo.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.google.common.base.MoreObjects;
import com.deveiredemo.cms.UiWriter;
import com.deveiredemo.util.ContentStateUtils;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.squareup.okhttp.HttpUrl;

@ToolUi.Referenceable(via = YouTubeVideoEnhancement.class)

@Recordable.DisplayName("Video (YouTube)")
@Recordable.PreviewField("getThumbnail")
public class YouTubeVideo extends Content implements Video {

    private static final String YOU_TUBE_OEMBED_PROVIDER_NAME = "YouTube";

    private static final String DEFAULT_YOU_TUBE_VIDEO_URL_FORMAT = "https://www.youtube.com/watch?v=%s";

    private static final List<String> YOU_TUBE_WATCH_HOSTS = Arrays.asList(
            "www.youtube.com",
            "youtube.com");

    private static final List<String> YOU_TUBE_SHORT_URL_HOSTS = Arrays.asList(
            "www.youtu.be",
            "youtu.be");

    private static final String YOU_TUBE_WATCH_VIDEO_ID_PARAMETER = "v";

    @DisplayName("YouTube URL or ID")
    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getPreviewVideoLinkNoteHtml(pageContext)}'></span>")
    private String videoIdentifier;

    @ToolUi.Placeholder(dynamicText = "${content.titleFallback}", editable = true)
    private String title;

    @ToolUi.RichText
    private String description;

    @ToolUi.NoteHtml("<span data-dynamic-html='${content.getPreviewThumbnailNoteHtml(pageContext)}'></span>")
    private StorageItem thumbnail;

    @ToolUi.Hidden
    private String previousVideoIdentifier;

    @Embedded
    @ToolUi.Hidden
    private ExternalContent externalContent;

    public String getVideoIdentifier() {
        return videoIdentifier;
    }

    public String getTitle() {
        if (title == null) {
            return getTitleFallback();
        }
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    @Ignored(false) @ToolUi.Hidden
    public StorageItem getThumbnail() {
        if (thumbnail == null) {
            return getOembedThumbnail();
        }
        return thumbnail;
    }

    public ExternalContent getExternalContent() {
        return externalContent;
    }

    public String getTitleFallback() {
        return getOembedMetadata(String.class, Constants.OEMBED_TITLE_KEY);
    }

    public StorageItem getOembedThumbnail() {

        String thumbnailUrl = getOembedMetadata(String.class, Constants.OEMBED_THUMBNAIL_URL_KEY);
        if (thumbnailUrl != null && HttpUrl.parse(thumbnailUrl) != null) {

            StorageItem thumbnail = StorageItem.Static.createUrl(thumbnailUrl);

            Map<String, Object> metadata = new HashMap<>();

            metadata.put(Constants.STORAGE_ITEM_METADATA_WIDTH_KEY, getOembedMetadata(Integer.class, Constants.OEMBED_THUMBNAIL_WIDTH_KEY));
            metadata.put(Constants.STORAGE_ITEM_METADATA_HEIGHT_KEY, getOembedMetadata(Integer.class, Constants.OEMBED_THUMBNAIL_HEIGHT_KEY));

            thumbnail.setMetadata(metadata);

            return thumbnail;
        }
        return null;
    }

    public Integer getOembedWidth() {
        return getOembedMetadata(Integer.class, Constants.OEMBED_WIDTH_KEY);
    }

    public Integer getOembedHeight() {
        return getOembedMetadata(Integer.class, Constants.OEMBED_HEIGHT_KEY);
    }

    public String getPreviewVideoLinkNoteHtml(PageContext pageContext) throws IOException {
        String html = "";
        if (isPlayable()) {
            String videoUrl = MoreObjects.firstNonNull(getVideoUrl(), "");

            UiWriter ui = new UiWriter(pageContext);

            ui.writeStart("a",
                    "class", "icon icon-action-preview",
                    "target", "contentExternalPreview",
                    "onclick", "this.href = CONTEXT_PATH + '/content/externalPreview?url=' + encodeURIComponent('" + StringUtils.escapeJavaScript(videoUrl) + "'); return true;");
            {
                ui.writeHtml("Preview");
            }
            ui.writeEnd();
            ui.writeHtml(" | ");
            html += ui.toHtml();
        }
        html += " Video URL or ID.";
        return html;
    }

    public String getPreviewThumbnailNoteHtml(PageContext pageContext) throws IOException {

        if (thumbnail == null) {

            StorageItem oembedVideoThumbnail = getOembedThumbnail();

            if (oembedVideoThumbnail != null) {

                UiWriter ui = new UiWriter(pageContext);
                ui.writeStart("p", "style", "margin:0 0 3px 0;");
                {
                    ui.writeHtml("Thumbnail Preview");
                }
                ui.writeEnd();
                ui.writeCmsPreviewImage(oembedVideoThumbnail, 250);

                return ui.toHtml();
            }
        }

        return null;
    }

    // --- Video support ---

    @Override
    public boolean isPlayable() {
        return YOU_TUBE_OEMBED_PROVIDER_NAME.equals(getOembedMetadata(String.class, Constants.OEMBED_PROVIDER_NAME_KEY));
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

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {
        // TODO: Waiting on designs for video page
        return null;
    }

    // --- Record support ---

    public String getLabel() {
        String label = getTitle();
        if (label == null) {
            label = getVideoIdentifier();
        }
        return label;
    }

    @Override
    protected void beforeSave() {
        super.beforeSave();

        if (!ContentStateUtils.isContentStateRequest()) {
            updateExternalContent();
        }
    }

    // --- UbikContent support ---

    @Override
    public StorageItem getCardImage() {
        return getThumbnail();
    }

    private void updateExternalContent() {

        if (!ObjectUtils.equals(getVideoIdentifier(), previousVideoIdentifier)) {

            String videoId = getVideoId();
            String videoUrl = getVideoUrl();

            this.previousVideoIdentifier = this.videoIdentifier;

            if (videoUrl != null) {
                this.externalContent = new ExternalContent();
                this.externalContent.setUrl(videoUrl);
            } else {
                this.externalContent = null;
            }
        }
    }

    private String getVideoId() {

        String videoId = null;

        String videoUrl = getVideoUrl();
        if (videoUrl != null) {

            HttpUrl url = HttpUrl.parse(videoUrl);
            if (url != null) {

                String host = url.host();
                if (YOU_TUBE_WATCH_HOSTS.contains(host)) {
                    videoId = url.queryParameter(YOU_TUBE_WATCH_VIDEO_ID_PARAMETER);

                } else if (YOU_TUBE_SHORT_URL_HOSTS.contains(host)) {

                    List<String> segments = url.pathSegments();
                    if (segments.size() >= 1) {
                        videoId = segments.get(0);

                        if (StringUtils.isBlank(videoId)) {
                            videoId = null;
                        }
                    }
                }

            }
        } else {
            videoId = getVideoIdentifier();
        }

        return videoId;
    }

    private String getVideoUrl() {

        String videoUrl = null;

        String identifier = getVideoIdentifier();

        if (!StringUtils.isBlank(identifier)) {
            identifier = identifier.trim();

            HttpUrl url = HttpUrl.parse(identifier);
            if (url != null) {
                videoUrl = identifier;
            } else {
                videoUrl = String.format(DEFAULT_YOU_TUBE_VIDEO_URL_FORMAT, identifier);
            }
        }

        return videoUrl;
    }

    private <T> T getOembedMetadata(Class<T> returnType, String key) {
        Map<String, Object> response;
        if (externalContent != null && (response = externalContent.getResponse()) != null) {
            return ObjectUtils.to(returnType, CollectionUtils.getByPath(response, key));
        }
        return null;
    }
}
