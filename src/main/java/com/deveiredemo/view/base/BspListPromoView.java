package com.deveiredemo.view.base;

import com.psddev.handlebars.HandlebarsTemplate;

import java.util.List;

@HandlebarsTemplate("components/bsp-list-promo")
public interface BspListPromoView {

    Options getOptions();

    Object getHeading();

    List<? extends ItemView> getListItems();

    CallToAction getCta();

    interface Options {

        default String getHorizontalSplit() {
            return null;
        }

        default String getModifierClass() {
            return null;
        }
    }

    interface ItemView {

        List<? extends SectionView> getSections();

        interface SectionView {
        }
    }

    interface CallToAction {

        Options getOptions();

        BspLinkView getContent();

        interface Options {
            String getLocation();
        }
    }
}
