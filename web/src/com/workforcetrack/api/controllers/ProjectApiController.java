package com.workforcetrack.api.controllers;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.workforcetrack.api.aspects.CheckRequest;
import com.workforcetrack.api.base.APIConstants;
import com.workforcetrack.api.base.APISelectItemList;
import com.workforcetrack.api.base.RestServiceUtils;
import com.workforcetrack.api.exceptions.ApiExceptions;
import com.workforcetrack.api.exceptions.BaseApiException;
import com.workforcetrack.api.presenter.BaseApiPresenter;
import com.workforcetrack.api.presenter.ProjectApiPresenter;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.project.MClientList;
import com.workforcetrack.mobile.services.ProjectWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sancho
 * Date: 18.05.12
 * Time: 17:52
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping(value = "/project")
public class ProjectApiController {

    public static final String OBJECT_ID = "objectID";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String DESCRIPTION = "description";
    public static final String CLIENT = "client";
    public static final String CLIENT_ID = "clientID";
    public static final String LAST_MODIFIED_BY = "lastModifiedBy";
    public static final String LAST_MODIFIED = "lastModified";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    public static final String STATUS_NAME = "statusName";
    public static final String STATUS_ID = "statusID";
    public static final String COMPLETE = "complete";
    public static final String PERMISSION = "permission";
    public static final String MANAGER_ID = "managerID";
    public static final String MANAGER_NAME = "managerName";
    public static final String BACKUP_MANAGER_ID = "backupManagerID";
    public static final String BACKUP_MANAGER_IDS = "backupManagerIDs";
    public static final String BACKUP_MANAGER_NAME = "backupManagerName";

    public static final String PROJECT_MEMBERS = "projectMembers";
    public static final String DUE_DATE = "dueDate";

