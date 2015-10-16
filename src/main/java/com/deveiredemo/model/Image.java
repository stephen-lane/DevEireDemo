package com.deveiredemo.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.deveiredemo.migration.MigratedContent;
import com.deveiredemo.util.ContentStateUtils;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.util.StorageItemImageMetadata;
import com.deveiredemo.view.GalleryFullscreenSlideView;
import com.deveiredemo.view.GalleryFullscreenView;
import com.deveiredemo.view.ImageMainView;
import com.deveiredemo.view.ImagePromoView;
import com.deveiredemo.view.ImageView;
import com.deveiredemo.view.PageView;
import com.psddev.aod.UbikContent;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.view.MainViewClass;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.ColorImage;
import com.psddev.dari.db.Location;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.ContentType;
import com.psddev.util.SequentialDirectoryItem;
import com.psddev.util.sitemap.SiteMapEntry;
import com.psddev.util.sitemap.SiteMapItem;

@PageViewClass(PageView.class)
@MainViewClass(ImageMainView.class)

@ViewMapping(PageView.FromImage.class)
@ViewMapping(ImageMainView.FromImage.class)
@ViewMapping(GalleryFullscreenView.FromImage.class)
@ViewMapping(GalleryFullscreenSlideView.FromImage.class)
@ViewMapping(ImagePromoView.FromGolfTournamentImage.class)

@ViewMapping(ImageView.FromImage.class)

@Recordable.PreviewField("file")

@ToolUi.Referenceable(via = ImageEnhancement.class)

