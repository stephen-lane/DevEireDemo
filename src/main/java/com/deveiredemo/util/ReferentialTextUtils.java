package com.deveiredemo.util;

import com.psddev.dari.db.ReferentialText;
import com.psddev.dari.util.StringUtils;

import org.jsoup.Jsoup;

/**
 * Utility methods for dealing with ReferentialText objects.
 */
public final class ReferentialTextUtils {

    private ReferentialTextUtils() {
    }

    /**
     * Checks if the given referential text is blank.
     *
     * @param refText the object to check.
     * @return true if the referential text is effectively blank, false otherwise.
     */
    public static boolean isBlank(ReferentialText refText) {
        if (refText == null) {
            return true;
        } else {
            int size = refText.size();
            if (size == 0) {
                return true;
            } else if (size > 1) {
                return false;
            } else {
                Object item = refText.get(0);
                return item instanceof String && StringUtils.isBlank(((String) item).replaceAll("<br\\s*/?>", ""));
            }
        }
    }

    /**
     * Checks if the given "rich text" is blank. "Rich text" is a String field
     * that is annotated with {@link com.psddev.cms.db.ToolUi.RichText} and
     * receives the UI treatment as if it were a ReferentialText object.
     *
     * @param richText the rich text to check.
     * @return true if the rich text is effectively blank, false otherwise.
     */
    public static boolean isBlank(String richText) {
        return isBlank(fromHtmlString(richText));
    }

    /**
     * Converts a raw HTML String to a ReferentialText object.
     *
     * @param html the raw html to convert
     * @return a ReferentialText object of size one containing the html.
     */
    public static ReferentialText fromHtmlString(String html) {
        ReferentialText refText = new ReferentialText();
        if (!StringUtils.isBlank(html)) {
            refText.add(html);
        }
        return refText;
    }

    public static String stripHtml(String html) {
        return html != null ? Jsoup.parse(html).text() : null;
    }
}
