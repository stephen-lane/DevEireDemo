package com.deveiredemo.view.base;

import com.deveiredemo.view.ScorecardLegendView;
import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-gallery-module-configurable")
public interface BspGalleryModuleConfigurableView {

    Options getOptions();

    String getGalleryTitle();

    List<Object> getGallerySlides();

    ScorecardLegendView getLegend();

    interface Options {

        Item getThemeConfig();

        String getModifierClass();

        interface Item {

            boolean getDots();

            boolean getArrows();
        }
    }

}
