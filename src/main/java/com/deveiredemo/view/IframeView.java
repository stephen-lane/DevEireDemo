package com.deveiredemo.view;

import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.HtmlWriter;
import com.psddev.dari.util.PageContextFilter;
import com.psddev.handlebars.HandlebarsTemplate;

import com.deveiredemo.model.Constants;
import com.deveiredemo.model.ExternalContentModification;
import com.deveiredemo.model.HtmlEmbedVideo;
import com.deveiredemo.model.HtmlEmbedVideoEnhancement;
import com.deveiredemo.model.Tweet;
import com.deveiredemo.model.TweetEnhancement;
import com.deveiredemo.model.YouTubeVideo;
import com.deveiredemo.model.YouTubeVideoEnhancement;

import java.io.IOException;
import java.io.StringWriter;

@HandlebarsTemplate("components/jsg-iframe")
public interface IframeView extends ReferentialTextItemView.Content {

    Options getOptions();

    String getContent();

    String getTitle();

    interface Options {

        default String getContentAlign() {
            return null;
        }

        default Integer getContentWidth() {
            return null;
        }

        default Boolean getContentStretch() {
            return null;
        }

        default Boolean getContentStage() {
            return null;
        }

        default String getContentStageBackgroundColor() {
            return null;
        }

        default Boolean getHideMobile() {
            return null;
        }
    }

    static class FromTweetEnhancement extends AbstractViewCreator<TweetEnhancement> implements IframeView {

        @Override
        public Options getOptions() {
            return new Options() {
                @Override
                public String getContentAlign() {
                    return "center";
                }

                @Override
                public Boolean getHideMobile() {
                    return true;
                }
            };
        }

        @Override
        public String getContent() {
            Tweet tweet = model.getObject();
            if (tweet != null) {
                return tweet.getEmbedHtml();
            }
            return null;
        }

        @Override
        public String getTitle() {
            return model.getTitle();
        }
    }

    static class FromHtmlEmbedVideoEnhancement extends AbstractViewCreator<HtmlEmbedVideoEnhancement> implements IframeView {

        @Override
        public Options getOptions() {
            return new Options() {

                @Override
                public String getContentAlign() {
                    return "center";
                }

                @Override
                public Boolean getContentStretch() {
                    return true;
                }

                @Override
                public Boolean getHideMobile() {
                    return model.isHideOnMobile();
                }
            };
        }

        @Override
        public String getContent() {
            HtmlEmbedVideo video = model.getObject();
            if (video != null) {
                return video.getEmbedCode();
            }
            return null;
        }

        @Override
        public String getTitle() {
            String title = model.getTitle();
            if (title == null) {
                HtmlEmbedVideo video = model.getObject();
                if (video != null) {
                    title = video.getTitle();
                }
            }

            if (Constants.EMPTY_PLACEHOLDER_KEY.equals(title)) {
                title = null;
            }

            return title;
        }
    }

    static class FromYouTubeVideoEnhancement extends AbstractViewCreator<YouTubeVideoEnhancement> implements IframeView {

        @Override
        public Options getOptions() {
            return new Options() {
                @Override
                public String getContentAlign() {
                    return "center";
                }

                @Override
                public Boolean getContentStretch() {
                    return true;
                }

                @Override
                public Boolean getHideMobile() {
                    return model.isHideOnMobile();
                }
            };
        }

        @Override
        public String getTitle() {

            String title = model.getTitle();
            if (title == null) {
                YouTubeVideo video = model.getObject();
                if (video != null) {
                    title = video.getTitle();
                }
            }

            if (Constants.EMPTY_PLACEHOLDER_KEY.equals(title)) {
                title = null;
            }

            return title;
        }

        @Override
        public String getContent() {

            YouTubeVideo video = model.getObject();
            if (video != null) {
                return getExternalContentHtml(video.getExternalContent());
            }

            return null;
        }
    }

    static class FromExternalContent extends AbstractViewCreator<ExternalContent> implements IframeView {

        @Override
        public Options getOptions() {
            return new Options() {
                @Override
                public String getContentAlign() {
                    return "center";
                }

                @Override
                public Boolean getHideMobile() {
                    return model.as(ExternalContentModification.class).isHideOnMobile();
                }
            };
        }

        @Override
        public String getTitle() {
            return model.as(ExternalContentModification.class).getTitle();
        }

        @Override
        public String getContent() {
            return getExternalContentHtml(model);
        }
    }

    public static String getExternalContentHtml(ExternalContent externalContent) {
        if (externalContent != null) {

            StringWriter html = new StringWriter();
            try {
                externalContent.renderObject(
                        PageContextFilter.Static.getRequestOrNull(),
                        PageContextFilter.Static.getResponseOrNull(),
                        new HtmlWriter(html));

                return html.toString();

            } catch (IOException e) {
                // do nothing
            }
        }
        return null;
    }
}
