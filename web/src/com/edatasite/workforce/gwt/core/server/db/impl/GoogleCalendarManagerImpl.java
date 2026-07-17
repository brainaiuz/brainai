package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarReminderManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.TaskReminderManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Transactional
@Repository("googleCalendarManager")
public class GoogleCalendarManagerImpl extends BaseManager<EdsGoogleCalendar> implements GoogleCalendarManager, Constants {

    private final String USER_OWNED_CALENDARS_URL = "http://www.google.com/calendar/feeds/default/owncalendars/full";
    private static final String GOOGLE_MARKETPLACE_CALENDAR_URL = "https://www.google.com/calendar/feeds/default/private/full";
    private static final String XOAUTH_REQUESTOR_ID = "xoauth_requestor_id";
    private final String RED = "#A32929";
    private final String GREEN = "#0D7813";
    private static final Logger log = LoggerFactory.getLogger(GoogleCalendarManagerImpl.class);

    @Autowired
    private GoogleManager googleManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private GoogleCalendarReminderManager calendarReminderManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private TaskReminderManager taskReminderManager;

    public GoogleCalendarManagerImpl() {
        super(EdsGoogleCalendar.class);
    }

    public EdsGoogleCalendar getGoogleCalendar(EdsUser user, boolean withCheck) {
        return (EdsGoogleCalendar) findSingle("from EdsGoogleCalendar gc where gc.user=?" + (withCheck ? " and (gc.active is null or gc.active=true)" : ""), user);
    }

    public EdsGoogleCalendar getOfficeCalendar(EdsUser user, boolean withCheck) {
        return (EdsGoogleCalendar) findSingle("from EdsGoogleCalendar gc where gc.officeCalendar = true and gc.user=?" + (withCheck ? " and (gc.active is null or gc.active=true)" : ""), user);
    }

    public boolean validateUser(EdsUser user) {
        EdsGoogleCalendar calendar = getGoogleCalendar(user, true);
        if (calendar != null) {
            return calendar.getToken() != null && calendar.getCalendarID() != null && (calendar.getOfficeCalendar() == null || !calendar.getOfficeCalendar());
        } else {
            return false;
        }
    }

    public Boolean validateOfficeUser(EdsUser user) {
        EdsGoogleCalendar calendar = getGoogleCalendar(user, true);
        if (calendar != null && calendar.getOfficeCalendar()) {
            return calendar.getToken() != null && calendar.getCalendarID() != null;
        } else {
            return false;
        }
    }
    /**
     * When user has not opened in his google calendar even once, it should return false.
     *
     * @param token
     * @return
     */
    public String createCalendarDetails(String token) throws IOException, GeneralSecurityException, AuthenticationException, ServiceException {
        EdsUser user = getUser();
        if (!validateUser(user)) {
            EdsGoogleCalendar googleCalendar = getGoogleCalendar(user, false);
            GoogleCredential credential = googleManager.getGoogleCredential(token);
            credential.setRefreshToken(token);
            Calendar calendarService = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
            if (googleCalendar != null) {
                if (googleCalendar.getCalendarID() != null && (userSettings == null || !userSettings.isSyncFromDefaultCalendar())) {
                    calendarService.calendars().delete(googleCalendar.getCalendarID()).execute();
                }
                if (googleCalendar.getTaskCalendarID() != null) {
                    calendarService.calendars().delete(googleCalendar.getTaskCalendarID()).execute();
                }
                delete(googleCalendar);
            }
            String googleID = null;
            for (CalendarListEntry entry : calendarService.calendarList().list().execute().getItems()) {
                if (entry.getPrimary() != null && entry.isPrimary()) {
                    googleID = entry.getId();
                    break;
                }
            }
            log.info("User's googleID: " + googleID);
            if (googleID == null || (googleID != null && !googleID.contains("@"))) {
                return googleID;
            }

            googleCalendar = new EdsGoogleCalendar();
            googleCalendar.setUser(user);
            googleCalendar.setGoogleID(googleID);
            googleCalendar.setToken(token);
            create(googleCalendar);

            String eventCalendarID = null;
            if (userSettings != null && userSettings.isSyncFromDefaultCalendar()) {
                eventCalendarID = googleID;
            } else {
                eventCalendarID = getCalendarID(calendarService);
            }
            log.info("Event Calendar ID: " + eventCalendarID);
            if (eventCalendarID.contains("Error occured")) {
                delete(googleCalendar);
                return eventCalendarID;
            }
            googleCalendar.setCalendarID(eventCalendarID);

            String taskCalendarID = getTaskCalendarID(calendarService);
            log.info("Task Calendar ID: " + taskCalendarID);
            if (taskCalendarID.contains("Error occured")) {
                delete(googleCalendar);
                return taskCalendarID;
            }
            googleCalendar.setTaskCalendarID(taskCalendarID);
            update(googleCalendar);

            return "1"; // if successfully configured
        }
        return "2"; // if already configured
    }

