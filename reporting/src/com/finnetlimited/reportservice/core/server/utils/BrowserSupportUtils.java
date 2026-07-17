package com.finnetlimited.reportservice.core.server.utils;

import com.finnetlimited.reportservice.core.client.ui.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 15.06.2010
 * Time: 15:40:30
 * To change this template use File | Settings | File Templates.
 */
public class BrowserSupportUtils implements Constants {

    public static final double SAFARI = 4.02;
    public static final double IE = 7;
    public static final double FIREFOX = 2;
    public static final double CHROME = 3.01;

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
        if (str.contains("Safari") && !str.contains("Chrome")) {
            int start = str.indexOf("Version/") + "Version/".length();
            int end = str.indexOf("Safari") - 1;
            String version = str.substring(start, end);
            version = version.substring(0, version.indexOf(".")) + "." + version.substring(version.indexOf(".")).replace(".", "");
            Double doubleVal = Double.valueOf(version);
            supported = doubleVal >= SAFARI;
        }
        return supported;
    }

    private static boolean isSupportedChrome(String str) {
        boolean supported = false;
        if (str.contains("Chrome")) {
            int start = str.indexOf("Chrome/") + "Chrome/".length();
            int end = str.indexOf("Safari") - 1;
            String version = str.substring(start, end);
            version = version.substring(0, version.indexOf(".")) + "." + version.substring(version.indexOf(".")).replace(".", "");
            Double doubleVal = Double.valueOf(version);
            supported = doubleVal >= CHROME;
        }
        return supported;
    }

    private static boolean isSupportedIE(String str) {
        boolean supported = false;
        if (str.contains("MSIE")) {
            int start = str.indexOf("MSIE") + "MSIE".length();
            int end = str.indexOf(".", start);
            String version = str.substring(start, end);
            Double doubleVal = Double.valueOf(version);
            supported = doubleVal >= IE;
        }
        return supported;
    }

    private static boolean isSupportedFirefox(String str) {
        boolean supported = false;
        if (str.contains("Firefox")) {
            int start = str.indexOf("Firefox/") + "Firefox/".length();
            String version = str.substring(start);
            int end = version.indexOf(".") + 2;
            version = version.substring(0, end);
            Double doubleVal = Double.valueOf(version);
            supported = doubleVal >= FIREFOX;
        }
        return supported;
    }
}
