package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.TextView;

@ViewMapping(TextView.FromText.class)

@Recordable.Embedded
public class Text extends Content implements LinkOrText {

    private String text;

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
