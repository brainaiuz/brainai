package com.edatasite.workforce.gwt.office365.server.app;

import com.edatasite.workforce.core.domain.CalendarObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TaskSolrEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365SettingsManager;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Calendar;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Event;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365ResourceCollection;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365EventService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Fetcher;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarService;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarSettings;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Transactional
@Service("office365CalendarService")
public class Office365CalendarServiceImpl implements Office365CalendarService, Office365CalendarServiceLocal, SchedulerConstant, Constants, Office365Constants {
    private final static TypeReference calendarItemListType = new TypeReference<Office365ResourceCollection<Office365Calendar>>() {
    };

    private static final Logger log = LoggerFactory.getLogger(Office365CalendarServiceImpl.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private Office365SettingsManager office365SettingsManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365EventService office365EventService;
    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void saveCalendarSettings(Office365CalendarSettings settings) {
        office365SettingsManager.updateCalendarSettings(settings);
    }

    @Override
    public void saveToken(Office365AccessTokenDTO token) {
        EdsUser user = userManager.getUser();
        EdsEmployee employee = employeeManager.get(user.getObjectID());

        if (!googleCalendarManager.validateOfficeUser(user)) {
            EdsGoogleCalendar googleCalendar = googleCalendarManager.getOfficeCalendar(user, false);

            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
            if (googleCalendar != null) {
                if (googleCalendar.getCalendarID() != null && (userSettings == null || !userSettings.isSyncFromDefaultCalendar())) {
                    deleteCalendar(googleCalendar.getCalendarID(), token);
                }
                if (googleCalendar.getTaskCalendarID() != null) {
                    deleteCalendar(googleCalendar.getTaskCalendarID(), token);
                }
                googleCalendarManager.delete(googleCalendar);
            }
            String primeryID = null;


            Office365ResourceCollection<Office365Calendar> list = null;
            try {
                list = getCalendarList(token);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (list != null && (list.getValue() != null && !list.getValue().isEmpty())) {
                primeryID = list.getValue().get(0).getId();
                log.info("User's primeryID: " + primeryID);
                if (primeryID == null) {
                    return;
                }

                googleCalendar = new EdsGoogleCalendar();
                googleCalendar.setUser(user);
                googleCalendar.setOfficeCalendar(true);
                googleCalendar.setGoogleID(primeryID);
                googleCalendar.setToken(token.getAccessToken());
                googleCalendar.setRefreshToken(token.getRefreshToken());
                googleCalendar.setCalendarTimeZone(user.getCompany() != null ? user.getCompany().getCountryZone().getZone() : null);
                googleCalendarManager.create(googleCalendar);

                String eventCalendarID = null;
                if (userSettings != null && userSettings.isSyncFromDefaultCalendar()) {
                    eventCalendarID = primeryID;
                } else {
                    eventCalendarID = getCalendarID(list, token);
                }
                log.info("Office Event Calendar ID: " + eventCalendarID);
                if (eventCalendarID == null) {
                    googleCalendarManager.delete(googleCalendar);
                    return;
                }
                googleCalendar.setCalendarID(eventCalendarID);

                if (user.getCalendarID() != null && !eventCalendarID.equals(user.getCalendarID())) {
                    employeeEventManager.removeOfficeIDFromEmployeeEvents(employee);
                    employeeEventManager.removeOfficeIDFromEvents(employee);
                }

                String taskCalendarID = null;
                try {
                    taskCalendarID = getTaskCalendarID(list, token);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                log.info("Office Task Calendar ID: " + taskCalendarID);
                if (taskCalendarID == null) {
                    googleCalendarManager.delete(googleCalendar);
                    return;
                }
                googleCalendar.setTaskCalendarID(taskCalendarID);
                if (user.getTaskCalendarID() != null && !taskCalendarID.equals(user.getTaskCalendarID())) {
                    employeeTaskManager.removeOfficeIDFromEmployeeTasks(employee);
                    employeeTaskManager.removeOfficeIDFromTasks(employee);
                }
                user.setCalendarID(eventCalendarID);
                user.setTaskCalendarID(taskCalendarID);

                userManager.update(user);
                googleCalendarManager.update(googleCalendar);
            }
        }
    }

    private void deleteCalendar(String calendarID, Office365AccessTokenDTO token) {
        office365EventService.deleteCalendar(calendarID, token);
    }

    private String getTaskCalendarID(Office365ResourceCollection<Office365Calendar> list, Office365AccessTokenDTO tokenDTO) {
        EdsCompany company = userManager.getUser().getCompany();
        String newCalendarName = company.getName() + " Tasks";
        for (Office365Calendar calendar : list.getValue()) {
            if (newCalendarName.equals(calendar.getName())) {
                return calendar.getId();
            }
        }

        if (tokenDTO == null) {
            tokenDTO = office365AuthService.getUserAccessToken(Constants.OFFICE_365);
        }
        Office365Calendar item = new Office365Calendar();
        item.setName(newCalendarName);
        item.setColor("auto");
        item.setChangeKey("changeKey-value");

        Office365Calendar calendar = office365EventService.createCalendar(item, tokenDTO);
        return calendar != null ? calendar.getId() : null;
    }

    private String getCalendarID(Office365ResourceCollection<Office365Calendar> list, Office365AccessTokenDTO tokenDTO) {
        EdsCompany company = userManager.getUser().getCompany();
        String newCalendarName = company.getName() + " Events";
        for (Office365Calendar calendar : list.getValue()) {
            if (newCalendarName.equals(calendar.getName())) {
                return calendar.getId();
            }
        }

        if (tokenDTO == null) {
            tokenDTO = office365AuthService.getUserAccessToken(Constants.OFFICE_365);
        }
        Office365Calendar item = new Office365Calendar();
        item.setName(newCalendarName);
        item.setColor("auto");
        item.setChangeKey("changeKey-value");

        Office365Calendar calendar = office365EventService.createCalendar(item, tokenDTO);
        return calendar != null ? calendar.getId() : null;
    }

    public Office365ResourceCollection<Office365Calendar> getCalendarList(Office365AccessTokenDTO tokenDTO) {
        String url = CALENDAR_LIST;
        url = url + "?$top=100";

        if (tokenDTO == null) {
            tokenDTO = office365AuthService.getUserAccessToken(Constants.OFFICE_365);
        }

        return new Office365Fetcher.Request<Office365ResourceCollection<Office365Calendar>>(OFFICE_ONE_DRIVE, url, tokenDTO)
                .setTypeReference(calendarItemListType)
                .sendGet()
                .getResource();

    }

    public Office365ResourceCollection<Office365Calendar> getCalendarList() {
        return this.getCalendarList(null);
    }


    public ArrayList<Appointment> syncEvents(String hostUrl, Integer userId, Date start, Date end) throws ParseException {
        HashSet<Integer> objectIDs = new HashSet<>();
        HashSet<Integer> deletedObjectIDs = new HashSet<>();

        EdsUser user;

        if (userId != null) {
            user = userManager.get(userId);
        } else {
            user = userManager.getUser();
        }


        if (user == null || user.getDeleted() || !user.getCompany().getActive()) {
            return null;
        }
        if (hostUrl == null) {
            hostUrl = EdsContextParams.getHost();
        }

        System.out.println(" ************  user id *****************************   " + (user != null ? user.getObjectID() : " null"));
        System.out.println(" ************  host token *****************************   " + (hostUrl != null ? hostUrl : " null"));

        Office365AccessTokenDTO token = office365AuthService.getUserAccessToken(hostUrl, user, Constants.OFFICE_365);
        System.out.println(" ************ token *****************************   " + (token != null ? " good" : " null"));

        if (token == null) {
            return null;
        }

        EdsGoogleCalendar calendar = googleCalendarManager.getOfficeCalendar(user, true);

        if (calendar == null || calendar.getCalendarTimeZone() == null) {
            return null;
        }

        System.out.print("Starting synchronization for office calendar for userID: " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");

        HashMap<Integer, Office365Event> eventsToAdd = new HashMap<>();
        HashMap<Integer, EdsEmployeeEvent> newEmployeeEvents = new HashMap<>();

        HashMap<Integer, Office365Event> eventsToEdit = new HashMap<>();
        HashMap<Integer, EdsEmployeeEvent> newEmployeEeventsToEdit = new HashMap<>();

        HashMap<Integer, Office365Event> eventsToDelete = new HashMap<>();
        HashMap<Integer, EdsEmployeeEvent> newEmployeEeventsToDelete = new HashMap<>();

        HashMap<Integer, Office365Event> tasksToAdd = new HashMap<>();
        HashMap<Integer, EdsEmployeeTask> newEmployeeTaskAdd = new HashMap<>();

        HashMap<Integer, Office365Event> tasksToEdit = new HashMap<>();
        HashMap<Integer, EdsEmployeeTask> newEmployeTaskToEdit = new HashMap<>();

        HashMap<Integer, Office365Event> tasksToDelete = new HashMap<>();
        HashMap<Integer, EdsEmployeeTask> newEmployeTaskToDelete = new HashMap<>();

        EdsEmployee employee = user.getEmployee();

        Object[] events = office365EventService.getOfficeEventsOrTasks(token, employee, start, end, true);

        Map<String, Appointment> officeEventsMap = (Map<String, Appointment>) events[1];
        Map<String, Office365Event> calendarEventEntries = (HashMap<String, Office365Event>) events[0];
        // get events created or shared this user with date range
        List<Integer> idList = new ArrayList<>();
        idList.add(employee.getObjectID());
        List<EdsEmployeeEvent> employeeEvents = employeeEventManager.getCalendarEvents(idList, start, end, false, null, false);

        // get office Calendar tasks with date range from Office as Appointment and CalendarEventEntry Map
        Object[] tasks = office365EventService.getOfficeEventsOrTasks(token, employee, start, end, false);
        Map<String, Appointment> officeTasksMap = (Map<String, Appointment>) tasks[1];
        Map<String, Office365Event> calendarTaskEntries = (HashMap<String, Office365Event>) tasks[0];
        // get tasks created or shared this user with date range (PM, CRM, Calendar tasks)
        List<EdsEmployeeTask> employeeTasks = taskManager.getCalendarTasks(idList, start, end, false);

        log.info("Initial Events from Office: " + officeEventsMap.values().size());
        log.info("Initial Events from KPI: " + employeeEvents.size());

        normalizeOfficeAndKpiCalendarCollections(employeeEvents, officeEventsMap, calendarEventEntries, token, user, true, start);

        for (EdsEmployeeEvent employeeEvent : employeeEvents) {
            if (employeeEvent.getOfficeID() != null) { // Event previously synced with Office
                if (officeEventsMap.containsKey(employeeEvent.getOfficeID())) {// if contained in Office, Update event based on last modified time
                    Appointment appointment = officeEventsMap.get(employeeEvent.getOfficeID());
                    if (appointment.getLastModifiedDate().after(employeeEvent.getLastModifiedDate())) {// Office event has latest version so updated WFT event
                        // Event updated on Office end, so update WFT event
                        EdsEvent event = employeeEvent.getEvent();
                        event.setSubject(appointment.getSubject());
                        event.setVenue(appointment.getLocation());
                        event.setDescription(appointment.getDescription());
                        if (appointment.isAllDay()) {
                            Date allStartDate = new Date(appointment.getStartDate().getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                            Date allEndDate = new Date(appointment.getEndDate().getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                            allEndDate.setHours(allEndDate.getHours() - 1);
                            event.setStartDate(allStartDate);
                            event.setEndDate(allEndDate);
                        } else {
                            event.setStartDate(appointment.getStartDate());
                            event.setEndDate(appointment.getEndDate());
                        }
                        event.setAllDay(appointment.isAllDay());
                        event.setLastModifiedDate(appointment.getLastModifiedDate() != null ? appointment.getLastModifiedDate() : new Date()); //Set Google events last modified date to Employee Events, and Events Last Modified Date
                        event.setLastModifiedBy(user);
                        event.setPrivate(appointment.getIsPrivate());
                        eventManager.update(event);
                        objectIDs.add(event.getObjectID());
                        employeeEventManager.setEmployeeEventsModifiedDate(employeeEvent.getEvent(), appointment.getLastModifiedDate());
                        officeEventsMap.remove(employeeEvent.getOfficeID());
                        calendarEventEntries.remove(employeeEvent.getOfficeID());
                        log.info("Event updated on Office end, update WFT event. EventID: " + event.getObjectID() + "; name: " + event.getSubject());
                    } else if (appointment.getLastModifiedDate().before(employeeEvent.getLastModifiedDate())) { // WFT event has latest version so update Office event
                        Office365Event eventEntry = calendarEventEntries.get(employeeEvent.getOfficeID());
                        if (employeeEvent != null) {
                            Office365Event office365Event = office365EventService.updateCalendarEventEntry(user, eventEntry, employeeEvent, null);
                            Integer index = getNewKey(eventsToEdit.keySet());
                            if (office365Event.getIsAllDay()) {
                                office365Event.setOriginalStartTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.setOriginalEndTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.getStart().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.getEnd().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                            }
                            eventsToEdit.put(index, office365Event);
                            newEmployeEeventsToEdit.put(index, employeeEvent);

                            officeEventsMap.remove(employeeEvent.getOfficeID());
                            calendarEventEntries.remove(employeeEvent.getOfficeID());
                        }
                    } else {
                        // No need to sync since nothing has been changed since then
                        officeEventsMap.remove(employeeEvent.getOfficeID());
                        calendarEventEntries.remove(employeeEvent.getOfficeID());
                    }
                } else { // if Not contained in Office, means it has been deleted from Office, so delete from WFT
                    //THE FOLLOWING CHECK ALWAYS RETURNS NULL! THEREFORE COMMENTED (FARHOD)
                    //CalendarEventEntry eventEntry = googleManager.getEntry(calendarService, employeeEvent.getOfficeID(), CalendarEventEntry.class);
                    //if (eventEntry == null) {// if there is indeed no event in Office then delete from WFT. It might be that Office event did not fall in the date range period we have specified, that's why double checking
                    employeeEvent.setDeleted(true);
                    List<EdsEmployeeEvent> employeeEventList = employeeEventManager.getEmployeeEvents(employeeEvent.getEvent().getObjectID());
                    if (employeeEventList != null && !employeeEventList.isEmpty()) {
                        //
                    } else {
                        employeeEvent.getEvent().setDeleted(true);
                        employeeEvent.getEvent().setLastModifiedBy(user);
                        employeeEvent.getEvent().setLastModifiedDate(new Date());
                        deletedObjectIDs.add(employeeEvent.getEvent().getObjectID());
                    }
                    log.info("Event deleted while sync with office. EventID: " + employeeEvent.getEvent().getObjectID() + "; employeeEventID: " + employeeEvent.getObjectID() + "; date: " + new Date());
                }
            } else { // New event not yet synced with Office
                Appointment appointment = new Appointment();
                EdsEvent event = employeeEvent.getEvent();
                appointment.setSubject(event.getSubject());
                appointment.setDescription(event.getDescription());
                appointment.setLocation(event.getVenue());
                appointment.setStartDate(event.getStartDate());
                appointment.setEndDate(event.getEndDate());
                appointment.setAllDay(event.isAllDay());
                Office365Event eventEntry = office365EventService.createCalendarEventEntry(user, appointment, employeeEvent, null);
                Integer index = getNewKey(eventsToAdd.keySet());
                if (eventEntry.getIsAllDay()) {
                    eventEntry.setOriginalStartTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.setOriginalEndTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.getStart().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.getEnd().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                }
                eventsToAdd.put(index, eventEntry);
                newEmployeeEvents.put(index, employeeEvent);
            }
        }

        // Add new events from Office to WFT, or delete old WFT events from Office
        for (Appointment appointment : officeEventsMap.values().toArray(new Appointment[]{})) {
            if (appointment.getObjectID() != null) {
                EdsEmployeeEvent checkEmployeeEvent = employeeEventManager.get(appointment.getObjectID());
                if (checkEmployeeEvent == null || (checkEmployeeEvent != null && checkEmployeeEvent.getDeleted())) {// if there is indeed no event in WFT then delete from Office. It might be that WFT event did not fall in the date range period we have specified, that's why double checking
                    Office365Event myEntry = calendarEventEntries.get(appointment.getOfficeID());
                    if (myEntry != null) {
                        Integer index = getNewKey(eventsToDelete.keySet());
                        System.out.print("Events  To delete from Office: " + myEntry.getSubject());
                        eventsToDelete.put(index, myEntry);
                        newEmployeEeventsToDelete.put(index, checkEmployeeEvent);
                    }
                }
            } else {
                Date sDate = (Date) appointment.getStartDate().clone();
                Date eDate = (Date) appointment.getEndDate().clone();
                if (appointment.isAllDay()) {
                    Date allStartDate = new Date(sDate.getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                    Date allEndDate = new Date(eDate.getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                    allEndDate.setHours(allEndDate.getHours() - 1);
                    appointment.setStartDate(allStartDate);
                    appointment.setEndDate(allEndDate);
                } else {
                    appointment.setStartDate(sDate);
                    appointment.setEndDate(eDate);
                }
                googleCalendarServiceLocal.saveCalendarEvent(employee.getObjectID(), appointment, true, false, true, objectIDs, null);
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        log.info("Initial Tasks from Office: " + officeTasksMap.values().size());
        log.info("Initial Tasks from KPI: " + employeeTasks.size());

        normalizeOfficeAndKpiCalendarCollections(employeeTasks, officeTasksMap, calendarTaskEntries, token, user, false, start);

        for (EdsEmployeeTask employeeTask : employeeTasks) {
            if (employeeTask.getOfficeID() != null) { // Task previously synced with Office
                if (officeTasksMap.containsKey(employeeTask.getOfficeID())) { // if contained in Office, Update task based on last modified time
                    Appointment appointment = officeTasksMap.get(employeeTask.getOfficeID());
                    if (appointment.getLastModifiedDate().after(employeeTask.getLastModifiedDate())) {// Office task has latest version so updated WFT task
                        // Task updated on Office end, so update WFT task
                        EdsTask task = employeeTask.getTask();
                        task.setName(appointment.getSubject());
                        task.setDescription(appointment.getDescription());
                        task.setAllDay(appointment.isAllDay());
                        if (appointment.isAllDay()) {
                            Date allStartDate = new Date(appointment.getStartDate().getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                            Date allEndDate = new Date(appointment.getEndDate().getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                            allEndDate.setHours(allEndDate.getHours() - 1);
                            task.setStartAndDueDates(allStartDate, allEndDate);
                        } else {
                            task.setStartAndDueDates(appointment.getStartDate(), appointment.getEndDate());
                        }
                        task.setLastModifiedDate(appointment.getLastModifiedDate());//Set Office tasks last modified date to Employee Tasks, and Tasks Last Modified Date
                        taskManager.update(task);
                        employeeTaskManager.setEmployeeTasksModifiedDate(task, appointment.getLastModifiedDate());
                        EdsBusinessEvent taskBusinessEvent = baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
                        EdsCompany company = user.getCompany();
                        try {
                            taskSolrComponent.index(task);
                            taskBusinessEvent.setSolrIndexed(true);
                            SolrEvent event = solrTransactionManager.registerEvent(SolrEvent.TASK_ADD, task, company);
                        } catch (Exception e) {
                            taskBusinessEvent.setSolrIndexed(false);
                        }
                        officeTasksMap.remove(employeeTask.getOfficeID());
                        calendarTaskEntries.remove(employeeTask.getOfficeID());
                        log.info("Task updated on Office end, update WFT task. EventID: " + task.getObjectID() + "; name: " + task.getName());
                    } else if (appointment.getLastModifiedDate().before(employeeTask.getLastModifiedDate())) {// WFT task has latest version so update Office task
                        Office365Event eventEntry = calendarTaskEntries.get(employeeTask.getOfficeID());
                        if (eventEntry != null) {
                            Office365Event office365Event = office365EventService.updateCalendarEventEntry(user, eventEntry, null, employeeTask);
                            Integer index = getNewKey(tasksToEdit.keySet());
                            if (office365Event.getIsAllDay()) {
                                office365Event.setOriginalStartTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.setOriginalEndTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.getStart().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                                office365Event.getEnd().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                            }
                            tasksToEdit.put(index, office365Event);
                            newEmployeTaskToEdit.put(index, employeeTask);
                            officeTasksMap.remove(employeeTask.getOfficeID());
                            calendarTaskEntries.remove(employeeTask.getOfficeID());
                        }
                    } else {
                        // No need to sync since nothing has been changed since then
                        officeTasksMap.remove(employeeTask.getOfficeID());
                        calendarTaskEntries.remove(employeeTask.getOfficeID());
                    }
                } else { // if Not contained in Office, that means it was removed from Office, so remove from WFT
                    //THE FOLLOWING CHECK ALWAYS RETURNS NULL! THEREFORE COMMENTED (FARHOD)
                    //Appointment checkGoogleTask = googleCalendarManager.getGoogleTask(employee, employeeTask.getOfficeID());
                    //if (checkGoogleTask == null) {// if there is indeed no task in Office then delete from WFT. It might be that Office task did not fall in the date range period we have specified, that's why double checking
//                                log.info("ET might have been deleted while sync with Office. Employee Task ID:" + employeeTask.getObjectID());
                    EdsTask task = employeeTask.getTask();
                    log.info("Task deleted while sync with office. TaskID: " + task.getObjectID() + "; employeeTaskID: " + employeeTask.getObjectID() + "; date: " + new Date());
                    int size = task.getUnDeletedAssignments().size();
                    employeeTaskManager.deleteEmployeeTask(employeeTask);
                    size--;
                    if (size == 0) {  // If task was assigned to himself only, so delete the whole task
                        taskServiceLocal.deleteTask(task, user, null);
                    }
                }
            } else { // New task not yet synced with Office
                Appointment appointment = new Appointment();
                EdsTask task = employeeTask.getTask();
                appointment.setSubject(task.getName());
                appointment.setDescription(task.getDescription());
                if (task.getProject() != null) {
                    appointment.setLocation(task.getProject().getName());
                }
                appointment.setStartDate(task.getStartDate());
                appointment.setEndDate(task.getDueDate());
                appointment.setAllDay(task.isAllDay());
                Office365Event eventEntry = office365EventService.createCalendarEventEntry(user, appointment, null, employeeTask);
                task.setLastUpdateTime(employeeTask.getLastModifiedDate());
                if (eventEntry.getIsAllDay()) {
                    eventEntry.setOriginalStartTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.setOriginalEndTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.getStart().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                    eventEntry.getEnd().setTimeZone(calendar.getCalendarTimeZone().getMicrosoftZoneID());
                }
                Integer index = getNewKey(tasksToAdd.keySet());
                tasksToAdd.put(index, eventEntry);
                newEmployeeTaskAdd.put(index, employeeTask);
            }
        }

        // Add new tasks from Office to WFT, or delete old WFT tasks from Office
        for (Appointment appointment : officeTasksMap.values().toArray(new Appointment[]{})) {
            if (appointment.getObjectID() != null) {
                EdsEmployeeTask checkEmployeeTask = employeeTaskManager.get(appointment.getObjectID());
                if (checkEmployeeTask == null || checkEmployeeTask.getDeleted()) {// if there is indeed no task in WFT then delete from Office. It might be that WFT task did not fall in the date range period we have specified, that's why double checking
                    Office365Event taskEntry = calendarTaskEntries.get(appointment.getOfficeID());
                    if (taskEntry != null) {
                        Integer index = getNewKey(tasksToDelete.keySet());
                        System.out.print("Task  To delete from office: " + taskEntry.getSubject());
                        tasksToDelete.put(index, taskEntry);
                        newEmployeTaskToDelete.put(index, checkEmployeeTask);
                    }
                }
            } else {
                if (user.getCompany().getDefaultProject() != null) { // if there is no Default project we can't add this event sorry
                    TaskSingleItem newTask = new TaskSingleItem();
                    newTask.setName(appointment.getSubject());
                    newTask.setDescription(appointment.getDescription());
                    newTask.setProjectID(user.getCompany().getDefaultProject().getObjectID());

                    Date sDate = appointment.getStartDate();
                    Date eDate = appointment.getEndDate();
                    if (appointment.isAllDay()) {
                        Date allStartDate = new Date(sDate.getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                        Date allEndDate = new Date(eDate.getTime() - (calendar.getCalendarTimeZone().getTimeZoneOffset()));
                        allEndDate.setHours(allEndDate.getHours() - 1);
                        newTask.setStartDate(allStartDate);
                        newTask.setDueDate(allEndDate);
                    } else {
                        newTask.setStartDate(sDate);
                        newTask.setDueDate(eDate);
                    }
                    newTask.setLastModified(appointment.getLastModifiedDate());
                    newTask.setOfficeID(appointment.getOfficeID());
                    newTask.setAllDay(appointment.isAllDay());

                    newTask.setPriorityID(referenceManager.findReference(EdsTask.TASK_PRIORITY, EdsTask.MEDIUM).getObjectID());
                    newTask.setStatusID(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED).getObjectID());

                    EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(user.getEmployee(), user.getCompany().getDefaultProject());
                    newTask.setProjectEmployees(new IdTime[]{new IdTime(projectEmployee.getObjectID(), 0, Float.valueOf("0.0"))});
                    newTask.setTaskCreatorID(userId);
                    SelectItem taskId = googleCalendarServiceLocal.saveCalendarTask(newTask, user.getObjectID());
                    EdsTask task = taskManager.get(taskId.getId());
                    EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), user.getObjectID());
                    if (employeeTask != null) {
                        employeeTask.setOfficeID(appointment.getOfficeID());
                        employeeTask.setLastModifiedDate(appointment.getLastModifiedDate());
                        employeeTaskManager.update(employeeTask);
                    }
                }
            }
        }


        System.out.print("Events To Add count ************* " + eventsToAdd.size() + "\n");
        if (eventsToAdd != null && eventsToAdd.size() > 0 && !eventsToAdd.values().isEmpty()) {
            runEventOperation(user, token, eventsToAdd, newEmployeeEvents, "insert");
        }
        System.out.print("Events To Edit count ************* " + eventsToEdit.size() + "\n");
        if (eventsToEdit != null && eventsToEdit.size() > 0 && !eventsToEdit.values().isEmpty()) {
            runEventOperation(user, token, eventsToEdit, newEmployeEeventsToEdit, "update");
        }
        System.out.print("Events To Delete count ************* " + eventsToDelete.size() + "\n");
        if (eventsToDelete != null && eventsToDelete.size() > 0 && !eventsToDelete.values().isEmpty()) {
            runEventOperation(user, token, eventsToDelete, newEmployeEeventsToDelete, "delete");
        }

        System.out.print("Tasks To Add count ************* " + tasksToAdd.size() + "\n");
        if (tasksToAdd != null && tasksToAdd.size() > 0 && !tasksToAdd.values().isEmpty()) {
            runTaskOperation(user, token, tasksToAdd, newEmployeeTaskAdd, "insert");
        }
        System.out.print("Tasks To Edit count ************* " + tasksToEdit.size() + "\n");
        if (tasksToEdit != null && tasksToEdit.size() > 0 && !tasksToEdit.values().isEmpty()) {
            runTaskOperation(user, token, tasksToEdit, newEmployeTaskToEdit, "update");
        }
        System.out.print("Tasks To delete count ************* " + tasksToDelete.size() + "\n");
        if (tasksToDelete != null && tasksToDelete.size() > 0 && !tasksToDelete.values().isEmpty()) {
            runTaskOperation(user, token, tasksToDelete, newEmployeTaskToDelete, "delete");
        }

        System.out.print("Ending synchronization for office calendar for userId " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");
        ArrayList<Appointment> appointments = new ArrayList<>();
        if (officeEventsMap != null && officeEventsMap.size() > 0) {
            appointments.addAll(Arrays.asList(officeEventsMap.values().toArray(new Appointment[]{})));
        }
        eventManager.addToSolr(objectIDs.toArray(new Integer[]{}));
        try {
            solrManager.deleteEvents(deletedObjectIDs.toArray(new Integer[]{}));
            return appointments;
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void normalizeOfficeAndKpiCalendarCollections(List<? extends CalendarObject> employeeCalendarObjects,
                                                          Map<String, Appointment> googleCalendarObjectsMap,
                                                          Map<String, Office365Event> calendarEventEntries,
                                                          Office365AccessTokenDTO token, EdsUser user,
                                                          boolean isEvent, Date start) {

        EdsGoogleCalendar googleCalendar = googleCalendarManager.getOfficeCalendar(user, true);
        List<Office365Event> eventList = null;
        if (googleCalendar != null) {
            if (isEvent) {
                Office365ResourceCollection<Office365Event> eventCollection = office365EventService.listCalendarEvents(googleCalendar.getCalendarID(), token, start);
                if (eventCollection != null && eventCollection.getValue() != null && !eventCollection.getValue().isEmpty()) {
                    eventList = eventCollection.getValue();
                }

            } else {
                Office365ResourceCollection<Office365Event> eventCollection = office365EventService.listCalendarEvents(googleCalendar.getTaskCalendarID(), token, start);
                if (eventCollection != null && eventCollection.getValue() != null && !eventCollection.getValue().isEmpty()) {
                    eventList = eventCollection.getValue();
                }
            }
        }

        if (eventList != null && !eventList.isEmpty()) {
            for (CalendarObject employeeEvent : employeeCalendarObjects) {
                for (Office365Event calendarEvent : eventList) {
                    //if selected range of wft calendar has some events/tasks that are outside the selected range from office calendar, then we should also grab them and put them into Maps
                    if (employeeEvent.getOfficeID() != null && !googleCalendarObjectsMap.containsKey(calendarEvent.getId()) && employeeEvent.getOfficeID().equals(calendarEvent.getId())) {
                        googleCalendarObjectsMap.put(calendarEvent.getId(), office365EventService.wrapCalendarEventToAppointment(calendarEvent, isEvent));
                        calendarEventEntries.put(calendarEvent.getId(), calendarEvent);
                    }
                }
            }
        }

        //convert employeeCalendarObjects list into map to easily check for ContainsKey
        Map<Integer, String> tempEmployeeEventMap = new HashMap<>();
        for (CalendarObject employeeEvent : employeeCalendarObjects) {
            tempEmployeeEventMap.put(employeeEvent.getObjectID(), "");
        }

        //if some office events/tasks can also be found in wft, but did not fall into employeeCalendarObjects due to time range,
        // then we need to put them into employeeCalendarObjects to avoid not updating in wft
        for (Appointment appointment : googleCalendarObjectsMap.values().toArray(new Appointment[]{})) {
            if (appointment.getObjectID() != null) {
                if (isEvent) {
                    EdsEmployeeEvent checkEmployeeEvent = employeeEventManager.get(appointment.getObjectID());
                    if (checkEmployeeEvent != null && !checkEmployeeEvent.getDeleted() && !tempEmployeeEventMap.containsKey(checkEmployeeEvent.getObjectID())) {
                        ((List<CalendarObject>) employeeCalendarObjects).add(checkEmployeeEvent);
                    } else if (checkEmployeeEvent != null && !checkEmployeeEvent.getDeleted() && tempEmployeeEventMap.containsKey(checkEmployeeEvent.getObjectID())) {
                        for (EdsEmployeeEvent object : (List<EdsEmployeeEvent>) employeeCalendarObjects) {
                            if (object.getObjectID().equals(appointment.getObjectID())) {
                                object.setOfficeID(appointment.getOfficeID());
                            }
                        }
                    }
                } else {
                    EdsEmployeeTask checkEmployeeTask = employeeTaskManager.get(appointment.getObjectID());
                    if (checkEmployeeTask != null && !checkEmployeeTask.getDeleted() && !tempEmployeeEventMap.containsKey(checkEmployeeTask.getObjectID())) {
                        ((List<CalendarObject>) employeeCalendarObjects).add(checkEmployeeTask);
                    }
                }
            }
        }
    }


    private Integer getNewKey(Set<Integer> keys) {
        int i = 1;
        if (keys != null && !keys.isEmpty()) {
            Integer[] keyList = keys.toArray(new Integer[0]);
            i = keyList[keyList.length - 1] + 1;
        }
        return i;
    }

    private void runEventOperation(EdsUser user, Office365AccessTokenDTO token, HashMap<Integer, Office365Event> values, HashMap<Integer, EdsEmployeeEvent> employeeEvents, String name) throws ParseException {
        EdsGoogleCalendar googleCalendar = googleCalendarManager.getOfficeCalendar(user, true);
        for (Integer key : values.keySet())
            if ("insert".equals(name)) {
                Office365Event event = office365EventService.createCalendarEvent(googleCalendar.getCalendarID(), values.get(key), token);
                if (event != null && employeeEvents.get(key) != null) {
                    EdsEmployeeEvent edsEmployeeEvent = employeeEvents.get(key);
                    EdsEvent edsEvent = edsEmployeeEvent.getEvent();
                    edsEvent.setOfficeID(event.getId());
                    edsEmployeeEvent.setOfficeID(event.getId());
                    edsEvent.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    edsEmployeeEvent.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    eventManager.update(edsEvent);
                    employeeEventManager.update(edsEmployeeEvent);
                    log.info("Successfully executed batch request (insert) for event: " + edsEvent.getSubject() + "; employeeEventID: " + edsEmployeeEvent.getObjectID());
                }
            } else if ("update".equals(name)) {
                Office365Event event = office365EventService.updateEventByCalendarID(googleCalendar.getCalendarID(), values.get(key), token);
                if (event != null && employeeEvents.get(key) != null) {
                    EdsEmployeeEvent edsEmployeeEvent = employeeEvents.get(key);
                    EdsEvent edsEvent = edsEmployeeEvent.getEvent();
                    edsEvent.setOfficeID(event.getId());
                    edsEmployeeEvent.setOfficeID(event.getId());
                    edsEvent.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    edsEmployeeEvent.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    eventManager.update(edsEvent);
                    employeeEventManager.update(edsEmployeeEvent);
                    log.info("Successfully executed batch request (update) for event: " + edsEvent.getSubject() + "; employeeEventID: " + edsEmployeeEvent.getObjectID());
                }
            } else if ("delete".equals(name)) {
                office365EventService.deleteEventByCalendarID(googleCalendar.getCalendarID(), values.get(key).getId(), token);
                System.out.print("Events deleted  ************* " + values.get(key).getSubject() + "\n");
            }
    }

    private void runTaskOperation(EdsUser user, Office365AccessTokenDTO token, HashMap<Integer, Office365Event> values, HashMap<Integer, EdsEmployeeTask> employeeEvents, String name) throws ParseException {
        EdsGoogleCalendar googleCalendar = googleCalendarManager.getOfficeCalendar(user, true);
        for (Integer key : values.keySet())
            if ("insert".equals(name)) {
                Office365Event event = office365EventService.createCalendarEvent(googleCalendar.getTaskCalendarID(), values.get(key), token);
                if (event != null && employeeEvents.get(key) != null) {
                    EdsEmployeeTask edsEmployeeTask = employeeEvents.get(key);
                    EdsTask edsTask = edsEmployeeTask.getTask();
                    edsTask.setOfficeID(event.getId());
                    edsEmployeeTask.setOfficeID(event.getId());
                    edsTask.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    edsEmployeeTask.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    taskManager.update(edsTask);
                    employeeTaskManager.update(edsEmployeeTask);
                    log.info("Successfully executed batch request (insert) for task: " + edsTask.getName() + "; employeeTaskID: " + edsEmployeeTask.getObjectID());
                }
            } else if ("update".equals(name)) {
                Office365Event event = office365EventService.updateEventByCalendarID(googleCalendar.getTaskCalendarID(), values.get(key), token);
                if (event != null && employeeEvents.get(key) != null) {
                    EdsEmployeeTask edsEmployeeTask = employeeEvents.get(key);
                    EdsTask edsTask = edsEmployeeTask.getTask();
                    edsTask.setOfficeID(event.getId());
                    edsEmployeeTask.setOfficeID(event.getId());
                    edsTask.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    edsEmployeeTask.setLastModifiedDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(event.getLastModifiedDateTime()));
                    taskManager.update(edsTask);
                    employeeTaskManager.update(edsEmployeeTask);
                    log.info("Successfully executed batch request (update) for task: " + edsTask.getName() + "; employeeTaskID: " + edsEmployeeTask.getObjectID());
                }
            } else if ("delete".equals(name)) {
                office365EventService.deleteEventByCalendarID(googleCalendar.getTaskCalendarID(), values.get(key).getId(), token);
                System.out.print("Tasks deleted  ************* " + values.get(key).getSubject() + "\n");
            }
    }

}
