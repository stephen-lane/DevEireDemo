package com.deveiredemo.view;

import com.psddev.cms.db.RichTextReference;
import com.psddev.dari.db.Reference;

import com.deveiredemo.model.Constants;

/**
 * The view for each item in a ReferentialText object.
 */
public interface ReferentialTextItemView {

    /**
     * @return the options for this item view.
     */
    Options getOptions();

    /**
     * @return the content view to render for this item.
     */
    Content getContent();

    /**
     * Set of options for how each enhancement should be rendered.
     */
    interface Options {

        /**
         * @return true if this item is an enhancement, false if it's just text/html.
         */
        boolean getEnhancement();

        /**
         * @return the alignment
         */
        Alignment getAlign();
    }

    /**
     * The content to render (text or enhancement)
     */
    interface Content {
    }

    /**
     * The alignment of an enhancement.
     */
    enum Alignment {

        LEFT("left"),
        RIGHT("right"),
        FULL("full");

        private String attributeSuffix;

        private Alignment(String attributeSuffix) {
            this.attributeSuffix = attributeSuffix;
        }

        /**
         * @return the "align-*" attribute suffix used when rendering enhancements.
         */
        public String getAttributeSuffix() {
            return attributeSuffix;
        }

        @Override
        public String toString() {
            return getAttributeSuffix();
        }

        /**
         * Gets the alignment of a reference.
         *
         * @param reference the reference containing alignment data.
         * @return the alignment of the reference.
         */
        public static Alignment fromReference(Reference reference) {

            String alignment = reference.as(RichTextReference.class).getAlignment();
            if (alignment == null) {
                alignment = Constants.RICH_TEXT_ALIGNMENT_FULL;
            }

            switch (alignment) {
                case Constants.RICH_TEXT_ALIGNMENT_LEFT:
                    return Alignment.LEFT;

                case Constants.RICH_TEXT_ALIGNMENT_RIGHT:
                    return Alignment.RIGHT;

                case Constants.RICH_TEXT_ALIGNMENT_FULL:
                    return Alignment.FULL;

                default:
                    return null;
            }
        }
    }

    /**
     * The default options for rendering text.
     */
    static final class HtmlOptions implements Options {

        @Override
        public final boolean getEnhancement() {
            return false;
        }

        @Override
        public final Alignment getAlign() {
            return null;
        }
    }
}
