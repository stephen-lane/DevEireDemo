package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Gallery;
import com.deveiredemo.view.base.BspMainView;
import com.deveiredemo.view.base.BspOneColumnLayoutView;

import java.util.Arrays;
import java.util.Collections;

public interface GalleryMainView extends BspOneColumnLayoutView {

    static class FromGallery extends AbstractViewCreator<Gallery> implements GalleryMainView {

        @Override
        public Options getOptions() {
            return () -> null;
        }

        @Override
        public BspMainView getMain() {
            return () -> {
                Object mainView = request.createView(GalleryFullscreenView.class, model);
                return mainView != null ? Arrays.asList(mainView) : Collections.emptyList();
            };
        }
    }
}
