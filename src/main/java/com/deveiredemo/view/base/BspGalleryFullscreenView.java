package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-gallery-fullscreen")
public interface BspGalleryFullscreenView {

    Options getOptions();

    String getGalleryTitle();

    List<? extends BspGalleryFullscreenSlideView> getGallerySlides();

    BspSocialShareView getSharing();

    interface Options {

        boolean getGalleryThumbnails();

        boolean getDynamicSlideLoad();

        boolean getDeepLinking();

        String getDeepLinkingQueryParam();
    }
}
