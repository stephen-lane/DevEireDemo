package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("elements/list")
public interface BspListView {

    Options getOptions();

    List<?> getItems();

    interface Options {

        default Boolean getOrdered() {
            return null;
        }

        default String getType() {
            return null;
        }

        default Boolean getUnstyled() {
            return null;
        }

        default Boolean getInline() {
            return null;
        }

        default String getModifierClass() {
            return null;
        }
    }
}
