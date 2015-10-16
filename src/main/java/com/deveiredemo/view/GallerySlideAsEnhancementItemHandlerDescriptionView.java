package com.deveiredemo.view;

import com.deveiredemo.view.base.BspListPromoItemHandlerView;

public interface GallerySlideAsEnhancementItemHandlerDescriptionView extends ListPromoItemHandlerView {

    @Override
    default LinkOrImageView getImage() {
        return null;
    }

    @Override
    default LinkOrTextView getTitle() {
        return null;
    }

    @Override
    default BspListPromoItemHandlerView.Options getOptions() {
        return null;
    }

    @Override
    default Object getLink() {
        return null;
    }
}
