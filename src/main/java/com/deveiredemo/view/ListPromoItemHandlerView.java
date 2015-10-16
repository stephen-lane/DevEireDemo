package com.deveiredemo.view;

import com.deveiredemo.view.base.BspListPromoItemHandlerView;

public interface ListPromoItemHandlerView extends BspListPromoItemHandlerView {

    LinkOrImageView getImage(); // common/image | common/link

    LinkOrTextView getTitle(); // common/link | common/text

    Object getLink(); // common/link | elements/list
}
