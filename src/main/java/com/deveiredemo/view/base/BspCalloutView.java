package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/bsp-callout")
public interface BspCalloutView {

    String getStyle();

    BspCalloutTextView getCalloutText();

    BspLinkView getCta();
}
