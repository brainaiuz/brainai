package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarReminderManager;
import com.edatasite.workforce.gwt.core.server.db.TaskReminderManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Calendar;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365DateTimeTimeZone;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Event;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ItemBody;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Recipient;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365EventService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Fetcher;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365ODataQuery;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by umakarimov on 10/6/15.
 */
@Service("office365EventService")
public class Office365EventServiceImpl extends Office365Fetcher implements Office365EventService, Office365Constants {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private GoogleCalendarReminderManager calendarReminderManager;
    @Autowired
    private TaskReminderManager taskReminderManager;
    @Autowired
    private UserManager userManager;

    private final static TypeReference eventListType = new TypeReference<Office365ResourceCollection<Office365Event>>() {
    };

    private final static TypeReference calendarListType = new TypeReference<Office365ResourceCollection<Office365Calendar>>() {
    };

    /**
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/user_list_calendars
     */
    @Override
    public Office365ResourceCollection<Office365Calendar> listCalendars(Office365AccessTokenDTO token) {
        return new Request<Office365ResourceCollection<Office365Calendar>>(OFFICE_ONE_DRIVE, CALENDAR_LIST, token)
                .setTypeReference(calendarListType)
                .sendGet()
                .getResource();
    }

    /**
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_get
     */
    @Override
    public Office365Calendar getDefaultCalendar(Office365AccessTokenDTO token) {
        return new Request<Office365Calendar>(OFFICE_ONE_DRIVE, CALENDAR_DEFAULT_ITEM, token)
                .setClass(Office365Calendar.class)
                .sendGet()
                .getResource();

    }

    /**
     * @param calendarId
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_get
     */
    @Override
    public Office365Calendar getCalendarItem(String calendarId, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM, calendarId);

