package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.view.HtmlView;

import java.util.List;

@HandlebarsTemplate("components/bsp-gallery-fullscreen-slide")
public interface BspGalleryFullscreenSlideView {

    BspImageView getImage();

    List<HtmlView> getCaption();

    BspLinkView getSeoLink();

    BspImageView getThumbnail();

    String getSlideId();
}
