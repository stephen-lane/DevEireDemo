package com.deveiredemo.model.app;

import com.psddev.aod.AbstractScreen;
import com.psddev.aod.ListScreen;
import com.psddev.aod.NavigationScreen;
import com.psddev.dari.util.PaginatedResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppMenuScreen extends AbstractScreen implements ListScreen<AppMenuItem>, NavigationScreen {

    private List<AppMenuItem> menuItems;

    public List<AppMenuItem> getMenuItems() {
        if (menuItems == null) {
            menuItems = new ArrayList<>();
        }
        return menuItems;
    }

    public void setMenuItems(List<AppMenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    @Override
    public PaginatedResult<AppMenuItem> getItemsResultBefore(long offset, int limit, Date date) {
        return new PaginatedResult<>(offset, limit, getMenuItems());
    }
}