    public static final String TASK_COUNT = "taskCount";

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ProjectWebService projectWebService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RestServiceUtils restServiceUtils;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private CommonService commonService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/assignees/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getAssignees(@PathVariable Integer Id) throws BaseApiException {
        try {
            if (Id != null && Id > 0) {
                PositionsSelectItem[] positionsSelectItems = taskService.getAssigneesWithPositions1(Id);
                return new APISelectItemList(positionsSelectItems);
            }
            throw ApiExceptions.PARAMS_INCORRECT;
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
            /* MProjectListItem item = new MProjectListItem(saveDataMap);
         Integer saveResult = projectWebService.save(item);*/

            Integer objectId = (Integer) saveDataMap.get(BaseApiPresenter.OBJECT_ID);
            ProjectApiPresenter presenter = new ProjectApiPresenter();

            if (objectId != null && objectId != 0) {
                EditProject editProject = projectService.getProjectForEdit(objectId, null, null);
                if (presenter.convertToEditProject(saveDataMap, editProject)) {
                    projectService.updateProject(editProject);
                }
            } else {
                ProjectSingleItem projectSingleItem = presenter.convertToProjectSingleItem(saveDataMap);
                projectSingleItem.setNumberData(projectService.generateProjectNumber(projectSingleItem.getStartDate(), projectSingleItem.getClientId(), null));
                objectId = projectService.saveProject(projectSingleItem);
            }
            return objectId;
        } catch (ParseException | ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }

    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.DELETE, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object delete(@PathVariable Integer Id) throws BaseApiException {
        try {
            return projectWebService.delete(Id);
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        }
    }

    @CheckRequest
    @ResponseBody
    @RequestMapping(value = "/employees", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    public Object getProjectEmployeesWithTeams() throws BaseApiException {
        try {
            ProjectMember[] members = employeeService.getProjectEmployeesWithTeams(null);
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put(BaseApiPresenter.TOTAL_COUNT, members != null ? members.length : 0);
            resultMap.put(BaseApiPresenter.ITEMS, members != null ? members : "");
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getStatusList() throws BaseApiException {
        try {
            SelectItem[] selectItems = projectService.getProjectStatuses();
            return new APISelectItemList(selectItems);
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object getClients(@RequestBody Map<String, Object> params) throws BaseApiException {
        try {
            MFilterParametrs fp = restServiceUtils.getMFilterParameter(params);
            if (fp == null) {
                throw ApiExceptions.PARAMS_INCORRECT;
            }
            Integer objectID = (Integer) params.get(APIConstants.OBJECT_ID);
            MClientList selectItems = projectWebService.getClients(fp, objectID);
            return new APISelectItemList(selectItems.getClientListItem());
        } catch (Exception e) {
            e.printStackTrace();
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object searchProject(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                @RequestParam(value = "rows", required = false, defaultValue = "15") int rows,
                                @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey) throws BaseApiException {
        ListResult<ProjectListItem> projectList = new ListResult<>();
        try {
            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setStart(page);
            fp.setLimit(rows);

            if (!"".equals(searchKey)) {
                return searchByKey(searchKey, page, rows);
            }

            projectList = projectService.getProjectList(fp);

            ProjectApiPresenter presenter = new ProjectApiPresenter();
            return presenter.convertToMapListing(projectList);

            /* if (request.getParameterMap().size() > 0 && (request.getParameter("managerId") != null || request.getParameter("clientId") != null || request.getParameter("statusId") != null)) {

                FacetFilterRpc ffRpc = new FacetFilterRpc(this.getColumnCodes(FacetContentType.ProjectFacetFilter), this.getProjectSolrFields());
                ffRpc.setType(ListPanelType.ProjectListPanel);

                List<SelectItem> facetItems = new ArrayList<SelectItem>();
                if (request.getParameter("managerId") != null) {
                    facetItems.add(new SelectItem(Integer.valueOf(request.getParameter("managerId")), null));
                    ffRpc.getFacetContentMap().get(FacetContentType.ProjectFacetFilter.getContentCode()[0]).setFacetItems(facetItems.toArray(new SelectItem[]{}));
                }
                if (request.getParameter("clientId") != null) {
                    facetItems.add(new SelectItem(Integer.valueOf(request.getParameter("clientId")), null));
                    ffRpc.getFacetContentMap().get(FacetContentType.ProjectFacetFilter.getContentCode()[1]).setFacetItems(facetItems.toArray(new SelectItem[]{}));
                }
                if (request.getParameter("statusId") != null) {
                    facetItems.add(new SelectItem(Integer.valueOf(request.getParameter("statusId")), null));
                    ffRpc.getFacetContentMap().get(FacetContentType.ProjectFacetFilter.getContentCode()[2]).setFacetItems(facetItems.toArray(new SelectItem[]{}));
                }
                fp.setFacetFilter(ffRpc);
            }*/

        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    @RequestMapping(value = "/{Id}", method = RequestMethod.GET, headers = APIConstants.ACCEPT_APPLICATION_JSON)
    @CheckRequest
    @ResponseBody
    public Object searchProject(@PathVariable Integer Id) throws BaseApiException {
        try {
            ProjectApiPresenter presenter = new ProjectApiPresenter();

            EditProject searchResult = projectService.getProjectForEdit(Id, null, null);
            ProjectMember[] members = projectService.getProjectEmployees(Id);
            searchResult.setMembers(members);
            Map<String, Object> resultMap = presenter.convertToMapItem(searchResult);

            SelectItem[] statusList = projectService.getProjectStatuses();

            ListingFilterParameter fp = new ListingFilterParameter();
            fp.setProjectId(Id);
            fp.setAccountType(CrmAccountItem.CUSTOMER);
            fp.setSearchByParent(false);
            ArrayList<SelectItem> clientList = allInOneService.getCrmAccountAsSelectItem(CrmConstants.CRM_ACCOUNT_ID, fp).getList();

            if (statusList != null) {
                resultMap.put(BaseApiPresenter.STATUS_LIST, BaseApiPresenter.toMSelectItemList(statusList));
            }
            if (clientList != null) {
                resultMap.put(BaseApiPresenter.CLIENT_List, BaseApiPresenter.toMSelectItemList(clientList));
            }
            return resultMap;
        } catch (ClassCastException e) {
            throw ApiExceptions.PARAMS_INCORRECT;
        } catch (Exception e) {
            throw ApiExceptions.RUNTIME_EXCEPTION_BASE;
        }
    }

    private Object searchByKey(String searchKey, int start, int limit) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(start);
        fp.setLimit(limit);
        fp.setSearchKey(searchKey);

        ListResult<ProjectListItem> searchResult = projectService.getProjectList(fp);
        ProjectApiPresenter presenter = new ProjectApiPresenter();
        return presenter.convertToMapListing(searchResult);
    }

    public static ArrayList<String> getColumnCodes(FacetContentType contentType) {
        ArrayList<String> columnCodes = new ArrayList<>(Arrays.asList(contentType.getContentCode()));
        return columnCodes;
    }

    private HashMap<String, FacetSolrField> getProjectSolrFields() {
        FacetSolrField solrField = null;
        HashMap<String, FacetSolrField> resultSolrField = new HashMap<>();
        String[] contentCodes = FacetContentType.ProjectFacetFilter.getContentCode();

        solrField = new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID, SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID_NAME, LocalizationType.REFERENCE);
        resultSolrField.put(contentCodes[0], solrField);
        solrField = new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID, SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID_NAME, LocalizationType.REFERENCE);
        resultSolrField.put(contentCodes[1], solrField);
        solrField = new FacetSolrField(SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID, SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID_CODE, LocalizationType.REFERENCE);
        solrField.setLocalizationType(LocalizationType.REFERENCE);
        resultSolrField.put(contentCodes[2], solrField);
        return resultSolrField;
    }
}

