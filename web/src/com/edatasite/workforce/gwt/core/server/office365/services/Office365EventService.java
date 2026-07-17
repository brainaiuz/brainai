package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Calendar;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Event;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365ODataQuery;

import java.util.Date;

/**
 * Created by umakarimov on 10/6/15.
 */
public interface Office365EventService {
    Office365ResourceCollection<Office365Calendar> listCalendars(Office365AccessTokenDTO token);

    Office365Calendar getDefaultCalendar(Office365AccessTokenDTO token);

    Office365Calendar getCalendarItem(String calendarId, Office365AccessTokenDTO token);

    Office365Calendar createCalendar(Office365Calendar calendar, Office365AccessTokenDTO token);

    Office365Calendar updateCalendar(Office365Calendar calendar, Office365AccessTokenDTO token);

    void deleteCalendar(String calendarId, Office365AccessTokenDTO token);

    Office365ResourceCollection<Office365Event> listCalendarEvents(String calendarId, Office365AccessTokenDTO token, Date start);

    Office365ResourceCollection<Office365Event> listCalendarEventsByStartEndDate(String calendarId, Date startDate, Date endDate, Office365AccessTokenDTO token);

    Office365ResourceCollection<Office365Event> listDefaultCalendarEvents(Office365AccessTokenDTO token);

    Office365Event createDefaultCalendarEvent(Office365Event event, Office365AccessTokenDTO token);

    Office365Event createCalendarEvent(String calendarId, Office365Event event, Office365AccessTokenDTO token);

    Office365ResourceCollection<Office365Event> listEvents(Office365ODataQuery query, Office365AccessTokenDTO token);

    Office365Event createEvent(Office365Event event, Office365AccessTokenDTO token);

    Office365Event getEvent(String eventId, Office365AccessTokenDTO token);

    Office365Event updateEvent(Office365Event event, Office365AccessTokenDTO token);

    void deleteEvent(String eventId, Office365AccessTokenDTO token);

    void acceptEvent(String eventId, String comment, Boolean sendResponse, Office365AccessTokenDTO token);

    void tentativeAcceptEvent(String eventId, String comment, Boolean sendResponse, Office365AccessTokenDTO token);

    void declineEvent(String eventId, String comment, Boolean sendResponse, Office365AccessTokenDTO token);

    void dismissReminder(String eventId, Office365AccessTokenDTO token);

    void snoozeReminder(String eventId, Date newReminderTime, Office365AccessTokenDTO token);

    Object[] getOfficeEventsOrTasks(Office365AccessTokenDTO token, EdsEmployee employee, Date start, Date end, boolean isEvent);

    Office365Event updateCalendarEventEntry(EdsUser user, Office365Event eventEntry, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask);

    Office365Event createCalendarEventEntry(EdsUser user, Appointment appointment, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask);

    Office365Event updateEventByCalendarID(String googleCalendarGoogleID, Office365Event office365Event, Office365AccessTokenDTO token);

    void deleteEventByCalendarID(String googleCalendarGoogleID, String id, Office365AccessTokenDTO token);

    Appointment wrapCalendarEventToAppointment(Office365Event calendarEvent, boolean isEvent);
}
