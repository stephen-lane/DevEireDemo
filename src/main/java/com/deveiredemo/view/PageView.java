package com.deveiredemo.view;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.db.ElFunctionUtils;
import com.psddev.cms.db.Seo;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Query;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;
import com.psddev.dari.util.StringUtils;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.ArticleLead;
import com.deveiredemo.model.Footer;
import com.deveiredemo.model.Gallery;
import com.deveiredemo.model.GalleryFallPage;
import com.deveiredemo.model.Header;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.JsgSite;
import com.deveiredemo.model.OpenGraphDefinable;
import com.deveiredemo.model.OpenGraphType;
import com.deveiredemo.model.PhotoFallPage;
import com.deveiredemo.model.Section;
import com.deveiredemo.model.TwitterCardDefinable;
import com.deveiredemo.util.ReferentialTextUtils;
import com.deveiredemo.view.base.BspComponentGroupView;
import com.deveiredemo.view.base.BspFooterView;
import com.deveiredemo.view.base.BspFullPageLayoutView;
import com.deveiredemo.view.base.BspHeaderView;
import com.deveiredemo.view.base.BspLayoutView;
import com.deveiredemo.view.base.BspOneColumnLayoutView;
import com.deveiredemo.view.base.BspPageTitleView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@HandlebarsTemplate("common/page.hbs")
public interface PageView extends com.psddev.cms.view.PageView {

    Map<String, String> getHeadAttributes();

    HeadView getHead();

    String getBodyCssClass();

    BodyView getBody();

    static class FromObject<T> extends AbstractViewCreator<T> implements PageView {

        private static final Logger LOGGER = LoggerFactory.getLogger(FromObject.class);

        @Override
        public Map<String, String> getHeadAttributes() {

            Map<String, String> attributes = new LinkedHashMap<>();

            if (model instanceof OpenGraphDefinable) {
                OpenGraphType ogType = ((OpenGraphDefinable) model).getOpenGraphData().getOpenGraphType();

                if (ogType != null) {
                    attributes.put("prefix", ogType.getHeadPrefix());
                }
            }

            return !attributes.isEmpty() ? attributes : null;
        }

