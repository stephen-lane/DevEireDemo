package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.view.HtmlView;
import com.deveiredemo.view.ScorecardLegendView;

import java.util.List;

@HandlebarsTemplate("components/bsp-table/bsp-table")
public interface BspTableView {

    Options getOptions();

    String getTitle();

    List<Heading> getHeadings();

    List<Row> getRows();

    ScorecardLegendView getLegend();

    CallToAction getCta();

    interface Options {

        String getModifierClass();
    }

    interface Heading {

        String getText();
    }

    interface Row {

        List<Cell> getRow();
    }

    interface Cell {
    }

    interface TextCell extends Cell {

        String getText();

        String getStatus();
    }

    interface CallToAction {

        Options getOptions();

        HtmlView getContent();

        interface Options {

            String getLocation();
        }
    }
}
