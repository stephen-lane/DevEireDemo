package com.deveiredemo.view;

public interface PromoModuleSuperItemHandlerTitleDescriptionView extends PromoModuleSuperItemHandlerView {

    @Override
    default LinkOrImageView getImage() {
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
