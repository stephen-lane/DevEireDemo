package com.deveiredemo.view;

public interface PromoModuleSuperItemHandlerSlugView extends PromoModuleSuperItemHandlerView {

    default LinkOrImageView getImage() {
        return null;
    }

    default String getDescription() {
        return null;
    }

    default Options getOptions() {
        return null;
    }

    default LinkView getLink() {
        return null;
    }
}
