package com.workforcetrack.mobile.services;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetSolrField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrTaskRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.client.ui.rbacpermission.TaskPermissionEnum;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.task.client.rpc.EditTask;
import com.edatasite.workforce.gwt.task.client.rpc.TaskList;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.workforcetrack.mobile.rpc.base.MFacetFilter;
import com.workforcetrack.mobile.rpc.base.MSelectItemList;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.calendar.MTaskList;
import com.workforcetrack.mobile.rpc.calendar.MTaskListItem;
import com.workforcetrack.mobile.rpc.client.MFilterParametrs;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.opportunity.MNumberData;
import com.workforcetrack.mobile.rpc.project.MProjectItem;
import com.workforcetrack.mobile.rpc.task.MPositionList;
import com.workforcetrack.mobile.rpc.task.MPriorityList;
import com.workforcetrack.mobile.rpc.task.MProjectItemList;
import com.workforcetrack.mobile.rpc.task.MTaskFilterData;
import com.workforcetrack.mobile.rpc.task.MTaskStatusList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import com.edatasite.workforce.gwt.task.client.rpc.NewTaskList;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/23/11
 * Time: 3:18 PM
 */
@Transactional
@Service("taskWebService")
public class TaskWebServiceImpl implements TaskWebService {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private RbacService rbacService;
    @Autowired
    private AllInOneService allInOneService;

    @Override
    public MProjectItemList getProjects() {
        ProjectItem[] projectItems = commonService.getProjects(false);

        return new MProjectItemList(projectItems);
    }

    @Override
    public MProjectItemList getUserProjects() {
        EdsUser user = projectManager.getUser();
        MProjectItemList mProjectList = null;
        List<EdsProject> projects = projectManager.getUserProjects(user);
        if (projects != null && projects.size() > 0) {
            mProjectList = new MProjectItemList();
            List<MProjectItem> projectItemList = new ArrayList<>();
            for (EdsProject pr : projects) {
                MProjectItem mProjectItem = new MProjectItem(pr.getObjectID(), pr.getName());
                mProjectItem.setManager(user.equals(pr.getManager()) || pr.isUserBackupManager(user.getObjectID()));
                projectItemList.add(mProjectItem);
            }
            mProjectList.setProjectItem(projectItemList);
        }

        return mProjectList;
    }

    @Override
    public MPriorityList getPriorities() {
        SelectItem[] priorities = taskService.getPriorities();

        return new MPriorityList(priorities);
    }

    @Override
    public MPositionList getAssigneesWithPositions(Integer projectID) {
        if (projectID == null || projectID == 0) {
            return null;
        }

        PositionsSelectItem[] positionsSelectItems = taskServiceLocal.getAssigneesWithPositionsForMobile(projectID);

        return new MPositionList(positionsSelectItems);
    }

    @Override
    public MTaskStatusList getStatusList() {

        SelectItem[] selectItems = commonService.getAddTaskStatusDrop();

        return new MTaskStatusList(selectItems);
    }

    @Override
    public MTaskFilterData getFilterData() {
        FacetFilterRpc taskFacetFilterRpc = new FacetFilterRpc(getTaskColumnCode(), getAllTaskSolrFields());
        taskFacetFilterRpc.setType(ListPanelType.TaskListPanel);
        taskFacetFilterRpc.setFilterChanges(true);
        taskFacetFilterRpc = rbacService.getTaskFacetFilterData(taskFacetFilterRpc, true);
        MTaskFilterData resultFilterData = new MTaskFilterData();
        resultFilterData.setProject(getFacetItems(taskFacetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[0]));
        resultFilterData.setClient(getFacetItems(taskFacetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[1]));
        resultFilterData.setPriority(getFacetItems(taskFacetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[4]));
        resultFilterData.setStatus(getFacetItems(taskFacetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[3]));
        resultFilterData.setAssignee(getFacetItems(taskFacetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[5]));

        return resultFilterData;
    }

