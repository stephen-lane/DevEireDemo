package com.psddev.util.sitemap;

import com.psddev.dari.util.AbstractFilter;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.RoutingFilter;
import com.psddev.dari.util.Settings;

import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class SiteMapFilter extends AbstractFilter implements AbstractFilter.Auto {

    private static final String SITEMAP_XML_PATH_PATTERN = "/sitemap([\\d]*).xml";

    protected void doRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws Exception {

        String servletPath = request.getServletPath();

        if (servletPath.matches(SITEMAP_XML_PATH_PATTERN)) {

            // When not in production mode, the _generateSiteMap=true parameter will force a refresh of the sitemap.xml
            if (!Settings.isProduction() && ObjectUtils.to(boolean.class, request.getParameter("_generateSiteMap"))) {

                // TODO: Figure out how to force an update.

                if (Settings.isDebug()) {
                    response.setContentType("text/html");

                } else {
                    response.setContentType("text/plain");
                }

                response.getWriter().print("OK");

            } else { // return the data stored in the database.

                String requestUrl = request.getRequestURL().toString();

                String siteMapUrl = requestUrl.substring(0, requestUrl.length() - servletPath.length());

                Integer siteMapIndex = ObjectUtils.to(Integer.class, servletPath.replaceFirst(SITEMAP_XML_PATH_PATTERN, "$1"));

                SiteMap siteMap = SiteMapUtils.getSiteMap(siteMapUrl, siteMapIndex);

                if (siteMap != null) {
                    response.setContentType("text/xml");
                    response.getWriter().print(siteMap.getXml());

                } else {
                    response.sendError(404);
                }
            }

        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void updateDependencies(Class<? extends AbstractFilter> filterClass, List<Class<? extends javax.servlet.Filter>> dependencies) {
        if (RoutingFilter.class.isAssignableFrom(filterClass)) {
            dependencies.add(getClass());
        }
    }
}
