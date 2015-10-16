package com.deveiredemo.view;

import com.psddev.cms.view.AbstractViewCreator;

import com.google.common.collect.ImmutableMap;
import com.deveiredemo.model.ExternalLink;
import com.deveiredemo.model.LinkTarget;
import com.deveiredemo.model.SocialLink;

import java.util.Map;

public interface SocialLinkView extends LinkView {

    String getIcon();

    static class FromSocialLink extends AbstractViewCreator<SocialLink> implements SocialLinkView {

        @Override
        public String getCssClass() {
            return null;
        }

        /**
         * Gets the social icon name.
         *
         * @return the social icon name.
         */
        @Override
        public String getIcon() {
            switch (model.getIcon()) {
                case FACEBOOK:
                    return "facebook";
                case TWITTER:
                    return "twitter";
                case PINTEREST:
                    return "pinterest";
                case LINKED_IN:
                    return "linkedin";
                case INSTAGRAM:
                    return "instagram";
                case GOOGLE_PLUS:
                    return "googleplus";
                case EMAIL:
                    return "email";
                default:
                    return "";
            }
        }

        @Override
        public Map<String, String> getAttributes() {

            ExternalLink externalLink = model.getLink();
            if (externalLink != null) {

                String linkTitle;
                switch (model.getIcon()) {
                    case FACEBOOK:
                        linkTitle = "Facebook";
                        break;
                    case TWITTER:
                        linkTitle = "Twitter";
                        break;
                    case PINTEREST:
                        linkTitle = "Pinterest";
                        break;
                    case LINKED_IN:
                        linkTitle = "LinkedIn";
                        break;
                    case EMAIL:
                        linkTitle = "Email";
                        break;
                    default:
                        linkTitle = "";
                }

                String target = "";
                LinkTarget linkTarget = externalLink.getTarget();
                if (linkTarget != null) {
                    target = linkTarget.getHtmlAttributeValue();
                }

                return ImmutableMap.of(
                        "href", externalLink.getUrl(),
                        "title", linkTitle,
                        "target", target);
            } else {
                return null;
            }
        }

        @Override
        public Object getBody() {
            return null;
        }
    }
}
