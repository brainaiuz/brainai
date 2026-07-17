package com.edatasite.workforce.gwt.googlecalendar.client.localization;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: 13.11.2008
 * Time: 15:27:09
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleCalendarMessages extends Messages {

    String conflictsWithTheseEmployees(String p0);

    class App {
        public static GoogleCalendarMessages get() {
            return (GoogleCalendarMessages) GWT.create(GoogleCalendarMessages.class);
        }
    }


}
