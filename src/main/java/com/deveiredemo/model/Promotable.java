package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.cms.UiWriter;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.util.TextUtils;
import com.deveiredemo.view.PromoModuleLargeItemView;
import com.deveiredemo.view.PromoModuleMediumItemView;
import com.deveiredemo.view.PromoModuleSmallItemView;
import com.deveiredemo.view.PromoModuleSuperItemView;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

@ViewMapping(PromoModuleSmallItemView.FromPromotable.class)
@ViewMapping(PromoModuleMediumItemView.FromPromotable.class)
@ViewMapping(PromoModuleLargeItemView.FromPromotable.class)
@ViewMapping(PromoModuleSuperItemView.FromPromotable.class)

public interface Promotable extends Recordable {

    public static final String FIELD_INTERNAL_NAME_PREFIX = "jsg.promotable.";

    /** For fields that only allow Promotable values that have an image. */
    public static final String HAS_IMAGE_PREDICATE = Promotable.Data.class.getName()
            + "/" + FIELD_INTERNAL_NAME_PREFIX + "hasImage = true";

    /**
     * Type safe shortcut to get the data modification.
     *
     * @return the data modification.
     */
    default Data getPromotableData() {
        return as(Promotable.Data.class);
    }

    /**
     * Returns the fallback title for this promotable. DO NOT CALL the
     * {@link Promotable.Data#getTitle()} method in your implementation or an
     * infinite recursive loop will occur, and DO NOT CALL this method outside
     * of this class as a public API, it is for internal use only!
     *
     * @return the title
     */
    default String getPromotableTitleFallback() {
        return null;
    }

    /**
     * Returns the fallback mobile title for this promotable. DO NOT CALL the
     * {@link Promotable.Data#getMobileTitle()} method in your implementation
     * or an infinite recursive loop will occur, and DO NOT CALL this method
     * outside of this class as a public API, it is for internal use only!
     *
     * @return the mobile title
     */
    default String getPromotableMobileTitleFallback() {
        return null;
    }

    /**
     * Returns the fallback description for this promotable. DO NOT CALL the
     * {@link Promotable.Data#getDescription()} method in your implementation or an
     * infinite recursive loop will occur, and DO NOT CALL this method outside
     * of this class as a public API, it is for internal use only!
     *
     * @return the description
     */
    default String getPromotableDescriptionFallback() {
        return null;
    }

    /**
     * Returns the fallback mobile description for this promotable. DO NOT CALL
     * the {@link Promotable.Data#getMobileDescription()} method in your
     * implementation or an infinite recursive loop will occur, and DO NOT CALL
     * this method outside of this class as a public API, it is for internal
     * use only!
     *
     * @return the mobile description
     */
    default String getPromotableMobileDescriptionFallback() {
        return null;
    }

    /**
     * Returns the fallback image for this promotable. DO NOT CALL the
     * {@link Promotable.Data#getImage()} method in your implementation or an
     * infinite recursive loop will occur, and DO NOT CALL this method outside
     * of this class as a public API, it is for internal use only!
     * @return the image
     */
    default Image getPromotableImageFallback() {
        return null;
    }

    default Link getPromotableLinkFallback() {
        return null;
    }

    /**
     * Returns the Content object this {@link Promotable} represents.  In most
     * cases, this will just return {@code this}. If the object acts as a
     * Promotable wrapper, it will return the content that it references.
     *
     * @return Can be {@code null}
     */
    default Promotable getPromotableReference() {
        return this;
    }

    @BeanProperty("promotable")
    @FieldInternalNamePrefix(FIELD_INTERNAL_NAME_PREFIX)
    static class Data extends Modification<Promotable> {

        public static final String TAB_NAME = "Promoted Fields";

        @ToolUi.DisplayLast
        @ToolUi.Heading(TAB_NAME)
        @DisplayName("Promo Title")
        @ToolUi.Placeholder(dynamicText = "${content.promotable.getTitleFallback()}", editable = true)
        private String title;

        @ToolUi.DisplayLast
        @DisplayName("Promo Title (Mobile)")
        @ToolUi.Placeholder(dynamicText = "${content.promotable.getMobileTitleFallback()}", editable = true)
        private String mobileTitle;

        @ToolUi.DisplayLast
        @DisplayName("Promo Description")
        @ToolUi.RichText
        @ToolUi.Placeholder(dynamicText = "${content.promotable.getDescriptionFallback()}", editable = true)
        private String description;

