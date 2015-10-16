package com.deveiredemo.view;

import com.psddev.cms.db.PageFilter;
import com.psddev.cms.db.Profile;
import com.psddev.cms.view.ViewRequest;
import com.psddev.dari.util.ObjectUtils;
import com.psddev.dari.util.PageContextFilter;

import com.deveiredemo.model.Promotable;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

public final class DeviceDetect {

    private DeviceDetect() {
    }

    /**
     * Returns true if the view is being rendered on a mobile web device.
     *
     * @param request the current view request.
     * @return true for mobile, false otherwise.
     */
    public static boolean isMobile(ViewRequest request) {

        boolean isMobile = false;

        HttpServletRequest httpRequest = PageContextFilter.Static.getRequestOrNull();
        if (httpRequest != null) {

            Profile profile = PageFilter.Static.getProfile(httpRequest);
            if (profile != null) {
                isMobile = profile.isUserAgentIphone() || isUserAgentAndroidMobile(profile);
            }
        }

        String device = request.getParameter(String.class, "_device").findFirst().orElse(null);
        if (device != null) {
            isMobile = "mobile".equals(device);
        }

        return isMobile;
    }

    private static boolean isUserAgentAndroidMobile(Profile profile) {
        String userAgent = profile.getUserAgent();

        return userAgent != null
                && userAgent.toLowerCase(Locale.ENGLISH).contains("android")
                && userAgent.toLowerCase(Locale.ENGLISH).contains("mobile");
    }

    /**
     * Returns a device specific value based on whether the current request is
     * for desktop or mobile. If the mobile value is blank, the desktop value
     * is returned instead.
     *
     * @param request the current view request.
     * @param desktopValue the desktop value.
     * @param mobileValue the mobile value.
     * @param <T> the type of value.
     * @return the device specific value.
     */
    public static <T> T getDeviceValue(ViewRequest request, T desktopValue, T mobileValue) {
        return isMobile(request) && !ObjectUtils.isBlank(mobileValue) ? mobileValue : desktopValue;
    }

    /**
     * Gets the title of the promotable based on the current device.
     *
     * @param request the current view request.
     * @param promotable the promo data to access.
     * @return the device appropriate promo title.
     */
    public static String getPromoTitle(ViewRequest request, Promotable promotable) {
        return isMobile(request)
                ? promotable.getPromotableData().getMobileTitle()
                : promotable.getPromotableData().getTitle();
    }

    /**
     * Gets the description of the promotable based on the current device.
     *
     * @param request the current view request.
     * @param promotable the promo data to access.
     * @return the device appropriate promo description.
     */
    public static String getPromoDescription(ViewRequest request, Promotable promotable) {
        return isMobile(request)
                ? promotable.getPromotableData().getMobileDescription()
                : promotable.getPromotableData().getDescription();
    }
}
