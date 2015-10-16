package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

@HandlebarsTemplate("components/jsg-next-content")
public interface NextContentView {

    String getUrl();

    String getLinkText();
}
