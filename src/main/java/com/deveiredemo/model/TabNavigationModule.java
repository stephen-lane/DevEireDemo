package com.deveiredemo.model;

import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.view.TabNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ViewMapping(TabNavigationView.FromTabNavigationModule.class)

@Recordable.DisplayName("Module (Tab Navigation)")
public class TabNavigationModule extends Module {

    private List<Link> tabLinks;

    public List<Link> getTabLinks() {
        if (tabLinks == null) {
            tabLinks = new ArrayList<>();
        }
        return tabLinks;
    }

    public void setTabLinks(List<Link> tabLinks) {
        this.tabLinks = tabLinks;
    }

    @Override
    public String getLabel() {
        if (!StringUtils.isBlank(getName())) {
            return getName();
        } else {
            return getTabLinks().stream()
                    .map(Link::getText)
                    .filter((text) -> text != null)
                    .collect(Collectors.joining(" | "));
        }
    }
}
