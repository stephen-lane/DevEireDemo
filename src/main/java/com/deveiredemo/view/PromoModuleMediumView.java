package com.deveiredemo.view;

import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.util.StringUtils;

import com.google.common.collect.Lists;
import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.ModuleBackgroundTheme;
import com.deveiredemo.view.base.BspComponentGroupView;

import java.util.ArrayList;
import java.util.List;

public interface PromoModuleMediumView extends ListPromoView, PromoModuleView {

    @Override
    List<PromoModuleMediumItemView> getListItems();

    @Override
    default CallToAction getCta() {
        return null;
    }

    /**
     * Creates a list of PromoModuleMediumView for a given view request
     *
     * @param request the current view request.
     * @param title the module title. Appears only on the first module.
     * @param mediumItemViews the list of module items. Will be partitioned appropriately.
     * @return newly created list of small promo module views.
     */
    static List<PromoModuleMediumView> createFromItems(ViewRequest request, LinkOrText title, String description, ModuleBackgroundTheme bgTheme, List<PromoModuleMediumItemView> mediumItemViews) {

        // number of items in each promo module
        final int total = 2;

        // list of modules
        List<PromoModuleMediumView> mediumViews = new ArrayList<>();

        // break the items into groups of <total>.
        List<List<PromoModuleMediumItemView>> groups = Lists.partition(mediumItemViews, total);

        // if it's not empty
        int groupsSize = groups.size();
        if (groupsSize > 0) {

            // Commenting out per JSPIETH-579
//            // if the last group does not have enough items, get a view of the groups list without it.
//            if (groups.get(groupsSize - 1).size() < total) {
//                groups = groups.subList(0, groupsSize - 1);
//            }

            boolean isFirstLoop = true;

            // loop over the item groups and create a new module from each group and add to list.
            for (List<PromoModuleMediumItemView> group : groups) {

                boolean isFirstGroup = isFirstLoop;

                mediumViews.add(new PromoModuleMediumView() {

                    @Override
                    public Options getOptions() {
                        return new Options() {
                            @Override
                            public String getHorizontalSplit() {
                                return "half";
                            }

                            @Override
                            public String getModifierClass() {
                                return "medium-promo " + (bgTheme != null ? bgTheme.getCssClass() : "");
                            }
                        };
                    }

                    @Override
                    public BspComponentGroupView getHeading() {

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
                    public List<PromoModuleMediumItemView> getListItems() {
                        return group;
                    }
                });

                isFirstLoop = false;
            }
        }

        return mediumViews;
    }
}
