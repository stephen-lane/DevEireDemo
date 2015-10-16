package com.deveiredemo.model;

import com.psddev.aod.AODMessagable;
import com.psddev.aod.UbikContent;
import com.psddev.aod.UbikScreen;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.RichTextCleaner;
import com.psddev.cms.db.Seo;
import com.psddev.cms.db.Site;
import com.psddev.cms.db.ToolUi;
import com.psddev.cms.tool.CmsTool;
import com.psddev.cms.view.PageViewClass;
import com.psddev.cms.view.ViewMapping;
import com.psddev.dari.db.Application;
import com.psddev.dari.db.Reference;
import com.psddev.dari.db.ReferentialText;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.util.sitemap.SiteMapEntry;
import com.psddev.util.sitemap.SiteMapItem;

import com.deveiredemo.migration.MigratedContent;
import com.deveiredemo.view.ArticleLeadImageFirstView;
import com.deveiredemo.view.PageView;
import com.deveiredemo.view.SocialShareView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.List;

@PageViewClass(PageView.class)
// @MainViewClass is determined programmatically since article needs 2 different layouts.

@ViewMapping(PageView.FromArticle.class)

@ViewMapping(ArticleLeadImageFirstView.FromArticle.class)

@ViewMapping(SocialShareView.FromArticle.class)

@Seo.TitleFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getTitle")
@Seo.DescriptionFields(Promotable.FIELD_INTERNAL_NAME_PREFIX + "getDescriptionAsText")
public class Article extends Content implements AODMessagable,
                                                Directory.Item,
                                                MigratedContent,
                                                OpenGraphDefinable,
                                                Promotable,
                                                SiteMapItem,
                                                Taggable,
                                                TwitterCardDefinable,
                                                UbikContent,
                                                UbikScreen {

    @Required
    @Indexed
    private String headline;

    @ToolUi.Placeholder(dynamicText = "${content.getMobileHeadlineFallback()}", editable = true)
    private String mobileHeadline;

    private Image headlineLogo;

    private String subHeadline;

    @ToolUi.Placeholder(dynamicText = "${content.getMobileSubHeadlineFallback()}", editable = true)
    private String mobileSubHeadline;

    @ToolUi.Note("Deprecated: Move the image to the Headline Logo field above.")
    @Deprecated
    private Image subHeadlineLogo;

    private Author author;

    private ArticleLead lead;

    private ReferentialText body;

    private Link originalContent;

    private String originalSource;

    public String getHeadline() {
        return headline;
    }

    public String getMobileHeadline() {
        if (mobileHeadline == null) {
            return getMobileHeadlineFallback();
        }
        return mobileHeadline;
    }

    public String getSubHeadline() {
        return subHeadline;
    }

    public String getMobileSubHeadline() {
        if (mobileSubHeadline == null) {
            return getMobileSubHeadlineFallback();
        }
        return mobileSubHeadline;
    }

    public Image getHeadlineLogo() {
        // Backward compatibility for field name change.
        if (headlineLogo == null) {
            return subHeadlineLogo;
        }
        return headlineLogo;
    }

    public Author getAuthor() {
        return author;
    }

    public ArticleLead getLead() {
        return lead;
    }

    public ReferentialText getBody() {
        return body;
    }

    public Link getOriginalContent() {
        return originalContent;
    }

    public String getOriginalSource() {
        return originalSource;
    }

    /**
     * Not for public use!
     */
    public String getMobileHeadlineFallback() {
        return getHeadline();
    }

    /**
     * Not for public use!
     */
    public String getMobileSubHeadlineFallback() {
        return getSubHeadline();
    }

    @Override
    public void beforeSave() {

        // Backward compatibility for field name change.
        if (subHeadlineLogo != null) {
            if (headlineLogo == null) {
                headlineLogo = subHeadlineLogo;
            }
            subHeadlineLogo = null;
        }
    }

    // --- Directory.Item support ---

    @Override
    public String createPermalink(Site site) {

        String slug = StringUtils.toNormalized(headline);

        if (!StringUtils.isBlank(slug)) {
            return "/article/" + slug;
        } else {
            return null;
        }
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

    // --- Promotable support ---

    @Override
    public String getPromotableTitleFallback() {
        return getHeadline();
    }

    @Override
    public String getPromotableMobileTitleFallback() {
        return getMobileHeadline();
    }

    @Override
    public String getPromotableDescriptionFallback() {
        return getSubHeadline();
    }

    @Override
    public String getPromotableMobileDescriptionFallback() {
        return getMobileSubHeadline();
    }

    @Override
    public Image getPromotableImageFallback() {
        ArticleLead lead = getLead();
        return lead != null ? lead.getImage() : null;
    }

    // --- OpenGraphDefinable support ---

    @Override
    public OpenGraphType getOpenGraphType() {
        return OpenGraphType.ARTICLE;
    }

    // --- AODMessagable support ---

    @Override
    public String getMessage() {
        return getHeadline();
    }

    // --- UbikContent support ---

    @Override
    public String getCardTitle() {
        return getPromotableData().getTitle();
    }

    @Override
    public StorageItem getCardImage() {
        Image image = getPromotableData().getImage();
        return image != null ? image.getFile() : null;
    }

    @Override
    public String getCardBlurb() {
        return getPromotableData().getDescriptionAsText();
    }

    // --- UbikScreen support ---

    @Override
    public String getScreenTitle() {
        return getHeadline();
    }

    @Override
    public StorageItem getScreenImage() {
        Image image = getPromotableData().getImage();
        return image != null ? image.getFile() : null;
    }

    @Override
    public String getButtonText() {
        return null;
    }

    @Override
    public StorageItem getButtonImage() {
        return null;
    }

    // --- AOD Freemarker template support ---

    public String getAodBodyHtml() {
        StringBuilder bodyHtml = new StringBuilder();

        String siteUrl = Application.Static.getInstance(CmsTool.class).getDefaultSiteUrl();

        ReferentialText body = getBody();
        if (body != null) {
            for (Object item : body.toPublishables(new RichTextCleaner())) {
                if (item instanceof Reference) {
                    bodyHtml.append("<span class=\"bsp-enhancement ").append(((Reference) item).getId().toString()).append("\"></span>");

                } else {
                    String html = item.toString();

                    if (siteUrl != null) {
                        Document doc = Jsoup.parse(item.toString());

                        doc.select("a").forEach((element) -> {

                            String href = element.attr("href");
                            if (href != null && href.startsWith("/")) {
                                element.attr("href", StringUtils.removeEnd(siteUrl, "/") + href);
                            }
                        });

                        html = doc.body().html();
                    }

                    bodyHtml.append(html);
                }
            }
        }

        return bodyHtml.toString();
    }
}
