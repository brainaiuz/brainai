package com.edatasite.workforce.gwt.googlecalendar.client;

import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw.GoogleCalendarView;

import java.util.LinkedList;

public class GoogleCalendarSinksContainer extends SinksContainer {

    public GoogleCalendarSinksContainer(String name, String description, String[] params) {
        super(name, description, params);
    }

    @Override
    protected void initViews(LinkedList<View> viewList) {

    }

    protected void initViews() {
        String calendarType = null;
        String dateString = null;
        if (params.length > 1) {
            calendarType = params[1];
            if (params.length > 2) {
                dateString = params[2];
            }
            addView(new GoogleCalendarView("calendar", WfmStrings.App.get().calendar(), Integer.valueOf(calendarType), dateString));
        }
    }
}
