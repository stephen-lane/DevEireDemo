package com.deveiredemo.view;

public interface GallerySlideAsEnhancementItemHandlerImageView extends ListPromoItemHandlerView {

    @Override
    default LinkOrTextView getTitle() {
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

    @Override
    default Object getLink() {
        return null;
    }
}
