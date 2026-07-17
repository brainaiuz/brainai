package com.finnetlimited.reportservice.core.server.utils;

/**
 * User: kotabek
 * Date: 02.01.13 9:21
 */
public final class StrUtils {
    /**
     * String is null or empty
     *
     * @param str testing string
     * @return
     */
    public static boolean isEmpty(String str) {
        return isEmpty(str, true);
    }

    /**
     * String is null or empty
     *
     * @param str  testing string
     * @param trim trim string in validating
     * @return
     */
    public static boolean isEmpty(String str, boolean trim) {
        if (str == null || str.length() == 0) {
            return true;
        }
        if (trim) {
            return isEmpty(str.trim(), false);
        }
        return false;
    }

    public static String replaceCommaToUnderscroll(String str) {
        if (isEmpty(str)) {
            return str;
        }
        str = str.replace(".", "_");
        if (str.contains(".")) {
            return replaceCommaToUnderscroll(str);
        }
        return str;
    }

    public static String join(String[] array, String separate) {
        return join(array, separate, 0, array.length);
    }

    public static String join(String[] array, String separate, int startIndex, int endIndex) {
        if (array == null || array.length == 0 || isEmpty(separate)) {
            return "";
        }
        if (startIndex < 0) {
            startIndex = 0;
        }
        if (endIndex >= array.length) {
            endIndex = array.length - 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i <= endIndex; i++) {
            if (sb.length() > 1) {
                sb.append(separate);
            }
            sb.append(array[i]);
        }

        return sb.toString();
    }

    public static String trimStart(String str, String trimStr) {
        if (isEmpty(str) || isEmpty(trimStr)) {
            return str;
        }
        if (str.startsWith(trimStr)) {
            return trimStart(str.substring(trimStr.length(), str.length()), trimStr);
        }
        return str;
    }
}
