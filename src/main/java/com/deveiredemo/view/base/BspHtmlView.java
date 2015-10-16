package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

/**
 * Renders pre-formatted HTML.
 */
@FunctionalInterface
@HandlebarsTemplate("common/html")
public interface BspHtmlView {

    /**
     * @return pre-formatted HTML.
     */
    String getText();
}
