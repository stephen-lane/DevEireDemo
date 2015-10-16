package com.deveiredemo.view;

import com.psddev.cms.db.CropOption;
import com.psddev.cms.db.StandardImageSize;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.handlebars.HandlebarsTemplate;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.ImageEnhancement;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.util.ReferentialTextUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@HandlebarsTemplate("components/jsg-caption")
public interface CaptionView {

    Options getOptions();

    ImageView getContent();

    TextView getText();

    TextView getCredit();

    interface Options {

        Integer getWidth();
    }

    static class FromImageEnhancement extends AbstractViewCreator<ImageEnhancement> implements CaptionView,
                                                                                               ReferentialTextItemView.Content {

        @Override
        public Options getOptions() {
            return () -> ObjectUtils.to(Integer.class, getImageAttributes().get("width"));
        }

        @Override
        public ImageView getContent() {
            return this::getImageAttributes;
        }

        @Override
        public TextView getText() {
            String caption = ReferentialTextUtils.stripHtml(model.getCaptionOverride());
            return !StringUtils.isBlank(caption) ? () -> caption : null;
        }

        @Override
        public TextView getCredit() {
            String credit = model.getCreditOverride();
            return credit != null ? () -> credit : null;
        }

        /**
         * DO NOT call {@link ImageTagBuilder#hideDimensions} in this
         * implementation since we need it in order to get the width attribute
         * value from it that's required by the Options.
         *
         * @return the Map of img tag attributes.
         */
        private Map<String, String> getImageAttributes() {

            Image image = model.getObject();
            if (image != null) {

                StorageItem file = image.getFile();
                if (file != null) {

                    Integer width = model.getWidth();
                    Integer height = model.getHeight();

                    ImageTagBuilder builder = new ImageTagBuilder(file);

                    if (ReferentialTextItemView.Alignment.fromReference(model) == ReferentialTextItemView.Alignment.FULL) {

                        builder.standardImageSize(ImageUtils.findStandardSize(
                                Constants.STANDARD_IMAGE_SIZE_ARTICLE_INLINE_LANDSCAPE_2_1));

                    } else {
                        builder.width(width);
                        builder.height(height);

                        if (model.isMaintainAspectRatio()) {
                            builder.cropOption(CropOption.NONE);

                        } else {
                            StandardImageSize standardSize = ImageUtils.findStandardSize(
                                    Constants.STANDARD_IMAGE_SIZE_ARTICLE_INLINE_PORTRAIT_1_2);

                            if (standardSize != null) {
                                int standardWidth = standardSize.getWidth();
                                int standardHeight = standardSize.getHeight();

                                if (width == null && height == null
                                        || (width != null && height != null && standardWidth > 0 && standardHeight > 0
                                        && ImageUtils.isRatioEquivalent(width, height, standardWidth, standardHeight))) {

                                    builder.standardImageSize(standardSize);
                                }
                            }
                        }
                    }

                    Map<String, String> attributes = new LinkedHashMap<>(builder.toAttributes());
                    String altText = image.getAltText();
                    if (altText != null) {
                        attributes.put("alt", altText);
                    }
                    return attributes;
                }
            }

            return Collections.emptyMap();
        }
    }
}
