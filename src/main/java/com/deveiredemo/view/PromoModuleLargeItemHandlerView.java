package com.deveiredemo.view;

public interface PromoModuleLargeItemHandlerView extends ListPromoItemHandlerView {

    @Override
    LinkOrImageWithOverlayView getImage();

    @Override
    default LinkOrTextView getTitle() {
        return null;
    }

    @Override
    default LinkView getLink() {
        return null;
    }

    @Override
    default String getDescription() {
        return null;
    }

    @Override
    default Options getOptions() {
        return null;
    }
}
