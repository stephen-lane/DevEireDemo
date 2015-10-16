package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.SharedModule;

@HandlebarsTemplate("components/jsg-shared-module.hbs")
public interface SharedModuleView extends ModuleView {

    ModuleView getDelegate();

    static class FromSharedModule extends JsgViewCreator<SharedModule> implements SharedModuleView {

        @Override
        public ModuleView getDelegate() {
            return request.createView(ModuleView.class, model.getDelegate());
        }
    }
}
