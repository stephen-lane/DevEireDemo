package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-tabber")
public interface BspTabberView {

    String getModifierClass();

    List<Tab> getTabs();

    interface Tab {

        Options getOptions();

        String getLabel();

        List<Object> getComponent();

        interface Options {
            String getModifierClass();
        }
    }

}
