package com.deveiredemo.view;

public interface ErrorPageLinksView extends ListPromoItemHandlerView {

    default LinkOrImageView getImage() {
        return null;
    }

    default String getDescription() {
        return null;
    }

    default Options getOptions() {
        return null;
    }
}
