package com.deveiredemo.util;

import com.psddev.dari.util.PageContextFilter;

import javax.servlet.http.HttpServletRequest;

public final class ContentStateUtils {

    public static boolean isContentStateRequest() {
        HttpServletRequest request = PageContextFilter.Static.getRequestOrNull();
        return request != null && request.getServletPath().endsWith("/contentState");
    }
}
