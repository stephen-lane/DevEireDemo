package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Link;
import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.Promo;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.model.Text;
import com.deveiredemo.model.Video;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.util.ReferentialTextUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface PromoModuleMediumItemView extends ListPromoView.ItemView {

    @Override
    List<? extends PromoModuleMediumItemHandlerView> getSections();

    default List<PromoModuleMediumItemHandlerView> createSections(
            LinkOrTextView slug,
            LinkOrImageWithOverlayView imageWithDescription) {

        return Arrays.asList(
                new PromoModuleMediumItemHandlerTitleView() {

            @Override
            public String getName() {
                return "text-above";
            }

            @Override
            public LinkOrTextView getTitle() {
                return slug;
            }
        },
                new PromoModuleMediumItemHandlerImageView() {

            @Override
            public String getName() {
                return "image";
            }

            @Override
            public LinkOrImageWithOverlayView getImage() {
                return imageWithDescription;
            }
        });
    }

    static class FromPromotable extends AbstractViewCreator<Promotable> implements PromoModuleMediumItemView {

        @Override
        public List<? extends PromoModuleMediumItemHandlerView> getSections() {

            // get the slug
            LinkOrTextView slug = null;
            if (model instanceof Promo) {
                LinkOrText linkOrText = ((Promo) model).getSlug();

                if (linkOrText instanceof Link) {
                    slug = request.createView(LinkView.class, linkOrText);

                } else if (linkOrText instanceof Text) {
                    slug = (TextView) ((Text) linkOrText)::getText;
                }
            }

            // get the image with description overlay.
            LinkOrImageWithOverlayView imageWithDescription = null;
            Image image = model.getPromotableData().getImage();
            if (image != null) {

                StorageItem file = image.getFile();

                if (file != null) {

                    ImageTagBuilder builder = new ImageTagBuilder(file)
                            .standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                    Constants.STANDARD_IMAGE_SIZE_PROMO_MEDIUM_2_1, 2, 1, true))
                            .hideDimensions();

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

                    imageWithDescription = ImageOverlayView.fromImageTagBuilder(builder, overlay, image);
                }
            }

            return createSections(slug, imageWithDescription);
        }
    }
}
