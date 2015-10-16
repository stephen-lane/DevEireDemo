package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Section;
import com.deveiredemo.view.base.BspFullPageLayoutView;
import com.deveiredemo.view.base.BspMainView;

import java.util.stream.Collectors;

public interface SectionMainView extends BspFullPageLayoutView {

    static class FromSection extends AbstractViewCreator<Section> implements SectionMainView {

        @Override
        public Options getOptions() {
            return () -> null;
        }

        @Override
        public BspMainView getMain() {
            return () -> model.getModules().stream()
                    .map((module) -> request.createView(ModuleView.class, module))
                    .filter((view) -> view != null)
                    .collect(Collectors.toList());
        }
    }
}
