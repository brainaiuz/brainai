package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.APISelectItemList;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.TaskApiPresenter;
import com.workforcetrack.mobile.rpc.calendar.MTaskList;
import com.workforcetrack.mobile.rpc.calendar.MTaskListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.services.TaskWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 01.05.12
 * Time: 17:12
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/task")
public class TaskApiController {

    public static final String OBJECT_ID = "objectID";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String DESCRIPTION = "description";
    public static final String STATUS_NAME = "statusName";
    //public static final String STATUS_ID = "statusId";
    public static final String PRIORITY_NAME = "priorityName";
    public static final String PROJECT_NAME = "projectName";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String ESTIMATED = "estimated";
    public static final String ASSIGNED_TO = "assignedTo";
    public static final String COMPLETE = "complete";
    public static final String HOUR_SPENT = "hoursSpent";
    public static final String DUE_DATE = "dueDate";
    public static final String CLIENT = "client";
    public static final String BILLABLE = "billable";
    public static final String ALL_DAY = "allDay";
    public static final String PROJECT_MANAGER_ID = "projectManagerID";
    public static final String PROJECT_BACKUP_MANAGER_ID = "projectBackupManagerID";
    public static final String PROJECT_BACKUP_MANAGER_IDS = "projectBackupManagerIDs";

    public static final String PRIORITY_ID = "priorityID";
    public static final String STATUS_ID = "statusID";
    public static final String PROJECT_ID = "projectID";
    public static final String PERCENT = "percent";
    public static final String ASSIGNEE= "assignee";

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskWebService taskWebService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private RestServiceUtils restServiceUtils;
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;

    @RequestMapping(value = "/list", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getList(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            MFilterParametrs fp = restServiceUtils.getMFilterParameter(params);
            if (fp == null) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }

            MTaskList resultList = taskWebService.getList(fp);
            return resultList.getAsMap();
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object get(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Integer objectID = (Integer) params.get(BaseApiPresenter.OBJECT_ID);
            if (objectID == null || objectID <= 0) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            return taskWebService.get(objectID);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object save(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Map<String, Object> saveDataMap = (Map<String, Object>) params.get(APIConstants.SAVE_DATA);
            if (saveDataMap == null || saveDataMap.isEmpty()) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            MTaskListItem item = new MTaskListItem(saveDataMap);
            return taskWebService.save(item);
        } catch (ParseException | ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object delete(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            Integer objectID = (Integer) params.get(BaseApiPresenter.OBJECT_ID);
            if (objectID == null || objectID.equals(0)) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            return taskWebService.delete(objectID);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        }
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getStatusList() throws BaseApiException {
        try {
            SelectItem[] selectItems = commonService.getAddTaskStatusDrop();
            return new APISelectItemList(selectItems);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }

    @RequestMapping(value = "/priorities", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getPriorities() throws BaseApiException {
        try {
            SelectItem[] priorities = taskService.getPriorities();
            return new APISelectItemList(priorities);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/projects", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getProjects() throws BaseApiException {
        try {
            ProjectItem[] projectItems = commonService.getProjects(false);
            return new APISelectItemList(projectItems);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }


    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/overdue", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getOverdueTasks() throws BaseApiException {
        try {
            ArrayList<Appointment> overDueTasks = googleCalendarServiceLocal.getUserOverdueTasks();
            TaskApiPresenter presenter = new TaskApiPresenter();
            return presenter.convertToMap(overDueTasks);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }


    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                         @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                         @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey) throws BaseApiException {
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);
            fp.setSearchKey(searchKey);
			fp.setFromMobile(true);
            TaskList searchResult = taskService.getTaskList(fp);

            TaskApiPresenter presenter = new TaskApiPresenter();
            return presenter.convertToMapListing(searchResult.getList());
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object search(@PathVariable Integer Id) throws BaseApiException {
        try {

            EditTask searchResult = taskService.getTaskForEdit(Id);

            TaskApiPresenter presenter = new TaskApiPresenter();
            return presenter.convertToMapItem(searchResult);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }
}
