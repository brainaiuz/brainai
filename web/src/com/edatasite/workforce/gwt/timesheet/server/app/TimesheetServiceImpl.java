package com.edatasite.workforce.gwt.timesheet.server.app;

import au.com.bytecode.opencsv.CSVReader;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.log.KpiLog;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsAttendanceRawData;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsMonthlyTimesheet;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectEmployeeWageClientRateHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTaskComment;
import com.edatasite.workforce.core.domain.EdsTaskEstimateTimeSpentHistory;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsTimeSheetSettings;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.enums.EmployeeAssignmentEnum;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReportResult;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TimesheetSummary;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.AttendanceRawDataManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.MonthlyTimesheetManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.RolePermissionManager;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.TaskCommentManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetApprovalSessionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeTaskEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.StatusTaskEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TaskSolrEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.TimeSheetEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.dashboard.client.rpc.DashboardService;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeSheetSuggestItem;
import com.edatasite.workforce.gwt.timesheet.client.TimesheetConstants;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.StatData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.SuggestionResponseDTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskStatus;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalListItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalSingleItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalSingleItemsList;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntriesPerPeriod;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetEntry;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetReportItemTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetReportTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetFilterData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetProjectItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetItem;
import com.edatasite.workforce.rest.base.to.TimesheetWeeklyEntryTO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.timesheet.ProjectTaskForMobile;
import com.workforcetrack.mobile.rpc.timesheet.ProjectTreeForMobile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;

import static com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum.ENABLE_MULTI_PROJECT_TO_TIMESHEET;

