package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.PredicateParser;
import com.psddev.dari.util.StringUtils;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.PromoList;
import com.deveiredemo.model.PromoListDynamic;
import com.deveiredemo.model.PromoListShared;
import com.deveiredemo.model.PromoModule;
import com.deveiredemo.model.PromoModuleSize;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.servlet.PromoModuleLoadMoreServlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@HandlebarsTemplate("components/jsg-promo-module-list")
public interface PromoModuleListView extends ModuleView {

    List<? extends PromoModuleView> getModules();

    static class FromPromoModule extends AbstractViewCreator<PromoModule> implements PromoModuleListView {

        @Override
        public List<? extends PromoModuleView> getModules() {

            List<? extends PromoModuleView> moduleViews;

            PromoList promoList = model.getContent();
            if (promoList != null) {
                promoList.applyFilter(PredicateParser.Static.parse(Promotable.HAS_IMAGE_PREDICATE));
            }

            PromoModuleSize size = model.getModuleSize();
            switch (size) {
                case SMALL:
                    moduleViews = getSmallModuleViews();
                    break;

                case MEDIUM:
                    moduleViews = getMediumModuleViews();
                    break;

                case LARGE:
                    moduleViews = getLargeModuleViews();
                    break;

                case SUPER:
                    moduleViews = getSuperModuleViews();
                    break;

                default:
                    moduleViews = Collections.emptyList();
                    break;
            }

            List<PromoModuleView> views = new ArrayList<>(moduleViews);

            PromoListDynamic dynamicList = getDynamicLoadMorePromoList();
            if (dynamicList != null && !moduleViews.isEmpty()) {

                int page = getPage();

                views.add(new PromoModuleLoadMoreView() {
                    @Override
                    public String getUrl() {
                        return StringUtils.addQueryParameters(Constants.AJAX_ENDPOINT_PROMO_MODULE_LOAD_MORE,
                                PromoModuleLoadMoreServlet.PROMO_LIST_ID_PARAMETER, dynamicList.getId(),
                                PromoModuleLoadMoreServlet.PROMO_LIST_PAGE_PARAMETER, (page + 1),
                                PromoModuleLoadMoreServlet.PROMO_LIST_MODULE_SIZE_PARAMETER, PromoModuleSize.SMALL.getInternalName());
                    }

                    @Override
                    public String getLinkText() {
                        return "Load More";
                    }
                });
            }

            return views;
        }

        private int getPage() {
            int page = request.getParameter(Integer.class, PromoModuleLoadMoreServlet.PROMO_LIST_PAGE_PARAMETER).findFirst().orElse(1);
            if (page <= 0) {
                page = 1;
            }
            return page;
        }

        private PromoListDynamic getDynamicLoadMorePromoList() {
            PromoListDynamic dynamicList = null;

            PromoList promoList = model.getContent();
            if (promoList instanceof PromoListShared) {

                PromoList delegate = ((PromoListShared) promoList).getDelegate();
                if (delegate instanceof PromoListDynamic) {
                    dynamicList = (PromoListDynamic) delegate;
                }
            } else if (promoList instanceof PromoListDynamic) {
                dynamicList = (PromoListDynamic) promoList;
            }

            if (dynamicList != null && dynamicList.isLoadMore()) {
                return dynamicList;
            } else {
                return null;
            }
        }

        private List<PromoModuleSmallView> getSmallModuleViews() {

            // get the promo list.
            PromoList promoList = model.getContent();
            if (promoList != null) {

                List<PromoModuleSmallItemView> smallItemViews = new ArrayList<>();

                // create a module item from each promo.
                for (Promotable promo : promoList.getContent()) {

                    PromoModuleSmallItemView smallItem = request.createView(PromoModuleSmallItemView.class, promo);

                    // Make sure the module item is not null AND it actually has sections, aka content.
                    if (smallItem != null && smallItem.getSections() != null) {
                        smallItemViews.add(smallItem);
                    }
                }

                // create a list of module views from the list of module item views, partitioning appropriately.
                return PromoModuleSmallView.createFromItems(
                        request,
                        model.getModuleTitledData().getTitle(),
                        model.getCallToAction(),
                        model.getBackgroundTheme(),
                        smallItemViews);
            }

            return Collections.emptyList();
        }

        private List<PromoModuleMediumView> getMediumModuleViews() {

            // get the promo list.
            PromoList promoList = model.getContent();
            if (promoList != null) {

                List<PromoModuleMediumItemView> mediumItemViews = new ArrayList<>();

                // create a module item from each promo.
                for (Promotable promo : promoList.getContent()) {

                    PromoModuleMediumItemView mediumItem = request.createView(PromoModuleMediumItemView.class, promo);

                    // Make sure the module item is not null AND it actually has sections, aka content.
                    if (mediumItem != null && mediumItem.getSections() != null) {
                        mediumItemViews.add(mediumItem);
                    }
                }

                // create a list of module views from the list of module item views, partitioning appropriately.
                return PromoModuleMediumView.createFromItems(
                        request,
                        model.getModuleTitledData().getTitle(),
                        model.getDescription(),
                        model.getBackgroundTheme(),
                        mediumItemViews);
            }

            return Collections.emptyList();
        }

        private List<PromoModuleLargeView> getLargeModuleViews() {

            // get the promo list.
            PromoList promoList = model.getContent();
            if (promoList != null) {

                List<PromoModuleLargeItemView> largeItemViews = new ArrayList<>();

                // create a module item from each promo.
                for (Promotable promo : promoList.getContent()) {

                    PromoModuleLargeItemView largeItem = request.createView(PromoModuleLargeItemView.class, promo);

                    // Make sure the module item is not null AND it actually has sections, aka content.
                    if (largeItem != null && largeItem.getSections() != null) {
                        largeItemViews.add(largeItem);
                    }
                }

                // create a list of module views from the list of module item views, partitioning appropriately.
                return PromoModuleLargeView.createFromItems(
                        request,
                        model.getModuleTitledData().getTitle(),
                        model.getDescription(),
                        model.getBackgroundTheme(),
                        largeItemViews);
            }

            return Collections.emptyList();
        }

        private List<PromoModuleSuperView> getSuperModuleViews() {

            List<PromoModuleSuperView> superViews;

            PromoModuleSuperView superView = request.createView(PromoModuleSuperView.class, model);

            if (superView != null && !superView.getGallerySlides().isEmpty()) {
                superViews = Arrays.asList(superView);

            } else {
                superViews = Collections.emptyList();
            }

            return superViews;
        }
    }
}
