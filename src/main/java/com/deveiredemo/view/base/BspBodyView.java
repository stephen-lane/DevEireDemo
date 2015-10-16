package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("common/body")
public interface BspBodyView {

    BspHeaderView getHeaderView();

    BspLayoutView getMainView();

    BspFooterView getFooterView();
}
