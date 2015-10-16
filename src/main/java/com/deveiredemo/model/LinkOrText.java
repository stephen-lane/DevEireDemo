package com.deveiredemo.model;

import com.psddev.dari.db.Recordable;

@Recordable.Embedded
public interface LinkOrText extends Recordable {

    String getText();
}
