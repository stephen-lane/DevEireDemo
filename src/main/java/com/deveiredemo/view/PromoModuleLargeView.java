package com.deveiredemo.view;

import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.ModuleBackgroundTheme;
import com.deveiredemo.view.base.BspComponentGroupView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface PromoModuleLargeView extends ListPromoView, PromoModuleView {

    @Override
    List<PromoModuleLargeItemView> getListItems();

    @Override
    default CallToAction getCta() {
        return null;
    }

    /**
     * Creates a list of PromoModuleLargeView for a given view request
     *
     * @param request the current view request.
     * @param title the module title. Appears only on the first module.
     * @param largeItemViews the list of module items. Will be partitioned appropriately.
     * @return newly created list of small promo module views.
     */
    static List<PromoModuleLargeView> createFromItems(ViewRequest request, LinkOrText title, String description, ModuleBackgroundTheme bgTheme, List<PromoModuleLargeItemView> largeItemViews) {

        // list of modules
        List<PromoModuleLargeView> largeViews = new ArrayList<>();

        boolean isFirstLoop = true;

        // loop over the item groups and create a new module from each group and add to list.
        for (PromoModuleLargeItemView largeItem : largeItemViews) {

            boolean isFirstGroup = isFirstLoop;

            largeViews.add(new PromoModuleLargeView() {

                @Override
                public Options getOptions() {
                    return new Options() {
                        @Override
                        public String getHorizontalSplit() {
                            return null;
                        }

                        @Override
                        public String getModifierClass() {
                            return "large-promo-hover-overlay " + (bgTheme != null ? bgTheme.getCssClass() : "");
                        }
                    };
                }

                @Override
                public BspComponentGroupView getHeading() {
                    // title goes in the first group only.
                    //return isFirstGroup && title != null ? request.createView(LinkOrTextView.class, title) : null;

                    // title/description goes in the first group only.
                    if (isFirstGroup) {

                        List<Object> components = new ArrayList<>();

                        // LinkOrTextView
                        if (title != null) {
                            components.add(request.createView(LinkOrTextView.class, title));
                        }

                        // TextView
                        if (!StringUtils.isBlank(description)) {
                            components.add(TextView.createWithModifier(description, "bsp-sub-title"));
                        }

                        if (!components.isEmpty()) {
                            return () -> components;
                        }
                    }

                    return null;
                }

                @Override
                public List<PromoModuleLargeItemView> getListItems() {
                    return Collections.singletonList(largeItem);
                }
            });

            isFirstLoop = false;
        }

        return largeViews;
    }
}