        @ToolUi.DisplayLast
        @DisplayName("Promo Description (Mobile)")
        @ToolUi.RichText
        @ToolUi.Placeholder(dynamicText = "${content.promotable.getMobileDescriptionFallback()}", editable = true)
        private String mobileDescription;

        @ToolUi.DisplayLast
        @DisplayName("Promo Image")
        @ToolUi.NoteHtml("<span data-dynamic-html='${content.promotable.getPlaceholderImageHtml(pageContext)}'></span>")
        private Image image;

        @Ignored(false) @ToolUi.Hidden
        public String getTitle() {
            if (StringUtils.isBlank(title)) {
                // don't override the instance variable because the CMS will persist it
                return getTitleFallback();
            }

            return TextUtils.cleanPlaceholderText(title);
        }

        public String getMobileTitle() {
            if (StringUtils.isBlank(mobileTitle)) {
                // don't override the instance variable because the CMS will persist it
                return getMobileTitleFallback();
            }

            return TextUtils.cleanPlaceholderText(mobileTitle);
        }

        public String getDescription() {
            if (StringUtils.isBlank(description)) {
                // don't override the instance variable because the CMS will persist it
                return getDescriptionFallback();
            }

            return TextUtils.cleanPlaceholderText(description);
        }

        @Ignored(false) @ToolUi.Hidden
        /** Returns the description with HTML tags stripped out. */
        public String getDescriptionAsText() {
            return ReferentialTextUtils.stripHtml(getDescription());
        }

        public String getMobileDescription() {
            if (StringUtils.isBlank(mobileDescription)) {
                // don't override the instance variable because the CMS will persist it
                return getMobileDescriptionFallback();
            }

            return TextUtils.cleanPlaceholderText(mobileDescription);
        }

        public Image getImage() {
            if (image == null) {
                // don't override the instance variable because the CMS will persist it
                return ObjectUtils.firstNonNull(getOriginalObject().getPromotableImageFallback());
            }

            return image;
        }

        public Link getLink() {
            Link promoLink = getOriginalObject().getPromotableLinkFallback();

            if (promoLink != null) {
                return promoLink;

            } else {
                Object object = getOriginalObject();
                if (object instanceof Content) {

                    ContentLink link = new ContentLink();

                    link.setContent((Content) object);

                    return link;

                } else {
                    return null;
                }
            }
        }

        @ToolUi.Hidden
        @Indexed
        public boolean hasImage() {
            return getImage() != null;
        }

        /**
         * Gets the {@code title} fallback. Delegates to interface method
         * {@link Promotable#getPromotableTitleFallback()}. This is a workaround
         * for the JavaBean spec not supporting default interface methods.
         */
        public String getTitleFallback() {
            return getOriginalObject().getPromotableTitleFallback();
        }

        /**
         * Gets the {@code mobileTitle} fallback. Delegates to interface method
         * {@link Promotable#getPromotableMobileTitleFallback()}. This is a
         * workaround for the JavaBean spec not supporting default interface
         * methods.
         */
        public String getMobileTitleFallback() {
            return ObjectUtils.firstNonNull(
                    getOriginalObject().getPromotableMobileTitleFallback(),
                    getTitle());
        }

        /**
         * Gets the {@code description} fallback. Delegates to interface method
         * {@link Promotable#getPromotableDescriptionFallback()}. This is a
         * workaround for EL not recognizing default interface methods.
         */
        public String getDescriptionFallback() {
            return getOriginalObject().getPromotableDescriptionFallback();
        }

        /**
         * Gets the {@code mobileDescription} fallback. Delegates to interface
         * method {@link Promotable#getPromotableMobileDescriptionFallback()}.
         * This is a workaround for EL not recognizing default interface
         * methods.
         */
        public String getMobileDescriptionFallback() {
            return ObjectUtils.firstNonNull(
                    getOriginalObject().getPromotableMobileDescriptionFallback(),
                    getDescription(),
                    // not sure why I have to return empty string here,
                    // but I get an EL error in the CMS if I don't.
                    "");
        }

        /**
         * Returns CMS UI HTML markup for the promotable image fallback when
         * it's not explicitly published in the promo data. Used for dynamic
         * ToolUi.NoteHtml.
         *
         * @param pageContext the servlet pageContext
         * @return CMS UI HTML
         * @throws java.io.IOException
         */
        public String getPlaceholderImageHtml(PageContext pageContext) throws IOException {

            if (image == null) {
                Image fallbackImage = getOriginalObject().getPromotableImageFallback();
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
