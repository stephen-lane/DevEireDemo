package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.db.Reference;

import com.deveiredemo.view.QuoteView;

@Recordable.DisplayName("Quote Enhancement")
@ViewMapping(QuoteView.FromQuoteEnhancement.class)
public class QuoteEnhancement extends Reference {

    private String text;

    private String source;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
