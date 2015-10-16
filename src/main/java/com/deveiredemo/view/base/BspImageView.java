package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.Map;

/**
 * An img tag.
 */
@HandlebarsTemplate("common/image")
public interface BspImageView {

    /**
     * @return key/value pairs representing the attributes of the img tag.
     */
    Map<String, String> getAttributes();
}