    private String getTaskCalendarID(Calendar calendarService) {
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        try {
            EdsCompany company = getUser().getCompany();
            calendar.setSummary("2." + company.getName() + " Tasks (created: " + company.getCompanyDate() + ")");
            calendar.setDescription("You can see all your company related tasks.");
            calendar.setTimeZone(company.getTimeZone().getID());
            calendar = googleManager.insertCalendar(calendarService, calendar);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Error occured while creating task calendar.";
        }
        return calendar.getId();
    }

    private String getCalendarID(Calendar calendarService) {
        com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
        try {
            EdsCompany company = getUser().getCompany();
            calendar.setSummary("1." + company.getName() + " Events (created: " + company.getCompanyDate() + ")");
            calendar.setDescription("You can see all your company related events.");
            calendar.setTimeZone(company.getTimeZone().getID());
            calendar = googleManager.insertCalendar(calendarService, calendar);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Error occured while creating event calendar.";
        }
        return calendar.getId();
    }

    public Calendar getServiceLoggedIn(EdsUser user) throws GeneralSecurityException, IOException {
        EdsGoogleCalendar googleCalendar = getGoogleCalendar(user, true);
        GoogleCredential credential = googleManager.getGoogleCredential(googleCalendar.getToken());
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
    }

    public void deleteGoogleCalendar(EdsEmployee employee, boolean deleteCalendar) throws IOException, ServiceException, GeneralSecurityException {
        EdsGoogleCalendar calendar = getGoogleCalendar(employee, false);
        if (calendar != null && employee != null) {
            employeeEventManager.removeGoogleIDFromEmployeeEvents(employee);
            employeeTaskManager.removeGoogleIDFromEmployeeTasks(employee);
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(employee);
            if (deleteCalendar) {
//                CalendarService service = getServiceLoggedIn(employee);
                Calendar calendarService = getCalendarService(employee);
                if (calendarService != null) {
                    if (calendar.getCalendarID() != null && (userSettings == null || !userSettings.isSyncFromDefaultCalendar())) {
                        calendarService.calendars().delete(calendar.getCalendarID()).execute();
                    }
                    if (calendar.getTaskCalendarID() != null) {
                        calendarService.calendars().delete(calendar.getTaskCalendarID()).execute();
                    }
                }
            }
        }
    }

    private Calendar getCalendarService(EdsEmployee user) throws GeneralSecurityException, IOException {
        EdsGoogleCalendar googleCalendar = getGoogleCalendar(user, true);
        if (googleCalendar != null) {
            GoogleCredential credential = googleManager.getGoogleCredential(googleCalendar.getToken());
            return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
        }
        return null;
    }

