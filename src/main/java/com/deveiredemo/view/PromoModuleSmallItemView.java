package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.util.ImageUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface PromoModuleSmallItemView extends ListPromoView.ItemView {

    @Override
    List<? extends PromoModuleSmallItemHandlerView> getSections();

    default List<PromoModuleSmallItemHandlerView> createSections(
            LinkOrTextView title,
            LinkOrImageView image,
            String description) {

        // Image is required for this promo.
        if (image != null) {
            return Arrays.asList(
                    new PromoModuleSmallItemHandlerImageView() {

                        @Override
                        public String getName() {
                            return "image";
                        }

                        @Override
                        public LinkOrImageView getImage() {
                            return image;
                        }
                    },
                    new PromoModuleSmallItemHandlerTitleDescriptionView() {

                        @Override
                        public String getName() {
                            return "text-below";
                        }

                        @Override
                        public LinkOrTextView getTitle() {
                            return title;
                        }

                        @Override
                        public String getDescription() {
                            return description;
                        }
                    });
        } else {
            return null;
        }
    }

    static class FromPromotable extends AbstractViewCreator<Promotable> implements PromoModuleSmallItemView {

        @Override
        public List<? extends PromoModuleSmallItemHandlerView> getSections() {

            // get the title
            LinkView titleView = new LinkView.TextLink() {
                @Override
                public TextView getBody() {
                    return () -> DeviceDetect.getPromoTitle(request, model);
                }

                @Override
                public Map<String, String> getAttributes() {
                    return LinkView.createAttributes(model.getPromotableData().getLink());
                }
            };

            // get the image, linked.
            LinkOrImageView imageView = null;

            Image image = model.getPromotableData().getImage();
            if (image != null) {

                StorageItem file = image.getFile();

                if (file != null) {

                    ImageTagBuilder builder = new ImageTagBuilder(file)
                            .standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                    Constants.STANDARD_IMAGE_SIZE_PROMO_SMALL_2_1, 2, 1, true))
                            //.width(370)
                            //.height(185)
                            .hideDimensions();

                    imageView = new LinkView.ImageLink() {
                        @Override
                        public ImageView getBody() {
                            return ImageView.fromImageTagBuilder(builder, image);
                        }

                        @Override
                        public Map<String, String> getAttributes() {
                            return LinkView.createAttributes(model.getPromotableData().getLink());
                        }
                    };
                }
            }

            // get the description
            String description = DeviceDetect.getPromoDescription(request, model);

            return createSections(titleView, imageView, description);
        }
    }
}
