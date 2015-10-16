package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/jsg-section-title")
public interface SectionTitleView {

    Options getOptions();

    TextView getTitle();

    interface Options {

        String getModifierClass();

    }

}
