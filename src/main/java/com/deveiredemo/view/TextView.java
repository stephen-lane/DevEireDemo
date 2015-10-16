package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Text;
import com.deveiredemo.view.base.BspListPromoView;
import com.deveiredemo.view.base.BspTextView;

@FunctionalInterface
public interface TextView extends BspTextView, HtmlOrTextView, LinkOrTextView, BspListPromoView.ItemView.SectionView {

    static class FromText extends AbstractViewCreator<Text> implements TextView {

        @Override
        public String getText() {
            return model.getText();
        }
    }

    static TextView createWithModifier(String text, String modifier) {

        return new TextView() {

            @Override
            public String getText() {
                return text;
            }

            @Override
            public Options getOptions() {
                return () -> modifier;
            }
        };
    }
}