@Seo.TitleFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getTitle")
@Seo.DescriptionFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getDescriptionAsText")
public class Image extends Content implements BeforeSavePermalinkTrigger,
                                              ColorImage,
                                              MigratedContent,
                                              OpenGraphDefinable,
                                              Promotable,
                                              SequentialDirectoryItem,
                                              SiteMapItem,
                                              Taggable,
                                              TwitterCardDefinable,
                                              UbikContent {

    @Indexed
    @ToolUi.Note(Constants.TOOL_UI_NOTE_CMS_USE_ONLY)
    @ToolUi.Placeholder(dynamicText = "${content.nameFallback}", editable = true)
    private String name;

    @Indexed
    @ToolUi.Placeholder(dynamicText = "${content.titleFallback}", editable = true)
    private String title;

    @Indexed
    @ToolUi.RichText
    @ToolUi.Placeholder(dynamicText = "${content.captionFallback}", editable = true)
    private String caption;

    @ToolUi.Note("Author/Source")
    @ToolUi.Placeholder(dynamicText = "${content.creditFallback}", editable = true)
    private String credit;

    @ToolUi.Note("For accessibility.")
    @ToolUi.Placeholder(dynamicText = "${content.altTextFallback}", editable = true)
    private String altText;

    @Required
    private StorageItem file;
    private transient StorageItemImageMetadata fileMetadata;

    private Date dateTaken;

    @ToolUi.Tab("Location")
    @Indexed
    private Location location;

    public String getName() {
        if (name == null) {
            return getNameFallback();
        }
        return name;
    }

    public String getTitle() {
        if (title == null) {
            return getTitleFallback();
        }
        return title;
    }

    public String getCaption() {
        if (ReferentialTextUtils.isBlank(caption)) {
            return getCaptionFallback();
        }
        return caption;
    }

    public String getAltText() {
        if (altText == null) {
            return getAltTextFallback();
        }
        return altText;
    }

    public String getCredit() {
        if (credit == null) {
            return getCreditFallback();
        }
        return credit;
    }

    public StorageItem getFile() {
        return file;
    }

    public void setFile(StorageItem file) {
        this.file = file;
        this.fileMetadata = null;
    }

    private StorageItemImageMetadata getFileMetadata() {
        if (fileMetadata == null) {
            fileMetadata = new StorageItemImageMetadata(getFile());
        }
        return fileMetadata;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public Location getLocation() {
        return location;
    }

    @ToolUi.Filterable
    @Indexed @ToolUi.Hidden
    public ImageOrientation getOrientation() {

        Integer width = getFileMetadata().getWidth();
        Integer height = getFileMetadata().getHeight();

        if (width != null && width > 0 && height != null && height > 0) {

            if (width > height) {
                return ImageOrientation.LANDSCAPE;

            } else if (width < height) {
                return ImageOrientation.PORTRAIT;

            } else {
                return ImageOrientation.SQUARE;
            }
        } else {
            return ImageOrientation.UNKNOWN;
        }
    }

    // --- ColorImage support ---

    @Override
    public StorageItem getColorImage() {

        if (ContentStateUtils.isContentStateRequest()) {
            return null;
        }

        // FIXME: Still broken, so returning null for now...
        return null;
    }

    // --- SequentialDirectoryItem support ---

    @Override
    public String createSequentialPermalink(Site site) {
        String title = getTitle();

        // try to strip the file extension from the permalink creation if it exists.
        if (title != null) {
            int lastDotIndex = title.lastIndexOf('.');
            if (lastDotIndex >= 0 && !title.endsWith(".")) {

                String extension = title.substring(lastDotIndex + 1);

                for (ContentType contentType : ContentType.values()) {

                    if (contentType.getVariants().contains(extension)) {
                        title = title.substring(0, lastDotIndex);
                        break;
                    }
                }
            }
        }

        return !StringUtils.isBlank(title) ? "/photo/" + StringUtils.toNormalized(title) : null;
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

    // --- Record support ---

    @Override
    public void beforeSave() {
        if (StringUtils.isBlank(name)) {
            name = getNameFallbackValue();
        }
        if (StringUtils.isBlank(name)) {
            getState().addError(getState().getField("name"), "Required!");
        }

        // Date fields don't support placeholder text, so falling back to beforeSave.
        if (dateTaken == null) {
            dateTaken = getDateTakenFallback();
        }

        // Location fields don't support placeholder text, so falling back to beforeSave.
        if (location == null) {
            location = getLocationFallback();
        }

        if (getState().isNew()) {
            getFileMetadata().fixOrientation();
        }
    }

    @Override
    public String getLabel() {
        return getName();
    }

    // --- Promotable support ---

    @Override
    public String getPromotableTitleFallback() {
        return getTitle();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getCaption();
    }

    @Override
    public Image getPromotableImageFallback() {
        return this;
    }

    // --- UbikContent support ---

    @Override
    public String getCardTitle() {
        return getTitle();
    }

    @Override
    public String getCardBlurb() {
        return getCaption();
    }

    @Override
    public StorageItem getCardImage() {
        return getFile();
    }

    // --- Helpers ---

    /** Not for external use! */
    public String getNameFallback() {
        if (getState().isNew()) {
            return getNameFallbackValue();
        } else {
            return null;
        }
    }

    private String getNameFallbackValue() {
        return ObjectUtils.firstNonBlank(
                getFileMetadata().getTitle(),
                getFileMetadata().getOriginalFileName(),
                getFileNameFromStoragePath());
    }

    /** Not for external use! */
    public String getTitleFallback() {
        return getName();
    }

    /** Not for external use! */
    public String getCaptionFallback() {
        return getFileMetadata().getCaption();
    }

    /** Not for external use! */
    public String getCreditFallback() {
        String byline = getFileMetadata().getByline();
        String source = getFileMetadata().getCredit();

        if (byline != null) {
            if (source != null) {
                return byline + "/" + source;

            } else {
                return byline;
            }
        } else {
            return source;
        }
    }

    /** Not for external use! */
    public String getAltTextFallback() {
        return getTitle();
    }

    private String getFileNameFromStoragePath() {

        StorageItem file = getFile();
        if (file != null) {

            String path = file.getPath();
            if (path != null) {

                int lastSlashAt = path.lastIndexOf('/');
                if (lastSlashAt >= 0) {
                    return path.substring(lastSlashAt + 1);
                } else {
                    return path;
                }
            }
        }
        return null;
    }

    private Date getDateTakenFallback() {

        Date dateTaken = getFileMetadata().getDateTaken();

        // fallback to publish date
        if (dateTaken == null) {
            Date publishDate = as(ObjectModification.class).getPublishDate();
            if (publishDate != null) {
                dateTaken = publishDate;
            }
        }

        return dateTaken;
    }

    private Location getLocationFallback() {
        return getFileMetadata().getGpsLocation();
    }
}
