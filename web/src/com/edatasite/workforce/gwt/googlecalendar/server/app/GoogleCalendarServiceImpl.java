package com.edatasite.workforce.gwt.googlecalendar.server.app;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.social.zoom.ZoomService;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.ScheduledCourseManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.*;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.CalendarEventGuestCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.CalendarFilter;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.UsersCalendarSettingsItem;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.WorkforceEvents;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarService;
import com.edatasite.workforce.gwt.profile.server.app.RecurrenceService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.mail.EdsTemplates;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.services.calendar.model.Event;
import com.google.common.collect.Lists;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.RedirectRequiredException;
import com.google.gdata.util.ServiceException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service("googleCalendarService")
public class GoogleCalendarServiceImpl implements GoogleCalendarService, GoogleCalendarServiceLocal, SchedulerConstant, Constants {

    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarServiceImpl.class);

    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private HolidayManager holidayManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private RecurrenceJobManager recurrenceJobManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private RecurrenceService recurrenceService;
    @Autowired
    private GoogleCalendarReminderManager eventReminderManager;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private GoogleCalendarSettingsManager calendarSettingsManager;
    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    @Qualifier("allInOneService")
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private CountryManager countyManager;
    @Autowired
    private TicketManager ticketManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private TaskReminderManager taskReminderManager;
    @Autowired
    private BookingItemReservationManager reservationManager;
    @Autowired
    private LocationManager locationManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private GlobalAuthJdbcSpringManager jdbcSpringManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private WorkflowRuleManager workflowRuleManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private SelectedEmployeeFromCalendarManager calendarManager;
    @Autowired
    private UserSessionManager sessionManager;
    @Autowired
    private Office365CalendarService calendarService;
    @Autowired
    private TimeZoneManager timeZoneManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private ZoomService zoomService;
    @Autowired
    private ZoomMeetingManager zoomMeetingManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private ScheduledCourseManager scheduledCourseManager;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean validateCurrentUser() {
        return googleCalendarManager.validateUser(eventManager.getUser());
    }

    public String saveToken(String token) throws Exception {
        try {
            return googleCalendarManager.createCalendarDetails(token);
        } catch (GeneralSecurityException ex) {
            ex.printStackTrace();
            throw new GeneralSecurityException(ex.getMessage());
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            throw new AuthenticationException(ex.getMessage());
        } catch (ServiceException ex) {
            ex.printStackTrace();
            throw new ServiceException(ex);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new IOException(ex.getMessage());
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsUserSession getUserBySession(String sessionId) {
        return sessionManager.getUserSession(sessionId);
    }

    @Transactional
    public void createRecurringEvent() {
        ArrayList<EdsRecurrence> eventRecurrences = recurrenceManager.getFeaturedItemsRecurrences(RECURRING_EVENT);
        if (eventRecurrences != null && !eventRecurrences.isEmpty()) {
            for (EdsRecurrence recurrence : eventRecurrences) {
                ServerSecurityContext.getInstance().setCompanyId(recurrence.getCompanyID());
                ServerSecurityContext.getInstance().setDatabase(jdbcSpringManager.getCompanyClusterType(recurrence.getCompanyID()));
                try {
                    EdsEvent event = eventManager.get(recurrence.getBusObjectId());
                    EdsEmployee employee = event.getOwner().getEmployee();
                    if (!event.isDeleted() && employee != null && employee.getCompany().getActive() && !employee.getDeleted()) {
                        ServerSecurityContext.getInstance().setStaticUserID(employee.getObjectID());
                        HashSet<Integer> objectIDs = new HashSet<>();

                        EdsRecurrence tempRecurrence = recurrence.cloneShallow();
                        tempRecurrence.setStartDate(recurrence.getExtendDate());
                        tempRecurrence.setOccurrence(Integer.valueOf(tempRecurrence.getBusObjectParams()) + CREATE_EVENT_INDEX);
                        List<Date> recurringDates = recurrenceService.getRecurringDates(tempRecurrence);
                        EdsRecurrence edsRecurrence = recurrenceManager.getRecurrenceJob(CALENDAR_EVENT_REMINDER, event.getObjectID(), userManager.getUser().getCompany().getObjectID());
                        Integer reminderRecID = edsRecurrence != null ? edsRecurrence.getObjectID() : null;
                        createEventRecurringInstances(event, recurrence, recurringDates.subList(CREATE_EVENT_INDEX, recurringDates.size()), objectIDs, true, reminderRecID, true);

                        eventManager.addToSolr(objectIDs.toArray(new Integer[]{}));
                        ServerSecurityContext.getInstance().setStaticUserID(null);
                    }
                    ServerSecurityContext.getInstance().removeCompanyId();
                } catch (Exception e) {
                    e.printStackTrace();
                    ServerSecurityContext.getInstance().removeCompanyId();
                }
            }
        }
    }

    private Integer saveEventReminder(Appointment appointment, Integer eventID, Integer reminderRecurrenceID) {
        Integer recurrenceID = null;
        // remove existing reminders for this event
        eventReminderManager.deleteEventReminders(eventID);

        // remove reminders recurrences for this event from DB and RAM
        if (appointment.getReminder() != null && appointment.getReminder().size() > 0) {
            EdsEvent event = eventManager.get(eventID);
            if (event.getOwner() != null) {
                List<EdsRecurrence> edsRecurrence = recurrenceManager.getRecurrenceJobList(CALENDAR_EVENT_REMINDER, eventID, event.getOwner().getCompany().getObjectID());
                if (edsRecurrence != null && edsRecurrence.size() > 0) {
                    for (EdsRecurrence rec : edsRecurrence) {
                        recurrenceService.updateRecurrence(rec, true, true);
                    }
                }
            }

            for (int i = 0; i < appointment.getReminder().size(); i++) {
                if (reminderRecurrenceID == null) {
                    if (Integer.valueOf(1).equals(appointment.getReminder().get(i).getValue()) || Integer.valueOf(3).equals(appointment.getReminder().get(i).getValue()) || Integer.valueOf(5).equals(appointment.getReminder().get(i).getValue())) {
                        Date recStartDate = appointment.getStartDate();
                        recStartDate = DateUtil.addMinutes(recStartDate, (-1) * appointment.getReminder().get(i).getReminderTimes());
                        // creating new reminders recurrences
                        if (appointment.getRecurrenceJobItem() != null) {
                            RecurrenceJobItem recurrenceJobItem = appointment.getRecurrenceJobItem().clone();
                            recurrenceJobItem.setJobType(CALENDAR_EVENT_REMINDER);
                            recurrenceJobItem.setBusObjectParams(appointment.getReminder().get(i).getValue().toString());
                            recurrenceJobItem.setUserTimeZone(null);
                            recurrenceJobItem.setStartDate(recStartDate);
                            recurrenceID = recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                            EdsRecurrence recurrence = recurrenceManager.get(recurrenceID);
                            if (recurrence != null) {
                                List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
                                if (recurringDates != null && !recurringDates.isEmpty()) {
                                    recurrence.setEndDate(DateUtil.addMinutes(recurringDates.get(recurringDates.size() - 1), MINUTES));
                                    recurrenceManager.update(recurrence);
                                }
                            }
                        } else {
                            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
                            recurrenceJobItem.setEnabled(true);
                            recurrenceJobItem.setJobType(CALENDAR_EVENT_REMINDER);
                            recurrenceJobItem.setBusObjectParams(appointment.getReminder().get(i).getValue().toString());
                            recurrenceJobItem.setUserTimeZone(null);
                            recurrenceJobItem.setStartDate(recStartDate);
                            recurrenceJobItem.setEndDate(event.getEndDate());
                            recurrenceJobItem.setBusObjectId(event.getObjectID());
                            recurrenceJobItem.setType(RECURRENCE_TYPE_DAILY);
                            recurrenceJobItem.setEndType(END_BY_DATE);
                            recurrenceJobItem.setInterval(1);
                            recurrenceJobItem.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                            recurrenceID = recurrenceService.saveRecurrenceJob(recurrenceJobItem);
                        }
                    }
                }
                EdsGoogleCalendarReminder reminder = new EdsGoogleCalendarReminder();
                reminder.setEvent(event);
                reminder.setReminderType(appointment.getReminder().get(i).getValue());
                reminder.setMinutes(appointment.getReminder().get(i).getReminderTimes());
                eventReminderManager.create(reminder);
            }
        }
        return recurrenceID;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<CalendarEventReminder> getReminders(Integer objectID, boolean isEvent) {
        if (isEvent) {
            return eventReminderManager.getReminders(objectID);
        } else {
            return taskReminderManager.getReminders(objectID);
        }
    }

    public UsersCalendarSettingsItem saveCalendarSettings(UsersCalendarSettingsItem calendarSettingsItem) {
        calendarSettingsItem.setUserId(calendarSettingsManager.getUser().getObjectID());
        EdsGoogleCalendarSettings googleCalendarSettings = calendarSettingsManager.getUserCalendarSettings(calendarSettingsItem.getUserId());
        wrapUsersCalendarSettingsItemToEdsGoogleCalendarSettings(calendarSettingsItem, googleCalendarSettings);
        calendarSettingsManager.update(googleCalendarSettings);
        return wrapEdsGoogleCalendarSettingsToUsersCalendarSettingsItem(googleCalendarSettings);
    }

    @Override
    @Transactional
    public SelectItem saveCalendarEvent(Integer employeeId, Appointment appointment, boolean withNotify) {
        appointment.setSubject(appointment.getSubject().contains("undefined") ? appointment.getSubject().substring(0, ((appointment.getSubject().length() - 2) - "undefined".length())) : appointment.getSubject());
        if (employeeId == null && !appointment.isMissedCall()) {
            employeeId = employeeManager.getUser().getObjectID();
        }
        EdsEmployee employee = employeeManager.get(employeeId);
        SelectItem result = new SelectItem();
        EdsUser usersEmployee = employeeManager.getUser();
        if (appointment != null && appointment.isComplatedCall()) {
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(appointment.getStartDate());
            endDate.add(Calendar.SECOND, (int) appointment.getCallDuration());
            appointment.setEndDate(endDate.getTime());
        }
        if (employee != null && usersEmployee != null && !employee.getObjectID().equals(usersEmployee.getObjectID())) {
            IdTime[] idTimes = appointment.getProjectEmployees();
            ArrayList<IdTime> idTimeList = new ArrayList<>();
            if (idTimes != null) {
                Collections.addAll(idTimeList, idTimes);
            }
            idTimeList.add(new IdTime(employeeId, 0));
            appointment.setProjectEmployees(idTimeList.toArray(new IdTime[]{}));
        }
        List<EdsEmployeeEvent> employeeEvent;
        //SAVE EVENT
        employeeEvent = saveCalendarEvent(employee, appointment, false, withNotify, false, null, null);

        if (appointment.isClone()) {
            Appointment clone = appointment.clone();
            clone.setStartDate(appointment.getStartDateClone());
            clone.setEndDate(appointment.getEndDateClone());
            clone.setAllDay(appointment.isAllDayClone());
            employeeEvent = saveCalendarEvent(employee, clone, false, withNotify, false, null, null);
        }
        if (employeeEvent != null && !employeeEvent.isEmpty()) {
            result.setId(employeeEvent.get(0).getEvent().getObjectID());
            result.setName(employeeEvent.get(0).getEvent().getName());
            if (employeeEvent.get(0).getEvent().getOwner() != null) {
                result.setDescription(employeeEvent.get(0).getEvent().getOwner().getObjectID().toString());
            }
        }
        if (appointment.isRelationChanged()) {
            if (appointment.getRelations() != null && !appointment.getRelations().isEmpty()) {
                for (RelationItem relationItem : appointment.getRelations()) {
                    if (relationItem.getObjectID() == null) {
                        if (relationItem.getIDByType(RelationItem.TYPE_CANDIDATE) != null && appointment.getActivityType() == Appointment.INTERVIEW) {
                            EdsCrmContact candidate = crmContactManager.get(relationItem.getIDByType(RelationItem.TYPE_CANDIDATE));
                            if (candidate != null) {
                                candidate.setLeadStatus(referenceManager.findReference(EdsCrmContact._CANDIDATE_STATUS, ContactListItem.C_S_INTERVIEW));
                                crmContactManager.update(candidate, true);
                            }
                        } else if (relationItem.getIDByType(RelationItem.TYPE_LEAD) != null && appointment.getActivityType() != Appointment.INTERVIEW) {
                            EdsCrmContact lead = crmContactManager.get(relationItem.getIDByType(RelationItem.TYPE_LEAD));
                            if (lead != null) {
                                lead.clear();
                                crmContactManager.update(lead, true);
                                baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, lead, usersEmployee);
                                baseEventPostProcessor.registerEvent(CrmLeadEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, lead, usersEmployee);
                            }
                        }
                    }
                }
            }
            allInOneServiceLocal.saveRelations(RelationItem.TYPE_EVENT, result.getId(), result.getName(), appointment.getRelations());
        }
        if (employeeEvent != null && !employeeEvent.isEmpty()) {
            eventManager.addToSolr(employeeEvent.get(0).getEvent().getObjectID());
        }
        Date newDate = appointment.getStartDate() != null ? new Date(appointment.getStartDate().getTime()) : new Date();
        int scrollTime = usersEmployee.getUserDate(newDate).getHours() > 1 ? usersEmployee.getUserDate(newDate).getHours() - 1 : usersEmployee.getUserDate(newDate).getHours();
        usersEmployee.setCalendarScrollToHour(scrollTime);
        userManager.update(usersEmployee);
        return result;
    }

    /**
     * Saving or updating calendar event
     *
     * @param userID
     * @param appointment
     * @param forSync
     * @param withNotify  - notify event guests(if exist) when update or delete this event
     * @param objectIDs
     * @return
     */
    public ArrayList<Integer> saveCalendarEvent(Integer userID, Appointment appointment, boolean forSync, boolean withNotify, boolean isSolrIndex, HashSet<Integer> objectIDs, Integer reminderRecurrenceID) {
        EdsUser user = userManager.get(userID);
        List<EdsEmployeeEvent> employeeEvents = saveCalendarEvent(user, appointment, forSync, withNotify, isSolrIndex, objectIDs, reminderRecurrenceID);
        ArrayList<Integer> ids = new ArrayList<>();
        for (EdsEmployeeEvent employeeEvent : employeeEvents) {
            ids.add(employeeEvent.getObjectID());
        }
        return ids;
    }

    private List<EdsEmployeeEvent> saveCalendarEvent(EdsUser user, Appointment appointment, boolean forSync, boolean withNotify, boolean isSolrIndex, HashSet<Integer> objectIDs, Integer reminderRecurrenceID) {
        if (objectIDs == null) {
            objectIDs = new HashSet<>();
        }
        List<EdsEmployeeEvent> result = new ArrayList<>();
        Date timerStart = new Date();
        // appointment.getObjectID - gives us WFT Event ID
        boolean isNew = (appointment.getObjectID() == null || appointment.isCopy());
        boolean isRecurring = (appointment.getRecurrenceId() != null || (appointment.getRecurrenceJobItem() != null && appointment.getRecurrenceJobItem().isEnabled()));
        boolean isRecurringEdited = false;
        boolean isRecurringAdded = false;
        boolean isRecurringRemoved = (appointment.getRecurrenceId() != null && (appointment.getRecurrenceJobItem() == null));
        boolean isSeries = (isNew && isRecurring && appointment.getRecurrenceJobItem() == null);
        long startDateDiff = 0;
        long endDateDiff = 0;

        EdsEvent event = new EdsEvent();
        event.clear();
        event.setAsteriskid(appointment.getAsteriskid());
        event.setPhoneNumber(appointment.getPhoneNumber());
        if (!isNew) {
            event = eventManager.get(appointment.getObjectID());
            startDateDiff = appointment.getStartDate().getTime() - event.getStartDate().getTime();
            endDateDiff = appointment.getEndDate() != null && event.getEndDate() != null ? appointment.getEndDate().getTime() - event.getEndDate().getTime() : 0;
            if (event.getRecurrenceID() != null) {
                EdsRecurrence oldRecurrence = recurrenceManager.get(event.getRecurrenceID());
                EdsRecurrence newRecurrence = new EdsRecurrence();
                if (appointment.getRecurrenceJobItem() != null) {
                    if (oldRecurrence != null) {
                        recurrenceService.wrapRecurrenceJobItemToEdsRecurrence(appointment.getRecurrenceJobItem(), newRecurrence, recurrenceJobManager.get(RECURRING_EVENT));
                        String oldExpression = recurrenceManager.getCronExpression(oldRecurrence);
                        String newExpression = recurrenceManager.getCronExpression(newRecurrence);
                        if (!oldExpression.equals(newExpression) || recurrenceManager.getTriggerEndDate(oldRecurrence).getTime() != recurrenceManager.getTriggerEndDate(newRecurrence).getTime()) {
                            isRecurringEdited = true;
                        }
                    } else {
                        isRecurringAdded = true;
                    }
                }
            }
        }

        if (appointment.getCustomFieldItems() != null && !appointment.getCustomFieldItems().isEmpty()) {
            StringBuilder changesBuilder = new StringBuilder();
            for (CompanyCustomFieldItem cit : appointment.getCustomFieldItems()) {
                changesBuilder.append(event.getEventCustomFields() != null && CustomFieldsUtils.getObjectValue(event.getEventCustomFields(), cit.getColumnCode()) != null ? getChanges(CustomFieldsUtils.getObjectValue(event.getEventCustomFields(), cit.getColumnCode()), cit) : (cit.getColumnCode() + ","));
            }
            String changes = changesBuilder.toString();
            if (!"".equals(changes)) {
                event.addCustomFieldChanges(changes);
            }
        }
        //SAVE CUSTOM FIELDS
        event.setEventCustomFields(contactServiceLocal.saveCustomFields(event.getEventCustomFields(), appointment.getCustomFieldItems()));
        //T11875 change owner (createdby) if call from asterisk
        if (StringUtils.isNotBlank(appointment.getAsteriskid()) && appointment.getOwnerID() != null) {
            event.setOwner(employeeManager.get(appointment.getOwnerID()));
        }

        wrapAppointmentToEdsEvent(appointment, event, user != null ? user.getEmployee() : null, withNotify);
        if (isNew) {
            eventManager.create(event);
            if (appointment.getZoomObjectId() != null) {
                EdsZoomMeeting edsZoomMeeting = zoomMeetingManager.get(appointment.getZoomObjectId());
                edsZoomMeeting.setEventId(event);
                zoomMeetingManager.update(edsZoomMeeting);
            }
            baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, event, user);
            objectIDs.add(event.getObjectID());
            if (!isRecurring) {
                if (appointment.getAttachments() != null && appointment.getAttachments().length > 0) {
                    EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
                    if (eventFolder != null) {
                        attachmentUtilsManager.saveAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID(), appointment.getAttachments());
                    }
                }
                createOrUpdateEventGuests(appointment, event);
            }
            result = updateEventAssignees(event, appointment, user);

            if (isRecurring) {
                Integer recurrenceId = appointment.getRecurrenceId();
                if (recurrenceId == null) {
                    RecurrenceJobItem jobItem = appointment.getRecurrenceJobItem();
                    if (jobItem != null) {
                        jobItem.setBusObjectId(event.getObjectID());
                        jobItem.setJobType(RECURRING_EVENT);
                        recurrenceId = recurrenceService.saveRecurrenceJob(jobItem);
                    }
                }
                EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
                recurrence.setChanged(false);
                recurrenceManager.update(recurrence);
                event.setRecurrenceID(recurrence.getObjectID());
                if (appointment.getFireTime() != null) {
                    event.setFireTime(appointment.getFireTime());
                } else {
                    event.setFireTime(recurrence.getStartDate());
                }
                eventManager.update(event);
                baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                // If new Recurring event has been saved, populate future instances
                if (!isSeries) { // Important, might get in to recursive loop
                    List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
                    Appointment tempAppointment = appointment.clone();
                    if (recurringDates != null && !recurringDates.isEmpty()) {
                        Date endDate = recurringDates.get(recurringDates.size() - 1);
                        tempAppointment.setEndDate(endDate);
                    }
                    Integer reminderRecID = saveEventReminder(tempAppointment, event.getObjectID(), null);
                    createEventRecurringInstances(event, recurrence, recurringDates, objectIDs, false, reminderRecID, appointment.isRegisterNestedWorkflowEvents());
                } else {
                    saveEventReminder(appointment, event.getObjectID(), reminderRecurrenceID);
                }
            }
            if (event.getObjectID() != null) {
                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsEvent.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                kpiLog.setEntityId(event.getObjectID());
                ServerUtils.kpiLog(logger, kpiLog, "Add activity");
            }
        } else {
            isRecurringAdded = appointment.getRecurrenceId() == null && appointment.getRecurrenceJobItem() != null && appointment.getRecurrenceJobItem().isEnabled();
            if (isRecurring) {
                if (isRecurringRemoved) {
                    event.setRecurrenceID(null);
                    eventManager.update(event);
                    baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                    objectIDs.add(event.getObjectID());
                    updateEventAssignees(event, appointment, user);
                } else if (isRecurringEdited) {
                    if (Constants.EDIT_ALL_SERIES.equals(appointment.getAction())) {
                        editEventAllSeries(event, user, appointment, startDateDiff, objectIDs);
                    } else if (Constants.EDIT_ALL_FOLLOWING.equals(appointment.getAction())) {
                        EdsRecurrence edsRecurrence = recurrenceManager.getRecurrenceJob(CALENDAR_EVENT_REMINDER, event.getObjectID(), userManager.getUser().getCompany().getObjectID());
                        Integer reminderRecID = edsRecurrence != null ? edsRecurrence.getObjectID() : null;
                        deleteAllInstances(event, user.getObjectID(), true);
                        editRecurringEvent(event, user, appointment, objectIDs, reminderRecID);
                    } else if (Constants.EDIT_THIS_INSTANCE.equals(appointment.getAction())) {
                        eventManager.update(event);
                        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                        objectIDs.add(event.getObjectID());
                        updateEventAssignees(event, appointment, user);
                        createOrUpdateEventGuests(appointment, event);
                    }
                } else if (isRecurringAdded) {
                    Integer recurrenceId = appointment.getRecurrenceId();
                    RecurrenceJobItem jobItem = appointment.getRecurrenceJobItem();
                    if (jobItem != null) {
                        jobItem.setBusObjectId(event.getObjectID());
                        jobItem.setJobType(RECURRING_EVENT);
                        recurrenceId = recurrenceService.saveRecurrenceJob(jobItem);
                    }
                    EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
                    recurrence.setChanged(false);
                    recurrenceManager.update(recurrence);
                    event.setRecurrenceID(recurrence.getObjectID());
                    if (appointment.getFireTime() != null) {
                        event.setFireTime(appointment.getFireTime());
                    } else {
                        event.setFireTime(recurrence.getStartDate());
                    }
                    eventManager.update(event);
                    baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                    objectIDs.add(event.getObjectID());
                    // If new Recurring event has been saved, populate future instances
                    if (!isSeries) { // Important, might get in to recursive loop
                        createEventRecurringInstances(event, recurrence, objectIDs, reminderRecurrenceID);
                    }
                } else {
                    if (Constants.EDIT_THIS_INSTANCE.equals(appointment.getAction())) {
                        updateEventAssignees(event, appointment, user);
                        eventManager.update(event);
                        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                        objectIDs.add(event.getObjectID());
                    } else if (Constants.EDIT_ALL_SERIES.equals(appointment.getAction())) {
                        editEventAllSeries(event, user, appointment, startDateDiff, objectIDs);
                    } else if (Constants.EDIT_ALL_FOLLOWING.equals(appointment.getAction())) {
                        updateEventAssignees(event, appointment, user);
                        eventManager.update(event);
                        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                        objectIDs.add(event.getObjectID());
                        updateAllEventInstances(event, true, startDateDiff, endDateDiff, objectIDs);
                    }
                }
            } else {
                eventManager.update(event);
                baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
                objectIDs.add(event.getObjectID());
                result = updateEventAssignees(event, appointment, user);
                createOrUpdateEventGuests(appointment, event);
                if (appointment.getAttachments() != null && appointment.getAttachments().length > 0) {
                    EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
                    if (eventFolder != null) {
                        attachmentUtilsManager.saveAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID(), appointment.getAttachments());
                    }
                }
            }
            /**
             * @see GoogleCalendarServiceImpl#synchronizeEvents(Integer, java.util.Date, java.util.Date)
             */
            employeeEventManager.setEmployeeEventsModifiedDate(event, new Date());
            if (user != null) {
                logger.info("Updated event: " + event.getSubject() + "; eventID: " + event.getObjectID() + "; updated by: " + user.getObjectID() + "; companyID: " + user.getCompany().getObjectID() + "; date: " + new Date());
            } else {
                logger.info("Updated event: " + event.getSubject() + "; eventID: " + event.getObjectID() + "; updated by: " + null + "; companyID: " + null + "; date: " + new Date());
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsEvent.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(event.getObjectID());
            ServerUtils.kpiLog(logger, kpiLog, "Update activity");
        }
        appointment.setOwnerID(event.getOwner() != null ? event.getOwner().getObjectID() : null);

        // Save Event reminder
        if (!isSeries && !isRecurring) { // if this is not recurring event
            saveEventReminder(appointment, event.getObjectID(), null);
        }
        appointment.setObjectID(event.getObjectID());
        logger.info("Save Event Timer: " + ((new Date()).getTime() - timerStart.getTime()));
        if (!isSeries && !forSync) {
            if (!isSolrIndex) {
                if (result != null && !result.isEmpty()) {
                    objectIDs.remove(result.get(0).getEvent().getObjectID());
                }
            }
            eventManager.addToSolr(objectIDs.toArray(new Integer[]{}));
        }
        if (!isNew && zoomMeetingManager.getMeetingByEventId(event.getObjectID()) != null) {
            zoomService.updateMeeting(event);
        }
        event.setSendEmailNotification(appointment.isRegisterWorkFlowEventPerDate());
        EdsBusinessEvent workflowEvent = baseEventPostProcessor.registerEvent(WorkflowActionDetectedEventListenerImpl.TYPE, isNew ? BaseEventsPostProcessorImpl.EVENT_TYPE_ADD : (event.isDeleted() != null && event.isDeleted() ? BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE : BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT), event, user);
        workflowEvent.setEntityType(RelationItem.TYPE_EVENT);

        return result;
    }

    private String getChanges(Object ob, CompanyCustomFieldItem item) {
        if (ob != null) {
            if (DATA_TYPE_TEXT.equals(item.getDataType())) {
                String text = (String) ob;
                return !text.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_NUMBER.equals(item.getDataType())) {
                String s = String.valueOf(((Double) ob).intValue());
                return !s.equals(item.getFieldStringValue()) ? (item.getColumnCode() + ",") : "";
            } else if (DATA_TYPE_DATE.equals(item.getDataType())) {
                Date date = (Date) ob;
                return !date.equals(item.getFieldDateNonConvertedValue() != null ? item.getFieldDateNonConvertedValue().getNonConvertedDate() : null) ? (item.getColumnCode() + ",") : "";
            }
        }
        return "";
    }

    private void editEventAllSeries(EdsEvent event, EdsUser user, Appointment appointment, long startDateDiff, HashSet<Integer> objectIDs) {
        EdsEvent firstEvent = eventManager.getFirstOrLastEventInRecurringSeries(event.getRecurrenceID(), true);
        Date startDate = (Date) firstEvent.getStartDate().clone();
        if (!event.getObjectID().equals(firstEvent.getObjectID())) {
            startDate = new Date(startDate.getTime() + startDateDiff);
        }
        deleteAllInstances(firstEvent, user.getObjectID(), false);
        appointment.getRecurrenceJobItem().setStartDate(startDate);
        Integer recurrenceId = recurrenceService.saveRecurrenceJob(appointment.getRecurrenceJobItem());
        long timeDiff = appointment.getEndDate().getTime() - appointment.getStartDate().getTime();
        EdsRecurrence recurrence = recurrenceManager.get(recurrenceId);
        List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
        if (recurringDates != null && recurringDates.size() > 0) {
            appointment.setStartDate(recurringDates.get(0));
            appointment.setEndDate(new Date(recurringDates.get(0).getTime() + timeDiff));
            wrapAppointmentToEdsEvent(appointment, event, user.getEmployee(), false);
            event.setFireTime(recurrence.getStartDate());
            event.setRecurrenceID(recurrence.getObjectID());
            eventManager.create(event);
            baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, event, user);
            objectIDs.add(event.getObjectID());
            updateEventAssignees(event, appointment, user);
            createOrUpdateEventGuests(appointment, event);
            Integer reminderRecurrenceID = saveEventReminder(appointment, event.getObjectID(), null);
            if (recurringDates.size() > 1) {
                List<Date> recDates = recurringDates.subList(1, recurringDates.size());
                if (recDates.size() > 0) {
                    createEventRecurringInstances(event, recurrence, objectIDs, reminderRecurrenceID);
                }
            }
        }
    }

    private void editRecurringEvent(EdsEvent event, EdsUser user, Appointment appointment, HashSet<Integer> objectIDs, Integer reminderRecurrenceID) {
        EdsRecurrence recurrence = new EdsRecurrence();
        Integer recurrenceId = appointment.getRecurrenceId();
        if (appointment.getRecurrenceJobItem() != null) {
            appointment.getRecurrenceJobItem().setBusObjectId(event.getObjectID());
            appointment.getRecurrenceJobItem().setJobType(RECURRING_EVENT);
            recurrenceId = recurrenceService.saveRecurrenceJob(appointment.getRecurrenceJobItem());
        }
        if (recurrenceId != null) {
            recurrence = recurrenceManager.get(recurrenceId);
        }
        long timeDiff = appointment.getEndDate().getTime() - appointment.getStartDate().getTime();
        recurrence.setStartDate(appointment.getStartDate());
        List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
        if (recurringDates != null && recurringDates.size() > 0) {
            appointment.setStartDate(recurringDates.get(0));
            appointment.setEndDate(new Date(recurringDates.get(0).getTime() + timeDiff));
            wrapAppointmentToEdsEvent(appointment, event, user.getEmployee(), false);
            if (appointment.getFireTime() != null) {
                event.setFireTime(appointment.getFireTime());
            } else {
                event.setFireTime(recurrence.getStartDate());
            }
            event.setRecurrenceID(recurrence.getObjectID());
            eventManager.create(event);
            baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, event, user);
            objectIDs.add(event.getObjectID());
            updateEventAssignees(event, appointment, user);
            createOrUpdateEventGuests(appointment, event);
            saveEventReminder(appointment, event.getObjectID(), reminderRecurrenceID);
            if (recurringDates.size() > 1) {
                List<Date> recDates = recurringDates.subList(1, recurringDates.size());
                if (recDates.size() > 0) {
                    createEventRecurringInstances(event, recurrence, objectIDs, reminderRecurrenceID);
                }
            }
        }
    }

    /**
     * Will update assignees of EdsEvent based on new list given in transfer object of Appointment
     * <br><b>Note:</b> If attendees list is empty will include the owner or the current user as attendee
     *
     * @param event       - EdsEvent that is going to be updated to database
     * @param appointment - Transfer object containing updated list of assignees
     * @param owner
     */
    private List<EdsEmployeeEvent> updateEventAssignees(EdsEvent event, Appointment appointment, EdsUser owner) {
        List<EdsEmployeeEvent> result;
        ArrayList<Attendee> newAttendees = (ArrayList<Attendee>) appointment.getAttendees().clone();
        if (newAttendees.isEmpty() && owner != null) {
            Attendee attendee = new Attendee();
            attendee.setID(owner.getObjectID());
            attendee.setGoogleID(appointment.getGoogleID());
            attendee.setOfficeID(appointment.getOfficeID());
            attendee.setShared(true);
            newAttendees.add(attendee);
        }

        Appointment oldAppointment = wrapEdsEventToAppointment(event);
        System.err.println("updateEventAssignees for event: " + event.getObjectID());

        ArrayList<Attendee> oldAttendees = oldAppointment.getAttendees();


        //Must delete Old Attandees first because they will be recreated futher
        for (Attendee attendee : oldAttendees) {
            // Attendee removed from event
            EdsEmployee emp = employeeManager.get(attendee.getID());
            EdsEmployeeEvent employeeEvent = employeeEventManager.getEmployeeEvent(emp, event);
            if (employeeEvent != null) {
                employeeEvent.setDeleted(true);
                employeeEventManager.update(employeeEvent);
            }
        }

        result = employeeEventManager.getEmployeeEvents(event.getObjectID());

        final Date lastModifiedDate = new Date();
        for (Attendee attendee : newAttendees) {
            // New Attendee added to the event
            EdsEmployeeEvent employeeEvent = new EdsEmployeeEvent();
            employeeEvent.setEvent(event);
            try {
                employeeEvent.setEmployee(userManager.get(attendee.getID()));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            if (attendee.isShared()) {
                employeeEvent.setPermission(EdsEmployeeEvent.READ_WRITE);
            }
            employeeEvent.setShared(attendee.isShared());
            employeeEvent.setGoogleId(attendee.getGoogleID());
            employeeEvent.setOfficeID(attendee.getOfficeID());
            employeeEvent.setLastModifiedDate(attendee.getOfficeID() != null ? (appointment.getLastModifiedDate() != null ? appointment.getLastModifiedDate() : lastModifiedDate) : lastModifiedDate);
            employeeEventManager.create(employeeEvent);
            result.add(employeeEvent);
        }
        return result;
    }

    private void updateEventTickets(EdsEvent event, Appointment appointment) {
        ArrayList<TicketItem> newTickets = new ArrayList<>();
        if (appointment.getTickets() != null && appointment.getTickets().length > 0) {
            newTickets.addAll(Arrays.asList(appointment.getTickets()));
        }

        Appointment oldAppointment = wrapEdsEventToAppointment(event);
        ArrayList<TicketItem> oldTickets = oldAppointment.getTicketsAsList();

        ArrayList<TicketItem> sameTickets = (ArrayList<TicketItem>) ServerUtils.intersect(newTickets, oldTickets);

        //update existing tickets
        for (TicketItem eTicket : sameTickets) {
            EdsTicket ticket = new EdsTicket();
            if (eTicket.getObjectID() != null && ticketManager.get(eTicket.getObjectID()) != null) {
                ticket = ticketManager.get(eTicket.getObjectID());
            }
            ticket.setName(eTicket.getName());
            ticket.setQty(eTicket.getQty().intValue());
            ticket.setPrice(BigDecimal.valueOf(eTicket.getPrice()));
            ticket.setEvent(event);
            ticket.setMaxCount(eTicket.getMaxTikcets());
            ticket.setMinCount(eTicket.getMinTickets());
            ticket.setCurrencyId(eTicket.getCurrencyId());
            ticket.setFree(eTicket.isFree());
            ticket.setInFee(eTicket.isInFee());
            ticket.setTopFee(eTicket.isTopFee());
            ticket.setSalesStartDate(eTicket.getSalesStartDate());
            ticket.setSalesEndDate(eTicket.getSalesEndDate());
            ticket.setDescription(eTicket.getDescription());
            ticketManager.createOrUpdate(ticket);
        }

        //create new tickets
        for (TicketItem nTicket : newTickets) {
            EdsTicket ticket = new EdsTicket();
            ticket.setName(nTicket.getName());
            ticket.setQty(nTicket.getQty().intValue());
            ticket.setPrice(BigDecimal.valueOf(nTicket.getPrice()));
            ticket.setEvent(event);
            ticket.setMaxCount(nTicket.getMaxTikcets());
            ticket.setMinCount(nTicket.getMinTickets());
            ticket.setCurrencyId(nTicket.getCurrencyId());
            ticket.setFree(nTicket.isFree());
            ticket.setInFee(nTicket.isInFee());
            ticket.setTopFee(nTicket.isTopFee());
            ticket.setSalesStartDate(nTicket.getSalesStartDate());
            ticket.setSalesEndDate(nTicket.getSalesEndDate());
            ticket.setDescription(nTicket.getDescription());
            ticketManager.create(ticket);
        }

        //delete old tickes
        if (oldTickets != null && oldTickets.size() > 0) {
            for (TicketItem oTicket : oldTickets) {
                ticketManager.deleteTicket(oTicket.getObjectID());
            }
        }

    }

    /**
     * Saving calendar task
     *
     * @param appointment
     * @return SelectItem object with task properties
     */
    public SelectItem saveCalendarTask(Appointment appointment) {
        TaskSingleItem taskSingleItem = wrapAppointmentToTaskSingleItem(appointment);
        return saveCalendarTask(taskSingleItem);
    }

    public SelectItem saveCalendarTask(TaskSingleItem newTask) {
        SelectItem result = new SelectItem();

        try {
            if (newTask.getObjectID() == null) {
                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                boolean isUniqueNumber = false;
                if (settings != null && settings.getTaskNumberingFormat() != null) {
                    isUniqueNumber = settings.isUniqueNumber(settings.getTaskNumberingFormat(), WIDGET_DATE_YEAR, WIDGET_UNIQUE_NUMBER_ALL_PROJECT);
                }
                Integer intNumber = taskManager.getProjectTasksLastIntNumber(newTask.getProjectID(), isUniqueNumber);
                String pojectNumber = "";
                Integer pojectClientCode = null;
                if (newTask.getProjectID() != null) {
                    Object[] tt = projectManager.getProjectNumberById(newTask.getProjectID()).get(0);
                    pojectNumber = tt[0] != null ? tt[0].toString() : "";
                    pojectClientCode = tt[1] != null ? (Integer) tt[1] : null;
                }
                String clientCode = null;
                if (pojectClientCode != null) {
                    clientCode = crmAccountManager.getCrmAccountNumberById(pojectClientCode).get(0);
                }
                NumberData taskNumber = null;
                if (settings != null && settings.getTaskNumberingFormat() != null) {
                    taskNumber = settings.parseNumberDataForALL(intNumber, settings.getTaskNumberingFormat(), settings.getDelimetrTask(), null, clientCode, pojectNumber, "task");
                } else {
                    taskNumber = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_TASK_PREFIX/*false*/);
                }
                newTask.setNumberData(taskNumber);
            }
            Integer[] saveResult = taskService.saveTask(newTask);
            String companyID = ServerSecurityContext.getInstance().getCompanyId();
            String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task|summary/" + saveResult[1].toString())) + "&cid=" + EncryptionHelper.encryptURL(companyID);
            result.setId(saveResult[1]);
            result.setDescription(saveResult[2].toString() + "#" + link);// this is a creator for a task this will be casted to integer in UI
            result.setName(saveResult[3].toString());
            EdsUser user = userManager.getUser();
            if (user != null) {
                Date newDate = new Date(newTask.getStartDate().getTime());
                int scrollTime = user.getUserDate(newDate).getHours() > 1 ? user.getUserDate(newDate).getHours() - 1 : user.getUserDate(newDate).getHours();
                user.setCalendarScrollToHour(scrollTime);
                userManager.update(user);
            }
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public ArrayList<SelectItem> getTimeZones() {
        List<EdsTimeZone> timeZones = timeZoneManager.getMicrosoftTimeZones();
        ArrayList<SelectItem> list = new ArrayList<>();
        if (timeZones != null && !timeZones.isEmpty()) {
            for (EdsTimeZone timeZone : timeZones) {
                SelectItem item = new SelectItem();
                item.setId(timeZone.getObjectID());
                item.setName(timeZone.getName());
                list.add(item);
            }
            return list;
        }
        return null;
    }

    @Override
    public Boolean saveOfficeCalendarTimeZone(Integer timeZoneID) {
        if (timeZoneID != null) {
            EdsUser user = userManager.getUser();
            EdsGoogleCalendar calendar = googleCalendarManager.getOfficeCalendar(user, false);
            EdsTimeZone timeZone = timeZoneManager.get(timeZoneID);
            if (calendar != null && timeZone != null) {
                calendar.setCalendarTimeZone(timeZone);
                googleCalendarManager.update(calendar);
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getSelectedTimeZone() {
        EdsUser user = userManager.getUser();
        EdsGoogleCalendar calendar = googleCalendarManager.getOfficeCalendar(user, false);
        if (calendar != null && calendar.getCalendarTimeZone() != null) {
            return calendar.getCalendarTimeZone().getObjectID();
        }
        return null;
    }

    @Override
    public SelectItem saveCalendarTask(TaskSingleItem newTask, Integer userID) {
        SelectItem result = new SelectItem();
        try {
            if (newTask.getObjectID() == null) {
                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                boolean isUniqueNumber = false;
                if (settings != null && settings.getTaskNumberingFormat() != null) {
                    isUniqueNumber = settings.isUniqueNumber(settings.getTaskNumberingFormat(), WIDGET_DATE_YEAR, WIDGET_UNIQUE_NUMBER_ALL_PROJECT);
                }
                Integer intNumber = taskManager.getProjectTasksLastIntNumber(newTask.getProjectID(), isUniqueNumber);
                String pojectNumber = "";
                Integer pojectClientCode = null;
                if (newTask.getProjectID() != null) {
                    Object[] tt = projectManager.getProjectNumberById(newTask.getProjectID()).get(0);
                    pojectNumber = tt[0] != null ? tt[0].toString() : "";
                    pojectClientCode = tt[1] != null ? (Integer) tt[1] : null;
                }
                String clientCode = null;
                if (pojectClientCode != null) {
                    clientCode = crmAccountManager.getCrmAccountNumberById(pojectClientCode).get(0);
                }
                NumberData taskNumber = null;
                if (settings != null && settings.getTaskNumberingFormat() != null) {
                    taskNumber = settings.parseNumberDataForALL(intNumber, settings.getTaskNumberingFormat(), settings.getDelimetrTask(), null, clientCode, pojectNumber, "task");
                } else {
                    taskNumber = EdsNumberingSettings.getDefaultData(intNumber, EdsNumberingSettings.DEF_TASK_PREFIX/*false*/);
                }
                newTask.setNumberData(taskNumber);
            }
            Integer[] saveResult = new Integer[0];

            saveResult = taskService.saveTask(newTask, userID);

            String companyID = ServerSecurityContext.getInstance().getCompanyId();
            String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task|summary/" + saveResult[1].toString())) + "&cid=" + EncryptionHelper.encryptURL(companyID);
            result.setId(saveResult[1]);
            result.setDescription(saveResult[2].toString() + "#" + link);// this is a creator for a task this will be casted to integer in UI
            result.setName(saveResult[3].toString());
        } catch (NumberExistingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private TaskSingleItem wrapAppointmentToTaskSingleItem(Appointment appointment) {
        EdsUser user = userManager.getUser();
        TaskSingleItem taskSingleItem = new TaskSingleItem();
        taskSingleItem.setName(appointment.getSubject());
        taskSingleItem.setDescription(appointment.getDescription());
        taskSingleItem.setObjectID(appointment.getObjectID());
        taskSingleItem.setProjectName(appointment.getLocation());
        if (appointment.getProjectID() != null) {
            taskSingleItem.setProjectID(appointment.getProjectID());
        }
        if (appointment.getPriorityID() != null) {
            taskSingleItem.setPriorityID(appointment.getPriorityID());
        }
        if (appointment.getRecurrenceJobItem() != null) {
            taskSingleItem.setRecurrenceJobItem(appointment.getRecurrenceJobItem());
        }
        taskSingleItem.setRecurrenceId(appointment.getRecurrenceId());
        if (appointment.isAllDay() && user != null) {
            taskSingleItem.setStartDate(appointment.getStartDate());
            taskSingleItem.setDueDate(appointment.getEndDate());

            Date userStartDate = user.getUserDate(appointment.getStartDate());
            Date userEndDate = user.getUserDate(appointment.getEndDate());

            taskSingleItem.setTimeZoneOffset(user.getUserTimezone().getRawOffset());
            int dayCount = ServerUtils.getDayCountInCalendar(userStartDate, userEndDate);
            taskSingleItem.setDayCount(dayCount != 0 ? dayCount : 1);
        } else {
            taskSingleItem.setStartDate(appointment.getStartDate());
            taskSingleItem.setDueDate(appointment.getEndDate());
        }
        taskSingleItem.setAction(appointment.getAction());
        taskSingleItem.setAllDay(appointment.isAllDay());
        taskSingleItem.setInstancesCount(10);
        taskSingleItem.setReminder(appointment.getReminder());
        if (appointment.getProjectEmployees() != null && appointment.getProjectEmployees().length > 0) {
            taskSingleItem.setProjectEmployees(appointment.getProjectEmployees());
        }
        return taskSingleItem;
    }

    private void shareEvent(Integer employeeID, ArrayList<Attendee> attendees, int eventID, boolean onlyShare) {
        ArrayList<EdsUser> users = getRealEmployees(attendees);
        EdsEvent event = eventManager.get(eventID);

        for (EdsUser user : users) {
            EdsEmployeeEvent employeeEvent = employeeEventManager.getEmployeeEvent(user, event);
            if (employeeEvent == null) {
                googleCalendarManager.createEmployeeEvent(user, event, users, onlyShare);
            } else {
                if (user.isShared()) {
                    employeeEvent.setPermission(EdsEmployeeEvent.READ_WRITE);
                }
            }
        }

        //It saves employees in workforce and if it has google token then also in google.
        EdsUser employee = employeeManager.get(employeeID);

        try {
            messageManager.sendCalendarShareEventNotification(employeeEventManager.getEmployeeEvent(employee, event), users, onlyShare);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<EdsUser> getRealEmployees(ArrayList<Attendee> attendees) {
        ArrayList<EdsUser> employees = new ArrayList<>();

        for (Attendee attendee : attendees) {
            if (attendee.getID() != null) {
                EdsEmployee employee = employeeManager.get(attendee.getID());
                if (employee != null) {
                    employee.setShared(attendee.isShared());
                    employees.add(employee);
                }
            }
        }

        return employees;
    }

    /**
     * Returns the list of conflicted employees.
     *
     * @param eventID
     * @return
     */
    public ArrayList<SelectItem> shareEventToEmployees(int eventID, ArrayList<Attendee> attendees, boolean forceToShare) {
        ArrayList<SelectItem> conflictedEmployees = new ArrayList<>();
        if (!forceToShare) {
            EdsEvent event = eventManager.get(eventID);
            conflictedEmployees = getConflictedEmployees(attendees, event.getStartDate(), event.getEndDate(), eventID);
        }

        if (forceToShare || conflictedEmployees.size() == 0)//If there are no conflicted employees.
        {
            shareEvent(employeeManager.getUser().getObjectID(), attendees, eventID, true);
        }

        return conflictedEmployees;
    }

    @Override
    public WorkforceEvents getWorkforceTrackEvents(Date startDate) {
        WorkforceEvents events = new WorkforceEvents();
        EdsUser user = userManager.getUser();
        EdsGoogleCalendarSettings calendarSettings = calendarSettingsManager.getUserCalendarSettings(user.getObjectID());
        UsersCalendarSettingsItem settingsItem = wrapEdsGoogleCalendarSettingsToUsersCalendarSettingsItem(calendarSettings);
        boolean isShowConsolidateCalendar = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED);
        if (isShowConsolidateCalendar) {
            List<EdsSelectedEmployeeFromCalendar> employeeFromCalendars = calendarManager.getByUser(userManager.getUser());
            if (employeeFromCalendars != null && !employeeFromCalendars.isEmpty()) {
                ArrayList<SelectItem> selectItems = new ArrayList<>();
                for (EdsSelectedEmployeeFromCalendar selectedEmployeeFromCalendar : employeeFromCalendars) {
                    SelectItem item = new SelectItem();
                    item.setId(selectedEmployeeFromCalendar.getSelectedUser().getObjectID());
                    item.setName(selectedEmployeeFromCalendar.getSelectedUser().getFullName());
                    EdsEmployeeDepartment employeeDepartment = employeeDepartmentManager.getByEmployeeId(selectedEmployeeFromCalendar.getSelectedUser().getObjectID());
                    if (employeeDepartment != null && employeeDepartment.getEmployeeTaskColor() != null) {
                        item.setDescription("#" + employeeDepartment.getEmployeeTaskColor());
                    } else {
                        item.setDescription("#4cb052");
                    }
                    selectItems.add(item);
                }
                events.setSelectedUsers(selectItems);
            }
        }
        events.setCalendarSettings(settingsItem);
        /**
         * Below we are getting working hours of current employee.
         */
        for (EdsTimeSlotItem item : user.getEmployee().getTimeSlot().getItems()) {
            if (item.getDay() == 1) {//getDay == 1 means the first day of the week which is Monday.
                //In order to get time with hours we should divide this to 60.
                events.setWorkingHourStart(item.getStartTime() / 60);
                events.setWorkingHourEnd(item.getEndTime() / 60);
                break;
            }
        }
        if (user.getCalendarScrollToHour() != null && user.getCalendarScrollToHour() != 0) {
            int hour = user.getCalendarScrollToHour();
            events.setScrollToHour(hour);
            user.setCalendarScrollToHour(0);
            userManager.update(user);

        }
        final java.util.Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        startCalendar.add(java.util.Calendar.DATE, -7);

        final java.util.Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(startDate);
        endCalendar.add(java.util.Calendar.DATE, 14);
        endCalendar.add(java.util.Calendar.MONTH, 1);
        events.setStartDate(startCalendar.getTime());
        events.setEndDate(endCalendar.getTime());

        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsGoogleCalendar.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.VIEW);
        ServerUtils.kpiLog(logger, kpiLog, "View CRM Calendar");
        return events;
    }


    @Transactional
    public WorkforceEvents getWorkforceTrackEventsForPDF(Date start, Date end, boolean forPDF) {
        EdsUser employee = employeeManager.getUser();
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        employeeIDs.add(employee.getObjectID());
        return getWorkforceTrackEventsInternal(employeeIDs, start, end, forPDF);
    }

    /**
     * Getting Calendar event's
     *
     * @param employeeIDs
     * @param start
     * @param end
     * @param forPDF
     * @return
     */
    @Transactional
    public WorkforceEvents getWorkforceTrackEventsInternal(ArrayList<Integer> employeeIDs, Date start, Date end, boolean forPDF) {
        WorkforceEvents events = new WorkforceEvents();

        ArrayList<Appointment> projectEvents = new ArrayList<>();
        ArrayList<Appointment> issueEvents = new ArrayList<>();
        ArrayList<Appointment> leaveRequestEvents = new ArrayList<>();
        ArrayList<Appointment> holidayEvents = new ArrayList<>();

        EdsUser user = userManager.getUser();
        EdsGoogleCalendarSettings calendarSettings = calendarSettingsManager.getUserCalendarSettings(user.getObjectID());
        UsersCalendarSettingsItem settingsItem = wrapEdsGoogleCalendarSettingsToUsersCalendarSettingsItem(calendarSettings);
        events.setCalendarSettings(settingsItem);

        if (settingsItem != null && settingsItem.isEventIsChecked() != null && settingsItem.isEventIsChecked()) {
            CalendarFilter filter = new CalendarFilter();
            filter.setEmployeeIDs(employeeIDs);
            filter.setStart(start);
            filter.setEnd(end);
            filter.setFromAgenda(false);
            filter.setVisible(settingsItem.isEventIsChecked());
            filter.setForPDF(forPDF);
            filter.setCall(false);
            ArrayList<Appointment> eventsList = getCalendarEvents(filter);
            // this need for drawing calendar UI; when eventsList is null or is empty, then in UI side variable assigned to false value;
            // this affected while you check/uncheck appointment's checkBox
            if (eventsList != null && !eventsList.isEmpty()) {
                events.setEvents(eventsList);
            } else {
                events.setEvents(null);
            }
        }
        if (settingsItem != null && settingsItem.isCallIsChecked() != null && settingsItem.isCallIsChecked()) {
            CalendarFilter filter = new CalendarFilter();
            filter.setEmployeeIDs(employeeIDs);
            filter.setStart(start);
            filter.setEnd(end);
            filter.setFromAgenda(false);
            filter.setVisible(settingsItem.isCallIsChecked());
            filter.setForPDF(forPDF);
            filter.setCall(true);
            ArrayList<Appointment> eventsList = getCalendarEvents(filter);
            // this need for drawing calendar UI; when eventsList is null or is empty, then in UI side variable assigned to false value;
            // this affected while you check/uncheck appointment's checkBox
            if (eventsList != null && !eventsList.isEmpty()) {
                events.setCalls(eventsList);
            } else {
                events.setCalls(null);
            }
        }

//        //User related projects
        if (!(user.getCompany().getObjectID() == 25608)) { // 25608 robert companyID
            if (settingsItem != null && settingsItem.isProjectIsChecked() != null && settingsItem.isProjectIsChecked()) {
                List<EdsProject> projects = projectManager.getCalendarProjects(employeeIDs, start, end);
                for (EdsProject project : projects) {
                    Appointment appointment = new Appointment();
                    appointment.setObjectID(project.getObjectID());
                    appointment.setSubject(project.getName());
                    appointment.setStartDate(new Date(project.getStartDate().getTime()));
                    appointment.setEndDate(new Date(project.getDueDate().getTime()));
                    appointment.setDescription(project.getDescription());
                    appointment.setCreatedBy(project.getCreator() != null ? project.getCreator().getFullName() : "N/A");
                    appointment.setAllDay(appointment.isAllDay());
                    appointment.setMultiDay(appointment.isMultiDayAppointment());
                    appointment.setStyle(Appointment.ORANGE);
                    appointment.setVisible(settingsItem.isProjectIsChecked());
                    appointment.setEditable(false);
                    appointment.setNumberData(project.getNumber());
                    String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("project|summary/" + project.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
                    appointment.setLinkURL(link);

                    projectEvents.add(appointment);
                }
                if (!projectEvents.isEmpty()) {
                    events.setProjects(projectEvents);
                } else {
                    events.setProjects(null);
                }
            }

            //User related tasks
            if (settingsItem != null && settingsItem.isTaskIsChecked() != null && settingsItem.isTaskIsChecked()) {
                ArrayList<Appointment> tasksList = getCalendarTasks(employeeIDs, start, end, false, settingsItem.isTaskIsChecked(), forPDF);
                if (tasksList != null && !tasksList.isEmpty()) {
                    events.setTasks(tasksList);
                } else {
                    events.setTasks(null);
                }
            }

            //User related issues
            if (settingsItem != null && settingsItem.isIssueIsChecked() != null && settingsItem.isIssueIsChecked()) {
                ListingFilterParameter filterParams = new ListingFilterParameter();
                filterParams.setStartDate(start);
                filterParams.setEndDate(end);
                filterParams.setPlannedStart(true);
                List<EdsIssue> issues = issueManager.list(filterParams);
                for (EdsIssue issue : issues) {
                    Appointment appointment = new Appointment();
                    appointment.setSubject(issue.getName());
                    appointment.setStartDate(new Date(issue.getStartDate().getTime()));
                    appointment.setEndDate(new Date(issue.getDueDate().getTime()));
                    appointment.setDescription(issue.getDescription());
                    appointment.setCreatedBy(issue.getCreator() != null ? issue.getCreator().getFullName() : "N/A");
                    appointment.setAllDay(appointment.isAllDay());
                    appointment.setMultiDay(appointment.isMultiDayAppointment());
                    appointment.setStyle(Appointment.PURPLE);
                    appointment.setVisible(settingsItem.isIssueIsChecked());
                    appointment.setEditable(false);
                    String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("issue|summary/" + issue.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
                    appointment.setLinkURL(link);
                    issueEvents.add(appointment);
                }
                if (!issueEvents.isEmpty()) {
                    events.setIssues(issueEvents);
                } else {
                    events.setIssues(null);
                }
            }

        }

        //User related sick requests
        if (settingsItem != null && settingsItem.isLeaveRequestIsChecked() != null && settingsItem.isLeaveRequestIsChecked()) {
            List<EdsSickRequest> requests = sickRequestManager.getCalendarSickRequests(employeeIDs, start, end);
            for (EdsSickRequest request : requests) {
                Appointment appointment = new Appointment();
                appointment.setSubject(request.getDescription());
                TimeZone timeZone = user.getUserTimezone();
                Date sDate = (Date) request.getStartDate().clone();
                Date newStart = new Date(sDate.getYear(), sDate.getMonth(), sDate.getDate(), sDate.getHours(), sDate.getMinutes() - (timeZone.getRawOffset() / 60000), sDate.getSeconds());
                appointment.setStartDate(newStart);
                Date eDate = (Date) request.getEndDate().clone();
                Date newEnd = new Date(eDate.getYear(), eDate.getMonth(), eDate.getDate(), eDate.getHours(), eDate.getMinutes() - (timeZone.getRawOffset() / 60000), eDate.getSeconds());
                appointment.setEndDate(newEnd);
                appointment.setDescription(request.getDescription());
                appointment.setCreatedBy(request.getRegisteredBy() != null ? request.getRegisteredBy().getFullName() : "N/A");
                appointment.setAllDay(appointment.isAllDay());
                appointment.setMultiDay(appointment.isMultiDayAppointment());
                appointment.setStyle(Appointment.RED);
                appointment.setVisible(settingsItem.isLeaveRequestIsChecked());
                appointment.setEditable(false);
                leaveRequestEvents.add(appointment);
            }
            if (!leaveRequestEvents.isEmpty()) {
                events.setLeaveRequests(leaveRequestEvents);
            } else {
                events.setLeaveRequests(null);
            }
        }

        //Country yearly holidays
        if (settingsItem != null && settingsItem.isHolidayIsChecked() != null && settingsItem.isHolidayIsChecked()) {
            EdsLocation location = user.getLocation();
            List<EdsHoliday> holidays = holidayManager.getCalendarHolidays(location, start, end);
            EdsCompany company = employeeManager.getUser().getCompany();
            for (EdsHoliday holiday : holidays) {
                Appointment appointment = new Appointment();
                appointment.setSubject(holiday.getName());
                appointment.setStartDate(new Date(holiday.getStartDate().getTime() - user.getUserTimezone().getRawOffset()));
                appointment.setEndDate(new Date(holiday.getEndDate().getTime() - user.getUserTimezone().getRawOffset()));
                appointment.setDescription(holiday.getDescription());
                appointment.setCreatedBy(company.getName());
                appointment.setAllDay(appointment.isAllDay());
                appointment.setMultiDay(appointment.isMultiDayAppointment());
                appointment.setStyle(Appointment.PINK);
                appointment.setVisible(settingsItem.isHolidayIsChecked());
                appointment.setEditable(false);
                if (location != null) {
                    String locationName = location.getCountry().getName() + "," + (location.getState() != null ? (location.getState().getName() + ",") : "") + location.getCity();
                    appointment.setLocation(locationName);
                }
                holidayEvents.add(appointment);
            }
            if (!holidayEvents.isEmpty()) {
                events.setHolidays(holidayEvents);
            } else {
                events.setHolidays(null);
            }
        }

        /**
         * Below we are getting working hours of current employee.
         */
        for (EdsTimeSlotItem item : user.getEmployee().getTimeSlot().getItems()) {
            if (item.getDay() == 1) {//getDay == 1 means the first day of the week which is Monday.
                //In order to get time with hours we should divide this to 60.
                events.setWorkingHourStart(item.getStartTime() / 60);
                events.setWorkingHourEnd(item.getEndTime() / 60);
                break;
            }
        }

        return events;
    }

    private Date getMonthLastTime(Date end) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(end);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    private UsersCalendarSettingsItem wrapEdsGoogleCalendarSettingsToUsersCalendarSettingsItem(EdsGoogleCalendarSettings settings) {
        UsersCalendarSettingsItem settingsItem = null;
        if (settings != null) {
            settingsItem = new UsersCalendarSettingsItem();
            settingsItem.setUserId(settings.getCalendarOwner().getObjectID());
            settingsItem.setEventIsChecked(settings.isEventIsChecked());
            settingsItem.setCallIsChecked(settings.isCallIsChecked());
            settingsItem.setProjectIsChecked(settings.isProjectIsChecked());
            settingsItem.setTaskIsChecked(settings.isTaskIsChecked());
            settingsItem.setIssueIsChecked(settings.isIssueIsChecked());
            settingsItem.setLeaveRequestIsChecked(settings.isLeaveRequestIsChecked());
            settingsItem.setPaIsChecked(settings.isPaIsChecked());
            settingsItem.setHolidayIsChecked(settings.isHolidayIsChecked());
            settingsItem.setCourseIsChecked(settings.isCourseIsChecked());
            settingsItem.setDefaultView(settings.getDefaultView());
        }
        return settingsItem;
    }

    private void wrapUsersCalendarSettingsItemToEdsGoogleCalendarSettings(UsersCalendarSettingsItem settingsItem, EdsGoogleCalendarSettings calendarSettings) {
        calendarSettings.setCalendarOwner(userManager.get(settingsItem.getUserId()));
        calendarSettings.setEventIsChecked(settingsItem.isEventIsChecked());
        calendarSettings.setCallIsChecked(settingsItem.isCallIsChecked());
        calendarSettings.setProjectIsChecked(settingsItem.isProjectIsChecked());
        calendarSettings.setTaskIsChecked(settingsItem.isTaskIsChecked());
        calendarSettings.setIssueIsChecked(settingsItem.isIssueIsChecked());
        calendarSettings.setLeaveRequestIsChecked(settingsItem.isLeaveRequestIsChecked());
        calendarSettings.setPaIsChecked(settingsItem.isPaIsChecked());
        calendarSettings.setHolidayIsChecked(settingsItem.isHolidayIsChecked());
        calendarSettings.setCourseIsChecked(settingsItem.isCourseIsChecked());
        calendarSettings.setDefaultView(settingsItem.getDefaultView());
    }

    @Override
    public ArrayList<Appointment> getCalendarTasks(ArrayList<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, boolean visible, boolean forPDF) {
        Date monthLastDate = DateUtil.getMonthLastDate((Date) end.clone());
        monthLastDate = getMonthLastTime(monthLastDate);

        EdsUser user = taskManager.getUser();
        boolean isShowConsolidateCalendar = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CONSOLIDATED_CALENDAR_ENABLED);
        if (isShowConsolidateCalendar && (employeeIDs == null || employeeIDs.isEmpty()) || employeeIDs.contains(user.getObjectID())) {
            if (employeeIDs == null) {
                employeeIDs = new ArrayList<>();
            }
            employeeIDs.clear();
            List<EdsSelectedEmployeeFromCalendar> employeeFromCalendars = calendarManager.getByUser(user);
            if (employeeFromCalendars != null && !employeeFromCalendars.isEmpty()) {
                for (EdsSelectedEmployeeFromCalendar selectedEmployeeFromCalendar : employeeFromCalendars) {
                    employeeIDs.add(selectedEmployeeFromCalendar.getSelectedUser().getObjectID());
                }
            }
            if (employeeIDs.size() == 0) {
                employeeIDs.add(user.getObjectID());
            }
        }

        List<EdsEmployeeTask> employeeTasks = taskManager.getUserTasks(employeeIDs, start, forPDF ? end : monthLastDate, fromAgenda);
        ArrayList<Appointment> appointments = new ArrayList<>();

        Map<Integer, ArrayList<Date>> isHasTasksEmployee = new HashMap<>();
        for (EdsEmployeeTask employeeTask : employeeTasks) {
            EdsTask task = employeeTask.getTask();
            Appointment appointment = null;
            String taskStrings = task.getObjectID() + "_" + task.getStartDate().toString();
            if (!employeeTask.getTask().isAllDay()) {
                if (isHasTasksEmployee.containsKey(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                    isHasTasksEmployee.get(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID()).add(user.getUserDate(employeeTask.getTask().getStartDate()));
                } else {
                    ArrayList<Date> dates = new ArrayList<>();
                    dates.add(user.getUserDate(employeeTask.getTask().getStartDate()));
                    isHasTasksEmployee.put(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), dates);
                }
            }
            if (fromAgenda) {
                appointment = wrapEdsTaskToAppointmentShort(employeeTask);

            } else {
                appointment = wrapEdsTaskToAppointment(employeeTask, visible);
                if (employeeIDs.contains(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                    if (employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployeeTaskColor() != null) {
                        appointment.setStyle("#" + employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployeeTaskColor());
                        appointment.setBorder("#" + employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployeeTaskColor());
                    }
                }
                appointment.setVisible(visible);
            }
            String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task|summary/" + task.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            if (appointment != null) {
                appointment.setLinkURL(link);
                appointment.setIsTask(true);
                appointments.add(appointment);
            }
        }
        //Create No Task appointments
        if (isShowConsolidateCalendar) {
            int daysCount = DateUtil.countDays(start, end);
            for (Integer id : employeeIDs) {
                EdsEmployee employee = employeeManager.get(id);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(start);
                for (int i = 1; i < daysCount; i++) {
                    calendar.add(Calendar.DATE, i == 1 ? 0 : 1);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    if ((!isHasTasksEmployee.containsKey(id) || !isOneDay(calendar.getTime(), isHasTasksEmployee.get(id))) && !fromAgenda) {
                        Appointment appointment = new Appointment();
                        appointment.setEditable(false);
                        appointment.setNoTask(true);
                        appointment.setIsTask(true);
                        appointment.setAllDay(false);
                        appointment.setSubject("No Task");
                        appointment.setDescription("No task");
                        appointment.setStartDate(user.getServerDateByUserDate(calendar.getTime()));
                        calendar.set(Calendar.HOUR, 23);
                        appointment.setEndDate(user.getServerDateByUserDate(calendar.getTime()));

                        if (employee.getEmployeeTeam().getEmployeeTaskColor() != null) {
                            appointment.setBorder("#" + employee.getEmployeeTeam().getEmployeeTaskColor());
                        }
                        appointments.add(appointment);
                    }
                }
            }
        }
        return appointments;
    }

    private boolean isOneDay(Date time, ArrayList<Date> dates) {
        for (Date date : dates) {
            if (DateUtil.countDays(time, date) == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<Appointment> getCalendarIssues(Date start, Date end) {
        ArrayList<Appointment> issueEvents = new ArrayList<>();
        EdsUser user = userManager.getUser();
        ListingFilterParameter filterParams = new ListingFilterParameter();
        filterParams.setStartDate(start);
        filterParams.setEndDate(end);
        filterParams.setPlannedStart(true);
        List<EdsIssue> issues = issueManager.list(filterParams);
        for (EdsIssue issue : issues) {
            Appointment appointment = new Appointment();
            appointment.setSubject(issue.getName());
            appointment.setStartDate(new Date(issue.getStartDate().getTime()));
            appointment.setEndDate(new Date(issue.getDueDate().getTime()));
            appointment.setDescription(issue.getDescription());
            appointment.setCreatedBy(issue.getCreator() != null ? issue.getCreator().getFullName() : "N/A");
            appointment.setAllDay(appointment.isAllDay());
            appointment.setMultiDay(appointment.isMultiDayAppointment());
            appointment.setStyle(Appointment.PURPLE);
            appointment.setVisible(true);
            appointment.setEditable(false);
            String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("issue|summary/" + issue.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            appointment.setLinkURL(link);
            issueEvents.add(appointment);
        }
        return issueEvents;
    }

    @Override
    public ArrayList<Appointment> getCalendarCourses(CalendarFilter filter) {
        EdsUser user = userManager.getUser();
        ArrayList<Appointment> courses = new ArrayList<>();
        List<EdsCourseSchedule> schedules = scheduledCourseManager.getScheduledCourses(filter);
        for (EdsCourseSchedule course : schedules) {
            Appointment appointment = new Appointment();
            appointment.setObjectID(course.getObjectID());
            appointment.setSubject(course.getName());
            appointment.setStartDate(new Date(course.getStartDate().getTime()));
            appointment.setEndDate(new Date(course.getEndDate().getTime()));
            appointment.setLocation(course.getLocation() != null ? course.getLocation().getName() : "N/A");
            appointment.setLocationId(course.getLocation() != null ? course.getLocation().getObjectID() : null);
            appointment.setOwnerName(course.getInstructor() != null ? course.getInstructor().getFullName() : null);
            appointment.setMaxAttendents(course.getNumberOfSeats());
            appointment.setAllDay(appointment.isAllDay());
            appointment.setMultiDay(appointment.isMultiDayAppointment());
            appointment.setStyle(Appointment.YELLOW);
            appointment.setVisible(true);
            appointment.setEditable(true);
            String link = "TrainingCenter.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("scheduledcourse|summary/" + course.getObjectID())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            appointment.setLinkURL(link);
            courses.add(appointment);
        }
        return courses;
    }

    @Override
    public ArrayList<Appointment> getCalendarLeaveRequests(ArrayList<Integer> employeeIDs, Date start, Date end) {
        ArrayList<Appointment> leaveRequestEvents = new ArrayList<>();
        EdsUser user = userManager.getUser();
        List<EdsSickRequest> requests = sickRequestManager.getCalendarSickRequests(employeeIDs, start, end);
        for (EdsSickRequest request : requests) {
            Appointment appointment = new Appointment();
            appointment.setObjectID(request.getObjectID());
            appointment.setSubject(request.getDescription());
            TimeZone timeZone = user.getUserTimezone();
            Date sDate = (Date) request.getStartDate().clone();
            Date newStart = new Date(sDate.getYear(), sDate.getMonth(), sDate.getDate(), sDate.getHours(), sDate.getMinutes() - (timeZone.getRawOffset() / 60000), sDate.getSeconds());
            appointment.setStartDate(newStart);
            Date eDate = (Date) request.getEndDate().clone();
            Date newEnd = new Date(eDate.getYear(), eDate.getMonth(), eDate.getDate(), eDate.getHours(), eDate.getMinutes() - (timeZone.getRawOffset() / 60000), eDate.getSeconds());
            appointment.setEndDate(newEnd);
            appointment.setDescription(request.getDescription());
            appointment.setCreatedBy(request.getRegisteredBy() != null ? request.getRegisteredBy().getFullName() : "N/A");
            appointment.setAllDay(appointment.isAllDay());
            appointment.setMultiDay(appointment.isMultiDayAppointment());
            appointment.setStyle(Appointment.RED);
            appointment.setVisible(true);
            appointment.setEditable(false);
            leaveRequestEvents.add(appointment);
        }
        return leaveRequestEvents;
    }

    @Override
    public ArrayList<Appointment> getCalendarHolidays(Date start, Date end) {
        ArrayList<Appointment> holidayEvents = new ArrayList<>();
        EdsUser user = userManager.getUser();
        EdsLocation location = user.getLocation();
        List<EdsHoliday> holidays = holidayManager.getCalendarHolidays(location, start, end);
        EdsCompany company = employeeManager.getUser().getCompany();
        for (EdsHoliday holiday : holidays) {
            Appointment appointment = new Appointment();
            appointment.setSubject(holiday.getName());
            appointment.setStartDate(new Date(holiday.getStartDate().getTime() - user.getUserTimezone().getRawOffset()));
            appointment.setEndDate(new Date(holiday.getEndDate().getTime() - user.getUserTimezone().getRawOffset()));
            appointment.setDescription(holiday.getDescription());
            appointment.setCreatedBy(company.getName());
            appointment.setAllDay(appointment.isAllDay());
            appointment.setMultiDay(appointment.isMultiDayAppointment());
            appointment.setStyle(Appointment.PINK);
            appointment.setVisible(true);
            appointment.setEditable(false);
            if (location != null) {
                String locationName = location.getCountry().getName() + "," + (location.getState() != null ? (location.getState().getName() + ",") : "") + location.getCity();
                appointment.setLocation(locationName);
            }
            holidayEvents.add(appointment);
        }
        return holidayEvents;
    }

    private Appointment wrapEdsTaskToAppointmentShort(EdsEmployeeTask employeeTask) {
        EdsTask task = employeeTask.getTask();
        final Appointment appointment = new Appointment();
        appointment.setObjectID(task.getObjectID()); //Appointment ID should be Events ID
        appointment.setSubject(task.getName());
        appointment.setLocation(task.getProject() != null ? task.getProject().getName() : "N/A");
        appointment.setStartDate(task.getStartDate());
        appointment.setEndDate(task.getDueDate());
        appointment.setStyle(Appointment.GREEN);

        EdsReference completedTaskStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);
        EdsReference closedTaskStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
        EdsReference taskStatus = employeeTask.getStatus();

        appointment.setTaskCompleted(taskStatus != null && ((completedTaskStatus != null && taskStatus.getObjectID().equals(completedTaskStatus.getObjectID())) || (closedTaskStatus != null && taskStatus.getObjectID().equals(closedTaskStatus.getObjectID()))));
        return appointment;
    }

    private Appointment wrapEdsTaskToAppointment(EdsEmployeeTask empTask, boolean visible) {
        Appointment appointment = wrapEdsTaskToAppointmentShort(empTask);
        EdsTask task = empTask.getTask();
        EdsUser owner = task.getCreator();
        appointment.setDescription(task.getDescription());
        appointment.setCreatedBy(owner != null ? owner.getFullName() : "N/A");
        if (task.getPriority() != null) {
            appointment.setPriorityID(task.getPriority().getObjectID());
        }
        appointment.setProjectID(task.getProject() != null ? task.getProject().getObjectID() : Integer.valueOf(76));
        appointment.setVisible(visible);
        appointment.setAllDay(task.isAllDay());
        appointment.setMultiDay(task.isMultiDayAppointment());
        appointment.setProjectEmployees(wrapTaskAssigneesToIdTime(task));
        if (owner != null) {
            appointment.setOwnerID(owner.getObjectID());
        }
        if (task.getFireTime() != null) {
            appointment.setFireTime(task.getFireTime());
        }
        if (task.getRecurrenceID() != null) {
            EdsRecurrence recurrence = recurrenceManager.get(task.getRecurrenceID());
            if (recurrence != null) {
                appointment.setRecurrenceId(recurrence.getObjectID());
                RecurrenceJobItem recurrenceJobItem = recurrence.createRecurrenceItem(RECURRING_TASK);
                recurrenceJobItem.setEnabled(true);
                appointment.setRecurrenceJobItem(recurrenceJobItem);
            }
        }
        EdsUser user = taskManager.getUser();
        for (EdsRole role : user.getRoles()) {
            appointment.setEditable(false);
            if (EdsRole.PM.equals(role.getObjectID()) || EdsRole.TL.equals(role.getObjectID()) || EdsRole.ADMIN.equals(role.getObjectID()) || EdsRole.CALENDAR_EDITOR.equals(role.getObjectID()) || user.getFullName().equals(appointment.getCreatedBy())) {
                appointment.setEditable(true);
                break;
            }
        }
        List<EdsEmployeeTask> employeeTasks = employeeTaskManager.getEmployeeTasks(null, task);
        ArrayList<Attendee> attendees = new ArrayList<>();
        for (EdsEmployeeTask employeeTask : employeeTasks) {
            Attendee attendee = new Attendee();
            EdsProjectEmployee employee = employeeTask.getProjectEmployee();
            attendee.setID(employee.getObjectID());
            attendee.setName(employee.getEmployeeDepartment().getEmployee().getFullName());
            attendee.setGoogleID(employeeTask.getGoogleID());
            if (employeeTask.getGoogleID() != null && !"".equals(employeeTask.getGoogleID().trim())) {
                appointment.setHasGoogleAccount(true);
            }
            attendees.add(attendee);
        }
        appointment.setAttendees(attendees);
        return appointment;
    }

    private IdTime[] wrapTaskAssigneesToIdTime(EdsTask task) {
        Set<EdsEmployeeTask> employeeTasks = task.getUnDeletedAssignments();
        IdTime[] projectEmployees = new IdTime[employeeTasks.size()];
        int i = 0;
        for (EdsEmployeeTask employeeTask : employeeTasks) {
            projectEmployees[i] = new IdTime(
                    employeeTask.getProjectEmployee().getObjectID(),
                    employeeTask.getEstimatedTime(),
                    employeeTask.getTimeSpent(),
                    employeeTask.getPercent(),
                    employeeTask.getStatus() != null ? employeeTask.getStatus().getObjectID() : null,
                    employeeTask.getGoogleID());
            i++;

        }
        return projectEmployees;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<Appointment> getUserOverdueTasks() {
        ArrayList<Appointment> overdueTasks = new ArrayList<>();
        Set<String> employeeTasksSet = new HashSet<>();
        EdsUser user = userManager.getUser();
        Date currentDate = new Date();
        List<EdsEmployeeTask> employeeOverdueTasks = taskManager.getEmployeeOverdueTasks(user.getObjectID(), currentDate);
        for (EdsEmployeeTask employeeTask : employeeOverdueTasks) {
            EdsTask task = employeeTask.getTask();
            Appointment appointment = null;
            String taskStrings = task.getObjectID() + "_" + task.getStartDate().toString();
            if (!employeeTasksSet.contains(taskStrings)) {
                employeeTasksSet.add(taskStrings);
                appointment = wrapEdsTaskToAppointmentShort(employeeTask);
                appointment.setAllDay(task.isAllDay());
                appointment.setMultiDay(task.isMultiDayAppointment());
                String link = EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("task|summary/" + task.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
                appointment.setLinkURL(link);
            }
            overdueTasks.add(appointment);
        }
        return overdueTasks;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<Appointment> getCalendarTasksAndEvents(ArrayList<Integer> employeeIDs, Date start, Date end, boolean eventVisible, boolean taskVisible) {
        boolean fromAgenda = false;
        if (start == null) {
            fromAgenda = true;
            start = new Date();
            start.setHours(0);
            start.setMinutes(0);
            start.setSeconds(0);
        }
        CalendarFilter filter = new CalendarFilter();
        filter.setEmployeeIDs(employeeIDs);
        filter.setStart(start);
        filter.setEnd(end);
        filter.setFromAgenda(fromAgenda);
        filter.setVisible(eventVisible);
        ArrayList<Appointment> appointments = new ArrayList<>(getCalendarEvents(filter));
        filter.setCall(true);
        appointments.addAll(getCalendarEvents(filter));
        appointments.addAll(getCalendarTasks(employeeIDs, start, end, fromAgenda, taskVisible, false));
        return appointments;
    }

    @Override
    public ArrayList<Appointment> getCalendarEvents(CalendarFilter calendarFilter) {
        EdsUser user = userManager.getUser();
        Set<String> employeeEventsSet = new HashSet<>();
        ArrayList<Appointment> appointments = new ArrayList<>();
        if (calendarFilter.getEnd() == null) {
            GregorianCalendar endDate = new GregorianCalendar();
            endDate.setTime(calendarFilter.getStart());
            endDate.add(Calendar.YEAR, 1);
            calendarFilter.setEnd(endDate.getTime());
        }
        if (!calendarFilter.isForPDF()) {
            Date endDate = calendarFilter.getEnd();
            if (!calendarFilter.isFromMobile()) {
                endDate = getMonthLastTime(DateUtil.getMonthLastDate(calendarFilter.getEnd()));
            }

            List<EdsEmployeeEvent> employeeEvents = employeeEventManager.getCalendarEvents(calendarFilter.getEmployeeIDs(), calendarFilter.getStart(), endDate, calendarFilter.isFromAgenda(), calendarFilter.getLocationID(), !user.hasRoles(ADMIN), calendarFilter.isCall());
            List<Integer> eventIDs = employeeEventManager.getCalendarEventIDs(calendarFilter.getEmployeeIDs(), calendarFilter.getStart(), endDate, calendarFilter.isFromAgenda(), calendarFilter.getLocationID(), !user.hasRoles(ADMIN), calendarFilter.isCall());
            Map<Integer, List<EdsRelation>> eventRelations = getAllEventRelations(eventIDs);
            for (EdsEmployeeEvent employeeEvent : employeeEvents) {
                EdsEvent event = employeeEvent.getEvent();
                String eventStrings = event.getObjectID().toString() + "_" + event.getStartDate().toString();
                if (!employeeEventsSet.contains(eventStrings)) {
                    employeeEventsSet.add(eventStrings);
                    Appointment appointment = null;
                    if (calendarFilter.isFromAgenda()) {
                        appointment = wrapEdsEventToAppointmentShort(event);
                        appointment.setRelations(EdsRelation.asRPCs(eventRelations.get(appointment.getObjectID())));
                    } else {
                        if (calendarFilter.isForUIOnly()) {
                            appointment = getAppointmentForUI(event);
                            appointment.setRelations(EdsRelation.asRPCs(eventRelations.get(appointment.getObjectID())));
                            appointment.setEditable(user.hasEitherRoles(PM, TL, ADMIN, CALENDAR_EDITOR) || user.getFullName().equals(appointment.getCreatedBy()));

                            String link = "Crm.html?link=" +
                                    EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("event|summary/" +
                                            event.getObjectID().toString() +
                                            (event.getActivityType() == Appointment.CALL_LOG ? "/true" : ""))
                                    ) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
                            appointment.setLinkURL(link);
                        } else {
                            appointment = wrapEdsEventToAppointment(event, calendarFilter.isReadOnly(), false, false);
                            appointment.setRelations(EdsRelation.asRPCs(eventRelations.get(appointment.getObjectID())));
                            appointment.setVisible(calendarFilter.isVisible());
                        }
                    }
                    if (appointment.getActivityType() != Appointment.SMS) {
                        appointments.add(appointment);
                    }
                }
            }
        } else {
            List<EdsEmployeeEvent> employeeEvents = employeeEventManager.getCalendarEvents(calendarFilter.getEmployeeIDs(), calendarFilter.getStart(), calendarFilter.getEnd(), calendarFilter.isFromAgenda(), null, !user.hasRoles(ADMIN), calendarFilter.isCall());
            List<Integer> eventIDs = employeeEventManager.getCalendarEventIDs(calendarFilter.getEmployeeIDs(), calendarFilter.getStart(), calendarFilter.getEnd(), calendarFilter.isFromAgenda(), null, !user.hasRoles(ADMIN), calendarFilter.isCall());
            Map<Integer, List<EdsRelation>> eventRelations = getAllEventRelations(eventIDs);
            for (EdsEmployeeEvent employeeEvent : employeeEvents) {
                EdsEvent event = employeeEvent.getEvent();
                String eventStrings = event.getObjectID().toString() + "_" + event.getStartDate().toString();
                if (!employeeEventsSet.contains(eventStrings)) {
                    employeeEventsSet.add(eventStrings);
                    Appointment appointment = null;
                    if (calendarFilter.isFromAgenda()) {
                        appointment = wrapEdsEventToAppointmentShort(event);
                    } else {
                        appointment = wrapEdsEventToAppointment(event, calendarFilter.isReadOnly(), false, false);
                        appointment.setVisible(calendarFilter.isVisible());
                    }
                    ArrayList<EdsRelation> relations = new ArrayList<>();
                    for (EdsRelation relation : eventRelations.get(appointment.getObjectID())) {
                        if (RelationItem.TYPE_BOOKING.equals(relation.getFromType())) {
                            EdsRelation relationClone = relation.cloneShallow();
                            EdsBookingItemReservation reservation = reservationManager.get(relation.getFromID());
                            relationClone.setFromName(relationClone.getFromName() + "<br>From: " + ServerUtils.longDateFormat(reservation.getFrom(), user, null)
                                    + "<br>To: " + ServerUtils.longDateFormat(reservation.getTo(), user, null));
                            relations.add(relationClone);
                        } else {
                            relations.add(relation);
                        }
                    }
                    appointment.setRelations(EdsRelation.asRPCs(relations));
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    private Map<Integer, List<EdsRelation>> getAllEventRelations(List<Integer> eventIDs) {
        Map<Integer, List<EdsRelation>> eventRelations = new HashMap<>();
        List<EdsRelation> fromRelations = relationManager.getAllFromRelations(RelationItem.TYPE_EVENT, eventIDs);
        List<EdsRelation> toRelations = relationManager.getAllToRelations(RelationItem.TYPE_EVENT, eventIDs);
        for (EdsRelation rel : fromRelations) {
            if (eventRelations.containsKey(rel.getFromID())) {
                eventRelations.get(rel.getFromID()).add(rel);
            } else {
                List<EdsRelation> relationList = new ArrayList<>();
                relationList.add(rel);
                eventRelations.put(rel.getFromID(), relationList);
            }
        }
        for (EdsRelation rel : toRelations) {
            if (eventRelations.containsKey(rel.getToID())) {
                eventRelations.get(rel.getToID()).add(rel);
            } else {
                List<EdsRelation> relationList = new ArrayList<>();
                relationList.add(rel);
                eventRelations.put(rel.getToID(), relationList);
            }
        }
        return eventRelations;
    }

    @Override
    public ArrayList<Appointment> getCalendarProjects(ArrayList<Integer> employeeIDs, Date start, Date end) {
        ArrayList<Appointment> projectEvents = new ArrayList<>();
        EdsUser user = userManager.getUser();
        List<EdsProject> projects = projectManager.getCalendarProjects(employeeIDs, start, end);
        for (EdsProject project : projects) {
            Appointment appointment = new Appointment();
            appointment.setObjectID(project.getObjectID());
            appointment.setSubject(project.getName());
            appointment.setStartDate(new Date(project.getStartDate().getTime()));
            appointment.setEndDate(new Date(project.getDueDate().getTime()));
            appointment.setDescription(project.getDescription());
            appointment.setCreatedBy(project.getCreator() != null ? project.getCreator().getFullName() : "N/A");
            appointment.setAllDay(appointment.isAllDay());
            appointment.setMultiDay(appointment.isMultiDayAppointment());
            appointment.setStyle(Appointment.ORANGE);
            appointment.setVisible(true);
            appointment.setEditable(false);
            appointment.setNumberData(project.getNumber());
            String link = "ProjectManagement.html?link=" + EncryptionHelper.encodeURL(EncryptionHelper.encryptURL("project|summary/" + project.getObjectID().toString())) + "&cid=" + EncryptionHelper.encryptURL(user.getCompany().getObjectID().toString());
            appointment.setLinkURL(link);
            projectEvents.add(appointment);
        }
        return projectEvents;
    }

    private void createOrUpdateEventGuests(Appointment appointment, EdsEvent event) {
        if (event != null) {
            ArrayList<String> oldGuests = new ArrayList<>();
            List<EdsGoogleCalendarEventGuests> guests = eventGuestsManager.getEventGuests(event.getObjectID());
            if (guests != null && !guests.isEmpty()) {
                ArrayList<SelectItem> guestList = new ArrayList<>();
                for (EdsGoogleCalendarEventGuests guest : guests) {
                    oldGuests.add(guest.getEmail());
                }
            }

            ArrayList<String> newGuests = new ArrayList<>();
            if (appointment.getGuests() != null && !appointment.getGuests().isEmpty()) {
                for (SelectItem item : appointment.getGuests()) {
                    if (!item.getName().contains("&")) {
                        newGuests.add(item.getName());
                    } else {
                        String[] guestNames = item.getName().split("&");
                        newGuests.add(guestNames[1]);
                    }
                }
            }
            ServerUtils.intersect(newGuests, oldGuests);   // separate event's old and new guests
            if (!newGuests.isEmpty()) {
                createEventGuests(newGuests, event);       // creating event's new guests
            }
        }
    }

    private void createEventGuests(ArrayList<String> newGuests, EdsEvent event) {
        if (newGuests != null && !newGuests.isEmpty()) {
            EdsUser user = userManager.getUser();
            for (String guest : newGuests) {
                EdsGoogleCalendarEventGuests eventGuest = new EdsGoogleCalendarEventGuests();
                eventGuest.setEvent(event);
                eventGuest.setEmail(guest);
                eventGuest.setStatus(EVENT_GUEST_STATUS_PENDING);
                eventGuestsManager.create(eventGuest);
                baseEventPostProcessor.registerEvent(GoogleCalendarGuestsEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, event, user);
            }
        }
    }


    public ArrayList<SelectItem> wrapEdsGoogleCalendarEventGuestsToSelectItem(EdsEvent event, boolean replaceEmailToName) {
        List<EdsGoogleCalendarEventGuests> guests = eventGuestsManager.getEventGuests(event.getObjectID());
        if (guests != null && !guests.isEmpty()) {
            ArrayList<SelectItem> guestList = new ArrayList<>();
            for (EdsGoogleCalendarEventGuests guest : guests) {
                String userFullName = "";
                if (replaceEmailToName) {
                    EdsCrmContact contact = crmContactManager.getContactByEmail(guest.getEmail(), event.getOwner().getCompany().getObjectID());
                    if (contact != null) {
                        userFullName = contact.getName();
                    } else {
                        EdsCrmContact lead = crmContactManager.getContactByPrimaryEmail(guest.getEmail());
                        if (lead != null) {
                            userFullName = lead.getFirstName() + " " + lead.getLastName();
                        }
                    }
                }
                SelectItem guestItem = new SelectItem(guest.getObjectID(), !"".equals(userFullName) ? userFullName + "<" + guest.getEmail() + ">" : guest.getEmail() + "<" + guest.getEmail() + ">", guest.getStatus());
                guestList.add(guestItem);
            }
            return guestList;
        }
        return null;
    }

    private void wrapAppointmentToEdsEvent(Appointment appointment, EdsEvent event, EdsEmployee owner, boolean withNotify) {
        EdsUser user = userManager.getUser();
        event.setSubject(appointment.getSubject());
        event.setInboundCall(appointment.isInboundCall());
        event.setOutboundCall(appointment.isOutboundCall());
        event.setMissedCall(appointment.isMissedCall());
        event.setDescription(appointment.getDescription());
        event.setVenue(appointment.getLocation());
        event.setAddress1(appointment.getAddress1());
        event.setAddress2(appointment.getAddress2());
        event.setCity(appointment.getCity());
        event.setOfficeID(appointment.getOfficeID());
        event.setPrivate(appointment.getIsPrivate());
        event.setTwilioCallSID(appointment.getTwilioCallSID());
        if (appointment.getCallDuration() > 0) {
            event.setCallDuration(appointment.getCallDuration());
        }
        event.setCurrentCall(appointment.isCurrentCall());
        event.setCompletedCall(appointment.isComplatedCall());
        event.setScheduleCall(appointment.isScheduleCall());
        event.setLastModifiedDate(new Date());
        if (appointment.getCountryId() != null) {
            event.setCountry(countyManager.get(appointment.getCountryId()));
        }
        event.setPostcode(appointment.getPostCode());
        if (appointment.isAllDay() && user != null) {
            event.setStartDate(appointment.getStartDate());
            event.setEndDate(appointment.getEndDate());

            Date userStartDate = user.getUserDate(event.getStartDate());
            Date userEndDate = user.getUserDate(event.getEndDate());

            event.setTimeZoneOffset(user.getUserTimezone().getRawOffset());
            int dayCount = ServerUtils.getDayCountInCalendar(userStartDate, userEndDate);
            event.setDayCount(dayCount != 0 ? dayCount : 1);
        } else {
            event.setStartDate(appointment.getStartDate());
            event.setEndDate(appointment.getEndDate());
        }
        event.setFireTime(appointment.getFireTime());
        event.setOrganizationName(appointment.getOrganizationName());
        event.setOrganizationDescription(appointment.getOrganizationDescription());
        if (appointment.getWorkflowID() != null) {
            event.setWorkflowID(appointment.getWorkflowID());
            event.setWorkflowItem(appointment.getWorkflowID() != null);
            event.setWorkflowDueDateGranularity(appointment.getWorkflowDueDateGranularity());
            event.setWorkflowDueDate(appointment.getWorkflowDueDate());
            event.setWorkflowStartDate(appointment.getWorkflowStartDate());
            event.setWorkflowStartDateAttributes(appointment.getWorkflowStartDateAttributes());
            event.setWorkflowActionTimeBased(appointment.isWorkflowActionTimeBased());
            event.setWorkflowActionStartTime(appointment.getWorkflowActionStartTime());
            event.setWorkflowActionStartTimeUnit(appointment.getWorkflowActionStartTimeUnit());
            event.setWorkflowActionStartTimeGranularity(appointment.getWorkflowActionStartTimeGranularity());
            event.setDeleted(true);
        }
        if (appointment.getLogoId() != null) {
            event.setLogoId(appointment.getLogoId());
        }
        event.setLastModifiedDate(new Date());
        event.setLastModifiedBy(owner);
        event.setAllDay(appointment.isAllDay());
        event.setSendEmailNotification(appointment.isSendEmailNotification());
        event.setIncludeAttachments(appointment.isIncludeAttachments());
        event.setWithNotify(withNotify);
        event.setBooking(appointment.isBooking());
        event.setActivityType(appointment.getActivityType());
        event.setCreatedFrom(appointment.getCreatedFrom());
        event.setPublic(appointment.isPublic());
        if (appointment.getLocationId() != null) {
            event.setLocationID(appointment.getLocationId());
        }
        if (appointment.getTickets() != null && appointment.getTickets().length > 0) {
            Integer totalTicketCoint = 0;
            for (TicketItem ticket : appointment.getTickets()) {
                totalTicketCoint += ticket.getQty().intValue();
            }
            event.setTotalTicketCount(totalTicketCoint);

        }
        if (appointment.getPublished() != null) {
            event.setPublished(appointment.getPublished());
        }
        if (appointment.isBooking()) {
            event.setMaxAttendants(appointment.getMaxAttendents());
        }

        if (owner != null && event.getOwner() == null) {
            event.setOwner(owner);
        }
//        eventManager.update(event);
//        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
    }

    private Appointment wrapEdsEventToAppointmentShort(EdsEvent event) {
        final Appointment appointment = new Appointment();
        appointment.setObjectID(event.getObjectID()); //Appointment ID should be Events ID
        appointment.setSubject(event.getSubject());
        appointment.setLocation(event.getVenue());
        appointment.setAddress1(event.getAddress1());
        appointment.setAddress2(event.getAddress2());
        appointment.setCity(event.getCity());
        appointment.setLocationId(event.getLocationID());

        if (event.getCountry() != null) {
            appointment.setCountryId(event.getCountry().getObjectID());
        }
        appointment.setPostCode(event.getPostcode());
        appointment.setStartDate(event.getStartDate());
        appointment.setEndDate(event.getEndDate());
        appointment.setAllDay(appointment.isAllDay());
        appointment.setStyle(Appointment.CALL_LOG == event.getActivityType() ? Appointment.AQUA : Appointment.BLUE);
        appointment.setSendEmailNotification(event.isSendEmailNotification() != null ? event.isSendEmailNotification() : true);
        appointment.setIncludeAttachments(event.getIncludeAttachments() != null ? event.getIncludeAttachments() : true);
        return appointment;
    }

    private Appointment wrapEdsEventToAppointment(EdsEvent event) {
        return wrapEdsEventToAppointment(event, false, true, false);
    }

    private Appointment wrapEdsEventToAppointment(EdsEvent event, boolean isReadOnly, boolean withRelations, boolean isCopy) {
        EdsUser user = userManager.getUser();
        final Appointment appointment = new Appointment();
        appointment.setObjectID(event.getObjectID()); //Appointment ID should be Events ID
        appointment.setSubject(event.getSubject());
        appointment.setInboundCall(event.isInboundCall());
        appointment.setOutboundCall(event.isOutboundCall());
        appointment.setMissedCall(event.isMissedCall());
        appointment.setDescription(event.getDescription());
        if (event.getOwner() != null) {
            appointment.setCreatedBy(event.getOwner().getFullName());
        }
        appointment.setLocation(event.getVenue());
        appointment.setAddress1(event.getAddress1());
        appointment.setAddress2(event.getAddress2());
        appointment.setCity(event.getCity());
        appointment.setLocationId(event.getLocationID());
        appointment.setTwilioCallSID(event.getTwilioCallSID());
        appointment.setCallDuration(event.getCallDuration());
        appointment.setSendEmailNotification(event.isSendEmailNotification());
        appointment.setAsteriskid(event.getAsteriskid());
        appointment.setCurrentCall(event.isCurrentCall());
        appointment.setComplatedCall(event.isCompletedCall());
        appointment.setScheduleCall(event.isScheduleCall());

        if (event.getWorkflowID() != null) {
            EdsWorkflowRule workflowRule = workflowRuleManager.get(event.getWorkflowID());
            appointment.setWorkflowDueDateGranularity(event.getWorkflowDueDateGranularity());
            appointment.setWorkflowDueDate(event.getWorkflowDueDate());
            appointment.setWorkflowStartDate(event.getWorkflowStartDate());
            appointment.setWorkflowStartDateAttributes(event.getWorkflowStartDateAttributes());
            appointment.setWorkflowID(event.getWorkflowID());
            appointment.setWorkflowModule(workflowRule.getModule());
            appointment.setWorkflowItem(event.isWorkflowItem());
            appointment.setWorkflowActionTimeBased(event.isWorkflowActionTimeBased());
            appointment.setWorkflowActionStartTime(event.getWorkflowActionStartTime());
            appointment.setWorkflowActionStartTimeUnit(event.getWorkflowActionStartTimeUnit());
            appointment.setWorkflowActionStartTimeGranularity(event.getWorkflowActionStartTimeGranularity());
        }
        if (event.getCountry() != null) {
            appointment.setCountryId(event.getCountry().getObjectID());
        }
        appointment.setPostCode(event.getPostcode());
        appointment.setStartDate(event.getStartDate());
        appointment.setEndDate(event.getEndDate());
        appointment.setLastModifiedDate(event.getLastModifiedDate());
        if (!isReadOnly) {
            appointment.setGuests(wrapEdsGoogleCalendarEventGuestsToSelectItem(event, true));
        }
        appointment.setActivityType(event.getActivityType());
        appointment.setCreatedFrom(event.getCreatedFrom());
        appointment.setAllDay(event.isAllDay());
        appointment.setIsPrivate(event.getIsPrivate());
        if (user != null && event.getOwner() != null) {
            appointment.setIsOwner(event.getOwner().getObjectID().equals(user.getObjectID()));
        }
        appointment.setMultiDay(event.isMultiDayAppointment());
        appointment.setStyle(Appointment.CALL_LOG == event.getActivityType() ? Appointment.AQUA : Appointment.BLUE);
        EdsUser owner = event.getOwner();
        if (owner != null) {
            appointment.setOwnerID(owner.getObjectID());
        }
        if (event.getFireTime() != null) {
            appointment.setFireTime(event.getFireTime());
        }
        if (event.getRecurrenceID() != null) {
            EdsRecurrence recurrence = recurrenceManager.get(event.getRecurrenceID());
            if (recurrence != null) {
                appointment.setRecurrenceId(recurrence.getObjectID());
                RecurrenceJobItem recurrenceJobItem = recurrence.createRecurrenceItem(RECURRING_EVENT);
                recurrenceJobItem.setEnabled(true);
                appointment.setRecurrenceJobItem(recurrenceJobItem);
            }
        }
        if (event.getBooking() != null) {
            appointment.setBooking(event.getBooking());
            appointment.setMaxAttendents(event.getMaxAttendants());
        }

        if (event.getPublic()) {
            appointment.setPublic(event.getPublic());
            ArrayList<TicketItem> tickets = new ArrayList<>();
            for (EdsTicket ticket : event.getTickets()) {
                tickets.add(ticket.getRPC());
            }
            appointment.setOrganizationName(event.getOrganizationName());
            appointment.setOrganizationDescription(event.getOrganizationDescription());
            appointment.setTickets(tickets.toArray(new TicketItem[]{}));

            if (event.getTotalTicketCount() != null) {

                Integer remainingTicketCount = 0;
                for (TicketItem ticket : tickets) {
                    remainingTicketCount += ticket.getQty().intValue();
                }

                appointment.setTotalTicketCount(event.getTotalTicketCount());
                appointment.setSold(event.getTotalTicketCount() - remainingTicketCount);
                appointment.setRemaining(remainingTicketCount);
            }

            appointment.setPublished(event.getPublished());
            appointment.setLogoId(event.getLogoId());
            if (event.getCountry() != null) {
                appointment.setCountryId(event.getCountry().getObjectID());
            }
        }

        final List<EdsEmployeeEvent> employeeEvents = employeeEventManager.getEmployeeEvents(event.getObjectID());
        final ArrayList<Attendee> attendees = new ArrayList<>();
        for (EdsEmployeeEvent employeeEvent : employeeEvents) {
            Attendee attendee = new Attendee();
            EdsUser employee = employeeEvent.getEmployee();
            if (employee != null) {
                attendee.setID(employee.getObjectID());
                attendee.setName(employee.getFullName());
            }
            attendee.setGoogleID(employeeEvent.getGoogleID());
            attendee.setOfficeID(employeeEvent.getOfficeID());
            if (employeeEvent.getGoogleID() != null && !"".equals(employeeEvent.getGoogleID().trim())) {
                appointment.setHasGoogleAccount(true);
            }
            attendee.setShared(employeeEvent.isShared());
            attendees.add(attendee);
        }
        if (!isReadOnly) {
            if (user != null) {
                for (EdsRole role : user.getRoles()) {
                    appointment.setEditable(false);
                    if (EdsRole.PM.equals(role.getObjectID()) || EdsRole.TL.equals(role.getObjectID()) || EdsRole.ADMIN.equals(role.getObjectID()) || EdsRole.CALENDAR_EDITOR.equals(role.getObjectID()) || user.getFullName().equals(appointment.getCreatedBy())) {
                        appointment.setEditable(true);
                        break;
                    }
                }
            }
        }
        appointment.setAttendees(attendees);
        ArrayList<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(Appointment.CALL_LOG == event.getActivityType() ? ViewName.LogACall : ViewName.Activity);
        appointment.setCustomFieldItems(CustomFieldsUtils.setRPCCustomFieldItems(event.getEventCustomFields(), customFieldsItems));

        if (isCopy) {
            if (appointment.getCustomFieldItems() != null && appointment.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem ccfItem : appointment.getCustomFieldItems()) {
                    ccfItem.setObjectId(null);
                }
            }
        }
        if (withRelations) {
            appointment.setRelations(EdsRelation.asRPCs(relationManager.getAllRelations(RelationItem.TYPE_EVENT, appointment.getObjectID())));
        }
        return appointment;
    }

    private Appointment getAppointmentForUI(EdsEvent event) {
        final Appointment appointment = new Appointment();
        appointment.setObjectID(event.getObjectID());
        appointment.setSubject(event.getSubject());
        appointment.setInboundCall(event.isInboundCall());
        appointment.setMissedCall(event.isMissedCall());
        appointment.setOutboundCall(event.isOutboundCall());
        appointment.setDescription(event.getDescription());
        if (event.getOwner() != null) {
            appointment.setOwnerID(event.getOwner().getObjectID());
            appointment.setCreatedBy(event.getOwner().getFullName());
        }
        appointment.setLocation(event.getVenue());
        appointment.setAddress1(event.getAddress1());
        appointment.setAddress2(event.getAddress2());
        appointment.setCity(event.getCity());
        appointment.setPostCode(event.getPostcode());
        appointment.setStartDate(event.getStartDate());
        appointment.setEndDate(event.getEndDate());
        appointment.setLastModifiedDate(event.getLastModifiedDate());
        appointment.setActivityType(event.getActivityType());
        appointment.setCreatedFrom(event.getCreatedFrom());
        appointment.setAllDay(event.isAllDay());
        appointment.setMultiDay(event.isMultiDayAppointment());
        appointment.setStyle(Appointment.CALL_LOG == event.getActivityType() ? Appointment.AQUA : event.isAllDay() ? Appointment.BLUE : Appointment.BLUE_SHORT);
        appointment.setVisible(true);
        appointment.setPublished(event.getPublished());
        appointment.setCallDuration(event.getCallDuration());
        appointment.setTwilioCallSID(event.getTwilioCallSID());
        if (event.getBooking() != null) {
            appointment.setBooking(event.getBooking());
            appointment.setMaxAttendents(event.getMaxAttendants());
        }
        if (event.getRecurrenceID() != null) {
            appointment.setRecurrenceId(event.getRecurrenceID());
        }
        return appointment;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<TeamEmployees> getCompanyEmployeesWithTeams() {
        ArrayList<TeamEmployees> result = new ArrayList<>();
        List<Integer> employeeIDs = new ArrayList<>();
        LinkedHashMap<WfmTreeItem, LinkedList<WfmTreeItem>> map = new LinkedHashMap<>();
        for (Object[] items : employeeManager.getTeamEmployees()) {
            EdsDepartment department = (EdsDepartment) items[0];
            EdsEmployee employee = (EdsEmployee) items[1];

            WfmTreeItem team = new WfmTreeItem(department.getObjectID(), department.getName());
            map.computeIfAbsent(team, k -> new LinkedList<>());
            if (!employeeIDs.contains(employee.getObjectID())) {
                WfmTreeItem item = new WfmTreeItem(employee.getObjectID(), employee.getName());
                item.setColor(employee.getEmployeeDepartment().getEmployeeTaskColor());
                map.get(team).add(item);
                employeeIDs.add(employee.getObjectID());
            }
        }
        for (Map.Entry<WfmTreeItem, LinkedList<WfmTreeItem>> team : map.entrySet()) {
            if (team.getValue().size() > 0) {
                LinkedList<WfmTreeItem> members = team.getValue();
                result.add(new TeamEmployees(team.getKey(), members));
            }
        }
        return result;
    }

    /**
     * get only available employees in this date range to assign task
     *
     * @param startDate
     * @param endDate
     * @return
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.AppointmentShareView#getOnlyAvailableCompanyEmployeesWithTeams
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<TeamEmployees> getAvailableCompanyEmployeesWithTeams(Date startDate, Date endDate) {
        EdsUser user = employeeManager.getUser();
        ArrayList<TeamEmployees> result = new ArrayList<>();
        List<EdsDepartment> departments = departmentManager.getCompanyDepartments(user.getCompany());

        List<Integer> employeeIDs = new ArrayList<>();

        //here we need to get all unavailable Employee ID's
        List<Integer> unavailableEmployeeIDs = employeeEventManager.getUnavailableEmployeeIDs(user.getCompany(), startDate, endDate);

        //now we run through each department and members to it (1) and at the same time remove unavailable ones from the member map (2)
        for (EdsDepartment department : departments) {
            WfmTreeItem team = new WfmTreeItem(department.getObjectID(), department.getName());
            Map<Integer, WfmTreeItem> members = new HashMap<>();

            //(1) put members of a department to the map
            for (EdsEmployeeDepartment employeeDepartment : department.getMembers()) {
                EdsEmployee employee = employeeDepartment.getEmployee();
                if (employee != null && /*employee.getActive() &&*/ !employeeDepartment.getDeleted() && !employeeIDs.contains(employee.getObjectID())) {
                    members.put(employee.getObjectID(), new WfmTreeItem(employee.getObjectID(), employee.getName()));
                    employeeIDs.add(employee.getObjectID());
                }
            }

            //(2) remove unavailable members from the map
            if (unavailableEmployeeIDs.size() > 0) {
                for (Integer id : unavailableEmployeeIDs) {
                    members.remove(id);
                }
            }

            if (members.size() != 0) {
                LinkedList<WfmTreeItem> memberList = new LinkedList<WfmTreeItem>(members.values());
                memberList.sort(Comparator.comparing(SelectItem::getName));
                result.add(new TeamEmployees(team, memberList));
            }
        }
        return result;
    }

    /**
     * get Company employees to employees dropdown in Google Calendar View
     *
     * @return
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw.GoogleCalendarView#drawLeftPanel
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw.GoogleCalendarView#drawLeftPanel
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCompanyEmployees() {
        EdsEmployee currentEmployee = (EdsEmployee) employeeManager.getUser();
        List<EdsEmployee> employees = employeeManager.getCompanyEmployees();
        employees.remove(currentEmployee);

        SelectItem[] result = new SelectItem[employees.size() + 1];
        result[0] = new SelectItem(currentEmployee.getObjectID(), currentEmployee.getFullName() + referenceWfmMessageSource.localize("mySelf", " (" + MYSELF + ")"));
        int i = 1;
        for (EdsEmployee employee : employees) {
            result[i] = new SelectItem(employee.getObjectID(), employee.getName());
            i++;
        }

        Arrays.sort(result, Comparator.comparing(SelectItem::getName));

        return result;
    }

    /**
     * synchronize WFT Caendar Events and Tasks with Google Calendar;
     *
     * @param userId
     * @param start
     * @param end
     * @return
     * @throws Exception
     * @see com.edatasite.workforce.scheduler.SynchronizeWithGoogleCalendarRecurrenceJob#execute(org.quartz.JobExecutionContext)
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.draw.GoogleCalendarView#synchronizeEvents(java.util.Date, java.util.Date)
     */
    public ArrayList<Appointment> synchronizeEvents(Integer userId, Date start, Date end) throws Exception {
        HashSet<Integer> objectIDs = new HashSet<>();
        HashSet<Integer> deletedObjectIDs = new HashSet<>();

        EdsUser user;
        if (userId != null) {
            user = userManager.get(userId);
        } else {
            user = userManager.getUser();
        }

        EdsGoogleCalendar googleCalendar = googleCalendarManager.getGoogleCalendar(user, true);
        logger.info("=========================================GOOGLE CALENDAR SYNC STRTED=============================================");
        logger.info("=====================================############=====================================");
        logger.info("/////////_User's CompanyID: " + ServerSecurityContext.getInstance().getCompanyId());
        logger.info("=====================================############=====================================");
        logger.info("======================================================================================");
        if (user != null && user.getCompany().getActive() && !user.getDeleted() && googleCalendar != null) {
            TimeZone userTimeZone = TimeZone.getTimeZone(user.getTimezone());
            com.google.api.services.calendar.Calendar calendarService = googleCalendarManager.getServiceLoggedIn(user);
            logger.info("/////////_calendarService =: " + calendarService);
            if (calendarService != null) {
                try {
                    System.out.print("Starting synchronization for calendar for userID: " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");
                    logger.info("Starting synchronization for calendar for userID: " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");

                    List<Event> eventsToAdd = new ArrayList<>();
                    List<Event> eventsToEdit = new ArrayList<>();
                    List<Event> eventsToDelete = new ArrayList<>();

                    List<Event> tasksToAdd = new ArrayList<>();
                    List<Event> tasksToEdit = new ArrayList<>();
                    List<Event> tasksToDelete = new ArrayList<>();
                    EdsEmployee employee = user.getEmployee();

                    // get Google Calendar events with date range from Google as Appointment and CalendarEventEntry Map
                    Object[] events = googleCalendarManager.getGoogleEventsOrTasks(employee, start, end, true);
                    Map<String, Appointment> googleEventsMap = (Map<String, Appointment>) events[1];
                    Map<String, Event> calendarEventEntries = (HashMap<String, Event>) events[0];
                    // get events created or shared this user with date range
                    List<Integer> idList = new ArrayList<>();
                    idList.add(employee.getObjectID());
                    List<EdsEmployeeEvent> employeeEvents = employeeEventManager.getCalendarEvents(idList, start, end, false, null, false);

                    // get Google Calendar tasks with date range from Google as Appointment and CalendarEventEntry Map
                    Object[] tasks = googleCalendarManager.getGoogleEventsOrTasks(employee, start, end, false);
                    Map<String, Appointment> googleTasksMap = (Map<String, Appointment>) tasks[1];
                    Map<String, Event> calendarTaskEntries = (HashMap<String, Event>) tasks[0];
                    // get tasks created or shared this user with date range (PM, CRM, Calendar tasks)
                    List<EdsEmployeeTask> employeeTasks = taskManager.getCalendarTasks(idList, start, end, false);

                    logger.info("==========================================================================");
                    logger.info("=====================================############=====================================");
                    logger.info("Initial Events from Google: " + googleEventsMap.values().size());
                    logger.info("Initial Events from KPI: " + employeeEvents.size());
                    boolean isDelete = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ON_THE_SYNC_CALENDAR_DELETE_EVENT_OR_TASK_FROM_GOOGLE_AND_KPI);
                    normalizeGoogleAndWFTCalendarCollections(employeeEvents, googleEventsMap, calendarEventEntries, calendarService, user, true, isDelete);
                    logger.info("Events from Google after normalization: " + googleEventsMap.values().size());
                    logger.info("Events from KPI after normalization: " + employeeEvents.size());
                    logger.info("=====================================############=====================================");
                    logger.info("==========================================================================");

                    for (EdsEmployeeEvent employeeEvent : employeeEvents) {
                        if (employeeEvent.getGoogleID() != null) { // Event previously synced with Google
                            if (googleEventsMap.containsKey(employeeEvent.getGoogleID())) {// if contained in Google, Update event based on last modified time
                                Appointment appointment = googleEventsMap.get(employeeEvent.getGoogleID());
                                if (appointment.getLastModifiedDate().after(employeeEvent.getLastModifiedDate())) {// Google event has latest version so updated WFT event
                                    // Event updated on Google end, so update WFT event
                                    EdsEvent event = employeeEvent.getEvent();
                                    event.setSubject(appointment.getSubject());
                                    event.setVenue(appointment.getLocation());
                                    event.setDescription(appointment.getDescription());
                                    event.setStartDate(appointment.getStartDate());
                                    event.setEndDate(appointment.getEndDate());
                                    event.setAllDay(appointment.isAllDay());
                                    event.setLastModifiedDate(appointment.getLastModifiedDate() != null ? appointment.getLastModifiedDate() : new Date()); //Set Google events last modified date to Employee Events, and Events Last Modified Date
                                    event.setLastModifiedBy(user);
                                    eventManager.update(event);
                                    baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeEvent.getEvent(), userManager.getUser());
                                    objectIDs.add(event.getObjectID());
                                    employeeEventManager.setEmployeeEventsModifiedDate(employeeEvent.getEvent(), appointment.getLastModifiedDate());
                                    googleEventsMap.remove(employeeEvent.getGoogleID());
                                    calendarEventEntries.remove(employeeEvent.getGoogleID());
                                    logger.info("Event updated on Google end, update WFT event. EventID: " + event.getObjectID() + "; name: " + event.getSubject());
                                } else if (appointment.getLastModifiedDate().before(employeeEvent.getLastModifiedDate())) { // WFT event has latest version so update Google event
                                    Event eventEntry = calendarEventEntries.get(employeeEvent.getGoogleID());
                                    if (employeeEvent != null) {
                                        googleCalendarManager.updateCalendarEventEntry(user, eventEntry, employeeEvent, null);
                                        eventsToEdit.add(eventEntry);
                                        googleEventsMap.remove(employeeEvent.getGoogleID());
                                        calendarEventEntries.remove(employeeEvent.getGoogleID());
                                    }
                                } else {
                                    // No need to sync since nothing has been changed since then
                                    googleEventsMap.remove(employeeEvent.getGoogleID());
                                    calendarEventEntries.remove(employeeEvent.getGoogleID());
                                }
                            } else { // if Not contained in Google, means it has been deleted from Google, so delete from WFT
                                //THE FOLLOWING CHECK ALWAYS RETURNS NULL! THEREFORE COMMENTED (FARHOD)
                                //CalendarEventEntry eventEntry = googleManager.getEntry(calendarService, employeeEvent.getGoogleID(), CalendarEventEntry.class);
                                //if (eventEntry == null) {// if there is indeed no event in Google then delete from WFT. It might be that Google event did not fall in the date range period we have specified, that's why double checking
                                if (isDelete) {
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
                                    logger.info("Event deleted while sync with google. EventID: " + employeeEvent.getEvent().getObjectID() + "; employeeEventID: " + employeeEvent.getObjectID() + "; date: " + new Date());
                                } else {
                                    logger.info("Employee Event might have been deleted while sync with google. EmployeeEventID: " + employeeEvent.getObjectID());
                                }
                                //}
                            }
                        } else { // New event not yet synced with Google
                            Appointment appointment = new Appointment();
                            EdsEvent event = employeeEvent.getEvent();
                            appointment.setSubject(event.getSubject());
                            appointment.setDescription(event.getDescription());
                            appointment.setLocation(event.getVenue());
                            appointment.setStartDate(event.getStartDate());
                            appointment.setEndDate(event.getEndDate());
                            appointment.setAllDay(event.isAllDay());
                            Event eventEntry = googleCalendarManager.createCalendarEventEntry(user, appointment, employeeEvent, null);
                            eventsToAdd.add(eventEntry);
                        }
                    }

                    // Add new events from Google to WFT, or delete old WFT events from Google
                    for (Appointment appointment : googleEventsMap.values().toArray(new Appointment[]{})) {
                        if (appointment.getObjectID() != null) {
                            EdsEmployeeEvent checkEmployeeEvent = employeeEventManager.get(appointment.getObjectID());
                            if (checkEmployeeEvent == null || (checkEmployeeEvent != null && checkEmployeeEvent.getDeleted())) {// if there is indeed no event in WFT then delete from Google. It might be that WFT event did not fall in the date range period we have specified, that's why double checking
                                Event myEntry = calendarEventEntries.get(appointment.getGoogleID());
                                if (myEntry != null) {
                                    eventsToDelete.add(myEntry);
                                }
                            }
                        } else {
                            Date sDate = (Date) appointment.getStartDate().clone();
                            Date eDate = (Date) appointment.getEndDate().clone();
                            appointment.setStartDate(sDate);
                            appointment.setEndDate(eDate);
                            List<EdsEmployeeEvent> newEvent = saveCalendarEvent(employee, appointment, true, false, true, objectIDs, null);
                            Event eventEntry = calendarEventEntries.get(appointment.getGoogleID());
                            Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
                            Map<String, String> privateExtendedProperties = new HashMap<>();
                            privateExtendedProperties.put("employeeEventID", String.valueOf(newEvent.get(0).getObjectID()));
                            extendedProperties.setPrivate(privateExtendedProperties);
                            eventEntry.setExtendedProperties(extendedProperties);
                            eventsToEdit.add(eventEntry);
                        }
                    }

                    logger.info("Initial Tasks from Google: " + googleTasksMap.values().size());
                    logger.info("Initial Tasks from KPI: " + employeeTasks.size());
                    normalizeGoogleAndWFTCalendarCollections(employeeTasks, googleTasksMap, calendarTaskEntries, calendarService, user, false, isDelete);
                    logger.info("Tasks from Google after normalization: " + googleTasksMap.values().size());
                    logger.info("Tasks from KPI after normalization: " + employeeTasks.size());
                    for (EdsEmployeeTask employeeTask : employeeTasks) {
                        if (employeeTask.getGoogleID() != null) { // Task previously synced with Google
                            if (googleTasksMap.containsKey(employeeTask.getGoogleID())) { // if contained in Google, Update task based on last modified time
                                Appointment appointment = googleTasksMap.get(employeeTask.getGoogleID());
                                if (appointment.getLastModifiedDate().after(employeeTask.getLastModifiedDate())) {// Google task has latest version so updated WFT task
                                    // Task updated on Google end, so update WFT task
                                    EdsTask task = employeeTask.getTask();
                                    task.setName(appointment.getSubject());
                                    task.setDescription(appointment.getDescription());
                                    task.setAllDay(appointment.isAllDay());
                                    task.setStartAndDueDates(appointment.getStartDate(), appointment.getEndDate());
                                    task.setLastModifiedDate(appointment.getLastModifiedDate());//Set Google tasks last modified date to Employee Tasks, and Tasks Last Modified Date
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
                                    googleTasksMap.remove(employeeTask.getGoogleID());
                                    calendarTaskEntries.remove(employeeTask.getGoogleID());
                                    logger.info("Task updated on Google end, update WFT task. EventID: " + task.getObjectID() + "; name: " + task.getName());
                                } else if (appointment.getLastModifiedDate().before(employeeTask.getLastModifiedDate())) {// WFT task has latest version so update Google task
                                    Event eventEntry = calendarTaskEntries.get(employeeTask.getGoogleID());
                                    if (eventEntry != null) {
                                        googleCalendarManager.updateCalendarEventEntry(user, eventEntry, null, employeeTask);
                                        tasksToEdit.add(eventEntry);
                                        googleTasksMap.remove(employeeTask.getGoogleID());
                                        calendarTaskEntries.remove(employeeTask.getGoogleID());
                                    }
                                } else {
                                    // No need to sync since nothing has been changed since then
                                    googleTasksMap.remove(employeeTask.getGoogleID());
                                    calendarTaskEntries.remove(employeeTask.getGoogleID());
                                }
                            } else { // if Not contained in Google, that means it was removed from google, so remove from WFT
                                if (isDelete) {
                                    EdsTask task = employeeTask.getTask();
                                    logger.info("Task deleted while sync with google. TaskID: " + task.getObjectID() + "; employeeTaskID: " + employeeTask.getObjectID() + "; date: " + new Date());
                                    int size = task.getUnDeletedAssignments().size();
                                    employeeTaskManager.deleteEmployeeTask(employeeTask);
                                    size--;
                                    if (size == 0) {  // If task was assigned to himself only, so delete the whole task
                                        taskServiceLocal.deleteTask(task, user, null);
                                    }
                                }
                                //}
                            }
                        } else { // New task not yet synced with Google
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
                            Event eventEntry = googleCalendarManager.createCalendarEventEntry(user, appointment, null, employeeTask);
                            task.setLastUpdateTime(employeeTask.getLastModifiedDate());
                            tasksToAdd.add(eventEntry);
                        }
                    }

                    // Add new tasks from Google to WFT, or delete old WFT tasks from Google
                    for (Appointment appointment : googleTasksMap.values().toArray(new Appointment[]{})) {
                        if (appointment.getObjectID() != null) {
                            EdsEmployeeTask checkEmployeeTask = employeeTaskManager.get(appointment.getObjectID());
                            if (checkEmployeeTask == null || checkEmployeeTask.getDeleted()) {// if there is indeed no task in WFT then delete from Google. It might be that WFT task did not fall in the date range period we have specified, that's why double checking
                                Event taskEntry = calendarTaskEntries.get(appointment.getGoogleID());
                                if (taskEntry != null) {
                                    tasksToDelete.add(taskEntry);
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
                                newTask.setStartDate(sDate);
                                newTask.setDueDate(eDate);
                                newTask.setLastModified(appointment.getLastModifiedDate());
                                newTask.setGoogleID(appointment.getGoogleID());
                                newTask.setAllDay(appointment.isAllDay());

                                newTask.setPriorityID(referenceManager.findReference(EdsTask.TASK_PRIORITY, EdsTask.MEDIUM).getObjectID());
                                newTask.setStatusID(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED).getObjectID());

                                EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(user.getEmployee(), user.getCompany().getDefaultProject());
                                newTask.setProjectEmployees(new IdTime[]{new IdTime(projectEmployee.getObjectID(), 0, Float.valueOf("0.0"))});
                                newTask.setTaskCreatorID(userId);
                                SelectItem taskId = saveCalendarTask(newTask);
                                EdsTask task = taskManager.get(taskId.getId());
                                EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), user.getObjectID());
                                if (employeeTask != null) {
                                    employeeTask.setGoogleID(appointment.getGoogleID());
                                    employeeTask.setLastModifiedDate(appointment.getLastModifiedDate());
                                    employeeTaskManager.update(employeeTask);
                                }
                                Event taskEntry = calendarTaskEntries.get(appointment.getGoogleID());
                                Event.ExtendedProperties extendedProperties = new Event.ExtendedProperties();
                                Map<String, String> privateExtendedProperties = new HashMap<>();
                                privateExtendedProperties.put("employeeTask", String.valueOf(employeeTask.getObjectID()));
                                extendedProperties.setPrivate(privateExtendedProperties);
                                taskEntry.setExtendedProperties(extendedProperties);
                                tasksToEdit.add(taskEntry);
                            }
                        }
                    }

                    // this parts of code need for execute batch operation with google (for events)
                    if (eventsToAdd != null && eventsToAdd.size() > 0) {
                        runBatchOperation(user, calendarService, eventsToAdd.toArray(new Event[]{}), true, "insert");
                    }
                    if (eventsToEdit != null && eventsToEdit.size() > 0) {
                        runBatchOperation(user, calendarService, eventsToEdit.toArray(new Event[]{}), true, "update");
                    }
                    if (eventsToDelete != null && eventsToDelete.size() > 0) {
                        runBatchOperation(user, calendarService, eventsToDelete.toArray(new Event[]{}), true, "delete");
                    }

                    // this parts of code need for execute batch operation with google (for tasks)
                    if (tasksToAdd != null && tasksToAdd.size() > 0) {
                        runBatchOperation(user, calendarService, tasksToAdd.toArray(new Event[]{}), false, "insert");
                    }
                    if (tasksToEdit != null && tasksToEdit.size() > 0) {
                        runBatchOperation(user, calendarService, tasksToEdit.toArray(new Event[]{}), false, "update");
                    }
                    if (tasksToDelete != null && tasksToDelete.size() > 0) {
                        runBatchOperation(user, calendarService, tasksToDelete.toArray(new Event[]{}), false, "delete");
                    }

                    System.out.print("Ending synchronization for calendar for userId " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");
                    logger.info("Ending synchronization for calendar for userId " + userId + "; companyID: " + user.getCompany().getObjectID() + "\n");
                    ArrayList<Appointment> appointments = new ArrayList<>();
                    if (googleEventsMap != null && googleEventsMap.size() > 0) {
                        appointments.addAll(Arrays.asList(googleEventsMap.values().toArray(new Appointment[]{})));
                    }
                    eventManager.addToSolr(objectIDs.toArray(new Integer[]{}));
                    solrManager.deleteEvents(deletedObjectIDs.toArray(new Integer[]{}));
                    logger.info("appointments:" + appointments.size());
                    logger.info("=========================================GOOGLE CALENDAR SYNC ENDED=============================================");
                    return appointments;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    throw new IOException(ex.getMessage());
                } catch (GeneralSecurityException ex) {
                    ex.printStackTrace();
                    throw new GeneralSecurityException(ex.getMessage());
                } catch (AuthenticationException ex) {
                    ex.printStackTrace();
                    throw new AuthenticationException(ex.getMessage());
                } catch (ServiceException ex) {
                    ex.printStackTrace();
                    throw new ServiceException(ex);
                }
            } else {
                logger.info("The current user has no token or invalid token!!!");
            }
        }
        return new ArrayList<>();
    }

    private void normalizeGoogleAndWFTCalendarCollections(List<? extends CalendarObject> employeeCalendarObjects, Map<String, Appointment> googleCalendarObjectsMap, Map<String, Event> calendarEventEntries, com.google.api.services.calendar.Calendar calendarService, EdsUser user, Boolean isEvent, boolean isDelete) throws IOException {
        int kpiEvents = 0;
        int googleEvents = 0;
        if (employeeCalendarObjects != null) {
            kpiEvents = employeeCalendarObjects.size();
        }
        if (googleCalendarObjectsMap != null) {
            googleEvents = googleCalendarObjectsMap.values().size();
        }

        if (kpiEvents - googleEvents > 25) {//if more than 25 item to fetch from google use batch request
            logger.info("::::::::::::::::::::::::::::::::::::using batch request to normalize");
            String calendarID = null;
            EdsGoogleCalendar googleCalendar = googleCalendarManager.getGoogleCalendar(user, true);
            List<Event> eventList = null;
            if (googleCalendar != null) {
                if (isEvent) {
                    eventList = calendarService.events().list(googleCalendar.getCalendarID()).setMaxResults(5000).setSingleEvents(true).execute().getItems();

                } else {
                    eventList = calendarService.events().list(googleCalendar.getTaskCalendarID()).setMaxResults(5000).setSingleEvents(true).execute().getItems();
                }
            }

            //if some wft events/tasks can also be found in google but did not fall into googleCalendarObjectsMap and calendarEntries due to time range,
            // then we need to put them into googleCalendarObjectsMap and calendarEntries to avoid deletion from wft
            if (eventList != null && eventList.size() > 0) {
                for (CalendarObject employeeEvent : employeeCalendarObjects) {
                    for (Event calendarEvent : eventList) {
                        //if selected range of wft calendar has some events/tasks that are outside the selected range from google calendar, then we should also grab them and put them into Maps
                        if (employeeEvent.getGoogleID() != null && !googleCalendarObjectsMap.containsKey(calendarEvent.getId()) && employeeEvent.getGoogleID().equals(calendarEvent.getId())) {
                            googleCalendarObjectsMap.put(calendarEvent.getId(), googleCalendarManager.wrapCalendarEventToAppointment(user.getUserTimezone(), calendarEvent, isEvent));
                            calendarEventEntries.put(calendarEvent.getId(), calendarEvent);
                        }
                    }
                }
            }

        } else if (!isDelete) {
            logger.info("::::::::::::::::::::::::::::::::::::using request by ID to normalize");
            //if some wft events/tasks can also be found in google but did not fall into googleCalendarObjectsMap and calendarEntries due to time range,
            // then we need to put them into googleCalendarObjectsMap and calendarEntries to avoid deletion from wft
            int i = 0;
            for (CalendarObject employeeEvent : employeeCalendarObjects) {
                //if selected range of wft calendar has some events/tasks that are outside the selected range from google calendar, then we should also grab them and put them into Maps
                if (employeeEvent.getGoogleID() != null && !googleCalendarObjectsMap.containsKey(employeeEvent.getGoogleID())) {
                    Event calendarEvent = getCalendarEvent(calendarService, employeeEvent.getGoogleID(), user, isEvent);
                    i++;
                    if (calendarEvent != null) {
                        System.out.println("::::::::::::::::::::::" + i);
                        googleCalendarObjectsMap.put(calendarEvent.getId(), googleCalendarManager.wrapCalendarEventToAppointment(user.getUserTimezone(), calendarEvent, isEvent));
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

        //if some google events/tasks can also be found in wft, but did not fall into employeeCalendarObjects due to time range,
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
                                object.setGoogleId(appointment.getGoogleID());
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

    private Event getCalendarEvent(com.google.api.services.calendar.Calendar calendarService, String savedID, EdsUser user, Boolean isEvent) {
        Event eventList = null;
        EdsGoogleCalendar googleCalendar = googleCalendarManager.getGoogleCalendar(user, true);
        try {
            if (isEvent) {
                eventList = calendarService.events().get(googleCalendar.getCalendarID(), savedID).execute();
            } else {
                eventList = calendarService.events().get(googleCalendar.getTaskCalendarID(), savedID).execute();
            }

        } catch (IOException e) {
            logger.info("This " + (isEvent ? "event " : "task ") + e.getMessage());  //To change body of catch statement use File | Settings | File Templates.
        }
        return eventList;
    }

    /**
     * Executing batch operation with user's Google Calendar
     *
     * @param service        - CalendarService
     * @param eventsToAction - array of events to action (to add, edit, or remove)
     * @param isEvent        - when if true then it's event else task
     * @param action         - action: add, edit, or remove
     */
    private void runBatchOperation(EdsUser user, com.google.api.services.calendar.Calendar service, Event[] eventsToAction, boolean isEvent, String action) {
        // Add each item in eventsToAction to the batch request.
        try {
            JsonBatchCallback<Event> callback = new JsonBatchCallback<Event>() {

                public void onSuccess(Event event, HttpHeaders responseHeaders) throws IOException {
                    executeAouthBatchOperation(event);
                }

                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                    System.out.println("Error Message: " + e.getMessage());
                }
            };

            JsonBatchCallback<Void> deleteCallback = new JsonBatchCallback<Void>() {
                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders httpHeaders) throws IOException {
                    System.out.println("Error Message: " + e.getMessage());
                }

                @Override
                public void onSuccess(Void aVoid, HttpHeaders httpHeaders) throws IOException {
                }
            };
            // Get the URL to make batch requests to
            BatchRequest batch = service.batch();
            EdsGoogleCalendar googleCalendar = googleCalendarManager.getGoogleCalendar(user, true);

            if (googleCalendar != null) {
                for (Event toAction : eventsToAction) {
                    if (isEvent) {
                        if ("insert".equals(action)) {
                            service.events().insert(googleCalendar.getCalendarID(), toAction).queue(batch, callback);
                        } else if ("update".equals(action)) {
                            service.events().update(googleCalendar.getCalendarID(), toAction.getId(), toAction).queue(batch, callback);
                        } else if ("delete".equals(action)) {
                            service.events().delete(googleCalendar.getCalendarID(), toAction.getId()).queue(batch, deleteCallback);
                        }
                    } else {
                        if ("insert".equals(action)) {
                            service.events().insert(googleCalendar.getTaskCalendarID(), toAction).queue(batch, callback);
                        } else if ("update".equals(action)) {
                            service.events().update(googleCalendar.getTaskCalendarID(), toAction.getId(), toAction).queue(batch, callback);
                        } else if ("delete".equals(action)) {
                            service.events().delete(googleCalendar.getTaskCalendarID(), toAction.getId()).queue(batch, deleteCallback);
                        }
                    }
                }
                logger.info(":::::::::::::Started Batch Operation: " + action);
                logger.info(">>>>>>>>>>>>>> Batch Request Size: " + batch.size());
                batch.execute();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void executeAouthBatchOperation(Event event) throws IOException {
        Event.ExtendedProperties properties = event.getExtendedProperties();
        Map<String, String> property = properties.getPrivate();
        if (property != null && property.size() > 0) {
            for (String propertyKey : property.keySet()) {
                logger.info(">>>>>>>>>>>>>Property: " + property.get(propertyKey));
                if ("employeeEventID".equals(propertyKey)) {
                    EdsEmployeeEvent employeeEvent = employeeEventManager.get(Integer.valueOf(property.get(propertyKey)));
                    logger.info("Event ID: " + property.get(propertyKey));
                    if (employeeEvent != null) {
                        employeeEvent.setGoogleId(event.getId());
                        logger.info(">>>>>>>>>>>>>>> Google ID: " + event.getId());
                        employeeEvent.setLastModifiedDate(new Date(event.getUpdated().getValue()));
                        employeeEvent.getEvent().setLastModifiedDate(new Date());
                        employeeEventManager.update(employeeEvent);
                        eventManager.update(employeeEvent.getEvent());
                        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeEvent.getEvent(), userManager.getUser());
                        logger.info("Successfully executed batch request (insert/update) for event: " + employeeEvent.getEvent().getSubject() + "; employeeEventID: " + employeeEvent.getObjectID());
                    }
                    break;
                } else if ("employeeTaskID".equals(propertyKey)) {
                    EdsEmployeeTask employeeTask = employeeTaskManager.get(Integer.valueOf(property.get(propertyKey)));
                    logger.info("Task ID: " + property.get(propertyKey));
                    if (employeeTask != null) {
                        employeeTask.setGoogleID(event.getId());
                        logger.info(">>>>>>>>>>>>>>> Google ID: " + event.getId());
                        employeeTask.setLastModifiedDate(new Date(event.getUpdated().getValue()));
                        employeeTask.getTask().setLastModifiedDate(employeeTask.getLastModifiedDate());
                        employeeTaskManager.update(employeeTask);
                        logger.info("Successfully executed batch request (insert/update) for task: " + employeeTask.getTask().getName() + "; employeeTaskID: " + employeeTask.getObjectID());
                    }
                    break;
                }
            }
            logger.info("FINISHED UPDATING GoogleID properties");
        }
    }

    private CalendarEventFeed getCalendarFeed(CalendarService service, String calendarID) {
        CalendarEventFeed feed = null;
        calendarID = calendarID.replaceFirst("default/owncalendars/full/", "") + "/private/full?max-results=5000";
        URL eventFeedUrl = null;
        try {
            eventFeedUrl = new URL(calendarID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            feed = service.getFeed(eventFeedUrl, CalendarEventFeed.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (
                RedirectRequiredException e) {//If caught this means your request to Google Data APIs server is redirected more than once by a http proxy. If the redirection is valid, you can choose to follow the redirected url as follows:
            try {
                eventFeedUrl = new URL(e.getRedirectLocation());
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            }
            try {
                feed = service.getFeed(eventFeedUrl, CalendarEventFeed.class);
            } catch (IOException | ServiceException e1) {
                e1.printStackTrace();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        return feed;
    }


    /**
     * get Company employees IDs to share event's component
     *
     * @param eventID
     * @return
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.AppointmentShareView#loadTree
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<Integer> getEmployeesIDByEvent(Integer eventID) {
        return (ArrayList<Integer>) employeeEventManager.getEventRelatedEmployees(eventID);
    }

    /**
     * get conflicted employees list, while share event with other employees
     *
     * @param attendees
     * @param start
     * @param end
     * @param eventID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<SelectItem> getConflictedEmployees(ArrayList<Attendee> attendees, Date start, Date end, Integer eventID) {
        ArrayList<SelectItem> conflictedEmployees = new ArrayList<>();
        for (Attendee attendee : attendees) {
            EdsEmployee employee = employeeManager.get(attendee.getID());
            /**
             * We have to check to isSharing, because we are also receiving employees who are stopped from sharing.
             */
            if (!attendee.getID().equals(employeeManager.getUser().getObjectID())) {
                if (employee != null && attendee.isShared() && employeeEventManager.hasConflictedEvents(eventID, employee, start, end) != null) {
                    conflictedEmployees.add(new SelectItem(employee.getObjectID(), employee.getName()));
                }
            }
        }

        return conflictedEmployees;
    }

    /**
     * This method used for sending event reminder notification;
     *
     * @param eventId
     * @see com.edatasite.workforce.scheduler.CalendarEventRecurrenceJob#execute(org.quartz.JobExecutionContext)
     */
    public void sendEventNotification(Integer eventId, Integer reminderType) {
        try {
            EdsEvent event = eventManager.get(eventId);
            ArrayList<CalendarEventReminder> reminders = eventReminderManager.getReminders(eventId);
            if (event != null && reminders != null && !reminders.isEmpty()) {
                EdsEmployee owner = event.getOwner();
                List<EdsUser> userList = employeeEventManager.getEventAttendees(event);
                for (EdsUser user : userList) {
                    if (EMPLOYEE_STATUS_ACTIVE.equals(user.getAccountStatus().getCode())) {
                        EdsEmployeeEvent employeeEvent = employeeEventManager.getEmployeeEvent(user, event);
                        if (employeeEvent != null && user.getCompany().getActive() && !user.getDeleted()) {
                            if (reminderType.equals(Constants.E_MAIL)) {
                                messageManager.sendEventReminder(employeeEvent);
                            } else if (reminderType.equals(Constants.SMS)) {
                                messageManager.sendEventReminderSms(employeeEvent);
                            } else if (reminderType.equals(Constants.PUSH_NOTIFICATION)) {
                                messageManager.sendEventReminderNotification(employeeEvent);
                            }
                        }
                    }
                }
                if (reminderType.equals(Constants.E_MAIL)) {
                    List<EdsRelation> edsRelationList = relationManager.getAllRelations(EdsRelation.TYPE_EVENT, event.getObjectID());
                    if (edsRelationList != null && !edsRelationList.isEmpty()) {
                        edsRelationList.forEach(c -> {
                            if ("candidate".equals(c.getToType()) && c.getToID() != null) {
                                EdsEmailSetting companyEmailSetting = emailSettingsManager.getCompanyEmailSetting(SecurityContext.getCompanyID());
                                String subject = commonLocalizer.localize("EVENT_REMINDER") + ": " + event.getSubject() + " - " + getDate(event, owner);
                                Map<String, Object> values = new TreeMap<>();
                                EdsCrmContact contact = crmContactManager.get(c.getToID());
                                values.put("CREATOR", owner);
                                values.put("EVENT_NAME", event.getSubject());
                                values.put("DATE", getDate(event, owner));
                                values.put("WHERE", getLocation(event.getVenue()));
                                values.put("DESCRIPTION", getDescription(event.getDescription()));
                                String guests = messageManager.getEventGuests(event);
                                values.put("GUESTS", !"".equals(guests) ? "<p>" + commonLocalizer.localize("guests") + " : " + guests + "</p>" : "");
                                values.put("LINK", EdsContextParams.getHost(owner.getCompany().getObjectID()) + "/Crm.html#event|summary/" + event.getObjectID().toString());

                                try {
                                    String text = EdsTemplates.processTemplate(owner, values, EdsTemplates.CALENDAR_ADD_EVENT_REMINDER);
                                    messageManager.registerInternalMessageBasic(companyEmailSetting.getEmail(), contact.getPrimaryEmail(), subject, text, SecurityContext.getCompanyID());
                                } catch (EdsTemplateException | EdsDbException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }

                EdsRecurrence recurrence = recurrenceManager.getRecurrenceJob(CALENDAR_EVENT_REMINDER, eventId, event.getOwner().getCompany().getObjectID());
                if (event.getRecurrenceID() != null) {
                    if (recurrence != null) {
                        List<EdsEvent> eventList = eventManager.getAllEventInstancesAfter(event.getRecurrenceID(), event.getStartDate());
                        if (eventList != null && !eventList.isEmpty()) {
                            int count = 0;
                            for (EdsEvent edsEvent : eventList) {
                                EdsGoogleCalendarReminder nextEventReminder = eventReminderManager.get(edsEvent.getObjectID());
                                if (nextEventReminder != null) {
                                    Date date = (Date) nextEventReminder.getEvent().getStartDate().clone();
                                    date.setMinutes(date.getMinutes() - nextEventReminder.getMinutes());
                                    if (date.after(new Date())) {
                                        recurrence.setStartDate(date);
                                        recurrenceService.reLoadTrigger(recurrence);
                                        count++;
                                        break;
                                    }
                                }
                            }
                            if (count == 0) {
                                recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
                                recurrenceService.updateRecurrence(recurrence, true, true);
                            }
                        } else {
                            recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
                            recurrenceService.updateRecurrence(recurrence, true, true);
                        }
                    }
                } else {
                    if (recurrence != null) {
                        recurrenceService.removeTriggerFromScheduler(recurrence.getObjectID());
                        recurrenceService.updateRecurrence(recurrence, true, true);
                    }
                }
            }
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    private String getDescription(String description) {
        return description != null ? description : "<i>You have no description for this event.</i>";
    }

    private String getLocation(String location) {
        return location != null ? location : "<i>No location appointment for this event.</i>";
    }

    private String getDate(EdsEvent event, EdsUser owner) {
        EdsCompany company = owner.getCompany();
        TimeZone timeZone = owner.getUserTimezone();
        Date start = (Date) event.getStartDate().clone();
        Date end = (Date) event.getEndDate().clone();
        Date startDate = new Date(start.getYear(), start.getMonth(), start.getDate(), start.getHours(), start.getMinutes() + (timeZone.getRawOffset() / 60000), start.getSeconds());
        Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate(), end.getHours(), end.getMinutes() + (timeZone.getRawOffset() / 60000), end.getSeconds() + 1);
        final EdsCompanySettings edsCompanySettings = company.getCompanySettings();
        SimpleDateFormat longDateFormat = new SimpleDateFormat(edsCompanySettings != null ? edsCompanySettings.getLongDateFormat() : "MMM dd, yyyy [HH:mm]");
        SimpleDateFormat shortDateFormat = new SimpleDateFormat(edsCompanySettings != null ? edsCompanySettings.getShortDateFormat() : "MMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String startTime = timeFormat.format(startDate).toLowerCase();
        String endTime = timeFormat.format(endDate).toLowerCase();
        String dateString = "";
        if (event.isMultiDayAppointment()) {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate) + " - " + shortDateFormat.format(endDate);
            } else {
                dateString = longDateFormat.format(startDate) + " - " + longDateFormat.format(endDate);
            }
        } else {
            if (event.isAllDay()) {
                dateString = shortDateFormat.format(startDate);
            } else {
                dateString = shortDateFormat.format(startDate) + "," + startTime + " - " + endTime;
            }
        }
        return dateString + " (" + owner.getUserTimezone().getID() + ")";
    }

    /**
     * When open event short view, method get employee names shared this event
     *
     * @param eventID
     * @param creator
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getEventSharedEmployees(Integer eventID, String creator) {
        List<EdsUser> sharedEmployees = employeeEventManager.getEventSharedEmployees(eventID);
        StringBuilder userNames = new StringBuilder();
        for (EdsUser employee : sharedEmployees) {
            if (creator != null && !employee.getFullName().equals(creator)) {
                userNames.append(employee.getFullName());
                if (!sharedEmployees.get(sharedEmployees.size() - 1).equals(employee)) {
                    userNames.append(", ");
                }
            }
        }
        return userNames.toString();
    }

    /**
     * deleting task from UI
     *
     * @param employeeTaskID
     * @param deleteType     - only this instance, all tasks in the series, all following
     */
    public Boolean deleteTask(Integer employeeTaskID, String deleteType) {
        return taskService.deleteTask(taskManager.getUser().getObjectID(), employeeTaskID, deleteType);
    }

    /**
     * get task priorities for UI
     *
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getPriorities() {
        return taskService.getPriorities();
    }

    /**
     * used for reLoading project assignees in UI
     *
     * @param projectID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<WfmTreeItem> getAssigneesWithPositions2(Integer projectID) {
        PositionsSelectItem[] results = taskService.getAssigneesWithPositions1(projectID);
        LinkedList<WfmTreeItem> members = new LinkedList<>();
        for (PositionsSelectItem result : results) {
            WfmTreeItem member = new WfmTreeItem();
            member.setId(result.getId());
            member.setName(result.getName());
            member.setChecked(true);
            members.add(member);
        }
        return members;
    }

    /**
     * get only available employees to assign task in Calendar
     *
     * @param projectID
     * @param startDate
     * @param endDate
     * @return
     * @see com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.task.AddTaskDetailedView#reloadOnlyAvailableAssignees()
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LinkedList<WfmTreeItem> getOnlyAvailableAssigneesWithPosition1(Integer projectID, Date startDate, Date endDate) {
        PositionsSelectItem[] results = taskService.getOnlyAvailableAssigneesWithPosition1(projectID, startDate, endDate);
        LinkedList<WfmTreeItem> members = new LinkedList<>();
        for (PositionsSelectItem result : results) {
            WfmTreeItem member = new WfmTreeItem();
            member.setId(result.getId());
            member.setName(result.getName());
            members.add(member);
        }
        return members;
    }

    /**
     * get Task assignees to task summary view in Calendar
     *
     * @param taskId
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<IdTime> getTaskAssignees(Integer taskId) {
        ArrayList<IdTime> result = new ArrayList<>();
        EdsTask task = taskManager.get(taskId);
        Set<EdsEmployeeTask> employeeTasks = task.getUnDeletedAssignments();
        for (EdsEmployeeTask employeeTask : employeeTasks) {
            IdTime attendee = new IdTime(employeeTask.getProjectEmployee().getObjectID(), employeeTask.getEstimatedTime());
            attendee.setEmployeeName(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getFullName());
            attendee.setPercent(employeeTask.getPercent());
            result.add(attendee);
        }
        return result;
    }

    /**
     * update all recurring event instances
     *
     * @param event
     * @param allFollowing
     * @param startDateDiff
     * @param endDateDiff
     * @param objectIDs
     */
    private void updateAllEventInstances(EdsEvent event, boolean allFollowing, long startDateDiff, long endDateDiff, HashSet<Integer> objectIDs) {
        if (event.getRecurrenceID() != null) {
            List<EdsEvent> allEventInstances = new ArrayList<>();
            // getting recurring events in this recurring series and update all data
            if (allFollowing) {
                allEventInstances = eventManager.getAllEventInstancesAfter(event.getRecurrenceID(), event.getFireTime());
            } else {
                allEventInstances = eventManager.getAllEventInstances(event.getRecurrenceID());
            }
            Appointment appointment = wrapEdsEventToAppointment(event);
            for (EdsEvent eventInstance : allEventInstances) {
                if (!eventInstance.getObjectID().equals(event.getObjectID())) {
                    appointment.setStartDate(new Date(eventInstance.getStartDate().getTime() + startDateDiff));
                    appointment.setEndDate(new Date(eventInstance.getEndDate().getTime() + endDateDiff));
                    appointment.setFireTime(eventInstance.getFireTime());
                    wrapAppointmentToEdsEvent(appointment, eventInstance, event.getOwner(), false);
                    eventManager.update(eventInstance);
                    objectIDs.add(eventInstance.getObjectID());
                    updateEventAssignees(eventInstance, appointment, event.getOwner());
                    createOrUpdateEventGuests(appointment, event);
                }
            }
        }
    }

    /**
     * Creating recurring events after saving base event
     *
     * @param event
     * @param recurrence
     * @param objectIDs
     */
    private void createEventRecurringInstances(EdsEvent event, EdsRecurrence recurrence, HashSet<Integer> objectIDs, Integer reminderRecurrenceID) {
        List<Date> recurringDates = recurrenceService.getRecurringDates(recurrence);
        createEventRecurringInstances(event, recurrence, recurringDates, objectIDs, false, reminderRecurrenceID, true);
    }

    /*Recurring event yaratilganda agar instance lari soni CREATE_EVENT_LIMIT dan ko`p bo`lsa,
     unda birinchi CREATE_EVENT_LIMIT tasini yaratadi va qolganlarini BG da createRecurringEventInstancesTrigger triggeri yordamida yaratiladi;*/
    @Transactional
    public void createEventRecurringInstances(EdsEvent event, EdsRecurrence recurrence, List<Date> recurringDates, HashSet<Integer> objectIDs, boolean isFeaturedInstances, Integer reminderRecurrenceID, boolean registerWorkFlowEventPerDate) {
        List<Date> recDates = recurringDates;
        if (recurringDates != null && !recurringDates.isEmpty()) {
            if (recurringDates.size() > CREATE_EVENT_LIMIT) {
                if (isFeaturedInstances) {
                    recDates = recurringDates.subList(0, CREATE_EVENT_LIMIT);
                } else {
                    recDates = recurringDates.subList(1, CREATE_EVENT_LIMIT);
                }
                recurrence.setBusObjectParams(String.valueOf(recurringDates.size() > CREATE_EVENT_LIMIT ? recurringDates.size() - CREATE_EVENT_LIMIT : recurringDates.size()));
                recurrence.setExtendDate(recurringDates.get(CREATE_EVENT_LIMIT - CREATE_EVENT_INDEX));
            } else {
                if (!isFeaturedInstances) {
                    recDates = recurringDates.subList(1, recurringDates.size());
                }
                recurrence.setBusObjectParams(null);
                recurrence.setExtendDate(null);
            }
            recurrenceManager.update(recurrence);
            createEventInstance(event, taskManager.getUser(), recDates, objectIDs, reminderRecurrenceID, registerWorkFlowEventPerDate);
            if (recurringDates.size() <= CREATE_EVENT_LIMIT) {
                recurrence.setExtendDate(null);
            }
        }
    }

    /**
     * Creating recurring events with base event's recurring
     *
     * @param event
     * @param user
     * @param recurringDates
     * @param objectIDs
     */
    private void createEventInstance(EdsEvent event, EdsUser user, List<Date> recurringDates, HashSet<Integer> objectIDs, Integer reminderRecurrenceID, boolean registerWorkflowEventPerDate) {
        int flushLimit = 10;
        int flushCount = 0;

        long dateDiff = event.getEndDate().getTime() - event.getStartDate().getTime();
        for (Date recurringDate : recurringDates) {
            Appointment appointment = wrapEdsEventToAppointment(event);
            appointment.setRegisterWorkFlowEventPerDate(registerWorkflowEventPerDate);
            appointment.setObjectID(null);
            appointment.setRecurrenceJobItem(null);
            appointment.setStartDate(recurringDate);
            appointment.setEndDate(new Date(recurringDate.getTime() + dateDiff));
            appointment.setFireTime(recurringDate);
            for (Attendee attendee : appointment.getAttendees()) {
                attendee.setGoogleID(null);
            }
            ArrayList<CalendarEventReminder> reminders = eventReminderManager.getReminders(event.getObjectID());
            ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
            if (recurringDate.after(new Date()) && reminders != null && reminders.size() > 0) {
                for (CalendarEventReminder eventReminder : reminders) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    cer.setValue(eventReminder.getValue());
                    cer.setReminderTimes(eventReminder.getReminderTimes());
                    eventReminders.add(cer);
                }
                appointment.setReminder(eventReminders);
            }
            saveCalendarEvent(user, appointment, false, false, true, objectIDs, reminderRecurrenceID);
            createOrUpdateEventGuests(appointment, event);
            flushCount++;
            if (flushCount == flushLimit) {
                eventReminderManager.flushAndClear();
                flushCount = 0;
            }
        }
    }

    // deleting recurring event

    public Boolean deleteEvent(Integer employeeID, Integer eventID, String deleteType, boolean deleteWithNotify) {
        Boolean isFullRefreshNeeded = false;
        EdsEvent event = eventManager.get(eventID);
        EdsEvent lastEvent = null;
        EdsEvent firstEvent = null;
        if (event.getRecurrenceID() != null) {
            lastEvent = eventManager.getFirstOrLastEventInRecurringSeries(event.getRecurrenceID(), false);
            firstEvent = eventManager.getFirstOrLastEventInRecurringSeries(event.getRecurrenceID(), true);
        }
        EdsUser user = (employeeID != null) ? userManager.get(employeeID) : userManager.getUser();
        event.setLastModifiedDate(new Date());
        event.setLastModifiedBy(user);
        event.setLastUpdateTime(new Date());
        if (deleteType == null || Constants.DELETE_THIS_INSTANCE.equals(deleteType)) {
            employeeEventManager.deleteEmployeeEvents(event);
            baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, event, user);

            //if the event is the last or first in the series, then it is needed to change recurrence end date to the previous events end date
            //or set new occurence count
            if (lastEvent != null && lastEvent.getObjectID().intValue() == event.getObjectID().intValue()) {
                EdsRecurrence rec = recurrenceManager.get(event.getRecurrenceID());
                if (rec.getEndType().equals(END_BY_DATE)) {
                    Date newEndDate = eventManager.getRecurringEventFirstOrLastDate(event.getRecurrenceID(), event.getFireTime(), false);
                    rec.setEndDate(newEndDate);
                } else if (rec.getEndType().equals(END_AFTER_OCCURRENCES)) {
                    rec.setOccurrence(eventManager.getAllEventInstancesSize(event.getRecurrenceID()).intValue());
                }
                isFullRefreshNeeded = true;
            } else if (firstEvent != null && firstEvent.getObjectID().intValue() == event.getObjectID().intValue()) {
                EdsRecurrence rec = recurrenceManager.get(event.getRecurrenceID());
                Date newStartDate = eventManager.getRecurringEventFirstOrLastDate(event.getRecurrenceID(), event.getFireTime(), true);
                rec.setStartDate(newStartDate);
                if (rec.getEndType().equals(END_AFTER_OCCURRENCES)) {
                    rec.setOccurrence(eventManager.getAllEventInstancesSize(event.getRecurrenceID()).intValue());
                }
                isFullRefreshNeeded = true;
            }
            event.setRecurrenceID(null);
        } else if (Constants.DELETE_ALL_SERIES.equals(deleteType)) {
            deleteAllInstances(event, user.getObjectID(), false);
            if (event.getRecurrenceID() != null) {
                EdsRecurrence recurrence = recurrenceManager.get(event.getRecurrenceID());
                recurrenceService.updateRecurrence(recurrence, true, true);
            }
        } else if (Constants.DELETE_ALL_FOLLOWING.equals(deleteType)) {
            deleteAllInstances(event, user.getObjectID(), true);
            isFullRefreshNeeded = true;
        }
        List<EdsRecurrence> recurrence = recurrenceManager.getRecurrenceJobList(CALENDAR_EVENT_REMINDER, eventID, user.getCompany().getObjectID());
        if (recurrence != null && recurrence.size() > 0) {
            for (EdsRecurrence rec : recurrence) {
                recurrenceService.updateRecurrence(rec, true, true);
            }
        }

        employeeEventManager.deleteEmployeeEvents(event);
        baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, event, user);
        ArrayList<Integer> eventIDs = new ArrayList<>();
        eventIDs.add(eventID);
        if (Constants.DELETE_THIS_INSTANCE.equals(deleteType) || deleteType == null) {
            event.setDeleted(true);
            eventManager.update(event, true);
            baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, event, user);
        } else if (Constants.DELETE_ALL_SERIES.equals(deleteType)) {
            eventIDs.addAll(eventManager.getAllEventInstancesIDs(eventID, null));
        } else if (Constants.DELETE_ALL_FOLLOWING.equals(deleteType)) {
            eventIDs.addAll(eventManager.getAllEventInstancesIDs(eventID, event.getFireTime()));
        }
        eventGuestsManager.deleteCalendarEventGuests(eventIDs);
        eventReminderManager.deleteEventRemindersByEventIDs(eventIDs);
        try {
            solrManager.removeCompanyEventByIds(event.getObjectID());
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
        logger.info("Deleted event: " + event.getSubject() + "; eventID: " + event.getObjectID() + "; deleted by: " + employeeID + "; companyID: " + user.getCompany().getObjectID() + "; date: " + new Date());
        return isFullRefreshNeeded;
    }

    /**
     * Will delete all event instances in the recurring series but will leaves the events marked as Exception
     * <br><b>Note:</b> Exception events will be taken out of recurrence and will become normal non-recurring events
     *
     * @param event
     * @param userID
     */
    public void deleteAllInstances(EdsEvent event, Integer userID, boolean allFollowing) {
        if (event.getRecurrenceID() != null) {
            List<Integer> eventIDs = null;
            if (allFollowing) {
                eventIDs = eventManager.getAllEventInstancesIDs(event.getRecurrenceID(), event.getFireTime());
                eventManager.deleteEvents(event.getRecurrenceID(), userID, event.getFireTime());
                baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, event, userManager.getUser());
                eventReminderManager.deleteEventRemindersByEventIDs(eventIDs);
                //we need also update recurrence info end date or occurence count
                EdsRecurrence rec = recurrenceManager.get(event.getRecurrenceID());
                if (rec.getEndType().equals(END_BY_DATE)) {
                    Date newEndDate = eventManager.getRecurringEventFirstOrLastDate(event.getRecurrenceID(), event.getFireTime(), false);
                    rec.setEndDate(newEndDate);
                } else if (rec.getEndType().equals(END_AFTER_OCCURRENCES)) {
                    rec.setOccurrence(eventManager.getAllEventInstancesSize(event.getRecurrenceID()).intValue());
                }
                event.setRecurrenceID(null);
            } else {
                eventIDs = eventManager.getAllEventInstancesIDs(event.getRecurrenceID(), null);
                eventManager.deleteEvents(event.getRecurrenceID(), userID, null);
                baseEventPostProcessor.registerEvent(ActivityEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_DELETE, event, userManager.getUser());
                eventReminderManager.deleteEventRemindersByEventIDs(eventIDs);
            }
            try {
                solrManager.deleteEvents(eventIDs.toArray(new Integer[]{}));
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update event's guust's invitation status;
     * When guest clicked any option from email, then update guest's status in system
     *
     * @param companyID - event owner's company ID
     * @param eventId   - current event's ID
     * @param email     - guest's email address
     * @param answer    - guest's selected option (Yes, Maybe or No)
     */
    public void updateEventGuestStatus(Integer companyID, Integer eventId, String email, String answer) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        eventGuestsManager.updateEventGuestStatus(eventId, email, answer);
        try {
            messageManager.sendMailToOwnerAboutGuestsStatus(eventId, email, answer);
            registerUpdates(eventId, email);//register my updates;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ServerSecurityContext.getInstance().removeCompanyId();
    }

    /**
     * Register for My Updates to business event;
     *
     * @param eventId - registration eventID;
     * @param email   - quest's email;
     */
    private void registerUpdates(Integer eventId, String email) {
        List<EdsGoogleCalendarEventGuests> eventGuestsList = eventGuestsManager.getEventGuestsByEmail(eventId, email);
        if (eventGuestsList != null && eventGuestsList.size() > 0) {
            for (EdsGoogleCalendarEventGuests eventGuest : eventGuestsList) {
                if (eventGuest != null && eventGuest.getEvent() != null && eventGuest.getEvent().getOwner() != null) {
                    baseEventPostProcessor.registerEvent(CalendarEventGuestCustomEventListenerImpl.TYPE, CalendarEventGuestCustomEventListenerImpl.CALENDAR_EVENT_GUEST_STATUS_CHANGE, eventGuest, eventGuest.getEvent().getOwner());
                }
            }
        }
    }

    /**
     * Getting event related attachments
     *
     * @param eventID
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public FileResource[] getEventAttachments(Integer eventID) {
        if (eventID != null) {
            EdsEvent event = eventManager.get(eventID);
            EdsFolder eventFolder = folderManager.getFolderByFolderType(EdsFolder.F_EVENT);
            if (event != null && eventFolder != null) {
                List<FileResource> eventAttachments = attachmentUtilsManager.getAttachments(F_EVENT, eventFolder.getObjectID(), event.getObjectID());
                if (eventAttachments != null && !eventAttachments.isEmpty()) {
                    return eventAttachments.toArray(new FileResource[]{});
                }
            }
        }
        return null;
    }

    public Appointment getAppointment(Integer objectID, boolean isCopy) {
        if (objectID != null) {
            EdsEvent event = eventManager.get(objectID);
            if (event != null) {
                return wrapEdsEventToAppointment(event, false, true, isCopy);
            }
        }
        return null;
    }

    public Appointment getAppointmentByAsteriskid(String asteriskid) {
        if (StringUtils.isNotBlank(asteriskid)) {
            EdsEvent event = eventManager.getByAsteriskid(asteriskid);
            if (event != null) {
                return wrapEdsEventToAppointment(event, false, true, false);
            }
        }
        return null;
    }

    public SelectItem[] getLocationAsSelectItem() {
        ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
        return locationManager.getLocationsAsSelectItems(listingFilterParameter);
    }

    @Override
    public String isAssigneeOnHoliday(ArrayList<Attendee> attendees, Date startDate, Date endDate, boolean isAllDay) { //todo check this method
        EdsUser user = userManager.getUser();
        startDate = new Date(startDate.getTime() + user.getUserTimezone().getRawOffset());
        endDate = new Date(endDate.getTime() + user.getUserTimezone().getRawOffset());
        if (isAllDay) {
            startDate = new Date(startDate.getYear(), startDate.getMonth(), startDate.getDate(), 0, 0, 0);
            endDate = new Date(endDate.getYear(), endDate.getMonth(), endDate.getDate(), 23, 59, 59);
        }
        StringBuilder users = new StringBuilder();
        boolean isFirst = true;
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        fp.setReasonCode(Constants.LR_STATUS_SS_APPROVED);
        fp.setStartDate(startDate);
        fp.setEndDate(endDate);

        for (Attendee attendee : attendees) {
            if (attendee.getID() != null) {
                int employeeId = attendee.getID();
                fp.setEmployeeId(employeeId);
                EdsEmployee employee = employeeManager.get(employeeId);
                if (employee != null) {
                    List<EdsHoliday> holidays = holidayManager.getHolidaysByDatesAndLocation(startDate, endDate, employee.getLocation());
                    ListResult<LeaveRequestLisItem> listRequests = availabilityServiceLocal.getLeaveRequestList(fp);
                    if ((holidays != null && !holidays.isEmpty()) || (listRequests != null && !listRequests.getList().isEmpty())) {
                        if (isFirst) {
                            users = new StringBuilder(employee.getFullName());
                            isFirst = false;
                        } else {
                            users.append(", ").append(employee.getFullName());
                        }
                    }
                }
            }
        }
        //returns User names, that has holiday or LR on event period
        return users.toString();
    }

    @Override
    public void saveCalendarSyncSettings(boolean syncFromDefaultCalendar) {
        EdsUser user = userManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings == null) {
            userSettings = new EdsUserEmailSettings();
            userSettings.setUser(user);
        }
        userSettings.setSyncFromDefaultCalendar(syncFromDefaultCalendar);
        userEmailSettingsManager.createOrUpdate(userSettings);
    }

    @Override
    public ArrayList<Integer> getSelectedEmployees() {
        EdsUser user = userManager.getUser();
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        List<EdsSelectedEmployeeFromCalendar> employeeFromCalendars = calendarManager.getByUser(user);
        if (employeeFromCalendars != null && !employeeFromCalendars.isEmpty()) {
            for (EdsSelectedEmployeeFromCalendar selectedEmployeeFromCalendar : employeeFromCalendars) {
                employeeIDs.add(selectedEmployeeFromCalendar.getSelectedUser().getObjectID());
            }
        }
        return employeeIDs;
    }

    @Override
    public ArrayList<Appointment> syncEvents(String host, Integer userId, Date start, Date end) {
        try {
            return calendarService.syncEvents(host, userId, start, end);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addEmployeeToEvent(Integer eventId, Integer employeeId) {
        if (eventId == null || employeeId == null) {
            return;
        }
        EdsEvent edsEvent = eventManager.get(eventId);
        if (edsEvent == null) {
            return;
        }
        Appointment appointment = new Appointment();
        ArrayList<Attendee> attendees = Lists.newArrayList();
        List<EdsEmployeeEvent> edsEmployeeEvents = employeeEventManager.getEmployeeEvents(edsEvent.getObjectID());
        for (EdsEmployeeEvent employeeEvent : edsEmployeeEvents) {
            Attendee attendee = new Attendee();
            EdsUser employee = employeeEvent.getEmployee();
            attendee.setID(employee.getObjectID());
            attendee.setName(employee.getFullName());
            attendee.setGoogleID(employeeEvent.getGoogleID());
            attendee.setOfficeID(employeeEvent.getOfficeID());
            if (employeeEvent.getGoogleID() != null && !"".equals(employeeEvent.getGoogleID().trim())) {
                appointment.setHasGoogleAccount(true);
            }
            attendee.setShared(employeeEvent.isShared());
            attendees.add(attendee);
        }
        attendees.add(new Attendee(employeeId, true));
        appointment.setAttendees(attendees);

        this.updateEventAssignees(edsEvent, appointment, null);
    }
}
