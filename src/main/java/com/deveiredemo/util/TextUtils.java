package com.deveiredemo.util;

public final class TextUtils {

    public static final String NULL_PLACEHOLDER_CHARACTER = ".";
    public static final String NULL_PLACEHOLDER_PATTERN = "[(<br\\h*/?>)\\s]*\\x5b[(<br\\h*/?>)\\s]*\\x5d[(<br\\h*/?>)\\s]*"; // []

    private TextUtils() {
    }

    public static String cleanPlaceholderText(String text) {
        return text != null && (text.equals(NULL_PLACEHOLDER_CHARACTER) || text.matches(NULL_PLACEHOLDER_PATTERN)) ? null : text;
    }
}
