package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("layouts/one-col")
public interface BspOneColumnLayoutView extends BspLayoutView {

    BspMainView getMain();
}
