package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.view.base.BspHeaderView;

import java.util.List;

@HandlebarsTemplate("components/bsp-site-header-simple")
public interface BspSiteHeaderSimpleView extends BspHeaderView {

    String getSiteName();

    SiteLogoView getSiteLogo();

    List<LinkView> getNavLinks();

    interface SiteLogoView {

        LinkView getLink();
    }
}
