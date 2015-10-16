package com.deveiredemo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.deveiredemo.migration.MigratedContent;
import com.deveiredemo.view.GalleryFullscreenView;
import com.deveiredemo.view.GalleryGridView;
import com.deveiredemo.view.GalleryMainView;
import com.deveiredemo.view.PageView;
import com.deveiredemo.view.SocialShareView;
import com.psddev.aod.ListScreen;
import com.psddev.aod.UbikContent;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.page.SearchCarouselPreviewable;
import com.psddev.cms.view.MainViewClass;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.util.PaginatedResult;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.sitemap.SiteMapEntry;
import com.psddev.util.sitemap.SiteMapItem;

@ToolUi.Referenceable(via = GalleryEnhancement.class)

@PageViewClass(PageView.class)
@MainViewClass(GalleryMainView.class)

@ViewMapping(PageView.FromGallery.class)
@ViewMapping(GalleryMainView.FromGallery.class)
//@ViewMapping(GalleryModuleView.FromGallery.class)
@ViewMapping(GalleryFullscreenView.FromGallery.class)

@ViewMapping(SocialShareView.FromObject.class)

@ViewMapping(GalleryGridView.FromGalleryWithLayout1.class)
@ViewMapping(GalleryGridView.FromGalleryWithLayout2.class)
@ViewMapping(GalleryGridView.FromGalleryWithLayout3.class)
@ViewMapping(GalleryGridView.FromGalleryWithLayout4.class)

@ToolUi.PreviewField("getFirstSlideImageFile")

@Seo.TitleFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getTitle")
@Seo.DescriptionFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getDescriptionAsText")
public class Gallery extends Content implements Directory.Item,
                                                ListScreen<GallerySlide>,
                                                MigratedContent,
                                                OpenGraphDefinable,
                                                Promotable,
                                                SearchCarouselPreviewable,
                                                SiteMapItem,
                                                Taggable,
                                                TwitterCardDefinable,
                                                UbikContent {

    @ToolUi.Note(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    @Indexed
    private String name;

    @Indexed
    private String headline;

    @ToolUi.RichText
    private String description;

    private List<GallerySlide> slides;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GallerySlide> getSlides() {
        if (slides == null) {
            slides = new ArrayList<>();
        }
        return slides;
    }

    public void setSlides(List<GallerySlide> slides) {
        this.slides = slides;
    }

    @Ignored(false) @ToolUi.Hidden
    public StorageItem getFirstSlideImageFile() {
        GallerySlide slide = getSlides().stream().findFirst().orElse(null);
        if (slide != null) {
            Image slideImage = slide.getImage();
            if (slideImage != null) {
                return slideImage.getFile();
            }
        }
        return null;
    }

    // --- SearchCarouselPreviewable support ---

    @Override
    public StorageItem getSearchCarouselPreview() {
        return getFirstSlideImageFile();
    }

    @Override
    public String getLabel() {
        return getName();
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {

        String name = getName();

        if (name != null) {
            name = StringUtils.toNormalized(name);
            if (!StringUtils.isBlank(name)) {
                return "/gallery/" + name;
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

    // --- Promotable Support ---

    @Override
    public String getPromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getDescription();
    }

    @Override
    public Image getPromotableImageFallback() {
        for (GallerySlide slide : getSlides()) {

            Image image = slide.getImage();
            if (image != null) {
                return image;
            }
        }
        return null;
    }

    // --- ListScreen support ---

    @Override
    public PaginatedResult<GallerySlide> getItemsResultBefore(long offset, int limit, Date before) {
        return new PaginatedResult<>(offset, limit, getSlides());
    }

    @Override
    public String getScreenTitle() {
        return getName();
    }

    @Override
    public StorageItem getScreenImage() {
        return null;
    }

    @Override
    public String getButtonText() {
        return null;
    }

    @Override
    public StorageItem getButtonImage() {
        return null;
    }

    // --- UbikContent support ---

    @Override
    public String getCardTitle() {
        return getPromotableData().getMobileTitle();
    }

    @Override
    public StorageItem getCardImage() {
        Image img = getPromotableData().getImage();
        return img != null ? img.getFile() : null;
    }
}
