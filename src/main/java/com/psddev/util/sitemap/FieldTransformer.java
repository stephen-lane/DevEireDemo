package com.psddev.util.sitemap;

import com.google.common.base.CharMatcher;

interface FieldTransformer {

    static final CharMatcher CHARS_TO_REMOVE = CharMatcher.anyOf("\r\n\t").negate();
    static final CharMatcher CLEAN_STRING = CharMatcher.JAVA_ISO_CONTROL.and(CHARS_TO_REMOVE);

    Object transform(Object object);

    /**
     * Removes control characters from the provided String, with the exception
     * of newlines and tabs.
     *
     * @param text the String to clean.
     * @return cleaned copy of the text with control characters removed.
     */
    static String cleanString(String text) {
        return text != null ? CLEAN_STRING.removeFrom(text) : null;
    }
}
