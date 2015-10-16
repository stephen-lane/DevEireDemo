package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;

import com.deveiredemo.view.FooterView;

import java.util.ArrayList;
import java.util.List;

@ViewMapping(FooterView.FromFooter.class)

public class Footer extends Content {

    private List<SocialLink> socialLinks;

    @ToolUi.RichText
    private String disclaimer;

    public List<SocialLink> getSocialLinks() {
        if (socialLinks == null) {
            socialLinks = new ArrayList<>();
        }
        return socialLinks;
    }

    public String getDisclaimer() {
        return disclaimer;
    }
}