@Transactional
@Service("timesheetService")
public class TimesheetServiceImpl implements TimesheetService, TimesheetServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(TimesheetServiceImpl.class);

    private final String apiKey = SpringPropertiesUtil.getProperty("openai.api.key");

    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TimeSheetManager timesheetManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TaskCommentManager taskCommentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private TimeSheetApprovalSessionManager timeSheetApprovalSessionManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private TimeSheetSettingsManager timesheetSettingsManager;
    @Autowired
    private ReportService reportService;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private AttendanceRawDataManager attendanceRawDataManager;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private RolePermissionManager rolePermissionManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private MonthlyTimesheetManager monthlyTimesheetManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;


    private static final Map<String, ComparatorFactory<MonthlyTimesheetItem>> comparatorFactoryMonthlyTimesheet = new HashMap<>();

    static {
        comparatorFactoryMonthlyTimesheet.put("employeeName",
                sortOrder -> new AbstractComparator<MonthlyTimesheetItem>() {
                    public int compare(MonthlyTimesheetItem o1, MonthlyTimesheetItem o2) {
                        return internalCompare(o1.getEmployeeName(), o2.getEmployeeName(), sortOrder);
                    }
                }
        );
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<ProjectTreeForMobile> getDataForMobile(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        List<EdsReference> statusList = referenceManager.listReferences("_TIME_SHEET_ENTRY_STATUS");
        EdsReference approved = null;
        EdsReference waiting = null;
        EdsReference rejected = null;
        for (EdsReference reference : statusList) {
            if ("_APPROVE".equals(reference.getCode())) {
                approved = reference;
            } else if ("_WAITING".equals(reference.getCode())) {
                waiting = reference;
            } else if ("_REJECT".equals(reference.getCode())) {
                rejected = reference;
            }
        }
        ArrayList<ProjectTreeForMobile> result = new ArrayList<>();
        HashMap<SelectItem, ArrayList<ProjectTaskForMobile>> projectTasks = new HashMap<>();
        EdsEmployee employee = (EdsEmployee) taskManager.getUser();

        List<EdsEmployeeTask> employeeTaskList = employeeTaskManager.getTaskListForMobile(employee, calendar.getTime(), null);
        for (EdsEmployeeTask employeeTask : employeeTaskList) {
            EdsProject domainProject = employeeTask.getTask().getProject();
            SelectItem project = new SelectItem(domainProject.getObjectID(), domainProject.getName());
            if (projectTasks.containsKey(project)) {
                ArrayList<ProjectTaskForMobile> tasks = projectTasks.get(project);
                tasks.add(getProjectTask(employeeTask, calendar.getTime(), approved, waiting, rejected));
            } else {
                ArrayList<ProjectTaskForMobile> tasks = new ArrayList<>();
                tasks.add(getProjectTask(employeeTask, calendar.getTime(), approved, waiting, rejected));
                projectTasks.put(project, tasks);
            }
        }

        for (SelectItem project : projectTasks.keySet()) {
            result.add(new ProjectTreeForMobile(project.getId(), project.getName(), projectTasks.get(project)));
        }

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProjectTaskForMobile getProjectTask(EdsEmployeeTask employeeTask, Date date, EdsReference approved, EdsReference waiting, EdsReference rejected) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        date = calendar.getTime();
        Integer workedTime = 0;
        Integer totalWorkedTime = timesheetManager.getWorkedTimeForMobile(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), employeeTask.getTask().getObjectID(), null);
        List<EdsTimeSheet> timeSheetList = timesheetManager.getTimeSheets(employeeTask.getTask().getObjectID(), employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID(), date);
        List<Integer> oldEmployeeTaskList = new ArrayList<>();
        EdsTimeSheet timesheet = null;
        for (EdsTimeSheet ts : timeSheetList) {
            workedTime += ts.getTimeSpent();
            if (!ts.getEmployeeTask().getDeleted()) {
                timesheet = ts;
            } else if (ts.getTimeSpent() > 0) {
                oldEmployeeTaskList.add(ts.getEmployeeTask().getObjectID());
            }
        }
        Integer timesheetID = null;
        int status = 0;
        if (timesheet != null) {
            timesheetID = timesheet.getObjectID();
            if (timesheet.getStatus() == null) {
                status = TIMESHEET_ENTRY_NOTSUBMITTED;
            } else if (timesheet.getStatus().equals(approved)) {
                status = TIMESHEET_ENTRY_APPROVED;
            } else if (timesheet.getStatus().equals(waiting)) {
                status = TIMESHEET_ENTRY_WAITING;
            } else if (timesheet.getStatus().equals(rejected)) {
                status = TIMESHEET_ENTRY_REJECTED;
            }
        }

        EdsReference taskStatus = employeeTask.getTask().getStatus();
        Integer taskStatusID = taskStatus != null ? taskStatus.getObjectID() : null;
        ProjectTaskForMobile projectTask = new ProjectTaskForMobile(timesheetID, employeeTask.getObjectID(), employeeTask.getTask().getName(), taskStatusID);

        MNumberData numberData = new MNumberData(employeeTask.getTask().getNumber(), employeeTask.getTask().getIntNumber());
        projectTask.setNumberData(numberData);

        projectTask.setDailyWorkedMinutes(workedTime);
        projectTask.setTotalWorkedMinutes(totalWorkedTime);
        projectTask.setStatus(status);
        projectTask.setComment(timesheet != null ? timesheet.getComment() : null);
        projectTask.setStartDate(employeeTask.getTask().getStartDate());
        projectTask.setEndDate(employeeTask.getTask().getDueDate());
        projectTask.setPercentCompleted(employeeTask.getPercent() == null ? 0 : employeeTask.getPercent());
        if (timesheet != null && timesheet.getEmployeeTask().getDeleted()) {
            projectTask.setOldEmployeeTaskID(timesheet.getEmployeeTask().getObjectID());
        }
        projectTask.setOldEmployeeTaskIDList(oldEmployeeTaskList);
        return projectTask;
    }

    public void updateTimesheetForMobile(ProjectTaskForMobile[] items) {
        for (ProjectTaskForMobile item : items) {
            EdsUser employee = referenceManager.getUser();
            if (item.getOldEmployeeTaskIDList() != null && item.getOldEmployeeTaskIDList().size() > 0) {
                for (Integer oldEmployeeTaskID : item.getOldEmployeeTaskIDList()) {
                    EdsTimeSheet oldTimeSheet = timesheetManager.getTimeSheet(employeeTaskManager.get(oldEmployeeTaskID), item.getDate());
                    oldTimeSheet.setTimeSpent(0);
                }
            }
            EdsEmployeeTask employeeTask = employeeTaskManager.get(item.getEmployeeTaskID());
            EdsTask task = employeeTask.getTask();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(item.getDate());
            calendar.set(Calendar.AM_PM, 0);
            calendar.set(Calendar.HOUR, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            EdsTimeSheet testTimeSheet = timesheetManager.getTimeSheet(employeeTask, calendar.getTime());
            TimeZone timeZone = employee.getUserTimezone();
            EdsTimeSheet timeSheet = null;
            int oldTimespent = 0;
            boolean wasEnteredNowAutoApproving = false;
            EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
            if (testTimeSheet == null) {
                if (item.getDailyWorkedMinutes() > 0) {
                    timeSheet = new EdsTimeSheet();
                    timeSheet.setDate(item.getDate());
                    timesheetManager.create(timeSheet);
                } else {
                    continue;
                }
            } else {
                timeSheet = testTimeSheet;
                if (!timeSheet.getAutoApproved() && timeSheet.getTimeSpent() > 0) {
                    wasEnteredNowAutoApproving = true;
                }
                oldTimespent = testTimeSheet.getTimeSpent();
                if (item.getDailyWorkedMinutes() == 0) {
                    boolean wasApprovedNowRejecting = false;
                    timeSheet.setTimeSpent(0);
                    timeSheet.setEntryDate(null);
                    if (timeSheet.getDailyEstimatedTime() == null || timeSheet.getDailyEstimatedTime() == 0) {
                        timesheetManager.delete(timeSheet);
                    } else {
                        timesheetManager.update(timeSheet);
                    }
                    updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getDailyWorkedMinutes());
                    int difference = timeSheet.getTimeSpent() - oldTimespent;
                    if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
                        Integer timeSpent = 0;
                        if (!timeSheet.getAutoApproved()) {
                            timeSpent = timeSheet.getTimeSpent();
                            timeSheet.setStatus(null);
                        } else {
                            timeSpent = difference;
                            wasApprovedNowRejecting = true;
                        }
                        EdsTimeSheet tempTimesheet = new EdsTimeSheet();
                        tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
                        tempTimesheet.setStatus(timeSheet.getStatus());
                        tempTimesheet.setTimeSpent(timeSpent);
                        updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());
                    }
                    dashboardService.lastEnteredDate();
                    saveAttendanceRawDataTimesheet(timeSheet, difference, wasApprovedNowRejecting, wasEnteredNowAutoApproving);
                    continue;
                }
            }

            updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getDailyWorkedMinutes());
            timeSheet.setEmployeeTask(employeeTask);
            timeSheet.setTaskID(task.getObjectID());
            timeSheet.setProjectID(task.getProject().getObjectID());
            timeSheet.setComment(item.getComment());
            timeSheet.setEmployeeID(employeeTaskManager.get(item.getEmployeeTaskID()).getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID());
            timeSheet.setTeamID(employeeTaskManager.get(item.getEmployeeTaskID()).getProjectEmployee().getEmployeeDepartment().getTeam().getObjectID());

            if (timeSheet.getTimeSpent() == null || !timeSheet.getTimeSpent().equals(item.getDailyWorkedMinutes())) {
                timeSheet.setEntryDate(new Date());
            }
            timeSheet.setTimeSpent(item.getDailyWorkedMinutes());

            if (timeSheet.getClientChargeRate() == null || timeSheet.getClientChargeRate().intValue() == 0) {
                EdsProjectEmployeeWageClientRateHistory clientWageRate = timesheetManager.getProjectEmployeeWageClientRateByDate(timeSheet.getDate(), employeeTask.getProjectEmployee().getObjectID());
                if (clientWageRate != null) {
                    timeSheet.setClientChargeRate(clientWageRate.getClientChargeRate());
                    timeSheet.setWageRate(clientWageRate.getWageRate());
                }
            }

            if (item.getComment() != null && !(item.getComment().trim().equals(""))) {
                EdsTaskComment comment = new EdsTaskComment();
                comment.setTask(task);
                comment.setCreationDate(item.getDate());
                comment.setUser(employee);
                comment.setText(item.getComment() + " (" + ServerUtils.timeSpentToString(item.getDailyWorkedMinutes()) + " entered) ");
                taskCommentManager.create(comment);
            }

            EdsReference reject = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT");
            if (timeSheet.getStatus() != null && timeSheet.getStatus().equals(reject)) {
                timeSheet.setStatus(null);
            }
            //create new one year if today's date is not in datejoin table
            dashboardService.lastEnteredDate();
            int difference = timeSheet.getTimeSpent() - oldTimespent;
            if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
                Integer timeSpent = 0;
                if (!timeSheet.getAutoApproved()) {
                    timeSpent = timeSheet.getTimeSpent();
                } else {
                    timeSpent = difference;
                }
                timeSheet.setStatus(referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE"));
                timeSheet.setApprovalDate(new Date());
                timeSheet.setRejectedDate(null);
                timeSheet.setManagerComment("Automatically approved by the system");
                timeSheet.setAutoApproved(true);
                EdsTimeSheet tempTimesheet = new EdsTimeSheet();
                tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
                tempTimesheet.setStatus(timeSheet.getStatus());
                tempTimesheet.setTimeSpent(timeSpent);
                updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());
            }
            saveAttendanceRawDataTimesheet(timeSheet, difference, false, wasEnteredNowAutoApproving);
            sendNotificationActualTimeReached(timeSheet);
        }
    }

    private void sendNotificationActualTimeReached(EdsTimeSheet timeSheet) {
        if (timeSheet.getTaskID() != null) {
            EdsTask updateTask = taskManager.get(timeSheet.getTaskID());
            Double taskActualTimeSpent = timesheetManager.getTaskTimeSpents(timeSheet.getTaskID().toString(), "_APPROVE").get(timeSheet.getTaskID());
            if (updateTask != null && taskActualTimeSpent != null && updateTask.getEstimatedTime() != 0 && taskActualTimeSpent >= updateTask.getEstimatedTime() && !updateTask.isSentActualTimeReachedNotifation()) {
                try {
                    messageManager.sentActualTimeReachedNotifation(updateTask);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetFilterData getProjectsAndWorkstreams(Integer weekOffset, ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        Integer selectedProjectId = fp.getProjectId();

        TimesheetFilterData result = new TimesheetFilterData();

        HashSet<SelectItem> projects = new HashSet<>();
        HashSet<SelectItem> clients = new HashSet<>();
        HashSet<SelectItem> workstreams = new HashSet<>();
        Map<String, SelectItem> workstreamsMap = new HashMap<>();

        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        if (fp.getEmployeeId() != null) {
            employee = employeeManager.get(fp.getEmployeeId());
        }

        List<Integer> projectIdList = new ArrayList<>();

        List<EdsProjectEmployee> projectEmployees = projectManager.getEmployeeNotStartedOnGoingProjects(employee);
        for (EdsProjectEmployee projectEmployee : projectEmployees) {
            EdsProject domainProject = projectEmployee.getProject();
            if (domainProject == null) {
                continue;
            }
            projectIdList.add(domainProject.getObjectID());
            EdsCrmAccount client = domainProject.getClient();
            int clientId = 0;

            if (client != null) {
                SelectItem cItem = new SelectItem(client.getObjectID(), client.getName());
                clients.add(cItem);
                clientId = client.getObjectID();
            }

            if (domainProject.getClients() != null && !domainProject.getClients().isEmpty()) {
                for (EdsCrmAccount c : domainProject.getClients()) {
                    clients.add(new SelectItem(c.getObjectID(), c.getName()));
                }
            }
            SelectItem project = new SelectItem(domainProject.getObjectID(), domainProject.getName(), String.valueOf(clientId));
            projects.add(project);

        }
        SelectItem[] projectItems = projects.toArray(new SelectItem[]{});
        Arrays.sort(projectItems, Comparator.comparing(SelectItem::getName));

        result.setProjects(projectItems);

        SelectItem[] clientItems = clients.toArray(new SelectItem[]{});
        Arrays.sort(clientItems, Comparator.comparing(SelectItem::getName));
        result.setClients(clientItems);

        List<EdsWorkStream> projectWorkStreamList;
        if (selectedProjectId == null) {
            projectWorkStreamList = workStreamManager.listByProjectIds(ServerUtils.getAsCommoDelimited(projectIdList, "0", ","));
        } else {
            projectWorkStreamList = workStreamManager.listByProjectId(selectedProjectId);
        }

        for (EdsWorkStream edsWorkStream : projectWorkStreamList) {
            workstreamsMap.put(edsWorkStream.getName(), new SelectItem(edsWorkStream.getObjectID(), edsWorkStream.getName()));
        }

        for (Map.Entry<String, SelectItem> stringSelectItemEntry : workstreamsMap.entrySet()) {
            workstreams.add(stringSelectItemEntry.getValue());
        }

        SelectItem[] workstreamItems = workstreams.toArray(new SelectItem[]{});
        Arrays.sort(workstreamItems, Comparator.comparing(SelectItem::getName));
        result.setWorkstreams(workstreamItems);

        return result;
    }

    /**
     * @param weekOffset
     * @param fp         - task filters
     * @return the data to fill the table in TimesheetView.class
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetData getData(DateNonConvertable clientsCurrentDate, int weekOffset, ListingFilterParameter fp) {
        Date timer = new Date();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        if (fp.getEmployeeId() != null) {
            employee = employeeManager.get(fp.getEmployeeId());
        }

        Calendar todayCal = new GregorianCalendar();

        todayCal.setTime(clientsCurrentDate.getNonConvertedDate());
        todayCal.set(Calendar.AM_PM, 0);
        todayCal.set(Calendar.HOUR, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        Calendar ytdCal = new GregorianCalendar();
        ytdCal.setTime(clientsCurrentDate.getNonConvertedDate());
        ytdCal.set(Calendar.AM_PM, 0);
        ytdCal.add(Calendar.DAY_OF_YEAR, -1);
        ytdCal.set(Calendar.HOUR, 0);
        ytdCal.set(Calendar.MINUTE, 0);
        ytdCal.set(Calendar.SECOND, 0);
        ytdCal.set(Calendar.MILLISECOND, 0);

        DateNonConvertable[] dates = getTimesheetWeeklyDates(clientsCurrentDate, weekOffset);

        TimesheetData result = new TimesheetData();
        DateNonConvertable clientsServersideToday = new DateNonConvertable();
        result.setClientsToday(clientsServersideToday);
        result.setDates(dates);
        Calendar startDate = new GregorianCalendar();
        startDate.setTime(dates[0].getNonConvertedDate());
        startDate.set(Calendar.AM_PM, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = new GregorianCalendar();
        endDate.setTime(dates[6].getNonConvertedDate());
        endDate.set(Calendar.AM_PM, 0);
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 0);
        List<EdsEmployeeTask> taskList = employeeTaskManager.listDueTasks(employee, startDate.getTime(), endDate.getTime(), fp);

        result.setEmployeeId(employee.getObjectID());
        Map<Integer, TaskTransfer> taskTransferMap = new HashMap<>();
        StringBuilder taskIds = new StringBuilder();
        int i = 0;
        Map<String, Integer> undeletedEmployeeTasks = new HashMap<>();
        for (EdsEmployeeTask employeeTask : taskList) {
            if (taskTransferMap.containsKey(employeeTask.getTask().getObjectID())) {
                continue;
            }
            undeletedEmployeeTasks.put(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeeTask.getTask().getObjectID(), employeeTask.getObjectID());
            TaskTransfer transferTask = new TaskTransfer();
            taskTransferMap.put(employeeTask.getTask().getObjectID(), transferTask);
            EdsProject domainProject = employeeTask.getProjectEmployee().getProject();
            EdsCrmAccount client = domainProject.getClient();

            if (employeeTask.getTask() instanceof EdsIssue) {
                transferTask.setIsIssue(true);
            } else {
                transferTask.setIsIssue(false);
            }

            transferTask.setEmplTaskId(employeeTask.getObjectID());
            transferTask.setEmplTaskName(employeeTask.getTask().getName());

            if (client != null) {
                transferTask.setClientName(client.getName());
            } else {
                transferTask.setClientName("N/A");
            }

            transferTask.setProjectId(domainProject.getObjectID());
            transferTask.setProjectName(domainProject.getName());
            transferTask.getTaskStatus().setStatus(employeeTask.getStatus().getObjectID());
            transferTask.getTaskStatus().setStatusName(referenceWfmMessageSource.localizeRef(employeeTask.getStatus()));
            transferTask.getTaskStatus().setTaskId(employeeTask.getObjectID());
            transferTask.setEstimatedTime(employeeTask.getEstimatedTime());
            transferTask.setPercentCompleted(employeeTask.getPercent() == null ? 0 : employeeTask.getPercent());
            transferTask.setTaskId(employeeTask.getTask().getObjectID());

            Calendar emplTaskStart = new GregorianCalendar();
            if (employeeTask.getStartDate() != null) {
                emplTaskStart.setTime(employeeTask.getStartDate());
            } else {
                emplTaskStart.setTime(employeeTask.getTask().getStartDate());
            }

            emplTaskStart.set(Calendar.AM_PM, 0);
            emplTaskStart.set(Calendar.HOUR, 0);
            emplTaskStart.set(Calendar.MINUTE, 0);
            emplTaskStart.set(Calendar.SECOND, 0);
            emplTaskStart.set(Calendar.MILLISECOND, 0);
            transferTask.setStartDate(emplTaskStart.getTime());
            if (employeeTask.getEndDate() != null) {
                Calendar emplTaskEnd = new GregorianCalendar();
                emplTaskEnd.setTime(employeeTask.getEndDate());
                emplTaskEnd.set(Calendar.AM_PM, 0);
                emplTaskEnd.set(Calendar.HOUR, 23);
                emplTaskEnd.set(Calendar.MINUTE, 59);
                emplTaskEnd.set(Calendar.SECOND, 59);
                emplTaskEnd.set(Calendar.MILLISECOND, 0);
                transferTask.setEndDate(emplTaskEnd.getTime());
            }
            taskIds.append(employeeTask.getTask().getObjectID().toString());
            if (i != taskList.size() - 1) {
                taskIds.append(",");
            }
            transferTask.setTotalMinutes(0);
            i++;
        }

        if (taskIds.length() > 0 && taskIds.charAt(taskIds.length() - 1) == ',') {
            taskIds = new StringBuilder(taskIds.substring(0, taskIds.length() - 1));
        }

        if (!"".contentEquals(taskIds)) {
            List emplTaskTimeSheetTotal = timesheetManager.getEmployeeTaskTotalTimeSheets(taskIds.toString(), employee.getObjectID());
            for (Object anEmplTaskTimeSheetTotal : emplTaskTimeSheetTotal) {
                Object[] obj = (Object[]) anEmplTaskTimeSheetTotal;
                Integer taskId = (Integer) obj[0];
                Long totalMinuts = (Long) obj[1];
                taskTransferMap.get(taskId).setTotalMinutes(totalMinuts != null ? totalMinuts.intValue() : 0);
            }
        }

        i = 0;
        TaskTransfer[] transferTasks = new TaskTransfer[taskTransferMap.size()];
        for (TaskTransfer transfer : taskTransferMap.values()) {
            transferTasks[i] = transfer;
            i++;
        }

        Arrays.sort(transferTasks, Comparator.comparing(TaskTransfer::getProjectId));

        TimesheetFilterData timesheetFilterData = getProjectsAndWorkstreams(weekOffset, fp);

        result.setProjects(timesheetFilterData.getProjects());

        result.setClients(timesheetFilterData.getClients());

        result.setWorkstream(timesheetFilterData.getWorkstreams());

        result.setTransferTasks(transferTasks);

        List<EdsTimeSheet> timesheets = timesheetManager.list(employee, dates[0].getNonConvertedDate(), dates[6].getNonConvertedDate());

        Map<String, TimesheetSummary> timesheetSummaryMap = new HashMap<>();
        for (EdsTimeSheet timeSheet : timesheets) {
            if (timesheetSummaryMap.containsKey(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID())) {
                timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).setMinutes(timeSheet.getTimeSpent());
                if (!timeSheet.getEmployeeTask().getDeleted()) {
                    timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).setTimeSheet(timeSheet);
                } else if (timeSheet.getTimeSpent() > 0) {
                    timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).getOldEmployeeTaskIDList().add(timeSheet.getEmployeeTask().getObjectID());
                }
            } else {
                TimesheetSummary timesheetSummary = new TimesheetSummary();
                timesheetSummary.setMinutes(timeSheet.getTimeSpent());
                timesheetSummary.setTimeSheet(timeSheet);
                if (timeSheet.getEmployeeTask().getDeleted() && timeSheet.getTimeSpent() > 0) {
                    ArrayList<Integer> oldEmployeeTaskIDList = new ArrayList<>();
                    oldEmployeeTaskIDList.add(timeSheet.getEmployeeTask().getObjectID());
                    timesheetSummary.setOldEmployeeTaskIDList(oldEmployeeTaskIDList);
                }
                timesheetSummaryMap.put(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID(), timesheetSummary);
            }
        }

        TimesheetDataItem[] items = new TimesheetDataItem[timesheetSummaryMap.size()];
        i = 0;
        List<EdsReference> statusList = referenceManager.listReferences("_TIME_SHEET_ENTRY_STATUS");
        EdsReference approved = null;
        EdsReference waiting = null;
        EdsReference rejected = null;
        for (EdsReference reference : statusList) {
            if ("_APPROVE".equals(reference.getCode())) {
                approved = reference;
            } else if ("_WAITING".equals(reference.getCode())) {
                waiting = reference;
            } else if ("_REJECT".equals(reference.getCode())) {
                rejected = reference;
            }
        }
        for (TimesheetSummary timesheetSummary : timesheetSummaryMap.values()) {
            EdsTimeSheet timesheet = timesheetSummary.getTimeSheet();
            items[i] = new TimesheetDataItem();
            items[i].setDate(new Date(timesheet.getDate().getTime()));  // Non Convertable date is being used inside the TimeSheetDataItem
            items[i].setId(timesheet.getObjectID());
            items[i].setComment(timesheet.getComment());
            items[i].setMinutes(timesheetSummary.getMinutes());
            items[i].setAutoApproved(timesheet.getAutoApproved());
            if (timesheet.getStatus() == null) {
                items[i].setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
            } else if (timesheet.getStatus().equals(approved)) {
                items[i].setStatus(TIMESHEET_ENTRY_APPROVED);
            } else if (timesheet.getStatus().equals(waiting)) {
                items[i].setStatus(TIMESHEET_ENTRY_WAITING);
            } else if (timesheet.getStatus().equals(rejected)) {
                items[i].setStatus(TIMESHEET_ENTRY_REJECTED);
            }
            items[i].setTeamID(timesheet.getTeamID());
            items[i].setEmployeeID(timesheet.getEmployeeID());
            items[i].setProjectID(timesheet.getProjectID());
            items[i].setTaskID(timesheet.getTaskID());
            items[i].setOldEmployeeTaskIDList(timesheetSummary.getOldEmployeeTaskIDList());
            if (timesheet.getEmployeeTask().getDeleted()) {
                if (undeletedEmployeeTasks.containsKey(timesheet.getEmployeeID() + "/" + timesheet.getTaskID())) {
                    items[i].setOldEmployeeTaskID(timesheet.getEmployeeTask().getObjectID());
                    items[i].setEmployeeTaskID(undeletedEmployeeTasks.get(timesheet.getEmployeeID() + "/" + timesheet.getTaskID()));
                }
            } else {
                items[i].setEmployeeTaskID(timesheet.getEmployeeTask().getObjectID());
            }

            i++;
        }
        result.setItems(items);
        if (employee.getStartDate() != null && employee.getStartDate().after(dates[6].getNonConvertedDate())) {
            result.setLastWeek(true);// we need to show exact weekdays
        } // in employee's timesheet (not the whole week)
        result.setToday(new DateNonConvertable(todayCal.getTime()));
        result.setYesterday(new DateNonConvertable(ytdCal.getTime()));

        result.setDailyStatistics(getDailyStatistics(dates, employee)); //statistics should be calculated for all projects not per project for Robert mainly
        result.setWeeklyStatistics(getWeeklyStatistics(dates[0].getNonConvertedDate(), employee));

        Calendar selectedDate = GregorianCalendar.getInstance();
        selectedDate.set(Calendar.DAY_OF_MONTH, fp.getSelectedDay());
        selectedDate.set(Calendar.MONTH, fp.getSelectedMonth() - 1);
        selectedDate.set(Calendar.YEAR, fp.getSelectedYear());

        result.setMonthlyStatistices(getMonthlyStatistics(selectedDate.getTime(), employee));

        List<EdsAttendanceRawData> attendanceRawDataList = attendanceRawDataManager.getAttendanceRawDataByDates(startDate.getTime(), endDate.getTime(), employee.getObjectID());
        int[] plannedTimes = new int[7];
        int[] actualPlannedTimes = new int[7];
        if (attendanceRawDataList != null && attendanceRawDataList.size() > 0) {
            for (EdsTimeSlotItem item : employee.getTimeSlot().getItems()) {
                switch (item.getDay()) {
                    case 0 -> {
                        plannedTimes[0] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(0).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[0] = 0;
                        } else if (attendanceRawDataList.get(0).getLeave() > 0) {
                            actualPlannedTimes[0] = attendanceRawDataList.get(0).getTimeSlot() - attendanceRawDataList.get(0).getLeave();
                        } else {
                            actualPlannedTimes[0] = attendanceRawDataList.get(0).getTimeSlot();
                        }
                    }
                    case 1 -> {
                        plannedTimes[1] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(1).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[1] = 0;
                        } else if (attendanceRawDataList.get(1).getLeave() > 0) {
                            actualPlannedTimes[1] = attendanceRawDataList.get(1).getTimeSlot() - attendanceRawDataList.get(1).getLeave();
                        } else {
                            actualPlannedTimes[1] = attendanceRawDataList.get(1).getTimeSlot();
                        }
                    }
                    case 2 -> {
                        plannedTimes[2] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(2).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[2] = 0;
                        } else if (attendanceRawDataList.get(2).getLeave() > 0) {
                            actualPlannedTimes[2] = attendanceRawDataList.get(2).getTimeSlot() - attendanceRawDataList.get(2).getLeave();
                        } else {
                            actualPlannedTimes[2] = attendanceRawDataList.get(2).getTimeSlot();
                        }
                    }
                    case 3 -> {
                        plannedTimes[3] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(3).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[3] = 0;
                        } else if (attendanceRawDataList.get(3).getLeave() > 0) {
                            actualPlannedTimes[3] = attendanceRawDataList.get(3).getTimeSlot() - attendanceRawDataList.get(3).getLeave();
                        } else {
                            actualPlannedTimes[3] = attendanceRawDataList.get(3).getTimeSlot();
                        }
                    }
                    case 4 -> {
                        plannedTimes[4] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(4).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[4] = 0;
                        } else if (attendanceRawDataList.get(4).getLeave() > 0) {
                            actualPlannedTimes[4] = attendanceRawDataList.get(4).getTimeSlot() - attendanceRawDataList.get(4).getLeave();
                        } else {
                            actualPlannedTimes[4] = attendanceRawDataList.get(4).getTimeSlot();
                        }
                    }
                    case 5 -> {
                        plannedTimes[5] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(5).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[5] = 0;
                        } else if (attendanceRawDataList.get(5).getLeave() > 0) {
                            actualPlannedTimes[5] = attendanceRawDataList.get(5).getTimeSlot() - attendanceRawDataList.get(5).getLeave();
                        } else {
                            actualPlannedTimes[5] = attendanceRawDataList.get(5).getTimeSlot();
                        }
                    }
                    case 6 -> {
                        plannedTimes[6] = calculatePlannedTime(item);
                        if (attendanceRawDataList.get(6).getHoliday() || attendanceRawDataList.get(0).getDayOff()) {
                            actualPlannedTimes[6] = 0;
                        } else if (attendanceRawDataList.get(6).getLeave() > 0) {
                            actualPlannedTimes[6] = attendanceRawDataList.get(6).getTimeSlot() - attendanceRawDataList.get(6).getLeave();
                        } else {
                            actualPlannedTimes[6] = attendanceRawDataList.get(6).getTimeSlot();
                        }
                    }
                }
            }
        }
        TimeslotItem timeslotItem = new TimeslotItem();

        int monthlyPlannedTime = attendanceRawDataManager.getMonthlyPlanned(fp.getSelectedYear(), fp.getSelectedMonth(), employee.getObjectID());

        result.setTimeslotItem(timeslotItem);
        timeslotItem.setWeekDaysPlannedTime(plannedTimes);
        timeslotItem.setActualWeekDaysPlannedTime(actualPlannedTimes);
        timeslotItem.setMonthlyPlannedTime(monthlyPlannedTime);
        result.setTimeslotItem(timeslotItem);

        System.out.println("-------> TimeshetServiceImpl.getDate took: " + ((new Date()).getTime() - timer.getTime()));
        return result;
    }

    /**
     * Given the non-converted clients current date on his side, and the weekOffset it will return 7 days of thar particular week
     *
     * @param clientsCurrentDate non-converted date of the clients current date
     * @param weekOffset         weekOffset if he changed his timesheet view to a different week
     * @return dates of the selected week of which length=7
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DateNonConvertable[] getTimesheetWeeklyDates(DateNonConvertable clientsCurrentDate, int weekOffset) {
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        Integer weekStart = numberingSettings == null ? 2 : numberingSettings.getTimesheetWeekStart();

        Calendar c = new GregorianCalendar();
        c.setTime(clientsCurrentDate.getNonConvertedDate());
        c.add(Calendar.DATE, weekOffset * 7);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int differentStartWeekFromToday = 0;
        if (dayOfWeek == 1) {
            if (weekStart.equals(Calendar.MONDAY)) {
                differentStartWeekFromToday = 6;
            } else if (weekStart.equals(Calendar.SATURDAY)) {
                differentStartWeekFromToday = 1;
            }
        } else if (dayOfWeek == 7) {
            if (weekStart.equals(Calendar.MONDAY)) {
                differentStartWeekFromToday = 5;
            } else if (weekStart.equals(Calendar.SUNDAY)) {
                differentStartWeekFromToday = 6;
            }
        } else {
            differentStartWeekFromToday = dayOfWeek;
            if (weekStart.equals(Calendar.SUNDAY)) {
                differentStartWeekFromToday = dayOfWeek - 1;
            } else if (weekStart.equals(Calendar.MONDAY)) {
                differentStartWeekFromToday = dayOfWeek - 2;
            }
        }

        DateNonConvertable[] dates = new DateNonConvertable[7];
        for (int j = 0; j < 7; j++) {
            Calendar cal = new GregorianCalendar();
            cal.setTime(c.getTime());
            cal.add(Calendar.DATE, j - differentStartWeekFromToday);
            cal.set(Calendar.AM_PM, 0);
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            dates[j] = new DateNonConvertable(cal.getTime());
        }
        return dates;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, List<TimesheetItem>> getTimesheetData(Date startDate, Date endDate, EdsUser employee) {
        return getTimeSheetDataN(startDate, endDate, employee);
    }

    private Map<String, List<TimesheetItem>> getTimeSheetDataN(Date startDate, Date endDate, EdsUser employeeUser) {
        Date testST = new Date();
        if (employeeUser != null && !employeeUser.getDeleted() && employeeUser.getCompany().getActive()) {
            //------------------------------------------------------//
            //start date time calendar (ex: 2012-12-12 00:00:00)
            Calendar startDateTime = Calendar.getInstance();
            startDateTime.setTime(startDate);
            ServerUtils.setBeginningOfTheDay(startDateTime);
            //------------------------------------------------------//
            //end date time calendar (ex: 2012-12-12 23:59:59)
            Calendar endDateTime = Calendar.getInstance();
            endDateTime.setTime(endDate);
            ServerUtils.setEndOfTheDay(endDateTime);
            //------------------------------------------------------//
            //employees list
            List employees = employeeManager.getEmployeesForDailyTimeSheetData(employeeUser, startDateTime.getTime(), endDateTime.getTime());
            //------------------------------------------------------//
            if (employees != null) {
                //days count
                Calendar STDate = new GregorianCalendar();
                Calendar EDDate = new GregorianCalendar();
                STDate.setTime(startDateTime.getTime());
                EDDate.setTime(endDateTime.getTime());
                ArrayList<Calendar> availableDays = new ArrayList<>();
                while (EDDate.getTime().compareTo(STDate.getTime()) >= 0) {
                    Calendar nonDate = Calendar.getInstance();
                    nonDate.setTime(STDate.getTime());
                    ServerUtils.setBeginningOfTheDay(nonDate);
                    availableDays.add(nonDate);
                    STDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                int daysCount = availableDays.size();
                //------------------------------------------------------//
                TreeMap<String, List<TimesheetItem>> timeSheetItemMap = new TreeMap<>();
                Map<Integer, TimesheetItem> empDatA = new LinkedHashMap<>();
                //------------------------------------------------------//
                TimesheetItem empDatAS = null;
                ArrayList<SelectItem> datAndTime = null;
                Integer emp_ID = null;
                HashMap<Integer, Integer> totalTime = new HashMap<>();
                //------------------------------------------------------//
                int i = 1;
                for (Object object : employees) {
                    Object[] data = (Object[]) object;

                    Integer empID = (Integer) (data[0] != null ? data[0] : 0);
                    String empName = (String) (data[1] != null ? data[1] : "");
                    Date sDate = (Date) (data[2]);
                    String departmentName = (String) (data[3] != null ? data[3] : "");
                    Integer timeSlotTime = (Integer) (data[4] != null ? data[4] : 0);
                    BigInteger timeSheetTime = (BigInteger) (data[5]);//tod
                    Boolean isHoliday = (Boolean) (data[6] != null ? data[6] : Boolean.FALSE);
                    Boolean dayOff = (Boolean) (data[7] != null ? data[7] : Boolean.FALSE);
                    Boolean isLeaveR = (Boolean) (data[8] != null ? data[8] : Boolean.FALSE);
                    if (totalTime.get(empID) == null) {
                        totalTime.put(empID, timeSheetTime != null && timeSheetTime.intValue() > 0
                                ? timeSheetTime.intValue()
                                : 0);
                        i = 1;
                    } else {
                        if (totalTime.containsKey(empID)) {
                            totalTime.put(empID, totalTime.get(empID) + (timeSheetTime != null && timeSheetTime.intValue() > 0
                                    ? timeSheetTime.intValue()
                                    : 0));
                            i++;
                        }
                    }


                    if (empID != null && empID != 0) {
                        //set employee ID
                        if (emp_ID == null || !emp_ID.equals(empID)) {
                            emp_ID = empID;
                            if (!empDatA.containsKey(empID)) {
                                empDatAS = new TimesheetItem();
                                empDatA.put(empID, empDatAS);
                                //------------------------------------------------------//
                                datAndTime = new ArrayList<>(daysCount);
                                empDatAS.setDayAndTimeL(datAndTime);
                            } else {
                                empDatAS = empDatA.get(empID);
                                datAndTime = empDatAS.getDayAndTimeL();
                            }
                        }

                        if (empDatA.containsKey(empID)) {
                            //set employee ID & name to timeSheetItem
                            empDatAS.setEmployeeId(empID);
                            empDatAS.setEmployeeName(empName);
                            if (daysCount == i && daysCount > 1) {
                                Integer time = totalTime.get(empID);
                                Integer minuteInt = (time % 60);
                                String minuteST = minuteInt % 60 < 10
                                        ? "0" + minuteInt
                                        : minuteInt.toString();
                                empDatAS.setTotalTime(time / 60 + ":" + minuteST);
                            }
                            //------------------------------------------------------//
                            //set date & time item to timeSheetItem list
                            SelectItem selectItem = new SelectItem();
                            if (sDate != null) {
                                selectItem.setName(sDate.toString());
                            }
                            if (timeSheetTime != null && timeSheetTime.intValue() > 0) {
                                Integer minuteInt = (Integer.parseInt(timeSheetTime.toString()) % 60);
                                String minuteST = minuteInt % 60 < 10
                                        ? "0" + minuteInt
                                        : minuteInt.toString();
                                selectItem.setDescription((Integer.parseInt(timeSheetTime.toString()) / 60) + ":" + minuteST);
                            } else if (isLeaveR) {
                                selectItem.setDescription("<span style='color: #365f91'>LR</span>");
                            } else if (dayOff || isHoliday) {
                                selectItem.setDescription("<span style='color: #365f91'>Day-Off</span>");
                            } else {
                                selectItem.setDescription("<span style='color: #ff0000'>X</span>");
                            }
                            datAndTime.add(selectItem);
                            //------------------------------------------------------//
                            //set department name
                            if (departmentName != null && !"".equals(departmentName)) {
                                timeSheetItemMap.computeIfAbsent(departmentName, k -> new ArrayList<>());
                                if (!timeSheetItemMap.get(departmentName).contains(empDatAS)) {
                                    timeSheetItemMap.get(departmentName).add(empDatAS);
                                }
                            }
                            //------------------------------------------------------//
                        }
                    }
                }
                //------------------------------------------------------//
                //comparator factory
                ComparatorFactory<TimesheetItem> factory = sortOrder -> new AbstractComparator<TimesheetItem>() {
                    @Override
                    public int compare(TimesheetItem o1, TimesheetItem o2) {
                        return internalCompare(o1.getEmployeeName(), o2.getEmployeeName(), sortOrder);
                    }
                };
                //------------------------------------------------------//
                //sorting by department name
                for (Map.Entry<String, List<TimesheetItem>> entry : timeSheetItemMap.entrySet()) {
                    entry.getValue().sort(factory.createComparator(Constants.ASC));
                }
                //------------------------------------------------------//
                Date testED = new Date();
                System.out.println("TEST DATE AVERAGE : " + employeeUser.getObjectID() + " - " + (testED.getTime() - testST.getTime()));
                return timeSheetItemMap;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    /**
     * Saving Timesheet hours
     * Timesheet plugun
     *
     * @param item - Timesheet Transfer Object
     * @return Timesheet Id
     */
    public Integer applyUpdates(TimesheetDataItem item, Integer synchItemId) {

        EdsEmployee employee = (EdsEmployee) referenceManager.getUser();
        boolean createDateJoin = true;
        if (employee == null) {
            employee = employeeManager.get(item.getEmployeeID());
            createDateJoin = false;
        }
        if (item.getOldEmployeeTaskIDList() != null && item.getOldEmployeeTaskIDList().size() > 0) {
            for (Integer oldEmployeeTaskID : item.getOldEmployeeTaskIDList()) {
                EdsTimeSheet oldTimeSheet = timesheetManager.getTimeSheet(employeeTaskManager.get(oldEmployeeTaskID), item.getDate());
                oldTimeSheet.setTimeSpent(0);
            }
        }
        EdsEmployeeTask employeeTask = employeeTaskManager.get(item.getEmployeeTaskID());
        EdsTask task = employeeTask.getTask();

        EdsTimeSheet testTimeSheet = null;

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_BONNARD_CUSTOMIZATION) && item.getId() != null) {
            testTimeSheet = timesheetManager.get(item.getId());
            testTimeSheet.setDate(item.getDate());
            task.setStartDate(item.getDate());
        } else {
            testTimeSheet = timesheetManager.getTimeSheet(employeeTask, item.getDate());
        }


        TimeZone timeZone = employee.getUserTimezone();
        EdsTimeSheet timeSheet = null;
        boolean wasEnteredNowAutoApproving = false;
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
        if (testTimeSheet == null) {

            if (item.getMinutes() > 0) {
                timeSheet = new EdsTimeSheet();
                timeSheet.setDate(item.getDate());
                timesheetManager.create(timeSheet);
                item.setId(timeSheet.getObjectID());

                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsTimeSheet.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                if (timeSheet.getObjectID() != null) {
                    kpiLog.setEntityId(timeSheet.getObjectID());
                }
                ServerUtils.kpiLog(log, kpiLog, "New timesheet created");

            } else {
                return null;
            }
        } else {
            timeSheet = testTimeSheet;
            if (!timeSheet.getAutoApproved() && timeSheet.getTimeSpent() > 0) {
                wasEnteredNowAutoApproving = true;
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsTimeSheet.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(timeSheet.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Updating timesheet");
            if (item.getMinutes() == 0) {
                boolean wasApprovedNowRejecting = false;
                timeSheet.setTimeSpent(0);
                timeSheet.setEntryDate(null);
                if (timeSheet.getDailyEstimatedTime() == null || timeSheet.getDailyEstimatedTime() == 0) {
                    timesheetManager.delete(timeSheet);
                } else {
                    timesheetManager.update(timeSheet);
                }

                updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getMinutes());
                if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
                    Integer timeSpent = 0;
                    if (!timeSheet.getAutoApproved()) {
                        timeSpent = timeSheet.getTimeSpent();
                        timeSheet.setStatus(null);
                    } else {
                        timeSpent = item.getDifference();
                        wasApprovedNowRejecting = true;
                    }
                    EdsTimeSheet tempTimesheet = new EdsTimeSheet();
                    tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
                    tempTimesheet.setStatus(timeSheet.getStatus());
                    tempTimesheet.setTimeSpent(timeSpent);

                    updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());

                    if (timeSheet.getStatus() != null && referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE").getObjectID().equals(timeSheet.getStatus().getObjectID())) {
                        sendNotificationActualTimeReached(tempTimesheet);
                    }
                }
                dashboardService.lastEnteredDate();
                saveAttendanceRawDataTimesheet(timeSheet, item.getDifference(), wasApprovedNowRejecting, wasEnteredNowAutoApproving);
                return null;
            }
        }

        updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getMinutes());
        timeSheet.setEmployeeTask(employeeTask);
        timeSheet.setTaskID(task.getObjectID());
        timeSheet.setProjectID(task.getProject().getObjectID());
        EdsProjectEmployee edsProjectEmployee = employeeTaskManager.get(item.getEmployeeTaskID()).getProjectEmployee();
        timeSheet.setEmployeeID(edsProjectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
        timeSheet.setTeamID(edsProjectEmployee.getEmployeeDepartment().getTeam().getObjectID());
        timeSheet.setComment(item.getComment());
        timeSheet.setReference(item.getReference());
        if (timeSheet.getTimeSpent() == null || !timeSheet.getTimeSpent().equals(item.getMinutes())) {
            timeSheet.setEntryDate(new Date());
        }
        timeSheet.setTimeSpent(item.getMinutes());
        if (item.getHourTypeID() != 0) {
            timeSheet.setType(referenceManager.get(item.getHourTypeID()));
        } else {
            timeSheet.setType(null);
        }
        //employee wage rate
        if (timeSheet.getClientChargeRate() == null || timeSheet.getClientChargeRate().intValue() == 0) {
            EdsProjectEmployeeWageClientRateHistory clientWageRate = timesheetManager.getProjectEmployeeWageClientRateByDate(timeSheet.getDate(), edsProjectEmployee.getObjectID());
            if (clientWageRate != null) {
                timeSheet.setClientChargeRate(clientWageRate.getClientChargeRate());
                timeSheet.setWageRate(clientWageRate.getWageRate());
            }
        }

        EdsReference reject = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT");
        if (timeSheet.getStatus() != null && timeSheet.getStatus().equals(reject)) {
            timeSheet.setStatus(null);
        }

        if (item.getComment() != null && !(item.getComment().trim().equals(""))) {
            EdsTaskComment comment = new EdsTaskComment();
            comment.setTask(task);
            comment.setCreationDate(item.getDate());
            comment.setUser(employee);
            comment.setText(item.getComment() + " (" + ServerUtils.timeSpentToString(item.getMinutes()) + " entered) ");
            taskCommentManager.create(comment);
        }

        //create new one year if today's date is not in datejoin table
        if (createDateJoin) {
            dashboardService.lastEnteredDate();
        }
        if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
            Integer timeSpent = 0;
            if (!timeSheet.getAutoApproved()) {
                timeSpent = timeSheet.getTimeSpent();
            } else {
                timeSpent = item.getDifference();
            }
            timeSheet.setStatus(referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE"));
            timeSheet.setApprovalDate(new Date());
            timeSheet.setRejectedDate(null);
            timeSheet.setManagerComment("Automatically approved by the system");
            timeSheet.setAutoApproved(true);
            timesheetManager.update(timeSheet);

            EdsTimeSheet tempTimesheet = new EdsTimeSheet();
            tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
            tempTimesheet.setStatus(timeSheet.getStatus());
            tempTimesheet.setTimeSpent(timeSpent);
            updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());
        } else if (settings != null && settings.isWaitingForApproval()) {
            EdsReference sessionWaitingStatus = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
            timeSheet.setStatus(sessionWaitingStatus);
            timeSheet.setManagerComment("Automatically submitted for approval by the system");
            timeSheet.setSubmittedDate(new Date());
            timesheetManager.update(timeSheet);

            EdsTimeSheet tempTimesheet = new EdsTimeSheet();
            tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
            tempTimesheet.setStatus(timeSheet.getStatus());
            tempTimesheet.setTimeSpent(item.getDifference());
            updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());

            EdsTimeSheetApprovalSession timesheetSession = new EdsTimeSheetApprovalSession();

            Calendar fromDate = new GregorianCalendar();
            fromDate.setTime(timeSheet.getDate());
            ServerUtils.setBeginningOfTheDay(fromDate);
            timesheetSession.setStartDate(fromDate.getTime());

            Calendar toDate = new GregorianCalendar();
            toDate.setTime(timeSheet.getDate());
            ServerUtils.setEndOfTheDay(toDate);
            timesheetSession.setEndDate(toDate.getTime());

            timesheetSession.setSubmittedDate(new Date());
            timesheetSession.setStatus(sessionWaitingStatus);
            timesheetSession.getTimeentries().add(timeSheet);
            timesheetSession.setEmployee(employeeManager.get(timeSheet.getEmployeeID()));
            EdsTask edsTask = taskManager.get(timeSheet.getTaskID());
            if (edsTask != null) {
                timesheetSession.setProject(edsTask.getProject());
            }

            if (genericSettingsManager.isSettingsEnabled(ENABLE_MULTI_PROJECT_TO_TIMESHEET)) {
                timesheetSession.setApprover(employee.getProfile().getReportsTo());
            } else if (edsTask != null && edsTask.getProject() != null) {
                timesheetSession.setApprover(edsTask.getProject().getManager());
            }
            timeSheetApprovalSessionManager.create(timesheetSession);
        }
        saveAttendanceRawDataTimesheet(timeSheet, item.getDifference(), false, wasEnteredNowAutoApproving);
        sendNotificationActualTimeReached(timeSheet);
        return item.getId();
    }

    public Integer applyUpdates(TimesheetDataItem item) {
        EdsEmployee employee = (EdsEmployee) referenceManager.getUser();
        boolean createDateJoin = true;
        if (employee == null) {
            employee = employeeManager.get(item.getEmployeeID());
            createDateJoin = false;
        }

        TimeZone timeZone = employee.getUserTimezone();
        boolean wasEnteredNowAutoApproving = false;
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();

        DateNonConvertable dateNonConvertable = item.getDateNonConvertable();
        Date date = dateNonConvertable.getDate();
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        item.setDate(date);

        EdsTimeSheet timeSheet = null;
        EdsTimeSheet testTimeSheet = timesheetManager.getTimeSheet(item.getEmployeeID(), item.getTaskID(), item.getDate());

        EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeRelatedTask(item.getTaskID(), item.getEmployeeID());
        EdsTask task = employeeTask.getTask();

        if (testTimeSheet == null) {
            if (item.getMinutes() > 0) {
                timeSheet = new EdsTimeSheet();
                timeSheet.setDate(item.getDate());
                timesheetManager.create(timeSheet);
                item.setId(timeSheet.getObjectID());

                KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
                kpiLog.setEntityName(EdsTimeSheet.class.getSimpleName());
                kpiLog.setActionType(KpiLog.ActionType.ADD);
                if (timeSheet.getObjectID() != null) {
                    kpiLog.setEntityId(timeSheet.getObjectID());
                }
                ServerUtils.kpiLog(log, kpiLog, "New timesheet created");

            } else {
                return null;
            }
        } else {

            timeSheet = testTimeSheet;
            if (!timeSheet.getAutoApproved() && timeSheet.getTimeSpent() > 0) {
                wasEnteredNowAutoApproving = true;
            }
            KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
            kpiLog.setEntityName(EdsTimeSheet.class.getSimpleName());
            kpiLog.setActionType(KpiLog.ActionType.UPDATE);
            kpiLog.setEntityId(timeSheet.getObjectID());
            ServerUtils.kpiLog(log, kpiLog, "Updating timesheet");
            if (item.getMinutes() == 0) {
                boolean wasApprovedNowRejecting = false;
                timeSheet.setTimeSpent(0);
                timeSheet.setEntryDate(null);
                timeSheet.setComment(item.getComment());
                if (timeSheet.getDailyEstimatedTime() == null || timeSheet.getDailyEstimatedTime() == 0) {
                    timesheetManager.delete(timeSheet);
                } else {
                    timesheetManager.update(timeSheet);
                }

                updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getMinutes());
                if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
                    Integer timeSpent = 0;
                    if (!timeSheet.getAutoApproved()) {
                        timeSpent = timeSheet.getTimeSpent();
                        timeSheet.setStatus(null);
                    } else {
                        timeSpent = item.getDifference();
                        wasApprovedNowRejecting = true;
                    }
                    EdsTimeSheet tempTimesheet = new EdsTimeSheet();
                    tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
                    tempTimesheet.setStatus(timeSheet.getStatus());
                    tempTimesheet.setTimeSpent(timeSpent);

                    updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());

                    if (referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE").getObjectID().equals(timeSheet.getStatus().getObjectID())) {
                        sendNotificationActualTimeReached(tempTimesheet);
                    }
                }
                dashboardService.lastEnteredDate();
                saveAttendanceRawDataTimesheet(timeSheet, item.getDifference(), wasApprovedNowRejecting, wasEnteredNowAutoApproving);
                return null;
            }
        }
        updateActualStartEndDatesOfTask(task, item.getDate(), timeZone.getRawOffset(), item.getMinutes());
        timeSheet.setEmployeeTask(employeeTask);
        timeSheet.setTaskID(task.getObjectID());
        timeSheet.setProjectID(task.getProject().getObjectID());
        EdsProjectEmployee edsProjectEmployee = employeeTaskManager.get(employeeTask.getObjectID()).getProjectEmployee();
        timeSheet.setEmployeeID(edsProjectEmployee.getEmployeeDepartment().getEmployee().getObjectID());
        timeSheet.setTeamID(edsProjectEmployee.getEmployeeDepartment().getTeam().getObjectID());
        timeSheet.setComment(item.getComment());
        timeSheet.setReference(item.getReference());
        if (timeSheet.getTimeSpent() == null || !timeSheet.getTimeSpent().equals(item.getMinutes())) {
            timeSheet.setEntryDate(new Date());
        }
        timeSheet.setTimeSpent(item.getMinutes());
        if (item.getHourTypeID() != 0) {
            timeSheet.setType(referenceManager.get(item.getHourTypeID()));
        } else {
            timeSheet.setType(null);
        }
        if (item.getComment() != null && !(item.getComment().trim().equals(""))) {
            EdsTaskComment comment = new EdsTaskComment();
            comment.setTask(task);
            comment.setCreationDate(item.getDate());
            comment.setUser(employee);
            comment.setText(item.getComment() + " (" + ServerUtils.timeSpentToString(item.getMinutes()) + " entered) ");
            taskCommentManager.create(comment);
        }

        if (createDateJoin) {
            dashboardService.lastEnteredDate();
        }
        if (settings != null && settings.isAutomaticApproval()) {//if settings is null then timesheet hours approval method is manual
            Integer timeSpent = 0;
            if (!timeSheet.getAutoApproved()) {
                timeSpent = timeSheet.getTimeSpent();
            } else {
                timeSpent = item.getDifference();
            }
            timeSheet.setStatus(referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE"));
            timeSheet.setApprovalDate(new Date());
            timeSheet.setRejectedDate(null);
            timeSheet.setManagerComment("Automatically approved by the system");
            timeSheet.setAutoApproved(true);
            timesheetManager.update(timeSheet);

            EdsTimeSheet tempTimesheet = new EdsTimeSheet();
            tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
            tempTimesheet.setStatus(timeSheet.getStatus());
            tempTimesheet.setTimeSpent(timeSpent);
            updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());
        } else if (settings != null && settings.isWaitingForApproval()) {
            EdsReference sessionWaitingStatus = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
            timeSheet.setStatus(sessionWaitingStatus);
            timeSheet.setManagerComment("Automatically submitted for approval by the system");
            timeSheet.setSubmittedDate(new Date());
            timesheetManager.update(timeSheet);

            EdsTimeSheet tempTimesheet = new EdsTimeSheet();
            tempTimesheet.setEmployeeTask(timeSheet.getEmployeeTask());
            tempTimesheet.setStatus(timeSheet.getStatus());
            tempTimesheet.setTimeSpent(item.getDifference());
            updateTimeSpentAndBudget(tempTimesheet, false, false, true, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID(), settings.isAutomatic());

            EdsTimeSheetApprovalSession timesheetSession = new EdsTimeSheetApprovalSession();

            Calendar fromDate = new GregorianCalendar();
            fromDate.setTime(timeSheet.getDate());
            ServerUtils.setBeginningOfTheDay(fromDate);
            timesheetSession.setStartDate(fromDate.getTime());

            Calendar toDate = new GregorianCalendar();
            toDate.setTime(timeSheet.getDate());
            ServerUtils.setEndOfTheDay(toDate);
            timesheetSession.setEndDate(toDate.getTime());

            timesheetSession.setSubmittedDate(new Date());
            timesheetSession.setStatus(sessionWaitingStatus);
            timesheetSession.getTimeentries().add(timeSheet);
            timesheetSession.setEmployee(employeeManager.get(timeSheet.getEmployeeID()));
            EdsTask edsTask = taskManager.get(timeSheet.getTaskID());
            if (edsTask != null) {
                timesheetSession.setProject(edsTask.getProject());
            }
            if (genericSettingsManager.isSettingsEnabled(ENABLE_MULTI_PROJECT_TO_TIMESHEET)) {
                timesheetSession.setApprover(employee.getProfile().getReportsTo());
            } else if (edsTask != null && edsTask.getProject() != null) {
                timesheetSession.setApprover(edsTask.getProject().getManager());
            }
            timeSheetApprovalSessionManager.create(timesheetSession);
        }
        saveAttendanceRawDataTimesheet(timeSheet, item.getDifference(), false, wasEnteredNowAutoApproving);
        return item.getId();
    }

    private void updateActualStartEndDatesOfTask(EdsTask task, Date tshDate, int userTimeZoneOffset, int timeSpent) {
        Date timesheetDate = (Date) tshDate.clone();
        timesheetDate.setMinutes(timesheetDate.getMinutes() - userTimeZoneOffset / 60000);

        Date firstDate = timesheetManager.getFirstTimesheetDateForTask(task);
        if (firstDate != null) {
            firstDate.setMinutes(firstDate.getMinutes() - userTimeZoneOffset / 60000);
            if (timesheetDate.before(firstDate) && timeSpent > 0) {
                task.setActualStartDate(timesheetDate);
            } else {
                task.setActualStartDate(firstDate);
            }
        } else {
            if (timeSpent > 0) {
                task.setActualStartDate(timesheetDate);
            } else {
                task.setActualStartDate(new Date());
            }
        }

        Date lastDate = timesheetManager.getLastTimesheetDateForTask(task);
        if (lastDate != null) {
            lastDate.setMinutes(lastDate.getMinutes() - userTimeZoneOffset / 60000);
            if (timesheetDate.after(lastDate) && timeSpent > 0) {
                task.setActualEndDate(timesheetDate);
            } else {
                task.setActualEndDate(lastDate);
            }
        } else {
            if (timeSpent > 0) {
                task.setActualEndDate(timesheetDate);
            } else {
                task.setActualEndDate(null);
            }
        }
        taskManager.update(task);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTimeSpentAndBudget(EdsTimeSheet timeSheet, boolean wasApprovedNowRejecting, boolean wasApprovedNowApproving, boolean updateSolr, Integer rejectID, boolean isAutomatic) {

        EdsEmployeeTask employeeTask = timeSheet.getEmployeeTask();
        EdsTaskEstimateTimeSpentHistory estimateTimeSpentHistory = new EdsTaskEstimateTimeSpentHistory();

        int timeSpent = employeeTask.getTimeSpent() != null ? employeeTask.getTimeSpent() : 0;
        estimateTimeSpentHistory.setOldTimespent(timeSpent);
        // only unrejected time entries should be timespent hours
        //
        int dif = 0;
        if (timeSheet.getStatus() == null || (!timeSheet.getStatus().getObjectID().equals(rejectID) || (wasApprovedNowRejecting && timeSheet.getStatus().getObjectID().equals(rejectID)))) {
            dif = wasApprovedNowApproving ? 0 : (timeSheet.getTimeSpent() != null ? (wasApprovedNowRejecting ? -timeSheet.getTimeSpent() : timeSheet.getTimeSpent()) : 0);
        }
        employeeTask.setTimeSpent(timeSpent + dif);
        estimateTimeSpentHistory.setTimespent(employeeTask.getTimeSpent());


        EdsTask task = employeeTask.getTask();
        int taskTimeSpent = task.getTimespent() != null ? task.getTimespent() : 0;
        task.setTimespent(taskTimeSpent + dif);
        task.setLastUpdateTime(new Date());

        //this is for the recalculate task budgets
        task.setChangedCalculationFields(true);
        estimateTimeSpentHistory.setTask(task);
        task.getEstimateTimeSpentHistoryList().add(estimateTimeSpentHistory);

        EdsProject project = task.getProject();
        int projectTimeSpent = project.getTimespent() != null ? project.getTimespent() : 0;
        project.setTimespent(projectTimeSpent + dif);
        projectManager.update(project);
        //if settings is null then project percent calculation mode is manual by default
        if (isAutomatic) {
            baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, employeeTask, timesheetManager.getUser());
        } else if (updateSolr) {
            try {
                projectSolrComponent.index(project);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
            taskRbacManager.addRbacEntries(task);
            try {
                taskSolrComponent.index(task);
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
    }

    private void updateProjectStatus(EdsTask task) {
        EdsProject project = task.getProject();
        if (project != null && project.getStatus().equals(referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.NOT_STARTED))) {
            project.setStatus(referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING));
            project.setCompletedDate(null);
            projectManager.update(project);
        }
    }

    public void updateStatus(TaskStatus status) {
        EdsUser user = employeeTaskManager.getUser();
        log.info("Task change status name:" + status.getStatusName());
        log.info("USER:" + user.getObjectID() + " ##$## COMPANYID:" + user.getCompany().getObjectID());
        EdsEmployeeTask employeeTask = employeeTaskManager.get(status.getEmployeeTaskId());
        EdsReference ref = referenceManager.get(status.getStatus());
        EdsTask task = employeeTask.getTask();
        task.clear();
        if (!status.isForceChangeStatus() && task != null && task.getStatus() != null && !task.getStatus().getCode().equals(EdsTask.NOT_STARTED)) {//task status should be changed to In progress  only from Not started. But it should work when user select status from status drop down. So is isForceChangeStatus param has been added to skip to logic.
            return;
        }

        employeeTask.setStatus(ref);

        baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, employeeTask, user);
        String TASK_STATUS = employeeTask.getTask().getTaskLastStatus();
        EdsReference stat = referenceManager.findReference(EdsTask.TASK_STATUS, TASK_STATUS);
        //            EdsTask task = employeeTask.getTask();
        task.setStatus(stat);
        taskManager.update(task);
        /* if current user is one of the task assignees and the task assignee status is changed from timesheet view, update the task rbac assignee status as well*/
        taskRbacManager.updateTaskAssigneeRbacStatus(task.getObjectID(), user.getObjectID(), status.getStatus());

        Boolean isCompleted = stat.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED));
        if (stat.equals(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS)) || isCompleted) {
            updateProjectStatus(task);
        }
        if (isCompleted) {
            baseEventPostProcessor.registerEvent(StatusTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, task, user);
        }
        if (task.getIssue() == null || !task.getIssue()) {
            solrAndRbacEntries(user, task);
        }


        EdsReference completedStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.COMPLETED);

        if (completedStatus != null && status.getStatus() == completedStatus.getObjectID()) {
            employeeTask.setActualEndDate(timesheetManager.getTimeSheetMaxDateByTaskID(employeeTask));
            //if status == COMPLETED, employeeTask percent completed should been 100%;
            if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                updatePercentCompleted(employeeTask.getObjectID(), 100f, false);
            }
            employeeTask.setCompletedDate(new Date());
        } else {
            EdsReference closedStatus = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
            if (closedStatus != null && status.getStatus() == closedStatus.getObjectID()) {
                employeeTask.setClosedDate(new Date());
            } else {
                employeeTask.setCompletedDate(null);
            }
        }
    }

    private void solrAndRbacEntries(EdsUser user, EdsTask task) {
        baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, task, user);
    }

    private int calculatePlannedTime(EdsTimeSlotItem item) {
        int coffeeTime = 0, lunchTime = 0, totalTime;
        if (item.getCoffeeStart() != null && item.getCoffeeEnd() != null) {
            coffeeTime = item.getCoffeeEnd() - item.getCoffeeStart();
        }
        if (item.getLunchStart() != null && item.getLunchEnd() != null) {
            lunchTime = item.getLunchEnd() - item.getLunchStart();
        }
        totalTime = item.getEndTime() - item.getStartTime();
        return totalTime - (coffeeTime + lunchTime);
    }

    private TimesheetReport[] getMonthlyStatistics(Date day, EdsEmployee employee) {
        return getStatistics(day, employee, Constants.CALENDAR_MONTH);
    }

    private TimesheetReport[] getWeeklyStatistics(Date day, EdsEmployee employee) {
        return getStatistics(day, employee, Constants.CALENDAR_WEEK);
    }

    private int[] getDailyStatistics(DateNonConvertable[] day, EdsEmployee employee) {
        int[] sum = new int[day.length];
        Map<String, Integer> map = getStatistics(day[0].getNonConvertedDate(), day[day.length - 1].getNonConvertedDate(), employee);
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        for (int i = 0; i < day.length; i++) {
            sum[i] = 0;
            String date = dateFormat.format(day[i].getNonConvertedDate());
            if (map.containsKey(date)) {
                sum[i] = map.get(date);
            }
        }
        return sum;
    }

    public ArrayList<TimesheetWeeklyEntryTO> getDailyStatistics(DateNonConvertable selectedDate) {
        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        DateNonConvertable clientsCurrentDate = new DateNonConvertable();
        int weekOffset = getWeekOffset(clientsCurrentDate, selectedDate);
        DateNonConvertable[] days = getTimesheetWeeklyDates(clientsCurrentDate, weekOffset);
        ArrayList<TimesheetWeeklyEntryTO> result = new ArrayList<>();
        Map<String, Integer> map = getStatistics(days[0].getNonConvertedDate(), days[days.length - 1].getNonConvertedDate(), employee);
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        for (DateNonConvertable day : days) {
            TimesheetWeeklyEntryTO weeklyEntryTO = new TimesheetWeeklyEntryTO();
            weeklyEntryTO.setDate(day.getNonConvertedDate().getTime());
            weeklyEntryTO.setMinutes(0);
            String date = dateFormat.format(day.getNonConvertedDate());
            if (map.containsKey(date)) {
                weeklyEntryTO.setMinutes(map.get(date));
            }
            result.add(weeklyEntryTO);
        }
        return result;
    }

    private Map<String, Integer> getStatistics(Date startDate, Date endDate, EdsEmployee employee) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        startDate = calendar.getTime();

        calendar = new GregorianCalendar();

        calendar.setTime(endDate);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        endDate = calendar.getTime();
        return getResultForTimesheet(employee, startDate, endDate);
    }

    private TimesheetReport[] getStatistics(Date day, EdsEmployee employee, String statisticsType) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(day);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (Constants.CALENDAR_MONTH.equals(statisticsType)) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        Date startDate = calendar.getTime();

        calendar = new GregorianCalendar();

        calendar.setTime(day);
        calendar.set(Calendar.AM_PM, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (Constants.CALENDAR_MONTH.equals(statisticsType)) {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        } else if (Constants.CALENDAR_WEEK.equals(statisticsType)) {
            calendar.add(Calendar.DAY_OF_MONTH, 6);
        }
        Date endDate = calendar.getTime();
        return getResult(employee, startDate, endDate);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Map<String, Integer> getResultForTimesheet(EdsEmployee employee, Date from, Date to) {
        Integer viewAsId = null;
        if (timesheetManager.getUser().hasRole(EdsRole.TIMESHEET_EDITOR_CODE)) {
            viewAsId = EdsRole.ADMIN;
        }
        List<Object[]> values = projectManager.getResult(null, null, null, employee, viewAsId, "", from, to, false,
                false, false, false, false, true, true, false, false, false, false, false, false, false, false);
        Map<String, Integer> map = new HashMap<>();
        String pattern = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        for (Object[] value : values) {
            int i = 0, g = 0;
            g = i++;
            int total = Integer.valueOf(value[g] != null ? value[g].toString() : "0");
            g = i++;
            Date d = null;
            try {
                d = (Date) value[g];
            } catch (Exception e) {
                e.printStackTrace();
            }
            String date = dateFormat.format(d);
            if (map.containsKey(date)) {
                map.put(date, map.get(date) + total);
            } else {
                map.put(date, total);
            }
        }
        return map;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetReport[] getResult(EdsEmployee employee, Date from, Date to) {

        List<Object[]> values = projectManager.getResult(null, null, null, employee, null, "Project", from, to, false,
                true, false, false, false, false, false, false, false, false, false, false, false, false, false, true);

        TimesheetReport[] result = new TimesheetReport[values.size()];
        int k = 0;
        for (Object[] value : values) {
            int i = 0, g = 0;
            result[k] = new TimesheetReport();

            g = i++;
            result[k].setSum(Integer.parseInt(value[g] != null ? value[g].toString() : "0"));

            g = i++;
            result[k].setProjectName(value[g] != null ? value[g].toString() : "");

            k++;
        }
        return result;
    }

    /**
     * Given the date it will return week offset between current date
     *
     * @param choosenDate by which current date should be compared with
     * @return week offset
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer getWeekOffset(DateNonConvertable clientsCurrenDate, DateNonConvertable choosenDate) {
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        Integer weekStart = numberingSettings == null ? 2 : numberingSettings.getTimesheetWeekStart();

        Calendar current = new GregorianCalendar();
        Calendar selected = new GregorianCalendar();
        current.setTime(clientsCurrenDate.getNonConvertedDate());
        selected.setTime(choosenDate.getNonConvertedDate());
        current.setFirstDayOfWeek(weekStart);
        selected.setFirstDayOfWeek(weekStart);
        ServerUtils.setBeginningOfTheDay(current);
        ServerUtils.setBeginningOfTheDay(selected);
        return ServerUtils.getDateDiff(current, selected, Calendar.WEEK_OF_YEAR);
    }

    /**
     * Method used to get task statuses for
     * Add Task and Task Edit views
     *
     * @return SelectItem[]
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEditTaskStatusDrop(Integer taskId) {
        List<EdsReference> statuses = referenceManager.listReferences(EdsTask.TASK_STATUS);
        EdsReference onHold = referenceManager.findReference(
                EdsTask.TASK_STATUS, EdsTask.ON_HOLD);
        statuses.remove(onHold);
        if (taskId != null) {
            EdsTask task = taskManager.get(taskId);
            EdsUser user = taskManager.getUser();
            String hideCloseStatusByProjectIdString = genericSettingsManager.getValueByKey(GenericSettingsEnum.HIDE_CLOSE_STATUS_BY_PROJECT_ID);
            Integer hideCloseStatusByProjectId = !ServerUtils.isNullOrEmpty(hideCloseStatusByProjectIdString) ? Integer.valueOf(hideCloseStatusByProjectIdString) : null;

            if ((!user.hasRole(roleManager.get(EdsRole.ADMIN)) && !user.hasRole(roleManager.get(EdsRole.DR))) &&
                    (!task.getProject().getManager().getObjectID().equals(user.getObjectID()) &&
                            (task.getProject().isUserBackupManager(user.getObjectID()) || task.getProject().getBackupManagers().size() == 0))) {
                EdsReference closed = referenceManager.findReference(
                        EdsTask.TASK_STATUS, EdsTask.CLOSED);
                statuses.remove(closed);
            } else if (hideCloseStatusByProjectId != null && Objects.equals(task.getProject().getObjectID(), hideCloseStatusByProjectId)) {
                EdsReference closed = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.CLOSED);
                statuses.remove(closed);
            }
        }
        return referenceSelectItem(statuses);
    }

    public SelectItem[] referenceSelectItem(List<EdsReference> references) {
        SelectItem[] selectItems = new SelectItem[references.size()];
        int i = 0;
        for (EdsReference status : references) {
            selectItems[i] = new SelectItem();
            selectItems[i].setId(status.getObjectID());
            selectItems[i].setDescription(status.getCode());
            String value = status.getName();
            selectItems[i].setSelected(status.isRequiredComment());
            selectItems[i].setName(value);
            i++;
        }

        return selectItems;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetProjectItem[] getProjects() {
        EdsUser user = timesheetManager.getUser();

        List<EdsEmployeeTask> employeeTasks = employeeTaskManager.getEmployeeTasks(user.getEmployee());
        ArrayList<EdsProject> projectItems = new ArrayList<>();

        for (EdsEmployeeTask employeeTask : employeeTasks) {
            EdsProject pro = employeeTask.getProjectEmployee().getProject();
            if (!projectItems.contains(pro)) {
                projectItems.add(pro);
            }
        }
        TimesheetProjectItem[] result = new TimesheetProjectItem[projectItems.size()];
        int j = 0;
        for (EdsProject proj : projectItems) {
            result[j] = new TimesheetProjectItem(proj.getObjectID(), proj.getName());
            if (proj.getManager() != null) {
                result[j].setProjectManager(proj.getManager().getFullName());
            }
            if (proj.getBackupManager() != null) {
                result[j].setBackupManager(proj.getBackupManager().getFullName());
            }
            j++;
        }
        Arrays.sort(result, Comparator.comparing(SelectItem::getName));
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSheetEntriesPerPeriod getEntries(DateNonConvertable fromDate, DateNonConvertable endDate, ArrayList<Integer> projectIDs, LinkedHashMap<String, String> projectTasks) {
        EdsEmployee employee = (EdsEmployee) referenceManager.getUser();
        EdsReference waiting = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
        EdsReference approved = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");
        List<EdsTimeSheet> entries;
        if (projectIDs != null) {
            entries = timesheetManager.getTimesheetEntriesForApproval(projectIDs, employee, fromDate.getNonConvertedDate(), endDate.getNonConvertedDate(), waiting, approved);
        } else {
            entries = timesheetManager.getTimesheetEntriesForApprovalByProjectAndTaskIds(projectTasks, employee, fromDate.getNonConvertedDate(), endDate.getNonConvertedDate(), waiting, approved);
        }
        TimeSheetEntriesPerPeriod tentries = new TimeSheetEntriesPerPeriod();

        Calendar from = new GregorianCalendar();
        from.setTime(fromDate.getNonConvertedDate());
        ServerUtils.setBeginningOfTheDay(from);
        tentries.setFromDate(new DateNonConvertable(from.getTime()));

        Calendar to = new GregorianCalendar();
        to.setTime(endDate.getNonConvertedDate());
        ServerUtils.setEndOfTheDay(to);
        tentries.setToDate(new DateNonConvertable(to.getTime()));


        Map<Integer, List<TimeSheetEntry>> taskTimeEntries = new HashMap<>();
        List<EdsEmployeeTask> tasks = new ArrayList<>();
        List<Integer> taskIdList = new ArrayList<>();
        for (EdsTimeSheet ts : entries) {
            if (!taskIdList.contains(ts.getEmployeeTask().getObjectID())) {
                taskIdList.add(ts.getEmployeeTask().getObjectID());
                tasks.add(ts.getEmployeeTask());
            }
            if (!taskTimeEntries.containsKey(ts.getEmployeeTask().getObjectID())) {
                taskTimeEntries.put(ts.getEmployeeTask().getObjectID(), new ArrayList<>());
            }
            TimeSheetEntry te = new TimeSheetEntry();
            te.setTimeSheetId(ts.getObjectID());
            if (ts.getTimeSpent() != null) {
                te.setTimeSpent(ts.getTimeSpent());
            } else {
                te.setTimeSpent(0);
            }
            taskTimeEntries.get(ts.getEmployeeTask().getObjectID()).add(te);
        }
        TaskTimeSheetEntry[] entry = new TaskTimeSheetEntry[tasks.size()];
        for (int i = 0; i < tasks.size(); i++) {
            EdsEmployeeTask empTask = (EdsEmployeeTask) tasks.get(i);
            Integer taskid = empTask.getObjectID();
            entry[i] = new TaskTimeSheetEntry();
            entry[i].setTaskId(taskid);
            entry[i].setProjectId(empTask.getTask().getProject().getObjectID());
            entry[i].setTaskName(empTask.getTask().getName());
            TimeSheetEntry[] tes = taskTimeEntries.get(taskid).toArray(new TimeSheetEntry[]{});
            entry[i].setEntries(tes);
            int totaltimespent = 0;
            for (TimeSheetEntry te : tes) {
                totaltimespent += te.getTimeSpent();
            }
            entry[i].setTotalTimeSpent(totaltimespent);
        }
        tentries.setEntries(entry);
        tentries.setSettings(getTimesheetSettings());

        return tentries;
    }

    @Transactional
    public void submitTimesheetForApproval(TimeSheetEntriesPerPeriod timesheetForApproval) {
        if (timesheetForApproval.getEntries() != null && timesheetForApproval.getEntries().length > 0) {
            EdsReference waiting = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
            EdsReference sessionWaitingStatus = referenceManager.findReference(EdsTimeSheetApprovalSession.TIME_SHEET_APPROVAL_SESSION_STATUS, EdsTimeSheetApprovalSession.WAITING_FOR_APPROVAL);
            HashMap<Integer, EdsTimeSheetApprovalSession> employeesTimesheets = new HashMap<>();
            TaskTimeSheetEntry[] timeSheetEntries = timesheetForApproval.getEntries();
            List<EdsTimeSheet> entries = new ArrayList<>();
            Map<Integer, EdsProject> projectMap = new HashMap<>();
            StringBuilder ids = new StringBuilder();
            boolean hasString = false;

            EdsEmployee employee = (EdsEmployee) employeeManager.getUser();
            if (timesheetForApproval.getEmployeeID() != null) {
                employee = employeeManager.get(timesheetForApproval.getEmployeeID());
            }

            EdsProject project = null;
            EdsEmployee approver = null;
            for (TaskTimeSheetEntry te : timeSheetEntries) {
                for (TimeSheetEntry t : te.getEntries()) {
                    if (!projectMap.containsKey(te.getProjectId())) {
                        project = projectManager.get(te.getProjectId());
                        projectMap.put(project.getObjectID(), project);
                    } else {
                        project = projectMap.get(te.getProjectId());
                    }

                    approver = timesheetForApproval.getApproverID() != null
                            ? employeeManager.get(timesheetForApproval.getApproverID())
                            : genericSettingsManager.isSettingsEnabled(ENABLE_MULTI_PROJECT_TO_TIMESHEET) ? employee.getProfile().getReportsTo() : project.getManager();

                    EdsTimeSheet timeSheet = timesheetManager.get(t.getTimeSheetId());
                    timeSheet.setStatus(waiting);
                    timeSheet.setSubmittedDate(new Date());
                    if (employeesTimesheets.containsKey(project.getObjectID())) {
                        EdsTimeSheetApprovalSession timesheetSession = employeesTimesheets.get(project.getObjectID());
                        timesheetSession.getTimeentries().add(timeSheet);
                    } else {
                        EdsTimeSheetApprovalSession timesheetSession = new EdsTimeSheetApprovalSession();

                        Calendar fromDate = new GregorianCalendar();
                        fromDate.setTime(timesheetForApproval.getFromDate().getNonConvertedDate());
                        ServerUtils.setBeginningOfTheDay(fromDate);
                        timesheetSession.setStartDate(fromDate.getTime());

                        Calendar toDate = new GregorianCalendar();
                        toDate.setTime(timesheetForApproval.getToDate().getNonConvertedDate());
                        ServerUtils.setEndOfTheDay(toDate);
                        timesheetSession.setEndDate(toDate.getTime());

                        timesheetSession.setSubmittedDate(new Date());

                        timesheetSession.setStatus(sessionWaitingStatus);
                        timesheetSession.getTimeentries().add(timeSheet);
                        timesheetSession.setEmployee(employee);
                        timesheetSession.setProject(project);
                        timesheetSession.setApprover(approver);
                        timeSheetApprovalSessionManager.create(timesheetSession);
                        employeesTimesheets.put(project.getObjectID(), timesheetSession);
                    }
                }
            }
            for (EdsProject proj : projectMap.values().toArray(new EdsProject[]{})) {
                EdsTimeSheetApprovalSession session = employeesTimesheets.get(proj.getObjectID());
                baseEventPostProcessor.registerEvent(TimeSheetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, session, employee, approver);
            }
        }
        KpiLog kpiLog = SecurityContext.getInstance().getKpiLog();
        kpiLog.setEntityName(EdsTimeSheet.class.getSimpleName());
        kpiLog.setActionType(KpiLog.ActionType.UPDATE);
        ServerUtils.kpiLog(log, kpiLog, "Submitted timesheet for approval");
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<TimeSheetApprovalListItem> getTimeSheetApprovalSessionList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        EdsReference rejected = referenceManager.findReference(EdsTimeSheetApprovalSession.TIME_SHEET_APPROVAL_SESSION_STATUS, EdsTimeSheetApprovalSession.REJECTED);
        if (fp.getSortField() == null || "".equals(fp.getSortField())) {
            fp.setSortField(TimeSheetApprovalListItem.STATUS);
        }

        List<EdsTimeSheetApprovalSession> approvalList = timeSheetApprovalSessionManager.getList(fp, rejected.getObjectID());
        StringBuilder approvers;
        ArrayList<TimeSheetApprovalListItem> tsApprovalListItem = new ArrayList<>();
        EdsReference approve = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");
        for (EdsTimeSheetApprovalSession tas : approvalList) {
            TimeSheetApprovalListItem item = new TimeSheetApprovalListItem();
            item.setEmployeeName(tas.getEmployee().getName());
            item.setFromDate(new DateNonConvertable(tas.getStartDate()));
            item.setTimeSpent(totalTimeSpent(tas.getTimeentries()));
            item.setEndDate(new DateNonConvertable(tas.getEndDate()));
            item.setSubmittedDate(tas.getSubmittedDate() != null
                    ? new DateNonConvertable(tas.getSubmittedDate())
                    : null);
            item.setApprovalDate(tas.getApprovalDate() != null ? new DateNonConvertable(tas.getApprovalDate()) : null);
            item.setId(tas.getObjectID());
            item.setProjectName(tas.getProject().getName());
            if (tas.getApprover() != null) {
                approvers = new StringBuilder(tas.getApprover().getName());
                approvers.append(", ").append(tas.getProject().getManager().getName());
            } else {
                approvers = new StringBuilder(tas.getProject().getManager().getName());
            }
            List<EdsEmployee> backupManagers = tas.getProject().getBackupManagers();
            for (EdsEmployee backupManager : backupManagers) {
                approvers.append(", ").append(backupManager.getName());
            }
            item.setApprover(approvers.toString());
            if (tas.getStatus() != null) {
                item.setStatus(referenceWfmMessageSource.localizeRef(tas.getStatus()));
                item.setStatusCode(tas.getStatus().getCode());
            }
            BigInteger approvedHours = timeSheetApprovalSessionManager.getApprovedTimesheetHours(tas.getObjectID(), approve.getObjectID());
            if (approvedHours != null) {
                String appHours = ServerUtils.timeSpentToString(approvedHours.intValue());
                item.setApprovedHours(appHours);
            }
            tsApprovalListItem.add(item);
        }
        Integer totalCount = timeSheetApprovalSessionManager.getTotalCount(fp, rejected.getObjectID());
        return new ListResult<>(tsApprovalListItem, totalCount);
    }

    private String totalTimeSpent(Set<EdsTimeSheet> entries) {
        int timeSpent = 0;
        for (EdsTimeSheet ts : entries) {
            if (ts.getTimeSpent() != null) {
                timeSpent += ts.getTimeSpent();
            }
        }
        String timeSpentHM;
        if (timeSpent == 0) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timeSpent / 60 < 10) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpent % 60 < 10) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpent % 60;
        return timeSpentHM;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimeSheetApprovalSingleItemsList getTimeSheetApprovalSingleListItems(Integer timeSheetApprovalItem) {
        TimeSheetApprovalSingleItemsList list = new TimeSheetApprovalSingleItemsList();

        List<EdsReference> statuses = referenceManager.getTimeSheetEntryStatuses();
        EdsReference waitingForApproveStatus = null;//referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
        EdsReference approveStatus = null; //referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");//time sheet entry status -- approve;
        EdsReference rejectStatus = null; //referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT"); //time sheet entry status -- reject;

        //Integer waitingId = waitingForApproveStatus.getObjectID();

        List<SelectItem> actions = new ArrayList<>();
        for (EdsReference status : statuses) {
            switch (status.getCode()) {
                case TimesheetConstants.APPROVE -> {
                    approveStatus = status;
                    actions.add(new SelectItem(status.getObjectID(), referenceWfmMessageSource.localizeRef(status)));
                }
                case TimesheetConstants.REJECT -> {
                    rejectStatus = status;
                    actions.add(new SelectItem(status.getObjectID(), referenceWfmMessageSource.localizeRef(status)));
                }
                case TimesheetConstants.WAITING -> waitingForApproveStatus = status;
            }
        }

        list.setActions(actions.toArray(new SelectItem[0]));
        list.setId(timeSheetApprovalItem);

        EdsTimeSheetApprovalSession tsasession = timeSheetApprovalSessionManager.get(timeSheetApprovalItem);
        List<TimeSheetApprovalSingleItem> singleItems = new ArrayList<>();

        for (EdsTimeSheet ts : tsasession.getTimeentries()) {
            TimeSheetApprovalSingleItem titem = new TimeSheetApprovalSingleItem();
            titem.setComment(ts.getComment());
            titem.setDate(new DateNonConvertable(ts.getDate()));
            titem.setId(ts.getObjectID());
            titem.setDescription(ts.getEmployeeTask().getTask().getDescription());
            titem.setTaskName(ts.getEmployeeTask().getTask().getName());
            titem.setManagerComment(ts.getManagerComment());
            titem.setProjectName(ts.getEmployeeTask() != null ? ts.getEmployeeTask().getTask().getProject().getName() : "&nbsp;");
            titem.setTimeSpent("");
            if (ts.getStatus() != null && waitingForApproveStatus.getObjectID().equals(ts.getStatus().getObjectID())) {
                titem.setTimeSpent(ts.getTimeSpentHM());
            }
            titem.setTimeSpentInt(ts.getTimeSpent() != null ? ts.getTimeSpent() : 0);
            titem.setEstimatedTime(ServerUtils.timeSpentToString(ts.getEmployeeTask().getEstimatedTime()));
            String hourType = "-";
            if (ts.getType() != null) {
                hourType = ts.getType().getName();
            }
            titem.setHourType(hourType);
            if (ts.getStatus() != null) {
                titem.setApproved(approveStatus.getObjectID().equals(ts.getStatus().getObjectID()));
                titem.setRejected(rejectStatus.getObjectID().equals(ts.getStatus().getObjectID()));
            }
            /*Integer approvedHours = timesheetManager.getTotalTimeSheetHours(ts.getEmployeeTask().getObjectID(), approve.getObjectID());
            if (approvedHours != null) {
                String appHours = ServerUtils.timeSpentToString(approvedHours);
                titem.setApprovedHours(appHours);
            }*/
            titem.setApprovedHours("");
            if (ts.getStatus() != null && approveStatus.getObjectID().equals(ts.getStatus().getObjectID())) {
                titem.setApprovedHours(ServerUtils.timeSpentToString(ts.getTimeSpent()));
            }
            singleItems.add(titem);
        }
        list.setTotalCount(singleItems.size());
        list.setItems(singleItems.toArray(new TimeSheetApprovalSingleItem[]{}));
        list.setEmployeeName(tsasession.getEmployee().getName());
        list.setStatusCode(tsasession.getStatus().getCode());
        HashSet<String> permissions = projectService.getProjectSpecificPermissions(tsasession.getProject().getObjectID());
        list.setPermission(permissions.contains(PermissionConstants.PM_APPROVE_REJECT) ? EDIT : READ);
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        if (numberingSettings != null) {
            list.setTimesheetApprovalCommentRequired(numberingSettings.getTimesheetApprovalCommentRequired());
        }
        return list;
    }

    @Transactional
    public Boolean approveRejectTimesheetHours(TaskTimeSheetEntry[] reportItems, ArrayList<Integer> employeeIDs) {
        Map<Integer, TaskTimeSheetEntry> items = new HashMap<>();
        Map<Integer, EdsTimeSheetApprovalSession> timesheetApprovalSessions = new HashMap<>();
        Map<Integer, HashMap<Integer, String>> approvedHours = new HashMap<>();
        List<Integer> appSessionIDList = new ArrayList<>();
        if (reportItems != null) {
            for (TaskTimeSheetEntry reportItem : reportItems) {
                items.put(reportItem.getEntries()[0].getTimeSheetId(), reportItem);
            }
        }

        EdsReference timesheetApproved = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");              // for timesheet entry
        EdsReference timesheetRejected = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT");               // for timesheet entry
        EdsReference sessionApproved = referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", "_APPROVED"); // for timesheet approval session
        EdsReference sessionRejected = referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", "_REJECTED"); // for timesheet approval session
        EdsReference sessionWaitingStatus = referenceManager.findReference(EdsTimeSheetApprovalSession.TIME_SHEET_APPROVAL_SESSION_STATUS, EdsTimeSheetApprovalSession.WAITING_FOR_APPROVAL);
        Date approvalDate = new Date();
        EdsUser user = timesheetManager.getUser();
        SimpleDateFormat dateAndTimeFormatShort = new SimpleDateFormat("MMM dd, yyyy [HH:mm]", Locale.US);

        Set<EdsTimeSheet> tentries = new HashSet<>();
        for (TaskTimeSheetEntry ts : reportItems) {
            Object[] approvalSession = timeSheetApprovalSessionManager.getListTimesheetApprovalList(ts.getEntries()[0].getTimeSheetId());
            Integer approvalSessionId = (Integer) approvalSession[0];
            EdsTimeSheet timesheet = timesheetManager.get(ts.getEntries()[0].getTimeSheetId());
            String managerComment = "";
            if (ts.isRejected()) {
                if (ts.getManagerComment() != null && !"".equals(ts.getManagerComment())) {
                    managerComment = " " + dateAndTimeFormatShort.format(user.getUserDate()) + " " + user.getName() + ": " + ts.getManagerComment();
                }
            } else {
                if (ts.getManagerApproveComment() != null && !"".equals(ts.getManagerApproveComment())) {
                    managerComment = " " + dateAndTimeFormatShort.format(user.getUserDate()) + " " + user.getName() + ": " + ts.getManagerApproveComment();
                }
            }

            if (timesheetApprovalSessions.containsKey(approvalSessionId)) {
                EdsTimeSheetApprovalSession appSession = timesheetApprovalSessions.get(approvalSessionId);
                if (ts.isApproved()) {
                    appSession.setStatus(sessionApproved);
                    timesheet.setManagerComment(managerComment);
                    appSession.setApprovalDate(approvalDate);
                    HashMap<Integer, String> stringHashMap = approvedHours.get(approvalSessionId);
                    stringHashMap.put(approvalSessionId, EdsTimeSheet._APPROVE);
                    timesheet.setStatus(timesheetApproved);
                    timesheet.setApprovalDate(approvalDate);
                    timesheet.setRejectedDate(null);
                }
                if (ts.isRejected()) {
                    if (appSession.getStatus() == null || sessionWaitingStatus.equals(appSession.getStatus())) {
                        appSession.setStatus(sessionRejected);
                    }
                    timesheet.setManagerComment(managerComment);
                    HashMap<Integer, String> stringHashMap = new HashMap<>();
                    stringHashMap.put(approvalSessionId, EdsTimeSheet._REJECT);
                    approvedHours.put(approvalSessionId, stringHashMap);
                    timesheet.setStatus(timesheetRejected);
                    timesheet.setApprovalDate(null);
                    timesheet.setRejectedDate(new Date());
                }
                appSession.getTimeentries().add(timesheet);
            } else {
                EdsTimeSheetApprovalSession appSession = timeSheetApprovalSessionManager.get(approvalSessionId);
                if (ts.isApproved()) {
                    appSession.setStatus(sessionApproved);
                    timesheet.setManagerComment(managerComment);
                    appSession.setApprovalDate(approvalDate);
                    HashMap<Integer, String> stringHashMap = new HashMap<>();
                    stringHashMap.put(approvalSessionId, EdsTimeSheet._APPROVE);
                    approvedHours.put(approvalSessionId, stringHashMap);
                    timesheet.setStatus(timesheetApproved);
                    timesheet.setApprovalDate(approvalDate);
                    timesheet.setRejectedDate(null);
                }
                if (ts.isRejected()) {
                    if (appSession.getStatus() == null || sessionWaitingStatus.equals(appSession.getStatus())) {
                        appSession.setStatus(sessionRejected);
                    }
                    timesheet.setManagerComment(managerComment);

                    HashMap<Integer, String> stringHashMap = new HashMap<>();
                    stringHashMap.put(approvalSessionId, EdsTimeSheet._REJECT);
                    approvedHours.put(approvalSessionId, stringHashMap);
                    timesheet.setStatus(timesheetRejected);
                    timesheet.setApprovalDate(null);
                    timesheet.setRejectedDate(new Date());
                }
                appSession.getTimeentries().add(timesheet);
                timesheetApprovalSessions.put(approvalSessionId, appSession);
                appSessionIDList.add(approvalSessionId);
            }

            tentries.add(timesheet);
        }
        for (Integer appSessionId : appSessionIDList) {
            EdsTimeSheetApprovalSession session = timesheetApprovalSessions.get(appSessionId);
            timeSheetApprovalSessionManager.update(session);
            HashMap<Integer, String> map = approvedHours.get(appSessionId);
            updateSessionTimeSheetEntries(appSessionId, map);
            baseEventPostProcessor.registerEvent(TimeSheetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, session, user);
        }
        return true;
    }

    @Transactional
    public void timesheetBatchApproveOrReject(ArrayList<Integer> itemIds, String comment, boolean isApproved) {
        EdsUser user = timesheetManager.getUser();
        EdsUser manager = referenceManager.getUser();
        EdsReference approve = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", EdsTimeSheet._APPROVE);
        EdsReference reject = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", EdsTimeSheet._REJECT);
        SimpleDateFormat dateAndTimeFormatShort = new SimpleDateFormat("MMM dd, yyyy [HH:mm]", Locale.US);

        Date actionTime = new Date();
        for (Integer itemId : itemIds) {
            EdsTimeSheetApprovalSession session = timeSheetApprovalSessionManager.get(itemId);
            Map<Integer, String> approvedHours = new HashMap<>();
            for (EdsTimeSheet timeSheet : session.getTimeentries()) {
                if (isApproved) {
                    if (timeSheet.getStatus() != null && timeSheet.getStatus().getObjectID().equals(approve.getObjectID())) {
                        approvedHours.put(timeSheet.getObjectID(), EdsTimeSheet._APPROVE);
                    }
                    timeSheet.setStatus(approve);
                    timeSheet.setApprovalDate(actionTime);
                    timeSheet.setRejectedDate(null);
                } else {
                    if (timeSheet.getStatus() != null && timeSheet.getStatus().getObjectID().equals(approve.getObjectID())) {
                        approvedHours.put(timeSheet.getObjectID(), EdsTimeSheet._REJECT);
                    }

                    timeSheet.setStatus(reject);
                    timeSheet.setApprovalDate(null);
                    timeSheet.setRejectedDate(actionTime);
                }

                if (comment != null && !"".equals(comment)) {
                    String managerComment = dateAndTimeFormatShort.format(user.getUserDate()) + " " + user.getName() + ": " + comment;
                    timeSheet.setManagerComment(managerComment);
                }
            }
            if (isApproved) {
                session.setStatus(referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", EdsTimeSheetApprovalSession.APPROVED));
                session.setApprovalDate(actionTime);
            } else {
                session.setStatus(referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", EdsTimeSheetApprovalSession.REJECTED));
            }
            updateSessionTimeSheetEntries(session.getObjectID(), approvedHours);

            baseEventPostProcessor.registerEvent(TimeSheetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, session, manager);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean saveTimeSheetApprovalSessionListItem(TimeSheetApprovalSingleItemsList item) {
        long begin = System.currentTimeMillis();
        EdsTimeSheetApprovalSession session = timeSheetApprovalSessionManager.get(item.getId());
        EdsUser manager = referenceManager.getUser();
        Map<Integer, TimeSheetApprovalSingleItem> items = new HashMap<>();
        EdsReference approve = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", EdsTimeSheet._APPROVE);
        EdsReference reject = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", EdsTimeSheet._REJECT);
        boolean totalApproved = false;
        EdsUser user = timesheetManager.getUser();
        SimpleDateFormat dateAndTimeFormatShort = new SimpleDateFormat("MMM dd, yyyy [HH:mm]", Locale.US);
        for (TimeSheetApprovalSingleItem titem : item.getItems()) {
            items.put(titem.getId(), titem);
            if (titem.isApproved()) {
                totalApproved = true;
            }
        }
        Date actionTime = new Date();
        Map<Integer, String> approvedHours = new HashMap<>();
        for (EdsTimeSheet ts : session.getTimeentries()) {
            if (items.containsKey(ts.getObjectID())) {
                if (items.get(ts.getObjectID()) != null) {
                    if (items.get(ts.getObjectID()).isApproved()) {
                        if (ts.getStatus() != null && ts.getStatus().getObjectID().equals(approve.getObjectID())) {
                            approvedHours.put(ts.getObjectID(), EdsTimeSheet._APPROVE);
                        }
                        ts.setStatus(approve);
                        ts.setApprovalDate(actionTime);
                        ts.setRejectedDate(null);
                    }
                    if (!items.get(ts.getObjectID()).isApproved()) {
                        if (ts.getStatus() != null && ts.getStatus().getObjectID().equals(approve.getObjectID())) {
                            approvedHours.put(ts.getObjectID(), EdsTimeSheet._REJECT);
                        }

                        ts.setStatus(reject);
                        ts.setApprovalDate(null);
                        ts.setRejectedDate(actionTime);
                    }
                    //Manager's comment
                    if (items.get(ts.getObjectID()).getManagerApproveComment() != null && !"".equals(items.get(ts.getObjectID()).getManagerApproveComment())) {
                        String managerComment = dateAndTimeFormatShort.format(user.getUserDate()) + " " + user.getName() + ": " +
                                items.get(ts.getObjectID()).getManagerApproveComment();
                        ts.setManagerComment(managerComment);
                    }
                }
            }
        }
        if (totalApproved) {
            session.setStatus(referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", EdsTimeSheetApprovalSession.APPROVED));
            session.setApprovalDate(actionTime);
        } else {
            session.setStatus(referenceManager.findReference("_TIME_SHEET_APPROVAL_SESSION_STATUS", EdsTimeSheetApprovalSession.REJECTED));
        }
        System.out.println(">>P1 took - " + (System.currentTimeMillis() - begin));
        long begin1 = System.currentTimeMillis();
        updateSessionTimeSheetEntries(session.getObjectID(), approvedHours);
        System.out.println(">>P2 took - " + (System.currentTimeMillis() - begin1));

        baseEventPostProcessor.registerEvent(TimeSheetEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, session, manager);
        System.out.println("Approve Timesheet took - " + (System.currentTimeMillis() - begin));
        return true;
    }

    private void updateSessionTimeSheetEntries(Integer timeSheetApprovalSessionId, Map<Integer, String> approvedHours) {
        EdsTimeSheetApprovalSession updatedSession = timeSheetApprovalSessionManager.get(timeSheetApprovalSessionId);
        //create new one year if today's date is not in datejoin table
        dashboardService.lastEnteredDate();
        Integer rejectID = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID();
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        boolean isAutomatic = numberingSettings != null && numberingSettings.isAutomatic();

        //create attendance raw data for the current year and employee, if there is none
        availabilityService.createAttendaceRawDataRecords(updatedSession.getEmployee().getObjectID(), 0);
        for (EdsTimeSheet ts : updatedSession.getTimeentries()) {
            boolean wasApprovedNowRejecting = false, wasApprovedNowApproving = false;
            if (approvedHours.containsKey(ts.getObjectID())) {
                wasApprovedNowRejecting = approvedHours.get(ts.getObjectID()).equals(EdsTimeSheet._REJECT);
                wasApprovedNowApproving = approvedHours.get(ts.getObjectID()).equals(EdsTimeSheet._APPROVE);
            }
            updateTimeSpentAndBudget(ts, wasApprovedNowRejecting, wasApprovedNowApproving, false, rejectID, isAutomatic);

            saveAttendanceRawDataTimesheet(ts, ts.getTimeSpent(), wasApprovedNowRejecting, false, rejectID);

            if (approvedHours.containsValue(EdsTimeSheet._APPROVE)) {
                sendNotificationActualTimeReached(ts);
            }
            if (isAutomatic) {
                baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_CUSTOM, ts.getEmployeeTask(), timesheetManager.getUser());
            }
        }
    }

    @Transactional
    public Float updatePercentCompleted(Integer employeeTaskId, float percentCompleted, boolean solrUpdate) {
        EdsEmployeeTask eTask = employeeTaskManager.get(employeeTaskId);
        EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();//if settings is null then project percent calculation mode is manual by default
        if (settings == null || !settings.isAutomatic()) {
            eTask.setPercent(ServerUtils.decimalPrecision(percentCompleted, 2));
            employeeTaskManager.update(eTask);
            eTask.getTask().setPreviousPercent(eTask.getTask().getPercent());
            EdsProject project = eTask.getTask().getProject();
            Float average;
            Float averageProjectTasks;
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                average = eTask.getTask().getTaskAveragePercentCompletedNewLogic();
                averageProjectTasks = project.getProjectTasksAveragePercentCompletedNewLogic();
            } else {
                average = eTask.getTask().getTaskAveragePercentCompleted();
                averageProjectTasks = project.getProjectTasksAveragePercentCompleted();
            }
            eTask.getTask().setPercent(average);
            project.setPercent(averageProjectTasks);
            projectManager.update(project);
            EdsTask task = eTask.getTask();
            EdsUser user = employeeTaskManager.getUser();
            if ((task.getIssue() == null || !task.getIssue()) && solrUpdate) {
                solrAndRbacEntries(user, task);
            }
        }
        return eTask.getPercent();
    }

    public void sendMailToAccountants(Map<String, List<TimesheetItem>> data, EdsUser user, Date startDate, Date endDate) {
        try {
            if (user == null || !user.getCompany().getActive() || user.getDeleted() || user.getAccountStatus() == null || EMPLOYEE_STATUS_INACTIVE.equals(user.getAccountStatus().getCode()))
                return;
            messageManager.sendTimesheetReminder(data, user, startDate, endDate);
            log.info("SendTimesheetReminder " + NotificationTypeEnum.TimeSheetDueReminder.name() + ", Username:" + user.getFullName() + "," + user.getDeviceToken() + "," + user.getDeviceType());
            notificationMsgManager.createTimeSheetOverDueReminderNotificationEvent(user, ActionOnEntityEnum.TIMESHEET_REMINDER);

        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectsList(EdsUser user, Integer viewAs) {
        ListingFilterParameter fp = new ListingFilterParameter(null, null, null, user.getObjectID(), viewAs);
        List<EdsProject> projects = projectManager.list(fp, user);
        SelectItem[] result = new SelectItem[projects.size()];
        int i = 0;
        for (EdsProject project : projects) {
            result[i] = new SelectItem();
            result[i].setId(project.getObjectID());
            result[i].setName(project.getName());
            i++;
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetSettings getTimesheetSettings() {
        EdsUser loggedUser = timesheetManager.getUser();
        EdsTimeSheetSettings settings = timesheetSettingsManager.getCompanyTimesheetSettings(loggedUser.getCompany());

        TimesheetSettings result = new TimesheetSettings();
        result.setMaxMinutesPerDay(settings.getMaxHoursPerDay());
        result.setMinMinutesPerDay(settings.getMinHoursPerDay());
        result.setValidateDailyTimesheets(settings.isValidateDailyTimesheets());
        result.setValidateTimesheetApproval(settings.isValidateTimesheetApproval());
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        if (numberingSettings != null) {
            result.setTimesheetApprovalCommentRequired(numberingSettings.getTimesheetApprovalCommentRequired());
            result.setTimesheetCommentRequired(numberingSettings.getTimesheetCommentRequired());
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getEmployeesList(Integer clientId, Integer projectId, Integer formType) {
        EdsUser user = userManager.getUser();
        List<SelectItem> employeesList = new ArrayList<>();
        List<EdsRole> roleList = new LinkedList<EdsRole>(user.getRolesSorted());
        Integer userMaxRoleId = getUserSortedRolesList().get(0);
        EdsRole pmRole = roleManager.get(EdsRole.PM);
        if (formType.equals(TIMESHEET_APPROVAL_FORM) && roleList.contains(pmRole)) {
            userMaxRoleId = EdsRole.PM;
        }
        if (projectId != null && projectId > 0) {
            EdsProject project = projectManager.get(projectId);
            if (project != null) {
                return ServerUtils.sortSelectItem(getEmployeesList(project, userMaxRoleId, employeesList).toArray(new SelectItem[]{}));
            }
        } else {
            List<EdsProject> projectList = new ArrayList<>();
            if (formType.equals(TIMESHEET_APPROVAL_FORM) && roleList.contains(pmRole)) {
                userMaxRoleId = EdsRole.PM;
                projectList = projectManager.getEmployeeManagedProjects((EdsEmployee) user);
            } else {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setEmployeeId(user.getObjectID());
                fp.setViewAsId(userMaxRoleId);
                projectList = projectManager.list(fp);
            }
            if (projectList != null && projectList.size() > 0) {
                for (EdsProject project : projectList) {
                    employeesList = getEmployeesList(project, userMaxRoleId, employeesList);
                }
                return ServerUtils.sortSelectItem(employeesList.toArray(new SelectItem[]{}));
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SelectItem> getEmployeesList(EdsProject project, Integer userMaxRoleId, List<SelectItem> employeesList) {
        if (project != null) {
            List<Object[]> projectEmployeesList = projectManager.getProjectEmployees(project.getObjectID(), userMaxRoleId);
            if (projectEmployeesList != null && projectEmployeesList.size() > 0) {
                for (Object[] item : projectEmployeesList) {
                    EdsProjectEmployee projectEmployee = (EdsProjectEmployee) item[0];
                    EdsEmployee employee = (EdsEmployee) item[1];
                    if (employee != null) {
                        SelectItem employeeItem = new SelectItem(employee.getObjectID(), employee.getFullName());
                        if (!employeesList.contains(employeeItem)) {
                            employeesList.add(employeeItem);
                        }
//                        }
                    }
                }
            }
            return employeesList;
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getProjectsList(Integer clientId, Integer employeeId) {
        EdsUser user = referenceManager.getUser();
        List<EdsRole> roleList = new LinkedList<EdsRole>(user.getRolesSorted());
        return reportService.getProjectListForReport(clientId, null, employeeId, roleList.get(0).getObjectID(), null);
    }

    private List<Integer> getUserSortedRolesList() {
        EdsUser user = referenceManager.getUser();
        List<EdsRole> roleList = new LinkedList<EdsRole>(user.getRolesSorted());
        List<Integer> roleIDs = new ArrayList<>();
        for (EdsRole role : roleList) {
            roleIDs.add(role.getObjectID());
        }
        return ServerUtils.getUserRolesSorted(roleIDs);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public TimesheetData getProjectsAndClients(Integer employeeId, Integer formType, Boolean oldProjects) {
        EdsUser user = referenceManager.getUser();
        List<EdsRole> roleList = new LinkedList<EdsRole>(user.getRolesSorted());
        List<EdsProject> projectList = null;
        EdsRole pmRole = roleManager.get(EdsRole.PM);
        EdsRole adminRole = roleManager.get(EdsRole.ADMIN);
        Integer userMaxRoleId = null;
        if (formType.equals(TIMESHEET_APPROVAL_FORM) && roleList.contains(pmRole) && !roleList.contains(adminRole)) {
            userMaxRoleId = EdsRole.PM;
            projectList = projectManager.getEmployeeManagedProjects((EdsEmployee) user);
        } else {
            ListingFilterParameter fp = new ListingFilterParameter();
            if (!ServerUtils.hasPermission(PermissionConstants.PM_APPROVE_REJECT_ALL_TIMESHEETS, user)) {
                fp.setEmployeeId(employeeId);
            }
            if (oldProjects) {
                EdsReference all = referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ALL);
                fp.setProjectStatusId(all.getObjectID());
            }
            projectList = projectManager.list(fp);
        }
        TimesheetData timesheetData = new TimesheetData();
        int i = 0;
        List<SelectItem> projectsList = new ArrayList<>();
        List<SelectItem> clientsList = new ArrayList<>();
        if (projectList != null && projectList.size() > 0) {
            for (EdsProject project : projectList) {
                if (project != null) {
                    projectsList.add(new SelectItem(project.getObjectID(), project.getName()));
                }
            }
        }
        SelectItem[] employeeListItem = projectManager.getProjectEmployeeList(ServerUtils.getAsCommoDelimited(projectList, "(0)"), userMaxRoleId, user);
        timesheetData.setProjects(ServerUtils.sortSelectItem(projectsList.toArray(new SelectItem[]{})));
        timesheetData.setEmployees(employeeListItem);

        //timeSheet approvers
        boolean enableTimeSheetApproversDropDown = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_TIMESHEET_APPROVERS_DROPDOWN);
        if (enableTimeSheetApproversDropDown) {
            List<String> roleCodes = rolePermissionManager.getRolesByPermissionCode(PermissionConstants.PM_TIMESHEET_APPROVERS);
            if (roleCodes.isEmpty()) {
                roleCodes.add(EdsRole.ADMIN_CODE);
            }
            EdsEmployee currentEmployee = employeeManager.get(user.getObjectID());
            //approvers list
            List<EdsEmployee> timeSheetApprovers = employeeManager.getApprovers(currentEmployee, roleCodes);

            SelectItem[] approversSelectItem = ServerUtils.sortSelectItem(ServerUtils.getAsSelectItem(timeSheetApprovers, ServerUtils.EDS_EMPLOYEE));
            timesheetData.setApprovers(approversSelectItem);
        } else {
            timesheetData.setApprovers(new SelectItem[]{});
        }

        return timesheetData;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ReportResult[] getTimesheetReport(ArrayList<Integer> clientIDs, ArrayList<Integer> projectIDs,
                                             ArrayList<Integer> employeeId, Integer viewAsId, Integer statusId, String groupByName,
            DateNonConvertable from, DateNonConvertable to, boolean showClient, boolean showProject,
            boolean showDepartment, boolean showEmployee, boolean showTask,
            boolean showDate, boolean showComment, boolean showDescription, boolean showPercentCompleted, boolean showApprovedHours, boolean showStatus, boolean showTimesheetStatus, Integer formType) {

        if (projectIDs != null && projectIDs.size() > 0) {
            if (projectIDs.contains(0)) {
                projectIDs.remove(Integer.valueOf(0));
            }
        }
        if (TIMESHEET_APPROVAL_FORM.equals(formType)) {
            EdsUser user = taskManager.getUser();
            if (ServerUtils.hasPermission(PermissionConstants.PM_APPROVE_REJECT_ALL_TIMESHEETS, user)) {
                viewAsId = ADMIN;
            } else {
                viewAsId = PM;
            }
        } else if (TIMESHEET_SUBMIT_FOR_APPROVAL_FORM.equals(formType)) {
            viewAsId = MEM;
        }
        List<Object[]> values = projectManager.getResults(clientIDs, projectIDs, null,
                employeeId, viewAsId, groupByName, from.getNonConvertedDate(), to.getNonConvertedDate(), showClient,
                showProject, showDepartment, true, showTask, showDate, showComment, showDescription, showPercentCompleted,
                showApprovedHours, false, showStatus, showTimesheetStatus, formType, true, false, false);

        EdsReference approved = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_APPROVE");
        ReportResult[] result = new ReportResult[values.size()];
        int k = 0;
        HashMap<Date, Integer> dateMap = new HashMap<>();
        HashMap<Integer, Integer> projectItemPermission = new HashMap<>();
        Integer visibilityApproveReject = 0;
        for (Object[] value : values) {
            int i = 0;
            result[k] = new ReportResult();
            int g = 0;
            g = i++;
            result[k].setSum(Integer.parseInt(value[g] != null ? value[g].toString() : "0"));
            if (showClient || "Client".equals(groupByName)) {
                g = i++;
                result[k].setClientName(value[g] != null ? value[g].toString() : "N/A");
                g = i++;
            }
            if (showProject || "Project".equals(groupByName)) {
                g = i++;
                result[k].setProjectName(value[g] != null ? value[g].toString() : "N/A");
                g = i++;
                result[k].setProjectID((Integer) value[g]);
            }

            g = i++;
            result[k].setEmployeeName(value[g] != null ? value[g].toString() : "N/A");
            g = i++;
            result[k].setEmployeeID((Integer) value[g]);

            if (showDate || "Date".equals(groupByName)) {
                g = i++;
                Date date = null;
                try {
                    date = (Date) value[g];
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result[k].setCreatDate(new DateNonConvertable(date));
            }
            if (showTask) {
                g = i++;
                result[k].setTaskName(value[g] != null ? value[g].toString() : "N/A");
                g = i++;
                result[k].setTaskID((Integer) value[g]);
                g = i++;
                result[k].setTimesheetID((Integer) value[g]);
                g = i++;
                result[k].setBillable(value[g] != null ? (Boolean) value[g] : false);
                result[k].setApprovedHours(timesheetManager.getTotalTimeSheetHours(result[k].getTaskID(), approved.getObjectID()).toString());
            }
            if (showStatus) {
                g = i++;
                result[k].setStatus(value[g] != null ? value[g].toString() : "N/A");
            }
            if (showTimesheetStatus) {
                g = i++;
                result[k].setTimesheetStatus(value[g] != null ? value[g].toString() : "N/A");
            }
            if (showDescription) {
                g = i++;
                result[k].setDescription(value[g] != null ? value[g].toString() : "N/A");
            }

            g = i++;
            result[k].setEstimatedTime(value[g] != null ? Integer.valueOf(value[g].toString()) : Integer.valueOf(0));

            if (showComment) {
                g = i++;
                String comment = value[g] != null ? value[g].toString() : "";
                result[k].setComment(!"".equals(comment) ? comment : " ");
            }
            if (showPercentCompleted) {
                g = i++;
                result[k].setPercentCompleted(value[g] != null ? value[g].toString() + "%" : "0%");
            }
            if ("Client".equals(groupByName) || "Project".equals(groupByName) || "Employee".equals(groupByName) || "Date".equals(groupByName)) {
                g = i++;
                if (value[g] instanceof Date date) {
                    if (date != null && !dateMap.containsKey(date)) {
                        dateMap.put(date, k);
                    }
                    result[k].setGroupId(value[g] != null ? dateMap.get(date) : 0);
                } else {
                    result[k].setGroupId(Integer.valueOf(value[g] != null ? value[g].toString() : "0"));
                }
            }
            g = i++;
            String hourType = "-";
            if (value[g] != null) {
                hourType = referenceManager.get(Integer.valueOf(value[g].toString())).getName();
            }
            result[k].setHourType(hourType);

            visibilityApproveReject = READ;
            if (result[k].getProjectID() != null) {
                if (!projectItemPermission.containsKey(result[k].getProjectID())) {
                    HashSet<String> permissions = projectService.getProjectSpecificPermissions(result[k].getProjectID());
                    projectItemPermission.put(result[k].getProjectID(), permissions.contains(PermissionConstants.PM_APPROVE_REJECT)
                            ? EDIT
                            : READ);
                }
                visibilityApproveReject = projectItemPermission.get(result[k].getProjectID());
            }
            result[k].setApproveReject(visibilityApproveReject);
            k++;
        }
        return result;
    }

    /**
     * @param selectedDate
     * @param clientsCurrentDate
     * @param weekOffset         if weekOffset is null, timesheet  data will be retrieved from start/end date periods
     * @param fp
     * @return
     */
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FastTimesheetData getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsCurrentDate, Integer weekOffset, ListingFilterParameter fp) {
        return getFastTimesheetData(selectedDate, clientsCurrentDate, weekOffset, null, fp);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public FastTimesheetData getFastTimesheetData(DateNonConvertable selectedDate, DateNonConvertable clientsCurrentDate, Integer weekOffset, LinkedHashMap<String, String> projectTasks, ListingFilterParameter fp) {
        Date timer = new Date();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        EdsEmployee employee = (EdsEmployee) taskManager.getUser();
        if (fp.getEmployeeId() != null) {
            employee = employeeManager.get(fp.getEmployeeId());
        }

        Calendar todayCal = new GregorianCalendar();

        todayCal.setTime(clientsCurrentDate.getNonConvertedDate());
        todayCal.set(Calendar.AM_PM, 0);
        todayCal.set(Calendar.HOUR, 0);
        todayCal.set(Calendar.MINUTE, 0);
        todayCal.set(Calendar.SECOND, 0);
        todayCal.set(Calendar.MILLISECOND, 0);

        Calendar ytdCal = new GregorianCalendar();
        ytdCal.setTime(clientsCurrentDate.getNonConvertedDate());
        ytdCal.set(Calendar.AM_PM, 0);
        ytdCal.add(Calendar.DAY_OF_YEAR, -1);
        ytdCal.set(Calendar.HOUR, 0);
        ytdCal.set(Calendar.MINUTE, 0);
        ytdCal.set(Calendar.SECOND, 0);
        ytdCal.set(Calendar.MILLISECOND, 0);

        DateNonConvertable[] dates;
        if (weekOffset != null) {
            dates = getTimesheetWeeklyDates(clientsCurrentDate, weekOffset);
        } else {
            Calendar startDate = new GregorianCalendar();
            startDate.setTime(selectedDate.getNonConvertedDate());
            Calendar endDate = new GregorianCalendar();
            endDate.setTime(clientsCurrentDate.getNonConvertedDate());
            ArrayList<DateNonConvertable> availableDays = new ArrayList<>();
            while (endDate.getTime().compareTo(startDate.getTime()) >= 0) {
                Calendar nonDate = Calendar.getInstance();
                nonDate.setTime(startDate.getTime());
                ServerUtils.setBeginningOfTheDay(nonDate);
                availableDays.add(new DateNonConvertable(nonDate.getTime()));
                startDate.add(Calendar.DAY_OF_MONTH, 1);
            }
            dates = availableDays.toArray(new DateNonConvertable[0]);
        }

        if (fp.isFromMobile() && fp.isShortList()) {//Show only current date data
            for (DateNonConvertable dateNonConvertable : dates) {
                if (dateNonConvertable != null && dateNonConvertable.getDate() != null && dateNonConvertable.getDate().getTime() == selectedDate.getNonConvertedDate().getTime()) {
                    dates = new DateNonConvertable[1];
                    dates[0] = dateNonConvertable;
                    break;
                }
            }
        }
        FastTimesheetData result = new FastTimesheetData();
        DateNonConvertable clientsServersideToday = new DateNonConvertable();
        result.setClientsToday(clientsServersideToday);
        result.setDates(dates);

        Calendar startDate = new GregorianCalendar();
        startDate.setTime(dates[0].getNonConvertedDate());
        startDate.set(Calendar.AM_PM, 0);
        startDate.set(Calendar.HOUR, 0);
        startDate.set(Calendar.MINUTE, 0);
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);

        Calendar endDate = new GregorianCalendar();
        try {
            endDate.setTime(dates[dates.length - 1].getNonConvertedDate());
        } catch (Exception e) {
            log.error("", e);
            endDate.setTime(dates[6].getNonConvertedDate());
        }
        endDate.set(Calendar.AM_PM, 0);
        endDate.set(Calendar.HOUR, 23);
        endDate.set(Calendar.MINUTE, 59);
        endDate.set(Calendar.SECOND, 59);
        endDate.set(Calendar.MILLISECOND, 0);
        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        if (numberingSettings != null && !numberingSettings.getShowToDoListTasks()) {
            fp.setDoNotIncludeTasksFromToDoList(true);
        }
        List<EdsEmployeeTask> employeeTaskList = employeeTaskManager.listDueTasks(employee, startDate.getTime(), endDate.getTime(), projectTasks, fp);

        result.setEmployeeId(employee != null ? employee.getObjectID() : null);
        Map<Integer, FastTaskTransfer> taskTransferMap = new HashMap<>();
        StringBuilder taskIds = new StringBuilder();
        int i = 0;
        HashMap<String, Integer> undeletedEmployeeTasks = new HashMap<>();
        for (EdsEmployeeTask employeeTask : employeeTaskList) {
            if (taskTransferMap.containsKey(employeeTask.getTask().getObjectID())) {
                continue;
            }
            undeletedEmployeeTasks.put(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID() + "/" + employeeTask.getTask().getObjectID(), employeeTask.getObjectID());

            FastTaskTransfer transferTask = new FastTaskTransfer();
            taskTransferMap.put(employeeTask.getTask().getObjectID(), transferTask);
            EdsProject domainProject = employeeTask.getTask().getProject();
            EdsCrmAccount client = domainProject.getClient();

            transferTask.setTaskNumber(employeeTask.getTask().getNumber());

            if (employeeTask.getTask() instanceof EdsIssue) {
                transferTask.setIsIssue(true);
            } else {
                transferTask.setIsIssue(false);
            }

            transferTask.setEmplTaskId(employeeTask.getObjectID());
            transferTask.setEmplTaskName(employeeTask.getTask().getName());

            if (!domainProject.getClients().isEmpty()) {
                StringBuilder buildClientName = new StringBuilder();

                for (EdsCrmAccount c : domainProject.getClients()) {
                    if (c.getObjectID().equals(fp.getClientId())) {
                        transferTask.setClientName(c.getName());
                        break;
                    } else {
                        if (!buildClientName.toString().isEmpty()) {
                            buildClientName.append(", ");
                        }
                        buildClientName.append(c.getName());
                    }
                }

                if (transferTask.getClientName() == null) {
                    transferTask.setClientName(!buildClientName.toString().isEmpty() ? buildClientName.toString() : "N/A");
                }
            } else if (client != null) {
                transferTask.setClientName(client.getName());
            } else {
                transferTask.setClientName("N/A");
            }

            String number = domainProject.getNumber() != null ? domainProject.getNumber() + " " : "";

            transferTask.setProjectId(domainProject.getObjectID());
            transferTask.setProjectName(number + domainProject.getName());
            transferTask.getTaskStatus().setEmployeeTaskId(employeeTask.getObjectID());
            transferTask.getTaskStatus().setStatus(employeeTask.getStatus().getObjectID());
            transferTask.getTaskStatus().setStatusName(referenceWfmMessageSource.localizeRef(employeeTask.getStatus()));
            transferTask.getTaskStatus().setTaskId(employeeTask.getObjectID());
            if (employeeTask.getTask().getPriority() != null) {
                transferTask.getTaskStatus().setPriority(employeeTask.getTask().getPriority().getName());
            }
            transferTask.setEstimatedTime(employeeTask.getEstimatedTime());
            transferTask.setPercentCompleted(employeeTask.getPercent() == null ? 0 : ServerUtils.decimalPrecision(employeeTask.getPercent(), 2));
            transferTask.setTaskId(employeeTask.getTask().getObjectID());

            Calendar emplTaskStart = new GregorianCalendar();
            if (employeeTask.getStartDate() != null) {
                emplTaskStart.setTime(employeeTask.getStartDate());
            } else {
                emplTaskStart.setTime(employeeTask.getTask().getStartDate());
            }

            emplTaskStart.set(Calendar.AM_PM, 0);
            emplTaskStart.set(Calendar.HOUR, 0);
            emplTaskStart.set(Calendar.MINUTE, 0);
            emplTaskStart.set(Calendar.SECOND, 0);
            emplTaskStart.set(Calendar.MILLISECOND, 0);
            transferTask.setStartDate(emplTaskStart.getTime());
            if (employeeTask.getEndDate() != null) {
                Calendar emplTaskEnd = new GregorianCalendar();
                emplTaskEnd.setTime(employeeTask.getEndDate());
                emplTaskEnd.set(Calendar.AM_PM, 0);
                emplTaskEnd.set(Calendar.HOUR, 23);
                emplTaskEnd.set(Calendar.MINUTE, 59);
                emplTaskEnd.set(Calendar.SECOND, 59);
                emplTaskEnd.set(Calendar.MILLISECOND, 0);
                transferTask.setEndDate(emplTaskEnd.getTime());
            }
            if (employeeTask.getTask().getStartDate() != null) {
                transferTask.setTaskStartDate(employeeTask.getTask().getStartDate());
            }
            if (employeeTask.getTask().getDueDate() != null) {
                transferTask.setTaskEndDate(employeeTask.getTask().getDueDate());
            }
            if (i > 0) {
                taskIds.append(",").append(employeeTask.getTask().getObjectID().toString());
            } else {
                taskIds.append(employeeTask.getTask().getObjectID().toString());
            }
            transferTask.setTotalMinutes(0);
            i++;
        }

        if (!"".contentEquals(taskIds)) {
            List emplTaskTimeSheetTotal = timesheetManager.getEmployeeTaskTotalTimeSheets(taskIds.toString(), employee.getObjectID());
            for (Object anEmplTaskTimeSheetTotal : emplTaskTimeSheetTotal) {
                Object[] obj = (Object[]) anEmplTaskTimeSheetTotal;
                Integer taskId = (Integer) obj[0];
                Long totalMinuts = (Long) obj[1];
                taskTransferMap.get(taskId).setTotalMinutes(totalMinuts != null ? totalMinuts.intValue() : 0);
            }
            boolean showTaskRelated = companySystemSettingsManager.showTaskRelated();
            if (showTaskRelated) {
                Map<Integer, String> relationMap = relationManager.getAllRelationsForTimesheet(EdsRelation.TYPE_TASK, taskIds.toString());
                for (Integer taskId : relationMap.keySet()) {
                    taskTransferMap.get(taskId).setRelations(relationMap.get(taskId));
                }
            }
        }

        List<EdsTimeSheet> timesheets = timesheetManager.list(employee, dates[0].getNonConvertedDate(), dates[dates.length - 1].getNonConvertedDate());
        HashMap<String, TimesheetSummary> timesheetSummaryMap = new HashMap<>();
        for (EdsTimeSheet timeSheet : timesheets) {
            if (timesheetSummaryMap.containsKey(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID())) {
                timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).setMinutes(timeSheet.getTimeSpent());
                if (!timeSheet.getEmployeeTask().getDeleted()) {
                    timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).setTimeSheet(timeSheet);
                } else if (timeSheet.getTimeSpent() > 0) {
                    timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).getOldEmployeeTaskIDList().add(timeSheet.getEmployeeTask().getObjectID());
                    EdsTimeSheet currentTimesheet = timesheetSummaryMap.get(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID()).getTimeSheet();

                    if (currentTimesheet.getStatus() == null && timeSheet.getStatus() != null) {
                        currentTimesheet.setStatus(timeSheet.getStatus());
                    }
                }
            } else {
                TimesheetSummary timesheetSummary = new TimesheetSummary();
                timesheetSummary.setMinutes(timeSheet.getTimeSpent());
                timesheetSummary.setTimeSheet(timeSheet);
                if (timeSheet.getEmployeeTask().getDeleted() && timeSheet.getTimeSpent() > 0) {
                    ArrayList<Integer> oldEmployeeTaskIDList = new ArrayList<>();
                    oldEmployeeTaskIDList.add(timeSheet.getEmployeeTask().getObjectID());
                    timesheetSummary.setOldEmployeeTaskIDList(oldEmployeeTaskIDList);
                }
                timesheetSummaryMap.put(timeSheet.getDate().getTime() + "/" + timeSheet.getEmployeeID() + "/" + timeSheet.getTaskID(), timesheetSummary);
            }
        }

        TimesheetDataItem[] items = new TimesheetDataItem[timesheetSummaryMap.size()];
        i = 0;
        List<EdsReference> statusList = referenceManager.listReferences("_TIME_SHEET_ENTRY_STATUS");
        EdsReference approved = null;
        EdsReference waiting = null;
        EdsReference rejected = null;
        for (EdsReference reference : statusList) {
            if ("_APPROVE".equals(reference.getCode())) {
                approved = reference;
            } else if ("_WAITING".equals(reference.getCode())) {
                waiting = reference;
            } else if ("_REJECT".equals(reference.getCode())) {
                rejected = reference;
            }
        }

        HashMap<Integer, ArrayList<TimesheetDataItem>> timesheetDataItemMap = new HashMap<>();
        List<EdsReference> hourTypesList = referenceManager.listReferences(EdsTimeSheet._TIMESHEET_TYPE);
        SelectItem[] hourTypes = new SelectItem[hourTypesList.size()];
        int c = 0;
        for (EdsReference reference : hourTypesList) {
            hourTypes[c] = new SelectItem(reference.getObjectID(), reference.getName());
            c++;
        }
        for (TimesheetSummary timesheetSummary : timesheetSummaryMap.values()) {
            EdsTimeSheet timesheet = timesheetSummary.getTimeSheet();
            items[i] = new TimesheetDataItem();
            items[i].setDate(new Date(timesheet.getDate().getTime()));  // Non Convertable date is being used inside the TimeSheetDataItem
            items[i].setId(timesheet.getObjectID());
            items[i].setComment(timesheet.getComment());
            items[i].setHourTypeID(timesheet.getType() != null ? timesheet.getType().getObjectID() : 0);
            items[i].setHourTypes(hourTypes);
            items[i].setMinutes(timesheetSummary.getMinutes());
            items[i].setAutoApproved(timesheet.getAutoApproved());
            if (timesheet.getStatus() == null) {
                items[i].setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
            } else if (timesheet.getStatus().equals(approved)) {
                items[i].setStatus(TIMESHEET_ENTRY_APPROVED);
            } else if (timesheet.getStatus().equals(waiting)) {
                items[i].setStatus(TIMESHEET_ENTRY_WAITING);
            } else if (timesheet.getStatus().equals(rejected)) {
                items[i].setStatus(TIMESHEET_ENTRY_REJECTED);
            }
            items[i].setTeamID(timesheet.getTeamID());
            items[i].setEmployeeID(timesheet.getEmployeeID());
            items[i].setProjectID(timesheet.getProjectID());
            items[i].setTaskID(timesheet.getTaskID());
            items[i].setOldEmployeeTaskIDList(timesheetSummary.getOldEmployeeTaskIDList());
            if (timesheet.getEmployeeTask().getDeleted()) {
                if (undeletedEmployeeTasks.containsKey(timesheet.getEmployeeID() + "/" + timesheet.getTaskID())) {
                    items[i].setOldEmployeeTaskID(timesheet.getEmployeeTask().getObjectID());
                    items[i].setEmployeeTaskID(undeletedEmployeeTasks.get(timesheet.getEmployeeID() + "/" + timesheet.getTaskID()));
                }
            } else {
                items[i].setEmployeeTaskID(timesheet.getEmployeeTask().getObjectID());
            }

            if (timesheetDataItemMap.containsKey(timesheet.getEmployeeTask().getTask().getObjectID())) {
                timesheetDataItemMap.get(timesheet.getEmployeeTask().getTask().getObjectID()).add(items[i]);
            } else {
                ArrayList<TimesheetDataItem> list = new ArrayList<>();
                list.add(items[i]);
                timesheetDataItemMap.put(timesheet.getEmployeeTask().getTask().getObjectID(), list);
            }
            i++;
        }

        TimesheetReport[] weeklyReport = getWeeklyStatistics(dates[0].getNonConvertedDate(), employee);

        TimesheetReport[] monthlyReport;
        if (fp.useSelectedDate()) {
            monthlyReport = getMonthlyStatistics(selectedDate.getNonConvertedDate(), employee);
        } else {
            monthlyReport = getMonthlyStatistics(dates[0].getNonConvertedDate(), employee);
        }


        Integer projectId = 0;
        FastTaskTransfer[] transferTasks = sortTimesheetTasks(taskTransferMap);

        EdsAttendanceRawData[] attendanceRawDatas = new EdsAttendanceRawData[dates.length];
//        List<AttendanceItem> attendanceRawDataList = sickRequestManager.getEmployeeDurationItems(employee.getObjectID(), startDate.getTime(), endDate.getTime());
        List<EdsAttendanceRawData> attendanceRawDataList = attendanceRawDataManager.getAttendanceRawDataByDates(startDate.getTime(), endDate.getTime(), employee.getObjectID());
        int counter = 0;
        int[] actualPlannedTimes = new int[dates.length];
        boolean[] holiday = new boolean[dates.length];
        boolean[] LR = new boolean[dates.length];
        for (EdsAttendanceRawData attendanceRawData : attendanceRawDataList) {
            if (counter > 6) {
                break;
            }
            attendanceRawDatas[counter] = attendanceRawData;
            if (attendanceRawData.getHoliday() || attendanceRawData.getDayOff()) {
                actualPlannedTimes[counter] = 0;
            } else if (attendanceRawData.getLeave() > 0) {
                actualPlannedTimes[counter] = attendanceRawData.getTimeSlot() - attendanceRawData.getLeave();
            } else {
                actualPlannedTimes[counter] = attendanceRawData.getTimeSlot();
            }
            holiday[counter] = attendanceRawData.getHoliday();
            LR[counter] = attendanceRawData.getLeave() > 0 && attendanceRawData.getLeave() >= attendanceRawData.getTimeSlot();
            counter++;
        }
        for (FastTaskTransfer transfer : transferTasks) {
            fillTimesheetValue(dates, transfer, timesheetDataItemMap.get(transfer.getTaskId()), attendanceRawDatas, hourTypes, dates.length);

            if (!transfer.getProjectId().equals(projectId)) {
                StatData statData = new StatData();
                statData.setProjectFullName(transfer.getProjectName());
                statData.setProject(transfer.getProjectName().length() < 30
                        ? transfer.getProjectName()
                        : transfer.getProjectName().substring(0, 27) + "...");
                statData.setClient(transfer.getClientName().length() < 40
                        ? transfer.getClientName()
                        : transfer.getClientName().substring(0, 37) + "...");
                transfer.setShowStat(true);
                for (TimesheetReport aWeeklyReport : weeklyReport) {
                    if (transfer.getProjectName().endsWith(aWeeklyReport.getProjectName())) {
                        statData.setWeekly(aWeeklyReport.getSum());
                        break;
                    }
                }

                for (TimesheetReport aMonthlyReport : monthlyReport) {
                    if (transfer.getProjectName().endsWith(aMonthlyReport.getProjectName())) {
                        statData.setMonthly(aMonthlyReport.getSum());
                        break;
                    }
                }
                transfer.setStatData(statData);
                projectId = transfer.getProjectId();
            }
        }
        TimesheetFilterData timesheetFilterData = getProjectsAndWorkstreams(weekOffset, fp);

        result.setProjects(timesheetFilterData.getProjects());

        result.setClients(timesheetFilterData.getClients());

        result.setWorkstream(timesheetFilterData.getWorkstreams());

        result.setTransferTasks(transferTasks);

        if (employee.getStartDate() != null && employee.getStartDate().after(dates[dates.length - 1].getNonConvertedDate())) {
            result.setLastWeek(true);// we need to show exact weekdays
        } // in employee's timesheet (not the whole week)
        result.setToday(new DateNonConvertable(todayCal.getTime()));
        result.setYesterday(new DateNonConvertable(ytdCal.getTime()));

        result.setDailyStatistics(getDailyStatistics(dates, employee));
        result.setWeeklyStatistics(weeklyReport);
        result.setMonthlyStatistices(monthlyReport);
        TimeslotItem timeslotItem = new TimeslotItem();
        timeslotItem.setWeekDaysPlannedTime(actualPlannedTimes);
        timeslotItem.setHoliday(holiday);
        timeslotItem.setLR(LR);

        if (fp.useSelectedDate()) {
            todayCal.setTime(selectedDate.getNonConvertedDate());
        } else {
            todayCal.setTime(dates[0].getNonConvertedDate());
        }
        int monthlyPlannedTime = attendanceRawDataManager.getMonthlyPlanned(todayCal.get(Calendar.YEAR), todayCal.get(Calendar.MONTH) + 1, employee.getObjectID());
        timeslotItem.setMonthlyPlannedTime(monthlyPlannedTime);
        result.setTimeslotItem(timeslotItem);
        log.info(" getFastTimesheetData took: {}ms", System.currentTimeMillis() - timer.getTime());
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public ArrayList<MonthlyTimesheetItem> getMonthlyTimesheetData(DateNonConvertable selectedDate, Integer projectID, Integer employeeID, Boolean timesheetNotFilled) {
        ArrayList<MonthlyTimesheetItem> result = new ArrayList<>();//
        if (projectID == null)
            return result;
        ProjectMember[] projectMembers = employeeService.getProjectMembers(projectID, employeeID);
        EdsTask task = taskManager.getProjectTaskByName(projectID, "DEFAULT_TASK");
        EdsProject project = projectManager.get(projectID);

        if (!EmployeeAssignmentEnum.BY_POSITION.equals(project.getEmployeeAssignment())) {
            return result;
        }

        Map<Integer, EdsMonthlyTimesheet> monthlyTimesheetMap = monthlyTimesheetManager.getMonthlyTimesheetItems(projectID, employeeID, selectedDate);

        Date monthStartDate = selectedDate.getNonConvertedDate();
        Date monthEndDate = ServerUtils.getMonthEndDate(monthStartDate);
        EdsCompany company = projectManager.getUser().getCompany();

        boolean isEmployeeCodeInteger = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_EMPLOYEE_CODE_INTEGER);

        for (ProjectMember projectMember : projectMembers) {
            if (employeeID != null && !projectMember.getId().equals(employeeID)) {
                continue;
            }
            //check, if this employee contract is in the period or not
            if (!(monthStartDate.compareTo(projectMember.getContractStart().getNonConvertedDate()) <= 0 && projectMember.getContractStart().getNonConvertedDate().compareTo(monthEndDate) < 0
                    || projectMember.getContractEnd() != null && projectMember.getContractEnd().getNonConvertedDate().compareTo(monthStartDate) > 0 && projectMember.getContractEnd().getNonConvertedDate().compareTo(monthEndDate) <= 0
                    || projectMember.getContractStart().getNonConvertedDate().compareTo(monthStartDate) <= 0 && (projectMember.getContractEnd() == null
                    || projectMember.getContractEnd() != null && projectMember.getContractEnd().getNonConvertedDate().compareTo(monthEndDate) >= 0))) {
                continue;
            }

            MonthlyTimesheetItem item = new MonthlyTimesheetItem();
            item.setEmployeeName((projectMember.getEmployeeNumber() != null && !projectMember.getEmployeeNumber().isEmpty() ? projectMember.getEmployeeNumber() + " - " : "") + projectMember.getName());
            if (projectMember.getEmployeeNumber() != null && !"".equals(projectMember.getEmployeeNumber()) && isEmployeeCodeInteger) {
                try {
                    item.setEmployeeNumber(Long.parseLong(projectMember.getEmployeeNumber().replaceAll("[\\D]", "")));
                } catch (NumberFormatException ignored) {
                }
            }
            item.setProjectEmployeeID(projectMember.getProjectEmployeeId());
            item.setEmployeeID(projectMember.getId());
            item.setContractStart(projectMember.getContractStart() != null ? ServerUtils.shortDateFormat(projectMember.getContractStart().getNonConvertedDate(), company) : "");
            item.setContractEnd(projectMember.getContractEnd() != null ? ServerUtils.shortDateFormat(projectMember.getContractEnd().getNonConvertedDate(), company) : "");

            EdsMonthlyTimesheet mapItem = monthlyTimesheetMap.get(projectMember.getProjectEmployeeId());
            if (mapItem != null) {
                item.setWorkedHours(mapItem.getWorkedHours());
                item.setTotalWorkedDays(mapItem.getTotalDaysWorked());
                item.setOvertimeHours(mapItem.getOvertime());
                item.setHolidayOvertimeHours(mapItem.getHolidayOvertime());
                item.setWeekendOvertimeHours(mapItem.getWeekendOvertime());
            }
            if (task != null) {
                if (task.getProject() != null) {
                    if (task.getProject().getNumber() != null) {
                        item.setProjectName(task.getProject().getNumber() + "-" + task.getProject().getName());
                    } else {
                        item.setProjectName(task.getProject().getName());
                    }
                } else {
                    item.setProjectName("");
                }
            }
            if (!timesheetNotFilled || ((item.getWorkedHours() == null || item.getWorkedHours() == 0) && (item.getTotalWorkedDays() == null || item.getTotalWorkedDays() == 0))) {
                result.add(item);
            }
        }
        if (isEmployeeCodeInteger) {
            result.sort(Comparator.comparing(MonthlyTimesheetItem::getSortEmployeeNumber));
        } else {
            ComparatorFactory factory = comparatorFactoryMonthlyTimesheet.get("employeeName");
            result.sort(factory.createComparator(1));
        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveMonthlyTimesheetData(ArrayList<MonthlyTimesheetItem> items, DateNonConvertable selectedDate, Integer projectId) {
        String monthYear = monthlyTimesheetManager.getMonthYear(selectedDate.getNonConvertedDate());
        System.out.println("MonthYear = " + monthYear);
        EdsTask task = taskManager.getProjectTaskByName(projectId, "DEFAULT_TASK");
        boolean updateTask = false;
        if (task == null) {
            createDefaultTask(projectId, "DEFAULT_TASK");
            task = taskManager.getProjectTaskByName(projectId, "DEFAULT_TASK");
        }
        List<IdTime> ids = new ArrayList<>();
        for (MonthlyTimesheetItem item : items) {
            monthlyTimesheetManager.deleteByProjectIDandMonth(projectId, item.getProjectEmployeeID(), monthYear);
            EdsMonthlyTimesheet monthlyTimesheet = new EdsMonthlyTimesheet();
            if ((item.getWorkedHours() != null && item.getWorkedHours() > 0) || item.getTotalWorkedDays() != null) {
                TimesheetDataItem tdi = new TimesheetDataItem();
                tdi.setDateNonConvertable(selectedDate);
                Double minutes = item.getWorkedHours() != null ? item.getWorkedHours() * 60 : 0;
                tdi.setMinutes(minutes.intValue());
                EdsEmployeeTask eet = employeeTaskManager.getEmployeeTaskByProjectEmployee(task.getObjectID(), item.getProjectEmployeeID());
                if (eet == null) {
                    EdsReference reference = referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
                    task = taskManager.getProjectTaskByName(projectId, "DEFAULT_TASK");
                    eet = createNewEmployeeTask(task, minutes.intValue(), item.getProjectEmployeeID(), reference);
                    IdTime idTime = new IdTime();
                    idTime.setId(item.getProjectEmployeeID());
                    idTime.setActualTime(minutes.intValue());
                    ids.add(idTime);
                    updateTask = true;
                }
                tdi.setEmployeeTaskID(eet.getObjectID());
            }
            monthlyTimesheet.setTotalDaysWorked(item.getTotalWorkedDays());
            monthlyTimesheet.setOvertime(item.getOvertimeHours());
            monthlyTimesheet.setHolidayOvertime(item.getHolidayOvertimeHours());
            monthlyTimesheet.setWeekendOvertime(item.getWeekendOvertimeHours());
            monthlyTimesheet.setProjectEmployee(projectEmployeeManager.get(item.getProjectEmployeeID()));
            monthlyTimesheet.setMonthYear(monthYear);
            monthlyTimesheet.setWorkedHours(item.getWorkedHours());
            monthlyTimesheetManager.create(monthlyTimesheet);
            monthlyTimesheetManager.flushAndClear();
        }
        if (updateTask) {
            taskService.updateTaskAssignees(task.getObjectID(), ids.toArray(new IdTime[]{}));
            try {
                taskSolrComponent.index(taskManager.get(task.getObjectID()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void importMonthlyTimesheetData(Integer importingFileID, DateNonConvertable selectedDate, Integer projectId) {
        char defaultSeparator = ',';
        EdsAttachment attachment = attachmentManager.get(importingFileID);
        InputStream inputStream = uploadManager.getInputStream(attachment);
        InputStreamReader isr = new InputStreamReader(inputStream);
        CSVReader reader = new CSVReader(isr, defaultSeparator);

        try {
            List<String[]> listOfRows = reader.readAll();
            ArrayList<MonthlyTimesheetItem> items = geteMonthlyTimesheetItemsFromCSV(listOfRows, projectId, selectedDate);
            System.out.println("Timesheet Count: " + items.size());

            if (!items.isEmpty()) {
                saveMonthlyTimesheetData(items, selectedDate, projectId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<MonthlyTimesheetItem> geteMonthlyTimesheetItemsFromCSV(List<String[]> listOfRows, Integer projectId, DateNonConvertable date) {
        ArrayList<MonthlyTimesheetItem> items = new ArrayList<>();
        EdsProject project = projectManager.get(projectId);

        //HashMap<Integer, Integer[]> employeeContractedHoursMap = getEmployeeContractedHours(date, projectId, null);
        HashMap<Integer, Integer[]> employeeContractedHoursMap = null;

        boolean hasHeader = true;
        for (String[] row : listOfRows) {

            int column = 0;
            if (!hasHeader) {
                String employeeID = row[column++];

                if (employeeID != null && !employeeID.trim().isEmpty()) {
                    employeeID = employeeID.trim();

                    EdsEmployee employee = employeeManager.getEmployeeByNumber(employeeID);
                    if (employee == null) {
                        continue;
                    }

                    String contractStart = row[column++];
                    EdsProjectEmployee pe = null;

                    if (contractStart != null && !contractStart.isEmpty()) {
                        DateFormat dateFormat = new SimpleDateFormat(Constants.SHORT_DATE_FORMAT_2);
                        Date dateContranctStart = null;

                        if (contractStart.matches("(\\d\\d?)/(\\d\\d?)/(\\d\\d\\d\\d)")) {
                            try {
                                dateContranctStart = dateFormat.parse(contractStart);
                                pe = projectEmployeeManager.getProjectEmployee(employee, project, dateContranctStart);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (pe == null) {
                        pe = projectEmployeeManager.getProjectEmployee(employee, project, date.getNonConvertedDate());
                    }

                    if (pe == null) {
                        continue;
                    }

                    MonthlyTimesheetItem item = new MonthlyTimesheetItem();
                    item.setProjectEmployeeID(pe.getObjectID());

                    Double workingDays = 0d;
                    try {
                        workingDays = Double.valueOf(row[column++].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Double workingHours = 0d;
                    try {
                        workingHours = Double.valueOf(row[column++].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (employeeContractedHoursMap == null || employeeContractedHoursMap.get(item.getProjectEmployeeID()) == null) {
                        employeeContractedHoursMap = getEmployeeContractedHours(date, projectId, employee.getObjectID());
                    }

                    if (employeeContractedHoursMap.get(item.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(item.getProjectEmployeeID())[1]).compareTo(workingDays) < 0) {
                        workingDays = Double.valueOf(0);
                    }
                    if (employeeContractedHoursMap.get(item.getProjectEmployeeID()) != null && Double.valueOf(employeeContractedHoursMap.get(item.getProjectEmployeeID())[0]).compareTo(workingHours) < 0) {
                        workingHours = Double.valueOf(0);
                    }

//                    if (workingDays.intValue() > 0 || workingHours.intValue() > 0) {
                    item.setWorkedHours(workingHours);
                    item.setTotalWorkedDays(workingDays);
                    items.add(item);

                    Double overtimeHours = 0d;
                    try {
                        overtimeHours = Double.valueOf(row[column++].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    item.setOvertimeHours(overtimeHours);

                    Double weekendOvertimeHours = 0d;
                    try {
                        weekendOvertimeHours = Double.valueOf(row[column++].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    item.setWeekendOvertimeHours(weekendOvertimeHours);

                    Double holidayOvertimeHours = 0d;
                    try {
                        holidayOvertimeHours = Double.valueOf(row[column++].trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    item.setHolidayOvertimeHours(holidayOvertimeHours);

//                    }

                }

            } else {
                hasHeader = false;
            }
        }

        return items;
    }

    @Override
    public HashMap<Integer, Integer[]> getEmployeeContractedHours(DateNonConvertable dateNonConvertable, Integer projectId, Integer employeeId) {
        HashMap<Integer, Integer[]> result = new HashMap<>();

        if (projectId == null)
            return result;

        Date monthStartDate = dateNonConvertable.getNonConvertedDate();
        Date monthEndDate = ServerUtils.getMonthEndDate(monthStartDate);
        int monthDayCount = ServerUtils.getDayCount(monthStartDate, monthEndDate) + 1;

        ProjectMember[] projectMembers = employeeService.getProjectMembers(projectId, employeeId);

        for (ProjectMember projectMember : projectMembers) {
            Date startDate = null, endDate = null;

            EdsEmployee employee = employeeManager.get(projectMember.getId());

            if (projectMember.getContractEnd() == null) {
                projectMember.setContractEnd(monthEndDate != null ? new DateNonConvertable(monthEndDate) : null);
            }

            if (projectMember.getContractEnd() != null && projectMember.getContractStart() != null) {

                if (monthStartDate.compareTo(projectMember.getContractStart().getNonConvertedDate()) <= 0 && monthEndDate.compareTo(projectMember.getContractStart().getNonConvertedDate()) >= 0) {
                    startDate = projectMember.getContractStart().getNonConvertedDate();
                } else if (projectMember.getContractStart().getNonConvertedDate().compareTo(monthStartDate) <= 0 && monthStartDate.compareTo(projectMember.getContractEnd().getNonConvertedDate()) < 0) {
                    startDate = monthStartDate;
                }

                if (projectMember.getContractEnd().getNonConvertedDate().compareTo(monthStartDate) > 0 && projectMember.getContractEnd().getNonConvertedDate().compareTo(monthEndDate) <= 0) {
                    endDate = projectMember.getContractEnd().getNonConvertedDate();
                } else if (monthEndDate.compareTo(projectMember.getContractEnd().getNonConvertedDate()) <= 0 && monthEndDate.compareTo(projectMember.getContractStart().getNonConvertedDate()) >= 0) {
                    endDate = monthEndDate;
                }

                if (startDate == null || endDate == null) {
                    result.put(projectMember.getProjectEmployeeId(), new Integer[]{0, 0});
                    continue;
                }
            } else {
                startDate = monthStartDate;
                endDate = monthEndDate;
            }

            boolean isEnableSwitchOffValidation = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SWITCH_OFF_WORKED_DAYS_VALIDATION);

            if (isEnableSwitchOffValidation) {

                int dayCount = ServerUtils.getDayCount(startDate, endDate) + 1;
                int time = dayCount * (24 * 60);

                List<EdsMonthlyTimesheet> employeeOtherProjectEntiries = monthlyTimesheetManager.getEmployeeOtherProjectTimeEntiries(employee.getObjectID(), monthlyTimesheetManager.getMonthYear(startDate), projectId);
                if (employeeOtherProjectEntiries != null && !employeeOtherProjectEntiries.isEmpty()) {
                    int usedDayCount = 0;
                    for (EdsMonthlyTimesheet mt : employeeOtherProjectEntiries) {

                        if (mt.getTotalDaysWorked() != null) {
                            usedDayCount += mt.getTotalDaysWorked();
                        }

                    }

                    if (monthDayCount < (dayCount + usedDayCount)) {
                        dayCount += monthDayCount - (dayCount + usedDayCount);
                    }
                }
                result.put(projectMember.getProjectEmployeeId(), new Integer[]{time, dayCount});
            } else {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setStartDate(startDate);
                fp.setEndDate(endDate);
                fp.setEmployeeId(projectMember.getId());

                List<Date> dateList = attendanceRawDataManager.getWorkingDays(fp);

                if (dateList != null && !dateList.isEmpty()) {
                    Integer totalTime = 0;
                    for (Date date : dateList) {
                        EdsAttendanceRawData rowData = attendanceRawDataManager.getAttendanceRawDataByDate(date, projectMember.getId());
                        totalTime += (rowData.getTimeSlot() - rowData.getLeave());
                    }
                    result.put(projectMember.getProjectEmployeeId(), new Integer[]{totalTime, dateList.size()});
                } else {
                    result.put(projectMember.getProjectEmployeeId(), new Integer[]{0, 0});
                }
            }
        }

        return result;
    }

    private void createDefaultTask(Integer projectId, String defaultTask) {
        try {
            TaskSingleItem newTask = new TaskSingleItem();
            EdsProject project = projectManager.get(projectId);
            newTask.setProjectID(projectId);
            newTask.setName(defaultTask);
            newTask.setStartDate(project.getStartDate());
            newTask.setDueDate(project.getDueDate());
            newTask.setNumberData(new NumberData("D", 1));
            newTask.setBillable(true);
            newTask.setAllDay(true);
            newTask.setInstancesCount(1);

            taskService.saveTask(newTask);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private EdsEmployeeTask createNewEmployeeTask(EdsTask task, Integer timeSpent, Integer projectEmployeeID, EdsReference status) {
        EdsEmployeeTask eet = new EdsEmployeeTask();
        eet.setTask(taskManager.get(task.getObjectID()));
        eet.setProjectEmployee(projectEmployeeManager.get(projectEmployeeID));
        eet.setStartDate(task.getStartDate());
        eet.setStatus(status);
        eet.setTimeSpent(timeSpent);
        employeeTaskManager.create(eet);
        task.getAssignments().add(eet);
        return eet;
    }

    private FastTaskTransfer[] sortTimesheetTasks(Map<Integer, FastTaskTransfer> taskTransferMap) {
        FastTaskTransfer[] transferTasks = taskTransferMap.values().toArray(new FastTaskTransfer[0]);
        EdsNumberingSettings edsSettings = numberingSettingsManager.getNumberingSetting();
        if (!ServerSecurityContext.getInstance().getCompanyId().equals("8687") && !ServerSecurityContext.getInstance().getCompanyId().equals("25608") && !ServerSecurityContext.getInstance().getCompanyId().equals("3465")) {
            if (edsSettings != null && edsSettings.getSortTimesheetByTaskName()) {
                //sort by task name
                Arrays.sort(transferTasks, (o1, o2) -> {
                    if (o1.getProjectId() > o2.getProjectId()) {
                        return 1;
                    } else if (o1.getProjectId() < o2.getProjectId()) {
                        return -1;
                    } else {//if projects are the same then sort by task name
                        return o1.getEmplTaskName().compareToIgnoreCase(o2.getEmplTaskName());
                    }

                });
            } else {//sort by task ID
                Arrays.sort(transferTasks, (o1, o2) -> {
                    if (o1.getProjectId() > o2.getProjectId()) {
                        return 1;
                    } else if (o1.getProjectId() < o2.getProjectId()) {
                        return -1;
                    } else {//if projects are the same then sort by task IDs
                        return o1.getTaskId().compareTo(o2.getTaskId());
                    }
                });

            }
        } else {//this is for Robert's companies, sort by task names
            Arrays.sort(transferTasks, (o1, o2) -> {
                if (o1.getEmplTaskName().compareTo(o2.getEmplTaskName()) > 0) {
                    return 1;
                } else if (o2.getEmplTaskName().compareTo(o1.getEmplTaskName()) > 0) {
                    return -1;
                } else {
                    return 0;
                }
            });
        }
        return transferTasks;
    }

    private void fillTimesheetValue(DateNonConvertable[] dates, FastTaskTransfer transferTask, ArrayList<TimesheetDataItem> timesheetDataItems, EdsAttendanceRawData[] attendanceRawDatas, SelectItem[] hourTypes, int columnCount) {
        transferTask.setDataItems(new TimesheetDataItem[columnCount]);
        Map<Date, TimesheetDataItem> timesheetMap = new HashMap<>();
        if (timesheetDataItems != null) {
            for (TimesheetDataItem timeSheetDataItem : timesheetDataItems) {
                timesheetMap.put(timeSheetDataItem.getDate(), timeSheetDataItem);
            }
        }
        int k = 0;
        Date currentDate = new Date();
        for (DateNonConvertable date : dates) {
            TimesheetDataItem timeSheetDataItem;
            if (timesheetMap.get(date.getNonConvertedDate()) == null) {
                timeSheetDataItem = new TimesheetDataItem();
                timeSheetDataItem.setEmployeeTaskID(transferTask.getEmplTaskId());
                timeSheetDataItem.setTaskID(transferTask.getTaskId());
                timeSheetDataItem.setMinutes(0);
                timeSheetDataItem.setComment("");
                timeSheetDataItem.setHourTypeID(0);
                timeSheetDataItem.setHourTypes(hourTypes);
                timeSheetDataItem.setDate(date.getNonConvertedDate());
                timeSheetDataItem.setDateNonConvertable(date);
                timeSheetDataItem.setCurrentServerDate(currentDate);
                timeSheetDataItem.setStatus(TIMESHEET_ENTRY_NOTSUBMITTED);
                timeSheetDataItem.setTaskStart(transferTask.getTaskStartDate());
                timeSheetDataItem.setTaskEnd(transferTask.getTaskEndDate());
                if (attendanceRawDatas[k] != null) {
                    timeSheetDataItem.setTimeslotMinutes(attendanceRawDatas[k].getTimeSlot());
                    timeSheetDataItem.setTimesheetMinutes(attendanceRawDatas[k].getTimeSheet() + attendanceRawDatas[k].getTimeSheetPending());
                    timeSheetDataItem.setLeaveRequestMinutes(attendanceRawDatas[k].getLeave());
                    timeSheetDataItem.setHoliday(attendanceRawDatas[k].getHoliday());
                    timeSheetDataItem.setDayOff(attendanceRawDatas[k].getDayOff());
                }
                timeSheetDataItem.setProjectID(transferTask.getProjectId());
            } else {
                timeSheetDataItem = timesheetMap.get(date.getNonConvertedDate());
                timeSheetDataItem.setCurrentServerDate(currentDate);
                timeSheetDataItem.setTaskStart(transferTask.getTaskStartDate());
                timeSheetDataItem.setTaskEnd(transferTask.getTaskEndDate());
                if (attendanceRawDatas[k] != null) {
                    timeSheetDataItem.setTimeslotMinutes(attendanceRawDatas[k].getTimeSlot());
                    timeSheetDataItem.setTimesheetMinutes(attendanceRawDatas[k].getTimeSheet() + attendanceRawDatas[k].getTimeSheetPending());
                    timeSheetDataItem.setLeaveRequestMinutes(attendanceRawDatas[k].getLeave());
                    timeSheetDataItem.setHoliday(attendanceRawDatas[k].getHoliday());
                    timeSheetDataItem.setDayOff(attendanceRawDatas[k].getDayOff());
                }
                timeSheetDataItem.setProjectID(transferTask.getProjectId());
            }
            transferTask.getDataItems()[k] = timeSheetDataItem;
            timeSheetDataItem.setTaskTransfer(transferTask);
            k++;
        }
    }

    //ATTENDANCERAWDATA >> TIMESHEET CREATE/DELETE
    private void saveAttendanceRawDataTimesheet(EdsTimeSheet timesheet, int difference, boolean wasApprovedNowRejecting, boolean wasEnteredNowAutoApproving, Integer rejectRefID) {
        //create attendance raw data for the current year and employee, if there is none
        Calendar from = Calendar.getInstance();
        from.setTime(timesheet.getDate());
        from.set(Calendar.AM_PM, 0);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(from.getTime(), timesheet.getEmployeeID());
        EdsReference waitingForApproval = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_WAITING");
        if (attendanceRawData != null && (timesheet.getStatus() == null || timesheet.getStatus().equals(waitingForApproval))) {
            attendanceRawData.setTimeSheetPending(attendanceRawData.getTimeSheetPending() + timesheet.getTimeSpent() - (timesheet.getTimeSpent() - difference));
        } else if (timesheet.getStatus() != null && !timesheet.getStatus().getObjectID().equals(rejectRefID) && attendanceRawData != null) {
            if (!wasEnteredNowAutoApproving) {
                attendanceRawData.setTimeSheet(attendanceRawData.getTimeSheet() + timesheet.getTimeSpent() - (timesheet.getTimeSpent() - difference));
                if (!timesheet.getAutoApproved()) {
                    attendanceRawData.setTimeSheetPending(attendanceRawData.getTimeSheetPending() - (timesheet.getTimeSpent() - (timesheet.getTimeSpent() - difference)));
                }
            } else {
                attendanceRawData.setTimeSheet(attendanceRawData.getTimeSheet() + timesheet.getTimeSpent());
                attendanceRawData.setTimeSheetPending(attendanceRawData.getTimeSheetPending() - (timesheet.getTimeSpent() - difference));
            }
        } else if (wasApprovedNowRejecting && attendanceRawData != null) {
            attendanceRawData.setTimeSheet(attendanceRawData.getTimeSheet() - timesheet.getTimeSpent());
            attendanceRawData.setTimeSheetPending(attendanceRawData.getTimeSheetPending() + timesheet.getTimeSpent());
        }
    }

    private void saveAttendanceRawDataTimesheet(EdsTimeSheet timesheet, int difference, boolean wasApprovedNowRejecting, boolean wasEnteredNowAutoApproving) {
        availabilityService.createAttendaceRawDataRecords(timesheet.getEmployeeID(), 0);
        saveAttendanceRawDataTimesheet(timesheet, difference, wasApprovedNowRejecting, wasEnteredNowAutoApproving, referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT").getObjectID());
    }

    public void updateAttendanceForDeletedTask(EdsTask task) {
        Calendar from = Calendar.getInstance();
        from.set(Calendar.AM_PM, 0);
        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);
        from.set(Calendar.SECOND, 0);
        from.set(Calendar.MILLISECOND, 0);
        for (EdsEmployeeTask employeeTask : task.getAssignments()) {
            List<EdsTimeSheet> timeSheets = timesheetManager.getTimeSheetByTaskID(task.getObjectID(), employeeTask.getObjectID());
            for (EdsTimeSheet timeSheet : timeSheets) {
                from.setTime(timeSheet.getDate());
                EdsAttendanceRawData attendanceRawData = attendanceRawDataManager.getAttendanceRawDataByDate(from.getTime(), timeSheet.getEmployeeID());
                if (attendanceRawData != null) {
                    if (timeSheet.getStatus() != null && EdsTimeSheet._APPROVE.equals(timeSheet.getStatus().getCode())) {
                        attendanceRawData.setTimeSheet(attendanceRawData.getTimeSheet() - timeSheet.getTimeSpent());
                    } else {
                        attendanceRawData.setTimeSheetPending(attendanceRawData.getTimeSheetPending() - timeSheet.getTimeSpent());
                    }
                }
            }
        }
    }

    @Override
    public void fillTimesheetFromResUtil(Integer companyId) {
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        //company employees
        Date currentDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(currentDate);
        ServerUtils.setBeginningOfTheDay(calendar);

        List<EdsTimeSheet> timeSheets = timesheetManager.getNotFilledTimesheetForToday(calendar.getTime());
        for (EdsTimeSheet timeSheet : timeSheets) {
            TimesheetDataItem timesheetItem = new TimesheetDataItem();
            timesheetItem.setDifference(timeSheet.getDailyEstimatedTime());
            timesheetItem.setEmployeeTaskID(timeSheet.getEmployeeTask().getObjectID());
            timesheetItem.setDate(timeSheet.getDate());
            timesheetItem.setMinutes(timeSheet.getDailyEstimatedTime());
            timesheetItem.setComment(timeSheet.getEmployeeTask().getTask().getName());
            timesheetItem.setEmployeeID(timeSheet.getEmployeeID());

            applyUpdates(timesheetItem, null);
        }
    }

    @Override
    public void updateTimesheetDate(Integer objectID, DateNonConvertable changedDate) {
        EdsTimeSheet timeSheet = timesheetManager.get(objectID);

        if (timeSheet != null) {
            timeSheet.setDate(changedDate.getNonConvertedDate());
            timesheetManager.update(timeSheet);
        }
    }

    @Override
    public String getTimesheetComment(Integer taskId, Integer employeed, DateNonConvertable timesheetDate) {
        Date date = timesheetDate.getDate();
        date.setHours(00);
        date.setMinutes(00);
        date.setSeconds(00);
        EdsTimeSheet timeSheet = timesheetManager.getTimeSheet(employeed, taskId, date);
        if (timeSheet != null) {
            return timeSheet.getComment();
        } else {
            return "";
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimeSheetReportTO> getDailyTimesheets(Integer companyId) {
        if (companyId == null) {
            return Collections.emptyList();
        }
        Date currentDate = new Date();

        Date startDate = ServerUtils.getDayStartTime(currentDate);
        Date endDate = ServerUtils.getDayEndTime(currentDate);

        GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
        String databse = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId);
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        ServerSecurityContext.getInstance().setDatabase(databse);

        List<EdsTimeSheet> edsTimeSheets = timesheetManager.getDailyTimesheets(startDate, endDate);
        Map<Integer, TimeSheetReportTO> map = Maps.newHashMap();
        for (EdsTimeSheet edsTimeSheet : edsTimeSheets) {
            if (edsTimeSheet.getEmployeeTask().getProjectEmployee() == null ||
                    edsTimeSheet.getEmployeeTask().getProjectEmployee().getEmployeeDepartment() == null ||
                    edsTimeSheet.getEmployeeTask().getProjectEmployee().getEmployeeDepartment().getEmployee() == null) {
                continue;
            }
            EdsEmployee edsEmployee = edsTimeSheet.getEmployeeTask().getProjectEmployee().getEmployeeDepartment().getEmployee();
            if (map.get(edsEmployee.getObjectID()) == null) {
                TimeSheetReportTO to = new TimeSheetReportTO();
                to.setEmloyeeName(edsEmployee.getFullName());
                to.setTotalHours(edsTimeSheet.getTimeSpent());
                to.getItems().add(this.getReportItemTO(edsTimeSheet));
                map.put(edsEmployee.getObjectID(), to);
            } else {
                TimeSheetReportTO to = map.get(edsEmployee.getObjectID());
                to.addTotalHours(edsTimeSheet.getTimeSpent());
                to.getItems().add(this.getReportItemTO(edsTimeSheet));
            }
        }
        List<EdsSickRequest> edsSickRequests = sickRequestManager.getDailyLeaveRequests(startDate, endDate);
        for (EdsSickRequest edsSickRequest : edsSickRequests) {
            if (edsSickRequest.getEmployee() == null) {
                continue;
            }
            EdsEmployee edsEmployee = edsSickRequest.getEmployee();
            TimeSheetReportTO to = new TimeSheetReportTO();
            to.setLeaveRequest(true);
            to.setEmloyeeName(edsEmployee.getFullName());
            to.setLeaveRequestPeriod(ServerUtils.shortDateFormat(edsSickRequest.getStartDate(), edsEmployee) + " - " +
                    ServerUtils.shortDateFormat(edsSickRequest.getEndDate(), edsEmployee));
            map.put(edsEmployee.getObjectID(), to);
        }
        ServerSecurityContext.getInstance().setDatabase("");
        SecurityContext.removeCompanyID();

        return Lists.newArrayList(map.values());
    }

    private TimeSheetReportItemTO getReportItemTO(EdsTimeSheet edsTimeSheet) {
        TimeSheetReportItemTO item = new TimeSheetReportItemTO();
        if (edsTimeSheet.getEmployeeTask().getTask() != null) {
            item.setTaskName(edsTimeSheet.getEmployeeTask().getTask().getName());
            item.setTaskNumber(edsTimeSheet.getEmployeeTask().getTask().getNumber());
        }
        item.setTimeSpent(edsTimeSheet.getTimeSpent());
        return item;
    }

    @Override
    public void submitTimesheetAutomaticForApproval(DateNonConvertable fromDate, Integer weekOffSet, LinkedHashMap<String, String> projectTasks, Integer employeeId) {
        DateNonConvertable[] dates = getTimesheetWeeklyDates(fromDate, weekOffSet);
        DateNonConvertable minDate = dates[0];
        DateNonConvertable maxDate = dates[0];
        for (DateNonConvertable date : dates) {
            if (date.getDate().before(minDate.getDate())) {
                minDate = date;
            } else if (date.getDate().after(maxDate.getDate())) {
                maxDate = date;
            }
        }
        TimeSheetEntriesPerPeriod timeSheetEntriesPerPeriod = getEntries(minDate, maxDate, null, projectTasks);
        final TimeSheetEntriesPerPeriod entriesPerPeriod = new TimeSheetEntriesPerPeriod();
        entriesPerPeriod.setFromDate(dates[0]);
        entriesPerPeriod.setToDate(dates[1]);
        entriesPerPeriod.setEntries(timeSheetEntriesPerPeriod.getEntries());
        entriesPerPeriod.setEmployeeID(employeeId);
        submitTimesheetForApproval(entriesPerPeriod);
    }

    @Override
    public ArrayList<SuggestionResponseDTO> getTimesheetCommentSuggestion(Integer userId, Integer taskId) {
        // Fetch past timesheet entries for the task and user
        List<TaskTimeSheetSuggestItem> pastEntries = timesheetManager.getTimeSheetsForSuggest(taskId, userId);
        String prompt = "";

        // Determine the prompt based on whether there are past entries
        if (!pastEntries.isEmpty()) {
            // Recurring task: suggest three options
            prompt = buildRecurringTaskPrompt(pastEntries, taskId);
        } else {
            // New task: suggest one option
            EdsTask task = taskManager.get(taskId);
            if (task == null) {
                return new ArrayList<>(); // Return empty list if task not found
            }
            prompt = buildNewTaskPrompt(task.getNumber(), task.getName(), task.getStartDate(), task.getDueDate(), task.getDescription());
        }

        // Call OpenAI API and handle response
        try {
            String response = callOpenAiApi(prompt);
            if (response == null) {
                return new ArrayList<>();
            }
            return parseSuggestion(response);
        } catch (Exception e) {
            log.error("Error getting timesheet suggestion: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private String buildRecurringTaskPrompt(List<TaskTimeSheetSuggestItem> entries, Integer taskId) {
        String taskName = entries.get(0).getName();
        String taskDescription = entries.get(0).getDescription();
        StringBuilder sb = new StringBuilder();
        sb.append("For the recurring task '").append(taskId).append(": ").append(taskName)
                .append("', with description: '").append(taskDescription)
                .append("', here are the past timesheet entries:\n");
        for (TaskTimeSheetSuggestItem entry : entries) {
            sb.append("- Comment: '").append(entry.getComment())
                    .append("', Time Spent: ").append(entry.getTimeSpent())
                    .append(" minutes, Entry Date: '").append(entry.getEntryDate())
                    .append("'\n");
        }
        sb.append("Based on these past entries, please suggest three possible descriptions and corresponding times (in minutes) for the next timesheet entry. ")
                .append("Each description must be at least 50 tokens long and consistent with past activities. ")
                .append("Provide the output in JSON format as an array of three objects, each with 'description' and 'time' keys, like this: ")
                .append("[{\"description\": \"[Desc 1]\", \"time\": [Time 1]}, {\"description\": \"[Desc 2]\", \"time\": [Time 2]}, {\"description\": \"[Desc 3]\", \"time\": [Time 3]}]");
        return sb.toString();
    }

    private String buildNewTaskPrompt(String number, String name, Date startDate, Date dueDate, String description) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("For the new task '").append(number).append(" - ").append(name)
                .append("', with description: '").append(description)
                .append("', start date: '").append(startDate)
                .append("', and due date: '").append(dueDate)
                .append("', please suggest three possible timesheet descriptions. ")
                .append("Each description must be at least 50 tokens long and relevant to the task details. ")
                .append("Provide the output in JSON format as an array of three objects, each containing a 'description' field, like this: ")
                .append("[{\"description\": \"[Desc 1]\"}, {\"description\": \"[Desc 2]\"}, {\"description\": \"[Desc 3]\"}]");
        return prompt.toString();
    }

    private String callOpenAiApi(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Build request body for OpenAI chat completions
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");
        requestBody.put("max_tokens", 300);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "You are an assistant that generates concise timesheet comments."));
        messages.add(Map.of("role", "user", "content", prompt));
        requestBody.put("messages", messages);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // Make API call with error handling
        try {
            log.info("Calling OpenAI API");
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", entity, String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("OpenAI API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return null;
        } catch (Exception e) {
            log.error("Error calling OpenAI API: {}", e.getMessage(), e);
            return null;
        }
    }

    private ArrayList<SuggestionResponseDTO> parseSuggestion(String apiResponse) {
        JSONParser parser = new JSONParser();
        try {
            // Parse the OpenAI response
            JSONObject jsonResponse = (JSONObject) parser.parse(apiResponse);
            JSONArray choices = (JSONArray) jsonResponse.get("choices");
            JSONObject firstChoice = (JSONObject) choices.get(0);
            JSONObject message = (JSONObject) firstChoice.get("message");
            String suggestionText = (String) message.get("content");

            // Parse the suggestion text as a JSON array
            suggestionText = suggestionText.replace("```json", "").replace("`", "").trim();
            JSONArray suggestionsArray = (JSONArray) parser.parse(suggestionText);
            ArrayList<SuggestionResponseDTO> suggestions = new ArrayList<>();

            for (Object obj : suggestionsArray) {
                JSONObject suggestionJson = (JSONObject) obj;
                String description = (String) suggestionJson.get("description");
                Integer time = suggestionJson.containsKey("time") ? ((Number) suggestionJson.get("time")).intValue() : null;
                suggestions.add(new SuggestionResponseDTO(description, time));
            }

            return suggestions;
        } catch (Exception e) {
            throw new RuntimeException("Error processing suggestion: " + e.getMessage(), e);
        }
    }



}
