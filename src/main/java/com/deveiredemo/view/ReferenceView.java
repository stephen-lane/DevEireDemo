package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Reference;
import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("common/reference")
public interface ReferenceView extends ReferentialTextItemView.Content {

    ReferentialTextItemView.Content getEnhancement();

    public static class FromReference extends AbstractViewCreator<Reference> implements ReferenceView {

        @Override
        public ReferentialTextItemView.Content getEnhancement() {
            return request.createView(ReferentialTextItemView.Content.class, model.getObject());
        }
    }
}
