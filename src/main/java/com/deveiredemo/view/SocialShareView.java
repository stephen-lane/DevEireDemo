package com.deveiredemo.view;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.Directory;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.StorageItem;

import com.deveiredemo.model.Article;
import com.deveiredemo.model.Image;
import com.deveiredemo.model.JsgSite;
import com.deveiredemo.model.OpenGraphDefinable;
import com.deveiredemo.model.Promotable;
import com.deveiredemo.view.base.BspSocialShareView;

import java.util.ArrayList;
import java.util.List;

public interface SocialShareView extends BspSocialShareView {

    static class FromObject<T> extends AbstractViewCreator<T> implements SocialShareView {

        @Override
        public String getSocial() {
            return "Share";
        }

        @Override
        public boolean getMonochrome() {
            return false;
        }

        @Override
        public String getTitle() {

            String title = null;

            if (model instanceof OpenGraphDefinable) {
                title = ((OpenGraphDefinable) model).getOpenGraphData().getOpenGraphTitle();
            }

            if (title == null && model instanceof Promotable) {
                title = ((Promotable) model).getPromotableData().getTitle();
            }

            return cleanString(title);
        }

        @Override
        public String getDescription() {

            String description = null;

            if (model instanceof OpenGraphDefinable) {
                description = ((OpenGraphDefinable) model).getOpenGraphData().getOpenGraphDescription();
            }

            if (description == null && model instanceof Promotable) {
                description = ((Promotable) model).getPromotableData().getDescriptionAsText();
            }

            return cleanString(description);
        }

        @Override
        public Long getFacebookId() {
            return ObjectUtils.to(Long.class, JsgSite.getInstance().getFacebookAppId());
        }

        @Override
        public String getIconClass() {
            return "icon";
        }

        @Override
        public String getImage() {
            if (model instanceof Promotable) {
                Image image = ((Promotable) model).getPromotableData().getImage();
                if (image != null) {
                    StorageItem file = image.getFile();
                    if (file != null) {
                        return file.getPublicUrl();
                    }
                }
            }
            return null;
        }

        @Override
        public String getUrl() {
            if (model instanceof Content) {
                return ((Content) model).as(Directory.ObjectModification.class).getFullPermalink();
            }
            return null;
        }

        @Override
        public String getRedirectUrl() {
            if (model instanceof Content) {
                return ((Content) model).as(Directory.ObjectModification.class).getFullPermalink();
            }
            return null;
        }

        @Override
        public List<String> getServices() {
            List<String> services = new ArrayList<>();
            if (getFacebookId() != null) {
                services.add("facebook");
            }
            services.add("twitter");
            services.add("pinterest");
            services.add("linkedin");

            return services;
        }

        // Escape the double quotes since the value is placed within double quotes of a JSON string.
        private String cleanString(String text) {
            if (text != null) {
                text = text.replaceAll("\\x22", "\\\\\"");
            }
            return text;
        }
    }

    static class FromArticle extends FromObject<Article> {

        @Override
        public boolean getMonochrome() {
            return true;
        }
    }
}
