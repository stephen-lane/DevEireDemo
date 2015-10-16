package com.deveiredemo.view;

public interface PromoModuleMediumItemHandlerTitleView extends PromoModuleMediumItemHandlerView {

    @Override
    LinkOrTextView getTitle();

    @Override
    default String getDescription() {
        return null;
    }

    @Override
    default LinkOrImageWithOverlayView getImage() {
        return null;
    }

    @Override
    default LinkView getLink() {
        return null;
    }

    @Override
    default Options getOptions() {
        return null;
    }
}
