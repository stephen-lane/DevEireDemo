package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.view.GalleryGridSlideView;

import java.util.List;

@HandlebarsTemplate("components/bsp-image-promo")
public interface BspImagePromoView extends GalleryGridSlideView {

    Options getOptions();

    String getHeading();

    List<Image> getImages();

    CallToAction getCta();

    interface Options {

        String getModifierClass();

        boolean isHorizontalSplit();
    }

    interface Image {

        BspLinkView getImage();

        // Originally this was a List<? extends BspLinkView> but had to make it
        // just object to support JSPIETH-606 where rich text needed to go in
        // the field and thus could not be wrapped inside of a BspLinkView,
        // since the current common/link.hbs implementation ALWAYS prints the
        // anchor tag even if there are no attributes; something we will change
        // post launch probably.
        List<?> getPromoLinks();
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
