package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Header;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Link;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface HeaderView extends BspSiteHeaderSimpleView {

    static class FromHeader extends AbstractViewCreator<Header> implements HeaderView {

        @Override
        public String getSiteName() {
            return model.getSiteName();
        }

        @Override
        public SiteLogoView getSiteLogo() {

            Image siteLogo = model.getSiteLogoImage();
            if (siteLogo != null) {

                ImageView imageView = request.createView(ImageView.class, siteLogo);

                Link siteLogoLink = model.getSiteLogoLink();

                return () -> new LinkView() {

                    @Override
                    public String getCssClass() {
                        return null;
                    }

                    @Override
                    public Map<String, String> getAttributes() {
                        return LinkView.createAttributes(siteLogoLink);
                    }

                    @Override
                    public ImageView getBody() {
                        return imageView;
                    }
                };

            } else {
                return null;
            }
        }

        @Override
        public List<LinkView> getNavLinks() {
            return model.getNavLinks().stream()
                    .map((link) -> request.createView(LinkView.class, link))
                    .filter((view) -> view != null)
                    .collect(Collectors.toList());
        }
    }
}
