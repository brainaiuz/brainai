package com.edatasite.workforce.gwt.core.client;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 21, 2008
 * Time: 4:33:44 PM
 * To change this template use File | Settings | File Templates.
 */

public class NumberUtils {

    public static NumberFormat numberFormat = NumberFormat.getFormat(",#.00");
    public static NumberFormat currencyFormat = NumberFormat.getFormat(",##0.00");
    public static NumberFormat percentageFormat = NumberFormat.getPercentFormat();

    public static double round(final double number, final int decimalPlaces) {
        switch (decimalPlaces) {
            case 1:
                return Math.round(number * 10) / 10.0;
            case 2:
                return Math.round(number * 100) / 100.0;
            default:
                return Math.round(number * 100) / 100.0;
        }
    }

    public static String getCurrencyFormat(double number, String currencyCode) {
        if (currencyCode == null) {
            currencyCode = Utils.getParam(Utils.BASE_CURRENCY);
        }
        return currencyCode + (number >= 0 ? " " : "(") + currencyFormat.format(number).substring(number >= 0 ? 0 : 1) + (number >= 0 ? "" : ")");
//        return currencyFormat.format(number);
    }

    public static String getCurrencyFormat(double number) {
        return getCurrencyFormat(number, null);
    }

    public static double parseCurrency(String text, String currencyCode) {
        String currencySymbol = currencyCode != null ? currencyCode : Utils.getParam(Utils.BASE_CURRENCY);
        final String value = text.replaceAll("$".equals(currencySymbol) ? "\\$" : currencySymbol, "");
        final boolean negative = value.contains("(");
        return currencyFormat.parse(negative ? value.replaceAll("[(]", "").replaceAll("[)]", "") : value) * (negative ? -1 : 1);
    }

    public static double parseCurrency(String text) {
        return parseCurrency(text, null);
    }



    public static String getNumberFormatWithBigDecimal(double value) {
        return currencyFormat.format(value);
    }
}


