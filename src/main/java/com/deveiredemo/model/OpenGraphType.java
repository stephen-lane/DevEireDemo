package com.deveiredemo.model;

/**
 * Represents the values of the <a href="http://ogp.me/">Open Graph Protocol</a>
 * og:type property.
 */
public enum OpenGraphType {

    ARTICLE("Article", "article", "http://ogp.me/ns/article#"),
    WEBSITE("Website", "website", "http://ogp.me/ns/website#");

    private static final String HEAD_PREFIX_FORMAT = "og: http://ogp.me/ns# fb: http://ogp.me/ns/fb# %s: %s";

    private String label;

    private String propertyValue;

    private String namespaceUri;

    private OpenGraphType(String label, String propertyValue, String namespaceUri) {
        this.label = label;
        this.propertyValue = propertyValue;
        this.namespaceUri = namespaceUri;
    }

    /**
     * @return the display label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the value for the og:type property.
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * @return the namespace URI for this type.
     */
    public String getNamespaceUri() {
        return namespaceUri;
    }

    /**
     * @return the value to be placed in the HTML head element's prefix property.
     */
    public String getHeadPrefix() {
        return String.format(HEAD_PREFIX_FORMAT, getPropertyValue(), getNamespaceUri());
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
