package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsGoogleCalendarReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: May 21, 2010
 * Time: 5:27:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleCalendarReminderManager extends Manager<EdsGoogleCalendarReminder> {
    ArrayList<CalendarEventReminder> getReminders(Integer eventID);
    void deleteEventReminders(Integer eventId);
    void deleteEventRemindersByEventIDs(List<Integer> eventIDs);
    Integer getEventReminderRecurrenceID(Integer eventID);
    EdsGoogleCalendarReminder getNextEventReminder(Integer eventID);
}
