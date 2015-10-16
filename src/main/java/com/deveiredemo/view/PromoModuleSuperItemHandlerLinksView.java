package com.deveiredemo.view;

public interface PromoModuleSuperItemHandlerLinksView extends PromoModuleSuperItemHandlerView {

    @Override
    default LinkOrImageView getImage() {
        return null;
    }

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
}
