package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-social-share")
public interface BspSocialShareView {

    String getSocial();

    boolean getMonochrome();

    String getTitle();

    String getDescription();

    Long getFacebookId();

    String getIconClass();

    String getImage();

    String getUrl();

    String getRedirectUrl();

    List<String> getServices();
}
