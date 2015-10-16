package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.deveiredemo.model.Footer;
import com.deveiredemo.view.base.BspSiteFooterSimpleView;

import java.util.List;
import java.util.stream.Collectors;

public interface FooterView extends BspSiteFooterSimpleView {

    static class FromFooter extends AbstractViewCreator<Footer> implements FooterView {

        @Override
        public String getDisclaimer() {
            return model.getDisclaimer();
        }

        @Override
        public Sharing getSharing() {
            return new Sharing() {

                @Override
                public String getSocial() {
                    return "Follow Jordan";
                }

                @Override
                public List<SocialLinkView> getLinks() {
                    return model.getSocialLinks().stream()
                            .map((socialLink) -> request.createView(SocialLinkView.class, socialLink))
                            .collect(Collectors.toList());
                }
            };
        }
    }
}
