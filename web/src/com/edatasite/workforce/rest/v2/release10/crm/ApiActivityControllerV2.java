package com.edatasite.workforce.rest.v2.release10.crm;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeAsterisk;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsGoogleCalendarEventGuests;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAsteriskManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarEventGuestsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarReminderManager;
import com.edatasite.workforce.gwt.core.server.db.OpportunityManager;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.enums.ActivityTypeEnum;
import com.edatasite.workforce.rest.base.enums.CallTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ApiResult;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.ContactTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityGroupListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityMemberTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityResultListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ActivityTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.CallAdditionalInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.CallDetailsInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.CreateCallTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.EventAdditionalInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.EventBaseInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.EventDetailsInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.EventEmployeeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.EventGuestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LatestActivityTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LinksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceRepeatsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceUntilTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ShareWithDepartmentsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.TimeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;
import com.edatasite.workforce.rest.v2.release10.enums.ActivityEntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.LinkTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EVENT_GUEST_STATUS_PENDING;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.E_MAIL;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_EVENT;
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

/**
 * Created by Dilshod Madrahimov on 01/29/2018.
 */
@Tag(name = "Activities", description = "Activities API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiActivityControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiActivityControllerV2.class);
    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
    @Autowired
    ProfileManager profileManager;
    @Autowired
    EmployeeAsteriskManager employeeAsteriskManager;
    @Autowired
    OpportunityManager opportunityManager;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private EmployeeEventManager employeeEventManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RecurrenceManager recurrenceManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private GoogleCalendarEventGuestsManager eventGuestsManager;
    @Autowired
    private GoogleCalendarReminderManager eventReminderManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private ContactServiceLocal contactServiceLocal;

    private static Map<String, String> toMap(String[] split) {
        Map<String, String> result = new HashMap<>();
        int[] keyless = {0, 1, 4, 5};
        for (int i = 0; i < split.length; i++) {
            String[] _s = split[i].split("-");
            int finalI = i;
            if (_s.length > 1 && IntStream.of(keyless).noneMatch(x -> x == finalI)) {
                String value = split[i].substring(split[i].indexOf(_s[0]) + _s[0].length() + 1).trim();
                if (org.apache.commons.lang.StringUtils.isNotBlank(value) && !"null".equalsIgnoreCase(value)) {
                    result.put(_s[0], value);
                }
            } else {
                String key = null;
                String value = split[i];
                if (i == 0) {
                    key = "type";
                } else if (i == 1) {
                    key = "office";
                } else if (i == 4) {
                    key = "date";
                } else if (i == 5) {
                    key = "time";
                    if (result.containsKey("date") && StringUtils.isNotBlank(result.get("date"))) {
                        result.put("datetime", result.get("date") + "," + value);
                    }
                } else if (i == 10) {
                    key = "lastaction";
                }
                if (!"null".equalsIgnoreCase(value)) {
                    result.put(key, value);
                }
            }
        }
        return result;
    }

    @Operation(summary = "Get Activities list\n", description = "Request to get activities list. Request needs to return list of activities grouped by same date")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the activities list")})
    @RequestMapping(value = "/activities", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST})
    public Object getActivityList(@RequestParam(value = "start_date") String start_date,
                                  @RequestParam(value = "positive_offset") Integer positive_offset,
                                  @RequestParam(value = "negative_offset") Integer negative_offset,
                                  @RequestParam(value = "positive_limit") Integer positive_limit,
                                  @RequestParam(value = "negative_limit") Integer negative_limit) throws RestException {
        log.info("ACTIVITY_START_DATE" + start_date);
        log.info("ACTIVITY_POSITIVE_OFFSET" + positive_offset);
        log.info("ACTIVITY_NEGATIVE_OFFSET" + negative_offset);
        log.info("ACTIVITY_POSITIVE_LIMIT" + positive_limit);
        log.info("ACTIVITY_NEGATIVE_LIMIT" + negative_limit);
        if (StringUtils.isBlank(start_date)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "start_date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        Date startDate;
        try {
            startDate = longDateTimezoneFormat.parse(start_date);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (positive_offset == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "positive_offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (negative_offset == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "negative_offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (positive_limit == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "positive_limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (negative_limit == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "negative_limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            ListingFilterParameter positiveFilterParam = new ListingFilterParameter();
            positiveFilterParam.setStart(positive_offset);
            positiveFilterParam.setLimit(positive_limit);

            ListingFilterParameter negativeFilterParam = new ListingFilterParameter();
            negativeFilterParam.setStart(negative_offset);
            negativeFilterParam.setLimit(negative_limit);

            ActivityResultListTO activityResultList = new ActivityResultListTO();

            LinkedHashMap<Integer, ArrayList<Date>> eventPositiveDatesMap = eventManager.getEventDates(ServerUtils.getStartDate(startDate), true, positiveFilterParam);
            LinkedHashMap<Integer, ArrayList<Date>> eventNegativeDatesMap = eventManager.getEventDates(ServerUtils.getStartDate(startDate), false, negativeFilterParam);

            getDatesData(positive_offset, positive_limit, activityResultList, eventPositiveDatesMap, true, null, null);
            getDatesData(negative_offset, negative_limit, activityResultList, eventNegativeDatesMap, false, null, null);

            return successResponse(activityResultList);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private void getDatesData(Integer offset, Integer limit,
                              ActivityResultListTO activityResultList, LinkedHashMap<Integer, ArrayList<Date>> datesMap, boolean isPositive, Integer relationId, String relationType) {
        Integer total = datesMap.keySet().stream().findFirst().get();
        ArrayList<Date> dates = datesMap.get(total);
        if (dates.size() > 0) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setDates(dates);
            filterParameter.setStart(0);
            filterParameter.setLimit(MAX_LIMIT);
            filterParameter.setSortField(EventItem.START_DATE);
            filterParameter.setAscending(true);
            filterParameter.setSortDir(Constants.ASC);
            filterParameter.setRelationID(relationId);
            filterParameter.setRelationType(relationType);

            ListResult<EventItem> result = crmServiceLocal.getEventList(filterParameter);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            PagingResultTO paginationTO = new PagingResultTO();
            paginationTO.setTotal_count(total);
            if (total < (limit + offset)) {
                paginationTO.setLeft(0);
            } else {
                paginationTO.setLeft(total - (offset + limit));
            }

            paginationTO.setCount(dates.size());
            paginationTO.setOffset(offset);

            if (isPositive) {
                //result.getList().sort((o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate()));
                activityResultList.setPositive_pagination(paginationTO);
            } else {
                activityResultList.setNegative_pagination(paginationTO);
            }

            ArrayList<String> activityDates = new ArrayList<>();
            ArrayList<ActivityTO> activityList = new ArrayList<>();
            ArrayList<ActivityGroupListTO> activityGroupList = new ArrayList<>();
            ActivityGroupListTO activityGroupListTO;
            for (EventItem event : result.getList()) {
                if (!activityDates.contains(dateFormat.format(event.getStartDate()))) {
                    activityDates.add(dateFormat.format(event.getStartDate()));

                    activityGroupListTO = new ActivityGroupListTO();
                    activityGroupListTO.setDate(longDateTimezoneFormat.format(event.getStartDate()));

                    activityList = new ArrayList<>();
                    activityList.add(convert(event));

                    activityGroupListTO.setActivities(activityList);

                    activityGroupList.add(activityGroupListTO);
                } else {
                    activityList.add(convert(event));
                }
            }

            if (activityResultList.getList() == null) {
                activityResultList.setList(activityGroupList);
            } else {
                activityResultList.getList().addAll(activityGroupList);
            }
        } else {
            PagingResultTO paginationTO = new PagingResultTO();
            paginationTO.setTotal_count(total);
            if (total < (limit + offset)) {
                paginationTO.setLeft(0);
            } else {
                paginationTO.setLeft(total - (offset + limit));
            }
            paginationTO.setCount(dates.size());
            paginationTO.setOffset(offset);
            if (isPositive) {
                activityResultList.setPositive_pagination(paginationTO);
            } else {
                activityResultList.setNegative_pagination(paginationTO);
            }
            if (activityResultList.getList() == null) {
                activityResultList.setList(new ArrayList<>());
            }
        }

    }

    private ActivityTO convert(EventItem event) {
        ActivityTO activity = new ActivityTO();
        activity.setItem_id(event.getObjectID());
        activity.setName(event.getSubject());
        if (StringUtils.isNotBlank(event.getDescription())) {
            activity.setDescription(event.getDescription());
        }

        if (Appointment.EVENT == event.getActivityType()) {
            activity.setType(ActivityTypeEnum.EVENT.name());
        } else if (Appointment.CALL_LOG == event.getActivityType()) {
            if ("Outbound".equals(event.asActivityItem().getStatus())) {
                activity.setType(ActivityTypeEnum.CALL_OUTCOMING.name());
            } else if ("Inbound".equals(event.asActivityItem().getStatus())) {
                activity.setType(ActivityTypeEnum.CALL_INCOMING.name());
            } else {
                activity.setType(ActivityTypeEnum.CALL.name());
            }
        }

        if (event.getStartDate() != null) {
            activity.setStart_date(longDateTimezoneFormat.format(event.getStartDate()));
        }

        if (event.getEndDate() != null) {
            activity.setEnd_date(longDateTimezoneFormat.format(event.getEndDate()));
        }

        List<EdsUser> sharedEmployees = employeeEventManager.getEventSharedEmployees(event.getObjectID());
        ArrayList<ActivityMemberTO> activityMemberList = new ArrayList<>();
        if (sharedEmployees != null && sharedEmployees.size() > 0) {
            for (EdsUser employee : sharedEmployees) {
                if (employee.getEmployee() != null) {
                    ActivityMemberTO activityMember = new ActivityMemberTO();
                    activityMember.setItem_id(employee.getObjectID());
                    activityMember.setName(employee.getName());
                    if (employee.getPhoto() != null) {
                        activityMember.setAvatar_image(commonServiceLocal.getImageUrl(employee.getPhoto().getObjectID()));
                    }
                    activityMemberList.add(activityMember);
                }
            }
        }
        activity.setMembers(activityMemberList);

        return activity;
    }

    @Operation(summary = "Get Latest Activities", description = "Get latest activities that belongs to particular entity like Lead, Opportunity, Company etc. Particular entity is described in path, like other requests.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the activities list")})
    @RequestMapping(value = "/{main_entity_name}/{item_id}/latest_activities", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST})
    public Object getLatestActivityList(
            @PathVariable(value = "main_entity_name") String main_entity_name,
            @PathVariable(value = "item_id") Integer item_id,
            @RequestParam(value = "sort_type", required = false) String sort_type,
            @RequestParam(value = "direction", required = false) String direction,
            @RequestParam(value = "limit", required = false) Integer limit) throws RestException {

        if (StringUtils.isBlank(main_entity_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer start = 0;
        limit = limit != null ? limit : MAX_LIMIT;

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(0);
        filterParameter.setLimit(MAX_LIMIT);
        filterParameter.setCreatedFrom(Appointment.FROM_CRM);

        //Sort field
        if (StringUtils.isNotBlank(sort_type)) {
            if (OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type)) {
                filterParameter.setSortField(OrderFieldEnum.ID.getField());
            } else if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
                filterParameter.setSortField(OrderFieldEnum.NAME.getField());
            } else if (OrderFieldEnum.DATE.getField().equalsIgnoreCase(sort_type)) {
                filterParameter.setSortField(OrderFieldEnum.DATE.getField());
            } else {
                filterParameter.setSortField(OrderFieldEnum.DATE.getField());
            }
        } else {
            filterParameter.setSortField(OrderFieldEnum.DATE.getField());
        }

        //Sort direction, by default oldest first
        String sortDir = StringUtils.isNotBlank(direction) ? direction : OrderByEnum.ASC.name();
        filterParameter.setAscending(OrderByEnum.ASC.name().equalsIgnoreCase(sortDir));
        if (OrderByEnum.getDirection(sortDir) != null) {
            filterParameter.setSortDir(OrderByEnum.getDirection(sortDir).getId());
        }

        //Relation Type
        String relationType;
        if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_OPPORTUNITY;
        } else if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_LEAD;
        } else if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_CONTACT;
        } else if (EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_ACCOUNT;
        } else if (EntityTypeEnum.ACTIVITIES.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_EVENT;
        } else if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(main_entity_name)) {
            relationType = CrmConstants.CRM_TASK;
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name should be one of | leads | opportunities | tasks | companies | contacts | activities", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        filterParameter.setRelationType(relationType);
        filterParameter.setRelationID(item_id);

        ListResult<ActivityItem> resultList;
        try {
            resultList = crmServiceLocal.getActivityList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Sorting
        if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
            resultList.getList().sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equalsIgnoreCase(sortDir)) {
                    return (StringUtils.isNotBlank(o1.getSubject()) && StringUtils.isNotBlank(o2.getSubject())) ? o1.getSubject().compareTo(o2.getSubject()) : -1;
                } else {
                    return (StringUtils.isNotBlank(o1.getSubject()) && StringUtils.isNotBlank(o2.getSubject())) ? o2.getSubject().compareTo(o1.getSubject()) : -1;
                }
            });
        } else if (OrderFieldEnum.ID.getField().equalsIgnoreCase(sort_type)) {
            resultList.getList().sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equalsIgnoreCase(sortDir)) {
                    return o1.getEntityId().compareTo(o2.getEntityId());
                } else {
                    return o2.getEntityId().compareTo(o1.getEntityId());
                }
            });
        } else {
            resultList.getList().sort((o1, o2) -> {
                if (OrderByEnum.ASC.name().equalsIgnoreCase(sortDir)) {
                    return o1.getStartDate().compareTo(o2.getStartDate());
                } else {
                    return o2.getStartDate().compareTo(o1.getStartDate());
                }
            });
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<LatestActivityTO> result = new ArrayList<>();
        for (ActivityItem activityItem : resultList.getList()) {
            LatestActivityTO latestActivity = new LatestActivityTO();
            latestActivity.setItem_id(item_id);
            latestActivity.setEntity_id(activityItem.getEntityId());

            if (CrmConstants.SALEINVOICE.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.INVOICE.name());
            } else if (CrmConstants.TASK.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.TASK.name());
                if (EdsTask.LOW.equalsIgnoreCase(activityItem.getPriorityCode())) {
                    latestActivity.setPriority(TaskPriorityEnum.LOW.name());
                } else if (EdsTask.MEDIUM.equalsIgnoreCase(activityItem.getPriorityCode())) {
                    latestActivity.setPriority(TaskPriorityEnum.MEDIUM.name());
                } else if (EdsTask.HIGH.equalsIgnoreCase(activityItem.getPriorityCode())) {
                    latestActivity.setPriority(TaskPriorityEnum.HIGH.name());
                } else {
                    latestActivity.setPriority(TaskPriorityEnum.NONE.name());
                }
            } else if (CrmConstants.CRM_EVENT.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.EVENT.name());
            } else if (CrmConstants.CRM_EVENT_CALLOG.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.CALL.name());
            } else if (CrmConstants.EMAIL.equals(activityItem.getActivityType())) {
                if (Constants.DRAFT.equals(activityItem.getStatus())) {
                    latestActivity.setType(ActivityEntityTypeEnum.DRAFT.name());
                } else {
                    latestActivity.setType(ActivityEntityTypeEnum.EMAIL.name());
                }
            } else if (CrmConstants.MASS_MAIL.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.MASS_MAIL.name());
            } else if (CrmConstants.SMS.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.SMS_ALERT.name());
            } else if (CrmConstants.SALEQUOTE.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.QUOTE.name());
            } else if (CrmConstants.SALEORDER.equals(activityItem.getActivityType())) {
                latestActivity.setType(ActivityEntityTypeEnum.ORDER.name());
            }

            /*if (StringUtils.isNotBlank(activityItem.getDescription())) {
                latestActivity.setDescription(activityItem.getDescription());//todo
            } else*/
            if (StringUtils.isNotBlank(activityItem.getSubject())) {
                latestActivity.setDescription(activityItem.getSubject());
            }

            if (activityItem.getStartDate() != null) {
                latestActivity.setStart_date(longDateTimezoneFormat.format(activityItem.getStartDate()));
            }
            if (activityItem.getDueDate() != null) {
                latestActivity.setEnd_date(longDateTimezoneFormat.format(activityItem.getDueDate()));
            }
            if (activityItem.getStatusID() != null) {
                EdsReference edsReference = referenceManager.get(activityItem.getStatusID());
                if (edsReference != null) {
                    FlowSettingsTO status = new FlowSettingsTO();
                    status.setStatus_id(edsReference.getObjectID());
                    status.setStatus_name(edsReference.getName());
                    status.setOrder_id(edsReference.getSorder());
                    status.setIs_system(edsReference.isSystemReference());

                    if (edsReference.getReferenceColor() != null) {
                        ColorTO color = new ColorTO();
                        color.setId(edsReference.getReferenceColor().getObjectID());
                        color.setName(edsReference.getReferenceColor().getName());
                        color.setHex(edsReference.getReferenceColor().getHex());
                        status.setStatus_color(color);
                    }
                    latestActivity.setStatus(status);
                }
            }
            result.add(latestActivity);
        }

        return successResponse(new ResponseListData<>(ListUtils.getSublistSmart(result, start, limit)));

    }

    @Operation(summary = "Delete Activity", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/activities/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST, PermissionConstants.CRM_REMOVE_ACTIVITY})
    public Object deleteActivity(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsEvent edsEvent = eventManager.get(item_id);
        if (edsEvent == null || edsEvent.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Activity with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_REMOVE_ACTIVITY)) {
            try {
                ArrayList objectIDs = new ArrayList();
                objectIDs.add(item_id);
                crmServiceLocal.deleteEvent(objectIDs);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Activities for Entities", description = "Request to get activities linked to Lead, Opportunity and whatever you want. Request needs to return list of activities grouped by same date.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the activities list")})
    @RequestMapping(value = "/{main_entity_path}/{id}/activities", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST})
    public Object getActivitiesByEntity(@PathVariable(value = "main_entity_path") String main_entity_path,
                                        @PathVariable(value = "id") Integer id,
                                        @RequestParam(value = "start_date") String start_date,
                                        @RequestParam(value = "positive_offset") Integer positive_offset,
                                        @RequestParam(value = "negative_offset") Integer negative_offset,
                                        @RequestParam(value = "positive_limit") Integer positive_limit,
                                        @RequestParam(value = "negative_limit") Integer negative_limit) throws RestException {

        if (StringUtils.isBlank(start_date)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "start_date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(main_entity_path)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Date startDate;
        try {
            startDate = longDateTimezoneFormat.parse(start_date);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (positive_offset == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "positive_offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (negative_offset == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "negative_offset is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (positive_limit == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "positive_limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (negative_limit == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "negative_limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            //Relation Type
            String relationType;
            if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_OPPORTUNITY;
            } else if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_LEAD;
            } else if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_CONTACT;
            } else if (EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_ACCOUNT;
            } else if (EntityTypeEnum.ACTIVITIES.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_EVENT;
            } else if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(main_entity_path)) {
                relationType = CrmConstants.CRM_TASK;
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name should be one of | leads | opportunities | tasks | companies | contacts | activities", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            ListingFilterParameter positiveFilterParam = new ListingFilterParameter();
            positiveFilterParam.setStart(positive_offset);
            positiveFilterParam.setLimit(positive_limit);
            positiveFilterParam.setRelationType(relationType);
            positiveFilterParam.setRelationID(id);

            ListingFilterParameter negativeFilterParam = new ListingFilterParameter();
            negativeFilterParam.setStart(negative_offset);
            negativeFilterParam.setLimit(negative_limit);
            negativeFilterParam.setRelationType(relationType);
            negativeFilterParam.setRelationID(id);

            ActivityResultListTO activityResultList = new ActivityResultListTO();

            LinkedHashMap<Integer, ArrayList<Date>> eventPositiveDatesMap = eventManager.getEventDates(ServerUtils.getStartDate(startDate), true, positiveFilterParam);
            LinkedHashMap<Integer, ArrayList<Date>> eventNegativeDatesMap = eventManager.getEventDates(ServerUtils.getStartDate(startDate), false, negativeFilterParam);

            getDatesData(positive_offset, positive_limit, activityResultList, eventPositiveDatesMap, true, id, relationType);
            getDatesData(negative_offset, negative_limit, activityResultList, eventNegativeDatesMap, false, id, relationType);

            return successResponse(activityResultList);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Entity Category List", description = "Get Categories for particular entities like leads, events")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the activities list")})
    @RequestMapping(value = {"/events/{field_type}/categories", "/calls/{field_type}/categories"}, method = RequestMethod.GET)
    public Object getEventFieldValues(
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EntityCategoryTO entityCategories = new EntityCategoryTO();
        if (EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            if (custom_field_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "custom_field_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            CompanyCustomFieldItem customFieldItem;
            try {
                customFieldItem = profileServiceLocal.getCustomFieldData(custom_field_id, null);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

            }
            if (customFieldItem == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Categories with custom field id " + custom_field_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            ArrayList<CategoryTO> categories = new ArrayList<>();
            Integer start = (offset != null && offset > 0) ? offset : 0;
            Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;

            if (Constants.UI_TYPE_DROPDOWN.equalsIgnoreCase(customFieldItem.getUiType()) || Constants.UI_TYPE_RADIOBUTTON.equalsIgnoreCase(customFieldItem.getUiType())
                    || Constants.UI_TYPE_CHECKBOX.equalsIgnoreCase(customFieldItem.getUiType())) {
                SelectItem[] predefinedValues = customFieldItem.getPredefinedValuesWithSorting() != null ? customFieldItem.getPredefinedValuesWithSorting() : new SelectItem[0];
                List<SelectItem> predefinedValuesList = Arrays.asList(predefinedValues);

                if (StringUtils.isNotBlank(query)) {
                    predefinedValuesList = predefinedValuesList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                }
                entityCategories.setTotal_count(predefinedValuesList.size());
                if (predefinedValuesList.size() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(predefinedValuesList.size() - (start + maxLimit));
                }
                entityCategories.setCount(predefinedValuesList.size());
                entityCategories.setOffset(start);
                ArrayList<SelectItem> stringArrayList = new ArrayList<>(predefinedValuesList);
                ArrayList<SelectItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);

                for (SelectItem values : sublist) {
                    Integer id = values.getId() != null ? values.getId() : sublist.size();
                    CategoryTO category = new CategoryTO();
                    category.setId(id);
                    category.setTitle(values.getName());
                    categories.add(category);
                }
                entityCategories.setList(categories);
            }
        }
        return successResponse(entityCategories);
    }

    @Operation(summary = "Event Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have event details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/events/{id}/details", method = RequestMethod.GET)
    public Object getEventDetailsInfo(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsEvent event;
        try {
            event = eventManager.get(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (event == null || event.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Event with id ".concat(id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        EventDetailsInfoTO eventDetails = new EventDetailsInfoTO();

        // Base info
        EventBaseInfoTO baseInfo = new EventBaseInfoTO();
        baseInfo.setItem_id(event.getObjectID());
        baseInfo.setName(event.getSubject());
        if (StringUtils.isNotBlank(event.getDescription())) {
            baseInfo.setDescription(event.getDescription());
        }
        if (event.getStartDate() != null) {
            baseInfo.setStart_date(longDateTimezoneFormat.format(event.getStartDate()));
        }
        if (event.getEndDate() != null) {
            baseInfo.setEnd_date(longDateTimezoneFormat.format(event.getEndDate()));
        }
        baseInfo.setAll_day(event.isAllDay());

        eventDetails.setBase_info(baseInfo);

        // Additional Information
        EventAdditionalInfoTO additionalInfo = new EventAdditionalInfoTO();
        if (StringUtils.isNotBlank(event.getVenue())) {
            additionalInfo.setAddress(event.getVenue());
        }
        RecurrenceTO recurrence = new RecurrenceTO();
        RecurrenceUntilTO recurrenceUntil = new RecurrenceUntilTO();
        EdsRecurrence recurrenceJobItem = recurrenceManager.get(event.getRecurrenceID());
        if (recurrenceJobItem != null) {
            if (recurrenceJobItem.getEndDate() != null) {
                recurrenceUntil.setType("DATE");
                recurrenceUntil.setDate(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
            } else {
                recurrenceUntil.setType("NUMBER_EVENTS");
                recurrenceUntil.setOccurences(recurrenceJobItem.getOccurrence());
            }
            recurrence.setUntil(recurrenceUntil);

            RecurrenceRepeatsTO recurrenceRepeats = new RecurrenceRepeatsTO();
            if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_DAILY)) {
                recurrenceRepeats.setType("DAILY");
                recurrenceRepeats.setCount(recurrenceJobItem.getInterval());
            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_WEEKLY)) {
                recurrenceRepeats.setType("WEEKLY");

                ArrayList<String> selectedDays = new ArrayList<>();

                if (recurrenceJobItem.isSunday()) {
                    selectedDays.add("SUNDAY");
                }
                if (recurrenceJobItem.isMonday()) {
                    selectedDays.add("MONDAY");
                }
                if (recurrenceJobItem.isTuesday()) {
                    selectedDays.add("TUESDAY");
                }
                if (recurrenceJobItem.isWednesday()) {
                    selectedDays.add("WEDNESDAY");
                }
                if (recurrenceJobItem.isThursday()) {
                    selectedDays.add("THURSDAY");
                }
                if (recurrenceJobItem.isFriday()) {
                    selectedDays.add("FRIDAY");
                }
                if (recurrenceJobItem.isSaturday()) {
                    selectedDays.add("SATURDAY");
                }
                recurrenceRepeats.setSelected_days(selectedDays);

            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_MONTHLY)) {
                recurrenceRepeats.setType("MONTHLY");
                recurrenceRepeats.setCount(recurrenceJobItem.getInterval());
            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_YEARLY)) {
                recurrenceRepeats.setType("YEARLY");
                if (recurrenceJobItem.getEndDate() != null) {
                    recurrenceRepeats.setYearly_date(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
                } else {
                    recurrenceRepeats.setCount(recurrenceJobItem.getOccurrence());
                }
            }
            recurrence.setRepeats(recurrenceRepeats);
            additionalInfo.setRecurrence(recurrence);

        }
        eventDetails.setAdditional_info(additionalInfo);

        ArrayList<LinksTO> links = new ArrayList<>();
        EventItem eventItem = crmServiceLocal.getEvent(id);
        if (eventItem.getRelations() != null && eventItem.getRelations().size() > 0) {
            eventItem.getRelations().forEach(relationItem -> {
                LinksTO link = new LinksTO();
                link.setId(relationItem.getToID());
                link.setName(relationItem.getToName());
                if (StringUtils.isNotBlank(relationItem.getToType())) {
                    link.setLink_type(getLinkType(relationItem.getToType()));
                }
                if (getLinkType(relationItem.getToType()) != null) {
                    links.add(link);
                }
            });
        }
        additionalInfo.setLinks(links);


        ArrayList<TimeTO> reminders = new ArrayList<>();
        ArrayList<CalendarEventReminder> eventReminders = eventReminderManager.getReminders(event.getObjectID());
        if (eventReminders != null) {
            eventReminders.forEach(calendarEventReminder -> {
                TimeTO reminderTO = new TimeTO();
                int hours = calendarEventReminder.getReminderTimes() / 60;
                int minutes = calendarEventReminder.getReminderTimes() - hours * 60;
                reminderTO.setMinute(minutes);
                reminderTO.setHour(hours);
                reminders.add(reminderTO);
            });
        }
        additionalInfo.setReminders(reminders);


        List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_EVENT, eventItem.getAttachmentFolderID(), eventItem.getObjectID());
        ArrayList<AttachmentTO> itemAttachments = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            attachments.forEach(fileItem -> itemAttachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl())));
        }
        additionalInfo.setAttachments(itemAttachments);


        ArrayList<EventEmployeeTO> employees = new ArrayList<>();
        if (eventItem.getSharedEmployees() != null && eventItem.getSharedEmployees().size() > 0) {
            eventItem.getSharedEmployees().forEach(sharedEmployee -> {
                EventEmployeeTO employee = new EventEmployeeTO();
                employee.setId(sharedEmployee.getEmployeeId());
                employee.setName(sharedEmployee.getName());
                try {
                    employee.setAvatar_image(hrmsServiceLocal.getEmployeeImageURL(sharedEmployee.getEmployeeId()));
                } catch (Exception e) {
                    log.error("", e);
                }
                EdsEmployee edsEmployee = employeeManager.get(sharedEmployee.getEmployeeId());
                if (edsEmployee != null && edsEmployee.getTeam() != null) {
                    CategoryTO department = new CategoryTO();
                    department.setId(edsEmployee.getTeam().getObjectID());
                    department.setTitle(edsEmployee.getTeam().getName());
                    employee.setDepartment(department);
                }
                employees.add(employee);
            });
        }
        additionalInfo.setEmployees(employees);

        ArrayList<EventGuestTO> guests = new ArrayList<>();
        List<EdsGoogleCalendarEventGuests> eventGuests = eventGuestsManager.getEventGuests(event.getObjectID());
        if (eventGuests != null) {
            eventGuests.forEach(eventGuest -> {
                EventGuestTO guest = new EventGuestTO();
                guest.setId(eventGuest.getObjectID());
                guest.setEmail(eventGuest.getEmail());
                guest.setStatus(eventGuest.getStatus().toUpperCase());
                guests.add(guest);
            });
        }
        additionalInfo.setGuests(guests);

        eventDetails.setAdditional_info(additionalInfo);

        String shareLink = EdsContextParams.getFullHost().concat(Constants.CRM_URL).concat("#").concat("event|summary/").concat(event.getObjectID().toString());
        eventDetails.setShare_link(shareLink);

        ArrayList<CustomFieldsTO> customFields = getCustomFields(eventItem.getCustomFieldItems());
        if (customFields != null && customFields.size() > 0) {
            eventDetails.setCustom_fields(customFields);
        }

        eventDetails.setCan_edit(eventItem.isEditable());

        return successResponse(eventDetails);
    }

    @Operation(summary = "Call Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have call details")})
    @RequestMapping(value = "/calls/{id}/details", method = RequestMethod.GET)
    public Object getCallDetailsInfo(@PathVariable(value = "id") Integer id) throws RestException {

        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsEvent event;
        try {
            event = eventManager.get(id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (event == null || event.isDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Call with id ".concat(id.toString()).concat(" is not found"), NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        CallDetailsInfoTO callDetails = new CallDetailsInfoTO();

        // Base info
        EventBaseInfoTO baseInfo = new EventBaseInfoTO();
        baseInfo.setItem_id(event.getObjectID());
        baseInfo.setName(event.getSubject());
        if (StringUtils.isNotBlank(event.getDescription())) {
            baseInfo.setDescription(event.getDescription());
        }
        if (event.getStartDate() != null) {
            baseInfo.setStart_date(longDateTimezoneFormat.format(event.getStartDate()));
        }
        if (event.getEndDate() != null) {
            baseInfo.setEnd_date(longDateTimezoneFormat.format(event.getEndDate()));
        }
        baseInfo.setAll_day(event.isAllDay());

        callDetails.setBase_info(baseInfo);

        // Additional Information
        CallAdditionalInfoTO additionalInfo = new CallAdditionalInfoTO();
        if (event.isInboundCall()) {
            additionalInfo.setCall_type(CallTypeEnum.INBOUND.getCode());
        } else {
            additionalInfo.setCall_type(CallTypeEnum.OUTBOUND.getCode());
        } /*else {
            additionalInfo.setCall_type(ActivityTypeEnum.CALL.name());
        }*/
        if (StringUtils.isNotBlank(event.getVenue())) {
            additionalInfo.setAddress(event.getVenue());
        }

        RecurrenceTO recurrence = new RecurrenceTO();
        RecurrenceUntilTO recurrenceUntil = new RecurrenceUntilTO();
        EdsRecurrence recurrenceJobItem = recurrenceManager.get(event.getRecurrenceID());
        if (recurrenceJobItem != null) {
            if (recurrenceJobItem.getEndDate() != null) {
                recurrenceUntil.setType("DATE");
                recurrenceUntil.setDate(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
            } else {
                recurrenceUntil.setType("NUMBER_EVENTS");
                recurrenceUntil.setOccurences(recurrenceJobItem.getOccurrence());
            }
            recurrence.setUntil(recurrenceUntil);

            RecurrenceRepeatsTO recurrenceRepeats = new RecurrenceRepeatsTO();
            if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_DAILY)) {
                recurrenceRepeats.setType("DAILY");
                recurrenceRepeats.setCount(recurrenceJobItem.getInterval());
            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_WEEKLY)) {
                recurrenceRepeats.setType("WEEKLY");

                ArrayList<String> selectedDays = new ArrayList<>();

                if (recurrenceJobItem.isSunday()) {
                    selectedDays.add("SUNDAY");
                }
                if (recurrenceJobItem.isMonday()) {
                    selectedDays.add("MONDAY");
                }
                if (recurrenceJobItem.isTuesday()) {
                    selectedDays.add("TUESDAY");
                }
                if (recurrenceJobItem.isWednesday()) {
                    selectedDays.add("WEDNESDAY");
                }
                if (recurrenceJobItem.isThursday()) {
                    selectedDays.add("THURSDAY");
                }
                if (recurrenceJobItem.isFriday()) {
                    selectedDays.add("FRIDAY");
                }
                if (recurrenceJobItem.isSaturday()) {
                    selectedDays.add("SATURDAY");
                }
                recurrenceRepeats.setSelected_days(selectedDays);

            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_MONTHLY)) {
                recurrenceRepeats.setType("MONTHLY");
                recurrenceRepeats.setCount(recurrenceJobItem.getInterval());
            } else if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_YEARLY)) {
                recurrenceRepeats.setType("YEARLY");
                if (recurrenceJobItem.getEndDate() != null) {
                    recurrenceRepeats.setYearly_date(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
                } else {
                    recurrenceRepeats.setCount(recurrenceJobItem.getOccurrence());
                }
            }
            recurrence.setRepeats(recurrenceRepeats);
            additionalInfo.setRecurrence(recurrence);

        }
        callDetails.setAdditional_info(additionalInfo);

        ArrayList<Object> links = new ArrayList<>();
        EventItem eventItem = crmServiceLocal.getEvent(id);
        if (eventItem.getRelations() != null && eventItem.getRelations().size() > 0) {
            eventItem.getRelations().forEach(relationItem -> {
                LinksTO link = new LinksTO();
                link.setId(relationItem.getToID());
                link.setName(relationItem.getToName());
                if (StringUtils.isNotBlank(relationItem.getToType())) {
                    link.setLink_type(getLinkType(relationItem.getToType()));
                }
                if (getLinkType(relationItem.getToType()) != null) {
                    links.add(link);
                }
            });
        }
        additionalInfo.setLinks(links);

        //reminders
        ArrayList<TimeTO> reminders = new ArrayList<>();
        ArrayList<CalendarEventReminder> eventReminders = eventReminderManager.getReminders(event.getObjectID());
        if (eventReminders != null) {
            eventReminders.forEach(calendarEventReminder -> {
                TimeTO reminderTO = new TimeTO();
                int hours = calendarEventReminder.getReminderTimes() / 60;
                int minutes = calendarEventReminder.getReminderTimes() - hours * 60;
                reminderTO.setMinute(minutes);
                reminderTO.setHour(hours);
                reminders.add(reminderTO);
            });
        }
        additionalInfo.setReminders(reminders);

        //attachments
        List<FileResource> attachments = attachmentUtilsManager.getAttachments(F_EVENT, eventItem.getAttachmentFolderID(), eventItem.getObjectID());
        ArrayList<AttachmentTO> itemAttachments = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            attachments.forEach(fileItem -> itemAttachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl())));
        }
        additionalInfo.setAttachments(itemAttachments);

        //employees
        ArrayList<EventEmployeeTO> employees = new ArrayList<>();
        if (eventItem.getSharedEmployees() != null && eventItem.getSharedEmployees().size() > 0) {
            eventItem.getSharedEmployees().forEach(sharedEmployee -> {
                EventEmployeeTO employee = new EventEmployeeTO();
                employee.setId(sharedEmployee.getEmployeeId());
                employee.setName(sharedEmployee.getName());
                try {
                    employee.setAvatar_image(hrmsServiceLocal.getEmployeeImageURL(sharedEmployee.getEmployeeId()));
                } catch (Exception e) {
                    log.error("", e);
                }
                EdsEmployee edsEmployee = employeeManager.get(sharedEmployee.getEmployeeId());
                if (edsEmployee != null && edsEmployee.getTeam() != null) {
                    CategoryTO department = new CategoryTO();
                    department.setId(edsEmployee.getTeam().getObjectID());
                    department.setTitle(edsEmployee.getTeam().getName());
                    employee.setDepartment(department);
                }
                employees.add(employee);
            });
        }
        additionalInfo.setEmployees(employees);

        //guests
        ArrayList<EventGuestTO> guests = new ArrayList<>();
        List<EdsGoogleCalendarEventGuests> eventGuests = eventGuestsManager.getEventGuests(event.getObjectID());
        if (eventGuests != null) {
            eventGuests.forEach(eventGuest -> {
                EventGuestTO guest = new EventGuestTO();
                guest.setId(eventGuest.getObjectID());
                guest.setEmail(eventGuest.getEmail());
                guest.setStatus(eventGuest.getStatus().toUpperCase());
                guests.add(guest);
            });
        }
        additionalInfo.setGuests(guests);

        callDetails.setAdditional_info(additionalInfo);

        //share link
        String shareLink = EdsContextParams.getFullHost().concat(Constants.CRM_URL).concat("#").concat("event|summary/").concat(event.getObjectID().toString());
        callDetails.setShare_link(shareLink);

        ArrayList<CustomFieldsTO> customFields = getCustomFields(eventItem.getCustomFieldItems());
        if (customFields != null && customFields.size() > 0) {
            callDetails.setCustom_fields(customFields);
        }

        callDetails.setCan_edit(eventItem.isEditable());

        return successResponse(callDetails);
    }

    @Transactional
    @Operation(summary = "Create Call")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request to create new Call. It's multipart request."),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/calls/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST, PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL})
    public Object createCall(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {
        return createActivity(multipartRequest, EntityTypeEnum.CALLS.name(), jsonString);
    }

    @Transactional
    @Operation(summary = "Log Asteriks Call")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully created. It's multipart request.")})
    @RequestMapping(value = "/calls/asteriks", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST, PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT, PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL})
    public ApiResult logAsteriksCall(MultipartRequest multipartRequest, @RequestParam(name = "body") String callLogs) throws RestException {
        /*incoming,
                kpien,
                callerid-01732617967,
                uniqueid-1583133701.79,
                2020-03-02,
                12:22:08,
                option-11,
                operator-,
                billsec-26,
                filename-kpiivr-callerid-01732617967-1583133701.79--2020-03-02--12-21-51,
            lastaction-null,
            survey-null,
            holdtime-,
            startpos-,
            stage-9*/
        try {
            log.debug("REST request to save CallLogs : {}", URLDecoder.decode(callLogs, "UTF-8"));
            Map<String, String> strings = toMap(URLDecoder.decode(callLogs, "UTF-8").split(","));
            log.info("{}", strings);
            if (strings.isEmpty() || StringUtils.isBlank(strings.get("uniqueid"))) {
                throw new RestException("Invalid input", "Invalid input", INVALID, HttpStatus.BAD_REQUEST);
            }

            Appointment appointment = googleCalendarServiceLocal.getAppointmentByAsteriskid(strings.get("uniqueid"));
            if (appointment == null) {
                appointment = new Appointment();
                appointment.setAsteriskid(strings.get("uniqueid"));
                appointment.setActivityType(Appointment.CALL_LOG);
                appointment.setCreatedFrom(Appointment.FROM_CRM);

                appointment.setStyle(Appointment.AQUA);
                appointment.setAllDay(Boolean.FALSE);
//                appointment.setSubject("Call From: " + strings.get("callerid"));
//                appointment.setSubject("");
                appointment.setLocation("Asteriks");
                appointment.setCreatedBy(userManager.getUser().getName());
            } else {
                appointment.setRegisterWorkFlowEventPerDate(false);
            }
            appointment.setInboundCall("incoming".equalsIgnoreCase(strings.get("type")));
            appointment.setOutboundCall("outgoing".equalsIgnoreCase(strings.get("type")));
            appointment.setMissedCall(StringUtils.isBlank(strings.get("operator")) || "null".equalsIgnoreCase(strings.get("operator")) || "noanswer".equalsIgnoreCase(strings.get("lastaction")));
            appointment.setSubject(appointment.isOutboundCall() ? "Call to: " + strings.get("callerid") : "Call from: " + strings.get("callerid"));
            appointment.setDescription(appointment.getSubject());
            appointment.setComplatedCall(true);

            Integer assigneeId = null;
            if (!appointment.isMissedCall()) {
                try {
                    assigneeId = eventManager.getUser().getObjectID();
                    String asteriskUsername = strings.get("operator").contains("/") ? strings.get("operator").substring(strings.get("operator").indexOf("/") + 1) : strings.get("operator");
                    List<EdsEmployeeAsterisk> employeeAsterisks = employeeAsteriskManager.getByAsteriskUsername(asteriskUsername);
                    if (!CollectionUtils.isEmpty(employeeAsterisks)) {
                        assigneeId = employeeAsterisks.get(0).getUserId();
                        ArrayList<Attendee> attendees = new ArrayList<>();
                        for (EdsEmployeeAsterisk employeeAsterisk : employeeAsterisks) {
                            Attendee attendee = new Attendee();
                            attendee.setID(employeeAsterisk.getUserId());
                            attendee.setGoogleID(appointment.getGoogleID());
                            attendee.setOfficeID(appointment.getOfficeID());
                            attendee.setShared(true);
                            attendees.add(attendee);
                        }
                        appointment.setAttendees(attendees);

                        //Set created by for outgoing calls
                        if (employeeAsterisks.get(0).getUser() != null) {
                            appointment.setCreatedBy(employeeAsterisks.get(0).getUser().getName());
                            appointment.setOwnerName(employeeAsterisks.get(0).getUser().getName());
                            appointment.setOwnerID(employeeAsterisks.get(0).getUserId());
                        }
                    }
                } catch (Exception e) {
                    log.error("Error parsing Asterisk operator", strings.get("operator"));
                }
            }

            if (strings.get("datetime") != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss");
//                formatter.setTimeZone(TimeZone.getTimeZone("Etc/GMT"));
                formatter.setTimeZone(TimeZone.getTimeZone("Asia/Tashkent"));
                if (appointment.getObjectID() == null) {
                    Date startDate;
                    try {
                        startDate = formatter.parse(strings.get("datetime"));
                        appointment.setStartDate(startDate);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                } else {
                    Date endDate;
                    try {
                        endDate = formatter.parse(strings.get("datetime"));
                        appointment.setEndDate(endDate);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    long diff = appointment.getEndDate().getTime() - appointment.getStartDate().getTime();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                    appointment.setCallDuration(seconds);
                }
            }
//            datetime -> 2020-03-03,11:59:57
            //get details from crm, if no details found, just return phone number
            List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(strings.get("callerid"));
            List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(strings.get("callerid"));
//            ContactListItem item = new ContactListItem();
            ArrayList<ContactTO> contactTOS = new ArrayList<>();
            ArrayList<RelationItem> relations = new ArrayList<>();
            //edit mode
            if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
                try {
                    relationManager.deleteAllRelations(RelationItem.TYPE_EVENT, appointment.getObjectID());
                } catch (Exception e) {
                    log.error("Api error occurred while deleting event relations", e);
                }
            }
            if (contacts != null && !contacts.isEmpty()) {
                appointment.setSubject((appointment.isOutboundCall() ? "Call to: " : "Call from: ") + contacts.get(0).getName() + "(" + strings.get("callerid") + ")");
                for (EdsCrmContact contact : contacts) {
                    ContactTO contactTO = new ContactTO();
                    contactTO.setPhone(strings.get("callerid"));
                    //Set Activity Subject/Description if contacts exist
                    appointment.setDescription(appointment.getSubject());

                    RelationItem relation = new RelationItem();
                    if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
                        relation.setFromID(appointment.getObjectID());
                    }
                    relation.setFromID(appointment.getObjectID());
                    relation.setFromType(RelationItem.TYPE_EVENT);
                    relation.setToID(contact.getObjectID());
                    relation.setToType(RelationItem.getByContactType(contact.getContactType()));
                    relation.setToName(contact.getName());
                    if (relations.stream().noneMatch(x -> x.getToID().equals(relation.getToID()))) {
                        relations.add(relation);
                    }
                    List<EdsOpportunity> opportunities = opportunityManager.getOpportunityByCrmContactID(contact.getObjectID());
                    if (opportunities != null && opportunities.size() > 0) {
                        for (EdsOpportunity opportunity : opportunities) {
                            RelationItem relationItem = new RelationItem();
                            relationItem.setFromID(appointment.getObjectID());
                            relationItem.setFromType(RelationItem.TYPE_EVENT);
                            relationItem.setToID(opportunity.getObjectID());
                            relationItem.setToType(getEntityRelation(LinkTypeEnum.OPPORTUNITY.name()));
                            relationItem.setToName(opportunity.getName());
                            if (relations.stream().noneMatch(x -> x.getToID().equals(relationItem.getToID()))) {
                                relations.add(relationItem);
                            }
                        }
                    }


                    //Gather data to send thru socket to user
                    contactTO.setItem_id(contact.getObjectID());
                    contactTO.setFirst_name(contact.getFirstName());
                    contactTO.setLast_name(contact.getLastName());
                    contactTO.setName(contact.getName());
                    if (contact.getCrmAccount() != null) {
                        contactTO.setCompany(contactServiceLocal.convertCompany(contact.getCrmAccount()));

                        //Add Contacts company as relation
                        RelationItem contactsCompanyRelation = new RelationItem();
                        if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
                            contactsCompanyRelation.setFromID(appointment.getObjectID());
                        }
                        contactsCompanyRelation.setFromType(RelationItem.TYPE_EVENT);
                        contactsCompanyRelation.setToID(contact.getCrmAccount().getObjectID());
                        contactsCompanyRelation.setToType(getEntityRelation(LinkTypeEnum.CRM_ACCOUNT.name()));
                        contactsCompanyRelation.setToName(contact.getCrmAccount().getName());
                        if (relations.stream().noneMatch(x -> x.getToID().equals(contactsCompanyRelation.getToID()))) {
                            relations.add(contactsCompanyRelation);
                        }
                        contactTOS.add(contactTO);
                /*item = contact.getRPC(new ListingFilterParameter(false), item);
                if (item.getWorkPhone().size() > 0) {
                    item.getWorkPhone().set(0, strings.get("callerid"));
                } else {
                    item.getWorkPhone().add(strings.get("callerid"));
                }*/
                    }
                }
            }

            if (accounts != null && !accounts.isEmpty()) {
                for (EdsCrmAccount account : accounts) {
//                    ContactTO contactTO = new ContactTO();
//                for (ContactTO contactTO : contactTOS) {
                    //Set Activity Subject/Description if contacts exist
//                    if (contactTO.getItem_id() == null) {
//                    appointment.setSubject("Call From: " + account.getName() + " (" + strings.get("callerid") + ")");
//                    appointment.setDescription(appointment.getSubject());
                    //Gather data to send thru socket to user
//                    contactTO.setItem_id(account.getObjectID());
//                    contactTO.setName(account.getName());
//                    }
//                    if (contactTO.getCompany() == null) {
//                        contactTO.setCompany(contactServiceLocal.convertCompany(account));
//                    }

                    RelationItem relation = new RelationItem();
                    if (appointment.getObjectID() != null && appointment.getObjectID() > 0) {
                        relation.setFromID(appointment.getObjectID());
                    }
                    relation.setFromType(RelationItem.TYPE_EVENT);
                    relation.setToID(account.getObjectID());
                    relation.setToType(getEntityRelation(LinkTypeEnum.CRM_ACCOUNT.name()));
                    relation.setToName(account.getName());
                    if (relations.stream().noneMatch(x -> x.getToID().equals(relation.getToID()))) {
                        relations.add(relation);
                    }
                }
            }
            appointment.setRelations(relations);
            int candidateCount = 0;
            for (RelationItem relationItem : appointment.getRelations()) {
                if (RelationItem.TYPE_CANDIDATE.equals(relationItem.getToType())) {
                    candidateCount++;
                }
            }
            if (candidateCount == appointment.getRelations().size() && candidateCount > 0) {
                appointment.setCreatedFrom(Appointment.FROM_HRMS);
            } else if (candidateCount > 0 && appointment.getRelations().size() > 0) {
                appointment.setCreatedFrom(Appointment.FROM_BOTH);
            }
            appointment.setPhoneNumber(strings.get("callerid"));
            try {
                SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(assigneeId, appointment, false);
                log.info("Save Asteriks Call Log result:" + result.getId() + " " + result.getName());
//                    eventId = result.getId();

                if (appointment.getObjectID() != null && appointment.isMissedCall()) {
                    eventManager.addToSolr(appointment.getObjectID());
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            /*if ("incoming".equalsIgnoreCase(strings.get("type"))) {
                try {
                    WebSocketServerObject message = new WebSocketServerObject();
                    message.setUserId(userManager.getUser().getObjectID());
                    message.setData(new Gson().toJson(contactTO));
                    message.setEventType(WfmUiEventType.ON_PHONE_CALLED);
//                WebSocketServletImpl.sendMessageToAll(Integer.parseInt(SecurityContext.getInstance().getCompanyId()), message);
                    RedisSocketObject redisSocketObject = new RedisSocketObject();
                    redisSocketObject.setCompanyId(Integer.parseInt(SecurityContext.getInstance().getCompanyId()));
                    redisSocketObject.setSendToAll(true);
                    redisSocketObject.setWebSocketServerObject(message);
                    RedisClient.publish(redisSocketObject);
                } catch (NumberFormatException e) {
                    log.error(e.getMessage(), e);
                }
            }*/

//            return createCallLogs(CallLogDTO.create(URLDecoder.decode(callLogs, "UTF-8")));

        } catch (
                UnsupportedEncodingException e) {
//            e.printStackTrace();
            log.debug("<<<<<<<<<<<<<<< Catch >>>>>>>>>>>>>>>>>>>>");
            Map<String, String> strings = toMap(callLogs.split(","));
            log.info("{}", strings);
//            return createCallLogs(CallLogDTO.create(callLogs));
        }

        //return createActivity(multipartRequest, EntityTypeEnum.EVENTS.name(), jsonString);
        return

                successResponse(new ResponseData());
    }

    /*public static CallLogDTO create(String callLogs) {
//        incoming,alquoz,callerid-0521049447,uniqueid-1492862014.24968,2017-04-22,15:53:53,option-1,null,null,null,null,null,null
//        incoming,test,callerid-391,uniqueid-1542423116.39,2018-11-17,06:52:19,option-2,operator-Agent/301,billsec-23,filename-Option-2-1542423116.39-2018-11-17--06-52,lastaction-null,survey-null,holdtime-1,startpos-1,stage-9,trackid-null,trackstatus-null


        Map<String, String> strings = toMap(callLogs.split(","));
        CallLogDTO callLog = new CallLogDTO();
        callLog.setSourceType(CallSourceType.ivr);
        callLog.setType(CallType.fromString(strings.get("type")));//type
        callLog.setOffice(strings.get("office"));//office
        callLog.setCallerNumber(strings.get("callerid"));//callerNumber
        callLog.setUniqueID(strings.get("uniqueid"));//uniqueID
        callLog.setLogDate(DateUtils.parseIVR(strings.get("datetime")));//logDate
        callLog.setOption(strings.get("option"));//option
        callLog.setPin(strings.get("trackid"));//trackid
        callLog.setStatus(strings.get("trackstatus"));//trackstatus
        callLog.setMemberName(strings.get("operator"));//operator
        callLog.setRecordedFile(strings.get("filename"));//filename
        callLog.setDuration(strings.get("billsec"));//duration
        callLog.setLastAction(CallLastAction.fromString(strings.get("lastaction")));//lastaction
        callLog.setCalledNumber(strings.get("exten"));//exten
        callLog.setSurvey(strings.get("survey"));//survey
        callLog.setHoldTime(strings.get("holdtime"));//holdtime
        callLog.setStartPos(strings.get("startpos"));//startpos
        callLog.setStep(strings.get("stage"));//stage
        return callLog;
    }*/

    @Transactional
    @Operation(summary = "Save Asteriks Call log")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully saved. It's multipart request.")})
    @RequestMapping(value = "/calls/asteriks/upload", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST, PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT, PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL})
    public ApiResult uploadAsteriksCallFile(MultipartRequest multipartRequest) throws RestException {

        if (multipartRequest.getFileMap() == null || multipartRequest.getFileMap().isEmpty()) {
            throw new RestException("No files provided", "No files provided", INVALID, HttpStatus.BAD_REQUEST);
        }

        ArrayList<FileResource> savedFiles = new ArrayList<>();

        for (MultipartFile file : multipartRequest.getFileMap().values()) {
            //Expected file name is kpiivr-callerid-981159915-1585581475.8881--2020-03-30--20-18-29.ogg
//            kpiivr-outgoing-885-903941009-1595314116.12-2020-07-21--11-4
            if (StringUtils.isNotBlank(file.getOriginalFilename()) && file.getOriginalFilename().indexOf("-") > -1) {

                String[] parsedName = file.getOriginalFilename().split("-");

                if (parsedName.length > 3) {
                    String callId = parsedName[3];
                    if ("outgoing".equalsIgnoreCase(parsedName[1])) {
                        callId = parsedName[4];
                    }
                    Appointment appointment = googleCalendarServiceLocal.getAppointmentByAsteriskid(callId);
                    if (appointment != null && appointment.getObjectID() != null) {
                        try {
                            FileResource savedItem = documentsServiceLocal.saveDocumentFile(file, null, Constants.F_EVENT, appointment.getObjectID(), null);
                            savedFiles.add(savedItem);
                        } catch (Exception e) {
                            log.error("", e);
                            throw new RestException("Error occured", e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                        try {
                            File newFile = new File(file.getOriginalFilename());
                            file.transferTo(newFile);
                            AudioFile f = AudioFileIO.read(newFile);
                            appointment.setCallDuration(f.getAudioHeader() != null ? f.getAudioHeader().getTrackLength() : 0);
                            googleCalendarServiceLocal.saveCalendarEvent(null, appointment, false);
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    } else {
                        throw new RestException("Call not found", "Call not found", INVALID, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    throw new RestException("File name format is wrong (ex. kpiivr-callerid-981159915-1585581475.8881--2020-03-30--20-18-29.ogg) ",
                            "File name format is wrong (ex. kpiivr-callerid-981159915-1585581475.8881--2020-03-30--20-18-29.ogg) ", INVALID, HttpStatus.BAD_REQUEST);
                }

            } else {
                throw new RestException("File name format is wrong (ex. kpiivr-callerid-981159915-1585581475.8881--2020-03-30--20-18-29.ogg) ",
                        "File name format is wrong (ex. kpiivr-callerid-981159915-1585581475.8881--2020-03-30--20-18-29.ogg) ", INVALID, HttpStatus.BAD_REQUEST);
            }
        }
        if (!savedFiles.isEmpty()) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException("Nothing to save/associate", "Nothing to save/associate", INVALID, HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    @Operation(summary = "Create Event")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Request to create new Event. It's multipart request."),
            @ApiResponse(responseCode = "400", description = "item_id is required")})
    @RequestMapping(value = "/events/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CheckPermission(permissions = {PermissionConstants.CRM_ACTIVITIES_LIST, PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT})
    public Object createEvent(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {
        return createActivity(multipartRequest, EntityTypeEnum.EVENTS.name(), jsonString);
    }

    private Object createActivity(MultipartRequest multipartRequest,
                                  String event_type, String jsonString) throws RestException {

        CreateCallTO createCallTO;
        Date startDate;
        Date endDate;
        int activityType;

        ObjectMapper mapper = new ObjectMapper();
        try {
            createCallTO = mapper.readValue(jsonString, CreateCallTO.class);

        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong." + e.getMessage(), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsUser user = userManager.getUser();

        if (EntityTypeEnum.CALLS.name().equalsIgnoreCase(event_type)) {
            activityType = Appointment.CALL_LOG;
        } else if (EntityTypeEnum.EVENTS.name().equalsIgnoreCase(event_type)) {
            activityType = Appointment.EVENT;
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "event_type must be one of calls/events", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        if (StringUtils.isBlank(createCallTO.getWhat())) {
            throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"what\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (createCallTO.getWhen() != null) {

            if (StringUtils.isNotBlank(createCallTO.getWhen().getStart_date())) {
                try {
                    startDate = longDateTimezoneFormat.parse(createCallTO.getWhen().getStart_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"start_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isNotBlank(createCallTO.getWhen().getEnd_date())) {
                try {
                    endDate = longDateTimezoneFormat.parse(createCallTO.getWhen().getEnd_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"end_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"when\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }


        //Collect draft files ids
        HashSet<Integer> draftFilesIdSet = new HashSet<>();
        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(customFieldFileNameRegex)) {
                    String[] fileName = file.getName().split("_");
                    draftFilesIdSet.add(Integer.valueOf(fileName[2]));
                }
            }
        }

        //Collect custom fields ids
        Set<Integer> customFieldIdSet = new HashSet<>();
        for (Object customFieldObject : createCallTO.getCustom_fields()) {
            LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
            Integer customFieldId = (Integer) customFieldsMap.get("id");
            if (customFieldId != null) {
                customFieldIdSet.add(customFieldId);
            }
        }

        //Merge custom field ids with draft files ids
        for (Integer draftFileId : draftFilesIdSet) {
            if (!customFieldIdSet.contains(draftFileId)) {
                LinkedHashMap<Object, Object> customFieldsMap = new LinkedHashMap<>();
                customFieldsMap.put("id", draftFileId);
                customFieldsMap.put("draft_files", new ArrayList<>());
                createCallTO.getCustom_fields().add(customFieldsMap);
            }
        }

        ArrayList<MultipartFile> eventAttachments = new ArrayList<>();
        TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = new TreeMap<>();

        Pattern pattern = Pattern.compile(customFieldFileNameRegex);

        if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
            for (MultipartFile file : multipartRequest.getFileMap().values()) {
                if (file.getName().matches(entityFileNameRegex)) {
                    eventAttachments.add(file);
                } else if (file.getName().matches(customFieldFileNameRegex)) {
                    Matcher m = pattern.matcher(file.getName());
                    Integer customFieldFileId;
                    if (m.matches()) {
                        customFieldFileId = Integer.valueOf(m.group(1));
                        ArrayList<MultipartFile> files = customFieldAttachmentsMap.get(customFieldFileId) == null ? new ArrayList<>() : customFieldAttachmentsMap.get(customFieldFileId);
                        files.add(file);
                        customFieldAttachmentsMap.put(customFieldFileId, files);
                    }
                }
            }
        }


        Appointment appointment;
        if (createCallTO.getId() != null && createCallTO.getId() > 0) {
            appointment = googleCalendarServiceLocal.getAppointment(createCallTO.getId(), false);
            if (appointment == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Event/Call with id " + createCallTO.getId() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {
            appointment = new Appointment();
        }
        appointment.setActivityType(activityType);
        appointment.setCreatedFrom(Appointment.FROM_CRM);

        if (Appointment.CALL_LOG == activityType && StringUtils.isNotBlank(createCallTO.getCall_type())) {
            if (CallTypeEnum.INBOUND.getCode().equalsIgnoreCase(createCallTO.getCall_type())) {
                appointment.setInboundCall(Boolean.TRUE);
            } else if (CallTypeEnum.OUTBOUND.getCode().equalsIgnoreCase(createCallTO.getCall_type())) {
                appointment.setInboundCall(Boolean.FALSE);
            }
        }

        appointment.setSubject(createCallTO.getWhat());
        appointment.setLocation(createCallTO.getWhere());
        if (createCallTO.getWhen() != null) {
            appointment.setStartDate(startDate);
            appointment.setEndDate(endDate);
            appointment.setAllDay(Boolean.TRUE.equals(createCallTO.getWhen().getAll_day()));
        }
        appointment.setDescription(createCallTO.getDescription());
        appointment.setCreatedBy(user.getFullName());

//        appointment.setMultiDay(appointment.isMultiDayAppointment());

        appointment.setStyle(Appointment.CALL_LOG == activityType ? Appointment.AQUA : Appointment.BLUE);

        //Links
        //edit mode
        if (createCallTO.getId() != null && createCallTO.getId() > 0) {
            if (createCallTO.getLinks() != null && createCallTO.getLinks().size() > 0) {
                try {
                    relationManager.deleteAllRelations(RelationItem.TYPE_EVENT, createCallTO.getId());
                } catch (Exception e) {
                    log.error("Api error occurred while deleting event relations", e);
                }
                ArrayList<RelationItem> relations = new ArrayList<>();

                for (LinkTO linkTO : createCallTO.getLinks()) {
                    RelationItem relation = new RelationItem();
                    relation.setFromID(createCallTO.getId());
                    relation.setFromType(RelationItem.TYPE_EVENT);
                    relation.setToID(linkTO.getItem_id());
                    relation.setToType(getEntityRelation(linkTO.getLink_type()));
                    relation.setToName(linkTO.getName());
                    relations.add(relation);
                }
                appointment.setRelations(relations);
            } else {
                try {
                    relationManager.deleteAllRelations(RelationItem.TYPE_EVENT, createCallTO.getId());
                } catch (Exception e) {
                    log.error("Api error occurred while deleting event relations", e);
                }
            }
        } else {//add mode
            if (createCallTO.getLinks() != null && createCallTO.getLinks().size() > 0) {

                ArrayList<RelationItem> relations = new ArrayList<>();

                for (LinkTO linkTO : createCallTO.getLinks()) {
                    RelationItem relation = new RelationItem();
                    relation.setFromType(RelationItem.TYPE_EVENT);
                    relation.setToID(linkTO.getItem_id());
                    relation.setToType(getEntityRelation(linkTO.getLink_type()));
                    relation.setToName(linkTO.getName());
                    relations.add(relation);
                }
                appointment.setRelations(relations);
            }

        }
        //End of Links

        //Share Withs
        if (createCallTO.getShare_with() != null) {
            //Guests first
            if (createCallTO.getShare_with().getEmails() != null) {
                List<SelectItem> guestsList = createCallTO.getShare_with().getEmails().stream().map(email -> new SelectItem(0, email, EVENT_GUEST_STATUS_PENDING)).collect(Collectors.toList());
                appointment.setGuests((ArrayList<SelectItem>) guestsList);
                appointment.setSendEmailNotification(Boolean.TRUE.equals(createCallTO.getShare_with().getSend_invites()));
            }
            //Share with employees
            ArrayList<Attendee> attendees = new ArrayList<>();

            if (createCallTO.getShare_with().getDepartments() != null && !createCallTO.getShare_with().getDepartments().isEmpty()) {
                for (ShareWithDepartmentsTO department : createCallTO.getShare_with().getDepartments()) {
                    //If all employees of department are selected
                    if (Boolean.TRUE.equals(department.getIs_all_selected())) {

                        Map<Integer, Integer> excludedEmployeeIds = department.getExcluded_employees_ids().stream().collect(Collectors.toMap(emplId -> emplId, emplId -> emplId));
                        List<EdsDepartment> alldepartments = departmentManager.getCompanyDepartments(user.getCompany());

                        if (alldepartments != null) {
                            for (EdsDepartment team : alldepartments) {
                                List<EdsEmployeeDepartment> teamEmployees = employeeDepartmentManager.getTeamEmployees(team.getObjectID());
                                if (teamEmployees != null) {
                                    for (EdsEmployeeDepartment teamEmployee : teamEmployees) {
                                        if (teamEmployee.getEmployee() != null && excludedEmployeeIds.get(teamEmployee.getEmployee().getObjectID()) == null) {
                                            attendees.add(new Attendee(teamEmployee.getEmployee().getObjectID(), true));
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        //Else If not all employees of department are selected
                        if (department.getPicked_employees_ids() != null) {
                            department.getPicked_employees_ids().forEach(employeeId -> {
                                attendees.add(new Attendee(employeeId, true));
                            });
                        }
                    }
                }
            }
            if (attendees.size() > 0) {
                appointment.setAttendees(attendees);
            } else {
                throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"share_with employees\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RestException(commonLocalizer.localize("sureEnteredAllData"), "\"share_with\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        //End of Share Withs

        //Reminders
        if (createCallTO.getReminders() != null) {
            ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
            for (TimeTO reminder : createCallTO.getReminders()) {
                if ((reminder.getHour() != null && reminder.getHour() > 0) || (reminder.getMinute() != null && reminder.getMinute() > 0)) {
                    CalendarEventReminder cer = new CalendarEventReminder();
                    cer.setValue(E_MAIL);

                    if (reminder.getHour() != null && reminder.getHour() > 0) {
                        cer.setReminderTimes(reminder.getHour() * 60);
                    } else {
                        cer.setReminderTimes(reminder.getMinute());
                    }
                    eventReminders.add(cer);
                }
            }
            if (!eventReminders.isEmpty()) {
                appointment.setReminder(eventReminders);
            }
        }
        //End of Reminders

        //Recurring
        if (createCallTO.getRecurrence() != null) {

            RecurrenceJobItem recurrenceJobItem = new RecurrenceJobItem();
            recurrenceJobItem.setJobType(RECURRING_EVENT);
            recurrenceJobItem.setStartDate(startDate);
//            item.setBusObjectId(recurrenceItem.getBusObjectId());
            recurrenceJobItem.setEnabled(Boolean.TRUE);
            if (createCallTO.getRecurrence().getRepeats() != null) {
                if ("DAILY".equalsIgnoreCase(createCallTO.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_DAILY);
                    recurrenceJobItem.setInterval(createCallTO.getRecurrence().getRepeats().getCount());
                    recurrenceJobItem.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);

                } else if ("WEEKLY".equalsIgnoreCase(createCallTO.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_WEEKLY);
                    if (createCallTO.getRecurrence().getRepeats().getSelected_days() != null) {

                        Map<String, String> selectedDays = createCallTO.getRecurrence().getRepeats().getSelected_days()
                                .stream().collect(Collectors.toMap(dayOfWeek -> dayOfWeek, dayOfWeek -> dayOfWeek));

                        recurrenceJobItem.setInterval(1);
                        recurrenceJobItem.setSunday(selectedDays.get("SUNDAY") != null);
                        recurrenceJobItem.setMonday(selectedDays.get("MONDAY") != null);
                        recurrenceJobItem.setTuesday(selectedDays.get("TUESDAY") != null);
                        recurrenceJobItem.setWednesday(selectedDays.get("WEDNESDAY") != null);
                        recurrenceJobItem.setThursday(selectedDays.get("THURSDAY") != null);
                        recurrenceJobItem.setFriday(selectedDays.get("FRIDAY") != null);
                        recurrenceJobItem.setSaturday(selectedDays.get("SATURDAY") != null);
                    }
                } else if ("MONTHLY".equalsIgnoreCase(createCallTO.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_MONTHLY);
                    recurrenceJobItem.setInterval(createCallTO.getRecurrence().getRepeats().getCount());
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);

                } else if ("YEARLY".equalsIgnoreCase(createCallTO.getRecurrence().getRepeats().getType())) {

                    recurrenceJobItem.setType(RECURRENCE_TYPE_YEARLY);
                    recurrenceJobItem.setInterval(createCallTO.getRecurrence().getRepeats().getCount());
                    if (StringUtils.isNotBlank(createCallTO.getRecurrence().getRepeats().getYearly_date())) {
                        try {
                            Date yearlyDate = longDateTimezoneFormat.parse(createCallTO.getWhen().getStart_date());
                            Calendar yYear = new GregorianCalendar();
                            yYear.setTime(yearlyDate);
                            recurrenceJobItem.setMonthlyOrYearlyDay(yYear.get(Calendar.DAY_OF_MONTH)); // 15 of 31 (or 30 or 28-29) day of month
                            recurrenceJobItem.setYearlyMonth(yYear.get(Calendar.MONTH) + 1);
                        } catch (Exception e) {
                            log.error("", e);
                            throw new RestException("Invalid date format", "Invalid date format for yearly_date. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                        }

                    }
                    recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                }

            }
//            item.setUserTimeZone(getTimeZone(recurrenceJobItem.getStartDate()));

            //Until (End Date)
            if (createCallTO.getRecurrence().getUntil() != null) {
                if ("DATE".equalsIgnoreCase(createCallTO.getRecurrence().getUntil().getType())) {
                    recurrenceJobItem.setEndType(END_BY_DATE);
                    try {
                        recurrenceJobItem.setEndDate(longDateTimezoneFormat.parse(createCallTO.getRecurrence().getUntil().getDate()));
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException("Invalid date format", "Invalid date format for until. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }

                } else if ("NUMBER_EVENTS".equalsIgnoreCase(createCallTO.getRecurrence().getUntil().getType())) {
                    recurrenceJobItem.setEndType(END_AFTER_OCCURRENCES);
                    if (createCallTO.getRecurrence().getUntil().getOccurences() != null && createCallTO.getRecurrence().getUntil().getOccurences() > 0) {
                        recurrenceJobItem.setOccurrence(createCallTO.getRecurrence().getUntil().getOccurences());
                    } else {
                        recurrenceJobItem.setOccurrence(1);
                    }
                } else {
                    recurrenceJobItem.setEndType(NO_END_DATE);
                }
            }
            //End of Until (End Date)

            if (recurrenceJobItem.getType().equals(RECURRENCE_TYPE_MONTHLY)) {
                recurrenceJobItem.setMonthlyOrYearlyDay(appointment.getStartDate().getDate());
                recurrenceJobItem.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                //recurrenceJobItem.setInterval(1);
            }
            Date normalizedEndDate = recurrenceJobItem.getEndDate();
            if (normalizedEndDate != null) {
                if (!(Boolean.TRUE.equals(createCallTO.getWhen().getAll_day()))) {
                    normalizedEndDate.setHours(endDate.getHours());
                    normalizedEndDate.setMinutes(endDate.getMinutes());
                    recurrenceJobItem.setEndDate(normalizedEndDate);
                } else {
                    normalizedEndDate.setHours(23);
                    normalizedEndDate.setMinutes(59);
                    recurrenceJobItem.setEndDate(normalizedEndDate);
                }
            }
            appointment.setRecurrenceJobItem(recurrenceJobItem);

        } else {
            appointment.setRecurrenceJobItem(null);
        }
        //End Of Recurring
        appointment.setRegisterNestedWorkflowEvents(false);

        //Custom Fields
        Integer eventId = null;
        if (createCallTO.getId() == null || createCallTO.getId() == 0) {

            appointment.setCustomFieldItems(convertCustomFields(createCallTO.getCustom_fields(), multipartRequest));

            try {
                SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(eventManager.getUser().getObjectID(), appointment, false);
                log.info("Save Call Log result:" + result.getId() + " " + result.getName());
                eventId = result.getId();
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            //If event is creating, upload the event attachments without merging
            if (eventAttachments.size() > 0) {
                FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_EVENT, eventId);
                for (MultipartFile file : eventAttachments) {
                    if (file.getName().matches(entityFileNameRegex)) {
                        try {
                            documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), eventId, null);
                        } catch (Exception e) {
                            log.error("", e);
                        }
                    }
                }
            }

        }
        //End Of Custom Fields

        if (createCallTO.getId() != null && createCallTO.getId() > 0) {

            ArrayList<CompanyCustomFieldItem> customFieldItems = null;
            LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
            if (createCallTO.getCustom_fields() != null && createCallTO.getCustom_fields().size() > 0) {
                customFieldItems = convertCustomFields(createCallTO.getCustom_fields(), customFieldDraftAttachmentMap);
            }

            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            ArrayList<FileResource> oldAttachments = new ArrayList<>();
            HashSet<Integer> deleteIDs = new HashSet<>();

            if (appointment.getCustomFieldItems() != null && appointment.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem companyCustomFieldItem : appointment.getCustomFieldItems()) {
                    if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                        ArrayList<FileResource> fileResources = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, companyCustomFieldItem.getEntityId(), companyCustomFieldItem.getObjectId());
                        if (fileResources != null && fileResources.size() > 0) {
                            oldAttachments.addAll(fileResources);
                        }
                    }
                }
            }

            //if draft attachments are empty, remove all old custom field attachments.
            if (customFieldDraftAttachmentMap.isEmpty()) {
                if (oldAttachments.size() > 0) {
                    List<Integer> oldAttachmentIDs = new ArrayList<>();
                    for (FileResource fileResource : oldAttachments) {
                        oldAttachmentIDs.add(fileResource.getObjectId());
                    }
                    try {
                        documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }
            } else {//if draft attachments do not match with cash advance old attachments by filename and file size, delete not matched old attachments
                if (oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    for (ArrayList<AttachmentTO> draftAttachments : customFieldDraftAttachmentMap.values()) {
                        for (AttachmentTO draftAttachment : draftAttachments) {
                            draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                        }
                    }
                    for (FileResource oldAttachment : oldAttachments) {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_CUSTOM_FIELD_ITEM, draftFilename);
                            if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        } else {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    }
                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }

                    //after delete old attachments, get not deleted attachment as old attachments
                    oldAttachments.clear();
                    if (appointment.getCustomFieldItems() != null && appointment.getCustomFieldItems().size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : appointment.getCustomFieldItems()) {
                            ArrayList<FileResource> fileResources = documentsServiceLocal.getFileResources(Constants.F_CUSTOM_FIELD_ITEM, companyCustomFieldItem.getEntityId(), companyCustomFieldItem.getObjectId());
                            if (fileResources != null && fileResources.size() > 0) {
                                oldAttachments.addAll(fileResources);
                            }
                        }
                    }
                }
            }


            try {
                if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                    FolderResource tempFolder = documentsServiceLocal.getTempFolderByCompany(user.getCompany().getObjectID());
                    //if old files are empty, upload new files
                    if (oldAttachments.size() == 0) {
                        if (eventAttachments != null && eventAttachments.size() > 0) {
                            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_EVENT, eventId);
                            for (MultipartFile file : eventAttachments) {
                                if (file.getName().matches(entityFileNameRegex)) {
                                    try {
                                        documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), eventId, null);
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            }
                        }
                        if (customFieldItems != null && customFieldItems.size() > 0) {
                            for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                                if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                                    ArrayList<FileItem> attachments = new ArrayList<>();
                                    for (MultipartFile multipartFile : customFieldAttachmentsMap.get(companyCustomFieldItem.getEntityId())) {
                                        FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                        FileItem fileItem = new FileItem();
                                        fileItem.setId(fileResource.getObjectId());
                                        fileItem.setFileName(fileResource.getFileName());
                                        attachments.add(fileItem);
                                    }
                                    companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));
                                }
                            }
                        }
                    } else {//If old files aren't empty, merge old and new files
                        deleteIDs = new HashSet<>();
                        LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                        for (FileResource file : oldAttachments) {
                            oldFilesMap.put(file.getFileName(), file);
                        }

                        for (ArrayList<MultipartFile> multipartFiles : customFieldAttachmentsMap.values()) {
                            for (MultipartFile multipartFile : multipartFiles) {
                                FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                                if (oldFile != null) {
                                    deleteIDs.add(oldFile.getObjectId());
                                }
                            }
                        }

                        if (deleteIDs.size() > 0) {
                            try {
                                documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                            } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                                log.error("", e);
                            }
                        }

                        if (customFieldItems != null && customFieldItems.size() > 0) {
                            for (CompanyCustomFieldItem companyCustomFieldItem : customFieldItems) {
                                if (Constants.UI_TYPE_FILE_UPLOAD_WIDGET.equals(companyCustomFieldItem.getUiType()) || Constants.UI_TYPE_FILE_UPLOAD_ITEM.equals(companyCustomFieldItem.getUiType())) {
                                    ArrayList<FileItem> attachments = new ArrayList<>();
                                    for (MultipartFile multipartFile : customFieldAttachmentsMap.get(companyCustomFieldItem.getEntityId())) {
                                        FileResource fileResource = documentsServiceLocal.saveDocumentFile(multipartFile, tempFolder.getObjectId(), Constants.F_CUSTOM_FIELD_ITEM, null, "");
                                        FileItem fileItem = new FileItem();
                                        fileItem.setId(fileResource.getObjectId());
                                        fileItem.setFileName(fileResource.getFileName());
                                        attachments.add(fileItem);
                                    }
                                    companyCustomFieldItem.setAttachments(attachments.toArray(new FileItem[]{}));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("", e);
            }


            if (customFieldItems != null && customFieldItems.size() > 0) {
                appointment.setCustomFieldItems(customFieldItems);
            }

            try {
                SelectItem result = googleCalendarServiceLocal.saveCalendarEvent(user.getObjectID(), appointment, false);
                log.info("Save Call Log result:" + result.getId() + " " + result.getName());
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            ///////////////// SAVE EVENT ATTACHMENTS /////////////////////////////////////


            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            oldAttachments = documentsServiceLocal.getFileResources(Constants.F_EVENT, createCallTO.getId(), createCallTO.getId());
            deleteIDs = new HashSet<>();
            //if draft attachments are empty, remove all old cash advance attachments.
            if (createCallTO.getDraft_attachments() == null || createCallTO.getDraft_attachments().size() == 0) {
                if (oldAttachments != null && oldAttachments.size() > 0) {
                    List<Integer> oldAttachmentIDs = new ArrayList<>();
                    for (FileResource fileResource : oldAttachments) {
                        oldAttachmentIDs.add(fileResource.getObjectId());
                    }
                    try {
                        documentsServiceLocal.deleteFiles(oldAttachmentIDs);
                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                        log.error("", e);
                    }
                }
                //if draft attachments do not match with cash advance old attachments by filename and file size, delete not matched old attachments
            } else if (createCallTO.getDraft_attachments() != null && createCallTO.getDraft_attachments().size() > 0) {
                if (oldAttachments != null && oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    for (AttachmentTO draftAttachment : createCallTO.getDraft_attachments()) {
                        draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                    }
                    for (FileResource oldAttachment : oldAttachments) {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_EVENT, draftFilename);
                            if (fileResource != null && !fileResource.getContentLength().equals(oldAttachment.getContentLength())) {
                                deleteIDs.add(oldAttachment.getObjectId());
                            }
                        } else {
                            deleteIDs.add(oldAttachment.getObjectId());
                        }
                    }
                    if (deleteIDs.size() > 0) {
                        try {
                            documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                            log.error("", e);
                        }
                    }
                    //after delete old attachments, get not deleted attachment as old attachments
                    oldAttachments = documentsServiceLocal.getFileResources(Constants.F_EVENT, createCallTO.getId(), createCallTO.getId());
                }
            }
            uploadFiles(createCallTO.getId(), Constants.F_EVENT, null, eventAttachments, oldAttachments);

        }


        return successResponse(new ResponseData());
    }


}
