package com.deveiredemo.view;

public interface PromoModuleSuperItemHandlerImageView extends PromoModuleSuperItemHandlerView {

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
    default LinkView getLink() {
        return null;
    }
}
