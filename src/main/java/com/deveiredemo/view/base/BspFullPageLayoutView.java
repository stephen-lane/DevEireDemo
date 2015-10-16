package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("layouts/full-page")
public interface BspFullPageLayoutView extends BspLayoutView {

    BspMainView getMain();
}