    @Override
    public MTaskFilterData getFilterData(MFacetFilter facetFilter) {
        FacetFilterRpc facetFilterRpc = new FacetFilterRpc(getTaskColumnCode(), getAllTaskSolrFields());
        facetFilterRpc.setType(ListPanelType.TaskListPanel);
        facetFilterRpc.setOverallSearch(true);
        WebServiceUtils.setFacetItems(facetFilter.getProjectID(), true, facetFilterRpc, FacetContentType.TaskFacetFilter, 0);
        WebServiceUtils.setFacetItems(facetFilter.getClientID(), true, facetFilterRpc, FacetContentType.TaskFacetFilter, 1);
        WebServiceUtils.setFacetItems(facetFilter.getTaskStatus(), false, facetFilterRpc, FacetContentType.TaskFacetFilter, 3);
        WebServiceUtils.setFacetItems(facetFilter.getTaskPriority(), false, facetFilterRpc, FacetContentType.TaskFacetFilter, 4);
        WebServiceUtils.setFacetItems(facetFilter.getAssigneeID(), true, facetFilterRpc, FacetContentType.TaskFacetFilter, 5);

        facetFilterRpc.setFilterChanges(true);
        facetFilterRpc = rbacService.getTaskFacetFilterData(facetFilterRpc, true);

        MTaskFilterData resultFilterData = new MTaskFilterData();
        resultFilterData.setProject(getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[0]));
        resultFilterData.setClient(getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[1]));
        resultFilterData.setPriority(getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[4]));
        resultFilterData.setStatus(getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[3]));
        resultFilterData.setAssignee(getFacetItems(facetFilterRpc, FacetContentType.TaskFacetFilter.getContentCode()[5]));

        return resultFilterData;
    }

    private List<MSelectItem> getFacetItems(FacetFilterRpc facetFilterRpc, String solrFieldName) {
        SelectItem[] items = facetFilterRpc.getFacetContentMap().get(solrFieldName).getFacetItems();
        return WebServiceUtils.getAsMSelectItemList(items);
    }

    @Override
    public MTaskListItem get(Integer objectID) {
        if (objectID == null) {
            return null;
        }

        TaskSingleItem taskSingleItem = taskService.getTask(objectID, null);

        return new MTaskListItem(taskSingleItem);
    }

    @Override
    public MTaskListItem edit(Integer objectID) {
        if (objectID == null) {
            return null;
        }
        EditTask editTask = taskService.getTaskForEdit(objectID);
        if (editTask == null) {
            return null;
        }
        TaskInvolvedMember[] members = taskService.getAssignments(objectID);

        MTaskListItem mTaskListItem = new MTaskListItem(editTask);
        if (members != null && members.length > 0) {
            List<MSelectItem> assignees = new ArrayList<>();
            for (TaskInvolvedMember member : members) {
                assignees.add(new MSelectItem(member.getEmployeeID(), member.getEmployee()));
            }
            mTaskListItem.setAssignee(assignees);
        }

        return mTaskListItem;
    }

    @Override
    public MTaskList getList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        FacetFilterRpc facetFilter = new FacetFilterRpc(getTaskColumnCode(), getTaskSolrField());
        facetFilter.setType(ListPanelType.TaskListPanel);
        facetFilter.setStartDate(mFilterParametrs.getStartDate());
        facetFilter.setEndDate(mFilterParametrs.getEndDate());
        facetFilter.setOverallSearch(true);
        if (mFilterParametrs.getStatusName() != null && mFilterParametrs.getStatusName().size() > 0) {
            List<SelectItem> statusItems = new ArrayList<>();
            for (String statusName : mFilterParametrs.getStatusName()) {
                if (statusName != null && !"".equals(statusName.trim())) {
                    statusItems.add(new SelectItem(null, statusName));
                }
            }
            FacetContentRpc statusFacet = facetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[3]);
            statusFacet.setFacetItems(statusItems.toArray(new SelectItem[]{}));
        }

        mFilterParametrs.setFacetFilter(facetFilter);

        ListingFilterParameter fp = mFilterParametrs.convertToListingFilterParameter(null);
        fp.setFromMobile(true);
        TaskList newTaskList = taskService.getTaskList(fp);

        return new MTaskList(newTaskList);
    }

    @Override
    public MTaskList getNewList(MFilterParametrs mFilterParametrs) {
        if (mFilterParametrs == null) {
            return null;
        }

        FacetFilterRpc facetFilter = new FacetFilterRpc(getTaskColumnCode(), getAllTaskSolrFields());
        facetFilter.setType(ListPanelType.TaskListPanel);
        facetFilter.setStartDate(mFilterParametrs.getStartDate());
        facetFilter.setEndDate(mFilterParametrs.getEndDate());
        if (mFilterParametrs.getFilter() != null) {
            MFacetFilter filter = mFilterParametrs.getFilter();
            WebServiceUtils.setFacetItems(filter.getProjectID(), true, facetFilter, FacetContentType.TaskFacetFilter, 0);
            WebServiceUtils.setFacetItems(filter.getClientID(), true, facetFilter, FacetContentType.TaskFacetFilter, 1);
            WebServiceUtils.setFacetItems(filter.getTaskStatus(), false, facetFilter, FacetContentType.TaskFacetFilter, 3);
            WebServiceUtils.setFacetItems(filter.getTaskPriority(), false, facetFilter, FacetContentType.TaskFacetFilter, 4);
            WebServiceUtils.setFacetItems(filter.getAssigneeID(), true, facetFilter, FacetContentType.TaskFacetFilter, 5);
        }
        facetFilter.setFilterChanges(true);
        mFilterParametrs.setFacetFilter(facetFilter);

        ListingFilterParameter fp = mFilterParametrs.convertToListingFilterParameter(null);
        fp.setFromMobile(true);
        TaskList newTaskList = taskService.getTaskList(fp);

        return new MTaskList(newTaskList);
    }

    private ArrayList<String> getTaskColumnCode() {
        ArrayList<String> resultList = new ArrayList<>(Arrays.asList(FacetContentType.TaskFacetFilter.getContentCode()));
        return resultList;

    }

    private HashMap<String, FacetSolrField> getTaskSolrField() {
        HashMap<String, FacetSolrField> contactSolrField = new HashMap<>();
        FacetSolrField solrField = new FacetSolrField();
        solrField.setSolrFacetFieldName(SolrTaskRepresenter.FIELD_TASK_STATUS);
        solrField.setSolrFieldCriteriaName(SolrTaskRepresenter.FIELD_TASK_STATUS);
        solrField.setConditionItemId(false);
        contactSolrField.put(FacetContentType.TaskFacetFilter.getContentCode()[3], solrField);
        return contactSolrField;
    }

    private HashMap<String, FacetSolrField> getAllTaskSolrFields() {
        HashMap<String, FacetSolrField> fieldsMap = new HashMap<>();
        FacetSolrField solrField = new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_ID_NAME, LocalizationType.REFERENCE);
        fieldsMap.put(FacetContentType.TaskFacetFilter.getContentCode()[0], solrField);
        solrField = new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID, SolrTaskRepresenter.FIELD_TASK_PROJECT_CLIENT_ID_NAME, LocalizationType.REFERENCE);
        fieldsMap.put(FacetContentType.TaskFacetFilter.getContentCode()[1], solrField);
        solrField = new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_STATUS, SolrTaskRepresenter.FIELD_TASK_STATUS, LocalizationType.REFERENCE, false);
        fieldsMap.put(FacetContentType.TaskFacetFilter.getContentCode()[3], solrField);
        solrField = new FacetSolrField(SolrTaskRepresenter.FIELD_TASK_PRIORITY, SolrTaskRepresenter.FIELD_TASK_PRIORITY, LocalizationType.REFERENCE, false);
        fieldsMap.put(FacetContentType.TaskFacetFilter.getContentCode()[4], solrField);
        solrField = new FacetSolrField(SolrTaskRepresenter.FIELD_USER_ID, SolrTaskRepresenter.FIELD_USER_ID_NAME, LocalizationType.REFERENCE);
        fieldsMap.put(FacetContentType.TaskFacetFilter.getContentCode()[5], solrField);
        return fieldsMap;
    }

    @Override
    public Integer save(MTaskListItem item) {
        Integer[] result = new Integer[]{0, -1};
        try {
            TaskSingleItem taskSingleItem = null;
            EditTask editTask;

            if (item.getObjectID() == null || item.getObjectID().equals(0)) {
                if (item.getProjectID() == null || item.getProjectID().equals(0)) {
                    EdsProject defaultProject = projectManager.getUser().getCompany().getDefaultProject();
                    if (defaultProject != null) {
                        item.setProjectID(defaultProject.getObjectID());
                    }
                }
                taskSingleItem = item.convertToTaskSingleItem(taskSingleItem);
                taskSingleItem.setNumberData(taskService.generateTaskNumber(taskSingleItem.getProjectID(), taskSingleItem.getStartDate(), null));
                if (item.getNumber() != null && !"".equals(item.getNumber().getNumberString().trim())) {
                    taskSingleItem.getNumberData().setNumberString(item.getNumber().getNumberString());
                    taskSingleItem.getNumberData().setIntNumber(item.getNumber().getIntNumber());
                }
                result = taskService.saveTask(taskSingleItem);
            } else {
                if (item.getAssignee() != null && item.getAssignee().size() > 0) {
                    List<IdTime> ids = new ArrayList<>();
                    for (MSelectItem member : item.getAssignee()) {
                        if (member.getObjectID() != null) {
                            ids.add(new IdTime(member.getObjectID(), null));
                        }
                    }
                    if (ids.size() > 0) {
                        taskService.updateTaskAssignees(item.getObjectID(), ids.toArray(new IdTime[]{}));
                    }
                }

                editTask = taskService.getTaskForEdit(item.getObjectID());
                editTask = item.convertToEditTask(editTask);

                setTaskStatusPermissions(editTask);
                taskService.updateTask(editTask);

                result[1] = item.getObjectID();
            }

            return result[1];
        } catch (Exception ex) {
            ex.printStackTrace();
            return result[1];
        }
    }

    public Integer saveWithReturnID(MTaskListItem item) {
        Integer result = -1;
        try {
            TaskSingleItem taskSingleItem = null;
            EditTask editTask;
            if (item.getObjectID() == null || item.getObjectID().equals(0)) {
                if (item.getProjectID() == null || item.getProjectID().equals(0)) {
                    EdsProject defaultProject = projectManager.getUser().getCompany().getDefaultProject();
                    if (defaultProject != null) {
                        item.setProjectID(defaultProject.getObjectID());
                    }
                }
                if (item.getProjectID() != null && !item.getProjectID().equals(0)) {
                    EdsProject project = projectManager.get(item.getProjectID());
                    if (item.getAssignee() != null && item.getAssignee().size() > 0 && item.getAssignee().size() == 1 && !item.getAssignee().get(0).getObjectID().equals(0)) {
                        for (MSelectItem idTime : item.getAssignee()) {
                            if (idTime.getObjectID() != null) {
                                EdsEmployee employee = employeeManager.get(idTime.getObjectID());
                                EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employee, project);
                                idTime.setObjectID(projectEmployee.getObjectID());
                            }
                        }
                    } else {
                        List<MSelectItem> employeeAssignee = new ArrayList<>();
                        EdsUser user = employeeManager.getUser();
                        if (user != null) {
                            EdsEmployee employee = employeeManager.get(user.getObjectID());
                            EdsProjectEmployee projectEmployee = projectEmployeeManager.getProjectEmployee(employee, project);
                            if (projectEmployee != null) {
                                employeeAssignee.add(new MSelectItem(projectEmployee.getObjectID(), null));
                                item.setAssignee(employeeAssignee);
                            }
                        }
                    }
                }
                if (item.getAssignee() == null || item.getAssignee().size() == 0) {
                    return result;
                }
                taskSingleItem = item.convertToTaskSingleItem(taskSingleItem);
                taskSingleItem.setNumberData(taskService.generateTaskNumber(taskSingleItem.getProjectID(), taskSingleItem.getStartDate(), null));
                Integer[] resultTask = taskService.saveTask(taskSingleItem);
                result = (resultTask != null && resultTask[1] != null) ? resultTask[1] : 0;
                if (result > 0 && item.getPercent() != null && !item.getPercent().equals(0)) {
                    EdsTask edsTask = taskManager.get(result);
                    TaskListItem taskListItem = item.convertToTaskListItem(null);
                    taskService.saveTaskEditCellValue(taskListItem, TaskListItem.COMPLETE);
                }
            } else {
                editTask = taskService.getTaskForEdit(item.getObjectID());
                editTask = item.convertToEditTask(editTask);
                setTaskStatusPermissions(editTask);
                taskService.updateTask(editTask);
                result = editTask.getObjectID();
            }

            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return result;
        }
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Integer saveCrmTask(MTaskListItem item) {
        EdsUser user = employeeManager.getUser();
        EdsProject crmProject = projectManager.getCrmProject();
        Integer result = null;
        if (crmProject != null) {
            item.setProjectID(crmProject.getObjectID());
            result = saveWithReturnID(item);
        }
        return result;
    }

    private void setTaskStatusPermissions(EditTask editTask) {
        if (editTask != null) {
            if (editTask.getPermissions().contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode()) && editTask.getPermissions().contains(TaskPermissionEnum.STATUS_EDIT.getCode())) {
                editTask.setUpdateTaskStatusForAll(true);
                editTask.setUpdateAssignmentTaskStatus(true);
            } else if (editTask.getPermissions().contains(TaskPermissionEnum.ASSIGNEE_STATUS_EDIT.getCode()) && !editTask.getPermissions().contains(TaskPermissionEnum.STATUS_EDIT.getCode())) {
                editTask.setUpdateTaskStatusForAll(true);
                editTask.setUpdateAssignmentTaskStatus(false);
            } else {
                editTask.setUpdateTaskStatusForAll(false);
                editTask.setUpdateAssignmentTaskStatus(true);
            }
        }
    }

    @Override
    public Boolean delete(Integer objectID) {
        if (objectID == null) {
            return false;
        }

        try {
            taskService.deleteTask(objectID, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteList(ArrayList<Integer> objectIDs) {
        try {
            taskService.deleteTasks(objectIDs, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public MTaskList getMKList(MFilterParametrs mFp) {
        if (mFp == null) {
            return null;
        }

        FacetFilterRpc facetFilter = new FacetFilterRpc(getTaskColumnCode(), getTaskSolrField());
        facetFilter.setType(ListPanelType.TaskListPanel);
        facetFilter.setStartDate(mFp.getStartDate());
        facetFilter.setEndDate(mFp.getEndDate());
        facetFilter.setOverallSearch(true);
        if (mFp.getStatusName() != null && mFp.getStatusName().size() > 0) {
            List<SelectItem> statusItems = new ArrayList<>();
            for (String statusName : mFp.getStatusName()) {
                if (statusName != null && !"".equals(statusName.trim())) {
                    statusItems.add(new SelectItem(null, statusName));
                }
            }
            FacetContentRpc statusFacet = facetFilter.getFacetContentMap().get(FacetContentType.TaskFacetFilter.getContentCode()[3]);
            statusFacet.setFacetItems(statusItems.toArray(new SelectItem[]{}));
        }

        mFp.setFacetFilter(facetFilter);

        ListingFilterParameter fp = mFp.convertToListingFilterParameter(null);
        fp.setFromMobile(true);
        TaskList newTaskList = taskService.getTaskList(fp);
        MTaskList resultList = new MTaskList();
        if (newTaskList != null && newTaskList.getList() != null && newTaskList.getList().size() > 0) {
            resultList.setTotalCount(newTaskList.getTotal());
            List<MTaskListItem> taskItems = new ArrayList<>();
            for (TaskListItem item : newTaskList.getList()) {
                TaskSingleItem taskSingleItem = taskService.getTask(item.getObjectID(), null);
                taskItems.add(MTaskListItem.getForMK(taskSingleItem));
            }
            resultList.setTaskListItem(taskItems);
        }
        return resultList;

    }

    @Override
    public MSelectItemList lookUp(MFilterParametrs fp) {
        if (fp != null && fp.getProjectID() != null && fp.getProjectID() > 0) {
            ListingFilterParameter lfp = fp.convertToListingFilterParameter(null);
            lfp.setLookUp(true);
            lfp.setPM(true);
            SelectItem[] items = allInOneService.getLookUpItems(lfp, LookUpConstants.PM_TASK_ID, null);
            return new MSelectItemList(items);
        }
        return null;
    }

    @Override
    public MNumberData generateTaskNumber(Integer projectID, Date date) {
        NumberData numberData = taskService.generateTaskNumber(projectID, date, null);
        return new MNumberData(numberData);
    }

    @Override
    public MNumberData generateTaskNumber(Date date) {
        NumberData numberData = taskService.generateTaskNumber(null, date, null);
        return new MNumberData(numberData);
    }

}
