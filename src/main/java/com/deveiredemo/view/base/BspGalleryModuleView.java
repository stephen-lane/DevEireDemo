package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-gallery-module")
public interface BspGalleryModuleView {

    Options getOptions();

    BspTextView getGalleryTitle();

    List<? extends Slide> getGallerySlides();

    CallToAction getCta();

    interface Options {

        String getModifierClass();

        boolean isCarouselDots();
    }

    interface Slide {
    }

    interface CallToAction {

        Options getOptions();

        // TODO: Figure out what kinds of content can go here.
        Object getContent();

        interface Options {

            String getLocation();
        }
    }
}
