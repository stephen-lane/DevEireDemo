package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Site;
import com.psddev.cms.view.MainViewClass;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Singleton;
import com.psddev.util.sitemap.SiteMapEntry;
import com.psddev.util.sitemap.SiteMapItem;

import com.deveiredemo.view.GalleryFallPageView;
import com.deveiredemo.view.PageView;

import java.util.Collections;
import java.util.List;

@PageViewClass(PageView.class)
@MainViewClass(GalleryFallPageView.class)

@ViewMapping(PageView.FromGalleryFallPage.class)
@ViewMapping(GalleryFallPageView.FromGalleryFallPage.class)

public class GalleryFallPage extends Content implements Directory.Item,
                                                        OpenGraphDefinable,
                                                        Singleton,
                                                        SiteMapItem,
                                                        TwitterCardDefinable {

    private String title;

    private String subTitle;

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public String createPermalink(Site site) {
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
