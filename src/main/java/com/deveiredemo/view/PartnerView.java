package com.deveiredemo.view;

import com.psddev.cms.db.CropOption;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StorageItem;
import com.psddev.handlebars.HandlebarsTemplate;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Image;
import com.deveiredemo.model.PartnerModule;
import com.deveiredemo.view.base.BspListView;

import java.util.List;
import java.util.stream.Collectors;

@HandlebarsTemplate("components/jsg-partner")
public interface PartnerView extends ModuleView {

    Options getOptions();

    String getTitle();

    ImageView getLogo();

    HtmlOrTextView getContent();

    BspListView getLinks();

    interface Options {

        String getModifierClass();
    }

    static class FromPartnerModule extends AbstractViewCreator<PartnerModule> implements PartnerView {

        @Override
        public Options getOptions() {
            return () -> model.getBackgroundTheme().getCssClass();
        }

        @Override
        public String getTitle() {
            return model.getTitle();
        }

        @Override
        public ImageView getLogo() {

            Image logo = model.getLogo();
            if (logo != null) {

                StorageItem file = logo.getFile();
                if (file != null) {

                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                            .width(300)
                            .height(200)
                            .cropOption(CropOption.NONE), logo);
                }
            }

            return null;
        }

        @Override
        public HtmlView getContent() {
            return model::getDescription;
        }

        @Override
        public BspListView getLinks() {
            return new BspListView() {
                @Override
                public Options getOptions() {
                    return null;
                }

                @Override
                public List<?> getItems() {
                    return model.getLinks().stream()
                            .map((link) -> request.createView(LinkView.class, link))
                            .filter((linkView) -> linkView != null)
                            .collect(Collectors.toList());
                }
            };
        }
    }
}
