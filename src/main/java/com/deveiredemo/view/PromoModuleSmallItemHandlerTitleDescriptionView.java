package com.deveiredemo.view;

import com.deveiredemo.view.base.BspListPromoItemHandlerView;

public interface PromoModuleSmallItemHandlerTitleDescriptionView extends PromoModuleSmallItemHandlerView {

    @Override
    LinkOrTextView getTitle();

    @Override
    String getDescription();

    @Override
    default LinkOrImageWithOverlayView getImage() {
        return null;
    }

    @Override
    default LinkView getLink() {
        return null;
    }

    @Override
    default BspListPromoItemHandlerView.Options getOptions() {
        return null;
    }
}
