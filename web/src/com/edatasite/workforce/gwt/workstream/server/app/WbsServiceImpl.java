package com.edatasite.workforce.gwt.workstream.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.project.server.app.TaskItemFactory;
import com.edatasite.workforce.gwt.project.server.app.WorkStreamItemFactory;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.gwt.workstream.client.rpc.WbsService;
import com.edatasite.workforce.gwt.workstream.client.ui.WbscopyItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("wbsService")
public class WbsServiceImpl implements WbsService, Constants {

    @Autowired
    private TaskManager taskManager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskService;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem[] getSubItems(ListingFilterParameter filterParameter) {

        List<EdsWorkStream> workStreamList = workStreamManager.getOrderByWorkStream(filterParameter);
        List<EdsTask> tasksList = taskManager.getOrderByTask(filterParameter);

        List<WbsItem> workStreamResult = createWorkstreamResult(workStreamList);
        List<WbsItem> taskResult = createTaskResult(tasksList);

        workStreamResult.addAll(taskResult);
        if (!workStreamList.isEmpty() && !taskResult.isEmpty()) {
            if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
                if (filterParameter.getSortDir() == 1) {
                    workStreamResult.sort(Comparator.comparing(WbsItem::getStartDate));
                } else {
                    workStreamResult.sort((o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate()));
                }
            } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
                if (filterParameter.getSortDir() == 1) {
                    workStreamResult.sort(Comparator.comparing(WbsItem::getEndDate));
                } else {
                    workStreamResult.sort((o1, o2) -> o2.getEndDate().compareTo(o1.getEndDate()));
                }
            } else if (WbsItem.NAME.equals(filterParameter.getSortField())) {
                if (filterParameter.getSortDir() == 1) {
                    workStreamResult.sort(Comparator.comparing(SelectItem::getName));
                } else {
                    workStreamResult.sort((o1, o2) -> o2.getName().compareTo(o1.getName()));
                }
            } else {
                workStreamResult.sort(Comparator.comparing(SelectItem::getName));
            }
        }
        return workStreamResult.toArray(new WbsItem[0]);
    }

    private List<WbsItem> createTaskResult(List<EdsTask> tasks) {
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return ListUtils.createTreeItemList(tasks, referenceWfmMessageSource, new TaskItemFactory());
    }

    private List<WbsItem> createWorkstreamResult(List<EdsWorkStream> workstreams) {
        if (workstreams.isEmpty()) {
            return new ArrayList<>();
        }
        return ListUtils.createTreeItemList(workstreams, new WorkStreamItemFactory());
    }

    /*
         Items inclueds first level workstreams and first level tasks
     */

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem[] getItems(ListingFilterParameter filterParameter) {
        List<EdsWorkStream> workstreams = workStreamManager.findOrphanWorkstreams(filterParameter);
        List<EdsTask> tasks = taskManager.findOrphanTasks(filterParameter);
        List<WbsItem> taskResult = createTaskResult(tasks);
        List<WbsItem> workStreamResult = createWorkstreamResult(workstreams);
        workStreamResult.addAll(taskResult);
        if (taskResult.size() > 0) {
            workStreamResult.sort((o1, o2) -> {
                if (WbsItem.START_DATE.equals(filterParameter.getSortField())) {
                    return filterParameter.getSortDir() == 1 ? o2.getStartDate().compareTo(o1.getStartDate()) : o1.getStartDate().compareTo(o2.getStartDate());
                } else if (WbsItem.END_DATE.equals(filterParameter.getSortField())) {
                    return filterParameter.getSortDir() == 1 ? o2.getEndDate().compareTo(o1.getEndDate()) : o1.getEndDate().compareTo(o2.getEndDate());
                } else if (WbsItem.NAME.equals(filterParameter.getSortField())) {
                    return filterParameter.getSortDir() == 1 ? o1.getName().compareTo(o2.getName()) : o2.getName().compareTo(o1.getName());
                }
                return o2.getStartDate().compareTo(o1.getStartDate());
            });
        }
        return workStreamResult.toArray(new WbsItem[taskResult.size()]);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem[] getFirstLevelWorkstreams(Integer projectId) {
        List<EdsWorkStream> workstreams = workStreamManager.findOrphanWorkstreams(projectId);
        return createWorkstreamResult(workstreams).toArray(new WbsItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem getFirstLevelWorkstreams(Integer projectId, Integer workStreamID) {
        List<EdsWorkStream> workstreams = workStreamManager.findOrphanWorkstreams(projectId);
        for (EdsWorkStream wS : workstreams) {
            if (wS.getObjectID().equals(workStreamID)) {
                return new WbsItem(wS.getObjectID(), wS.getName(), 0);
            }
        }
        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void getRecurItems(List<WbsItem> itemChilds, int parentId, int tab) {
        WbsItem[] treeChilds = getSubWorkStreams(parentId);
        if (treeChilds != null && treeChilds.length > 0) {
            tab++;
            for (WbsItem child : treeChilds) {
                StringBuilder tabs = new StringBuilder();
                for (int i = 1; i <= tab; i++) {
                    tabs.append("-");
                }
                itemChilds.add(new WbsItem(child.getId(), tabs + child.getName(), parentId));
                getRecurItems(itemChilds, child.getId(), tab);
            }
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public WbsItem[] getSubWorkStreams(Integer workStreamId) {
        EdsWorkStream workstream = workStreamManager.get(workStreamId);
        List<EdsWorkStream> workStreamList = new ArrayList<>();
        Set<EdsWorkStream> undeletedWorkstream = workstream.getSubWorkStreams();
        for (EdsWorkStream undel : undeletedWorkstream) {
            if (!undel.isDeleted()) {
                workStreamList.add(undel);
            }
        }

        Arrays.sort(workStreamList.toArray(new EdsWorkStream[]{}), (o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        return createWorkstreamResult(workStreamList).toArray(new WbsItem[]{});
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ArrayList<WbsItem> getWorkStreamList(Integer projectID) {
        List<WbsItem> wbsItemList = getProjectSubItems(projectID);
        ArrayList<WbsItem> wbsItems = new ArrayList<>();
        for (WbsItem wbsItem : wbsItemList) {
            wbsItems.add(wbsItem);
            if (WbsItem.WORKSTREAM == wbsItem.getNodeType()) {
                wbsItem.setName("<b>" + wbsItem.getName() + "</b>");
                wbsItems.addAll(getWorkStreamChilds(wbsItem.getId(), "<i style='color:white;'>____</i>"));
            }
        }
        return wbsItems;
    }

    @Transactional
    public WbsItem getSelectCompilitedStatus(Integer projectID) {
        EdsProject workstreams = workStreamManager.selectCompilitedStatus(projectID);
        WbsItem wbsItem = new WbsItem();
        if (workstreams != null && workstreams.getStatus() != null) {
            wbsItem.setStatusId(workstreams.getStatus().getObjectID());
            wbsItem.setProjectStatus(workstreams.getStatus().getCode());
        }
        return wbsItem;
    }

    /**
     * Get WorkStream Children
     * Recursive function
     *
     * @param wbsID - wbs ID
     * @return - list
     */
    private List<WbsItem> getWorkStreamChilds(Integer wbsID, String childNum) {
        List<WbsItem> wbsItemList = new ArrayList<>();
        List<WbsItem> wbsList = getSubWorkStreamItems(wbsID);
        for (WbsItem wbsItem : wbsList) {
            wbsItemList.add(wbsItem);
            if (WbsItem.WORKSTREAM == wbsItem.getNodeType()) {
                wbsItem.setName(childNum + "<b>" + wbsItem.getName() + "</b>");
                wbsItemList.addAll(getWorkStreamChilds(wbsItem.getId(), childNum + "<i style='color:white;'>____</i>"));
            } else {
                wbsItem.setName(childNum + wbsItem.getName());
            }
        }
        return wbsItemList;
    }

    /**
     * Get Project SubWork Stream and Tasks
     *
     * @param projectId - newProject ID
     * @return - list
     */
    private List<WbsItem> getProjectSubItems(Integer projectId) {
        List<EdsWorkStream> workstreams = workStreamManager.findOrphanWorkstreams(projectId);
        List<EdsTask> tasks = taskManager.findOrphanTasks(projectId);
        List<WbsItem> taskResult = createTaskResult(tasks);
        List<WbsItem> workStreamResult = createWorkstreamResult(workstreams);
        workStreamResult.addAll(taskResult);
        return workStreamResult;
    }

    /**
     * Get Work Stream Children
     *
     * @param workStreamId - workStream ID
     * @return - list
     */
    private List<WbsItem> getSubWorkStreamItems(Integer workStreamId) {
        EdsWorkStream workstream = workStreamManager.get(workStreamId);
        Set<EdsWorkStream> removed = new HashSet<>();

        Set<EdsWorkStream> subWorkStreams = workstream.getSubWorkStreams();
        for (EdsWorkStream undeletedWorkstream : subWorkStreams) {
            if (undeletedWorkstream.isDeleted()) {
                removed.add(undeletedWorkstream);
            }
        }
        subWorkStreams.removeAll(removed);
        List<EdsWorkStream> workStreamList = new ArrayList<>(subWorkStreams);
        List<WbsItem> workStreamResult = createWorkstreamResult(workStreamList);
        List<EdsTask> tasksList = new ArrayList<>(workstream.getTasks());
        List<WbsItem> taskResult = createTaskResult(tasksList);
        workStreamResult.addAll(taskResult);
        return workStreamResult;
    }


    @Transactional
    public void copyWorkstreamToOtherProject(WbscopyItem item) {
        long begin = System.currentTimeMillis();
        HashMap<EdsTask, EdsTask> compareMap = new HashMap<>();
        copyWorkstreamToOtherProject(item, compareMap);
        if (!compareMap.isEmpty()) {
            for (EdsTask oldTask : compareMap.keySet()) {
                EdsTask oldTAsk1 = taskManager.get(oldTask.getObjectID());
                EdsTask newTask = compareMap.get(oldTask);
                newTask.getPredecessors().clear();
                if (oldTAsk1.getPredecessors() != null && !oldTAsk1.getPredecessors().isEmpty()) {
                    HashSet<EdsTask> preds = new HashSet<>();
                    for (EdsTask predTask : oldTAsk1.getPredecessors()) {
                        if (compareMap.containsKey(predTask)) {
                            preds.add(compareMap.get(predTask));
                        }
                    }
                    newTask.getPredecessors().addAll(preds);
                }
            }

            for (EdsTask oldTask : compareMap.keySet()) {
                EdsTask oldTAsk1 = taskManager.get(oldTask.getObjectID());
                EdsTask newTask = compareMap.get(oldTask);
                newTask.getSuccessors().clear();
                if (oldTAsk1.getSuccessors() != null && !oldTAsk1.getSuccessors().isEmpty()) {
                    HashSet<EdsTask> succs = new HashSet<>();
                    for (EdsTask succTask : oldTAsk1.getSuccessors()) {
                        EdsTask sTask = compareMap.get(succTask);
                        if (compareMap.containsKey(succTask)) {
                            sTask.getPredecessors().add(newTask);
                        }
                    }
                }
            }
        }
        System.out.println("CopyWorkstreamToOtherProject - " + (System.currentTimeMillis() - begin));
    }

    @Transactional
    public void copyWorkstreamToOtherProject(WbscopyItem item, HashMap<EdsTask, EdsTask> compareMap) {
        EdsWorkStream workStream = workStreamManager.get(item.getObjectID());
        EdsProject newProject = projectManager.get(item.getProjectID());
        copyWorkstreamToOtherProject(workStream, newProject, item.getStartDate(), item.isCopyTask(), item.isCopyAssignee(), item.getTaskStatusID(), null, compareMap);
    }

    @Transactional
    public void copyWorkstreamToOtherProject(EdsWorkStream workStream, EdsProject newProject, Date startDate, Boolean copyTasks, Boolean copyAssignees, Integer statusID, EdsWorkStream parentWS, HashMap<EdsTask, EdsTask> compareMap) {
        EdsUser user = taskManager.getUser();
        EdsWorkStream newWorkstream = new EdsWorkStream();
        newWorkstream.setName(workStream.getName());
        newWorkstream.setDescription(workStream.getDescription());
        newWorkstream.setProject(newProject);
        newWorkstream.setCreator(user);
        newWorkstream.setCreationTime(new Date());
        newWorkstream.setUpdater(user);
        newWorkstream.setLastUpdateTime(new Date());
        newWorkstream.setSavedNumberFormula(workStream.getSavedNumberFormula());
        NumberData wsNumberData = taskService.generateWorkstreamNumber(newProject.getObjectID(), startDate, null);
        if (wsNumberData != null) {
            newWorkstream.setIntNumber(wsNumberData.getIntNumber());
            newWorkstream.setNumber(wsNumberData.getNumberString());
        }

        Date newWsStart = (Date) startDate.clone();
        newWsStart.setHours(workStream.getStartDate().getHours());
        newWsStart.setMinutes(workStream.getStartDate().getMinutes());
        newWsStart.setSeconds(workStream.getStartDate().getSeconds());
        newWorkstream.setStartDate(newWsStart);

        long diff = workStream.getEndDate().getTime() - workStream.getStartDate().getTime();
        Date endDate = new Date(startDate.getTime() + diff);
        endDate.setHours(workStream.getEndDate().getHours());
        endDate.setMinutes(workStream.getEndDate().getMinutes());
        endDate.setSeconds(workStream.getEndDate().getSeconds());
        newWorkstream.setEndDate(endDate);

        workStreamManager.create(newWorkstream);
        if (copyTasks) {
            List<EdsTask> taskList = taskManager.getWorkStreamTasksOrderBy(workStream.getObjectID(), null);
            if (taskList != null) {
                for (EdsTask task : taskList) {
                    task = taskManager.get(task.getObjectID());
                    TaskSingleItem newTask = new TaskSingleItem();
                    long startDiff = workStream.getStartDate().getTime() - task.getStartDate().getTime();
                    Date newTaskStart = new Date(startDate.getTime() + (-1) * startDiff);
                    newTaskStart.setHours(task.getStartDate().getHours());
                    newTaskStart.setMinutes(task.getStartDate().getMinutes());
                    newTaskStart.setSeconds(task.getStartDate().getSeconds());
                    newTask.setStartDate(newTaskStart);
                    long dueDiff = task.getDueDate().getTime() - task.getStartDate().getTime();
                    Date dueDate = new Date(newTask.getStartDate().getTime() + dueDiff);
                    dueDate.setHours(task.getDueDate().getHours());
                    dueDate.setMinutes(task.getDueDate().getMinutes());
                    dueDate.setSeconds(task.getDueDate().getSeconds());
                    newTask.setDueDate(dueDate);

                    newTask.setName(task.getName());
                    newTask.setDescription(task.getDescription());
                    newTask.setProjectID(newProject.getObjectID());

                    NumberData numberData = taskService.generateTaskNumber(newProject.getObjectID(), startDate, null);
                    newTask.setNumberData(numberData);
                    newTask.setAllDay(task.isAllDay());
                    //copy assignees
                    if (copyAssignees) {
                        Set<EdsEmployeeTask> assignments = task.getUnDeletedAssignments();
                        ArrayList<IdTime> allAssignees = new ArrayList<>();

                        if (assignments != null) {
                            List<EdsEmployee> employeesByProject = projectEmployeeManager.getProjectEmployees2(newProject);
                            for (EdsEmployeeTask employeeTask : assignments) {

                                EdsProject newEdsProject = projectManager.get(newProject.getObjectID());
                                if (!employeesByProject.contains(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee())) {
                                    EdsProjectEmployee employee = taskService.addMembers(newEdsProject, employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee());
                                    allAssignees.add(new IdTime(employee.getObjectID(), 0, 0, Float.valueOf("0.0")));
                                } else {
                                    EdsProjectEmployee employee = projectEmployeeManager.getProjectEmployee(employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee(), newEdsProject);
                                    allAssignees.add(new IdTime(employee.getObjectID(), employeeTask.getEstimatedTime(), 0, 0.0f,
                                            statusID != null ? statusID : employeeTask.getStatus() != null ? employeeTask.getStatus().getObjectID() : null));
                                }
                            }
                        }

                        if (allAssignees.isEmpty()) {
                            newTask.setWithoutAssignees(true);
                            newTask.setProjectEmployees(new IdTime[]{});
                        } else {
                            IdTime[] projectEmployees = allAssignees.toArray(new IdTime[]{});
                            newTask.setProjectEmployees(projectEmployees);
                        }
                    } else {
                        newTask.setWithoutAssignees(true);
                        newTask.setProjectEmployees(new IdTime[]{});
                    }
                    newTask.setBillable(task.getBillable());
                    newTask.setPriorityID(task.getPriority() != null ? task.getPriority().getObjectID() : null);
                    newTask.setTypeID(task.getType() != null ? task.getType().getObjectID() : null);
                    if (statusID != null) {
                        newTask.setStatusID(statusID);
                    } else {
                        newTask.setStatusID(task.getStatus() != null ? task.getStatus().getObjectID() : null);
                    }
                    newTask.setWorkstreamID(newWorkstream.getObjectID());
                    newTask.setEstimatedTime(task.getEstimatedTime());//
                    newTask.setTaskGanttOrder(task.getTaskGanttOrder());

                    if (task.getProject().getManager().equals(user) || task.getProject().isUserBackupManager(user.getObjectID())) {
                        newTask.setPermission(EDIT);
                    } else if (user.hasRole(roleManager.get(EdsRole.DR)) || user.hasRole(roleManager.get(EdsRole.ADMIN)) || user.hasRole(roleManager.get(EdsRole.TL))) {
                        newTask.setPermission(EDIT);
                    } else {
                        if (task.getCreator() == null) {
                            task.setCreator(user);
                        }
                        if (task.getCreator().equals(user)) {
                            newTask.setPermission(EDIT);
                        } else {
                            newTask.setPermission(READ);
                        }
                    }
                    EdsTask newEdsTask = null;
                    try {
                        newEdsTask = taskService.saveTaskDetailed(newTask, user);
                    } catch (NumberExistingException e) {
                        e.printStackTrace();
                    }
                    compareMap.put(task, newEdsTask);
                }
            }
        }

        EdsProject newEdsProject = projectManager.get(newProject.getObjectID());

        try {
//            solrManager.indexAddProject(newEdsProject, newEdsProject.getCompany().getObjectID());
            projectSolrComponent.index(newEdsProject);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        newWorkstream.setParentWS(parentWS);
        EdsWorkStream workStream1 = workStreamManager.get(workStream.getObjectID());
        Set<EdsWorkStream> subWorkStreams = workStream1.getSubWorkStreams();
        if (subWorkStreams != null) {
            for (EdsWorkStream subWS : subWorkStreams) {
                copyWorkstreamToOtherProject(subWS, newEdsProject, startDate, copyTasks, copyAssignees, statusID, newWorkstream, compareMap);
            }
        }
    }
}
