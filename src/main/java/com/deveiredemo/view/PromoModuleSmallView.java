package com.deveiredemo.view;

import com.psddev.cms.view.ViewRequest;

import com.google.common.collect.Lists;
import com.deveiredemo.model.Link;
import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.ModuleBackgroundTheme;
import com.deveiredemo.view.base.BspLinkView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Specialized version of {@link ListPromoView} that contains
 * {@link PromoModuleSmallItemView} items.
 */
public interface PromoModuleSmallView extends ListPromoView, PromoModuleView {

    @Override
    List<PromoModuleSmallItemView> getListItems();

    /**
     * Creates a list of PromoModuleSmallView for a given view request
     *
     * @param request the current view request.
     * @param title the module title. Appears only on the first module.
     * @param callToAction the call to action link. Appears only on the last module.
     * @param smallItemViews the list of module items. Will be partitioned appropriately.
     * @return newly created list of small promo module views.
     */
    static List<PromoModuleSmallView> createFromItems(ViewRequest request, LinkOrText title, Link callToAction, ModuleBackgroundTheme bgTheme, List<PromoModuleSmallItemView> smallItemViews) {

        // number of items in each promo module
        final int total = 3;

        // list of modules
        List<PromoModuleSmallView> smallViews = new ArrayList<>();

        // break the items into groups of <total>.
        List<List<PromoModuleSmallItemView>> groups = Lists.partition(smallItemViews, total);

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
            for (Iterator<List<PromoModuleSmallItemView>> groupsIt = groups.iterator(); groupsIt.hasNext();) {

                List<PromoModuleSmallItemView> group = groupsIt.next();

                boolean isFirstGroup = isFirstLoop;
                boolean isLastGroup = !groupsIt.hasNext();

                smallViews.add(new PromoModuleSmallView() {

                    @Override
                    public Options getOptions() {
                        return new Options() {
                            @Override
                            public String getHorizontalSplit() {
                                return "third";
                            }

                            @Override
                            public String getModifierClass() {
                                return "small-promo-with-cta " + (bgTheme != null ? bgTheme.getCssClass() : "");
                            }
                        };
                    }

                    @Override
                    public LinkOrTextView getHeading() {
                        // title goes in the first group only.
                        return isFirstGroup && title != null ? request.createView(LinkOrTextView.class, title) : null;
                    }

                    @Override
                    public List<PromoModuleSmallItemView> getListItems() {
                        return group;
                    }

                    @Override
                    public CallToAction getCta() {

                        // callToAction goes in the last group only.
                        if (isLastGroup && callToAction != null) {

                            return new CallToAction() {
                                @Override
                                public Options getOptions() {
                                    return () -> "bottom-center";
                                }

                                @Override
                                public BspLinkView getContent() {
                                    return request.createView(LinkView.class, callToAction);
                                }
                            };

                        } else {
                            return null;
                        }
                    }
                });

                isFirstLoop = false;
            }
        }

        return smallViews;
    }
}
