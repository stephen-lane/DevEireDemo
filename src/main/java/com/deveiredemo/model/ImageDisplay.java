package com.deveiredemo.model;

import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Modification;

/**
 * Contains view related properties for images, thus the reason for the
 * separation out to a different class.
 */
public class ImageDisplay extends Modification<Image> {

    public static final String TAB_NAME = "Display";

    @Indexed
    @DisplayName("Show On Photo Fall?")
    @ToolUi.Tab(TAB_NAME)
    private boolean includeOnPhotoFall;

    @Indexed
    @DisplayName("Show On Tag Pages?")
    @ToolUi.Tab(TAB_NAME)
    private boolean includeOnTagPage;

    public boolean isIncludeOnPhotoFall() {
        return includeOnPhotoFall;
    }

    public boolean isIncludeOnTagPage() {
        return includeOnTagPage;
    }
}
