package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.core.solr.document.EventSolrDoc;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarReminderManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.utils.SolrFacetUtils;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ConvertUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.helper.RelationHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.EventDto;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.ReminderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment.CALL_LOG;
import static com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment.TYPE_EVENT;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.END_AFTER_OCCURRENCES;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.END_BY_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.NO_END_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_DAILY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_MONTHLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_WEEKLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_YEARLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRING_EVENT;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiActivityService implements Constants {
    private final CrmServiceLocal crmServiceLocal;
    private final EmployeeManager employeeManager;
    private final CommonServiceLocal commonServiceLocal;
    private final CRMService crmService;
    private final EventManager eventManager;
    private final GoogleCalendarServiceLocal googleCalendarServiceLocal;
    private final GoogleCalendarReminderManager googleCalendarReminderManager;
    private final RelationHelperV3 relationHelper;
    private final EventSolrComponent eventSolrComponent;

    @Autowired
    public ApiActivityService(CrmServiceLocal crmServiceLocal, EmployeeManager employeeManager, CommonServiceLocal commonServiceLocal, CRMService crmService, EventManager eventManager, GoogleCalendarServiceLocal googleCalendarServiceLocal, GoogleCalendarReminderManager googleCalendarReminderManager, RelationHelperV3 relationHelper, EventSolrComponent eventSolrComponent) {
        this.crmServiceLocal = crmServiceLocal;
        this.employeeManager = employeeManager;
        this.commonServiceLocal = commonServiceLocal;
        this.crmService = crmService;
        this.eventManager = eventManager;
        this.googleCalendarServiceLocal = googleCalendarServiceLocal;
        this.googleCalendarReminderManager = googleCalendarReminderManager;
        this.relationHelper = relationHelper;
        this.eventSolrComponent = eventSolrComponent;
    }

    public ListResultTO<EventDto> getEventList(ListingFilterParameter fp) {
        FacetFilterRpc eventFacetFilter = fp.getFacetFilter();
        if (eventFacetFilter != null && !eventFacetFilter.isFilterChanges()) {
            eventFacetFilter = commonServiceLocal.getUserFacetFilter(eventFacetFilter);
        }
        EdsUser edsUser = employeeManager.getUser();
        EdsCompany edsCompany = edsUser.getCompany();
        StringBuilder solrQuery = new StringBuilder();
        solrQuery.append(crmServiceLocal.getEventCoreSolrQuery(edsUser, eventFacetFilter, fp));
        solrQuery.append(SolrFacetUtils.generatedFacetFilterSolrQuery(eventFacetFilter, edsCompany, SolrEventRepresenter.FIELD_START_DATE, SolrEventRepresenter.FIELD_END_DATE));

//        SolrClient server = WfmJpaTemplate.getSolrServerForCore(SOLR_EVENT_CORE);
//        QueryResponse resp = null;
//        try {
//            resp = server.query(crmServiceLocal.getEventSolrQuery(fp, solrQuery.toString()), SolrRequest.METHOD.POST);
//        } catch (SolrServerException | IOException e) {
//            e.printStackTrace();
//        }
        Page<EventSolrDoc> eventSolrDocPage = eventSolrComponent.getList(fp, solrQuery.toString());

        ListResultTO<EventDto> listResultTO = new ListResultTO<>();
        if (eventSolrDocPage != null) {
            List<Integer> ids = eventSolrDocPage.getContent().stream().map(doc -> Integer.valueOf(Objects.requireNonNull(doc.getEventId()))).toList();
            listResultTO.setTotalNumber(ids.size());
            ArrayList<EventDto> events = new ArrayList<>();
            ids.forEach(id -> {
                EventItem item = crmService.getEvent(id);
                Optional.ofNullable(googleCalendarReminderManager.getReminders(id)).ifPresent(item::setReminder);
                events.add(ConvertUtils.toDto(item));
            });
            listResultTO.setItems(events);
        }

        return listResultTO;
    }

    @Transactional(readOnly = true)
    public EventDto getEventById(Integer id) throws RestException {
        Optional.ofNullable(eventManager.get(id)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));
        EventItem item = crmService.getEvent(id);
        return ConvertUtils.toDto(item);
    }

    @Transactional
    public void save(final EventDto event, boolean isNew) throws RestException {
        Appointment appointment;
        if (!isNew && event.getId() != null) {
            appointment = Optional.ofNullable(googleCalendarServiceLocal.getAppointment(event.getId(), false)).orElseThrow(() -> new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Event with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));
        } else {
            appointment = new Appointment();
        }
        appointment.setSubject(event.getSubject());
        if (event.getGuests() != null && event.getGuests().size() > 0) {
            ArrayList<SelectItem> guests = new ArrayList<>();
            event.getGuests().forEach(email -> guests.add(new SelectItem(0, email, EVENT_GUEST_STATUS_PENDING)));
            appointment.setGuests(guests);
        }
        appointment.setStartDate(event.getStartDate());
        appointment.setEndDate(event.getEndDate());

        if (event.getShares() != null && event.getShares().size() > 0) {
            ArrayList<PositionsSelectItem> shares = new ArrayList<>();
            for (IdCode share : event.getShares()) {
                PositionsSelectItem positionsSelectItem = new PositionsSelectItem();
                positionsSelectItem.setEmployeeId(share.getId());
                positionsSelectItem.setName(share.getCode());
                EdsEmployee edsEmployee = employeeManager.get(share.getId());
                if (edsEmployee != null) {
                    if (edsEmployee.getPosition() != null) {
                        positionsSelectItem.setPositionName(edsEmployee.getPosition().getName());
                    }
                    if (edsEmployee.getEmployeeTeam() != null && edsEmployee.getEmployeeTeam().getTeam() != null) {
                        positionsSelectItem.setDepartmentName(edsEmployee.getEmployeeTeam().getTeam().getName());
                    }
                }
                shares.add(positionsSelectItem);
            }
            appointment.setSharedEmployees(shares);
        }
        appointment.setDescription(event.getDescription());

        if (event.getReminders() != null) {
            ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
            for (ReminderDto reminder : event.getReminders()) {
                if (reminder != null && reminder.getTimes() != null && reminder.getTimes() > 0) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    cer.setValue(reminder.getType());

                    cer.setReminderTimes(reminder.getTimes());
                    eventReminders.add(cer);
                }
            }
            if (!eventReminders.isEmpty()) {
                appointment.setReminder(eventReminders);
            }
        }

        EdsEvent edsEvent = null;
        if (!isNew) {
            edsEvent = eventManager.get(event.getId());
        }
        appointment.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(event.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Event), !isNew ? edsEvent.getEventCustomFields() : null));

        appointment.setRecurring(event.isRecurrence());
        if (event.isRecurrence() && (event.getRecurrenceType() != null || event.getRecurrenceStartDate() != null || event.getRecurrenceEndDate() != null)) {
            RecurrenceJobItem recurrence = new RecurrenceJobItem();

            if (event.getRecurrenceType() != null) {

                recurrence.setJobType(RECURRING_EVENT);
                recurrence.setStartDate(event.getRecurrenceStartDate());
                if (event.getRecurrenceType().getDaily() != null) {
                    recurrence.setType(RECURRENCE_TYPE_DAILY);
                    recurrence.setInterval(event.getRecurrenceType().getDaily().getInterval());
                    recurrence.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                } else if (event.getRecurrenceType().getWeekly() != null) {
                    recurrence.setType(RECURRENCE_TYPE_WEEKLY);
                    for (Integer day : event.getRecurrenceType().getWeekly().getDays()) {
                        switch (day) {
                            case 0:
                                recurrence.setMonday(true);
                            case 1:
                                recurrence.setTuesday(true);
                            case 2:
                                recurrence.setWednesday(true);
                            case 3:
                                recurrence.setThursday(true);
                            case 4:
                                recurrence.setFriday(true);
                            case 5:
                                recurrence.setSaturday(true);
                            case 6:
                                recurrence.setSunday(true);
                        }
                    }
                    recurrence.setInterval(1);
                } else if (event.getRecurrenceType().getMonthly() != null) {
                    recurrence.setType(RECURRENCE_TYPE_MONTHLY);
                    recurrence.setMonthlyOrYearlyDay(event.getRecurrenceType().getMonthly().getDailyInterval());
                    recurrence.setInterval(event.getRecurrenceType().getMonthly().getMonthlyInterval());
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    Date itemStartDate = recurrence.getStartDate();

                    int month = itemStartDate.getMonth();
                    Date current = (Date) itemStartDate.clone();
                    while (current.getMonth() == month) {
                        current.setDate(current.getDate() + 1);
                    }
                    current.setDate(current.getDate() - 1);
                    if (current.getDate() < recurrence.getMonthlyOrYearlyDay()) {
                        int monthLastDay = DateUtil.getMonthLastDate(recurrence.getStartDate()).getDate();
                        itemStartDate.setDate(monthLastDay);
                        recurrence.setStartDate(itemStartDate);
                        recurrence.setMonthlyOrYearlyDay(monthLastDay);
                    } else {
                        itemStartDate.setDate(recurrence.getMonthlyOrYearlyDay());
                        recurrence.setStartDate(itemStartDate);
                    }
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                } else if (event.getRecurrenceType().getYearly() != null) {
                    recurrence.setInterval(1);
                    recurrence.setType(RECURRENCE_TYPE_YEARLY);
                    recurrence.setYearlyMonth(event.getRecurrenceType().getYearly().getMonth());
                    recurrence.setCustomPatternDay(event.getRecurrenceType().getYearly().getType());
                    recurrence.setMonthlyOrYearlyDay(event.getRecurrenceType().getYearly().getDay());
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                }
            }

            if (event.getRecurrenceEndDate() != null) {
                if (event.getRecurrenceEndDate().isNever()) {
                    recurrence.setEndType(NO_END_DATE);
                } else if (event.getRecurrenceEndDate().getAfter() != null) {
                    recurrence.setEndType(END_AFTER_OCCURRENCES);
                    if (event.getRecurrenceEndDate().getAfter().getId() != null && event.getRecurrenceEndDate().getAfter().getId() > 0) {
                        recurrence.setOccurrence(event.getRecurrenceEndDate().getAfter().getId());
                    } else {
                        recurrence.setOccurrence(1);
                    }
                } else if (event.getRecurrenceEndDate().getUntil() != null) {
                    recurrence.setEndType(END_BY_DATE);
                    recurrence.setEndDate(event.getRecurrenceEndDate().getUntil().getDate());
                }
            }
            appointment.setRecurrenceJobItem(recurrence);
        }

        if (event.getEventType().equals(CALL_LOG)) {
            appointment.setMissedCall(event.isMissed());
            appointment.setInboundCall(event.isInbound());
            appointment.setOutboundCall(event.isOutgoing());
            appointment.setCallDuration((event.getEndDate().getTime() - event.getStartDate().getTime()) / 1000L);
            appointment.setCurrentCall(event.isCurrent());
            appointment.setComplatedCall(event.isCompleted());
            appointment.setScheduleCall(event.isScheduled());
            appointment.setClone(event.isClone());
        }
        appointment.setAllDay(event.isAllDay());
        appointment.setActivityType(event.getEventType());

        if (event.getRelations() != null && event.getRelations().size() > 0) {
            ArrayList<RelationItem> relations = new ArrayList<>();
            event.getRelations().forEach(relation -> relations.add(relationHelper.convertRelation(relation, appointment.getObjectID(), appointment.getSubject(), TYPE_EVENT)));
            appointment.setRelations(relations);
        }
        googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);

        event.setId(appointment.getObjectID());
        event.setCreatedAt(appointment.getCreatedDate());
        event.setUpdatedAt(appointment.getLastModifiedDate());
    }

    @Transactional
    public void savePatch(final EventDto event) throws RestException {

        Optional.ofNullable(eventManager.get(event.getId())).orElseThrow(() -> new RestException(GENERAL_ERROR_MESSAGE, "Event with this id is not found", NOT_FOUND, HttpStatus.BAD_REQUEST));

        Appointment appointment = googleCalendarServiceLocal.getAppointment(event.getId(), false);

        Optional.ofNullable(event.getSubject()).ifPresent(appointment::setSubject);
        Optional.ofNullable(event.getDescription()).ifPresent(appointment::setDescription);
        Optional.ofNullable(event.getStartDate()).ifPresent(appointment::setStartDate);
        Optional.ofNullable(event.getEndDate()).ifPresent(appointment::setEndDate);

        Optional.ofNullable(event.getEventType()).ifPresent(appointment::setActivityType);
        Optional.of(event.isRecurrence()).ifPresent(appointment::setRecurring);
        Optional.of(event.isAllDay()).ifPresent(appointment::setAllDay);

        if (event.getEventType() != null && event.getEventType().equals(CALL_LOG)) {
            appointment.setCallDuration((appointment.getEndDate().getTime() - appointment.getStartDate().getTime()) / 1000L);
            Optional.of(event.isMissed()).ifPresent(appointment::setMissedCall);
            Optional.of(event.isInbound()).ifPresent(appointment::setInboundCall);
            Optional.of(event.isOutgoing()).ifPresent(appointment::setOutboundCall);
            Optional.of(event.isClone()).ifPresent(appointment::setClone);
            Optional.of(event.isCurrent()).ifPresent(appointment::setCurrentCall);
            Optional.of(event.isScheduled()).ifPresent(appointment::setScheduleCall);
            Optional.of(event.isCompleted()).ifPresent(appointment::setComplatedCall);
        }

        if (event.getGuests() != null && !event.getGuests().isEmpty()) {
            ArrayList<SelectItem> guests = new ArrayList<>();
            event.getGuests().forEach(email -> guests.add(new SelectItem(0, email, EVENT_GUEST_STATUS_PENDING)));
            appointment.setGuests(guests);
        }

        if (event.getReminders() != null && !event.getReminders().isEmpty()) {
            ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
            for (ReminderDto reminder : event.getReminders()) {
                if (reminder != null) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    Optional.ofNullable(reminder.getType()).ifPresent(cer::setValue);

                    Optional.ofNullable(reminder.getTimes()).ifPresent(cer::setReminderTimes);
                    eventReminders.add(cer);
                }
            }
            if (!eventReminders.isEmpty()) {
                appointment.setReminder(eventReminders);
            }
        }

        if (event.getShares() != null && !event.getShares().isEmpty()) {
            ArrayList<PositionsSelectItem> shares = new ArrayList<>();
            for (IdCode share : event.getShares()) {
                PositionsSelectItem positionsSelectItem = new PositionsSelectItem();
                Optional.ofNullable(share.getId()).ifPresent(positionsSelectItem::setEmployeeId);

                Optional.ofNullable(share.getId()).ifPresent(sh -> {
                    EdsEmployee edsEmployee = employeeManager.get(share.getId());
                    if (edsEmployee != null) {
                        if (edsEmployee.getPosition() != null) {
                            positionsSelectItem.setPositionName(edsEmployee.getPosition().getName());
                        }
                        if (edsEmployee.getEmployeeTeam() != null && edsEmployee.getEmployeeTeam().getTeam() != null) {
                            positionsSelectItem.setDepartmentName(edsEmployee.getEmployeeTeam().getTeam().getName());
                        }
                        positionsSelectItem.setName(edsEmployee.getFullName());
                    }
                });
                shares.add(positionsSelectItem);
            }
            appointment.setSharedEmployees(shares);
        }

        if (event.isRecurrence() && (event.getRecurrenceType() != null || event.getRecurrenceStartDate() != null || event.getRecurrenceEndDate() != null)) {
            RecurrenceJobItem recurrence = appointment.getRecurrenceJobItem();
            Optional.ofNullable(event.getRecurrenceStartDate()).ifPresent(recurrence::setStartDate);
            if (event.getRecurrenceType() != null) {
                if (event.getRecurrenceType().getDaily() != null) {
                    recurrence.setType(RECURRENCE_TYPE_DAILY);
                    Optional.ofNullable(event.getRecurrenceType().getDaily().getInterval()).ifPresent(recurrence::setInterval);
                    recurrence.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                } else if (event.getRecurrenceType().getWeekly() != null) {
                    recurrence.setType(RECURRENCE_TYPE_WEEKLY);
                    Optional.ofNullable(event.getRecurrenceType().getWeekly().getDays()).ifPresent(inter -> {
                        for (Integer day : event.getRecurrenceType().getWeekly().getDays()) {
                            switch (day) {
                                case 0:
                                    recurrence.setMonday(true);
                                case 1:
                                    recurrence.setTuesday(true);
                                case 2:
                                    recurrence.setWednesday(true);
                                case 3:
                                    recurrence.setThursday(true);
                                case 4:
                                    recurrence.setFriday(true);
                                case 5:
                                    recurrence.setSaturday(true);
                                case 6:
                                    recurrence.setSunday(true);
                            }
                        }
                    });
                    recurrence.setInterval(1);
                } else if (event.getRecurrenceType().getMonthly() != null) {
                    recurrence.setType(RECURRENCE_TYPE_MONTHLY);
                    Optional.ofNullable(event.getRecurrenceType().getMonthly().getDailyInterval()).ifPresent(recurrence::setMonthlyOrYearlyDay);
                    Optional.ofNullable(event.getRecurrenceType().getMonthly().getMonthlyInterval()).ifPresent(recurrence::setInterval);
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    Optional.ofNullable(recurrence.getStartDate()).ifPresent(s -> {
                        Date itemStartDate = recurrence.getStartDate();

                        int month = itemStartDate.getMonth();
                        Date current = (Date) itemStartDate.clone();
                        while (current.getMonth() == month) {
                            current.setDate(current.getDate() + 1);
                        }
                        current.setDate(current.getDate() - 1);
                        if (current.getDate() < recurrence.getMonthlyOrYearlyDay()) {
                            int monthLastDay = DateUtil.getMonthLastDate(recurrence.getStartDate()).getDate();
                            itemStartDate.setDate(monthLastDay);
                            recurrence.setStartDate(itemStartDate);
                            recurrence.setMonthlyOrYearlyDay(monthLastDay);
                        } else {
                            itemStartDate.setDate(recurrence.getMonthlyOrYearlyDay());
                            recurrence.setStartDate(itemStartDate);
                        }
                    });
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                } else if (event.getRecurrenceType().getYearly() != null) {
                    recurrence.setInterval(1);
                    recurrence.setType(RECURRENCE_TYPE_YEARLY);
                    Optional.ofNullable(event.getRecurrenceType().getYearly().getMonth()).ifPresent(recurrence::setYearlyMonth);
                    Optional.ofNullable(event.getRecurrenceType().getYearly().getType()).ifPresent(recurrence::setCustomPatternDay);
                    Optional.ofNullable(event.getRecurrenceType().getYearly().getDay()).ifPresent(recurrence::setMonthlyOrYearlyDay);
                    recurrence.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                }
            }

            if (event.getRecurrenceEndDate() != null) {
                if (event.getRecurrenceEndDate().isNever()) {
                    recurrence.setEndType(NO_END_DATE);
                } else if (event.getRecurrenceEndDate().getAfter() != null) {
                    recurrence.setEndType(END_AFTER_OCCURRENCES);
                    if (event.getRecurrenceEndDate().getAfter().getId() != null && event.getRecurrenceEndDate().getAfter().getId() > 0) {
                        recurrence.setOccurrence(event.getRecurrenceEndDate().getAfter().getId());
                    } else {
                        recurrence.setOccurrence(1);
                    }
                } else if (event.getRecurrenceEndDate().getUntil() != null) {
                    recurrence.setEndType(END_BY_DATE);
                    recurrence.setEndDate(event.getRecurrenceEndDate().getUntil().getDate());
                }
            }
            appointment.setRecurrenceJobItem(recurrence);
        }
        EdsEvent edsEvent = eventManager.getEventByID(event.getId());

        appointment.setCustomFieldItems(CustomFieldsUtils.convertCustomFields(event.getCustomFields(), commonServiceLocal.getCompanyCustomFields(ViewName.Event), edsEvent.getEventCustomFields()));

        googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);

        event.setId(appointment.getObjectID());

    }
}
