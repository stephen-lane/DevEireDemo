package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-component-group")
public interface BspComponentGroupView extends BspHeaderView {

    default Options getOptions() {
        return null;
    }

    List<Object> getComponents();

    interface Options {

        String getModifierClass();
    }
}
