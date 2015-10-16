package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

import com.deveiredemo.view.SocialLinkView;

@ViewMapping(SocialLinkView.FromSocialLink.class)

@Recordable.Embedded
public class SocialLink extends Content {

    private SocialLinkIcon icon;

    private ExternalLink link;

    public SocialLinkIcon getIcon() {
        return icon;
    }

    public ExternalLink getLink() {
        return link;
    }
}
