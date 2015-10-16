package com.deveiredemo.view;

import com.psddev.cms.db.CropOption;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Image;
import com.deveiredemo.model.LinkOrText;
import com.deveiredemo.model.PartnerCarouselModule;
import com.deveiredemo.view.base.BspGalleryModuleHorizontalView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface GalleryModuleHorizontalView extends BspGalleryModuleHorizontalView, ModuleView {

    @Override
    List<? extends LinkView.ImageLink> getGallerySlides();

    static class FromPartnerCarouselModule extends AbstractViewCreator<PartnerCarouselModule> implements GalleryModuleHorizontalView {

        @Override
        public Options getOptions() {
            return new Options() {

                @Override
                public String getModifierClass() {
                    return "jsg-partner-carousel " + model.getBackgroundTheme().getCssClass();
                }

                @Override
                public String getSlidesToShow() {
                    return "7";
                }

                @Override
                public String getSlidesToShowMobilePort() {
                    return "5";
                }

                @Override
                public String getSlidesToShowMobile() {
                    return "4";
                }
            };
        }

        @Override
        public String getGalleryTitle() {
            LinkOrText title = model.getModuleTitledData().getTitle();
            return title != null ? title.getText() : null;
        }

        @Override
        public List<? extends LinkView.ImageLink> getGallerySlides() {

            return model.getPartners().stream()
                    .filter((partner) -> partner.getImage() != null)
                    .map((partner) -> new LinkView.ImageLink() {

                        @Override
                        public ImageView getBody() {

                            Image image = partner.getImage();
                            if (image != null) {
                                StorageItem file = image.getFile();
                                if (file != null) {
                                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                            .width(150)
                                            .height(75)
                                            .cropOption(CropOption.NONE), image);
                                }
                            }

                            return null;
                        }

                        @Override
                        public Map<String, String> getAttributes() {
                            return LinkView.createAttributes(partner.getLink());
                        }

                    }).collect(Collectors.toList());
        }
    }
}
