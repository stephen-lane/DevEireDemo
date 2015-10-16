package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.dari.util.StorageItem;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.ErrorPage;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Link;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.view.base.BspListPromoView;
import com.deveiredemo.view.base.BspListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

public interface ErrorPageView extends BspListPromoView {

    static class FromErrorPage extends AbstractViewCreator<ErrorPage> implements ErrorPageView {

        private Integer getErrorStatusCode() {

            HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
            if (request != null) {
                return ObjectUtils.to(Integer.class, request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE));
            }

            return null;
        }

        @Override
        public Options getOptions() {
            return new Options() {
                @Override
                public String getModifierClass() {
                    return "error-jsg";
                }
            };
        }

        @Override
        public TextView getHeading() {
            return () -> model.getTitleByStatusCode(getErrorStatusCode());
        }

        @Override
        public List<? extends ItemView> getListItems() {

            String subTitle = model.getSubTitleByStatusCode(getErrorStatusCode());
            Image image = model.getImageByStatusCode(getErrorStatusCode());
            List<Link> links = model.getLinksByStatusCode(getErrorStatusCode());

            return Collections.singletonList(() -> {
                List<ItemView.SectionView> sections = new ArrayList<>();

                // image section
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

                        sections.add(new ErrorPageImageView() {
                            @Override
                            public String getName() {
                                return "image";
                            }

                            @Override
                            public LinkOrImageView getImage() {
                                return ImageView.fromImageTagBuilder(builder, image);
                            }
                        });
                    }
                }

                // links section
                sections.add(new ErrorPageLinksView() {

                    @Override
                    public String getName() {
                        return "text";
                    }

                    @Override
                    public LinkOrTextView getTitle() {
                        return (TextView) () -> subTitle;
                    }

                    @Override
                    public BspListView getLink() {
                        return new BspListView() {
                            @Override
                            public Options getOptions() {
                                return new Options() {
                                    @Override
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

                return sections;
            });
        }

        @Override
        public CallToAction getCta() {
            return null;
        }
    }
}
