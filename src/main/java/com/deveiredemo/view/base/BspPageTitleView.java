package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/bsp-page-title")
public interface BspPageTitleView {

    BspTextView getPageTitle();
}
