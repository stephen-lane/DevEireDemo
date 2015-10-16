package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

/**
 * Text, escaped to be HTML safe.
 */
@FunctionalInterface
@HandlebarsTemplate("common/text")
public interface BspTextView {

    /**
     * @return the text to render.
     */
    String getText();

    /**
     * @return options for the display of this text.
     */
    default Options getOptions() {
        return null;
    }

    static interface Options {

        String getModifierClass();
    }
}
