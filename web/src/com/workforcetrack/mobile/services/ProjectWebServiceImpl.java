package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MFilterData;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.project.MClientList;
import com.workforcetrack.mobile.rpc.project.MProjectList;
import com.workforcetrack.mobile.rpc.project.MProjectListItem;
import com.workforcetrack.mobile.rpc.project.MProjectMember;
import com.workforcetrack.mobile.rpc.project.MProjectMemberList;
import com.workforcetrack.mobile.rpc.project.MProjectStatusList;
import com.workforcetrack.mobile.rpc.project.MStatusList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/31/11
 * Time: 5:03 PM
 */
@Service("projectWebService")
public class ProjectWebServiceImpl implements ProjectWebService {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private AllInOneService allInOneService;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private RoleManager roleManager;

    @Override
    public MStatusList getProjectStatuses() {
        SelectItem[] projectStatusList = projectService.getProjectStatuses();

        return new MStatusList(projectStatusList);
    }

    @Override
    public MProjectStatusList getStatuses() {

        SelectItem[] statusList = projectService.getProjectStatuses();

        return new MProjectStatusList(statusList);
    }

    @Override
    public MClientList getClients(Integer projectID) {
        return getClients(new MFilterParametrs(), null);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public MClientList getClients(MFilterParametrs mFilterParametrs, Integer projectID) {
        if (mFilterParametrs == null) {
            mFilterParametrs = new MFilterParametrs();
        }
        ListingFilterParameter fp = mFilterParametrs.convertToFilterParametrs();
        SelectItem[] clientItems = projectService.searchClientsByProjectId(projectID, fp.getSearchKey());

        return new MClientList(clientItems);
    }

    @Override
    public MClientList getClients(MFilterParametrs mFilterParametrs) {
        return getClients(mFilterParametrs, null);
    }

    @Override
    public MClientList getClients() {
        return getClients(new MFilterParametrs(), null);
    }

    @Override
    public MProjectMemberList getProjectEmployees(Integer objectID) {
        ProjectMember[] projectMembers = employeeService.getProjectEmployees(objectID);

        return new MProjectMemberList(projectMembers);
    }

    @Override
    public MProjectMemberList getProjectEmployeesWithTeams(Integer objectID) {

        ProjectMember[] projectMembers = employeeService.getProjectEmployeesWithTeams(objectID);

        return new MProjectMemberList(projectMembers);
    }

    @Override
    public MProjectMemberList getProjectEmployeesWithTeams() {
        return getProjectEmployeesWithTeams(null);
    }

    @Override
    public MFilterData getFilterData() {
        return getMFilterData(null);
    }

    @Override
    public MFilterData getFilterData(MFacetFilter facetFilter) {
        return getMFilterData(facetFilter);
    }

    private MFilterData getMFilterData(MFacetFilter facetFilter) {
        FacetFilterRpc facetFilterRpc = new FacetFilterRpc(getProjectColumnCode(), getProjectSolrFields());
        facetFilterRpc.setType(ListPanelType.ProjectListPanel);
        facetFilterRpc.setOverallSearch(false);
        if (facetFilter != null) {
            WebServiceUtils.setFacetItems(facetFilter.getManagerID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 0);
            WebServiceUtils.setFacetItems(facetFilter.getClientID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 1);
            WebServiceUtils.setFacetItems(facetFilter.getStatusID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 2);
            WebServiceUtils.setFacetItems(facetFilter.getAssigneeID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 3);
        }
        facetFilterRpc.setFilterChanges(true);
        facetFilterRpc = rbacService.getProjectFacetFilterData(facetFilterRpc);

        MFilterData resultFilterData = new MFilterData();
        String[] projectContentCodes = FacetContentType.ProjectFacetFilter.getContentCode();
        resultFilterData.setManager(MFacetFilter.getFacetItems(facetFilterRpc, projectContentCodes[0]));
        resultFilterData.setClient(MFacetFilter.getFacetItems(facetFilterRpc, projectContentCodes[1]));
        resultFilterData.setStatus(MFacetFilter.getFacetItems(facetFilterRpc, projectContentCodes[2]));
        resultFilterData.setAssignee(MFacetFilter.getFacetItems(facetFilterRpc, projectContentCodes[3]));

        return resultFilterData;
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
        solrField = new FacetSolrField(SolrProjectListRepresenter.FIELD_USER_ID, SolrProjectListRepresenter.FIELD_USER_ID_NAME, LocalizationType.REFERENCE);
        resultSolrField.put(contentCodes[3], solrField);

        return resultSolrField;
    }

    private ArrayList<String> getProjectColumnCode() {
        return MFacetFilter.getColumnCodes(FacetContentType.ProjectFacetFilter);
    }


    @Override
    public MProjectList getList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null)
            return null;

        if (mFilterParametrs.getFilter() != null) {
            FacetFilterRpc facetFilterRpc = new FacetFilterRpc(getProjectColumnCode(), getProjectSolrFields());
            facetFilterRpc.setType(ListPanelType.ProjectListPanel);
            WebServiceUtils.setFacetItems(mFilterParametrs.getFilter().getManagerID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 0);
            WebServiceUtils.setFacetItems(mFilterParametrs.getFilter().getClientID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 1);
            WebServiceUtils.setFacetItems(mFilterParametrs.getFilter().getStatusID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 2);
            WebServiceUtils.setFacetItems(mFilterParametrs.getFilter().getAssigneeID(), true, facetFilterRpc, FacetContentType.ProjectFacetFilter, 3);
            facetFilterRpc.setFilterChanges(true);
            mFilterParametrs.setFacetFilter(facetFilterRpc);
        }

