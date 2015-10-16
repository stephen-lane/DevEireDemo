package com.deveiredemo.view;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.GallerySlide;
import com.deveiredemo.model.Image;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.view.base.BspGalleryFullscreenSlideView;
import com.deveiredemo.view.base.BspImageView;
import com.deveiredemo.view.base.BspLinkView;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.CropOption;
import com.psddev.cms.db.Directory;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.ImageTagBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public interface GalleryFullscreenSlideView extends BspGalleryFullscreenSlideView {

    public static final String SLIDE_ID_PARAMETER = "slide";

    public static final int SLIDE_IMAGE_WIDTH = 1590;
    public static final int SLIDE_IMAGE_HEIGHT = 894;

    static class FromGallerySlide extends AbstractViewCreator<GallerySlide> implements GalleryFullscreenSlideView {

        @Override
        public BspImageView getImage() {
            Image slideImage = model.getImage();
            if (slideImage != null) {

                StorageItem file = slideImage.getFile();
                if (file != null) {

                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                            .width(SLIDE_IMAGE_WIDTH)
                            .height(SLIDE_IMAGE_HEIGHT)
                            .cropOption(CropOption.NONE), slideImage);
                }
            }
            return null;
        }

        @Override
        public List<HtmlView> getCaption() {
            String title = model.getTitleOverride();
            String caption = model.getCaptionOverride();

            if (title != null || caption != null) {
                return Collections.singletonList(() -> Arrays.asList(title, caption)
                        .stream()
                        .filter((text) -> text != null)
                        .collect(Collectors.joining(" - ")));
            }
            return null;
        }

        @Override
        public BspLinkView getSeoLink() {
            return null;
        }

        @Override
        public BspImageView getThumbnail() {
            Image thumbImage = model.getImage();
            if (thumbImage != null) {

                StorageItem file = thumbImage.getFile();
                if (file != null) {

                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                            .standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                    Constants.STANDARD_IMAGE_SIZE_GRID_SMALL_SQUARE_1_1, 1, 1, false)), thumbImage);
                }
            }
            return null;
        }

        @Override
        public String getSlideId() {
            Image image = model.getImage();
            return image != null ? image.getId().toString() : null;
        }
    }

    static class FromImage extends AbstractViewCreator<Image> implements GalleryFullscreenSlideView {

        public static final String IMAGE_ID_PARAMETER = "id";
        public static final String NEXT_IMAGE_ID_PARAMETER = "nid";
        public static final String PREVIOUS_IMAGE_ID_PARAMETER = "pid";

        @Override
        public BspImageView getImage() {

            Map<String, String> attributes = new ImageTagBuilder(model.getFile())
                    .width(SLIDE_IMAGE_WIDTH)
                    .height(SLIDE_IMAGE_HEIGHT)
                    .cropOption(CropOption.NONE)
                    .toAttributes();

            Image previousImage = Query.findById(Image.class,
                    request.getParameter(UUID.class, PREVIOUS_IMAGE_ID_PARAMETER).findFirst().orElse(null));
            if (previousImage == null) {
                previousImage = getPreviousImage(model);
            }

            Image nextImage = Query.findById(Image.class,
                    request.getParameter(UUID.class, NEXT_IMAGE_ID_PARAMETER).findFirst().orElse(null));
            if (nextImage == null) {
                nextImage = getNextImage(model);
            }

            attributes.put("data-url", model.getPermalink());

            if (previousImage != null) {
                attributes.put("data-prev-url",
                        StringUtils.addQueryParameters(Constants.AJAX_ENDPOINT_PHOTO_FALL_SLIDE,
                                IMAGE_ID_PARAMETER, previousImage.getId(),
                                NEXT_IMAGE_ID_PARAMETER, model.getId()));
            }

            if (nextImage != null) {
                attributes.put("data-next-url",
                        StringUtils.addQueryParameters(Constants.AJAX_ENDPOINT_PHOTO_FALL_SLIDE,
                                IMAGE_ID_PARAMETER, nextImage.getId(),
                                PREVIOUS_IMAGE_ID_PARAMETER, model.getId()));
            }

            String altText = model.getAltText();
            if (altText != null) {
                attributes.put("alt", altText);
            }

            return () -> attributes;
        }

        @Override
        public List<HtmlView> getCaption() {
            String caption;
            if (ReferentialTextUtils.isBlank(model.getCaption())) {
                caption = StringUtils.escapeHtml(model.getTitle());
            } else {
                caption = model.getCaption();
            }
            return Collections.singletonList(() -> caption);
        }

        @Override
        public BspLinkView getSeoLink() {
            return new LinkView.TextLink() {
                @Override
                public TextView getBody() {
                    return null;
                }

                @Override
                public Map<String, String> getAttributes() {
                    return LinkView.createAttributes(model, "aria-hidden", "true");
                }
            };
        }

        @Override
        public BspImageView getThumbnail() {
            return null;
        }

        @Override
        public String getSlideId() {
            return null;
        }

        /**
         * Returns a query suitable for fetching the next set of images as part
         * of the PhotoFall dynamic gallery. You can specify current image
         * information to get a query relative to that image, or just pass in
         * null to those arguments if you're managing the pagination through
         * offset and limit.
         *
         * @param currentId if fetching images immediately after another image, this is that image's ID.
         * @param currentPublishDate if fetching images immediately after another image, this is that image's publish date.
         * @return the query to fetch the next image(s).
         */
        public static Query<Image> getNextQuery(UUID currentId, Date currentPublishDate) {
            return getPreviousOrNextQuery(currentId, currentPublishDate, true);
        }

        private static Image getPreviousImage(Image current) {
            Image prev = null;

            Date publishDate = current.getPublishDate();
            if (publishDate != null) {
                prev = getPreviousQuery(current.getId(), publishDate).first();
                if (prev == null) {
                    prev = getLastImage(current.getId());
                }
            }

            return prev;
        }

        private static Image getNextImage(Image current) {
            Image next = null;

            Date publishDate = current.getPublishDate();
            if (publishDate != null) {
                next = getNextQuery(current.getId(), publishDate).first();
                if (next == null) {
                    next = getFirstImage(current.getId());
                }
            }

            return next;
        }

        private static Query<Image> getBaseQuery() {
            return Query.from(Image.class)
                    .where("* matches *")
                    .and(Directory.Static.hasPathPredicate())
                    .and("includeOnPhotoFall = ?", true);
        }

        private static Query<Image> getPreviousQuery(UUID currentId, Date currentPublishDate) {
            return getPreviousOrNextQuery(currentId, currentPublishDate, false);
        }

        private static Query<Image> getPreviousOrNextQuery(UUID currentId, Date currentPublishDate, boolean isNext) {

            Query<Image> query = getBaseQuery();

            if (currentId != null) {
                query.and("_id != ?", currentId);
            }

            if (currentPublishDate != null) {
                String op = isNext ? "<" : ">";

                query.and("((" + Content.PUBLISH_DATE_FIELD + " " + op + " ?0)"
                                + " OR (" + Content.PUBLISH_DATE_FIELD + " = ?0 && _id " + op + " ?1))",
                        currentPublishDate, currentId);

            } else {
                query.and(Content.PUBLISH_DATE_FIELD + " != missing");
            }

            if (isNext) {
                query.sortDescending(Content.PUBLISH_DATE_FIELD);
                query.sortDescending("_id");
            } else {
                query.sortAscending(Content.PUBLISH_DATE_FIELD);
                query.sortAscending("_id");
            }

            return query;
        }

        private static Image getFirstImage(UUID currentId) {
            return getNextQuery(currentId, null).first();
        }

        private static Image getLastImage(UUID currentId) {
            return getPreviousQuery(currentId, null).first();
        }
    }
}
