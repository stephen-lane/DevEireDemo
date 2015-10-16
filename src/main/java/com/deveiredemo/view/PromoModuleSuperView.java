package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.PromoList;
import com.deveiredemo.model.PromoModule;
import com.deveiredemo.view.base.BspTextView;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface PromoModuleSuperView extends GalleryModuleView, PromoModuleView {

    static class FromPromoModule extends AbstractViewCreator<PromoModule> implements PromoModuleSuperView {

        @Override
        public Options getOptions() {
            return new Options() {
                @Override
                public String getModifierClass() {
                    return "super-promo-jsg " + model.getBackgroundTheme().getCssClass();
                }

                @Override
                public boolean isCarouselDots() {
                    return false;
                }
            };
        }

        @Override
        public BspTextView getGalleryTitle() {
            LinkOrText title = model.getModuleTitledData().getTitle();
            return title != null ? title::getText : null;
        }

        @Override
        public List<? extends Slide> getGallerySlides() {

            PromoList promoList = model.getContent();
            if (promoList != null) {
                return promoList.getContent().stream()
                        .map((promotable) -> request.createView(PromoModuleSuperItemView.FromPromotable.class, promotable))
                        .filter((item) -> item != null)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public CallToAction getCta() {
            // TODO: Still need to implement?
            return null;
        }
    }
}
