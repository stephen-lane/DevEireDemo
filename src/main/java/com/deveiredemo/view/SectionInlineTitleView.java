package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.InlineTitleModule;

@HandlebarsTemplate("components/jsg-section-inline-title")
public interface SectionInlineTitleView extends ModuleView {

    Options getOptions();

    TextView getTitle();

    TextView getContent();

    interface Options {

        String getModifierClass();
    }

    static class FromInlineTitleModule extends AbstractViewCreator<InlineTitleModule> implements SectionInlineTitleView {

        @Override
        public Options getOptions() {
            return () -> model.getBackgroundTheme().getCssClass();
        }

        @Override
        public TextView getTitle() {
            return model::getTitle;
        }

        @Override
        public TextView getContent() {
            return model::getDescription;
        }
    }
}
