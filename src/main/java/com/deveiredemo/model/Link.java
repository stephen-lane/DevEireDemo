package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.LinkView;

@Recordable.Embedded
@ViewMapping(LinkView.FromLink.class)
public abstract class Link extends Content implements LinkOrText {

    private String text;

    @Override
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public abstract Object getHref();

    public abstract LinkTarget getTarget();
}
