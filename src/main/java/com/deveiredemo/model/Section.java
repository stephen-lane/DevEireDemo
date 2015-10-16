package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.MainViewClass;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.sitemap.SiteMapEntry;
import com.psddev.util.sitemap.SiteMapItem;

import com.deveiredemo.view.PageView;
import com.deveiredemo.view.SectionMainView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PageViewClass(PageView.class)
@MainViewClass(SectionMainView.class)

@ViewMapping(PageView.FromSection.class)
@ViewMapping(SectionMainView.FromSection.class)

public class Section extends Content implements Directory.Item,
                                                OpenGraphDefinable,
                                                SiteMapItem,
                                                TwitterCardDefinable {

    @ToolUi.NoteHtml(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    private String name;

    private String title;

    @Embedded
    private List<Module> modules;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Module> getModules() {
        if (modules == null) {
            modules = new ArrayList<>();
        }
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    @Override
    public String createPermalink(Site site) {
        String permalink = getTitle();
        if (permalink != null) {
            permalink = StringUtils.toNormalized(permalink);

            if (!StringUtils.isBlank(permalink)) {
                return "/" + permalink;
            }
        }
        return null;
    }

    // --- SiteMapItem support ---

    @Override
    public List<SiteMapEntry> getSiteMapEntries() {

        String permalink = as(Directory.ObjectModification.class).getFullPermalink();

        if (permalink != null) {
            SiteMapEntry entry = new SiteMapEntry();

            entry.setPermalink(permalink);
            entry.setUpdateDate(getUpdateDate());
            entry.setChangeFrequency(as(SiteMapItem.Data.class).getChangeFrequency());
            entry.setPriority(as(SiteMapItem.Data.class).getPriority());

            return Collections.singletonList(entry);
        }

        return null;
    }
}
