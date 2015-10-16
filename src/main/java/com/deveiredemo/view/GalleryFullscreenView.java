package com.deveiredemo.view;

import com.deveiredemo.model.GallerySlide;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.PhotoFallPage;
import com.deveiredemo.view.base.BspGalleryFullscreenView;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Query;

import com.deveiredemo.model.Gallery;
import com.deveiredemo.view.base.BspSocialShareView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface GalleryFullscreenView extends BspGalleryFullscreenView {

    static class FromGallery extends AbstractViewCreator<Gallery> implements GalleryFullscreenView {

        @Override
        public Options getOptions() {
            return new Options() {

                @Override
                public boolean getGalleryThumbnails() {
                    return true;
                }

                @Override
                public boolean getDynamicSlideLoad() {
                    return false;
                }

                @Override
                public boolean getDeepLinking() {
                    return true;
                }

                @Override
                public String getDeepLinkingQueryParam() {
                    return GalleryFullscreenSlideView.SLIDE_ID_PARAMETER;
                }
            };
        }

        @Override
        public BspSocialShareView getSharing() {
            return request.createView(BspSocialShareView.class, model);
        }

        @Override
        public String getGalleryTitle() {
            return model.getHeadline();
        }

        @Override
        public List<GalleryFullscreenSlideView> getGallerySlides() {
            List<GalleryFullscreenSlideView> slideViews = new ArrayList<>();
            List<GallerySlide> slides = model.getSlides();

            if (slides != null) {
                for (GallerySlide slide : slides) {
                    slideViews.add(request.createView(GalleryFullscreenSlideView.class, slide));
                }
            }
            return slideViews;
        }
    }

    static class FromImage extends AbstractViewCreator<Image> implements GalleryFullscreenView {

        @Override
        public Options getOptions() {
            return new Options() {

                @Override
                public boolean getGalleryThumbnails() {
                    return false;
                }

                @Override
                public boolean getDynamicSlideLoad() {
                    return true;
                }

                @Override
                public boolean getDeepLinking() {
                    return false;
                }

                @Override
                public String getDeepLinkingQueryParam() {
                    return null;
                }
            };
        }

        @Override
        public String getGalleryTitle() {

            PhotoFallPage photoFallPage = Query.from(PhotoFallPage.class).first();
            if (photoFallPage != null) {
                return photoFallPage.getTitle();
            }

            return null;
        }

        @Override
        public List<GalleryFullscreenSlideView> getGallerySlides() {
            return Collections.singletonList(request.createView(GalleryFullscreenSlideView.class, model));
        }

        @Override
        public BspSocialShareView getSharing() {
            return null;
        }
    }
}
