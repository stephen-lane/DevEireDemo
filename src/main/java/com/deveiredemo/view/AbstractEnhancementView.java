package com.deveiredemo.view;

import com.psddev.cms.db.RichTextReference;
import com.psddev.dari.db.Reference;

/**
 * Base EnhancementView containing helper methods to get core metadata like
 * the alignment.
 */
public interface AbstractEnhancementView extends ReferentialTextItemView.Content {

    default String getAlignment(Reference reference) {
        return reference.as(RichTextReference.class).getAlignment();
    }
}
