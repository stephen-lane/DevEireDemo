package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.GalleryEnhancement;
import com.deveiredemo.view.base.BspGalleryModuleView;
import com.deveiredemo.view.base.BspTextView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface GalleryModuleView extends BspGalleryModuleView,
                                           ReferentialTextItemView.Content {

    abstract static class AbstractGalleryModuleViewCreator<T> extends AbstractViewCreator<T> implements GalleryModuleView {

        @Override
        public Options getOptions() {
            return null;
        }

        @Override
        public List<Slide> getGallerySlides() {

            Gallery gallery = getGallery();
            if (gallery != null) {
                return gallery.getSlides().stream()
                        .map((slide) -> request.createView(Slide.class, slide))
                        .filter((slide) -> slide != null)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public CallToAction getCta() {
            return null;
        }

        private Gallery getGallery() {
            if (model instanceof Gallery) {
                return (Gallery) model;

            } else if (model instanceof GalleryEnhancement) {
                return ((GalleryEnhancement) model).getGallery();

            } else {
                return null;
            }
        }
    }

    static class FromGallery extends AbstractGalleryModuleViewCreator<Gallery> {

        @Override
        public BspTextView getGalleryTitle() {
            return model::getHeadline;
        }
    }

    static class FromGalleryEnhancement extends AbstractGalleryModuleViewCreator<GalleryEnhancement> {

        @Override
        public BspTextView getGalleryTitle() {

            String headlineOverride = model.getHeadlineOverride();
            if (headlineOverride != null) {
                return () -> headlineOverride;

            } else {
                Gallery gallery = model.getGallery();
                if (gallery != null) {
                    return gallery::getHeadline;
                }
            }

            return null;
        }
    }
}
