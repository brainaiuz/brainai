package com.edatasite.workforce.rest.v1.release10.pm;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.NewClientList;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ModuleServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.ListingFilterHelper;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.TaskMiniTO;
import com.edatasite.workforce.rest.base.to.TimesheetEntryTO;
import com.edatasite.workforce.rest.base.to.TimesheetRowItemTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 02.02.15.
 */
@Tag(name = "Timesheet Plugin", description = "Timesheet Plugin API")
@RestController
@RequestMapping(value = "/timesheet/plugin", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTimesheetPluginControllerV1 extends BaseApiControllerV1 {

    private static final Logger log = LoggerFactory.getLogger(ApiTimesheetPluginControllerV1.class);
    @Autowired
    private ClientServiceLocal clientServiceLocal;
    @Autowired
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleServiceLocal moduleServiceLocal;
    @Autowired
    private HttpServletRequest servletRequest;

    @RequestMapping(value = "/permission/{userId}", method = RequestMethod.GET)
    public Object getTimesheetPermission(@PathVariable(value = "userId") Integer userId) {
        boolean hasPermission = ServerUtils.hasPermission(PermissionConstants.TIMESHEET_PLUGIN);
        boolean hasModuleEnabled = moduleServiceLocal.hasEnabled(PermissionConstants.TIMESHEET_PLUGIN);
        if (hasPermission && hasModuleEnabled) {
            return successResponse(ACCESS_PERMITTED, "ACCESS_PERMITTED", HttpServletResponse.SC_ACCEPTED);
        } else {
            log.info("User has no permission to timesheet plugin : {}", userId);
            return errorResponse(ACCESS_DENIED, "ACCESS_DENIED", HttpServletResponse.SC_NOT_ACCEPTABLE);
        }
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getClientList(@RequestBody MListingFilterParameter filterParameter) {
        ArrayList<SelectItemTO> result = new ArrayList<>();
        NewClientList clientListResult = clientServiceLocal.getNewClients(filterParameter.convertToFilterParameters());
        if (clientListResult.getList().isEmpty()) {
            log.info("Clients list are empty");
            return successResponse(result);
        }
        for (CrmAccountItem item : clientListResult.getList()) {
            SelectItemTO client = new SelectItemTO();
            client.setId(item.getObjectId());
            client.setName(item.getName());
            client.setCode(item.getNumber());
            result.add(client);
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/projects/{clientId}/{date}", method = RequestMethod.GET)
    public Object getProjectListByClient(@PathVariable(value = "clientId") Integer clientId,
                                         @PathVariable(value = "date") Long date) {
        ArrayList<SelectItemTO> result = new ArrayList<>();
        ListingFilterParameter filter = ListingFilterHelper.createFilterParameter(servletRequest, ListPanelType.ProjectListPanel);
        filter.setClientId(clientId);
        filter.setLimit(10000);
        filter.setListPanelTool(new ListPanelToolRpc());
        filter.getListPanelTool().setColumnCodeName(ListingFilterHelper.getProjectColumnNames());
        ListResult<ProjectListItem> projectList = projectServiceLocal.getProjectList(filter);
        if (projectList.getList().size() > 0) {
            for (ProjectListItem item : projectList.getList()) {
                SelectItemTO project = new SelectItemTO();
                project.setId(item.getObjectId());
                project.setName(item.getName());
                project.setCode(item.getNumber());
                result.add(project);
            }
        } else {
            log.info("Projects list By Client - " + clientId + " are empty");
        }

        return successResponse(result);
    }

    @RequestMapping(value = "/tasks/{projectId}/{date}", method = RequestMethod.GET)
    public Object getTaskListByProject(@PathVariable(value = "projectId") Integer projectId,
                                       @PathVariable(value = "date") Long date) {

        ArrayList<TaskMiniTO> result = new ArrayList<>();
        ListingFilterParameter filter = new ListingFilterParameter();
        filter = ListingFilterHelper.fillFilterParameter(filter, servletRequest, ListPanelType.TaskListPanel);
        filter.setCrmTaskList(false);
        filter.setProjectId(projectId);
        filter.setLimit(10000);

        if (filter.getListPanelTool() == null) {
            filter.setListPanelTool(new ListPanelToolRpc());
        }

        filter.getListPanelTool().setColumnCodeName(ListingFilterHelper.getTaskColumnNames());

        TaskList taskList = taskServiceLocal.getTaskList(filter);
        if (taskList.getList().size() > 0) {
            for (TaskListItem item : taskList.getList()) {
                TaskMiniTO task = new TaskMiniTO();
                task.setId(item.getObjectID());
                task.setName(item.getName());
                task.setNumber(item.getNumber());
                task.setDescription(item.getDescription());
                result.add(task);
            }
        } else {
            log.info("Task list By Project - " + projectId + " are empty");
        }

        return successResponse(result);
    }

    @RequestMapping(value = "/{date}", method = RequestMethod.GET)
    public Object getTimesheetRowData(@PathVariable(value = "date") Long date,
                                      @RequestParam(value = "taskIds") String taskIds) {
        return employeeTaskManager.getEmployeesAndTasks(taskIds, WrapUtils.longToDate(date));

    }

    @RequestMapping(value = "/{date}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveTimesheetRowData(@RequestBody ArrayList<TimesheetRowItemTO> timesheetRowItems,
                                       @PathVariable(value = "date") Long date) {

        EdsReference availableStatus = referenceManager.findReference(Constants.TIME_TRACK_STATUS, Constants.AVAILABLE);
        boolean fingerprintEnabled = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED);
        boolean isCustomFingerPrint = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FOR_CUSTOM_FINGER_PRINT);
        for (TimesheetRowItemTO rowItem : timesheetRowItems) {
            if (rowItem.getEmployee() == null) {
                continue;
            }
            //save task timesheet
            if (rowItem.getTasks() != null && rowItem.getTasks().size() > 0) {
                for (TaskMiniTO task : rowItem.getTasks()) {
                    if (task.getTimesheetEntry() != null) {
                        TimesheetEntryTO timesheetEntry = task.getTimesheetEntry();
                        TimesheetDataItem item = new TimesheetDataItem();
                        item.setEmployeeID(rowItem.getEmployee().getId());
                        item.setDate(new Date(date));
                        item.setTaskID(task.getId());
                        item.setId(timesheetEntry.getId());
                        item.setMinutes(timesheetEntry.getMinutes());
                        item.setComment(timesheetEntry.getComment());
                        item.setReference(timesheetEntry.getReference());
                        try {
                            taskServiceLocal.setTimeToTimesheet(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return errorResponse(ERROR_FAILED_SAVE);
                        }
                    }
                }
            }
            //save attendance
            if (rowItem.getEmployee().getAttendance() != null) {
                Date startTime = WrapUtils.longToDate(rowItem.getEmployee().getAttendance().getStartTime());
                Date endTime = WrapUtils.longToDate(rowItem.getEmployee().getAttendance().getEndTime());
                if (startTime != null && endTime != null) {
                    try {
                        commonServiceLocal.saveEmployeePresentTimeFromAPI(rowItem.getEmployee().getAttendance().getEmployeeId(), startTime, endTime, availableStatus.getObjectID(), fingerprintEnabled, isCustomFingerPrint);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return errorResponse(ERROR_FAILED_SAVE);
                    }
                }
            }
        }
        return successResponse(SUCCESS_SAVE);
    }
}
