package com.deveiredemo.model;

import com.psddev.cms.db.Content;
import com.psddev.cms.db.ToolUi;
import com.psddev.dari.db.Recordable;
import com.psddev.dari.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Recordable.Embedded
public class Promo extends Content implements Promotable {

    private LinkOrText slug;

    @Required
    private Link link;

    private String callToActionText;

    @ToolUi.Note("ONLY applies to Super Promo")
    @ToolUi.DisplayLast
    private List<Link> additionalLinks;

    public LinkOrText getSlug() {
        return slug;
    }

    public void setSlug(LinkOrText slug) {
        this.slug = slug;
    }

    /**
     * Gets the link of the Promo
     * @return link. Can be {@code null}
     */
    public Link getLink() {
        return link;
    }

    /**
     * Sets the link of the Promo
     * @param link the link to set.
     */
    public void setLink(Link link) {
        this.link = link;
    }

    public String getCallToActionText() {
        return callToActionText;
    }

    public void setCallToActionText(String callToActionText) {
        this.callToActionText = callToActionText;
    }

    public List<Link> getAdditionalLinks() {
        if (additionalLinks == null) {
            additionalLinks = new ArrayList<>();
        }
        return additionalLinks;
    }

    public void setAdditionalLinks(List<Link> additionalLinks) {
        this.additionalLinks = additionalLinks;
    }

    // --- Record Overrides ---

    /**
     * Gets the label for the Promo. If there is a link populated, it will try
     * to get the title of the link's promotable data {@code title} field. If that is null,
     * it will try to get the text of the link associated.
     *
     * @return label for the Promo. Can be {@code null}
     */
    @Override
    public String getLabel() {
        Promotable promo = getPromotableReference();

        if (promo != null) {
            String promoTitle = promo.getPromotableData().getTitle();

            if (!StringUtils.isBlank(promoTitle)) {
                return promoTitle;
            }
        } else if (link != null) {
            return link.getText();
        }

        return null;
    }

    // --- Promotable Support ---

    /**
     * @return The content Referenced by the {@code link} if it is of type {@link Promotable}. {@code null} otherwise.
     */
    @Override
    public Promotable getPromotableReference() {

        Link link = getLink();

        if (link instanceof ContentLink) {
            Content content = ((ContentLink) link).getContent();
            return content instanceof Promotable
                    ? (Promotable) content
                    : null;
        }

        return null;
    }

    @Override
    public Link getPromotableLinkFallback() {
        return link;
    }

    @Override
    public Image getPromotableImageFallback() {

        Promotable promoReference = getPromotableReference();
        if (promoReference != null) {
            return promoReference.getPromotableData().getImage();
        }

        return null;
    }

    @Override
    public String getPromotableTitleFallback() {

        Promotable promoReference = getPromotableReference();
        if (promoReference != null) {

            return promoReference.getPromotableData().getTitle();
        }

        return null;
    }

    @Override
    public String getPromotableDescriptionFallback() {

        Promotable promoReference = getPromotableReference();
        if (promoReference != null) {

            return promoReference.getPromotableData().getDescription();
        }

        return null;
    }
}
