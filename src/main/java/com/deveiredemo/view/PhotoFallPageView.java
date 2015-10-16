package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.PhotoFallPage;
import com.deveiredemo.view.base.BspMainView;
import com.deveiredemo.view.base.BspOneColumnLayoutView;

import java.util.Collections;

public interface PhotoFallPageView extends BspOneColumnLayoutView {

    static class FromPhotoFallPage extends AbstractViewCreator<PhotoFallPage> implements PhotoFallPageView {

        @Override
        public Options getOptions() {
            return () -> null;
        }

        @Override
        public BspMainView getMain() {
            return () -> Collections.singletonList(request.createView(GalleryGridView.FromPhotoFallPage.class, model));
        }
    }
}
