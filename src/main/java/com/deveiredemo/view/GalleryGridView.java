package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.handlebars.HandlebarsTemplate;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.GalleryEnhancement;
import com.deveiredemo.model.GalleryEnhancementStyle;
import com.deveiredemo.model.GallerySlide;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.PhotoFallPage;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.view.base.BspImagePromoView;
import com.deveiredemo.view.base.BspLinkView;
import com.deveiredemo.view.base.BspOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@HandlebarsTemplate("components/jsg-gallery-grid")
public interface GalleryGridView extends ReferentialTextItemView.Content {

    String getGalleryTitle();

    String getGalleryCaption();

    String getGalleryGridClassName();

    List<? extends GalleryGridThumbView> getGalleryGridThumbs();

    List<? extends BspOption> getOptions();

    List<? extends GalleryGridSlideView> getGallerySlides();

    Boolean getShowImagesAsList();

    NextContentView getGalleryGridNextThumbs();

    public static List<? extends LinkView.ImageLink> createLowResGalleryGridThumbs(List<Image> images) {

        List<LinkView.ImageLink> thumbs = new ArrayList<>();

        // tall, small, small, tall, large, wide, small, small, large
        String[] sizes = new String[] {
                Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1
        };

        if (images.size() >= 9) {
            images = new ArrayList<>(images.subList(0, 9));

            for (int i = 0; i < 9; i++) {

                String size = sizes[i];
                Image image = images.get(i);

                thumbs.add(new LinkView.ImageLink() {
                    @Override
                    public ImageView getBody() {

                        if (image != null) {

                            StorageItem file = image.getFile();
                            if (file != null) {

                                return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                        .standardImageSize(ImageUtils.findStandardSize(size)), image);
                            }
                        }

                        return null;
                    }

                    @Override
                    public Map<String, String> getAttributes() {
                        return null;
                    }
                });
            }

            return thumbs;

        } else {
            LoggerFactory.getLogger(GalleryGridView.class).warn("Suppressing grid because there are less than 9 slides with images.");
        }