        ListingFilterParameter fp = mFilterParametrs.convertToListingFilterParameter(null);
        ListResult<ProjectListItem> projectList = projectService.getProjectList(fp);

        return new MProjectList(projectList);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MProjectListItem get(Integer objectID) {

        EdsProject project = projectManager.get(objectID);
        MProjectListItem item = new MProjectListItem();
        item.setObjectID(project.getObjectID());
        MNumberData number = new MNumberData(project.getNumber(), project.getIntNumber());
        item.setNumber(number);
        item.setName(project.getName());
        item.setDescription(project.getDescription());

        item.setManagerName(project.getManager() != null ? project.getManager().getFullName() : null);
        item.setManagerID(project.getManager() != null ? project.getManager().getObjectID() : null);
        item.setBackupManagerID(project.getBackupManager() != null ? project.getBackupManager().getObjectID() : null);
        StringBuilder names = new StringBuilder();
        for (EdsEmployee backupManager : project.getBackupManagers()) {
            if (names.toString().equals("")) {
                names.append(backupManager.getFullName());
            } else {
                names.append(", ").append(backupManager.getFullName());
            }
        }
        item.setBackupManagerName(names.toString());
        item.setBackupManagerIDs(project.getBackupManagerIDs());
        if (project.getClient() != null && !project.getClient().isDeleted()) {
            item.setClientID(project.getClient().getObjectID());
            item.setClient(project.getClient().getName());
        }
        item.setStatusID(project.getStatus() != null ? project.getStatus().getObjectID() : null);
        item.setStatus(project.getStatus() != null ? project.getStatus().getName() : null);
        item.setComplete(project.getPercent() != null ? project.getPercent().toString() : "0.0");
        item.setLastUpdate(project.getLastUpdateTime() != null ? new Date(project.getLastUpdateTime().getTime()) : null);
        item.setStartDate(project.getStartDate() != null ? new Date(project.getStartDate().getTime()) : null);
        item.setEndDate(project.getEndDate() != null ? new Date(project.getEndDate().getTime()) : null);
        item.setDueDate(project.getDueDate() != null ? new Date(project.getDueDate().getTime()) : null);
        item.setHoursSpent(project.getTimeSpentHM() != null ? project.getTimeSpentHM() : "00:00");

        EdsUser user = projectManager.getUser();
        // Sets editable if user is PM or Project Backup Manager or Company Director, or Company Administrator
        if (!user.isClientContact() && (project.getManager().getObjectID().equals(user.getObjectID()) || project.isUserBackupManager(user.getObjectID()) || user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN)))) {
            item.setPermission(Constants.EDIT);
        } else {
            item.setPermission(Constants.READ);
        }
        int s = taskManager.getProjectTasks(project).size();
        item.setTaskCount(Long.valueOf(String.valueOf(s)));

        ProjectMember[] members = projectService.getProjectEmployees(objectID);
        item.setHeadCount(members != null ? members.length : 0);
        return item;
    }

    @Override
    public MProjectListItem edit(Integer objectID) {
        if (objectID == null || objectID == 0)
            return null;

        EditProject editProject = projectService.getProjectForEdit(objectID, null, null);
        ProjectMember[] members = projectService.getProjectEmployees(objectID);

        MProjectListItem mProjectListItem = new MProjectListItem(editProject);
        if (members != null) {
            ArrayList<MProjectMember> list = new ArrayList<>();
            for (ProjectMember projectMember : members) {
                list.add(new MProjectMember(projectMember));
            }
            mProjectListItem.setProjectMembers(list);
        }
        return mProjectListItem;
    }

    @Override
    public Integer save(MProjectListItem mProjectItem) {
        if (mProjectItem == null)
            return -1;
        Boolean result = null;
        Integer saveResult = null;
        try {
            if (mProjectItem.getObjectID() != null && mProjectItem.getObjectID() != 0) {
                EditProject editProject = projectService.getProjectForEdit(mProjectItem.getObjectID(), mProjectItem.getStartDate(), mProjectItem.getClientID());
                editProject = mProjectItem.convertToEditProject(editProject);
                projectService.updateProject(editProject);
                saveResult = mProjectItem.getObjectID();
            } else {
                ProjectSingleItem projectSingleItem = mProjectItem.convertToProjectSingleItem(null);
                projectSingleItem.setNumberData(projectService.generateProjectNumber(mProjectItem.getStartDate(), mProjectItem.getClientID(), null));
                saveResult = projectService.saveProject(projectSingleItem);
            }

            return (saveResult != null && saveResult != 0) ? saveResult : -1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Boolean delete(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        try {
            projectService.deleteProject(objectID);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MSelectItemList lookUp(MFilterParametrs fp) {
        if (fp != null) {
            ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
            lfp.setLookUp(true);
            lfp.setPM(true);
            SelectItem[] items = allInOneService.getLookUpItems(lfp, LookUpConstants.PM_PROJECT_ID, null);
            return new MSelectItemList(items);
        }

        return null;
    }

    @Override
    public MNumberData generateProjectNumber(Date date, Integer clientID) {
        NumberData numberData = projectService.generateProjectNumber(date, clientID, null);
        return new MNumberData(numberData);
    }

    @Override
    public MNumberData generateProjectNumber(Date date) {
        return generateProjectNumber(date, null);
    }
}
