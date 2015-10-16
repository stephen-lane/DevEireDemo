package com.deveiredemo.view;

import com.psddev.cms.db.Content;
import com.psddev.cms.view.AbstractViewCreator;
import com.psddev.dari.util.StringUtils;

import com.deveiredemo.model.ExternalLink;
import com.deveiredemo.model.Link;
import com.deveiredemo.model.LinkTarget;
import com.deveiredemo.view.base.BspLinkView;

import java.util.LinkedHashMap;
import java.util.Map;

public interface LinkView extends BspLinkView,
                                  LinkOrTextView,
                                  LinkOrHtmlView,
                                  LinkOrImageView {

    /**
     * A BspLinkView that only prints a basic text link.
     */
    interface TextLink extends LinkView {

        @Override
        TextView getBody();

        @Override
        default String getCssClass() {
            return null;
        }

        @Override
        Map<String, String> getAttributes();

        static TextLink create(TextView body, Map<String, String> attributes) {
            return new TextLink() {
                @Override
                public TextView getBody() {
                    return body;
                }

                @Override
                public Map<String, String> getAttributes() {
                    return attributes;
                }
            };
        }
    }

    /**
     * A BspLinkView that only prints a basic image link.
     */
    interface ImageLink extends LinkView, GalleryGridThumbView {

        @Override
        ImageView getBody();

        @Override
        default String getCssClass() {
            return null;
        }

        @Override
        Map<String, String> getAttributes();
    }

    /**
     * A BspLinkView that only prints text (no link).
     */
    interface TextOnly extends LinkView {

        @Override
        TextView getBody();

        @Override
        default String getCssClass() {
            return null;
        }

        @Override
        default Map<String, String> getAttributes() {
            return null;
        }
    }

    /**
     * A BspLinkView that only prints an image (no link).
     */
    interface ImageOnly extends LinkView {

        @Override
        ImageView getBody();

        @Override
        default String getCssClass() {
            return null;
        }

        @Override
        default Map<String, String> getAttributes() {
            return null;
        }
    }

    // --- ViewCreators ---

    static class FromLink extends AbstractViewCreator<Link> implements LinkView {

        @Override
        public String getCssClass() {
            return null;
        }

        @Override
        public Map<String, String> getAttributes() {
            return createAttributes(model);
        }

        @Override
        public TextView getBody() {
            return model::getText;
        }
    }

    static Map<String, String> createAttributes(Object object, String... keyValuePairs) {

        String href = createHref(object);
        if (href != null) {

            Map<String, String> attributes = new LinkedHashMap<>();

            attributes.put("href", href);

            // put the target for external links
            if (object instanceof ExternalLink) {
                LinkTarget target = ((ExternalLink) object).getTarget();
                if (target != null) {
                    attributes.put("target", target.getHtmlAttributeValue());
                }
            }

            if (keyValuePairs != null) {
                for (int i = 1; i < keyValuePairs.length; i += 2) {
                    String key = keyValuePairs[i - 1];
                    String value = keyValuePairs[i];

                    if (!StringUtils.isBlank(key)) {
                        attributes.put(key, value);
                    }
                }
            }

            return attributes;

        } else {
            return null;
        }
    }

    static String createHref(Object href) {
        if (href instanceof String) {
            return (String) href;

        } else if (href instanceof Link) {
            return createHref(((Link) href).getHref());

        } else if (href instanceof Content) {
            return ((Content) href).getPermalink();

        } else {
            return null;
        }
    }
}
