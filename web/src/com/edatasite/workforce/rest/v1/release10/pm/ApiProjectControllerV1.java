package com.edatasite.workforce.rest.v1.release10.pm;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.edatasite.workforce.gwt.project.server.actions.ProjectServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.ProjectAssigneeTO;
import com.edatasite.workforce.rest.base.to.ProjectTO;
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
 * Created by Umidbek on 17.02.2015.
 */
@Tag(name = "Project", description = "Project API")
@RestController
@RequestMapping(value = "/project", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiProjectControllerV1 extends BaseApiControllerV1 {

    @Autowired
    private ProjectServiceLocal projectServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private ReportService reportService;

    @RequestMapping(value = "/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        ListResult<ProjectListItem> projectList = projectServiceLocal.getProjectList(filterParameter);
        List<ProjectListItem> list = projectList.getList();

        ArrayList<ProjectTO> projectTOs = new ArrayList<>();
        if (list != null) {
            for (ProjectListItem item : list) {
                projectTOs.add(new ProjectTO(item, false));
            }
        }
        return successResponse(new ListResultTO<>(projectList.getTotal(), projectTOs));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) {
        ProjectViewItem viewItem = projectServiceLocal.viewProject(id);
        if (viewItem == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        return successResponse(new ProjectTO(viewItem));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody ProjectTO projectTO) {
        EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();

        if (user == null) {
            return errorResponse(REQUEST_USER_NOT_AUTHORIZED);
        }

        if (projectTO == null || StringUtil.isEmpty(projectTO.getName())) {
            return errorResponse(ERROR_INVALID_BODY_PARAM);
        }

        Long now = new Date().getTime();

        if (projectTO.getStartDate() == null) {
            projectTO.setStartDate(now);
        }

        if (projectTO.getEndDate() == null) {
            projectTO.setEndDate(now);
        }

        ProjectSingleItem item = new ProjectSingleItem();

        item.setName(projectTO.getName());
        item.setDescription(projectTO.getDescription());
        item.setStartDate(new Date(projectTO.getStartDate()));
        item.setEndDate(new Date(projectTO.getEndDate()));
        item.setBillable(projectTO.getBillable() != null ? projectTO.getBillable() : false);

        if (projectTO.getClient() != null && projectTO.getClient().getId() != null) {
            item.setClientId(projectTO.getClient().getId());
        }
        if (projectTO.getParentId() != null) {
            item.setParentId(projectTO.getParentId());
        }
        if (projectTO.getStatus() != null && projectTO.getStatus().getId() != null) {
            item.setStatusId(projectTO.getStatus().getId());
        }
        if (projectTO.getLocation() != null) {
            item.setLocationId(projectTO.getLocation().getId());
        }

        if (projectTO.getProjectAssignees() != null && projectTO.getProjectAssignees().size() > 0) {
            ArrayList<ProjectMember> members = new ArrayList<>();
            for (ProjectAssigneeTO assigneeTO : projectTO.getProjectAssignees()) {
                if (assigneeTO.getEmployee() != null && assigneeTO.getEmployee().getId() != null) {
                    ProjectMember member = new ProjectMember();
                    member.setId(assigneeTO.getEmployee().getId());
                    member.setWageRate(assigneeTO.getWageRate());
                    member.setClientChargeRate(assigneeTO.getClientChargeRate());
                    member.setWorkloadPercentage(WrapUtils.getFloat(assigneeTO.getWorkloadPercentage()));
                    members.add(member);
                }
            }
            item.setProjectMembers(members.toArray(new ProjectMember[0]));
        }

        if (projectTO.getManager().getId() != null) {
            item.setManagerId(projectTO.getManager().getId());
        } else {
            item.setManagerId(user.getObjectID());
        }

        NumberData numberData = projectServiceLocal.generateProjectNumber(new Date(), item.getClientId(), null);
        item.setNumberData(numberData);

        //Start Of Custom Fields
        item.setCustomFieldItems(convertCustomFields(projectTO.getCustomFields(), Collections.emptyMap()));
        //End Of Custom Fields

        Integer projectId = null;

        try {
            projectId = projectServiceLocal.saveProject(item);
        } catch (NumberExistingException ignored) {
        }

        if (projectId == null) {
            return errorResponse(ERROR_FAILED_SAVE);
        }

        return successResponse(SUCCESS_SAVE, projectId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "id") Integer id,
                         @RequestBody ProjectTO projectTO) {

        EditProject editProject = projectServiceLocal.getProjectForEdit(id, null, null);

        if (editProject == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        Long now = new Date().getTime();

        if (projectTO.getStartDate() == null) {
            projectTO.setStartDate(now);
        }

        if (projectTO.getEndDate() == null) {
            projectTO.setEndDate(now);
        }

        editProject.setName(projectTO.getName());
        editProject.setDescription(projectTO.getDescription());
        editProject.setStartDate(new Date(projectTO.getStartDate()));
        editProject.setDueDate(new Date(projectTO.getEndDate()));
        editProject.setEndDate(new Date(projectTO.getEndDate()));

        if (projectTO.getClient() != null) {
            editProject.setClientId(projectTO.getClient().getId());
        }

        if (projectTO.getStatus() != null) {
            editProject.setStatusId(projectTO.getStatus().getId());
        }
        if (projectTO.getLocation() != null) {
            editProject.setLocationId(projectTO.getLocation().getId());
        }

        if (projectTO.getProjectAssignees() != null) {
            ArrayList<ProjectMember> members = new ArrayList<>();
            for (ProjectAssigneeTO assignee : projectTO.getProjectAssignees()) {
                if (assignee.getEmployee() != null) {
                    ProjectMember member = new ProjectMember();
                    member.setId(assignee.getEmployee().getId());
                    member.setWageRate(assignee.getWageRate());
                    member.setClientChargeRate(assignee.getClientChargeRate());
                    member.setWorkloadPercentage(WrapUtils.getFloat(assignee.getWorkloadPercentage()));

                    members.add(member);
                }
            }
            editProject.setMembers(members.toArray(new ProjectMember[0]));
        }
        editProject.setCustomFieldItems(convertCustomFields(projectTO.getCustomFields(), Collections.emptyMap()));

        try {
            projectServiceLocal.updateProject(editProject);
        } catch (NumberExistingException ignored) {
            return errorResponse(ERROR_FAILED_UPDATE);
        }

        return successResponse(SUCCESS_UPDATE, id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) {
        try {
            projectServiceLocal.deleteProject(id);
            return successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }

    @RequestMapping(value = "/clients", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getClients(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        return successResponse(WrapUtils.wrapSelectItemTOs(projectServiceLocal.searchClientsByProjectId(null, filterParameter.getSearchKey())));
    }

    @RequestMapping(value = "/statuses", method = RequestMethod.GET)
    public Object getStatusList() {
        return successResponse(WrapUtils.wrapSelectItemTOs(projectServiceLocal.getProjectStatuses()));
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public Object getLocations() {
        return successResponse(WrapUtils.wrapSelectItemTOs(reportService.getLocationList()));
    }

    @RequestMapping(value = "/assignees", method = RequestMethod.GET)
    public Object getAssignees() {
        return getAssigneeEmployees(null, false);
    }

    @RequestMapping(value = "/{id}/assignees", method = RequestMethod.GET)
    public Object getProjectAssignees(@PathVariable(value = "id") Integer id) {
        return getAssigneeEmployees(id, true);
    }

    private Object getAssigneeEmployees(Integer id, boolean checkForId) {
        if (id == null && checkForId) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> employees = employeeServiceLocal.getProjectEmployeesForAddEdit(id, true);

        if (employees == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        ArrayList<ProjectAssigneeTO> projectAssigneeTOs = new ArrayList<>();

        for (ArrayList<KpiTreeInfo> projectAssignees : employees.values()) {
            for (KpiTreeInfo projectAssignee : projectAssignees) {
                if (projectAssignee.isSelected()) {
                    projectAssigneeTOs.add(new ProjectAssigneeTO(projectAssignee));
                }
            }
        }

        return successResponse(projectAssigneeTOs);
    }

    @RequestMapping(value = "/{id}/assignees", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object saveAssignees(@PathVariable(value = "id") Integer id,
                                @RequestBody ArrayList<ProjectAssigneeTO> projectAssigneeTOs) {
        EditProject editProject = projectServiceLocal.getProjectForEdit(id, null, null);

        if (editProject == null) {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }

        ArrayList<ProjectMember> members = new ArrayList<>();

        for (ProjectAssigneeTO to : projectAssigneeTOs) {
            ProjectMember member = new ProjectMember();

            if (to.getEmployee() == null || to.getEmployee().getId() == null) {
                return errorResponse(ERROR_INVALID_BODY_PARAM);
            }

            member.setId(to.getEmployee().getId());
            member.setWageRate(to.getWageRate());
            member.setClientChargeRate(to.getClientChargeRate());
            member.setWorkloadPercentage(WrapUtils.getFloat(to.getWorkloadPercentage()));

            members.add(member);
        }

        editProject.setMembers(members.toArray(new ProjectMember[0]));

        try {
            projectServiceLocal.updateProject(editProject);
            return successResponse(SUCCESS_SAVE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAILED_SAVE);
        }
    }

}