    /**
     * @param user
     * @param start   : CalendarEventFeed's Query startDate
     * @param end     : CalendarEventFeed's Query endDate
     * @param isEvent : for Google Events = true; for Google Tasks = false
     * @return : Google Events or Tasks ArrayList
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws AuthenticationException
     * @throws ServiceException
     */
    public Object[] getGoogleEventsOrTasks(EdsUser user, Date start, Date end, boolean isEvent) throws IOException, GeneralSecurityException, AuthenticationException, ServiceException {
        Map<String, Appointment> appointmentList = new HashMap<>();
        Map<String, Event> calendarEventEntryList = new HashMap<>();
        Object[] objects = new Object[2];
        objects[0] = calendarEventEntryList;
        objects[1] = appointmentList;
        CalendarQuery upcomingEventsQuery;
        EdsGoogleCalendar googleCalendar = getGoogleCalendar(user, true);
        Calendar calendarService = null;
        if (googleCalendar != null) {
            GoogleCredential credential = googleManager.getGoogleCredential(googleCalendar.getToken());
            calendarService = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), credential).build();
        }
        String calendarURL = null;
        com.google.api.client.util.DateTime minStartDate = new com.google.api.client.util.DateTime(start, TimeZone.getTimeZone("UTC"));
        com.google.api.client.util.DateTime maxStartDate = new com.google.api.client.util.DateTime(end, TimeZone.getTimeZone("UTC"));
        List<Event> eventList = null;
        if (isEvent) {
            eventList = calendarService.events().list(googleCalendar.getCalendarID()).setTimeMin(minStartDate)
                    .setTimeMax(maxStartDate).setMaxResults(5000).setSingleEvents(true).execute().getItems();

        } else {
            eventList = calendarService.events().list(googleCalendar.getTaskCalendarID()).setTimeMin(minStartDate)
                    .setTimeMax(maxStartDate).setMaxResults(5000).setSingleEvents(true).execute().getItems();
        }

        for (Event event : eventList) {
            appointmentList.put(event.getId(), wrapCalendarEventToAppointment(user.getUserTimezone(), event, isEvent));
            calendarEventEntryList.put(event.getId(), event);
        }
        return objects;
    }

    public Appointment wrapCalendarEventToAppointment(TimeZone userTimeZone, Event event, boolean isEvent) {
        if (event == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        // this is used for sync with google calendar
        Event.ExtendedProperties properties = event.getExtendedProperties();
        if (properties != null && properties.size() > 0) {
            if (properties.getPrivate() != null && properties.getPrivate().size() > 0) {
                for (String property : properties.getPrivate().keySet())
                    if (isEvent) {
                        if ("employeeEventID".equals(property)) {
                            if (properties.getPrivate().get(property) != null && !"".equals(properties.getPrivate().get(property))) {
                                appointment.setObjectID(Integer.valueOf(properties.getPrivate().get(property)));
                                break;
                            }
                        }
                    } else {
                        if ("employeeTaskID".equals(property)) {
                            if (properties.getPrivate().get(property) != null && !"".equals(properties.getPrivate().get(property))) {
                                appointment.setObjectID(Integer.valueOf(properties.getPrivate().get(property)));
                                break;
                            }
                        }
                    }
            }
        }

        appointment.setGoogleID(event.getId());
        appointment.setSubject(event.getSummary());
        appointment.setDescription(event.getDescription() != null ? event.getDescription() : "");

        Date startDate = new Date(event.getStart() != null ? event.getStart().getDateTime() != null ? event.getStart().getDateTime().getValue() :
                (event.getStart() != null ? event.getStart().getDate().getValue() : null) : null);
        Date endDate = (Date) startDate.clone();
        if (event.getEnd() == null && event.getEnd().getDateTime() == null) {
            endDate = (Date) startDate.clone();
            endDate.setDate(startDate.getDate() + 1);
        } else {
            endDate = new Date(event.getEnd() != null ? event.getEnd().getDateTime() != null ? event.getEnd().getDateTime().getValue() :
                    (event.getEnd() != null ? event.getEnd().getDate().getValue() : null) : null);
        }
        if (event.getStart().getDateTime() != null ? event.getStart().getDateTime().isDateOnly() : event.getStart().getDate().isDateOnly() &&
                event.getEnd().getDateTime() != null ? event.getEnd().getDateTime().isDateOnly() : event.getEnd().getDate().isDateOnly()) {
            appointment.setAllDay(true);
            startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes() - (userTimeZone.getRawOffset() / 60000), startDate.getSeconds());
            endDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate(), endDate.getHours(), endDate.getMinutes() - (userTimeZone.getRawOffset() / 60000), endDate.getSeconds() - 1);
        } else {
            appointment.setAllDay(false);
        }
        appointment.setStartDate(startDate);
        appointment.setEndDate(endDate);
