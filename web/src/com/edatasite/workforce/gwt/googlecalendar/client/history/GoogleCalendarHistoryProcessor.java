package com.edatasite.workforce.gwt.googlecalendar.client.history;

import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.GoogleCalendarSinksContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 13.11.2008
 * Time: 15:50:20
 * To change this template use File | Settings | File Templates.
 */
public class GoogleCalendarHistoryProcessor implements HistoryProcessor {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public SinksContainer process(String containerName, String[] strings) {
        return new GoogleCalendarSinksContainer(containerName + strings[0], WfmStrings.App.get().calendar(), strings);
    }

    public SinksContainer processAdd(String[] params) {
        return new GoogleCalendarSinksContainer("calendaradd", wfmStrings.calendar(), params);
    }
}
