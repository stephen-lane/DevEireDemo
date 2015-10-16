package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Promo;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.model.Video;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.util.ReferentialTextUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface PromoModuleLargeItemView extends ListPromoView.ItemView {

    @Override
    List<? extends PromoModuleLargeItemHandlerView> getSections();

    static class FromPromotable extends AbstractViewCreator<Promotable> implements PromoModuleLargeItemView {

        @Override
        public List<? extends PromoModuleLargeItemHandlerView> getSections() {
            return Collections.singletonList(new PromoModuleLargeItemHandlerView() {

                @Override
                public String getName() {
                    return "image";
                }

                @Override
                public LinkOrImageWithOverlayView getImage() {

                    Image image = model.getPromotableData().getImage();
                    if (image != null) {

                        StorageItem file = image.getFile();

                        if (file != null) {

                            ImageTagBuilder builder = new ImageTagBuilder(file);

                            if (DeviceDetect.isMobile(request)) {
                                builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                        Constants.STANDARD_IMAGE_SIZE_PROMO_LARGE_MOBILE_8_5, 8, 5, true));
                            } else {
                                builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                        Constants.STANDARD_IMAGE_SIZE_PROMO_LARGE_7_2, 7, 2, true));
                            }

                            builder.hideDimensions();

                            String description = ReferentialTextUtils.stripHtml(DeviceDetect.getPromoDescription(request, model));

                            OverlayLinkedView overlay = null;
                            if (!StringUtils.isBlank(description)) {
                                overlay = () -> new OverlayLinkedView.OverlayLink() {

                                    @Override
                                    public Map<String, String> getAttributes() {
                                        return LinkView.createAttributes(model.getPromotableData().getLink());
                                    }

                                    @Override
                                    public String getDescription() {
                                        return description;
                                    }

                                    @Override
                                    public String getCtaText() {

                                        String cta = null;

                                        if (model instanceof Promo) {
                                            cta = ((Promo) model).getCallToActionText();
                                        }

                                        if (cta == null) {
                                            Promotable ref = model.getPromotableReference();

                                            if (ref instanceof Article) {
                                                cta = "Read More";
                                            } else if (ref instanceof Video) {
                                                cta = "Watch";
                                            } else if (ref instanceof Image || ref instanceof Gallery) {
                                                cta = "View";
                                            }
                                        }

                                        return cta;
                                    }
                                };
                            }

                            return ImageOverlayView.fromImageTagBuilder(builder, overlay, image);
                        }
                    }

                    return null;
                }
            });
        }
    }
}
