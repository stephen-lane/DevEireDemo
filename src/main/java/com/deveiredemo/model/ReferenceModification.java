package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Modification;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.ReferenceView;

/**
 * Modification of Reference solely so a ViewMapping can be associated with the type.
 */
@ViewMapping(ReferenceView.FromReference.class)
public class ReferenceModification extends Modification<Reference> {
}
