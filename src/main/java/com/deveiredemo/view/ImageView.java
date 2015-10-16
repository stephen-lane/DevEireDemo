package com.deveiredemo.view;

import com.psddev.cms.db.StandardImageSize;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Image;
import com.deveiredemo.view.base.BspImageView;

import java.util.HashMap;
import java.util.Map;

public interface ImageView extends BspImageView, LinkOrImageView, ReferentialTextItemView.Content, ArticleLeadImageFirstView.LeadImage {

    static ImageView fromImageTagBuilder(ImageTagBuilder builder, Image image) {
        return fromImageTagBuilder(builder, image, null);
    }

    static ImageView fromImageTagBuilder(ImageTagBuilder builder, Image image, Float scale) {
        String altText = null;
        if (image != null) {
            altText = image.getAltText();
        }
        return fromImageTagBuilder(builder, altText, scale);
    }

    static ImageView fromImageTagBuilder(ImageTagBuilder builder, String altText) {
        return fromImageTagBuilder(builder, altText, null);
    }

    static ImageView fromImageTagBuilder(ImageTagBuilder builder, String altText, Float scale) {

        boolean scaled = false;

        if (scale != null) {

            Integer width = builder.getWidth();
            Integer height = builder.getHeight();

            if (width != null || height != null) {

                if (width != null) {
                    builder.width(Math.round(width * scale));
                    scaled = true;
                }

                if (height != null) {
                    builder.height(Math.round(height * scale));
                    scaled = true;
                }

            } else {
                StandardImageSize sis = builder.getStandardImageSize();
                if (sis != null) {

                    int sisWidth = sis.getWidth();
                    int sisHeight = sis.getHeight();

                    if (sisWidth > 0 || sisHeight > 0) {

                        if (sisWidth > 0) {
                            builder.width(Math.round(sisWidth * scale));
                            scaled = true;
                        }

                        if (sisHeight > 0) {
                            builder.height(Math.round(sisHeight * scale));
                            scaled = true;
                        }
                    }
                }
            }
        }

        Map<String, String> attributes = new HashMap<>(builder.toAttributes());

        if (scaled && !builder.isHideDimensions()) {

            Integer width = ObjectUtils.to(Integer.class, attributes.get("width"));
            Integer height = ObjectUtils.to(Integer.class, attributes.get("height"));

            // reset the calculated width and height to be what it would have
            // been had we not scaled the image.
            if (width != null || height != null) {

                if (width != null) {
                    attributes.put("width", String.valueOf(Math.round((float) width / scale)));
                }

                if (height != null) {
                    attributes.put("height", String.valueOf(Math.round((float) height / scale)));
                }
            }
        }

        if (altText != null) {
            attributes.put("alt", altText);
        }

        return () -> attributes;
    }

    /**
     * The most basic ViewCreator for an ImageView which simply outputs the
     * image src attribute with the image's raw URL.
     */
    static class FromImage extends AbstractViewCreator<Image> implements ImageView {

        @Override
        public Map<String, String> getAttributes() {
            Map<String, String> attributes = new ImageTagBuilder(model.getFile()).toAttributes();
            String altText = model.getAltText();
            if (altText != null && !attributes.isEmpty()) {
                attributes.put("alt", altText);
            }
            return attributes;
        }
    }
}
