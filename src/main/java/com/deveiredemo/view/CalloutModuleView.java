package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.CalloutModule;
import com.deveiredemo.model.Link;
import com.deveiredemo.view.base.BspCalloutAccentView;
import com.deveiredemo.view.base.BspCalloutTextView;
import com.deveiredemo.view.base.BspCalloutView;
import com.deveiredemo.view.base.BspLinkView;
import com.deveiredemo.view.base.BspTextView;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public interface CalloutModuleView extends BspCalloutView, ModuleView {

    static class FromCalloutModule extends AbstractViewCreator<CalloutModule> implements CalloutModuleView {

        @Override
        public String getStyle() {
            return getInlineCssStyles(
                    getCssDeclaration("background", model.getBackgroundColor()));
        }

        @Override
        public BspCalloutTextView getCalloutText() {
            return new BspCalloutTextView() {

                @Override
                public BspTextView getPlainText() {
                    return model::getTitle;
                }

                @Override
                public BspCalloutAccentView getAccentText() {
                    return new BspCalloutAccentView() {

                        @Override
                        public String getStyle() {
                            return getInlineCssStyles(
                                    getCssDeclaration("color", model.getTextColor()));
                        }

                        @Override
                        public BspTextView getBody() {
                            return model::getAccentTitle;
                        }
                    };
                }
            };
        }

        @Override
        public BspLinkView getCta() {

            Link link = model.getLink();
            if (link != null) {
                return new LinkView() {

                    @Override
                    public String getCssClass() {
                        return null;
                    }

                    @Override
                    public Map<String, String> getAttributes() {

                        Link link = model.getLink();
                        if (link != null) {
                            return LinkView.createAttributes(link.getHref(),
                                    "style", getInlineCssStyles(
                                            getCssDeclaration("color", model.getButtonTextColor()),
                                            getCssDeclaration("background", model.getButtonColor())),
                                    "title", getText());
                        }

                        return null;
                    }

                    @Override
                    public TextView getBody() {
                        String text = getText();
                        return text != null ? () -> text : null;
                    }

                    private String getText() {
                        Link link = model.getLink();
                        return link != null ? link.getText() : null;
                    }
                };
            } else {
                return null;
            }
        }

        private String getInlineCssStyles(String... cssDeclarations) {
            return Arrays.stream(cssDeclarations)
                    .filter((i) -> i != null)
                    .collect(Collectors.joining(" "));
        }

        private String getCssDeclaration(String name, String value) {
            if (name != null && value != null) {
                return name + ": " + value + ";";
            } else {
                return null;
            }
        }
    }
}