//        }

        appointment.setLastModifiedDate(new Date(event.getUpdated().getValue()));

        Event.Creator author = event.getCreator().size() > 0 ? event.getCreator() : null;
        if (author != null) {
            appointment.setCreatedBy(author.getDisplayName());
        }

        if (event.getLocation() != null && event.getLocation().length() > 0) {
            appointment.setLocation(event.getLocation());
        }

        appointment.setMultiDay(appointment.isMultiDayAppointment());
        return appointment;
    }

    private String getEventID(CalendarEventEntry event) {
        /**
         * As we mentioned in calendarID during returning id of the calendar, after moving newer version
         * of the gdata it stopped working properly, so returning simply event.getId() didn't work successfully,
         * thus we changed the returning link from event.getId() to event.getEditLink().getHref().
         * The same action has been done in case of calendar. See getCalendarID() method.
         */
        return event.getId();
//        return event.getEditLink().getHref();//event.getId();
    }

    private String getReminderMethod(Integer type) {
        if (ALERT.equals(type)) {
            return "alert";
        } else if (E_MAIL.equals(type)) {
            return "email";
        } else if (SMS.equals(type)) {
            return "sms";
        } else if (ALL.equals(type)) {
            return "all";
        } else if (PUSH_NOTIFICATION.equals(type)) {
            return "notification";
        }
        return "none";
    }

    public void createEmployeeEvent(EdsUser employee, EdsEvent event, ArrayList<EdsUser> attendees, boolean onlyShare) {
        EdsEmployeeEvent employeeEvent = new EdsEmployeeEvent();
        employeeEvent.setEmployee(employee);
        employeeEvent.setEvent(event);
        if (employee.isShared()) {
            employeeEvent.setPermission(EdsEmployeeEvent.READ_WRITE);
        }

        employeeEvent.setLastModifiedDate(new Date());
        employeeEventManager.create(employeeEvent);

        try {
            messageManager.sendCalendarShareEventNotification(employeeEvent, attendees, onlyShare);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private When getTime(Appointment appointment, boolean isAllDay) {
        DateTime startTime;
        DateTime endTime;
        if (appointment.getEndDate().before(appointment.getStartDate())) {
            Date start = appointment.getEndDate();
            appointment.setEndDate(appointment.getStartDate());
            appointment.setStartDate(start);
        }
        TimeZone timeZone = TimeZone.getTimeZone(getUser().getTimezone());
        return getTime(appointment.getStartDate(), appointment.getEndDate(), timeZone, isAllDay);
    }

    private When getTime(Date startDate, Date endDate, TimeZone userTimeZone, boolean isAllDay) {
        Date sDate = (Date) startDate.clone();
        Date eDate = (Date) endDate.clone();
        if (eDate.before(sDate)) {
            Date newStartDate = (Date) eDate.clone();
            eDate = (Date) sDate.clone();
            sDate = newStartDate;
        }
        DateTime startTime = new DateTime(sDate, TimeZone.getDefault());
        DateTime endTime = new DateTime(eDate, TimeZone.getDefault());
        if (isAllDay) {
            Date start = new Date(sDate.getYear(), sDate.getMonth(), sDate.getDate(), sDate.getHours(), sDate.getMinutes() + (userTimeZone.getRawOffset() / 60000), sDate.getSeconds());
            Date end = new Date(eDate.getYear(), eDate.getMonth(), eDate.getDate(), eDate.getHours(), eDate.getMinutes() + (userTimeZone.getRawOffset() / 60000), eDate.getSeconds());
            startTime = new DateTime(start, TimeZone.getDefault());
            endTime = new DateTime(end, TimeZone.getDefault());
            startTime.setDateOnly(true);
            endTime.setDateOnly(true);
            log.info(">>>>>>>>>>> Start Date: " + start);
            log.info(">>>>>>>>>>> End Date: " + end);
        }
        When eventTimes = new When();
        eventTimes.setStartTime(startTime);
        eventTimes.setEndTime(endTime);
        return eventTimes;
    }

    public Event createCalendarEventEntry(EdsUser user, Appointment appointment, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask) {
        Event event = new Event();
        if (appointment.getGoogleID() != null) {
            event.setId(appointment.getGoogleID());
        }
        event.setSummary(appointment.getSubject());
        event.setDescription(appointment.getDescription() != null ? appointment.getDescription() : "");

        event.setLocation(appointment.getLocation());
        event.setVisibility("default");

        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        boolean allDay = false;
        ExtendedProperty property = new ExtendedProperty();
        if (employeeEvent != null) {
            Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
            Map<String, String> privateExtendedProperties = new HashMap<>();
            privateExtendedProperties.put("employeeEventID", String.valueOf(employeeEvent.getObjectID()));
            extendedProperties.setPrivate(privateExtendedProperties);
            event.setExtendedProperties(extendedProperties);
            allDay = employeeEvent.getEvent().isAllDay() != null ? employeeEvent.getEvent().isAllDay() : true;
            reminders = calendarReminderManager.getReminders(employeeEvent.getEvent().getObjectID());
        }
        if (employeeTask != null) {
            EdsTask edsTask = employeeTask.getTask();
            Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
            Map<String, String> privateExtendedProperties = new HashMap<>();
            privateExtendedProperties.put("employeeTaskID", String.valueOf(employeeTask.getObjectID()));
            extendedProperties.setPrivate(privateExtendedProperties);
            event.setExtendedProperties(extendedProperties);

            allDay = edsTask.isAllDay() != null ? edsTask.isAllDay() : true;
            reminders = taskReminderManager.getReminders(edsTask.getObjectID());
        }
        String timezone = user.getTimezone();
        if (timezone == null || "".equals(timezone.trim())) {
            timezone = "GMT";
        }
        TimeZone userTimeZone = TimeZone.getTimeZone(timezone);
        Date startDate = appointment.getStartDate();
        Date endDate = appointment.getEndDate();
        if (endDate.before(startDate)) {
            Date newStartDate = (Date) endDate.clone();
            endDate = (Date) startDate.clone();
            startDate = newStartDate;
        }
        com.google.api.client.util.DateTime startTime = new com.google.api.client.util.DateTime(startDate, TimeZone.getTimeZone("UTC"));
        com.google.api.client.util.DateTime endTime = new com.google.api.client.util.DateTime(endDate, TimeZone.getTimeZone("UTC"));
        EventDateTime sDate = new EventDateTime();
        EventDateTime eDate = new EventDateTime();
        if (allDay) {
            startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), startDate.getHours(), startDate.getMinutes() + (userTimeZone.getRawOffset() / 60000), startDate.getSeconds());
            endDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate(), endDate.getHours(), endDate.getMinutes() + (userTimeZone.getRawOffset() / 60000), endDate.getSeconds() + 1);
            System.out.println("***************************************************************************");
            System.out.println("start date" + startDate);
            System.out.println("end date" + endDate);
            startTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(startDate));
            endTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(endDate));
            event.setStart(new EventDateTime().setDate(startTime));
            event.setEnd(new EventDateTime().setDate(endTime));
        } else {
            event.setStart(new EventDateTime().setDateTime(startTime));
            event.setEnd(new EventDateTime().setDateTime(endTime));
        }
        if (reminders != null && reminders.size() > 0) {
            for (CalendarEventReminder calendarReminder : reminders) {
                ArrayList<EventReminder> listEventReminder = new ArrayList<>();
                Event.Reminders reminder = new Event.Reminders();
                EventReminder eventReminder = new EventReminder();
                eventReminder.setMethod(getReminderMethod(calendarReminder.getValue()));
                eventReminder.setMinutes(calendarReminder.getReminderTimes());
                listEventReminder.add(eventReminder);
                reminder.setUseDefault(false);
                reminder.setOverrides(listEventReminder);

                event.setReminders(reminder);
            }
        }
        return event;
    }

    public Event updateCalendarEventEntry(EdsUser user, Event event, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask) {
        ExtendedProperty property = new ExtendedProperty();
        event.setLocation(null);
        event.setVisibility("default");
        TimeZone timeZone = user.getUserTimezone();
        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        boolean allDay = false;
        if (employeeEvent != null) {
            EdsEvent edsEvent = employeeEvent.getEvent();
            event.setSummary(edsEvent.getSubject());
            event.setDescription(edsEvent.getDescription() != null ? edsEvent.getDescription() : "");
            event.setLocation(edsEvent.getVenue());
            EventDateTime sDate = new EventDateTime();
            EventDateTime eDate = new EventDateTime();
            com.google.api.client.util.DateTime starTime = new com.google.api.client.util.DateTime(edsEvent.getStartDate(), TimeZone.getTimeZone("UTC"));
            com.google.api.client.util.DateTime endTime = new com.google.api.client.util.DateTime(edsEvent.getEndDate(), TimeZone.getTimeZone("UTC"));
            allDay = edsEvent.isAllDay() != null ? edsEvent.isAllDay() : true;
            Date startDate;
            Date endDate;
            if (allDay) {
                startDate = new Date(edsEvent.getStartDate().getYear(), edsEvent.getStartDate().getMonth(), edsEvent.getStartDate().getDate(), edsEvent.getStartDate().getHours(), edsEvent.getStartDate().getMinutes() + (timeZone.getRawOffset() / 60000), edsEvent.getStartDate().getSeconds());
                endDate = new Date(edsEvent.getEndDate().getYear(), edsEvent.getEndDate().getMonth(), edsEvent.getEndDate().getDate(), edsEvent.getEndDate().getHours(), edsEvent.getEndDate().getMinutes() + (timeZone.getRawOffset() / 60000), edsEvent.getEndDate().getSeconds() + 1);
                System.out.println("***************************************************************************");
                System.out.println("start date" + startDate);
                System.out.println("end date" + endDate);
                starTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(startDate));
                endTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(endDate));
                event.setStart(new EventDateTime().setDate(starTime));
                event.setEnd(new EventDateTime().setDate(endTime));
            } else {
                sDate.setDateTime(starTime);
                eDate.setDateTime(endTime);
                event.setStart(sDate);
                event.setEnd(eDate);
            }

            Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
            Map<String, String> privateExtendedProperties = new HashMap<>();
            privateExtendedProperties.put("employeeEventID", String.valueOf(employeeEvent.getObjectID()));
            extendedProperties.setPrivate(privateExtendedProperties);
            event.setExtendedProperties(extendedProperties);

            reminders = calendarReminderManager.getReminders(edsEvent.getObjectID());
        }
        if (employeeTask != null) {
            EdsTask edsTask = employeeTask.getTask();
            event.setSummary(edsTask.getName());
            event.setDescription(edsTask.getDescription() != null ? edsTask.getDescription() : "");
            event.setLocation(edsTask.getProject().getName());
            allDay = edsTask.isAllDay() != null ? edsTask.isAllDay() : true;
            Date startDate = (Date) edsTask.getStartDate().clone();
            Date endDate = (Date) edsTask.getDueDate().clone();
            EventDateTime sDate = new EventDateTime();
            EventDateTime eDate = new EventDateTime();
            com.google.api.client.util.DateTime starTime = new com.google.api.client.util.DateTime(startDate, TimeZone.getTimeZone("UTC"));
            com.google.api.client.util.DateTime endTime = new com.google.api.client.util.DateTime(endDate, TimeZone.getTimeZone("UTC"));
            if (allDay) {
                starTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(edsTask.getStartDate()));   //
                endTime = new com.google.api.client.util.DateTime(new SimpleDateFormat("yyyy-MM-dd").format(edsTask.getDueDate()));   //
                event.setStart(new EventDateTime().setDate(starTime));
                event.setEnd(new EventDateTime().setDate(endTime));
            } else {
                sDate.setDateTime(starTime);
                eDate.setDateTime(endTime);
                event.setStart(sDate);
                event.setEnd(eDate);
            }
            Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
            Map<String, String> privateExtendedProperties = new HashMap<>();
            privateExtendedProperties.put("employeeTaskID", String.valueOf(employeeTask.getObjectID()));
            extendedProperties.setPrivate(privateExtendedProperties);
            event.setExtendedProperties(extendedProperties);
            reminders = taskReminderManager.getReminders(edsTask.getObjectID());
        }
        if (reminders != null && reminders.size() > 0) {
            for (CalendarEventReminder calendarReminder : reminders) {
                ArrayList<EventReminder> listEventReminder = new ArrayList<>();
                Event.Reminders reminder = new Event.Reminders();
                EventReminder eventReminder = new EventReminder();
                eventReminder.setMethod(getReminderMethod(calendarReminder.getValue()));
                eventReminder.setMinutes(calendarReminder.getReminderTimes());
                listEventReminder.add(eventReminder);
                reminder.setUseDefault(false);
                reminder.setOverrides(listEventReminder);

                event.setReminders(reminder);
            }
        }
        return event;
    }
}