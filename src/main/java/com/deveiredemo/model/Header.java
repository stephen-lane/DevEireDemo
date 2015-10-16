package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;

import com.deveiredemo.view.HeaderView;

import java.util.ArrayList;
import java.util.List;

@ViewMapping(HeaderView.FromHeader.class)
public class Header extends Content {

    @ToolUi.Note(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    private String name;

    private String siteName;

    private Image siteLogoImage;

    private Link siteLogoLink;

    private List<Link> navLinks;

    public String getName() {
        return name;
    }

    public String getSiteName() {
        return siteName;
    }

    public Image getSiteLogoImage() {
        return siteLogoImage;
    }

    public Link getSiteLogoLink() {
        return siteLogoLink;
    }

    public List<Link> getNavLinks() {
        if (navLinks == null) {
            navLinks = new ArrayList<>();
        }
        return navLinks;
    }
}
