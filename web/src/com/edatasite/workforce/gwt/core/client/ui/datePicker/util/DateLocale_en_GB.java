package com.edatasite.workforce.gwt.core.client.ui.datePicker.util;

import com.google.gwt.i18n.client.DateTimeFormat;


public class DateLocale_en_GB implements DateLocale {

    public int[] DAYS_ORDER = {1, 2, 3, 4, 5, 6, 0};

    public int[] getDAY_ORDER() {
        return DAYS_ORDER;
    }

    public DateTimeFormat getDateTimeFormat() {
        return DateTimeFormat.getFormat("dd/MM/yyyy");
    }
}
