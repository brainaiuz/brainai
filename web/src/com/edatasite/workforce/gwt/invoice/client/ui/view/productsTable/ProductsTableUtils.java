package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;

import java.math.BigDecimal;

public class ProductsTableUtils {
    private static WfmStrings wfmStrings = WfmStrings.App.get();
    public static String getQuantityAsString(String text) {
        return text.equals(wfmStrings.notAvailable()) || text.equals("") ? "0" : (text.indexOf(':') == -1 ? text : getHourValue(text));
    }

    public static String getHourValue(String text) {
        String[] time = text.split(":");
        return (Double.parseDouble(time[0]) + Double.parseDouble(time[1]) / 60) + "";
    }

    public static BigDecimal getQuantity(String text) {
        return AccountingUtils.get().parseToBigDecimal(getQuantityAsString(text));
    }

}
