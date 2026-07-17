package com.edatasite.workforce.gwt.core.client.ui.datePicker.util;

import com.google.gwt.i18n.client.DateTimeFormat;


public class DateLocale_ implements DateLocale {

    public int[] DAYS_ORDER = {0, 1, 2, 3, 4, 5, 6};

    public int[] getDAY_ORDER() {
        return DAYS_ORDER;
    }

    public DateTimeFormat getDateTimeFormat() {
        return DateTimeFormat.getFormat("MM/dd/yyyy");
    }
}