        return null;
    }

    static class FromGalleryEnhancement extends AbstractViewCreator<GalleryEnhancement> implements GalleryGridView {

        private static final Logger LOGGER = LoggerFactory.getLogger(FromGalleryEnhancement.class);

        /**
         * @return true if it's a low resolution gallery that should be displayed
         * inline in the article as opposed to linking off to the full screen gallery
         * experience.
         */
        private boolean isLowRes() {
            return GalleryEnhancementStyle.LOW_RES == model.getGalleryStyle();
        }

        @Override
        public String getGalleryTitle() {
            return model.getHeadlineOverride();
        }

        @Override
        public String getGalleryCaption() {
            return model.getDescriptionOverride();
        }

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-grid-embedded";
        }

        @Override
        public List<? extends LinkView.ImageLink> getGalleryGridThumbs() {

            List<LinkView.ImageLink> thumbs = new ArrayList<>();

            // tall, small, small, tall, large, wide, small, small, large
            String[] sizes = new String[] {
                    Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1
            };

            Gallery gallery = model.getGallery();
            if (gallery != null) {

                List<GallerySlide> slides = gallery.getSlides();
                if (slides.size() >= 9) {
                    slides = slides.subList(0, 9);

                    for (int i = 0; i < 9; i++) {

                        String size = sizes[i];
                        GallerySlide slide = slides.get(i);

                        thumbs.add(new LinkView.ImageLink() {
                            @Override
                            public ImageView getBody() {

                                Image image = slide.getImage();
                                if (image != null) {

                                    StorageItem file = image.getFile();
                                    if (file != null) {

                                        return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                                .standardImageSize(ImageUtils.findStandardSize(size)), image);
                                    }
                                }

                                return null;
                            }

                            @Override
                            public Map<String, String> getAttributes() {

                                if (!isLowRes()) { // link off to the full screen gallery.

                                    String href = LinkView.createHref(model.getGallery());

                                    if (href != null) {

                                        Image image = slide.getImage();
                                        if (image != null) {
                                            href = StringUtils.addQueryParameters(href,
                                                    GalleryFullscreenSlideView.SLIDE_ID_PARAMETER, image.getId());
                                        }

                                        return LinkView.createAttributes(href,
                                                "title", slide.getTitleOverride());

                                    }
                                }

                                return null;
                            }
                        });
                    }

                    return thumbs;

                } else {
                    LOGGER.warn("Suppressing grid because there are less than 9 slides with images.");
                }
            }

            return null;
        }

        @Override
        public List<? extends BspOption> getOptions() {
            if (isLowRes()) { // set the lowRes options.
                return Arrays.asList(BspOption.fromKeyValue("lowRes", true));
            } else {
                return null;
            }
        }

        @Override
        public List<? extends ListPromoView> getGallerySlides() {

            if (isLowRes()) { // load the embedded slides.

                Gallery gallery = model.getGallery();
                if (gallery != null) {
                    return gallery.getSlides().stream()
                            .map((slide) -> request.createView(ListPromoView.FromGallerySlideAsEnhancement.class, slide))
                            .filter((view) -> view != null)
                            .collect(Collectors.toList());
                }
            }

            return null;
        }

        @Override
        public Boolean getShowImagesAsList() {
            return false;
        }

        @Override
        public NextContentView getGalleryGridNextThumbs() {
            return null;
        }
    }

    abstract static class FromGallery extends AbstractViewCreator<Gallery> implements GalleryGridView {

        @Override
        public String getGalleryTitle() {
            return null;
        }

        @Override
        public String getGalleryCaption() {
            return null;
        }

        @Override
        public List<? extends BspOption> getOptions() {
            return null;
        }

        @Override
        public List<? extends BspImagePromoView> getGallerySlides() {
            return null;
        }

        @Override
        public Boolean getShowImagesAsList() {
            return null;
        }

        @Override
        public NextContentView getGalleryGridNextThumbs() {
            return null;
        }

        /**
         *
         * @param imageSizes
         * @return
         */
        protected List<GalleryGridThumbView> createGalleryGridThumbs(String[] imageSizes) {

            int validSizes = 0;
            for (String size : imageSizes) {
                if (!StringUtils.isBlank(size)) {
                    validSizes++;
                }
            }

            List<GallerySlide> slides = model.getSlides();
            int slidesSize = slides.size();

            List<GallerySlide> slidesFilled = new ArrayList<>(slides);

            if (slidesSize < validSizes) {

                int diff = validSizes - slidesSize;
                // fill the slides

                for (int i = 0; i < diff; i++) {
                    slidesFilled.add(slides.get(i % slides.size()));
                }
            }

            List<GalleryGridThumbView> thumbs = new ArrayList<>();

            Iterator<GallerySlide> slideIt = slidesFilled.iterator();

            for (String size : imageSizes) {

                if (StringUtils.isBlank(size)) {
                    thumbs.add(new ListPromoView() {
                        @Override
                        public LinkOrTextView getHeading() {
                            String title = DeviceDetect.getPromoTitle(request, model);
                            return (TextView) () -> title;
                        }

                        @Override
                        public Options getOptions() {
                            return null;
                        }

                        @Override
                        public List<? extends ItemView> getListItems() {
                            String description = ReferentialTextUtils.stripHtml(DeviceDetect.getPromoDescription(request, model));
                            return Arrays.asList(() -> Collections.singletonList((TextView) () -> description));
                        }

                        @Override
                        public CallToAction getCta() {
                            return new CallToAction() {

                                @Override
                                public Options getOptions() {
                                    return null;
                                }

                                @Override
                                public BspLinkView getContent() {
                                    return new LinkView.TextLink() {
                                        @Override
                                        public TextView getBody() {
                                            return () -> "Visit Gallery";
                                        }

                                        @Override
                                        public Map<String, String> getAttributes() {
                                            return LinkView.createAttributes(model,
                                                    "title", model.getPromotableData().getTitle());
                                        }
                                    };
                                }
                            };
                        }
                    });

                } else {
                    GallerySlide slide = slideIt.next();

                    thumbs.add(new LinkView.ImageLink() {
                        @Override
                        public ImageView getBody() {

                            Image image = slide.getImage();
                            if (image != null) {

                                StorageItem file = image.getFile();
                                if (file != null) {

                                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                            .standardImageSize(ImageUtils.findStandardSize(size)), image);
                                }
                            }

                            return null;
                        }

                        @Override
                        public Map<String, String> getAttributes() {
                            String href = LinkView.createHref(model);

                            if (href != null) {

                                Image image = slide.getImage();
                                if (image != null) {
                                    href = StringUtils.addQueryParameters(href,
                                            GalleryFullscreenSlideView.SLIDE_ID_PARAMETER, image.getId());
                                }

                                return LinkView.createAttributes(href,
                                        "title", model.getPromotableData().getTitle());

                            } else {
                                return null;
                            }
                        }
                    });
                }
            }

            return thumbs;
        }
    }

    static class FromGalleryWithLayout1 extends FromGallery {

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-page-grid jsg-gallery-page-grid-layout1";
        }

        @Override
        public List<GalleryGridThumbView> getGalleryGridThumbs() {
            return createGalleryGridThumbs(new String[] {
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    "", // promo goes here
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1
            });
        }
    }

    static class FromGalleryWithLayout2 extends FromGallery {

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-page-grid jsg-gallery-page-grid-layout2";
        }

        @Override
        public List<GalleryGridThumbView> getGalleryGridThumbs() {
            return createGalleryGridThumbs(new String[] {
                    Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                    "", // promo goes here
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE
            });
        }
    }

    static class FromGalleryWithLayout3 extends FromGallery {

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-page-grid jsg-gallery-page-grid-layout3";
        }

        @Override
        public List<GalleryGridThumbView> getGalleryGridThumbs() {
            return createGalleryGridThumbs(new String[] {
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    "", // promo goes here
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                    Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE
            });
        }
    }

    static class FromGalleryWithLayout4 extends FromGallery {

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-page-grid jsg-gallery-page-grid-layout4";
        }

        @Override
        public List<GalleryGridThumbView> getGalleryGridThumbs() {
            return createGalleryGridThumbs(new String[] {
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_TALL,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    "", // promo goes here
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,
                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,
                    Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1
            });
        }
    }

    static class FromPhotoFallPage extends AbstractViewCreator<PhotoFallPage> implements GalleryGridView {

        public static final String PAGE_PARAMETER = "p";

        public static final String[] IMAGE_SIZES_LAYOUT_1 = new String[] {
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 0
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,                // 1
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,                // 2
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 3
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 4
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 5
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 6
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 7
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,                // 8
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 9
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 10
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 11
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 12
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,                // 13
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 14
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 15
                Constants.STANDARD_IMAGE_SIZE_GRID_WIDE,                // 16
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 17
                Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1,    // 18
                Constants.STANDARD_IMAGE_SIZE_GRID_LARGE_SQUARE_1_1,    // 19
                Constants.STANDARD_IMAGE_SIZE_GRID_TALL,                // 20
        };

        public static final int QUERY_LIMIT = IMAGE_SIZES_LAYOUT_1.length;

        @Override
        public String getGalleryTitle() {
            return null;
        }

        @Override
        public String getGalleryCaption() {
            return null;
        }

        @Override
        public List<? extends BspOption> getOptions() {
            return null;
        }

        @Override
        public Boolean getShowImagesAsList() {
            return null;
        }

        @Override
        public List<? extends BspImagePromoView> getGallerySlides() {
            return null;
        }

        @Override
        public String getGalleryGridClassName() {
            return "jsg-gallery-photofall-grid jsg-gallery-photofall-layout1";
        }

        @Override
        public List<LinkView.ImageLink> getGalleryGridThumbs() {

            List<LinkView.ImageLink> galleryGridThumbs = new ArrayList<>();

            Query<Image> query = getPhotoFallQuery();

            PaginatedResult<Image> result = query.select(offset(), limit());

            Iterator<Image> imageIt = result.getItems().iterator();

            for (String imageSize : IMAGE_SIZES_LAYOUT_1) {

                if (imageIt.hasNext()) {
                    Image image = imageIt.next();

                    galleryGridThumbs.add(new LinkView.ImageLink() {

                        @Override
                        public ImageView getBody() {
                            StorageItem file = image.getFile();
                            if (file != null) {
                                return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                        .standardImageSize(ImageUtils.findStandardSize(imageSize)), image);
                            }

                            return null;
                        }

                        @Override
                        public Map<String, String> getAttributes() {
                            return LinkView.createAttributes(image);
                        }
                    });
                } else {
                    break;
                }
            }

            return galleryGridThumbs;
        }

        @Override
        public NextContentView getGalleryGridNextThumbs() {

            if (page() != pageLimit()) {
                return new NextContentView() {
                    @Override
                    public String getUrl() {
                        return StringUtils.addQueryParameters(Constants.AJAX_ENDPOINT_PHOTO_FALL,
                                PAGE_PARAMETER, page() + 1);
                    }

                    @Override
                    public String getLinkText() {
                        return "Load More";
                    }
                };

            } else {
                return null;
            }
        }

        private int page() {
            int p = request.getParameter(int.class, PAGE_PARAMETER).findFirst().orElse(1);
            if (p < 1) {
                p = 1;
            }
            if (p > pageLimit()) {
                p = pageLimit();
            }
            return p;
        }

        private int pageLimit() {
            return model.getMaxPages();
        }

        private long offset() {
            return (page() - 1) * limit();
        }

        private int limit() {
            return QUERY_LIMIT;
        }

        private Query<Image> getPhotoFallQuery() {
            return GalleryFullscreenSlideView.FromImage.getNextQuery(null, null);
        }
    }
}
