package com.edatasite.workforce.rest.v2.release10.pm;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceColor;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomFieldLookUpTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ModelFieldManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.TaskReminderManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.impl.MessageManagerImpl;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.note.server.NoteServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.server.app.TimesheetServiceLocal;
import com.edatasite.workforce.rest.aspects.CheckPermission;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.ModelFieldLocalizer;
import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.EntityCategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.PagingListResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.customfield.CustomFieldFileUploadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.link.LinkTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.FilteredStatusItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.LinksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceRepeatsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.RecurrenceUntilTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.ShareWithDepartmentsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.activity.TimeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.payroll.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.ChangeTaskStatusSalesTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.ChangeTaskStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.RelatedTaskTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.StatusList;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskAssigneeTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskBaseInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailsInfoResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailsItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskInformationTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskLinksTO;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.TaskSearchItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.ColorTO;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.timesheet.TimesheetAddTO;
import com.edatasite.workforce.rest.v2.release10.enums.CustomFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityFieldTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.EntityTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderByEnum;
import com.edatasite.workforce.rest.v2.release10.enums.OrderFieldEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaskPriorityEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_DAILY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_MONTHLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_WEEKLY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_YEARLY;


/**
 * Created by Dilshod Madrahimov on 11/28/2017.
 */

