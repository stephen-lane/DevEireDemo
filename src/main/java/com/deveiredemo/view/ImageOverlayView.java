package com.deveiredemo.view;

import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Image;

import java.util.Map;

public interface ImageOverlayView extends ImageView,
                                          LinkOrImageView,
                                          LinkOrImageWithOverlayView {

    @Override
    OverlayLinkedView getOverlayLinked();

    /**
     * Creates an ImageView from an ImageTagBuilder.
     *
     * @param builder the
     * @param overlayLinkedView the view to be overlayed on the image.
     * @return an ImageView.
     */
    static ImageOverlayView fromImageTagBuilder(ImageTagBuilder builder, OverlayLinkedView overlayLinkedView, Image image) {
        return new ImageOverlayView() {

            @Override
            public OverlayLinkedView getOverlayLinked() {
                return overlayLinkedView;
            }

            @Override
            public Map<String, String> getAttributes() {

                Map<String, String> attributes = builder.toAttributes();

                String altText = null;
                if (image != null) {
                    altText = image.getAltText();
                }

                if (altText != null) {
                    attributes.put("alt", altText);
                }

                return attributes;
            }
        };
    }
}
