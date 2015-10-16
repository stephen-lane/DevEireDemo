package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.GallerySlide;
import com.deveiredemo.model.Image;
import com.deveiredemo.view.base.BspListPromoView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface ListPromoView extends BspListPromoView, GalleryGridThumbView, GalleryGridSlideView {

    static class FromGallerySlideAsEnhancement extends AbstractViewCreator<GallerySlide> implements ListPromoView {

        @Override
        public Options getOptions() {
            return null;
        }

        @Override
        public Object getHeading() {
            return null;
        }

        @Override
        public List<? extends ItemView> getListItems() {

            return Collections.singletonList(new ItemView() {

                public String getImgAlign() {
                    return "top";
                }

                @Override
                public List<? extends SectionView> getSections() {
                    return Arrays.asList(
                            new GallerySlideAsEnhancementItemHandlerImageView() {

                                @Override
                                public String getName() {
                                    return "image";
                                }

                                @Override
                                public LinkView getImage() {
                                    return (LinkView.ImageOnly) () -> {
                                        Image image = model.getImage();
                                        if (image != null) {

                                            StorageItem file = image.getFile();

                                            if (file != null) {
                                                return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                                        .srcAttribute("data-lazy")
                                                        .height(495), image);
                                            }
                                        }

                                        return null;
                                    };
                                }
                            }, new GallerySlideAsEnhancementItemHandlerDescriptionView() {

                                @Override
                                public String getName() {
                                    return "text";
                                }

                                // rich text
                                @Override
                                public String getDescription() {
                                    return model.getCaptionOverride();
                                }
                            });
                }
            });
        }

        @Override
        public CallToAction getCta() {
            return null;
        }
    }
}