@Tag(name = "Task", description = "Task API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
@Transactional
public class ApiTaskControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiTaskControllerV2.class);
    @Autowired
    private TimesheetServiceLocal timesheetServiceLocal;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    private TaskReminderManager taskReminderManager;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private ModelFieldManager modelFieldManager;
    @Autowired
    private ModelFieldLocalizer modelFieldLocalizer;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private NoteServiceLocal noteServiceLocal;
    @Autowired
    private RelationManager relationManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private CompanyCustomFieldsManager companyCFManager;
    @Autowired
    @Qualifier("taskViewPDFHandler")
    private IPostPDFHandler taskViewPDFHandler;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private UploadManager uploadManager;

    @Operation(summary = "Get Task statuses list", description = "Request a list of statuses for a specific task, so that the user can then change the status of the task.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of task statuses")})
    @RequestMapping(value = "/task/statuses", method = RequestMethod.GET)
    public Object getTaskStatusList(@RequestParam(value = "task_id") Integer task_id) throws RestException {

        if (task_id == null || task_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "task_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        SelectItem[] statusItems;
        try {
            statusItems = taskServiceLocal.getEditTaskStatusDrop(task_id);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<CategoryTO> result = new ArrayList<>();
        for (SelectItem item : statusItems) {
            result.add(new CategoryTO(item.getId(), item.getName()));
        }
        return successResponse(new StatusList(result));
    }

    @Operation(summary = "Log Work for task", description = "Request to add time spent on the task.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/task/log_work", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_TIMESHEET})
    public Object addTimesheet(@RequestBody TimesheetAddTO timesheetAdd) throws RestException {

        if (timesheetAdd.getTask_id() == null || timesheetAdd.getTask_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "task_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsTask task = taskManager.get(timesheetAdd.getTask_id());
        if (task == null || task.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task with task_id " + timesheetAdd.getTask_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        TimesheetDataItem item = new TimesheetDataItem();
        item.setDate(new Date());//TODO date field didn't mentioned, need ask from Stepan. We need date.I'm using current date ATM
        item.setTaskID(timesheetAdd.getTask_id());
        item.setEmployeeID(timesheetAdd.getEmployee_id() != null ? timesheetAdd.getEmployee_id() : 0);

        Integer hours = timesheetAdd.getHours() != null ? timesheetAdd.getHours() : 0;
        Integer minutes = timesheetAdd.getMinutes() != null ? timesheetAdd.getMinutes() : 0;

        item.setMinutes(hours * 60 + minutes);
        item.setComment(timesheetAdd.getComment());

        EdsEmployee edsEmployee = null;
        if (timesheetAdd.getEmployee_id() != null) {
            edsEmployee = employeeManager.get(timesheetAdd.getEmployee_id());
        }
        try {
            taskServiceLocal.setTimeToTimesheet(item);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (timesheetAdd.getCompleted() != null && edsEmployee != null && edsEmployee.hasRoles(Constants.ADMIN)) {
            try {
                EdsEmployeeTask employeeTask = employeeTaskManager.getEmployeeTask(task.getObjectID(), userManager.getUser().getEmployee().getObjectID(), false);
                if (employeeTask != null) {
                    timesheetServiceLocal.updatePercentCompleted(employeeTask.getObjectID(), timesheetAdd.getCompleted() <= 100f ? timesheetAdd.getCompleted() : 100f, true);
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Change task status", description = "A request to change the status for a task.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/task/change_status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object changeTaskStatus(@RequestBody ChangeTaskStatusTO changeTaskStatus) throws RestException {

        boolean taskEditPermission = ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_EDIT) && (ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_LIST) || ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_TASKS_LIST));
        if (!taskEditPermission) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (changeTaskStatus.getTask_id() == null || changeTaskStatus.getTask_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "task_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (changeTaskStatus.getStatus_id() == null || changeTaskStatus.getStatus_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "status_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsTask task = taskManager.get(changeTaskStatus.getTask_id());
        if (task == null || task.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task with task_id " + changeTaskStatus.getTask_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        EdsReference status = referenceManager.getReference(changeTaskStatus.getStatus_id());
        if (status == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task status with status_id " + changeTaskStatus.getStatus_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        TaskListItem taskListItem = new TaskListItem();
        taskListItem.setObjectID(task.getObjectID());
        taskListItem.setTaskStatusId(status.getObjectID());
        try {
            taskServiceLocal.saveTaskEditCellValue(taskListItem, TaskListItem.OVERALL_STATUS_NAME);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Change status of task", description = "Change the status/category task on the main screen when you drag the Task card to specific interaction areas (status change). If something went wrong, the server will give a specific error (err_code 3010), based on it, we can update the interface for example only those tasks that have been modified.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/tasks/change_status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object changeTaskStatusSales(@RequestBody ChangeTaskStatusSalesTO changeTaskStatus) throws RestException {

        boolean taskEditPermission = ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_EDIT) && (ServerUtils.hasPermission(PermissionConstants.CRM_TASKS_LIST) || ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_TASKS_LIST));
        if (!taskEditPermission) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (changeTaskStatus.getItem_id() == null || changeTaskStatus.getItem_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (changeTaskStatus.getStatus_id() == null || changeTaskStatus.getStatus_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "status_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsTask task = taskManager.get(changeTaskStatus.getItem_id());
        if (task == null || task.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task with task_id " + changeTaskStatus.getItem_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        EdsReference status = referenceManager.getReference(changeTaskStatus.getStatus_id());
        if (status == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task status with status_id " + changeTaskStatus.getStatus_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        TaskListItem taskListItem = new TaskListItem();
        taskListItem.setObjectID(task.getObjectID());
        taskListItem.setTaskStatusId(status.getObjectID());
        try {
            taskServiceLocal.saveTaskEditCellValue(taskListItem, TaskListItem.OVERALL_STATUS_NAME);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), ERROR_LEAD_MODIFY, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Task details", description = "Request for details on a particular application. The priority field is the priority of the task. It can have the following values: HIGH - high priority, MEDIUM - normal priority, LOW - low priority")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/task", method = RequestMethod.GET)
    public Object getTaskDetails(@RequestParam("task_id") Integer task_id) throws RestException {
        if (task_id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "task_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (task_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "task_id should be more then zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        TaskSingleItem taskItem;
        try {
            taskItem = taskServiceLocal.getTask(task_id, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (taskItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task Item has not been found with provided task_id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        TaskDetailsTO taskDetails = new TaskDetailsTO();
        taskDetails.setId(taskItem.getObjectID());
        taskDetails.setTitle(taskItem.getName());
        if (taskItem.getNumberData() != null) {
            taskDetails.setNumber(taskItem.getNumberData().getNumberString());
        }
        if (taskItem.getStatusName() != null) {
            taskDetails.setStatus(new CategoryTO(taskItem.getStatusID(), taskItem.getStatusName()));
        }
        if (StringUtils.isNotBlank(taskItem.getDescription())) {
            taskDetails.setDescription(taskItem.getDescription());
        }
        if (taskItem.getStartDate() != null) {
            taskDetails.setStart_date(longDateTimezoneFormat.format(taskItem.getStartDate()));
        }
        if (taskItem.getDueDate() != null) {
            taskDetails.setDue_date(longDateTimezoneFormat.format(taskItem.getDueDate()));
        }
        taskDetails.setPriority(TaskPriorityEnum.get(taskItem.getPriorityCode()));

        if (taskItem.getIssueEmployees() != null) {
            ArrayList<OwnerTO> assignees = new ArrayList<>();
            for (PositionsSelectItem positionsSelectItem : taskItem.getIssueEmployees()) {
                OwnerTO assignee = new OwnerTO();
                assignee.setId(positionsSelectItem.getExactEmployeeId());
                assignee.setName(positionsSelectItem.getName());
                assignee.setDepartment(positionsSelectItem.getDepartmentName());
                try {
                    assignee.setAvatar(hrmsServiceLocal.getEmployeeImageURL(positionsSelectItem.getExactEmployeeId()));
                } catch (Exception e) {
                    log.error("", e);
                }
                assignees.add(assignee);
            }
            taskDetails.setAssignees(assignees);
        }
        if (StringUtils.isNotBlank(taskItem.getProjectName())) {
            taskDetails.setProject(taskItem.getProjectName());
        }
        taskDetails.setBillable(taskItem.getBillable());
        taskDetails.setParent_workstream(taskItem.getWorkstreamName());
        ArrayList<CalendarEventReminder> reminders = taskReminderManager.getReminders(taskItem.getObjectID());
        if (reminders != null && reminders.size() > 0) {
            reminders.forEach(reminder -> {
                taskDetails.setRemind_by_email(reminder.getReminderTimes());//todo reminders are array in the system, but in mobile doc this is just a string, so api returns the last one
            });
        }

        RecurrenceJobItem recurrenceJobItem = taskItem.getRecurrenceJobItem();
        if (recurrenceJobItem != null && recurrenceJobItem.getType() != null) {
            if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_WEEKLY)) {
                int counter = 0;
                if (recurrenceJobItem.isMonday()) {
                    counter++;
                }
                if (recurrenceJobItem.isTuesday()) {
                    counter++;
                }
                if (recurrenceJobItem.isWednesday()) {
                    counter++;
                }
                if (recurrenceJobItem.isThursday()) {
                    counter++;
                }
                if (recurrenceJobItem.isFriday()) {
                    counter++;
                }
                if (recurrenceJobItem.isSaturday()) {
                    counter++;
                }
                if (recurrenceJobItem.isSunday()) {
                    counter++;
                }
                taskDetails.setRepeats(counter);
            } else {
                taskDetails.setRepeats(recurrenceJobItem.getInterval());
            }
            if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_DAILY)) {
                taskDetails.setRecurrence_type("DAILY");
            } else if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_WEEKLY)) {
                taskDetails.setRecurrence_type("WEEKLY");
            } else if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                taskDetails.setRecurrence_type("MONTHLY");
            } else if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_YEARLY)) {
                taskDetails.setRecurrence_type("YEARLY");
            }
            if (recurrenceJobItem.getEndDate() != null) {
                taskDetails.setEnd_date(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
            } else {
                taskDetails.setOccurence(recurrenceJobItem.getOccurrence());
            }
        }

        if (taskItem.getRelations() != null && taskItem.getRelations().size() > 0) {
            ArrayList<TaskLinksTO> taskLinkList = new ArrayList<>();
            taskItem.getRelations().forEach(relationItem -> {//todo need clarify link. Should we use the system generated link or mobile
                taskLinkList.add(new TaskLinksTO(relationItem.getToType(), relationItem.getToName()));
            });
            taskDetails.setLinks(taskLinkList);
        }
        List<FileResource> taskAttachments = attachmentUtilsManager.getAttachments(Constants.F_TASK, taskItem.getProjectID(), taskItem.getObjectID());
        if (taskAttachments != null && !taskAttachments.isEmpty()) {
            ArrayList<AttachmentTO> itemAttachments = new ArrayList<>();
            taskAttachments.forEach(fileItem -> itemAttachments.add(new AttachmentTO(fileItem.getFileName(), fileItem.getDownloadUrl())));
            taskDetails.setAttachments(itemAttachments);
        }

        ArrayList<CustomFieldsTO> customFields = getCustomFields(taskItem.getCustomFieldItems());
        if (customFields != null && customFields.size() > 0) {
            taskDetails.setCustom_fields(customFields);
        }
        return successResponse(taskDetails);
    }

    @Operation(summary = "Search Tasks", description = "")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have tasks based on search query"),
            @ApiResponse(responseCode = "400", description = "query is required")})
    @RequestMapping(value = "/tasks/search", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_TASKS_LIST})
    public Object searchTask(@RequestParam(value = "query") String query,
                             @RequestParam(value = "projectId", required = false) Integer projectId,
                             @RequestParam(value = "limit", required = false) Integer limit,
                             @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        PagingListResultTO<TaskSearchItemTO> taskListResult = new PagingListResultTO<>();

        if (StringUtils.isBlank(query)) {
            return successResponse(taskListResult);
        }

        query = query.replace("%20", " ").trim();

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : 100;

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(start);
        filterParameter.setLimit(maxLimit);
        filterParameter.setSearchKey(query);
        filterParameter.setDetectDuplicates(false);
        filterParameter.setSearchButton(true);
        filterParameter.setFromMobile(true);
        filterParameter.setProjectId(projectId);

        List<String> columnCodeNames = new ArrayList<>();
        columnCodeNames.add(TaskListItem.NAME);
        columnCodeNames.add(TaskListItem.NUMBER);
        columnCodeNames.add(TaskListItem.START_DATE);
        columnCodeNames.add(TaskListItem.END_DATE);
        columnCodeNames.add(TaskListItem.DUE_DATE);

        //Also Retrieve custom field values
        List<CompanyCustomFieldItem> customFieldsItems = commonService.getCompanyCustomFields(ViewName.Task);
        if (CollectionUtils.isNotEmpty(customFieldsItems)) {
            columnCodeNames.addAll(customFieldsItems.stream().map(CompanyCustomFieldItem::getColumnCode).toList());
            ListPanelToolRpc panelTools = new ListPanelToolRpc();
            panelTools.setColumnCodeName(new ArrayList<>(columnCodeNames));
            filterParameter.setListPanelTool(panelTools);
        }

        TaskList result;
        try {
            result = taskServiceLocal.getTaskList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        taskListResult.setTotal_count(result.getTotal());
        if (result.getTotal() < (maxLimit + start)) {
            taskListResult.setLeft(0);
        } else {
            taskListResult.setLeft(result.getTotal() - (start + maxLimit));
        }
        taskListResult.setCount(result.getList() != null ? result.getList().size() : 0);
        taskListResult.setOffset(start);

        ArrayList<TaskSearchItemTO> taskList = new ArrayList<>();
        for (TaskListItem taskListItem : result.getList()) {
            TaskSearchItemTO taskItem = new TaskSearchItemTO();
            taskItem.setName(taskListItem.getName());
            taskItem.setNumber(taskListItem.getNumber());
            taskItem.setItem_id(taskListItem.getObjectID());
            taskItem.setPriority(TaskPriorityEnum.get(taskListItem.getPriorityCode()));
            if (taskListItem.getDueDate() != null) {
                taskItem.setDue_date(longDateTimezoneFormat.format(taskListItem.getDueDate()));
            }
            if (taskListItem.getStartDate() != null) {
                taskItem.setStart_date(longDateTimezoneFormat.format(taskListItem.getStartDate()));
            }
            if (taskListItem.getEndDate() != null) {
                taskItem.setEnd_date(longDateTimezoneFormat.format(taskListItem.getEndDate()));
            }

            EdsReference taskStatus = referenceManager.get(taskListItem.getTaskStatusId());
            if (taskStatus != null) {
                taskItem.setStatus_id(taskStatus.getObjectID());
                FlowSettingsTO statusItem = new FlowSettingsTO();
                statusItem.setStatus_id(taskStatus.getObjectID());
                statusItem.setStatus_name(taskStatus.getName());
                statusItem.setOrder_id(taskStatus.getSorder());
                statusItem.setIs_system(taskStatus.isSystemReference());
                if (taskStatus.getReferenceColor() != null) {
                    ColorTO color = new ColorTO();
                    color.setId(taskStatus.getReferenceColor().getObjectID());
                    color.setName(taskStatus.getReferenceColor().getName());
                    color.setHex(taskStatus.getReferenceColor().getHex());
                    statusItem.setStatus_color(color);
                }
                taskItem.setStatus(statusItem);
            }

            //Task custom fields
            if (CollectionUtils.isNotEmpty(customFieldsItems)) {

                HashMap<Integer, Map<String, String>> cfLookupVals = new HashMap<>();
                customFieldsItems.forEach(cf -> {
                    if (Constants.TYPE_ENTITY_LOOKUP.equals(cf.getUiType())) {

                        Map<String, String> vals = Arrays.stream(companyCFManager.getCustomFieldDataByQuery(SecurityContext.getCompanyID(), cf.getQuery()))
                                .toList()
                                .stream()
                                .collect(
                                        Collectors.toMap(x -> x.getId().toString(), SelectItem::getName)
                                );
                        cfLookupVals.put(cf.getObjectId(), vals);
                    }
                });
                List<CompanyCustomFieldItem> productCustomFieldItems = new ArrayList<>();
                customFieldsItems.forEach(cf -> {
                    if (taskListItem.getCustomFieldsValue(cf.getColumnCode()) != null) {
                        if (cf.getColumnCode().contains("string")) {
                            if (Constants.TYPE_ENTITY_LOOKUP.equals(cf.getUiType())) {
                                cf.setFieldStringValue(cfLookupVals.get(cf.getObjectId()).get(taskListItem.getCustomFieldsValue(cf.getColumnCode()).toString()));
                            } else {
                                cf.setFieldStringValue(taskListItem.getCustomFieldsValue(cf.getColumnCode()).toString());
                            }
                        } else if (cf.getColumnCode().contains("double")) {
                            cf.setFieldStringValue(taskListItem.getCustomFieldsValue(cf.getColumnCode()).toString());
                        } else if (cf.getColumnCode().contains("date")) {
                            cf.setFieldDateNonConvertedValue(new DateNonConvertable((Date) (taskListItem.getCustomFieldsValue(cf.getColumnCode()))));
                        }
                        productCustomFieldItems.add(cf);
                    }
                });
                taskItem.setCustom_fields(getCustomFields(productCustomFieldItems));
            }

            taskList.add(taskItem);
        }

        taskListResult.setList(taskList);

        return successResponse(taskListResult);
    }

    @Transactional
    @Operation(summary = "Create/Update Task", description = "Request to create/update task")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/tasks/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object createUpdateTask(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {


        TaskAddTO taskAddTO;

        ObjectMapper mapper = new ObjectMapper();
        try {
            taskAddTO = mapper.readValue(jsonString, TaskAddTO.class);
            /*LinkedHashMap<Object, Object> customFieldsMap = new LinkedHashMap<>();
            customFieldsMap.put("id",289);
            customFieldsMap.put("draft_files",new ArrayList<>());
            taskAddTO.getTask().getCustom_fields().add(customFieldsMap);*/
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.".concat(e.getMessage()), REQUIRED, HttpStatus.BAD_REQUEST);
        }

        return createTask(multipartRequest, taskAddTO);
    }

    @Operation(summary = "Create a new task")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Task"))
    @RequestMapping(value = "/task/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createTaskByBody(@RequestBody TaskAddTO taskAddTO) throws RestException {
        return createTask(null, taskAddTO);
    }

    private Object createTask(MultipartRequest multipartRequest, TaskAddTO taskAddTO) throws RestException {
        if (taskAddTO.getTask() == null) {
            throw new RestException("Please be sure you entered all required data", "task details is empty", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (!ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (!ServerUtils.hasPermission(PermissionConstants.PM_TASKS_LIST)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        String permission = taskAddTO.getTask() == null ? PermissionConstants.PM_TASKS_ADD : PermissionConstants.PM_TASKS_EDIT;
        if (!ServerUtils.hasPermission(permission)) {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        if (StringUtils.isBlank(taskAddTO.getTask().getName())) {
            throw new RestException("Please be sure you entered all required data", "\"name\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (taskAddTO.getTask().getProject() == null || taskAddTO.getTask().getProject() <= 0) {
            throw new RestException("Please be sure you entered all required data", "\"project\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(taskAddTO.getTask().getPriority())) {
            throw new RestException("Please be sure you entered all required data", "\"priority\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        } else if (TaskPriorityEnum.from(taskAddTO.getTask().getPriority()) == null) {
            throw new RestException("Please be sure you entered all required data", "\"priority\" field must be one of LOW/MEDIUM/HIGH", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //validate
        /*if (taskAddTO.getTask().getSuccessor_tasks() != null && taskAddTO.getTask().getSuccessor_tasks().size() > 0) {
            if (Boolean.FALSE.equals(taskAddTO.getIgnore_warning())) {
                throw new RestException("Adding this task might shift the dates of successor tasks. You can choose one of the following options ", "Confirmation Popup", CONFORMATION, HttpStatus.BAD_REQUEST);
            }
        }*///todo need to clarify. This case is not mentioned in apiary doc

        if (taskAddTO.getTask().getPredecessor_tasks() != null && taskAddTO.getTask().getPredecessor_tasks().size() > 0) {
            if (Boolean.FALSE.equals(taskAddTO.getIgnore_warning())) {
                throw new RestException("The project has been started already, assigning predecessor/successor tasks or parent workstream to a new task might change the dates of successor tasks or parent workstream. \n" +
                        "Do you still want to continue?", "Confirmation Popup", CONFIRMATION, HttpStatus.BAD_REQUEST);
            }
        }

        Date startDate;
        Date endDate;
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        if (taskAddTO.getTask().getWhen() != null) {

            if (StringUtils.isNotBlank(taskAddTO.getTask().getWhen().getStart_date())) {
                try {
                    startDate = longDateTimezoneFormat.parse(taskAddTO.getTask().getWhen().getStart_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is ".concat(longDateTimezoneFormat.toPattern()), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException("Please be sure you entered all required data", "\"start_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (StringUtils.isNotBlank(taskAddTO.getTask().getWhen().getEnd_date())) {
                try {
                    endDate = longDateTimezoneFormat.parse(taskAddTO.getTask().getWhen().getEnd_date());
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException("Invalid date format", "Invalid date format. Acceptable format is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                }
            } else {
                throw new RestException("Please be sure you entered all required data", "\"end_date\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        } else {
            throw new RestException("Please be sure you entered all required data", "\"when\" field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        //End date must be after start date
        if (startDate.getTime() > endDate.getTime() || (startDate.getTime() == endDate.getTime() && Boolean.FALSE.equals(taskAddTO.getTask().getWhen().getAll_day()))) {
            throw new RestException("Please enter correct date and time range for your task", "start_date after end_date", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer attachmentModelFieldId = null;

        //Get model fields by entity type
        List<ModelField> modelFields;
        try {
            modelFields = modelFieldManager.getFields(FORM_TYPES.get(EntityTypeEnum.TASKS.name().toLowerCase()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        //Map key is model field id and man value is model field field_id. e.g 1,CRM_OPPORTUNITY_STAGE
        LinkedHashMap<Integer, String> modelFieldsMap = new LinkedHashMap<>();
        if (modelFields != null && modelFields.size() > 0) {
            modelFields.forEach(modelField -> modelFieldsMap.put(modelField.getObjectID(), modelField.getField_ID()));
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
        for (Object customFieldObject : taskAddTO.getTask().getCustom_fields()) {
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
                taskAddTO.getTask().getCustom_fields().add(customFieldsMap);
            }
        }

        //Map key is model field field_id and value is field value. e.g. CRM_OPPORTUNITY_STAGE, Close Won
        LinkedHashMap<String, Object> modelFieldValueMap = new LinkedHashMap<>();

        //Separate real custom fields and fields that are given as custom field by GAP_BTW_STATIC_AND_CUSTOM_FIELDS
        ArrayList<Object> customFieldObjects = new ArrayList<>();
        if (!taskAddTO.getTask().getCustom_fields().isEmpty()) {
            for (Object customFieldObject : taskAddTO.getTask().getCustom_fields()) {
                LinkedHashMap<Object, Object> customFieldsMap = (LinkedHashMap<Object, Object>) customFieldObject;
                if (customFieldsMap.get("id") != null) {
                    if ((Integer) customFieldsMap.get("id") < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {//it means this is real custom field
                        customFieldObjects.add(customFieldsMap);
                    } else {//this is static model field attachment
                        String fieldID = modelFieldsMap.get((Integer) customFieldsMap.get("id") - GAP_BTW_STATIC_AND_CUSTOM_FIELDS);//it means model field
                        if (StringUtils.isNotBlank(fieldID)) {
                            if (StringUtils.isNotBlank((String) customFieldsMap.get("text"))) {//for text fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("text"));
                            } else if (customFieldsMap.get("value") != null) {//for number fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("value"));
                            } else if (customFieldsMap.get("category_id") != null) {//for drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("category_id"));
                            } else if (customFieldsMap.get("choosed_ids") != null) {//for multi drop down fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("choosed_ids"));
                            } else if (customFieldsMap.get("date") != null) {//for date fields
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("date"));
                            } else if (customFieldsMap.get("draft_files") != null) {//for attachments
                                modelFieldValueMap.put(fieldID, customFieldsMap.get("draft_files"));
                                attachmentModelFieldId = (Integer) customFieldsMap.get("id") - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                            }
                        }
                    }
                }
            }
        }
        //ADD TASK
        if (taskAddTO.getTask().getId() == null) {


            //Map key is model field id that related to attachment, and value is attachment
            LinkedHashMap<Integer, ArrayList<MultipartFile>> attachmentsMap = new LinkedHashMap<>();
            ArrayList<MultipartFile> taskAttachments = new ArrayList<>();
            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                for (MultipartFile file : multipartRequest.getFileMap().values()) {
                    if (file.getName().matches(customFieldFileNameRegex)) {
                        String[] fileName = file.getName().split("_");
                        Integer id = Integer.valueOf(fileName[2]);
                        if (id > GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                            id = id - GAP_BTW_STATIC_AND_CUSTOM_FIELDS;
                            ArrayList<MultipartFile> files;
                            if (attachmentsMap.get(id) == null) {
                                files = new ArrayList<>();
                                files.add(file);
                                attachmentsMap.put(id, files);
                            } else {
                                attachmentsMap.get(id).add(file);
                            }
                        }
                    }
                }
            }


            TaskSingleItem newTask = new TaskSingleItem();
            newTask.setNumberData(taskServiceLocal.generateTaskNumber(taskAddTO.getTask().getProject(), startDate, null));
            newTask.setProjectID(taskAddTO.getTask().getProject());
            newTask.setName(taskAddTO.getTask().getName());
            newTask.setDescription(taskAddTO.getTask().getDescription());
            if (StringUtils.isNotBlank(taskAddTO.getTask().getPriority())) {
                EdsReference priority = referenceManager.getByCode(TaskPriorityEnum.from(taskAddTO.getTask().getPriority()));
                if (priority != null) {
                    newTask.setPriorityID(priority.getObjectID());
                }
            }
            newTask.setStartDate(startDate);
            newTask.setDueDate(endDate);
            newTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
            newTask.setStatusID(taskAddTO.getTask().getStatus());
            newTask.setBillable(taskAddTO.getTask().getBillable() != null ? taskAddTO.getTask().getBillable() : false);
            newTask.setWorkstreamID(taskAddTO.getTask().getParent_workstream());

            //Predecessor tasks
            LinkedHashMap<Integer, Integer> predecessorTasksMap = new LinkedHashMap<>();
            if (taskAddTO.getTask().getPredecessor_tasks() != null && taskAddTO.getTask().getPredecessor_tasks().size() > 0) {
                ArrayList<TaskSelectItem> predecessorTasks = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getPredecessor_tasks()) {
                    TaskSelectItem predecessorTask = new TaskSelectItem();
                    predecessorTask.setId(linkTO.getItem_id());
                    predecessorTask.setName(linkTO.getName());
                    predecessorTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
                    predecessorTask.setTaskStartDate(startDate);
                    predecessorTask.setTaskDueDate(endDate);
                    predecessorTask.setProjectId(taskAddTO.getTask().getProject());
                    predecessorTask.setTaskNumber(newTask.getNumberData().getNumberString());
                    predecessorTasks.add(predecessorTask);
                    predecessorTasksMap.put(linkTO.getItem_id(), linkTO.getItem_id());
                }
                newTask.setPredecessorTasks(predecessorTasks.toArray(new TaskSelectItem[0]));
            }

            //Successor tasks
            if (taskAddTO.getTask().getSuccessor_tasks() != null && taskAddTO.getTask().getSuccessor_tasks().size() > 0) {
                ArrayList<TaskSelectItem> successorTasks = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getSuccessor_tasks()) {
                    if (predecessorTasksMap.get(linkTO.getItem_id()) == null) {//predecessor tasks cannot be successor tasks at the same time
                        TaskSelectItem successorTask = new TaskSelectItem();
                        successorTask.setId(linkTO.getItem_id());
                        successorTask.setName(linkTO.getName());
                        successorTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
                        successorTask.setTaskStartDate(startDate);
                        successorTask.setTaskDueDate(endDate);
                        successorTask.setProjectId(taskAddTO.getTask().getProject());
                        successorTask.setTaskNumber(newTask.getNumberData().getNumberString());
                        successorTasks.add(successorTask);
                    }
                }
                newTask.setSuccessorTasks(successorTasks.toArray(new TaskSelectItem[0]));
            }

            //Reminders
            if (taskAddTO.getTask().getReminders() != null && taskAddTO.getTask().getReminders().size() > 0) {
                ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
                for (TimeTO reminder : taskAddTO.getTask().getReminders()) {
                    if ((reminder.getHour() != null && reminder.getHour() > 0) || (reminder.getMinute() != null && reminder.getMinute() > 0)) {
                        CalendarEventReminder calendarEventReminder = new CalendarEventReminder();
                        calendarEventReminder.setValue(Constants.E_MAIL);

                        if (reminder.getHour() != null && reminder.getHour() > 0) {
                            calendarEventReminder.setReminderTimes(reminder.getHour() * 60);
                        } else {
                            calendarEventReminder.setReminderTimes(reminder.getMinute());
                        }
                        eventReminders.add(calendarEventReminder);
                    }
                }
                if (!eventReminders.isEmpty()) {
                    newTask.setReminder(eventReminders);
                }
            }

            //Assignees
            ArrayList<IdTime> assignees = new ArrayList<>();

            if (taskAddTO.getTask().getAssignees() != null && taskAddTO.getTask().getAssignees().getDepartments() != null && taskAddTO.getTask().getAssignees().getDepartments().size() > 0) {
                //EdsCompany company = departmentManager.getUser().getCompany();
                EdsProject project = projectManager.get(taskAddTO.getTask().getProject());
                EdsReference notStartedStatus = referenceManager.getByCode(EdsTask.NOT_STARTED);
                //List<EdsDepartment> allDepartments = departmentManager.getCompanyDepartments(company);
                for (ShareWithDepartmentsTO department : taskAddTO.getTask().getAssignees().getDepartments()) {
                    if (Boolean.TRUE.equals(department.getIs_all_selected())) {
                        Map<Integer, Integer> excludedEmployeeIds = department.getExcluded_employees_ids().stream().collect(Collectors.toMap(emplId -> emplId, emplId -> emplId));

                        //if (allDepartments != null && allDepartments.size() > 0) {
                        //for (EdsDepartment team : allDepartments) {
                        List<EdsEmployeeDepartment> teamEmployees = employeeDepartmentManager.getTeamEmployees(department.getId());
                        if (teamEmployees != null) {
                            for (EdsEmployeeDepartment teamEmployee : teamEmployees) {
                                if (teamEmployee.getEmployee() != null && excludedEmployeeIds.get(teamEmployee.getEmployee().getObjectID()) == null) {
                                    EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(teamEmployee.getEmployee(), project);
                                    if (projectEmployee == null) {
                                        projectEmployee = taskServiceLocal.addMembers(project, teamEmployee.getEmployee());
                                    }
                                    if (department.getEstimates() != null && department.getEstimates().size() > 0) {
                                        TimeTO timeTO = department.getEstimates().get(projectEmployee.getObjectID());
                                        if (timeTO != null) {
                                            int hoursInMin = timeTO.getHour() != null ? (timeTO.getHour() * 60) : 0;
                                            int min = timeTO.getMinute() != null ? timeTO.getMinute() : 0;
                                            assignees.add(new IdTime(projectEmployee.getObjectID(), (hoursInMin + min), notStartedStatus.getObjectID()));
                                        } else {
                                            assignees.add(new IdTime(projectEmployee.getObjectID(), 0, notStartedStatus.getObjectID()));
                                        }
                                    } else {
                                        assignees.add(new IdTime(projectEmployee.getObjectID(), 0, notStartedStatus.getObjectID()));
                                    }
                                }
                            }
                            //}
                            //}
                        }
                    } else {
                        //Else If not all employees of department are selected
                        if (department.getPicked_employees_ids() != null && department.getPicked_employees_ids().size() > 0) {
                            department.getPicked_employees_ids().forEach(employeeId -> {
                                TimeTO timeTO = (department.getEstimates() != null && department.getEstimates().size() > 0) ? department.getEstimates().get(employeeId) : null;
                                if (timeTO != null) {
                                    int hoursInMin = timeTO.getHour() != null ? (timeTO.getHour() * 60) : 0;
                                    int min = timeTO.getMinute() != null ? timeTO.getMinute() : 0;
                                    assignees.add(new IdTime(employeeId, (hoursInMin + min), notStartedStatus.getObjectID()));
                                } else {
                                    assignees.add(new IdTime(employeeId, 0, notStartedStatus.getObjectID()));
                                }
                            });
                        }
                    }
                }
                newTask.setProjectEmployees(assignees.toArray(new IdTime[0]));

            }
            //Links
            if (taskAddTO.getTask().getLinks() != null && taskAddTO.getTask().getLinks().size() > 0) {
                ArrayList<RelationItem> relations = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getLinks()) {
                    RelationItem relation = new RelationItem();
                    relation.setFromType(RelationItem.TYPE_TASK);
                    relation.setToID(linkTO.getItem_id());
                    relation.setToType(getEntityRelation(linkTO.getLink_type()));
                    relation.setToName(linkTO.getName());
                    relations.add(relation);
                }
                newTask.setRelations(relations);
            }

            newTask.setLastModified(new Date());

            //Other fields
            HistoryListItem note = null;
            for (String fieldID : modelFieldValueMap.keySet()) {
                if (CustomFormConstants.NUMBER.equals(fieldID)) {
                    newTask.getNumberData().setNumberString((String) modelFieldValueMap.get(fieldID));
                } else if (CustomFormConstants.TASK.TASK_NOTE.equals(fieldID)) {
                    if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                        note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                    }
                } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                    if (attachmentModelFieldId != null && attachmentsMap.get(attachmentModelFieldId) != null) {
                        taskAttachments.addAll(attachmentsMap.get(attachmentModelFieldId));
                    }
                }
            }

            //Custom fields
            ArrayList<CompanyCustomFieldItem> customFieldItems;
            if (customFieldObjects.size() > 0) {
                customFieldItems = convertCustomFields(customFieldObjects, multipartRequest);
                if (customFieldItems != null && customFieldItems.size() > 0) {
                    newTask.setCustomFieldItems(customFieldItems);
                }
            }

            Integer taskId;
            Integer projectId;
            try {
                //If employees_type is PROJECT_EMPLOYEES or ONLY_AVAILABLE we don't have to create project employees
                //if (taskAddTO.getTask().getAssignees() != null && (EmployeeTypeEnum.PROJECT_EMPLOYEES.name().equals(taskAddTO.getTask().getAssignees().getEmployees_type()) || EmployeeTypeEnum.ONLY_AVAILABLE.name().equals(taskAddTO.getTask().getAssignees().getEmployees_type()))) {
                Integer[] result = taskServiceLocal.saveTask(newTask);
                projectId = result[0];
                taskId = result[1];
                //}
                /*else {
                    Integer[] result = taskServiceLocal.saveTaskWithNewProjectEmployees(newTask);
                    projectId = result[0];
                    taskId = result[1];
                }*/
            } catch (NumberExistingException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (note != null) {
                try {
                    note.setRelatedToId(EdsNoteHistory.TASK);
                    note.setRelatedId(taskId);
                    noteServiceLocal.saveNote(note);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            if (taskAttachments.size() > 0) {
                FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_TASK, projectId);
                for (MultipartFile multipartFile : taskAttachments) {
                    try {
                        documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), folderResource.getFileType(), taskId, null);
                    } catch (Exception e) {
                        log.error("", e);
                        throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        } else {//EDIT TASK
            EditTask editTask = taskServiceLocal.getTaskForEdit(taskAddTO.getTask().getId());
            if (taskAddTO.getTask().getProject() != null) {
                editTask.setProjectId(taskAddTO.getTask().getProject());
            }
            editTask.setName(taskAddTO.getTask().getName());
            editTask.setDescription(taskAddTO.getTask().getDescription());
            if (StringUtils.isNotBlank(taskAddTO.getTask().getPriority())) {
                EdsReference priority = referenceManager.getByCode(TaskPriorityEnum.from(taskAddTO.getTask().getPriority()));
                if (priority != null) {
                    editTask.setPriorityId(priority.getObjectID());
                }
            }
            editTask.setBillable(taskAddTO.getTask().getBillable());
            editTask.setStartDate(startDate);
            editTask.setDueDate(endDate);
            editTask.setStatusId(taskAddTO.getTask().getStatus());
            editTask.setUpdateTaskStatusForAll(true);
            editTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
            if (taskAddTO.getTask().getParent_workstream() != null) {
                editTask.setParentWSItem(new SelectItem(taskAddTO.getTask().getParent_workstream()));
            } else {
                editTask.setParentWSItem(null);
            }

            //Predecessor tasks
            LinkedHashMap<Integer, Integer> predecessorTasksMap = new LinkedHashMap<>();
            if (taskAddTO.getTask().getPredecessor_tasks() != null && taskAddTO.getTask().getPredecessor_tasks().size() > 0) {
                ArrayList<TaskSelectItem> predecessorTasks = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getPredecessor_tasks()) {
                    TaskSelectItem predecessorTask = new TaskSelectItem();
                    predecessorTask.setId(linkTO.getItem_id());
                    predecessorTask.setName(linkTO.getName());
                    predecessorTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
                    predecessorTask.setTaskStartDate(startDate);
                    predecessorTask.setTaskDueDate(endDate);
                    predecessorTask.setProjectId(taskAddTO.getTask().getProject());
                    predecessorTask.setTaskNumber(editTask.getNumberData().getNumberString());
                    predecessorTasks.add(predecessorTask);
                    predecessorTasksMap.put(linkTO.getItem_id(), linkTO.getItem_id());
                }
                editTask.setPredecessorTaskItems(predecessorTasks.toArray(new TaskSelectItem[0]));
            } else {
                editTask.setPredecessorTaskItems(null);
            }

            //Successor tasks
            if (taskAddTO.getTask().getSuccessor_tasks() != null && taskAddTO.getTask().getSuccessor_tasks().size() > 0) {
                ArrayList<TaskSelectItem> successorTasks = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getSuccessor_tasks()) {
                    if (predecessorTasksMap.get(linkTO.getItem_id()) == null) {//predecessor tasks cannot be successor tasks at the same time
                        TaskSelectItem successorTask = new TaskSelectItem();
                        successorTask.setId(linkTO.getItem_id());
                        successorTask.setName(linkTO.getName());
                        successorTask.setAllDay(taskAddTO.getTask().getWhen().getAll_day());
                        successorTask.setTaskStartDate(startDate);
                        successorTask.setTaskDueDate(endDate);
                        successorTask.setProjectId(taskAddTO.getTask().getProject());
                        successorTask.setTaskNumber(editTask.getNumberData().getNumberString());
                        successorTasks.add(successorTask);
                    }
                }
                editTask.setSuccessorTaskItems(successorTasks.toArray(new TaskSelectItem[0]));
            } else {
                editTask.setSuccessorTaskItems(null);
            }

            //Reminders
            if (taskAddTO.getTask().getReminders() != null && taskAddTO.getTask().getReminders().size() > 0) {
                ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
                for (TimeTO reminder : taskAddTO.getTask().getReminders()) {
                    if ((reminder.getHour() != null && reminder.getHour() > 0) || (reminder.getMinute() != null && reminder.getMinute() > 0)) {
                        CalendarEventReminder calendarEventReminder = new CalendarEventReminder();
                        calendarEventReminder.setValue(Constants.E_MAIL);

                        if (reminder.getHour() != null && reminder.getHour() > 0) {
                            calendarEventReminder.setReminderTimes(reminder.getHour() * 60);
                        } else {
                            calendarEventReminder.setReminderTimes(reminder.getMinute());
                        }
                        eventReminders.add(calendarEventReminder);
                    }
                }
                if (!eventReminders.isEmpty()) {
                    editTask.setReminders(eventReminders);
                }
            } else {
                editTask.setReminders(null);
            }

            //Links
            if (taskAddTO.getTask().getLinks() != null && taskAddTO.getTask().getLinks().size() > 0) {
                try {
                    relationManager.deleteAllRelations(RelationItem.TYPE_TASK, editTask.getObjectID());
                } catch (Exception e) {
                    log.error("Api error occurred while deleting task relations", e);
                }
                ArrayList<RelationItem> relations = new ArrayList<>();
                for (LinkTO linkTO : taskAddTO.getTask().getLinks()) {
                    RelationItem relation = new RelationItem();
                    relation.setFromType(RelationItem.TYPE_TASK);
                    relation.setToID(linkTO.getItem_id());
                    relation.setToType(getEntityRelation(linkTO.getLink_type()));
                    relation.setToName(linkTO.getName());
                    relations.add(relation);
                }
                editTask.setRelations(relations);
            } else {
                try {
                    relationManager.deleteAllRelations(RelationItem.TYPE_TASK, editTask.getObjectID());
                } catch (Exception e) {
                    log.error("Api error occurred while deleting task relations", e);
                }
            }

            //Assignees
            ArrayList<IdTime> assignees = new ArrayList<>();
            LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> taskAssigneesMap = taskServiceLocal.getTaskMembersWithTreeInfo(taskAddTO.getTask().getId());

            LinkedHashMap<Integer, IdTime> assigneesMap = new LinkedHashMap<>();
            if (taskAssigneesMap != null && taskAssigneesMap.size() > 0) {
                for (ArrayList<KpiTreeInfo> treeInfoList : taskAssigneesMap.values()) {
                    for (KpiTreeInfo treeInfo : treeInfoList) {
                        if (treeInfo.isSelected()) {
                            assigneesMap.put(treeInfo.getId(), new IdTime(treeInfo.getId(), treeInfo.getTime(), treeInfo.getActualTime(), treeInfo.getPercent(), treeInfo.getStatusId()));
                        }
                    }
                }
            }

            if (taskAddTO.getTask().getAssignees() != null && taskAddTO.getTask().getAssignees().getDepartments() != null && taskAddTO.getTask().getAssignees().getDepartments().size() > 0) {
                //EdsCompany company = departmentManager.getUser().getCompany();
                //EdsReference notStartedStatus = referenceManager.getByCode(EdsTask.NOT_STARTED);
                //List<EdsDepartment> allDepartments = departmentManager.getCompanyDepartments(company);
                EdsProject project = projectManager.get(taskAddTO.getTask().getProject());
                for (ShareWithDepartmentsTO department : taskAddTO.getTask().getAssignees().getDepartments()) {
                    if (Boolean.TRUE.equals(department.getIs_all_selected())) {
                        Map<Integer, Integer> excludedEmployeeIdsMap = department.getExcluded_employees_ids().stream().collect(Collectors.toMap(emplId -> emplId, emplId -> emplId));

                        //if (allDepartments != null && allDepartments.size() > 0) {
                        //for (EdsDepartment team : allDepartments) {
                        List<EdsEmployeeDepartment> teamEmployees = employeeDepartmentManager.getTeamEmployees(department.getId());
                        if (teamEmployees != null) {
                            for (EdsEmployeeDepartment teamEmployee : teamEmployees) {
                                if (teamEmployee.getEmployee() != null) {
                                    EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(teamEmployee.getEmployee(), project);
                                    if (projectEmployee == null) {
                                        projectEmployee = taskServiceLocal.addMembers(project, teamEmployee.getEmployee());
                                    }
                                    if (excludedEmployeeIdsMap.get(projectEmployee.getObjectID()) == null) {
                                        TimeTO timeTO = (department.getEstimates() != null && department.getEstimates().size() > 0) ? department.getEstimates().get(projectEmployee.getObjectID()) : null;
                                        if (timeTO != null) {
                                            int hoursInMin = timeTO.getHour() != null ? (timeTO.getHour() * 60) : 0;
                                            int min = timeTO.getMinute() != null ? timeTO.getMinute() : 0;
                                            Integer statusId = assigneesMap.get(projectEmployee.getObjectID()) != null ? assigneesMap.get(projectEmployee.getObjectID()).getStatusId() : null;
                                            //statusId = statusId != null ? statusId : notStartedStatus.getObjectID();
                                            statusId = statusId != null ? statusId : editTask.getStatusId();
                                            assignees.add(new IdTime(projectEmployee.getObjectID(), (hoursInMin + min), statusId));
                                        } else {
                                            //assignees.add(new IdTime(projectEmployee.getObjectID(), 0, notStartedStatus.getObjectID()));
                                            assignees.add(new IdTime(projectEmployee.getObjectID(), 0, editTask.getStatusId()));
                                        }
                                    }
                                }
                            }
                            //}
                        }
                        //}
                    } else {
                        //Else If not all employees of department are selected
                        if (department.getPicked_employees_ids() != null && department.getPicked_employees_ids().size() > 0) {
                            department.getPicked_employees_ids().forEach(employeeId -> {
                                TimeTO timeTO = (department.getEstimates() != null && department.getEstimates().size() > 0) ? department.getEstimates().get(employeeId) : null;
                                if (timeTO != null) {
                                    int hoursInMin = timeTO.getHour() != null ? (timeTO.getHour() * 60) : 0;
                                    int min = timeTO.getMinute() != null ? timeTO.getMinute() : 0;
                                    Integer statusId = assigneesMap.get(employeeId) != null ? assigneesMap.get(employeeId).getStatusId() : null;
                                    statusId = statusId != null ? statusId : editTask.getStatusId();
                                    assignees.add(new IdTime(employeeId, (hoursInMin + min), statusId));
                                } else {
                                    assignees.add(new IdTime(employeeId, 0, editTask.getStatusId()));
                                }
                            });
                        }
                    }
                }

                editTask.setAssigneeItems(assignees.toArray(new IdTime[0]));
            }

            //editTask.setDontKeepDelays(taskAddTO.getIgnore_warning() == null ? true : taskAddTO.getIgnore_warning());

            //Other fields
            //HistoryListItem note = null;
            ArrayList<AttachmentTO> taskDraftAttachments = new ArrayList<>();
            for (String fieldID : modelFieldValueMap.keySet()) {
                if (CustomFormConstants.NUMBER.equals(fieldID)) {
                    editTask.getNumberData().setNumberString((String) modelFieldValueMap.get(fieldID));
                } else if (CustomFormConstants.TASK.TASK_NOTE.equals(fieldID)) {
                    /*if (StringUtils.isNotBlank((String) modelFieldValueMap.get(fieldID))) {
                        note = new HistoryListItem((String) modelFieldValueMap.get(fieldID));
                    }*/
                } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                    if (modelFieldValueMap.get(fieldID) != null && modelFieldValueMap.get(fieldID) instanceof List) {
                        List<LinkedHashMap<Object, Object>> objects = (List) modelFieldValueMap.get(fieldID);
                        if (objects != null && objects.size() > 0) {
                            for (LinkedHashMap<Object, Object> objMap : objects) {
                                AttachmentTO attachmentTO = new AttachmentTO();
                                attachmentTO.setFile_name((String) objMap.get("file_name"));
                                attachmentTO.setLink((String) objMap.get("link"));

                                taskDraftAttachments.add(attachmentTO);
                            }
                        }
                    }
                }
            }


            ArrayList<MultipartFile> taskAttachments = new ArrayList<>();
            TreeMap<Integer, ArrayList<MultipartFile>> customFieldAttachmentsMap = new TreeMap<>();

            Pattern pattern = Pattern.compile(customFieldFileNameRegex);

            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                for (MultipartFile file : multipartRequest.getFileMap().values()) {
                    if (file.getName().matches(customFieldFileNameRegex)) {
                        Matcher m = pattern.matcher(file.getName());
                        Integer customFieldFileId;
                        if (m.matches()) {
                            customFieldFileId = Integer.valueOf(m.group(1));
                            if (customFieldFileId > GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {
                                taskAttachments.add(file);
                            } else {
                                ArrayList<MultipartFile> files = customFieldAttachmentsMap.get(customFieldFileId) == null ? new ArrayList<>() : customFieldAttachmentsMap.get(customFieldFileId);
                                files.add(file);
                                customFieldAttachmentsMap.put(customFieldFileId, files);
                            }
                        }
                    }
                }
            }

            //CUSTOM FIELD ATTACHMENTS

            ArrayList<CompanyCustomFieldItem> customFieldItems = null;
            LinkedHashMap<Integer, ArrayList<AttachmentTO>> customFieldDraftAttachmentMap = new LinkedHashMap<>();
            if (taskAddTO.getTask().getCustom_fields().size() > 0) {
                customFieldItems = convertCustomFields(taskAddTO.getTask().getCustom_fields(), customFieldDraftAttachmentMap);
            }

            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            ArrayList<FileResource> oldAttachments = new ArrayList<>();
            HashSet<Integer> deleteIDs = new HashSet<>();

            if (editTask.getCustomFieldItems() != null && editTask.getCustomFieldItems().size() > 0) {
                for (CompanyCustomFieldItem companyCustomFieldItem : editTask.getCustomFieldItems()) {
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
                    if (editTask.getCustomFieldItems() != null && editTask.getCustomFieldItems().size() > 0) {
                        for (CompanyCustomFieldItem companyCustomFieldItem : editTask.getCustomFieldItems()) {
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
                    FolderResource tempFolder = documentsServiceLocal.getTempFolderByCompany(userManager.getUser().getCompany().getObjectID());
                    //if old files are empty, upload new files
                    if (oldAttachments.size() == 0) {
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
                editTask.setCustomFieldItems(customFieldItems);
            }

            try {
                synchronizedTaskUpdate(editTask);
            } catch (NumberExistingException e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }


            ///////////////// SAVE TASK ATTACHMENTS /////////////////////////////////////


            //Compare draft files to old files by unique keys: filename & file size. If there is a difference between them by name or size, delete the differ old files
            // but keep other non changed files
            oldAttachments = documentsServiceLocal.getFileResources(Constants.F_TASK, editTask.getProjectId(), editTask.getObjectID());
            FolderResource folderResource = documentsServiceLocal.getFolderResource(Constants.F_TASK, editTask.getProjectId());
            deleteIDs = new HashSet<>();
            //if draft attachments are empty, remove all old task attachments.
            if (taskDraftAttachments.size() == 0) {
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
                //after delete old attachments, get not deleted attachment as old attachments
                oldAttachments = documentsServiceLocal.getFileResources(Constants.F_TASK, editTask.getProjectId(), editTask.getObjectID());
                uploadFiles(editTask.getObjectID(), folderResource.getFileType(), folderResource.getObjectId(), taskAttachments, oldAttachments);

                //if draft attachments do not match with task old attachments by filename and file size, delete not matched old attachments
            } else if (taskDraftAttachments.size() > 0) {
                if (oldAttachments != null && oldAttachments.size() > 0) {
                    LinkedHashMap<String, String> draftAttachmentMap = new LinkedHashMap<>();
                    for (AttachmentTO draftAttachment : taskDraftAttachments) {
                        draftAttachmentMap.put(draftAttachment.getFile_name(), draftAttachment.getFile_name());
                    }
                    for (FileResource oldAttachment : oldAttachments) {
                        String draftFilename = draftAttachmentMap.get(oldAttachment.getFileName());
                        if (StringUtils.isNotBlank(draftFilename)) {
                            FileResource fileResource = documentsServiceLocal.getFileResourceByFileTypeAndName(Constants.F_TASK, draftFilename);
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
                    oldAttachments = documentsServiceLocal.getFileResources(Constants.F_TASK, editTask.getProjectId(), editTask.getObjectID());


                    try {
                        if (taskAttachments.size() > 0) {
                            //if old files are empty, upload new files
                            if (oldAttachments == null || oldAttachments.size() == 0) {
                                for (MultipartFile file : taskAttachments) {
                                    try {
                                        documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), editTask.getObjectID(), null);
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            } else {//If old files aren't empty, merge old and new files
                                deleteIDs = new HashSet<>();
                                LinkedHashMap<String, FileResource> oldFilesMap = new LinkedHashMap<>();
                                for (FileResource file : oldAttachments) {
                                    oldFilesMap.put(file.getFileName(), file);
                                }

                                for (MultipartFile multipartFile : taskAttachments) {
                                    FileResource oldFile = oldFilesMap.get(multipartFile.getOriginalFilename());
                                    if (oldFile != null) {
                                        deleteIDs.add(oldFile.getObjectId());
                                    }
                                }

                                if (deleteIDs.size() > 0) {
                                    try {
                                        documentsServiceLocal.deleteFiles(new ArrayList<>(deleteIDs));
                                    } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
                                        log.error("", e);
                                    }
                                }
                                for (MultipartFile file : taskAttachments) {
                                    try {
                                        documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), editTask.getObjectID(), null);
                                    } catch (Exception e) {
                                        log.error("", e);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("", e);
                    }

                }
            }

        }

        return successResponse(new ResponseData());
    }

    private synchronized void synchronizedTaskUpdate(EditTask editTask) throws NumberExistingException {
        taskServiceLocal.updateTask(editTask);
    }


    @Operation(summary = "Task Detail Info")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have task details"),
            @ApiResponse(responseCode = "400", description = "id is required")})
    @RequestMapping(value = "/tasks/{id}/details", method = RequestMethod.GET)
    public Object getTaskDetailsInfo(@PathVariable(value = "id") Integer id) throws RestException {
        if (id == null || id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
        TaskSingleItem taskItem;
        try {
            taskItem = taskServiceLocal.getTask(id, true);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (taskItem == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task Item has not been found with provided task_id", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        TaskDetailsItemTO crmTaskDetails = new TaskDetailsItemTO();

        //base_info
        TaskBaseInfoTO baseInfo = new TaskBaseInfoTO();
        baseInfo.setName(taskItem.getName());
        if (StringUtils.isNotBlank(taskItem.getDescription())) {
            baseInfo.setDescription(taskItem.getDescription());
        }
        if (taskItem.getStatusID() != null) {
            baseInfo.setStatus_id(taskItem.getStatusID());
        }
        baseInfo.setItem_id(taskItem.getObjectID());
        /*if (taskItem.getBaseTaskID() != null) {
            baseInfo.setItem_id(taskItem.getBaseTaskID());
        }*/
        if (taskItem.getDueDate() != null) {
            baseInfo.setDue_date(longDateTimezoneFormat.format(taskItem.getDueDate()));
        }
        baseInfo.setPriority(TaskPriorityEnum.get(taskItem.getPriorityCode()));

        crmTaskDetails.setBase_info(baseInfo);

        // status
        if (taskItem.getStatusID() != null) {
            FilteredStatusItemTO taskStatus = new FilteredStatusItemTO();
            EdsReference edsReference = referenceManager.get(taskItem.getStatusID());
            if (edsReference != null) {
                taskStatus.setStatus_id(edsReference.getObjectID());
                taskStatus.setStatus_name(edsReference.getName());
                taskStatus.setOrder_id(edsReference.getSorder());
                taskStatus.setIs_system(edsReference.isSystemReference());

                if (edsReference.getReferenceColor() != null) {
                    EdsReferenceColor edsReferenceColor = edsReference.getReferenceColor();
                    if (edsReferenceColor != null) {
                        taskStatus.setStatus_color(new ColorTO(edsReferenceColor.getObjectID(), edsReferenceColor.getHex(), edsReferenceColor.getName()));
                    } else {
                        taskStatus.setStatus_color(getDefaultColor());
                    }
                } else {
                    taskStatus.setStatus_color(getDefaultColor());
                }
            }
            crmTaskDetails.setStatus(taskStatus);
        }

        //share link
        String taskShareLink = EdsContextParams.getFullHost().concat(Constants.CRM_URL).concat("#").concat("task|summary/").concat(taskItem.getObjectID().toString()).concat("/true");
        crmTaskDetails.setShare_link(taskShareLink);
        // task assignees
        if (taskItem.getIssueEmployees() != null) {
            ArrayList<TaskAssigneeTO> taskAssignees = new ArrayList<>();
            for (PositionsSelectItem positionsSelectItem : taskItem.getIssueEmployees()) {
                if (positionsSelectItem != null) {
                    TaskAssigneeTO assignee = new TaskAssigneeTO();
                    assignee.setId(positionsSelectItem.getId());
                    assignee.setName(positionsSelectItem.getName());
                    try {
                        assignee.setAvatar_image(hrmsServiceLocal.getEmployeeImageURL(positionsSelectItem.getExactEmployeeId()));
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    if (positionsSelectItem.getDepartmentId() != null) {
                        assignee.setDepartment(new IdNameTO(positionsSelectItem.getDepartmentId(), positionsSelectItem.getDepartmentName()));
                    }
                    if (positionsSelectItem.getTime() != null && positionsSelectItem.getTime() > 0) {
                        TimeTO estimate = new TimeTO();
                        int hours = positionsSelectItem.getTime() / 60;
                        int minutes = positionsSelectItem.getTime() - hours * 60;
                        estimate.setMinute(minutes);
                        estimate.setHour(hours);
                        assignee.setEstimate(estimate);
                    }
                    taskAssignees.add(assignee);
                }
            }

            crmTaskDetails.setAssignees(taskAssignees);
        }

        //Main Information about a task
        TaskInformationTO taskInformation = new TaskInformationTO();
        if (taskItem.getStartDate() != null) {
            taskInformation.setStart_date(longDateTimezoneFormat.format(taskItem.getStartDate()));
        }
        if (taskItem.getEndDate() != null) {
            taskInformation.setEnd_date(longDateTimezoneFormat.format(taskItem.getEndDate()));
        }
        taskInformation.setAll_day(taskItem.isAllDay());
        if (taskItem.getNumberData() != null) {
            taskInformation.setNumber(taskItem.getNumberData().getNumberString());
        }
        if (taskItem.getProjectID() != null && taskItem.getProjectID() > 0) {
            taskInformation.setProject(new CategoryTO(taskItem.getProjectID(), taskItem.getProjectName()));
        }
        taskInformation.setBillable(taskItem.getBillable());
        if (taskItem.getWorkstreamID() != null && taskItem.getWorkstreamID() > 0) {
            taskInformation.setParent_workstream(new CategoryTO(taskItem.getWorkstreamID(), taskItem.getWorkstreamName()));
        }
        ArrayList<CalendarEventReminder> reminders = taskReminderManager.getReminders(taskItem.getObjectID());
        if (reminders != null && reminders.size() > 0) {
            ArrayList<TimeTO> taskReminders = new ArrayList<>();
            reminders.forEach(reminder -> {
                TimeTO reminderTO = new TimeTO();
                int hours = reminder.getReminderTimes() / 60;
                int minutes = reminder.getReminderTimes() - hours * 60;
                reminderTO.setMinute(minutes);
                reminderTO.setHour(hours);
                taskReminders.add(reminderTO);
            });
            taskInformation.setReminders(taskReminders);
        }

        RecurrenceJobItem recurrenceJobItem = taskItem.getRecurrenceJobItem();
        if (recurrenceJobItem != null) {
            RecurrenceTO recurrence = new RecurrenceTO();
            // recurrence until
            RecurrenceUntilTO recurrenceUntil = new RecurrenceUntilTO();
            if (recurrenceJobItem.getEndDate() != null) {
                recurrenceUntil.setType("DATE");
                recurrenceUntil.setDate(longDateTimezoneFormat.format(recurrenceJobItem.getEndDate()));
            } else {
                recurrenceUntil.setType("NUMBER_EVENTS");
                recurrenceUntil.setOccurences(recurrenceJobItem.getOccurrence());
            }
            recurrence.setUntil(recurrenceUntil);
            // recurrence repeats
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

            taskInformation.setRecurrence(recurrence);
        }
        if (taskItem.getRelations() != null && taskItem.getRelations().size() > 0) {
            ArrayList<LinksTO> taskLinkList = new ArrayList<>();
            for (RelationItem relationItem : taskItem.getRelations()) {
                if (!RelationItem.TYPE_EMAIL_TRACKER.equals(relationItem.getFromType())) {
                    LinksTO link = new LinksTO();
                    link.setId(relationItem.getToID());
                    link.setName(relationItem.getToName());
                    link.setLink_type(getLinkType(relationItem.getToType()));
                    if (getLinkType(relationItem.getToType()) != null)
                        taskLinkList.add(link);
                }
            }
            taskInformation.setLinks(taskLinkList);
        }
        if (taskItem.getPredecessorTasks() != null && taskItem.getPredecessorTasks().length > 0) {
            ArrayList<LinksTO> predecessorTasks = new ArrayList<>();
            for (SelectItem item : taskItem.getPredecessorTasks()) {
                LinksTO predecessorTask = new LinksTO();
                predecessorTask.setId(item.getId());
                predecessorTask.setName(item.getName());
                predecessorTask.setLink_type(RelationItem.TYPE_TASK);

                predecessorTasks.add(predecessorTask);
            }
            taskInformation.setPredecessor_tasks(predecessorTasks);
        }
        if (taskItem.getSuccessorTasks() != null && taskItem.getSuccessorTasks().length > 0) {
            ArrayList<LinksTO> successorTasks = new ArrayList<>();
            for (SelectItem item : taskItem.getSuccessorTasks()) {
                LinksTO successorTask = new LinksTO();
                successorTask.setId(item.getId());
                successorTask.setName(item.getName());
                successorTask.setLink_type(RelationItem.TYPE_TASK);

                successorTasks.add(successorTask);
            }
            taskInformation.setSuccessor_tasks(successorTasks);
        }
        crmTaskDetails.setInformation(taskInformation);

        ArrayList<Object> customFields = new ArrayList<>();
        List<FileResource> taskAttachments = attachmentUtilsManager.getAttachments(Constants.F_TASK, taskItem.getProjectID(), taskItem.getObjectID());
        if (taskAttachments != null && !taskAttachments.isEmpty()) {
            ArrayList<AttachmentTO> files = new ArrayList<>();
            CustomFieldsTO customField = new CustomFieldsTO();
            for (FileResource fileResource : taskAttachments) {
                AttachmentTO attachment = new AttachmentTO();
                attachment.setFile_name(fileResource.getFileName());
                attachment.setLink(fileResource.getDownloadUrl());
                files.add(attachment);
            }
            customField.setType(CustomFieldTypeEnum.FILE_UPLOAD.name());

            CustomFieldFileUploadTO fileUpload = new CustomFieldFileUploadTO();
            EdsModelField modelField = modelFieldManager.getByFieldID(LayoutRPC.TASK_MAX_FORM, CustomFormConstants.ATTACHMENTS);
            if (modelField != null) {
                fileUpload.setId(modelField.getObjectID() + GAP_BTW_STATIC_AND_CUSTOM_FIELDS);
                fileUpload.setTitle(modelFieldLocalizer.localizeTask(CustomFormConstants.ATTACHMENTS));
                fileUpload.setFiles(files);
            }
            customField.setObject(fileUpload);
            customFields.add(customField);
        }
        customFields.addAll(getCustomFields(taskItem.getCustomFieldItems()));
        if (customFields.size() > 0) {
            crmTaskDetails.setCustom_fields(customFields);
        }
        crmTaskDetails.setCan_edit(ServerUtils.hasPermission(PermissionConstants.PM_TASKS_EDIT));
        return successResponse(new TaskDetailsInfoResultTO(crmTaskDetails));
    }


    @Operation(summary = "Delete Task", description = "Delete particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests. Server should check if current user has permissions to delete this particular item, and if no give user message: You don't have permissions to delete this entry. Please contact your administrator")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/tasks/{item_id}/delete", method = RequestMethod.DELETE)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_TASKS_LIST, PermissionConstants.PM_TASKS_REMOVE})
    public Object deleteTask(@PathVariable(value = "item_id") Integer item_id) throws RestException {

        if (item_id == null || item_id <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsTask edsTask = taskManager.get(item_id);
        if (edsTask == null || edsTask.getDeleted()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Task with id " + item_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (ServerUtils.hasPermission(PermissionConstants.PM_TASKS_REMOVE)) {
            try {
                taskServiceLocal.deleteTask(item_id, null);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            throw new RestException(commonLocalizer.localize("youDontHavePermission"), commonLocalizer.localize("youDontHavePermission"), ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
        }

        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get tasks by related entity", description = "Get tasks that related to particular entity like Lead, Opportunity, Company, Contact etc. Particular entity is described in path, like other requests.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{id}/tasks", method = RequestMethod.GET)
    @CheckPermission(permissions = {PermissionConstants.PM_MAIN_MENU, PermissionConstants.PM_TASKS_LIST})
    public Object getTaskByRelatedEntity(@PathVariable("main_entity_path") String main_entity_name,
                                         @PathVariable(value = "id") Integer entityId,
                                         @RequestParam(value = "sort_type", required = false) String sort_type,
                                         @RequestParam(value = "direction", required = false) String direction,
                                         @RequestParam(value = "limit", required = false) Integer limit) throws RestException {

        if (StringUtils.isBlank(main_entity_name)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entityId == null || entityId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();

        filterParameter.setStart(0);
        filterParameter.setLimit(limit != null ? limit : MAX_LIMIT);

        //Sort field
        if (StringUtils.isNotBlank(sort_type)) {
            if (OrderFieldEnum.NAME.getField().equalsIgnoreCase(sort_type)) {
                filterParameter.setSortField(TaskListItem.NAME);
            } else if (OrderFieldEnum.DATE.getField().equalsIgnoreCase(sort_type)) {
                filterParameter.setSortField(TaskListItem.DUE_DATE);
            } else {
                filterParameter.setSortField(TaskListItem.ID);
            }
        }

        //Sort direction
        if (StringUtils.isNotBlank(direction)) {
            filterParameter.setAscending(OrderByEnum.ASC.name().equalsIgnoreCase(direction));
            if (OrderByEnum.getDirection(direction) != null) {
                filterParameter.setSortDir(OrderByEnum.getDirection(direction).getId());
            }
        } else {
            filterParameter.setAscending(false);
            filterParameter.setSortDir(OrderByEnum.DESC.getId());
        }

        filterParameter.setRelationID(entityId);
        //Relation Type
        if (EntityTypeEnum.OPPORTUNITIES.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_OPPORTUNITY);
            filterParameter.setCreatedFrom(Appointment.FROM_CRM);
        } else if (EntityTypeEnum.LEADS.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_LEAD);
            filterParameter.setCreatedFrom(Appointment.FROM_CRM);
        } else if (EntityTypeEnum.CONTACTS.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_CONTACT);
            filterParameter.setCreatedFrom(Appointment.FROM_CRM);
        } else if (EntityTypeEnum.COMPANIES.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_ACCOUNT);
            filterParameter.setCreatedFrom(Appointment.FROM_CRM);
        } else if (EntityTypeEnum.ACTIVITIES.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_EVENT);
            filterParameter.setCreatedFrom(Appointment.FROM_CRM);
        } else if (EntityTypeEnum.TASKS.name().equalsIgnoreCase(main_entity_name)) {
            filterParameter.setRelationType(CrmConstants.CRM_TASK);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_name should be one of | leads | opportunities | tasks | companies | contacts | activities", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ListPanelToolRpc panelSettings = ListPanelToolRpc.createIntance();
        panelSettings.setColumnCodeName(new ArrayList<>(Arrays.asList(
                TaskListItem.NAME,
                TaskListItem.DESCRIPTION,
                TaskListItem.STATUS_NAME,
                TaskListItem.PRIORITY_NAME,
                TaskListItem.DUE_DATE)));

        filterParameter.setListPanelTool(panelSettings);

        TaskList result;
        try {
            result = taskServiceLocal.getTaskList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<RelatedTaskTO> taskList = new ArrayList<>();
        for (TaskListItem taskListItem : result.getList()) {
            RelatedTaskTO taskItem = new RelatedTaskTO();
            taskItem.setName(taskListItem.getName());
            taskItem.setItem_id(taskListItem.getObjectID());
            if (StringUtils.isNotBlank(taskListItem.getDescription())) {
                taskItem.setDescription(taskListItem.getDescription());
            }
            taskItem.setPriority(TaskPriorityEnum.get(taskListItem.getPriorityCode()));
            if (taskListItem.getDueDate() != null) {
                taskItem.setDue_date(longDateTimezoneFormat.format(taskListItem.getDueDate()));
            }

            EdsReference taskStatus = referenceManager.get(taskListItem.getTaskStatusId());
            if (taskStatus != null) {
                taskItem.setStatus_id(taskStatus.getObjectID());
                FlowSettingsTO statusItem = new FlowSettingsTO();
                statusItem.setStatus_id(taskStatus.getObjectID());
                statusItem.setStatus_name(taskStatus.getName());
                statusItem.setOrder_id(taskStatus.getSorder());
                statusItem.setIs_system(taskStatus.isSystemReference());
                if (taskStatus.getReferenceColor() != null) {
                    ColorTO color = new ColorTO();
                    color.setId(taskStatus.getReferenceColor().getObjectID());
                    color.setName(taskStatus.getReferenceColor().getName());
                    color.setHex(taskStatus.getReferenceColor().getHex());
                    statusItem.setStatus_color(color);
                }
                taskItem.setStatus(statusItem);
            }

            taskList.add(taskItem);
        }

        return successResponse(new ResponseListData(taskList));
    }

    @Operation(summary = "Get Task Category List", description = "Get Categories of tasks")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the categories list of tasks")})
    @RequestMapping(value = "/tasks/{field_type}/categories", method = RequestMethod.GET)
    public Object getEntityFieldCategories(
            @PathVariable(value = "field_type") String field_type,
            @RequestParam(value = "custom_field_id", required = false) Integer custom_field_id,
            @RequestParam(value = "dependency_id", required = false) Integer dependency_id,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer offset) throws RestException {

        if (StringUtils.isBlank(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (!EntityFieldTypeEnum.PROJECT.name().equals(field_type) && !EntityFieldTypeEnum.PARENT_WORKSTREAM.name().equals(field_type) && !EntityFieldTypeEnum.CUSTOM.name().equals(field_type)
                && !EntityFieldTypeEnum.STATUS.name().equals(field_type)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "field_type should be one of COMPANY | CUSTOM | STATUS | PARENT_WORKSTREAM", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Integer start = (offset != null && offset > 0) ? offset : 0;
        Integer maxLimit = (limit != null && limit > 0) ? limit : MAX_LIMIT;
        EdsUser user = userManager.getUser();
        EntityCategoryTO entityCategories = new EntityCategoryTO();
        ArrayList<CategoryTO> categories = new ArrayList<>();

        if (EntityFieldTypeEnum.PROJECT.name().equals(field_type)) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setCategory(Constants.TASK);
            filterParameter.setCRM(true);

            ArrayList<EdsProject> projects;
            try {
                projects = (ArrayList) projectManager.list(filterParameter, user);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (projects != null) {
                if (StringUtils.isNotBlank(query)) {
                    projects = (ArrayList) projects.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                }
                entityCategories.setTotal_count(projects.size());
                if (projects.size() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(projects.size() - (start + maxLimit));
                }

                ArrayList<EdsProject> subList = ListUtils.getSublistSmart(projects, start, maxLimit);
                entityCategories.setCount(subList.size());
                entityCategories.setOffset(start);
                subList.forEach(edsReference -> {
                    CategoryTO category = new CategoryTO();
                    category.setId(edsReference.getObjectID());
                    category.setTitle(edsReference.getName());
                    categories.add(category);
                });
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.STATUS.name().equals(field_type)) {
            SelectItem[] statuses = commonServiceLocal.getAddTaskStatusDrop();
            if (statuses != null) {
                List<SelectItem> taskStatusList = Arrays.asList(statuses);

                if (StringUtils.isNotBlank(query)) {
                    taskStatusList = taskStatusList.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                }
                entityCategories.setTotal_count(taskStatusList.size());
                if (taskStatusList.size() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(taskStatusList.size() - (start + maxLimit));
                }
                ArrayList<SelectItem> stringArrayList = new ArrayList<>(taskStatusList);
                ArrayList<SelectItem> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                entityCategories.setCount(sublist.size());
                entityCategories.setOffset(start);
                for (SelectItem item : sublist) {
                    if (item != null) {
                        CategoryTO category = new CategoryTO();
                        category.setId(item.getId());
                        category.setTitle(item.getName());
                        categories.add(category);
                    }
                }
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.PARENT_WORKSTREAM.name().equals(field_type)) {
            if (dependency_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "dependency_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            ArrayList<EdsWorkStream> workStreams;
            try {
                workStreams = (ArrayList) workStreamManager.listByProjectId(dependency_id);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (workStreams != null) {
                if (StringUtils.isNotBlank(query)) {
                    workStreams = (ArrayList) workStreams.stream().filter(item -> item.getName().toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                }
                entityCategories.setTotal_count(workStreams.size());
                if (workStreams.size() < (maxLimit + start)) {
                    entityCategories.setLeft(0);
                } else {
                    entityCategories.setLeft(workStreams.size() - (start + maxLimit));
                }

                ArrayList<EdsWorkStream> subList = ListUtils.getSublistSmart(workStreams, start, maxLimit);
                entityCategories.setCount(subList.size());
                entityCategories.setOffset(start);
                subList.forEach(edsReference -> {
                    CategoryTO category = new CategoryTO();
                    category.setId(edsReference.getObjectID());
                    category.setTitle(edsReference.getName());
                    categories.add(category);
                });
                entityCategories.setList(categories);
            }
        } else if (EntityFieldTypeEnum.CUSTOM.name().equals(field_type)) {
            if (custom_field_id == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "custom_field_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (custom_field_id < GAP_BTW_STATIC_AND_CUSTOM_FIELDS) {

                CompanyCustomFieldItem customFieldItem;
                try {
                    customFieldItem = profileServiceLocal.getCustomFieldData(custom_field_id, null);
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                if (customFieldItem == null) {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Options list by custom field id " + custom_field_id + " are not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
                if (Constants.UI_TYPE_LOOKUP.equalsIgnoreCase(customFieldItem.getUiType())) {
                    //if type is lookup
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
                    filterParameter.setSearchKey(query);
                    filterParameter.setStart(start);
                    filterParameter.setLimit(limit);
                    if (CustomFieldLookUpTypeEnum.REFERENCE.equals(customFieldItem.getLookUpTypeEnum())) {
                        filterParameter.setParentID(customFieldItem.getReferenceItem() != null ? customFieldItem.getReferenceItem().getId() : null);
                    }
                    List<CategoryTO> lookupOptions = getCustomFieldLookupValues(filterParameter, customFieldItem).stream().map(selectItem -> {
                        CategoryTO category = new CategoryTO();
                        category.setId(selectItem.getId());
                        category.setCode(selectItem.getCode());
                        category.setTitle(selectItem.getName());
                        return category;
                    }).collect(Collectors.toList());
                    entityCategories.setList(lookupOptions);
                } else {
                    //If other dropdown types
                    List<String> predefinedValuesList = getCustomFieldValue(custom_field_id);
                    if (StringUtils.isNotBlank(query)) {
                        predefinedValuesList = predefinedValuesList.stream().filter(item -> item.toLowerCase().contains(query.toLowerCase())).collect(Collectors.toList());
                    }
                    entityCategories.setTotal_count(predefinedValuesList.size());
                    if (predefinedValuesList.size() < (maxLimit + start)) {
                        entityCategories.setLeft(0);
                    } else {
                        entityCategories.setLeft(predefinedValuesList.size() - (start + maxLimit));
                    }
                    ArrayList<String> stringArrayList = new ArrayList<>(predefinedValuesList);
                    ArrayList<String> sublist = ListUtils.getSublistSmart(stringArrayList, start, maxLimit);
                    entityCategories.setCount(sublist.size());
                    entityCategories.setOffset(start);
                    int id = 0;
                    for (String values : sublist) {
                        if (StringUtils.isNotBlank(values)) {
                            CategoryTO category = new CategoryTO();
                            category.setId(++id);
                            category.setTitle(values);
                            categories.add(category);
                        }
                    }
                    entityCategories.setList(categories);
                }
            }
        }

        return successResponse(entityCategories);
    }


    @RequestMapping(value = "/task/copy-pdf-to-one-drive", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getEntityFieldCategories(
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "offset", required = false) Integer start) {

        EdsUser user = userManager.getUser();
        String companyDate = MessageManagerImpl.defaultShortDateFormat(new Date());
        SolrReindexRpc solrReindex = new SolrReindexRpc();
        solrReindex.setAllReindex(true);
        List<EdsTask> tasks = taskManager.getCompanyTasksForSolr(solrReindex, start, limit);
        Office365AccessTokenDTO tokenDTO = office365AuthService.getUserAccessToken(EdsContextParams.getHost(), Constants.OFFICE_365);
        for (EdsTask edsTask : tasks) {
            ByteArrayOutputStream pdfStream = this.taskViewPDFHandler.getPDFStream(new RequestObject(edsTask.getObjectID()));
            String pdfName = "Task-" + "-" + edsTask.getNumber() + "-" + user.getCompany().getName() + companyDate + ".pdf";
            if (pdfStream != null) {
                ByteArrayInputStream inputStream = null;
                try {
                    inputStream = new ByteArrayInputStream(pdfStream.toByteArray());
                    if ("56895".equals(ServerSecurityContext.getInstance().getCompanyId())) {
                        if (tokenDTO != null) {
                            this.createUpload(inputStream, pdfName, "application/pdf", Constants.OFFICE_365);
                        }
                    }
                    inputStream.close();
                    pdfStream.flush();
                    pdfStream.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                        pdfStream.flush();
                        pdfStream.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return successResponse(new ResponseData());
    }

    @Transactional
    public EdsUpload createUpload(final InputStream inputStream, final String originalName, final String contentType, String storage) {
        final EdsUpload upload = new EdsUpload();
        upload.setContentType(contentType);
        upload.setOriginalName(originalName);
        upload.setType(this.referenceManager.findReference(Constants._UPLOAD_TYPE, storage));
        upload.setInputStream(inputStream);
        try {
            uploadManager.create(upload);
            System.out.print("****************File Uploaded******************");
        } catch (final Exception ex) {
            System.err.println("****************Failed to Upload File******************");
        }
        return upload;
    }

}
