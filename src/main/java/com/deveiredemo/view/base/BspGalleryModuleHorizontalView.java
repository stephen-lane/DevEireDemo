package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-gallery-module-horizontal")
public interface BspGalleryModuleHorizontalView {

    Options getOptions();

    String getGalleryTitle();

    List<? extends BspLinkView> getGallerySlides();

    interface Options {

        String getModifierClass();

        String getSlidesToShow();

        String getSlidesToShowMobilePort();

        String getSlidesToShowMobile();
    }
}
