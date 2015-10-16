package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.view.base.BspImagePromoView;
import com.deveiredemo.view.base.BspLinkView;

import java.util.Collections;
import java.util.List;

public interface ImagePromoView extends BspImagePromoView {

    static class FromGolfTournamentImage extends AbstractViewCreator<com.deveiredemo.model.Image> implements ImagePromoView {
        @Override
        public Options getOptions() {
            return null;
        }

        @Override
        public String getHeading() {
            return null;
        }

        @Override
        public List<Image> getImages() {
            return Collections.singletonList(new Image() {
                @Override
                public BspLinkView getImage() {
                    return (LinkView.ImageOnly) () -> request.createView(ImageView.class, model);
                }

                @Override
                public List<TextView> getPromoLinks() {
                    return Collections.singletonList((TextView) () -> model.getPromotableData().getTitle());
                }
            });
        }

        @Override
        public CallToAction getCta() {
            return null;
        }
    }
}
