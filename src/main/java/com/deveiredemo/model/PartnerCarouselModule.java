package com.deveiredemo.model;

import java.util.ArrayList;
import java.util.List;

import com.deveiredemo.view.GalleryModuleHorizontalView;
import com.psddev.aod.UbikContent;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

@ViewMapping(GalleryModuleHorizontalView.FromPartnerCarouselModule.class)

@Recordable.DisplayName("Module (Partner Carousel)")
public class PartnerCarouselModule extends Module implements ModuleTitled, UbikContent {

    private List<PartnerCarouselModuleItem> partners;

    public List<PartnerCarouselModuleItem> getPartners() {
        if (partners == null) {
            partners = new ArrayList<>();
        }
        return partners;
    }
}
