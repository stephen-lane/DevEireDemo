package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.cms.UiWriter;
import com.deveiredemo.util.TextUtils;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

public interface TwitterCardDefinable extends Recordable {

    static final String TAB_NAME = "Twitter Card";

    /**
     * Returns the fallback title for this twitter card. DO NOT CALL the
     * {@link TwitterCardDefinable.Data#getTwitterTitle()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value for the twitter card twitter:title property
     * value.
     */
    default String getTwitterTitleFallback() {
        if (this instanceof OpenGraphDefinable) {
            return ((OpenGraphDefinable) this).getOpenGraphData().getOpenGraphTitle();
        }
        return null;
    }

    /**
     * Returns the fallback description for this open graph. DO NOT CALL the
     * {@link TwitterCardDefinable.Data#getTwitterDescription()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value for the twitter card twitter:description
     * property value. The returned value SHOULD be plain text, and NOT HTML
     * markup.
     */
    default String getTwitterDescriptionFallback() {
        if (this instanceof OpenGraphDefinable) {
            return ((OpenGraphDefinable) this).getOpenGraphData().getOpenGraphDescription();
        }
        return null;
    }

    /**
     * Returns the fallback image for this open graph. DO NOT CALL the
     * {@link TwitterCardDefinable.Data#getTwitterImage()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value used to power the open graph og:image
     * property value.
     */
    default Image getTwitterImageFallback() {
        if (this instanceof OpenGraphDefinable) {
            return ((OpenGraphDefinable) this).getOpenGraphData().getOpenGraphImage();
        }
        return null;
    }

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getTwitterCardData() {
        return as(TwitterCardDefinable.Data.class);
    }

    @BeanProperty("twitterCard")
    static class Data extends Modification<TwitterCardDefinable> {

        private static final int MAX_TITLE_LENGTH = 70;
        private static final int MAX_DESCRIPTION_LENGTH = 200;

        @ToolUi.Tab(TAB_NAME)
        @ToolUi.SuggestedMaximum(MAX_TITLE_LENGTH)
        @ToolUi.Placeholder(dynamicText = "${content.twitterCard.getTwitterTitlePlaceholder()}", editable = true)
        private String twitterTitle;

        @ToolUi.Tab(TAB_NAME)
        @ToolUi.SuggestedMaximum(MAX_DESCRIPTION_LENGTH)
        @ToolUi.Placeholder(dynamicText = "${content.twitterCard.getTwitterDescriptionPlaceholder()}", editable = true)
        private String twitterDescription;

        // max 1MB
        @ToolUi.Tab(TAB_NAME)
        @ToolUi.NoteHtml("<span data-dynamic-html='${content.twitterCard.getPlaceholderImageHtml(pageContext)}'></span>")
        private Image twitterImage;

        // --- Getter methods ---

        public String getTwitterTitle() {
            if (StringUtils.isBlank(twitterTitle)) {
                // don't override the instance variable because the CMS will persist it
                return getTwitterTitlePlaceholder();
            }
            return TextUtils.cleanPlaceholderText(twitterTitle);
        }

        public String getTwitterDescription() {
            if (StringUtils.isBlank(twitterDescription)) {
                // don't override the instance variable because the CMS will persist it
                return getTwitterDescriptionPlaceholder();
            }
            return TextUtils.cleanPlaceholderText(twitterDescription);
        }

        public Image getTwitterImage() {
            if (twitterImage == null) {
                // don't override the instance variable because the CMS will persist it
                return getTwitterImagePlaceholder();
            }
            return twitterImage;
        }

        // --- Calculated Meta Values ---

        public String getMetaTwitterCard() {
            return "summary";
        }

        public String getMetaTwitterSite() {
            JsgSite siteConfig = JsgSite.getInstance();
            if (siteConfig != null) {

                String twitterHandle = siteConfig.getWebsiteTwitterHandle();
                if (twitterHandle != null && !twitterHandle.startsWith("@")) {

                    twitterHandle = "@" + twitterHandle;
                }

                return twitterHandle;
            }
            return null;
        }

        public String getMetaTwitterTitle() {
            return getTwitterTitle();
        }

        public String getMetaTwitterDescription() {
            return getTwitterDescription();
        }

        // TODO: Need to be less than 1MB.
        public String getMetaTwitterImage() {
            Image image = getTwitterImage();
            if (image != null) {

                StorageItem file = image.getFile();
                if (file != null) {

                    String url = file.getPublicUrl();

                    if (url != null) {
                        return url;
                    }
                }
            }

            return null;
        }

        // --- Placeholder Values ---

        public String getTwitterTitlePlaceholder() {
            return truncate(getOriginalObject().getTwitterTitleFallback(), MAX_TITLE_LENGTH);
        }

        public String getTwitterDescriptionPlaceholder() {
            return truncate(getOriginalObject().getTwitterDescriptionFallback(), MAX_DESCRIPTION_LENGTH);
        }

        public Image getTwitterImagePlaceholder() {
            return getOriginalObject().getTwitterImageFallback();
        }

        /**
         * Returns CMS UI HTML markup for the open graph image fallback when
         * it's not explicitly published in the open graph data. Used for
         * dynamic ToolUi.NoteHtml.
         *
         * @param pageContext the servlet pageContext
         * @return CMS UI HTML
         * @throws java.io.IOException
         */
        public String getPlaceholderImageHtml(PageContext pageContext) throws IOException {

            if (twitterImage == null) {
                Image placeholderImage = getTwitterImagePlaceholder();
                if (placeholderImage != null) {

                    StorageItem file = placeholderImage.getFile();
                    if (file != null) {

                        UiWriter ui = new UiWriter(pageContext);
                        ui.writeStart("p", "style", "margin:0 0 3px 0;");
                        ui.writeHtml("Placeholder Image");
                        ui.writeEnd();
                        ui.writeCmsPreviewImage(file);

                        return ui.toString();
                    }
                }
            }

            return null;
        }

        // --- Private methods ---

        private String truncate(String text, int maxCharacters) {
            // TODO: Improve implementation to detect words, and append ellipsis.
            if (text != null && text.length() > maxCharacters) {
                text = text.substring(0, maxCharacters);
            }
            return text;
        }
    }
}
