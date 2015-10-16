package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Link;
import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.Promo;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.model.Text;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.view.base.BspGalleryModuleView;
import com.deveiredemo.view.base.BspListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface PromoModuleSuperItemView extends ListPromoView, BspGalleryModuleView.Slide {

    @Override
    default Options getOptions() {
        return null;
    }

    @Override
    default Object getHeading() {
        return null;
    }

    @Override
    default CallToAction getCta() {
        return null;
    }

    static class FromPromotable extends AbstractViewCreator<Promotable> implements PromoModuleSuperItemView {

        @Override
        public List<? extends ItemView> getListItems() {

            return Collections.singletonList(() -> {

                List<PromoModuleSuperItemHandlerView> sections = new ArrayList<>();

                // image
                sections.add(new PromoModuleSuperItemHandlerImageView() {

                    @Override
                    public String getName() {
                        return "image";
                    }

                    @Override
                    public LinkView getImage() {

                        return new LinkView.ImageLink() {

                            @Override
                            public Map<String, String> getAttributes() {
                                return LinkView.createAttributes(model.getPromotableData().getLink());
                            }

                            @Override
                            public ImageView getBody() {

                                Image image = model.getPromotableData().getImage();
                                if (image != null) {

                                    StorageItem file = image.getFile();
                                    if (file != null) {

                                        ImageTagBuilder builder = new ImageTagBuilder(file);

                                        if (DeviceDetect.isMobile(request)) {
                                            builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                                    Constants.STANDARD_IMAGE_SIZE_PROMO_SUPER_MOBILE_8_5, 8, 5, false));
                                        } else {
                                            builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                                    Constants.STANDARD_IMAGE_SIZE_PROMO_SUPER_3_1, 3, 1, false));
                                        }

                                        builder.hideDimensions();

                                        return ImageView.fromImageTagBuilder(builder, image);
                                    }
                                }
                                return null;
                            }
                        };
                    }
                });

                // get the slug

                if (model instanceof Promo) {
                    LinkOrTextView slug;

                    LinkOrText linkOrText = ((Promo) model).getSlug();

                    if (linkOrText instanceof Link) {
                        slug = request.createView(LinkView.class, linkOrText);

                    } else if (linkOrText instanceof Text) {
                        slug = (TextView) ((Text) linkOrText)::getText;

                    } else {
                        slug = null;
                    }

                    if (slug != null) {
                        sections.add(new PromoModuleSuperItemHandlerSlugView() {

                            @Override
                            public String getName() {
                                return "title";
                            }

                            @Override
                            public LinkOrTextView getTitle() {

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

                                return slug;
                            }
                        });
                    }
                }

                sections.add(new PromoModuleSuperItemHandlerTitleDescriptionView() {

                    @Override
                    public String getName() {
                        return "text-below";
                    }

                    @Override
                    public LinkOrTextView getTitle() {
                        return new LinkView.TextLink() {
                            @Override
                            public Map<String, String> getAttributes() {
                                return LinkView.createAttributes(model.getPromotableData().getLink());
                            }

                            @Override
                            public TextView getBody() {
                                return (TextView) () -> DeviceDetect.getPromoTitle(request, model);
                            }
                        };
                    }

                    @Override
                    public String getDescription() {
                        return DeviceDetect.getPromoDescription(request, model);
                    }
                });

                if (model instanceof Promo) {
                    List<Link> links = ((Promo) model).getAdditionalLinks();

                    if (!links.isEmpty()) {

                        sections.add(new PromoModuleSuperItemHandlerLinksView() {
                            @Override
                            public String getName() {
                                return "bullets";
                            }

                            @Override
                            public BspListView getLink() {
                                return new BspListView() {
                                    @Override
                                    public Options getOptions() {
                                        return new Options() {
                                            public Boolean getUnstyled() {
                                                return true;
                                            }
                                        };
                                    }

                                    @Override
                                    public List<LinkView> getItems() {
                                        return links.stream()
                                                .map((link) -> request.createView(LinkView.class, link))
                                                .filter((linkView) -> linkView != null)
                                                .collect(Collectors.toList());
                                    }
                                };
                            }
                        });
                    }
                }

                return sections;
            });
        }
    }
}