        return new Request<Office365Calendar>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Calendar.class)
                .sendGet()
                .getResource();
    }


    /**
     * @param calendar
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/user_post_calendars
     */
    @Override
    public Office365Calendar createCalendar(Office365Calendar calendar, Office365AccessTokenDTO token) {
        return new Request<Office365Calendar>(OFFICE_ONE_DRIVE, CALENDAR_LIST, token)
                .setClass(Office365Calendar.class)
                .setResource(calendar)
                .sendPost()
                .getResource();
    }

    /**
     * @param calendar
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_update
     */
    @Override
    public Office365Calendar updateCalendar(Office365Calendar calendar, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM, calendar.getId());

        return new Request<Office365Calendar>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Calendar.class)
                .setResource(calendar)
                .sendPatch()
                .getResource();
    }

    /**
     * @param calendarId
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_delete
     */
    @Override
    public void deleteCalendar(String calendarId, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM, calendarId);

        new Request<Office365Calendar>(OFFICE_ONE_DRIVE, url, token).sendDelete();
    }

    /**
     * @param calendarId
     * @param token
     * @param start
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_delete
     */
    @Override
    public Office365ResourceCollection<Office365Event> listCalendarEvents(String calendarId, Office365AccessTokenDTO token, Date start) {
        String url = String.format(CALENDAR_ITEM_EVENTS, calendarId);
//        url = url + "?$orderBy=lastModifiedDateTime+desc&$filter=lastModifiedDateTime+ge+" + shortDateFormat.format(start) + "&$top=50";
        url = url + "?$orderBy=lastModifiedDateTime+desc&$filter=start/dateTime+ge+'" + shortDateFormat.format(start) + "'&$top=50";

        return new Request<Office365ResourceCollection<Office365Event>>(OFFICE_ONE_DRIVE, url, token)
                .setTypeReference(eventListType)
                .sendGet()
                .getResource();
    }

    /**
     * @param startDate
     * @param endDate
     * @param calendarId
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_delete
     */
    @Override
    public Office365ResourceCollection<Office365Event> listCalendarEventsByStartEndDate(String calendarId, Date startDate, Date endDate, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM, calendarId);
        url = url + CALENDAR_VIEW + START_DATE_TIME + dateFormat.format(startDate) + END_DATE_TIME + dateFormat.format(endDate) + "&$top=1000";
        return new Request<Office365ResourceCollection<Office365Event>>(OFFICE_ONE_DRIVE, url, token)
                .setTypeReference(eventListType)
                .sendGet()
                .getResource();
    }

    /**
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_list_events
     */
    @Override
    public Office365ResourceCollection<Office365Event> listDefaultCalendarEvents(Office365AccessTokenDTO token) {
        return new Request<Office365ResourceCollection<Office365Event>>(OFFICE_ONE_DRIVE, CALENDAR_DEFAULT_ITEM_EVENTS, token)
                .setTypeReference(eventListType)
                .sendGet()
                .getResource();
    }

    /**
     * @param event
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_post_events
     */
    @Override
    public Office365Event createDefaultCalendarEvent(Office365Event event, Office365AccessTokenDTO token) {
        return new Request<Office365Event>(OFFICE_ONE_DRIVE, CALENDAR_DEFAULT_ITEM_EVENTS, token)
                .setClass(Office365Event.class)
                .setResource(event)
                .sendPost()
                .getResource();
    }

    /**
     * @param calendarId
     * @param event
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/calendar_post_events
     */
    @Override
    public Office365Event createCalendarEvent(String calendarId, Office365Event event, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM_EVENTS, calendarId);
        String json = clearNullFields(event.toJSON(), event);

        return new Request<Office365Event>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Event.class)
                .setResource(json)
                .sendJSonPost()
                .getResource();
    }

    private String clearNullFields(String toJSON, Office365Event event) {
        String json = toJSON;
        if (event != null) {
            if (event.getAttendees() == null) {
                json = json.replace("\"attendees\":null,", "");
            }
            if (event.getBody() == null) {
                json = json.replace("\"body\":null,", "");
            }
            if (event.getBodyPreview() == null) {
                json = json.replace("\"bodyPreview\":null,", "");
            }
            if (event.getCategories() == null) {
                json = json.replace("\"categories\":null,", "");
            }
            if (event.getChangeKey() == null) {
                json = json.replace("\"changeKey\":null,", "");
            }
            if (event.getCreatedDateTime() == null) {
                json = json.replace("\"createdDateTime\":null,", "");
            }
            if (event.getEnd() == null) {
                json = json.replace("\"end\":null,", "");
            }
            if (event.getHasAttachments() == null) {
                json = json.replace("\"hasAttachments\":null,", "");
            }
            if (event.getiCalUId() == null) {
                json = json.replace("\"iCalUId\":null,", "");
            }
            if (event.getId() == null) {
                json = json.replace("\"id\":null,", "");
            }
            if (event.getImportance() == null) {
                json = json.replace("\"importance\":null,", "");
            }
            if (event.getIsAllDay() == null) {
                json = json.replace("\"isAllDay\":null,", "");
            }
            if (event.getIsCancelled() == null) {
                json = json.replace("\"isCancelled\":null,", "");
            }
            if (event.getIsOrganizer() == null) {
                json = json.replace("\"isOrganizer\":null,", "");
            }
            if (event.getIsReminderOn() == null) {
                json = json.replace("\"isReminderOn\":null,", "");
            }
            if (event.getLastModifiedDateTime() == null) {
                json = json.replace("\"lastModifiedDateTime\":null,", "");
            }
            if (event.getLocation() == null) {
                json = json.replace("\"location\":null,", "");
            }
            if (event.getOrganizer() == null) {
                json = json.replace("\"organizer\":null,", "");
            }
            if (event.getOriginalEndTimeZone() == null) {
                json = json.replace("\"originalEndTimeZone\":null,", "");
            }
            if (event.getOriginalStart() == null) {
                json = json.replace("\"originalStart\":null,", "");
            }
            if (event.getOriginalStartTimeZone() == null) {
                json = json.replace("\"originalStartTimeZone\":null,", "");
            }
            if (event.getRecurrence() == null) {
                json = json.replace("\"recurrence\":null,", "");
            }
            if (event.getReminderMinutesBeforeStart() == null) {
                json = json.replace("\"reminderMinutesBeforeStart\":null,", "");
            }
            if (event.getResponseRequested() == null) {
                json = json.replace("\"responseRequested\":null,", "");
            }
            if (event.getResponseStatus() == null) {
                json = json.replace("\"responseStatus\":null,", "");
            }
            if (event.getSensitivity() == null) {
                json = json.replace("\"sensitivity\":null,", "");
            }
            if (event.getSeriesMasterId() == null) {
                json = json.replace("\"seriesMasterId\":null,", "");
            }
            if (event.getShowAs() == null) {
                json = json.replace("\"showAs\":null,", "");
            }
            if (event.getStart() == null) {
                json = json.replace("\"start\":null,", "");
            }
            if (event.getSubject() == null) {
                json = json.replace("\"subject\":null,", "");
            }
            if (event.getType() == null) {
                json = json.replace("\"type\":null,", "");
            }
            if (event.getWebLink() == null) {
                json = json.replace("\"webLink\":null,", "");
            }

        }
        return json;
    }

    /**
     * @param query
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/user_list_events
     */
    @Override
    public Office365ResourceCollection<Office365Event> listEvents(Office365ODataQuery query, Office365AccessTokenDTO token) {
        return new Request<Office365ResourceCollection<Office365Event>>(OFFICE_ONE_DRIVE, EVENT_LIST, token)
                .setTypeReference(eventListType)
                .setQuery(query)
                .sendGet()
                .getResource();
    }

    /**
     * @param event
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/user_post_events
     */
    @Override
    public Office365Event createEvent(Office365Event event, Office365AccessTokenDTO token) {
        return new Request<Office365Event>(OFFICE_ONE_DRIVE, EVENT_LIST, token)
                .setResource(event)
                .setClass(Office365Event.class)
                .sendPost()
                .getResource();
    }

    /**
     * @param eventId
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_get
     */
    @Override
    public Office365Event getEvent(String eventId, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM, eventId);

        return new Request<Office365Event>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Event.class)
                .sendGet()
                .getResource();
    }

    /**
     * @param event
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_update
     */
    @Override
    public Office365Event updateEvent(Office365Event event, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM, event.getId());

        return new Request<Office365Event>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Event.class)
                .setResource(event)
                .sendPatch()
                .getResource();
    }

    /**
     * @param calendarId
     * @param event
     * @param token
     * @return
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_update
     */
    @Override
    public Office365Event updateEventByCalendarID(String calendarId, Office365Event event, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM_EVENTS, calendarId);
        url = url + "/" + event.getId();

        return new Request<Office365Event>(OFFICE_ONE_DRIVE, url, token)
                .setClass(Office365Event.class)
                .setResource(event)
                .sendPatch()
                .getResource();
    }

    /**
     * @param eventId
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_delete
     */
    @Override
    public void deleteEvent(String eventId, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token).sendDelete();
    }

    /**
     * @param eventId
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_delete
     */
    @Override
    public void deleteEventByCalendarID(String calendarId, String eventId, Office365AccessTokenDTO token) {
        String url = String.format(CALENDAR_ITEM_EVENTS, calendarId);
        url = url + "/" + eventId;

        new Request(OFFICE_ONE_DRIVE, url, token).sendDelete();
    }

    /**
     * @param eventId
     * @param comment
     * @param sendResponse
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_accept
     */
    @Override
    public void acceptEvent(String eventId, final String comment, final Boolean sendResponse, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM_ACCEPT, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token)
                .setResource(new EventRequest(comment, sendResponse))
                .sendPost();
    }

    /**
     * @param eventId
     * @param comment
     * @param sendResponse
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_tentativelyaccept
     */
    @Override
    public void tentativeAcceptEvent(String eventId, final String comment, final Boolean sendResponse, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM_TENTATIVE_ACCEPT, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token)
                .setResource(new EventRequest(comment, sendResponse))
                .sendPost();
    }

    /**
     * @param eventId
     * @param comment
     * @param sendResponse
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_decline
     */
    @Override
    public void declineEvent(String eventId, final String comment, final Boolean sendResponse, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM_DECLINE, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token)
                .setResource(new EventRequest(comment, sendResponse))
                .sendPost();
    }

    /**
     * @param eventId
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_dismissreminder
     */
    @Override
    public void dismissReminder(String eventId, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM_DISMISS_REMINDER, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token).sendPost();
    }

    /**
     * @param eventId
     * @param newReminderTime
     * @param token
     * @see https://graph.microsoft.io/docs/api-reference/v1.0/api/event_snoozereminder
     */
    @Override
    public void snoozeReminder(String eventId, Date newReminderTime, Office365AccessTokenDTO token) {
        String url = String.format(EVENT_ITEM_SNOOZE_REMINDER, eventId);

        new Request(OFFICE_ONE_DRIVE, url, token).setResource(new SnoozeRequest(newReminderTime)).sendPost();
    }

    private static class SnoozeRequest extends Office365BaseResource {
        private Date newReminderTime;

        public SnoozeRequest() {
        }

        public SnoozeRequest(Date newReminderTime) {
            this.newReminderTime = newReminderTime;
        }

        public Date getNewReminderTime() {
            return newReminderTime;
        }

        public void setNewReminderTime(Date newReminderTime) {
            this.newReminderTime = newReminderTime;
        }
    }

    private static class EventRequest extends Office365BaseResource {
        private String comment;
        private Boolean sendResponse;

        public EventRequest() {
        }

        public EventRequest(String comment, Boolean sendResponse) {
            this.comment = comment;
            this.sendResponse = sendResponse;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public Boolean getSendResponse() {
            return sendResponse;
        }

        public void setSendResponse(Boolean sendResponse) {
            this.sendResponse = sendResponse;
        }
    }


    @Override
    public Object[] getOfficeEventsOrTasks(Office365AccessTokenDTO token, EdsEmployee user, Date start, Date end, boolean isEvent) {
        Map<String, Appointment> appointmentList = new HashMap<>();
        Map<String, Office365Event> calendarEventEntryList = new HashMap<>();
        Object[] objects = new Object[2];
        objects[0] = calendarEventEntryList;
        objects[1] = appointmentList;
        Office365ResourceCollection<Office365Event> eventList = null;

        EdsGoogleCalendar officeCalendar = googleCalendarManager.getOfficeCalendar(user, true);
        if (officeCalendar != null) {
            if (isEvent) {
                eventList = listCalendarEventsByStartEndDate(officeCalendar.getCalendarID(), start, end, token);
            } else {
                eventList = listCalendarEventsByStartEndDate(officeCalendar.getTaskCalendarID(), start, end, token);
            }
        }
        if (eventList != null && eventList.getValue() != null && eventList.getValue().size() > 0) {
            for (Office365Event event : eventList.getValue()) {
                appointmentList.put(event.getId(), wrapCalendarEventToAppointment(event, isEvent));
                calendarEventEntryList.put(event.getId(), event);
            }
        }
        return objects;
    }

    @Override
    public Office365Event updateCalendarEventEntry(EdsUser user, Office365Event event, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask) {
        event.setLocation(null);
        event.setIsOrganizer(true);
        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        boolean allDay = false;
        if (employeeEvent != null) {
            EdsEvent edsEvent = employeeEvent.getEvent();
            event.setSubject(edsEvent.getSubject());
//            event.setBodyPreview(edsEvent.getDescription() != null ? edsEvent.getDescription() : "");
            Office365ItemBody body = new Office365ItemBody();
            body.setContentType("HTML");
            body.setContent("<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n</head>\r\n<body>\r\n" + (edsEvent.getDescription() != null ? edsEvent.getDescription() : "")
                    + "\r\n</body>\r\n</html>\r\n");
            event.setBody(body);
            allDay = edsEvent.isAllDay() != null ? edsEvent.isAllDay() : true;
            Date sDate = null, eDate = null;
            if (allDay) {
                Date allDatStart = new Date(edsEvent.getStartDate().getTime() + employeeEvent.getEvent().getTimeZoneOffset());
                allDatStart.setHours(0);
                allDatStart.setMinutes(0);
                allDatStart.setSeconds(0);
                sDate = allDatStart;
                Date allEndDate = new Date(allDatStart.getTime());
                int dayCount = 0;
                if (employeeEvent.getEvent().getDayCount() == null) {
                    Date startD = new Date(employeeEvent.getEvent().getStartDate().getTime());
                    Date endD = new Date(employeeEvent.getEvent().getEndDate().getTime());
                    dayCount = ServerUtils.getDayCountInCalendar(employeeEvent.getEmployee().getUserDate(startD), employeeEvent.getEmployee().getUserDate(endD));
                } else {
                    dayCount = employeeEvent.getEvent().getDayCount();
                }
                allEndDate.setDate(allEndDate.getDate() + (dayCount != 0 ? dayCount : 1));
                eDate = allEndDate;
            } else {
                sDate = edsEvent.getStartDate();
                eDate = edsEvent.getEndDate();
            }
            Office365DateTimeTimeZone startDate = new Office365DateTimeTimeZone();
            startDate.setDateTime(dateFormat.format(sDate));
            startDate.setTimeZone("UTC");

            Office365DateTimeTimeZone endDate = new Office365DateTimeTimeZone();
            endDate.setDateTime(dateFormat.format(eDate));
            endDate.setTimeZone("UTC");

            event.setStart(startDate);
            event.setEnd(endDate);
            event.setIsAllDay(allDay);
            if (employeeEvent.getEvent().getIsPrivate() != null && employeeEvent.getEvent().getIsPrivate()) {
                event.setSensitivity("private");
            } else {
                event.setSensitivity("normal");

            }

            reminders = calendarReminderManager.getReminders(edsEvent.getObjectID());
        }
        if (employeeTask != null) {
            EdsTask edsTask = employeeTask.getTask();
            event.setSubject(edsTask.getName());
//            event.setBodyPreview(edsTask.getDescription() != null ? edsTask.getDescription() : "");
            Office365ItemBody body = new Office365ItemBody();
            body.setContentType("HTML");
            body.setContent("<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n</head>\r\n<body>\r\n" + (edsTask.getDescription() != null ? edsTask.getDescription() : "")
                    + "\r\n</body>\r\n</html>\r\n");
            event.setBody(body);

            allDay = edsTask.isAllDay() != null ? edsTask.isAllDay() : true;
            Date sDate = null, eDate = null;
            if (allDay) {
                Date allDatStart = new Date(edsTask.getStartDate().getTime() + employeeTask.getTask().getTimeZoneOffset());
                allDatStart.setHours(0);
                allDatStart.setMinutes(0);
                allDatStart.setSeconds(0);
                sDate = allDatStart;
                Date allEndDate = new Date(allDatStart.getTime());
                int dayCount = 0;
                if (employeeTask.getTask().getDayCount() == null) {
                    Date startD = new Date(employeeTask.getTask().getStartDate().getTime());
                    Date endD = new Date(employeeTask.getTask().getDueDate().getTime());
                    EdsUser edsUser = userManager.get(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getEmployee().getObjectID());
                    dayCount = ServerUtils.getDayCountInCalendar(edsUser.getUserDate(startD), edsUser.getUserDate(endD));
                } else {
                    dayCount = employeeTask.getTask().getDayCount();
                }
                allEndDate.setDate(allEndDate.getDate() + (dayCount != 0 ? dayCount : 1));
                eDate = allEndDate;
            } else {
                sDate = edsTask.getStartDate();
                eDate = edsTask.getDueDate();
            }
            Office365DateTimeTimeZone startDate = new Office365DateTimeTimeZone();
            startDate.setDateTime(dateFormat.format(sDate));
            startDate.setTimeZone("UTC");

            Office365DateTimeTimeZone endDate = new Office365DateTimeTimeZone();
            endDate.setDateTime(dateFormat.format(eDate));
            endDate.setTimeZone("UTC");


            event.setStart(startDate);
            event.setEnd(endDate);
            event.setIsAllDay(allDay);
            reminders = taskReminderManager.getReminders(edsTask.getObjectID());
        }
        if (reminders != null && reminders.size() > 0) {
            for (CalendarEventReminder calendarReminder : reminders) {
                event.setIsReminderOn(true);
                event.setReminderMinutesBeforeStart(calendarReminder.getReminderTimes());
            }
        }
        return event;
    }

    @Override
    public Office365Event createCalendarEventEntry(EdsUser user, Appointment appointment, EdsEmployeeEvent employeeEvent, EdsEmployeeTask employeeTask) {
        Office365Event event = new Office365Event();
        if (appointment.getOfficeID() != null) {
            event.setId(appointment.getOfficeID());
        }
        event.setSubject(appointment.getSubject());
//        event.setBodyPreview(appointment.getDescription() != null ? appointment.getDescription() : "");
        Office365ItemBody body = new Office365ItemBody();
        body.setContentType("HTML");
        body.setContent("<html>\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\r\n</head>\r\n<body>\r\n" + (appointment.getDescription() != null ? appointment.getDescription() : "")
                + "\r\n</body>\r\n</html>\r\n");
        event.setBody(body);

        event.setIsOrganizer(true);
        ArrayList<CalendarEventReminder> reminders = new ArrayList<>();
        boolean allDay = false;
        if (employeeEvent != null) {
            allDay = employeeEvent.getEvent().isAllDay() != null ? employeeEvent.getEvent().isAllDay() : true;
            reminders = calendarReminderManager.getReminders(employeeEvent.getEvent().getObjectID());
        }
        if (employeeTask != null) {
            EdsTask edsTask = employeeTask.getTask();
            allDay = edsTask.isAllDay() != null ? edsTask.isAllDay() : true;
            reminders = taskReminderManager.getReminders(edsTask.getObjectID());
        }

        Date startDate = appointment.getStartDate();
        Date endDate = appointment.getEndDate();
        if (endDate.before(startDate)) {
            Date newStartDate = (Date) endDate.clone();
            endDate = (Date) startDate.clone();
            startDate = newStartDate;
        }
        if (appointment.isAllDay()) {
            Integer offset = null;
            Integer dayCount = null;
            if (employeeEvent != null && employeeEvent.getEvent() != null) {
                offset = employeeEvent.getEvent().getTimeZoneOffset() != null ? employeeEvent.getEvent().getTimeZoneOffset() : 0;
                if (employeeEvent.getEvent().getDayCount() == null) {
                    Date startD = new Date(employeeEvent.getEvent().getStartDate().getTime());
                    Date endD = new Date(employeeEvent.getEvent().getEndDate().getTime());
                    dayCount = ServerUtils.getDayCountInCalendar(employeeEvent.getEmployee().getUserDate(startD), employeeEvent.getEmployee().getUserDate(endD));
                } else {
                    dayCount = employeeEvent.getEvent().getDayCount();
                }
                dayCount = dayCount != 0 ? dayCount : 1;
            } else if (employeeTask != null && employeeTask.getTask() != null) {
                offset = employeeTask.getTask().getTimeZoneOffset() != null ? employeeTask.getTask().getTimeZoneOffset() : 0;
                if (employeeTask.getTask().getDayCount() == null) {
                    Date startD = new Date(employeeTask.getTask().getStartDate().getTime());
                    Date endD = new Date(employeeTask.getTask().getDueDate().getTime());
                    EdsUser edsUser = userManager.get(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getEmployee().getObjectID());
                    dayCount = ServerUtils.getDayCountInCalendar(edsUser.getUserDate(startD), edsUser.getUserDate(endD));
                } else {
                    dayCount = employeeTask.getTask().getDayCount();
                }
                dayCount = dayCount != 0 ? dayCount : 1;

            } else {
                offset = 0;
                dayCount = 1;
            }
            Date allDatStart = new Date(startDate.getTime() + offset);
            allDatStart.setHours(0);
            allDatStart.setMinutes(0);
            allDatStart.setSeconds(0);
            startDate = allDatStart;
            Date allEndDate = new Date(allDatStart.getTime());
            allEndDate.setDate(allEndDate.getDate() + dayCount);
            endDate = allEndDate;
        }
        Office365DateTimeTimeZone start = new Office365DateTimeTimeZone();
        start.setDateTime(dateFormat.format(startDate));
        start.setTimeZone("UTC");

        Office365DateTimeTimeZone end = new Office365DateTimeTimeZone();
        end.setDateTime(dateFormat.format(endDate));
        end.setTimeZone("UTC");

        event.setStart(start);
        event.setEnd(end);
        event.setIsAllDay(allDay);
        if (employeeEvent != null && employeeEvent.getEvent().getIsPrivate() != null && employeeEvent.getEvent().getIsPrivate()) {
            event.setSensitivity("private");
        }
        if (reminders != null && reminders.size() > 0) {
            for (CalendarEventReminder calendarReminder : reminders) {
                event.setIsReminderOn(true);
                event.setReminderMinutesBeforeStart(calendarReminder.getReminderTimes());
            }
        }
        return event;
    }

    public Appointment wrapCalendarEventToAppointment(Office365Event event, boolean isEvent) {
        if (event == null) {
            return null;
        }
        Appointment appointment = new Appointment();
        if (isEvent) {
            EdsEmployeeEvent kpiEvent = employeeEventManager.getByOfficeID(event.getId());
            if (kpiEvent != null) {
                appointment.setObjectID(kpiEvent.getObjectID());
            }
        } else {
            EdsEmployeeTask kpiTask = employeeTaskManager.getByOfficeID(event.getId());
            if (kpiTask != null) {
                appointment.setObjectID(kpiTask.getObjectID());
            }
        }
        appointment.setOfficeID(event.getId());
        appointment.setSubject(event.getSubject());
        appointment.setDescription(event.getBodyPreview());

        try {
            appointment.setStartDate(event.getStart() != null ? dateFormat.parse(event.getStart().getDateTime()) : null);
            appointment.setEndDate(event.getEnd() != null ? dateFormat.parse(event.getEnd().getDateTime()) : null);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        appointment.setAllDay(event.getIsAllDay());
        if ("private".equals(event.getSensitivity())) {
            appointment.setIsPrivate(true);
        }

        try {
            appointment.setLastModifiedDate(dateFormat.parse(event.getLastModifiedDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Office365Recipient author = event.getOrganizer();
        if (author != null && author.getEmailAddress() != null && !"".equals(author.getEmailAddress())) {
            appointment.setCreatedBy(author.getEmailAddress().getName());
        }

        /*if (event.getLocation() != null && event.getLocation().length() > 0) {
            appointment.setLocation(event.getLocation());
        }*/

        appointment.setMultiDay(appointment.isMultiDayAppointment());
        return appointment;
    }
}
