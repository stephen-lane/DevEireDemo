package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.QuoteEnhancement;

@HandlebarsTemplate("components/jsg-blockquote")
public interface QuoteView extends ReferentialTextItemView.Content {

    String getContent();

    String getAuthor();

    static class FromQuoteEnhancement extends AbstractViewCreator<QuoteEnhancement> implements QuoteView {

        @Override
        public String getContent() {
            return model.getText();
        }

        @Override
        public String getAuthor() {
            return model.getSource();
        }
    }
}
