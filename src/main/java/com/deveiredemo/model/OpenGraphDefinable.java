package com.deveiredemo.model;

import com.psddev.cms.db.Seo;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.cms.UiWriter;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.util.TextUtils;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

/**
 * Objects that implement the <a href="http://ogp.me/">open graph protocol</a>.
 */
public interface OpenGraphDefinable extends Recordable {

    static final String TAB_NAME = "Open Graph";

    /**
     * @return the open graph type used to power the og:type property value.
     */
    default OpenGraphType getOpenGraphType() {
        return OpenGraphType.WEBSITE;
    }

    /**
     * Returns the fallback title for this open graph. DO NOT CALL the
     * {@link OpenGraphDefinable.Data#getOpenGraphTitle()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value for the open graph og:title property
     * value.
     */
    default String getOpenGraphTitleFallback() {
        return this.as(Seo.ObjectModification.class).findTitle();
    }

    /**
     * Returns the fallback description for this open graph. DO NOT CALL the
     * {@link OpenGraphDefinable.Data#getOpenGraphDescription()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value for the open graph og:description property
     * value. The returned value SHOULD be plain text, and NOT HTML markup.
     */
    default String getOpenGraphDescriptionFallback() {
        return ReferentialTextUtils.stripHtml(this.as(Seo.ObjectModification.class).findDescription());
    }

    /**
     * Returns the fallback image for this open graph. DO NOT CALL the
     * {@link OpenGraphDefinable.Data#getOpenGraphImage()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the fallback value used to power the open graph og:image
     * property value.
     */
    default Image getOpenGraphImageFallback() {
        if (this instanceof Promotable) {
            return ((Promotable) this).getPromotableData().getImage();
        }
        return null;
    }

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getOpenGraphData() {
        return as(OpenGraphDefinable.Data.class);
    }

    @BeanProperty("og")
    static class Data extends Modification<OpenGraphDefinable> {

        @ToolUi.Tab(TAB_NAME)
        @DisplayName("OG Title")
        @ToolUi.Placeholder(dynamicText = "${content.og.getOpenGraphTitleFallback()}", editable = true)
        private String openGraphTitle;

        @ToolUi.Tab(TAB_NAME)
        @DisplayName("OG Description")
        @ToolUi.Placeholder(dynamicText = "${content.og.getOpenGraphDescriptionFallback()}", editable = true)
        private String openGraphDescription;

        @ToolUi.Tab(TAB_NAME)
        @DisplayName("OG Image")
        @ToolUi.NoteHtml("<span data-dynamic-html='${content.og.getPlaceholderImageHtml(pageContext)}'></span>")
        private Image openGraphImage;

        /**
         * @return the value for the og:site_name property.
         */
        public String getOpenGraphSiteName() {
            JsgSite siteConfig = JsgSite.getInstance();
            return siteConfig != null ? siteConfig.getWebsiteName() : null;
        }

        /**
         * @return the value used to power the og:type property.
         */
        public OpenGraphType getOpenGraphType() {
            return getOriginalObject().getOpenGraphType();
        }

        /**
         * @return the value for the og:title property.
         */
        public String getOpenGraphTitle() {
            if (StringUtils.isBlank(openGraphTitle)) {
                // don't override the instance variable because the CMS will persist it
                return getOpenGraphTitleFallback();
            }
            return TextUtils.cleanPlaceholderText(openGraphTitle);
        }

        /**
         * @return the value for the og:description property.
         */
        public String getOpenGraphDescription() {
            if (StringUtils.isBlank(openGraphDescription)) {
                // don't override the instance variable because the CMS will persist it
                return getOpenGraphDescriptionFallback();
            }
            return TextUtils.cleanPlaceholderText(openGraphDescription);
        }

        /**
         * @return the image used to power the og:image property.
         */
        public Image getOpenGraphImage() {
            if (openGraphImage == null) {
                // don't override the instance variable because the CMS will persist it
                return getOpenGraphImageFallback();
            }
            return openGraphImage;
        }

        /**
         * Gets the {@code openGraphTitle} fallback. Delegates to interface
         * method {@link OpenGraphDefinable#getOpenGraphTitleFallback()}. This
         * is a workaround for the JavaBean spec not supporting default
         * interface methods.
         */
        public String getOpenGraphTitleFallback() {
            return getOriginalObject().getOpenGraphTitleFallback();
        }

        /**
         * Gets the {@code openGraphDescription} fallback. Delegates to interface
         * method {@link OpenGraphDefinable#getOpenGraphDescriptionFallback()}.
         * This is a workaround for the JavaBean spec not supporting default
         * interface methods.
         */
        public String getOpenGraphDescriptionFallback() {
            return getOriginalObject().getOpenGraphDescriptionFallback();
        }

        /**
         * Gets the {@code openGraphImage} fallback. Delegates to interface
         * method {@link OpenGraphDefinable#getOpenGraphImageFallback()}. This
         * is a workaround for the JavaBean spec not supporting default
         * interface methods.
         */
        public Image getOpenGraphImageFallback() {
            return getOriginalObject().getOpenGraphImageFallback();
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

            if (openGraphImage == null) {
                Image fallbackImage = getOriginalObject().getOpenGraphImageFallback();
                if (fallbackImage != null) {

                    StorageItem file = fallbackImage.getFile();
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
    }
}
