package com.deveiredemo.view;

public interface ErrorPageImageView extends ListPromoItemHandlerView {

    default LinkOrTextView getTitle() {
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
