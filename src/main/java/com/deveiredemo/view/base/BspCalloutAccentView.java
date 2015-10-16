package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/bsp-callout-accent")
public interface BspCalloutAccentView {

    String getStyle();

    BspTextView getBody();
}
