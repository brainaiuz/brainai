package com.edatasite.workforce.rest.v1.release10.pm;

import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.TaskAssigneeTO;
import com.edatasite.workforce.rest.base.to.TaskTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Umidbek.
 */
@Tag(name = "Task", description = "Task API")
@RestController
@RequestMapping(value = "/task", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiTaskControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filter = mListingFilterParameter.convertToFilterParameters();
        TaskList taskList = taskServiceLocal.getTaskList(filter);
        List<TaskListItem> list = taskList.getList();
        ArrayList<TaskTO> taskTOs = new ArrayList<>();

        for (TaskListItem item : list) {
            taskTOs.add(new TaskTO(item));
        }

        return successResponse(new ListResultTO<>(taskList.getTotal(), taskTOs));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        TaskSingleItem item = taskServiceLocal.getTask(id, false);
        if (item == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        return successResponse(new TaskTO(item));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody TaskTO task) {
        return this.saveTask(task, new TaskSingleItem(), null);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id, @RequestBody TaskTO task) {
        return this.saveTask(task, taskServiceLocal.getTask(id, false), id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {

        String result = taskServiceLocal.deleteTask(id, PermissionConstants.PM_CONTEXT);
        if (Constants.USED_IN_INVOICE.equals(result)) {
            return errorResponse("You cannot delete invoiced task!");
        }
        return successResponse(SUCCESS_DELETE);
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getProjects(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setPM(true);
        return successResponse(WrapUtils.wrapSelectItemTOs(projectServiceLocal.getLookUpItems(filterParameter, CrmConstants.PM_PROJECT_ID)));
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public Object getStatusList() {
        return successResponse(WrapUtils.wrapSelectItemList(commonServiceLocal.getAddTaskStatusDrop()));
    }

    @RequestMapping(value = "/priorities", method = RequestMethod.GET)
    public Object getPriorities() {
        return successResponse(WrapUtils.wrapSelectItemTOs(taskServiceLocal.getPriorities()));
    }

    @RequestMapping(value = "/{projectId}/assignees/projectEmployees", method = RequestMethod.GET)
    public Object getProjectEmployees(@PathVariable(value = "projectId") Integer projectId) {
        return getAssignees(projectId, null);
    }

    @RequestMapping(value = "/{projectId}/assignees/availableEmployees", method = RequestMethod.GET)
    public Object getAvailableEmployees(@PathVariable(value = "projectId") Integer projectId) {
        return getAssignees(projectId, true);
    }

    @RequestMapping(value = "/{projectId}/assignees/allEmployees", method = RequestMethod.GET)
    public Object getAllEmployees(@PathVariable(value = "projectId") Integer projectId) {
        return getAssignees(projectId, false);
    }

    @RequestMapping(value = "/{id}/assignees", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object saveAssignees(@PathVariable(value = "id") Integer id, @RequestBody List<TaskAssigneeTO> data) {

        EditTask task = taskServiceLocal.getTaskForEdit(id);

        if (task == null) {
            return this.errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        ArrayList<IdTime> idTimeList = new ArrayList<>();

        for (TaskAssigneeTO to : data) {
            IdTime time = new IdTime();

            try {
                time.setId(to.getId());
                time.setTime(to.getEstimatedTime());
                time.setStatusId(to.getStatus().getId());

                if (to.getPercentCompleted() == null) {
                    time.setPercent(0F);
                } else {
                    time.setPercent(to.getPercentCompleted().floatValue());
                }

            } catch (Exception e) {
                return this.errorResponse(ERROR_INVALID_BODY_PARAM);
            }

            idTimeList.add(time);
        }

        task.setAssigneeItems(idTimeList.toArray(new IdTime[0]));

        try {
            taskServiceLocal.updateTask(task);
            return successResponse(SUCCESS_UPDATE);
        } catch (NumberExistingException e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_UPDATE);
        }
    }


    private Object saveTask(TaskTO task, TaskSingleItem newTask, Integer id) {

        if (newTask == null) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        if (task == null || task.getProject() == null || StringUtil.isEmpty(task.getName())) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        if (id != null && (!id.equals(task.getId()) || !id.equals(newTask.getObjectID()))) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        newTask.setName(task.getName());
        newTask.setProjectID(task.getProject().getId());

        newTask.setBillable(task.getBillable() != null ? task.getBillable() : false);

        if (task.getDescription() != null) {
            newTask.setDescription(task.getDescription());
        }

        if (task.getStartDate() == null) {
            if (newTask.getStartDate() == null) {
                newTask.setStartDate(new Date());
            }
        } else {
            newTask.setStartDate(new Date(task.getStartDate()));
        }

        if (task.getEndDate() == null) {
            if (newTask.getDueDate() == null) {
                newTask.setDueDate(new Date());
            }
        } else {
            newTask.setDueDate(new Date(task.getEndDate()));
        }

        if (newTask.getNumberData() == null) {
            newTask.setNumberData(taskServiceLocal.generateTaskNumber(task.getProject().getId(), newTask.getStartDate(), null));
        }

        if (task.getStatus() != null) {
            newTask.setStatusID(task.getStatus().getId());
        }

        if (task.getPriority() != null) {
            newTask.setPriorityID(task.getPriority().getId());
            newTask.setPriorityName(task.getPriority().getName());
        }

        if (task.getAssignees() != null) {
            ArrayList<IdTime> idTimes = new ArrayList<>();
            for (TaskAssigneeTO assigneeTO : task.getAssignees()) {
                IdTime idTime = new IdTime();
                idTime.setId(assigneeTO.getId());
                idTime.setTime(assigneeTO.getEstimatedTime());
                idTimes.add(idTime);
            }
            newTask.setProjectEmployees(idTimes.toArray(new IdTime[0]));
        }
        newTask.setCustomFieldItems(convertCustomFields(task.getCustomFields(), Collections.emptyMap()));
        try {
            Integer[] integers = taskServiceLocal.saveTask(newTask);
            return id == null ? successResponse(SUCCESS_SAVE, integers != null && integers.length > 1 ? integers[1] : "") : successResponse(SUCCESS_UPDATE);
        } catch (NumberExistingException e) {
            return errorResponse(ERROR_FAILED_SAVE);
        }

    }


    private Object getAssignees(Integer projectId, Boolean available) {
        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneesMap = null;
        if (available == null) {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMapWithParams(null, projectId, null, false);
        } else if (available) {
            assigneesMap = taskServiceLocal.getAssigneeListOnlyAvailableEmployees(null, projectId, null, null);
        } else {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMap(null);
        }

        ArrayList<TaskAssigneeTO> assigneeTOs = new ArrayList<>();

        for (ArrayList<KpiTreeInfo> list : assigneesMap.values()) {
            for (KpiTreeInfo info : list) {
                assigneeTOs.add(new TaskAssigneeTO(info));
            }
        }
        return successResponse(assigneeTOs);
    }

}