        @Override
        public HeadView getHead() {
            return new HeadView() {

                @Override
                public String getCanonicalLink() {
                    if (model instanceof Content) {
                        return ((Content) model).as(Directory.ObjectModification.class).getFullPermalink();
                    }
                    return null;
                }

                @Override
                public String getTitle() {
                    if (model instanceof Content) {
                        return ((Content) model).as(Seo.ObjectModification.class).findTitle();
                    }
                    return null;
                }

                @Override
                public String getDescription() {
                    if (model instanceof Content) {
                        return ReferentialTextUtils.stripHtml(((Content) model).as(Seo.ObjectModification.class).findDescription());
                    }
                    return null;
                }

                @Override
                public String getKeywords() {
                    if (model instanceof Content) {
                        Set<String> keywords = ((Content) model).as(Seo.ObjectModification.class).getKeywords();
                        if (!ObjectUtils.isBlank(keywords)) {
                            return StringUtils.join(new ArrayList<>(keywords), ",");
                        }
                    }
                    return null;
                }

                @Override
                public List<Meta> getMeta() {

                    List<Meta> metaList = new ArrayList<>();

                    metaList.add(HeadView.createCustomMeta("http-equiv", "X-UA-Compatible", "content", "IE=edge"));
                    metaList.add(HeadView.createNamedMeta("viewport", "width=device-width, initial-scale=1"));

                    metaList.addAll(createOpenGraphMetas());
                    metaList.addAll(createTwitterCardMetas());

                    return metaList;
                }

                @Override
                public List<Icon> getIcon() {

                    return Arrays.asList(
                            HeadView.createFavicon(ElFunctionUtils.resource("/assets/icons/favicon.ico")),

                            HeadView.createFaviconResize(ElFunctionUtils.resource("/assets/icons/favicon-16x16.png"), 16, 16),
                            HeadView.createFaviconResize(ElFunctionUtils.resource("/assets/icons/favicon-32x32.png"), 32, 32),
                            HeadView.createFaviconResize(ElFunctionUtils.resource("/assets/icons/favicon-96x96.png"), 96, 96),

                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-57x57.png"), 57, 57),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-60x60.png"), 60, 60),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-72x72.png"), 72, 72),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-76x76.png"), 76, 76),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-114x114.png"), 114, 114),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-120x120.png"), 120, 120),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-144x144.png"), 144, 144),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-152x152.png"), 152, 152),
                            HeadView.createAppleTouchIcon(ElFunctionUtils.resource("/assets/icons/apple-icon-180x180.png"), 180, 180)
                    );
                }

                @Override
                public List<Style> getStyle() {
                    List<Style> styles = new ArrayList<>();

                    styles.add(HeadView.createSourceStyle(StringUtils.addQueryParameters(
                            "http://fonts.googleapis.com/css", "family", "Roboto:300|Monda:400")));
                    styles.add(HeadView.createSourceStyle(ElFunctionUtils.resource(
                            "/assets/styles/main.min.css")));

                    return styles;
                }

                @Override
                public List<Script> getScript() {

                    List<Script> scripts = new ArrayList<>();

                    for (String scriptSource : new String[] {
                            "/assets/scripts/es6-collections.js",
                            "/assets/scripts/system-polyfills.js",
                            "/assets/scripts/main.min.js"
                    }) {
                        scripts.add(HeadView.createSourceScript(ElFunctionUtils.resource(scriptSource)));
                    }

                    Script googleAnalytics = createGoogleAnalyticsScript();
                    if (googleAnalytics != null) {
                        scripts.add(googleAnalytics);
                    }

                    return scripts;
                }

                private List<Meta> createOpenGraphMetas() {

                    List<Meta> ogMetas = new ArrayList<>();

                    if (model instanceof OpenGraphDefinable) {

                        JsgSite siteConfig = JsgSite.getInstance();
                        if (siteConfig != null) {

                            String facebookAppId = siteConfig.getFacebookAppId();
                            if (facebookAppId != null) {
                                ogMetas.add(HeadView.createPropertyMeta("fb:app_id", facebookAppId));
                            }
                        }

                        OpenGraphDefinable.Data ogData = ((OpenGraphDefinable) model).getOpenGraphData();

                        String ogSiteName = ogData.getOpenGraphSiteName();
                        OpenGraphType ogType = ogData.getOpenGraphType();
                        String ogTitle = ogData.getOpenGraphTitle();
                        String ogDescription = ogData.getOpenGraphDescription();
                        Image ogImage = ogData.getOpenGraphImage();

                        if (ogSiteName != null) {
                            ogMetas.add(HeadView.createPropertyMeta("og:site_name", ogSiteName));
                        }

                        if (ogType != null) {
                            ogMetas.add(HeadView.createPropertyMeta("og:type", ogType.getPropertyValue()));
                        }

                        if (model instanceof Content) {
                            String url = ((Content) model).as(Directory.ObjectModification.class).getFullPermalink();
                            if (url != null) {
                                ogMetas.add(HeadView.createPropertyMeta("og:url", url));
                            }
                        }

                        if (ogTitle != null) {
                            ogMetas.add(HeadView.createPropertyMeta("og:title", ogTitle));
                        }

                        if (ogDescription != null) {
                            ogMetas.add(HeadView.createPropertyMeta("og:description", ogDescription));
                        }

                        if (ogImage != null) {
                            StorageItem file = ogImage.getFile();
                            if (file != null) {

                                String imageUrl = file.getPublicUrl();
                                if (imageUrl != null) {
                                    ogMetas.add(HeadView.createPropertyMeta("og:image", imageUrl));
                                }
                            }
                        }
                    }

                    return ogMetas;
                }

                private List<Meta> createTwitterCardMetas() {
                    List<Meta> metas = new ArrayList<>();

                    if (model instanceof TwitterCardDefinable) {
                        TwitterCardDefinable.Data twitterCardData = ((TwitterCardDefinable) model).getTwitterCardData();

                        String card = twitterCardData.getMetaTwitterCard();
                        String site = twitterCardData.getMetaTwitterSite();
                        String title = twitterCardData.getMetaTwitterTitle();
                        String description = twitterCardData.getMetaTwitterDescription();
                        String image = twitterCardData.getMetaTwitterImage();

                        if (card != null && site != null && title != null && description != null) {

                            metas.add(HeadView.createNamedMeta("twitter:card", card));
                            metas.add(HeadView.createNamedMeta("twitter:site", site));
                            metas.add(HeadView.createNamedMeta("twitter:title", title));
                            metas.add(HeadView.createNamedMeta("twitter:description", description));

                            if (image != null) {
                                metas.add(HeadView.createNamedMeta("twitter:image", image));
                            }
                        }
                    }

                    return metas;
                }

                private Script createGoogleAnalyticsScript() {
                    JsgSite siteConfig = JsgSite.getInstance();
                    if (siteConfig != null) {

                        String googleAnalyticsKey = siteConfig.getGoogleAnalyticsKey();
                        if (googleAnalyticsKey != null) {

                            return HeadView.createInlineScript(
                                    "var _gaq = _gaq || [];\n"
                                    + "_gaq.push(['_setAccount','" + googleAnalyticsKey + "']);\n"
                                    + "_gaq.push(['_trackPageview']);\n"
                                    + "(function() {\n"
                                    + "    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;\n"
                                    + "    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';\n"
                                    + "    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);\n"
                                    + "})();");
                        }
                    }

                    return null;
                }
            };
        }

        @Override
        public String getBodyCssClass() {
            return null;
        }

        @Override
        public final BodyView getBody() {

            return new BodyView() {

                @Override
                public BspHeaderView getHeaderView() {
                    return createHeaderView();
                }

                @Override
                public BspLayoutView getMainView() {
                    return createMainView();
                }

                @Override
                public BspFooterView getFooterView() {
                    return createFooterView();
                }
            };
        }

        /**
         * Sub-classes may override this method to provide the page title to be
         * used. It's normally displayed as a big orange banner right below the header.
         *
         * @return the title of the page.
         */
        protected String createPageTitle() {
            return null;
        }

        protected BspHeaderView createHeaderView() {

            String pageTitle = createPageTitle();

            if (pageTitle == null) {
                return request.createView(HeaderView.class, Query.from(Header.class).first());

            } else {

                HeaderView header = request.createView(HeaderView.class, Query.from(Header.class).first());
                return (BspComponentGroupView) () ->Arrays.asList(
                                header,
                                (BspPageTitleView) () -> () -> pageTitle);
            }
        }

        protected BspLayoutView createMainView() {
            Object mainView = createMainView(model, request);
            if (mainView instanceof BspLayoutView) {
                return (BspLayoutView) mainView;

            } else {
                if (mainView != null) {
                    LOGGER.warn("Expected view of type [BspLayoutView] but got ["
                            + mainView.getClass().getName() + "] instead.");
                }
                return null;
            }
        }

        protected BspFooterView createFooterView() {
            return request.createView(FooterView.class, Query.from(Footer.class).first());
        }
    }

    /**
     * Article specific PageView creator.
     */
    static class FromArticle extends FromObject<Article> {

        @Override
        public BspLayoutView createMainView() {

            Object mainView = request.createView(ArticleLeadImageFirstView.class, model);

            ArticleLead lead = model.getLead();
            if (lead != null && lead.isSuperLead()) {
                return (BspFullPageLayoutView) () -> () -> mainView != null ? Arrays.asList(mainView) : Collections.emptyList();

            } else {
                return (BspOneColumnLayoutView) () -> () -> mainView != null ? Arrays.asList(mainView) : Collections.emptyList();
            }
        }
    }

    /**
     * Gallery specific PageView creator.
     */
    static class FromGallery extends FromObject<Gallery> {

        @Override
        public String getBodyCssClass() {
            return "bsp-fullscreen bsp-gallery-fullscreen";
        }
    }

    /**
     * Image specific PageView creator.
     */
    static class FromImage extends FromObject<Image> {

        @Override
        public String getBodyCssClass() {
            return "bsp-fullscreen bsp-gallery-fullscreen";
        }
    }

    /**
     * Section specific PageView creator.
     */
    static class FromSection extends FromObject<Section> {

        @Override
        public String getBodyCssClass() {
            return "section-jsg";
        }

        @Override
        public String createPageTitle() {
            if (!"/".equals(model.getPermalink())) {
                return model.getTitle();
            }
            return null;
        }
    }

    /**
     * GalleryFallPage specific PageView creator.
     */
    static class FromGalleryFallPage extends FromObject<GalleryFallPage> {

        @Override
        public String getBodyCssClass() {
            return "bsp-gallery-section";
        }

        @Override
        protected BspComponentGroupView createHeaderView() {

            HeaderView header = request.createView(HeaderView.class, Query.from(Header.class).first());

            TabNavigationView tabs = () -> {
                PhotoFallPage photoFall = Query.from(PhotoFallPage.class).first();
                return Arrays.asList(
                        LinkView.TextLink.create(model::getSubTitle, LinkView.createAttributes(model, "class", "active")),
                        LinkView.TextLink.create(photoFall::getSubTitle, LinkView.createAttributes(photoFall)));
            };

            return () -> Arrays.asList(
                            header,
                            (BspPageTitleView) () -> model::getTitle,
                            tabs);
        }
    }

    /**
     * PhotoFallPage specific PageView creator.
     */
    static class FromPhotoFallPage extends FromObject<PhotoFallPage> {

        @Override
        public String getBodyCssClass() {
            return "bsp-gallery-section";
        }

        @Override
        protected BspComponentGroupView createHeaderView() {

            HeaderView header = request.createView(HeaderView.class, Query.from(Header.class).first());

            TabNavigationView tabs = () -> {
                GalleryFallPage galleryFall = Query.from(GalleryFallPage.class).first();
                return Arrays.asList(
                        LinkView.TextLink.create(galleryFall::getSubTitle, LinkView.createAttributes(galleryFall)),
                        LinkView.TextLink.create(model::getSubTitle, LinkView.createAttributes(model, "class", "active")));
            };

            return () -> Arrays.asList(
                    header,
                    (BspPageTitleView) () -> model::getTitle,
                    tabs);
        }
    }
}
