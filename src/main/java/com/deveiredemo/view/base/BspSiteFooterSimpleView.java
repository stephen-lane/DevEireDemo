package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.view.SocialLinkView;

import java.util.List;

@HandlebarsTemplate("components/bsp-site-footer-simple")
public interface BspSiteFooterSimpleView extends BspFooterView {

    String getDisclaimer();

    Sharing getSharing();

    interface Sharing {

        String getSocial();

        List<SocialLinkView> getLinks();
    }
}
