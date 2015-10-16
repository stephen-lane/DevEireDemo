package com.deveiredemo.view;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/jsg-scorecard-legend")
public interface ScorecardLegendView {

    List<Item> getLegendItems();

    interface Item {

        String getTitle();

        String getClassName();
    }
}
