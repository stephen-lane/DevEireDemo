package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.Map;

@HandlebarsTemplate("common/link")
public interface BspLinkView {

    String getCssClass();

    Map<String, String> getAttributes();

    Object getBody();
}
