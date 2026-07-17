package com.edatasite.workforce.gwt.availability.client.history;

import com.edatasite.workforce.gwt.availability.client.HolidayAddSinksContainer;
import com.edatasite.workforce.gwt.availability.client.HolidaySinksContainer;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;

public class HolidayHistoryProcessor implements HistoryProcessor {
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new HolidaySinksContainer(containerName + strings[0], hrmsStrings.summaryHoliday(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new HolidayAddSinksContainer("holidayadd", hrmsStrings.addHoliday(), null);

    }
}
