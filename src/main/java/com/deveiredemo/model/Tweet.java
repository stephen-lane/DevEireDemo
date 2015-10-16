package com.deveiredemo.model;

import com.psddev.aod.UbikContent;
import com.psddev.cms.db.Content;
import com.psddev.cms.db.ExternalContent;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.util.CollectionUtils;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.social.SocialIngestible;

import java.util.List;
import java.util.Map;

/**
 * Adds ExternalContent support for great control over embedding tweets.
 */
@Content.Searchable
@ToolUi.Referenceable(via = TweetEnhancement.class)
public class Tweet extends com.psddev.social.Tweet implements UbikContent {

    // Ex. https://twitter.com/JordanSpieth/status/623962796938506240
    public static final String TWITTER_STATUS_URL_FORMAT = "https://twitter.com/%s/status/%s";

    private static final String INGEST_DATA_ID_PATH = "id_str";
    private static final String INGEST_DATA_USER_SCREEN_NAME_PATH = "user/screen_name";

    @Embedded
    @ToolUi.Hidden
    private ExternalContent externalContent;

    public ExternalContent getExternalContent() {
        return externalContent;
    }

    public void setExternalContent(ExternalContent externalContent) {
        this.externalContent = externalContent;
    }

    public String getEmbedHtml() {
        return getOembedMetadata(String.class, Constants.OEMBED_HTML_KEY);
    }

    @Override
    public void beforeSave() {
        super.beforeSave();

        if (externalContent == null) {
            Map<String, Object> ingestedData = as(SocialIngestible.Data.class).getServiceProvidedData();

            String tweetId = ObjectUtils.to(String.class, CollectionUtils.getByPath(ingestedData, INGEST_DATA_ID_PATH));
            String userName = ObjectUtils.to(String.class, CollectionUtils.getByPath(ingestedData, INGEST_DATA_USER_SCREEN_NAME_PATH));

            if (tweetId != null && userName != null) {
                externalContent = new ExternalContent();
                externalContent.setUrl(String.format(TWITTER_STATUS_URL_FORMAT, userName, tweetId));
            }
        }
    }

    public String getUserId() {
        return getSocialMetadata(String.class, "user/id_str");
    }

    public String getUserProfileImageUrl() {
        String url = getSocialMetadata(String.class, "user/profile_image_url");
        if (url != null) {
            url = url.replace("_normal", "");
        }
        return url;
    }

    public String getUserProfileImageThumbUrl() {
        return getSocialMetadata(String.class, "user/profile_image_url");
    }

    public String getTweetId() {
        return getSocialMetadata(String.class, "id_str");
    }

    public String getName() {
        return getSocialMetadata(String.class, "user/name");
    }

    public String getScreenName() {
        return getSocialMetadata(String.class, "user/screen_name");
    }

    public int getRetweetCount() {
        return getSocialMetadata(int.class, "retweet_count");
    }

    public int getFavoriteCount() {
        return getSocialMetadata(int.class, "favorite_count");
    }

    public String getTweetImageUrl() {
        Object media = getSocialMetadata("entities/media");

        if (media instanceof List) {
            List<?> list = (List<?>) media;
            if (!list.isEmpty()) {
                Object item = list.get(0);
                if (item instanceof Map) {
                    return ObjectUtils.to(String.class, CollectionUtils.getByPath(item, "media_url"));
                }
            }
        }

        return null;
    }

    private Object getSocialMetadata(String key) {
        return CollectionUtils.getByPath(as(SocialIngestible.Data.class).getServiceProvidedData(), key);
    }

    private <T> T getSocialMetadata(Class<T> returnType, String key) {
        return ObjectUtils.to(returnType, CollectionUtils.getByPath(as(SocialIngestible.Data.class).getServiceProvidedData(), key));
    }

    private <T> T getOembedMetadata(Class<T> returnType, String key) {
        Map<String, Object> response;
        if (externalContent != null && (response = externalContent.getResponse()) != null) {
            return ObjectUtils.to(returnType, CollectionUtils.getByPath(response, key));
        }
        return null;
    }
}
