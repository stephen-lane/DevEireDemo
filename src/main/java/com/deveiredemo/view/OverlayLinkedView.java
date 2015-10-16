package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.Map;

@HandlebarsTemplate("components/jsg-overlay-linked")
public interface OverlayLinkedView {

    OverlayLink getLink();

    interface OverlayLink {

        Map<String, String> getAttributes();

        String getDescription();

        String getCtaText();
    }
}
