package com.deveiredemo.view;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.CropOption;
import com.psddev.cms.db.RichTextCleaner;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.db.Reference;
import com.psddev.dari.db.ReferentialText;
import com.psddev.dari.util.StorageItem;
import com.psddev.handlebars.HandlebarsTemplate;
import com.psddev.util.ImageTagBuilder;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.ArticleLead;
import com.deveiredemo.model.Constants;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.Link;
import com.deveiredemo.model.Tag;
import com.deveiredemo.util.ImageUtils;
import com.deveiredemo.view.base.BspImageView;
import com.deveiredemo.view.base.BspPageTitleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@HandlebarsTemplate("components/bsp-article-lead-image-first")
public interface ArticleLeadImageFirstView {

    default Options getOptions() {
        return null;
    }

    String getPermalink();

    String getTitle();

    BspImageView getTitleImage();

    String getSubTitle();

    LeadImage getLeadImage();

    List<Author> getAuthors();

    List<ReferentialTextItemView> getBody();

    CallToAction getCta();

    SocialShareView getSharing();

    interface LeadImage {
    }

    interface Options {

        boolean getInfinite();
    }

    interface Author {

        String getName();

        LinkView getLink();
    }

    interface CallToAction {

        LinkView getContent();
    }

    static class FromArticle extends AbstractViewCreator<Article> implements ArticleLeadImageFirstView {

        private boolean isBannerView() {
            ArticleLead articleLead = model.getLead();
            return articleLead != null && articleLead.isSuperLead();
        }

        private String getSubHeadline() {
            return DeviceDetect.getDeviceValue(request,
                    model.getSubHeadline(),
                    model.getMobileSubHeadline());
        }

        @Override
        public String getPermalink() {
            return model.getPermalink();
        }

        @Override
        public String getTitle() {
            return DeviceDetect.getDeviceValue(request,
                    model.getHeadline(),
                    model.getMobileHeadline());
        }

        @Override
        public BspImageView getTitleImage() {
            Image logo = model.getHeadlineLogo();
            if (logo != null) {
                StorageItem file = logo.getFile();
                if (file != null) {
                    return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                            .height(40)
                            .cropOption(CropOption.NONE), logo);
                }
            }
            return null;
        }

        @Override
        public String getSubTitle() {

            if (!isBannerView()) {

                List<Tag> tags = model.getTaggableData().getTags();
                if (!tags.isEmpty()) {

                    Tag tag = tags.get(0);
                    if (tag != null && !tag.isInternal()) {

                        Content callToAction = tag.getCallToAction();
                        if (callToAction != null) {
                            String callToActionLink = callToAction.getPermalink();
                            if (callToActionLink != null) {
                                // TODO: Need to support this from FE.
                            }
                        }

                        return tag.getName();
                    }
                }
            }

            return null;
        }

        @Override
        public LeadImage getLeadImage() {

            if (isBannerView()) {

                return (ComponentGroupView) () -> {
                    List<Object> components = new ArrayList<>();

                    // add the banner title
                    String subHeadline = getSubHeadline();
                    if (subHeadline != null) {
                        components.add((BspPageTitleView) () -> () -> subHeadline);
                    }

                    // add the banner image
                    ArticleLead articleLead = model.getLead();
                    if (articleLead != null) {

                        Image leadImage = articleLead.getImage();
                        if (leadImage != null) {

                            StorageItem file = leadImage.getFile();
                            if (file != null) {

                                ImageTagBuilder builder = new ImageTagBuilder(file);

                                if (DeviceDetect.isMobile(request)) {
                                    builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                            Constants.STANDARD_IMAGE_SIZE_PROMO_SUPER_MOBILE_8_5, 8, 5, false));
                                } else {
                                    builder.standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                            Constants.STANDARD_IMAGE_SIZE_PROMO_SUPER_3_1, 3, 1, false));
                                }

                                builder.hideDimensions();

                                components.add(ImageView.fromImageTagBuilder(builder, leadImage));
                            }
                        }
                    }

                    return components;
                };

            } else {

                ArticleLead articleLead = model.getLead();
                if (articleLead != null) {

                    Image leadImage = articleLead.getImage();
                    if (leadImage != null) {

                        StorageItem file = leadImage.getFile();
                        if (file != null) {

                            return ImageView.fromImageTagBuilder(new ImageTagBuilder(file)
                                    .standardImageSize(ImageUtils.findStandardSizeWithAspectRatio(
                                            Constants.STANDARD_IMAGE_SIZE_ARTICLE_LEAD, 2, 1, false)), leadImage);
                        }
                    }
                }
            }

            return null;
        }

        @Override
        public List<Author> getAuthors() {

            String authorName;

            com.deveiredemo.model.Author author = model.getAuthor();
            if (author != null) {
                authorName = author.getName();

            } else {
                authorName = model.getOriginalSource();
            }

            if (authorName != null) {

                return Collections.singletonList(new Author() {
                    @Override
                    public String getName() {
                        return authorName;
                    }

                    @Override
                    public LinkView getLink() {
                        return (LinkView.TextOnly) () -> () -> authorName;
                    }
                });

            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public List<ReferentialTextItemView> getBody() {
            List<ReferentialTextItemView> bodyViews = new ArrayList<>();

            ReferentialText body = model.getBody();

            if (body != null) {
                for (Object item : body.toPublishables(new RichTextCleaner())) {

                    bodyViews.add(new ReferentialTextItemView() {

                        @Override
                        public Options getOptions() {
                            if (item instanceof String) {
                                return new HtmlOptions();

                            } else if (item instanceof Reference) {
                                Reference reference = (Reference) item;
                                return new Options() {
                                    @Override
                                    public boolean getEnhancement() {
                                        return true;
                                    }

                                    @Override
                                    public Alignment getAlign() {
                                        return Alignment.fromReference(reference);
                                    }
                                };
                            } else {
                                return null;
                            }
                        }

                        @Override
                        public Content getContent() {
                            if (item instanceof String) {
                                return (HtmlView) () -> (String) item;

                            } else if (item instanceof Reference) {
                                return request.createView(ReferentialTextItemView.Content.class, item);

                            } else {
                                return null;
                            }
                        }
                    });
                }
            }

            return bodyViews;
        }

        @Override
        public CallToAction getCta() {
            Link originalContent = model.getOriginalContent();
            return originalContent != null
                    ? () -> request.createView(LinkView.class, originalContent)
                    : null;
        }

        @Override
        public SocialShareView getSharing() {
            return request.createView(SocialShareView.class, model);
        }
    }
}
