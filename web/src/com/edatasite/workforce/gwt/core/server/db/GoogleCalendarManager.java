package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public interface GoogleCalendarManager extends Manager<EdsGoogleCalendar> {

    EdsGoogleCalendar getGoogleCalendar(EdsUser user, boolean withCheck);

    boolean validateUser(EdsUser user);

    String createCalendarDetails(String token) throws IOException, GeneralSecurityException, ServiceException;

    Object[] getGoogleEventsOrTasks(EdsUser user, Date start, Date end, boolean isEvent) throws IOException, GeneralSecurityException, ServiceException;

    void createEmployeeEvent(EdsUser employee, EdsEvent event, ArrayList<EdsUser> attendees, boolean onlyShare);

    void deleteGoogleCalendar(EdsEmployee employee, boolean deleteCalendar) throws IOException, ServiceException, GeneralSecurityException;

    Calendar getServiceLoggedIn(EdsUser user) throws GeneralSecurityException, IOException;

    Event createCalendarEventEntry(EdsUser user, Appointment appointment, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask);

    Event updateCalendarEventEntry(EdsUser user, Event event, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask);

    Appointment wrapCalendarEventToAppointment(TimeZone userTimeZone, Event event, boolean isEvent);

    Boolean validateOfficeUser(EdsUser user);

    EdsGoogleCalendar getOfficeCalendar(EdsUser user, boolean isActive);
}
