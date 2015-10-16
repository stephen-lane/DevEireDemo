package com.deveiredemo.model;

import java.util.ArrayList;
import java.util.List;

import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.view.PartnerView;
import com.psddev.aod.UbikViewable;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;

@ViewMapping(PartnerView.FromPartnerModule.class)

@Recordable.DisplayName("Module (Partner)")
public class PartnerModule extends Module implements UbikViewable {

    private String title;

    @ToolUi.RichText
    private String description;

    private Image logo;

    private List<Link> links;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getLogo() {
        return logo;
    }

    public void setLogo(Image logo) {
        this.logo = logo;
    }

    public List<Link> getLinks() {
        if (links == null) {
            links = new ArrayList<>();
        }
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String getLabel() {
        return getTitle();
    }

    public String getAodDescription() {
        return ReferentialTextUtils.stripHtml(getDescription());
    }
}
