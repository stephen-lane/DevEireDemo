package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/bsp-list-promo-item-handler")
public interface BspListPromoItemHandlerView extends BspListPromoView.ItemView.SectionView {

    String getName();

    Object getImage(); // common/image | common/link

    Object getTitle(); // common/link | common/text

    String getDescription();

    Options getOptions();

    Object getLink(); // common/link | elements/list

    interface Options {

        boolean getTruncate();

        Integer getMaxLines();
    }
}
