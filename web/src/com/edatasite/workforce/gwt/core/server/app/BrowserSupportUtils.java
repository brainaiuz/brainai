package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Nov 21, 2009
 * Time: 8:07:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class BrowserSupportUtils implements Constants {

    public static final double SAFARI = 4.02;
    public static final double IE = 7;
    public static final double FIREFOX = 2;
    public static final double CHROME = 3.01;

    private BrowserSupportUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean checkBrowserVersion(String userAgent) {
        boolean b = false;
        try {
            b = isSupportedSafari(userAgent) || isSupportedChrome(userAgent) || isSupportedIE(userAgent) || isSupportedFirefox(userAgent);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return b;
    }

    private static boolean isSupportedSafari(String str) {
        boolean supported = false;
        if (str != null && str.contains("Safari") && !str.contains("Chrome")) {
            int start = str.indexOf("Version/") + "Version/".length();
            int end = str.indexOf("Safari") - 1;
            String version = str.substring(start, end);
            version = version.substring(0, version.indexOf(".")) + "." + version.substring(version.indexOf(".")).replace(".", "");
            double doubleVal = Double.parseDouble(version);
            supported = doubleVal >= SAFARI;
        }
        return supported;
    }

    private static boolean isSupportedChrome(String str) {
        boolean supported = false;
        if (str != null && str.contains("Chrome")) {
            int start = str.indexOf("Chrome/") + "Chrome/".length();
            int end = str.indexOf("Safari") - 1;
            String version = str.substring(start, end);
            version = version.substring(0, version.indexOf(".")) + "." + version.substring(version.indexOf(".")).replace(".", "");
            double doubleVal = Double.parseDouble(version);
            supported = doubleVal >= CHROME;
        }
        return supported;
    }

    private static boolean isSupportedIE(String str) {
        boolean supported = false;
        if (str != null && str.contains("MSIE")) {
            int start = str.indexOf("MSIE") + "MSIE".length();
            int end = str.indexOf(".", start);
            String version = str.substring(start, end);
            double doubleVal = Double.parseDouble(version);
            supported = doubleVal >= IE;
        }
        return supported;
    }

    public static boolean isSupportedIE8(String str) {
        boolean supported = false;
        if (str != null && str.contains("MSIE")) {
            int start = str.indexOf("MSIE") + "MSIE".length();
            int end = str.indexOf(".", start);
            String version = str.substring(start, end);
            if (" 8".equals(version)) {
                supported = true;
            }
        }
        return supported;
    }

    private static boolean isSupportedFirefox(String str) {
        boolean supported = false;
        if (str != null && str.contains("Firefox")) {
            int start = str.indexOf("Firefox/") + "Firefox/".length();
            String version = str.substring(start);
            int end = version.indexOf(".") + 2;
            version = version.substring(0, end);
            double doubleVal = Double.parseDouble(version);
            supported = doubleVal >= FIREFOX;
        }
        return supported;
    }
}
