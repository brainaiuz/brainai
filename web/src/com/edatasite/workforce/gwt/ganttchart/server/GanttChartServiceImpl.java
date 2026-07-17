package com.edatasite.workforce.gwt.ganttchart.server;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.chart.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.GanttChartTaskEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.utils.AbstractComparator;
import com.edatasite.workforce.gwt.core.server.utils.ComparatorFactory;
import com.edatasite.workforce.gwt.ganttchart.client.GanttChartService;
import com.edatasite.workforce.gwt.ganttchart.client.enums.LoadItemType;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GCWorkstreamItem;
import com.edatasite.workforce.gwt.ganttchart.client.rpc.GanttItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskSelectItem;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceImpl;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 3/12/13
 * Time: 11:42 AM
 */

@Transactional
@Service("ganttchartService")
public class GanttChartServiceImpl implements GanttChartService, com.edatasite.workforce.gwt.ganttchart.client.Constants {

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    @Qualifier("taskService")
    private TaskServiceLocal taskService;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private TimeSheetManager timeSheetManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    protected GenericSettingsManager genericSettingsManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Override
    public ListResult<TaskSingleItem> getGanttChartTasks(Integer projectID, Integer employeeID, Date from, Date to, String sortBy, boolean showActual, LoadItemType loadType, Integer start) {
        ListResult<TaskSingleItem> resultList = new ListResult<>();
        ArrayList<TaskSingleItem> result = new ArrayList<>();
        ArrayList<GCWorkstreamItem> wsItems = new ArrayList<>();
        ArrayList<TaskSingleItem> taskItems = new ArrayList<>();
        HashMap<Integer, GCWorkstreamItem> workstreams = new HashMap<>();

        if (loadType == null || loadType.equals(LoadItemType.WORKSTREAM)) {
//            List<EdsWorkStream> workStreams = workStreamManager.findOrphanWorkstreams(projectID, from, to, sortBy);
            List<EdsWorkStream> workStreams = workStreamManager.findOrphanWorkstreams(projectID, from, to, sortBy, start, 5);
            resultList.setTotal(workStreams.size());

            if (workStreams != null && !workStreams.isEmpty()) {
                for (EdsWorkStream workStream : workStreams) {
                    collectWorkstreamData(wsItems, taskItems, workstreams, workStream, employeeID, sortBy, showActual);
                }
                /*if (sortBy.contains("Date")) {
                    Collections.sort(wsItems, getComparatorFactoryForWorkstreamDates().createComparator(sortBy.contains("asc") ? Constants.ASC : Constants.DESC));
                } else {
                    Collections.sort(wsItems, getComparatorFactoryForWorkstreamOrder().createComparator(sortBy.contains("asc") ? Constants.ASC : Constants.DESC));
                }*/
                //Collections.sort(wsItems, getComparatorFactoryForWorkstreamOrder().createComparator(Constants.ASC));
            }

            for (GCWorkstreamItem wsItem : wsItems) {
                TaskSingleItem item = new TaskSingleItem();
                item.setObjectID(wsItem.getObjectID());
                item.setName(wsItem.getName());
                item.setStartDate(wsItem.getStartDate());
                item.setEndDate(wsItem.getEndDate());
                item.setWorkstreamID(wsItem.getParentWSID());
                item.setTaskGanttOrder(wsItem.getTaskGanttOrder());
                BigDecimal percent = new BigDecimal("0.00");
                percent = percent.setScale(2, RoundingMode.HALF_UP);
                item.setPercent(percent.floatValue());
                item.setWorkstream(true);
                result.add(item);
                if (!wsItem.getTasks().isEmpty()) {
                    result.addAll(wsItem.getTasks());
                }
            }
        }

        if (loadType == null || loadType.equals(LoadItemType.TASK)) {
//            List<EdsTask> tasks = taskManager.findOrphanTasksForGanttChart(projectID, employeeID, from, to, sortBy);
            List<EdsTask> tasks = taskManager.findOrphanTasksForGanttChart(projectID, employeeID, from, to, sortBy, start, 50);
            resultList.setTotal(tasks.size());

            if (tasks != null && !tasks.isEmpty()) {
                int i = 0;
                for (EdsTask task : tasks) {
                    wrapEdsTaskToTaskSingleItem(task, showActual, workstreams, wsItems, taskItems);
                    if (i % 5 == 0) {
                        taskManager.flushAndClear();
                        i++;
                    }
                }
            }

            result.addAll(taskItems);
        }

        resultList.setList(result);
        return resultList;
    }

    private void collectWorkstreamData(ArrayList<GCWorkstreamItem> wsItems, ArrayList<TaskSingleItem> taskItems, HashMap<Integer, GCWorkstreamItem> workstreams, EdsWorkStream workStream, Integer employeeID, String sortBy, boolean showActual) {
        if (!workstreams.containsKey(workStream.getObjectID())) {
            if (employeeID != null) {
                Integer tasksCount = workStreamManager.getEmployeeAssignedTasksCount(workStream.getObjectID(), employeeID);
                if (tasksCount != null && tasksCount > 0) {
                    wrapWorkStreamData(wsItems, taskItems, workstreams, workStream, employeeID, sortBy, showActual);
                }
            } else {
                wrapWorkStreamData(wsItems, taskItems, workstreams, workStream, employeeID, sortBy, showActual);
            }

            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setWorkstreamID(workStream.getObjectID());
            List<EdsWorkStream> subWorkStreams = workStreamManager.getOrderByWorkStream(filterParameter);
            if (subWorkStreams != null && !subWorkStreams.isEmpty()) {
                for (EdsWorkStream workStream1 : subWorkStreams) {
                    collectWorkstreamData(wsItems, taskItems, workstreams, workStream1, employeeID, sortBy, showActual);
                }
            }
        }
    }

    private void wrapWorkStreamData(ArrayList<GCWorkstreamItem> wsItems, ArrayList<TaskSingleItem> taskItems, HashMap<Integer, GCWorkstreamItem> workstreams, EdsWorkStream workStream, Integer employeeID, String sortBy, boolean showActual) {
        GCWorkstreamItem wsItem = new GCWorkstreamItem();
        wsItem.setObjectID(workStream.getObjectID());
        wsItem.setName(workStream.getName());
        wsItem.setStartDate(workStream.getStartDate());
        wsItem.setEndDate(workStream.getEndDate());
        wsItem.setTaskGanttOrder(workStream.getTaskGanttOrder());
        if (workStream.getParentWS() != null) {
            wsItem.setParentWSID(workStream.getParentWS().getObjectID());
        }
        wsItems.add(wsItem);
        workstreams.put(workStream.getObjectID(), wsItem);
        Set<EdsTask> subTasks = workStream.getTasks();
        if (subTasks != null && !subTasks.isEmpty()) {
            for (EdsTask task : subTasks) {
                if (employeeID == null) {
                    TaskSingleItem item = wrapEdsTaskToTaskSingleItem(task, showActual, workstreams, wsItems, taskItems);
                    if (!wsItem.getTasks().contains(item)) {
                        wsItem.getTasks().add(item);
                    }
                } else {
                    Set<EdsProjectEmployee> unDeletedProjectEmployees = task.getUnDeletedProjectEmployees();
                    for (EdsProjectEmployee projectEmployee : unDeletedProjectEmployees) {
                        if (employeeID.equals(projectEmployee.getEmployeeDepartment().getEmployee().getObjectID())) {
                            TaskSingleItem item = wrapEdsTaskToTaskSingleItem(task, showActual, workstreams, wsItems, taskItems);
                            if (!wsItem.getTasks().contains(item)) {
                                wsItem.getTasks().add(item);
                            }
                        }
                    }
                }
            }
            if ("objectID asc".equals(sortBy)) {
                wsItem.getTasks().sort(getComparatorFactoryForTaskID().createComparator(Constants.ASC));
            } else if ("objectID desc".equals(sortBy)) {
                wsItem.getTasks().sort(getComparatorFactoryForTaskID().createComparator(Constants.DESC));
            } else if ("startDate asc".equals(sortBy)) {
                wsItem.getTasks().sort(getComparatorFactoryForTaskDates().createComparator(Constants.ASC));
            } else if ("startDate desc".equals(sortBy)) {
                wsItem.getTasks().sort(getComparatorFactoryForTaskDates().createComparator(Constants.DESC));
            }
        }
    }

    private TaskSingleItem wrapEdsTaskToTaskSingleItem(EdsTask task1, boolean showActual, HashMap<Integer, GCWorkstreamItem> workstreams, ArrayList<GCWorkstreamItem> wsItems, ArrayList<TaskSingleItem> taskItems) {
        TaskSingleItem item = new TaskSingleItem();
        EdsTask task = taskManager.get(task1.getObjectID());
        item.setObjectID(task.getObjectID());
        item.setProjectID(task.getProject().getObjectID());
        item.setName(task.getName());
        item.setProjectName(task.getProject().getName());
        NumberData numberData = new NumberData(task.getNumber(), task.getIntNumber());
        item.setNumberData(numberData);
        item.setStartDate(task.getStartDate());
        item.setEndDate(task.getDueDate());
        if (showActual) {
            item.setActualStartDate(task.getActualStartDate());
            item.setActualEndDate(task.getActualEndDate());
        }
        item.setTaskGanttOrder(task.getTaskGanttOrder());
        BigDecimal percent = BigDecimal.valueOf(task.getPercent() != null ? task.getPercent() : Float.valueOf(0.00f));
        percent = percent.setScale(2, RoundingMode.HALF_UP);
        item.setPercent(percent.floatValue());
        //Task overall status
        item.setStatusName(task.getStatus() != null ? (task.getStatus().getCode() != null ? referenceWfmMessageSource.localize(task.getStatus().getCode(), task.getStatus().getName()) : task.getStatus().getName()) : "");
        //Task priority
        item.setPriorityName(task.getPriority() != null ? (task.getPriority().getCode() != null ? referenceWfmMessageSource.localize(task.getPriority().getCode(), task.getPriority().getName()) : task.getPriority().getName()) : "");
        item.setPriorityID(task.getPriority() != null ? task.getPriority().getObjectID() : null);
        item.setBillable(task.getBillable());
        //Task estimate time
        Double[] taskCostAndTime = timeSheetManager.getTaskCostAndTimeSpent(task.getObjectID());
        item.setEstimatedTime(task.getEstimatedTime());
        //Task actual time
        item.setActualTime(taskCostAndTime != null ? (taskCostAndTime[TaskServiceImpl.TASK_ACTUAL_TIME_SPENT] != null ? taskCostAndTime[TaskServiceImpl.TASK_ACTUAL_TIME_SPENT].intValue() : 0) : 0);
        //Task actual start/end dates
        item.setActualStartDate(task.getActualStartDate());
        item.setActualEndDate(task.getActualEndDate());
        // Task assignees
        Set<EdsEmployeeTask> assignments = task.getUnDeletedAssignments();
        if (assignments != null && !assignments.isEmpty()) {
            ArrayList<TaskInvolvedMember> assignees = new ArrayList<>();
            for (EdsEmployeeTask assignee : assignments) {
                EdsEmployee employee = assignee.getProjectEmployee().getEmployeeDepartment().getEmployee();
                TaskInvolvedMember member = new TaskInvolvedMember(assignee.getObjectID(), employee.getObjectID(), employee.getFullName(), Long.valueOf(assignee.getTimeSpent()));
                member.setPercent(assignee.getPercent());
                assignees.add(member);
            }
            item.setInvolvedMembers(assignees.toArray(new TaskInvolvedMember[]{}));
        }
        // Task taskPredecessors
        Set<EdsTask> taskPredecessors = task.getPredecessors();
        if (taskPredecessors != null && !taskPredecessors.isEmpty()) {
            ArrayList<SelectItem> predecessors = new ArrayList<>();
            for (EdsTask predTask : taskPredecessors) {
                SelectItem pred = new SelectItem(predTask.getObjectID(), predTask.getName());
                predecessors.add(pred);
            }
            item.setPredecessorTasks(predecessors.toArray(new SelectItem[]{}));
        }
        //------------------------- Workstream params ---------------------------------
        if (task.getParentWS() != null) {
            if (!workstreams.containsKey(task.getParentWS().getObjectID())) {
                EdsWorkStream parentWS = task.getParentWS();
                GCWorkstreamItem wsItem = new GCWorkstreamItem();
                wsItem.setObjectID(parentWS.getObjectID());
                wsItem.setName(parentWS.getName());
                wsItem.setStartDate(parentWS.getStartDate());
                wsItem.setEndDate(parentWS.getEndDate());
                wsItem.setTaskGanttOrder(parentWS.getTaskGanttOrder());
                wsItems.add(wsItem);
                workstreams.put(parentWS.getObjectID(), wsItem);
                wsItem.getTasks().add(item);
            } else {
                workstreams.get(task.getParentWS().getObjectID()).getTasks().add(item);
            }
            item.setWorkstreamID(task.getParentWS().getObjectID());
        } else {
            taskItems.add(item);
        }
        return item;
    }

    public ArrayList<SelectItem> getProjectEmployees(Integer projectID) {
        ArrayList<SelectItem> result = new ArrayList<>();
        HashMap<Integer, EdsEmployee> employees = new HashMap<>();
        List<Object[]> projectEmployees = projectManager.getProjectEmployees(projectID, ServerUtils.getMaxRoleID(taskManager.getUser().getRolesAsIntegersString()));
        if (projectEmployees != null) {
            for (Object[] item : projectEmployees) {
                EdsEmployee employee = (EdsEmployee) item[1];
                if (!employees.containsKey(employee.getObjectID())) {
                    result.add(employee.getAsSelectItem());
                    employees.put(employee.getObjectID(), employee);
                }
            }
            result.sort(Comparator.comparing(SelectItem::getName));
        }
        return result;
    }

    public String deleteTask(Boolean isWorkstream, Integer taskID) {
        String result = PermissionConstants.ALLOW;
        if (isWorkstream) {
            try {
                taskService.deleteWorkstream(taskID, null, false);
            } catch (Exception e) {
                result = PermissionConstants.DENY;
                e.printStackTrace();
            }
        } else {
            result = taskService.deleteTask(taskID, PermissionConstants.PM_CONTEXT);
        }
        return result;
    }

    private ComparatorFactory<TaskSingleItem> getComparatorFactoryForTaskOrder() {
        return sortOrder -> new AbstractComparator<TaskSingleItem>() {
            public int compare(TaskSingleItem o1, TaskSingleItem o2) {
                return internalCompare(o1.getObjectID(), o2.getObjectID(), sortOrder);
            }
        };
    }

    private ComparatorFactory<GCWorkstreamItem> getComparatorFactoryForWorkstreamOrder() {
        return sortOrder -> new AbstractComparator<GCWorkstreamItem>() {
            public int compare(GCWorkstreamItem o1, GCWorkstreamItem o2) {
                return internalCompare(o1.getObjectID(), o2.getObjectID(), sortOrder);
            }
        };
    }

    private ComparatorFactory<GCWorkstreamItem> getComparatorFactoryForWorkstreamParent() {
        return sortOrder -> new AbstractComparator<GCWorkstreamItem>() {
            public int compare(GCWorkstreamItem o1, GCWorkstreamItem o2) {
                return internalCompare(o1.getParentWSID(), o2.getParentWSID(), sortOrder);
            }
        };
    }

    private ComparatorFactory<TaskSingleItem> getComparatorFactoryForTaskDates() {
        return sortOrder -> new AbstractComparator<TaskSingleItem>() {
            public int compare(TaskSingleItem o1, TaskSingleItem o2) {
                return internalCompare(o1.getStartDate() != null ? o1.getStartDate() : new Date(), o2.getStartDate() != null ? o2.getStartDate() : new Date(), sortOrder);
            }
        };
    }

    private ComparatorFactory<GCWorkstreamItem> getComparatorFactoryForWorkstreamDates() {
        return sortOrder -> new AbstractComparator<GCWorkstreamItem>() {
            public int compare(GCWorkstreamItem o1, GCWorkstreamItem o2) {
                return internalCompare(o1.getStartDate() != null ? o1.getStartDate() : new Date(), o2.getStartDate() != null ? o2.getStartDate() : new Date(), sortOrder);
            }
        };
    }

    private ComparatorFactory<TaskSingleItem> getComparatorFactoryForTaskID() {
        return sortOrder -> new AbstractComparator<TaskSingleItem>() {
            public int compare(TaskSingleItem o1, TaskSingleItem o2) {
                return internalCompare(o1.getObjectID(), o2.getObjectID(), sortOrder);
            }
        };
    }

    public void saveTaskDates(Integer taskID, Date startDate, Date endDate) {
        EdsCompany company = taskManager.getUser().getCompany();
        EdsTask edsTask = taskManager.get(taskID);
        Date dueDate = new Date(edsTask.getDueDate().getTime());
        edsTask.setStartAndDueDates(startDate, endDate);
        taskService.updateTaskDates(true, taskManager.getUser(), edsTask, dueDate);
        edsTask.setLastUpdateTime(new Date());
        taskService.updateTaskDailyLoad(edsTask);
        taskManager.updateTask(edsTask);
        try {
            taskSolrComponent.index(edsTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recursivelyChangeWSDates(EdsWorkStream workStream) {
        EdsWorkStream parentWS = workStream.getParentWS();
        if (parentWS != null) {
            if (workStream.getStartDate().before(parentWS.getStartDate())) {
                parentWS.setStartDate(workStream.getStartDate());
            }
            if (workStream.getEndDate() != null && parentWS.getEndDate() != null && workStream.getEndDate().after(parentWS.getEndDate())) {
                parentWS.setEndDate(workStream.getEndDate());
            }
            workStreamManager.update(parentWS);
            recursivelyChangeWSDates(parentWS);
        }
    }

    public void saveGanttChartSettings(Integer projectID, String columns) {
        EdsUser user = projectManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        userSettings.setGanttChartColumns(columns);
    }

    public GanttItem getProjectDetailsForGanttChart(Integer projectID) {
        GanttItem result = new GanttItem();
        EdsProject project = projectManager.get(projectID);
        result.setProjectID(projectID);
        result.setName(project.getName());
        result.setDescription(project.getDescription());
        result.setManager(project.getManager() != null ? project.getManager().getFullName() : "");
        result.setStartDate(project.getStartDate());
        result.setEndDate(project.getDueDate());
        Date actualStartDate = taskManager.getFirstProjectTask(projectID);
        Date taskActualStartDate = projectManager.getProjectActualStartDate(projectID);
        if (actualStartDate != null && result.getStartDate().after(actualStartDate)) {
            result.setStartDate(actualStartDate);
        }
        if (taskActualStartDate != null && result.getStartDate().after(taskActualStartDate)) {
            result.setStartDate(taskActualStartDate);
        }

        Date actualEndDate = taskManager.getLastExistingProjectTask(projectID);
        Date taskActualEndDate = projectManager.getProjectActualEndDate(projectID);
        if (result.getEndDate() == null) {
            result.setEndDate(new Date());
        }
        if (actualEndDate != null && result.getEndDate().before(actualEndDate)) {
            result.setEndDate(actualEndDate);
        }
        if (taskActualEndDate != null && result.getEndDate().before(taskActualEndDate)) {
            result.setEndDate(taskActualEndDate);
        }
        EdsUser user = userEmailSettingsManager.getUser();
        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        if (userSettings != null) {
            result.setColumnNames(userSettings.getGanttChartColumns() != null ? userSettings.getGanttChartColumns() : "");
            result.setLocale(userSettings.getInternationalization() != null && !"".equals(userSettings.getInternationalization()) ? userSettings.getInternationalization() : "en");
        } else {
            result.setColumnNames("");
            result.setLocale("en");
        }
        result.setPriorities(taskService.getPriorities());
        result.setEmployees(getProjectEmployees(projectID));
        EdsTimeSlot defaultTimeSlot = user.getCompany().getDefaultTimeSlot();
        if (defaultTimeSlot != null) {
            ArrayList<Integer> dayOffs = new ArrayList<>();
            for (EdsTimeSlotItem timeSlotItem : defaultTimeSlot.getItems()) {
                if (timeSlotItem.getStartTime() == 0 && timeSlotItem.getEndTime() == 0) {
                    dayOffs.add(timeSlotItem.getDay());
                }
            }
            result.setDayOffs(dayOffs);
            /*EdsCompanySystemSettings byCompanyID = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
			result.setWeekStartDay(byCompanyID.getOverallDatePickerWeekStart() - 1);*/
        }
        return result;
    }

    @Override
    public void saveCellValues(Integer taskID, String columnCodeName, String value) {
        EdsUser user = taskManager.getUser();
        EdsCompany company = user.getCompany();
        try {
            EdsTask edsTask = taskManager.get(taskID);
            System.out.println("GanttChart cell edited. CompanyID: " + company.getObjectID() + "; ProjectName: " + edsTask.getProject().getName() + "; TaskName: " + edsTask.getName() + "; FieldName: " + columnCodeName + "; Value: " + value);
            if (TaskListItem.START_DATE.equals(columnCodeName)) {
                edsTask.setStartDate(new Date(Long.valueOf(value)));
                baseEventPostProcessor.registerEvent(GanttChartTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, user);
            } else if (TaskListItem.END_DATE.equals(columnCodeName)) {
                Date taskOldDueDate = edsTask.getDueDate();
                edsTask.setDueDate(new Date(Long.valueOf(value)));
                baseEventPostProcessor.registerEvent(GanttChartTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, user);
                taskService.shiftAllSuccessors(edsTask, null, user, false, 0, taskOldDueDate, availabilityCircularResolver.getCompanyTimeSlot());
            } else if (TaskListItem.COMPLETE.equals(columnCodeName)) {
                taskService.updateTaskCellPercent(edsTask, Float.valueOf(value));
            } else if (TaskListItem.PRIORITY_NAME.equals(columnCodeName)) {
                edsTask.setPriority(referenceManager.get(Integer.valueOf(value)));
            } else if (TaskListItem.ESTIMATED.equals(columnCodeName)) {
                edsTask.setEstimatedTime(60 * Integer.parseInt(value));
            } else if (TaskListItem.BILLABLE.equals(columnCodeName)) {
                edsTask.setBillable(Boolean.valueOf(value));
            }
            edsTask.setLastUpdateTime(new Date());
            taskService.updateTaskDailyLoad(edsTask);
            taskSolrComponent.index(edsTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaskDates(Integer taskID, String startDate, String endDate) {
        EdsUser user = taskManager.getUser();
        EdsCompany company = user.getCompany();
        try {
            EdsTask edsTask = taskManager.get(taskID);
            edsTask.setStartDate(new Date(Long.valueOf(startDate)));
            Date taskOldDueDate = edsTask.getDueDate();
            edsTask.setDueDate(new Date(Long.valueOf(endDate)));
            baseEventPostProcessor.registerEvent(GanttChartTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_EDIT, edsTask, user);
            taskService.shiftAllSuccessors(edsTask, null, user, false, 0, taskOldDueDate, availabilityCircularResolver.getCompanyTimeSlot());

            edsTask.setLastUpdateTime(new Date());
            taskService.updateTaskDailyLoad(edsTask);
            taskSolrComponent.index(edsTask);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GanttItem getGanttChart(GanttItem ganttItem) {
        List<EdsWorkStream> workStreams = workStreamManager.findOrphanWorkstreams(ganttItem.getProjectID(), ganttItem.getStartDate(), ganttItem.getEndDate(), ganttItem.getSortBy());
        if (workStreams != null) {
            for (EdsWorkStream workStream : workStreams) {
                ganttItem.getSubWorkstreams().add(wrapWorkstream(workStream));
            }
        }
        List<EdsTask> tasks = taskManager.findOrphanTasksForGanttChart(ganttItem.getProjectID(), ganttItem.getEmployeeID(), ganttItem.getStartDate(), ganttItem.getEndDate(), ganttItem.getSortBy());
        if (tasks != null) {
            for (EdsTask task : tasks) {
                ganttItem.getTasks().add(new TaskSingleItem(task.getObjectID(), task.getName()));
            }
        }
        return ganttItem;
    }

    private GCWorkstreamItem wrapWorkstream(EdsWorkStream workStream) {
        GCWorkstreamItem item = new GCWorkstreamItem(workStream.getObjectID(), workStream.getName());
        Set<EdsTask> tasks = workStream.getTasks();
        if (tasks != null) {
            for (EdsTask task : tasks) {
                item.getTasks().add(new TaskSingleItem(task.getObjectID(), task.getName()));
            }
        }
        Set<EdsWorkStream> subWorkStreams = workStream.getSubWorkStreams();
        if (subWorkStreams != null) {
            for (EdsWorkStream ws : subWorkStreams) {
                item.getSubWorkstreams().add(wrapWorkstream(ws));
            }
        }
        return item;
    }

    public void saveTaskDependency(Integer taskID, TaskSelectItem[] dependencies, String action) {
        EdsUser user = taskManager.getUser();
        EdsTask task = taskManager.get(taskID);
        boolean taskPredecessorsOrSuccessorsChanged = false;
        Set<EdsTask> oldPreds = new HashSet<>(task.getPredecessors());
        Set<EdsTask> oldSuccs = new HashSet<>(task.getSuccessors());
        Set<EdsTask> newDependencies = new HashSet<>();
        if (dependencies != null) {
            for (TaskSelectItem item : dependencies) {
                newDependencies.add(taskManager.load(item.getId()));
            }
        }
        if (!oldPreds.containsAll(newDependencies) || !newDependencies.containsAll(oldPreds)
                || !oldSuccs.containsAll(newDependencies) || !newDependencies.containsAll(oldSuccs)) {
            taskPredecessorsOrSuccessorsChanged = true;
        }
        Date taskOldDueDate = task.getDueDate();
        if (taskPredecessorsOrSuccessorsChanged) {
            if (SET_PREDECESSOR.equals(action)) {
                taskService.refreshTaskDependencies(newDependencies, task.getPredecessors());
            } else {
                taskService.refreshTaskDependencies(newDependencies, task.getSuccessors());
            }
            taskService.shiftAllSuccessors(task, null, user, true, 0, taskOldDueDate, availabilityCircularResolver.getCompanyTimeSlot());
            Set<EdsWorkStream> workStreams = new HashSet<>();
            if (!task.getPredecessors().isEmpty()) {
                taskService.findPredecessorParentWS(task, workStreams);
            }
            if (!task.getSuccessors().isEmpty()) {
                taskService.findSuccessorParentWS(task, workStreams);
            }
            if (task.getParentWS() != null) {
                workStreams.add(task.getParentWS());
            }
            for (EdsWorkStream ws : workStreams) {
                taskService.updateWorkStreamDateRange(null, ws);
            }
            try {
                taskSolrComponent.index(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                projectSolrComponent.index(task.getProject());
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
    }

    public void saveTaskDependency(Integer taskID, Integer dependencyID, String action) {
        EdsTask task = taskManager.get(taskID);
        EdsTask dependent = taskManager.get(dependencyID);
        if (SET_PREDECESSOR.equals(action)) {
            task.getPredecessors().add(dependent);
        } else {
            dependent.getPredecessors().add(task);
        }
    }

    @Override
    public ChartData getGanttChartData(Integer projectId, HashSet<String> columns) {
        boolean isMultiColumn = columns.size() > 0;
        BigDecimal hundred = new BigDecimal(100);
        ChartData chartData = new ChartData();
        EdsProject project = projectManager.get(projectId);

        Date from = project.getStartDate() != null ? project.getStartDate() : new Date();
        Date to = project.getEndDate() != null ? project.getEndDate() : new Date();

        Date actualStartDate = taskManager.getFirstProjectTask(projectId);
        if (actualStartDate != null && from.after(actualStartDate)) {
            from = actualStartDate;
        }

        Date actualEndDate = taskManager.getLastExistingProjectTask(projectId);
        if (actualEndDate != null && to.before(actualEndDate)) {
            to = actualEndDate;
        }

        chartData.setGanttMinDate(DateUtil.addDays(from, -1));
        chartData.setGanttMaxDate(DateUtil.addDays(to, 1));

        ChartConfItem confItem = new ChartConfItem();
//        confItem.setTitle(project.getName()); // hover qiganda projectName chiqmaydi endi
        confItem.setShowLabel(false);

        chartData.setConf(confItem);

        LinkedHashMap<Integer, List<EdsTask>> tasks = taskManager.findTasksForGanttChart(projectId, null, from, to);

        HashMap<Integer, Double[]> taskCostAndTimeMap = new HashMap<>();
        if (columns.contains(TaskListItem.HOUR_SPENT) || columns.contains(TaskListItem.ACTUAL_HOURS_SPENT)) {
            List<Integer> taskIds = new ArrayList<>();
            for (List<EdsTask> value : tasks.values()) {
                for (EdsTask edsTask : value) {
                    taskIds.add(edsTask.getObjectID());
                }
            }
            taskCostAndTimeMap = timeSheetManager.getCostAndTimeSpentOnTasks(ServerUtils.getAsCommoDelimited(taskIds, "0", ","));
        }
        SerieData overAllStatus = new SerieData();
        overAllStatus.setName("Overall Progress");
        overAllStatus.setColor("#333");
        BigDecimal overallPercent = new BigDecimal("0.0").divide(hundred, 2, RoundingMode.HALF_UP);
        ;
        int countOfTasks = 0;
        for (Map.Entry<Integer, List<EdsTask>> entry : tasks.entrySet()) {
            Integer workstreamId = entry.getKey();
            SerieData sprint = null;
            BigDecimal sprintPercent = new BigDecimal("0.0").divide(hundred, 2, RoundingMode.HALF_UP);
            if (!workstreamId.equals(0)) {
                sprint = isMultiColumn ? new GanttSerieData() : new SerieData();
                sprint.setId("ws_" + workstreamId);
                sprint.setName(workStreamManager.get(workstreamId).getName());
                sprint.setCollapsed(true);
                if (isMultiColumn) {
                    chartData.getGanttSerieData().add((GanttSerieData) sprint);
                } else {
                    chartData.getSeries().add(sprint);
                }
            }

            Date sprintStart = null;
            Date sprintEnd = null;
            for (EdsTask task : entry.getValue()) {
                SerieData data = isMultiColumn ? new GanttSerieData() : new SerieData();
                data.setId(String.valueOf(task.getObjectID()));
                data.setName(task.getName());
                data.setParent(sprint != null ? sprint.getId() : null);

                if (task.getPredecessors().size() > 0) {
                    for (EdsTask predTask : task.getPredecessors()) {
                        data.setDependency(predTask.getObjectID().toString());
                        break;
                    }
                }

                Date start = task.getStartDate() != null ? task.getStartDate() : from;
                Date end = task.getDueDate() != null ? task.getDueDate() : to;

                data.setStart(start);
                data.setEnd(end);
                data.setMilestone(DateUtil.areOnTheSameDay(start, end));
                if (overAllStatus.getStart() == null) overAllStatus.setStart(start);
                else if (start.before(overAllStatus.getStart())) overAllStatus.setStart(start);

                if (overAllStatus.getEnd() == null) overAllStatus.setEnd(end);
                else if (end.after(overAllStatus.getEnd())) overAllStatus.setEnd(end);

                EdsNumberingSettings settings = numberingSettingsManager.getNumberingSetting();
                boolean percentOverComplete = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED);

                BigDecimal percent;
                if (settings != null && settings.isAutomatic()) {//if settings is null then project percent calculation mode is manual by default
                    percent = task.getTaskAveragePercentCompletedNewLogic() != null ?
                            BigDecimal.valueOf(task.getTaskAveragePercentCompletedNewLogic()).divide(hundred, 2, RoundingMode.HALF_UP)
                            : null;
                } else {
                    percent = task.getPercent() != 0f ? BigDecimal.valueOf(task.getPercent()).divide(hundred, 2, RoundingMode.HALF_UP) : null;
                }
                data.setPercent((percent != null && percent.compareTo(new BigDecimal(1)) > 0 && !percentOverComplete)
                        ? new BigDecimal(100).divide(hundred, 2, RoundingMode.HALF_UP)
                        : percent != null ? percent : new BigDecimal("0.0"));
                sprintPercent = sprintPercent.add(percent == null ? new BigDecimal("0.0") : percent);
                overallPercent = overallPercent.add(percent == null ? new BigDecimal("0.0") : percent);
                countOfTasks++;
                if (isMultiColumn) {
                    if (columns.contains(TaskListItem.ACTUAL_START_DATE)) {
                        ((GanttSerieData) data).setActualStartDate(task.getActualStartDate());
                    }
                    if (columns.contains(TaskListItem.ACTUAL_END_DATE)) {
                        ((GanttSerieData) data).setActualEndDate(task.getActualEndDate());
                    }
                    if (columns.contains(TaskListItem.ESTIMATED)) {
                        ((GanttSerieData) data).setEstimated(task.getEstimatedTime());
                    }
                    if (columns.contains(TaskListItem.OVERALL_STATUS_NAME) && task.getStatus() != null) {
                        ((GanttSerieData) data).setOverallStatusName(task.getStatus().getName());
                    }
                    Double[] taskCostAndTimeSpent = taskCostAndTimeMap.get(task.getObjectID());
                    if (columns.contains(TaskListItem.HOUR_SPENT) && taskCostAndTimeSpent != null) {
                        ((GanttSerieData) data).setHourSpent(ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[Constants.TASK_ACTUAL_TIME_SPENT] != null
                                ? taskCostAndTimeSpent[Constants.TASK_ACTUAL_TIME_SPENT].intValue() : 0));
                    }
                    if (columns.contains(TaskListItem.ACTUAL_HOURS_SPENT) && taskCostAndTimeSpent != null) {
                        ((GanttSerieData) data).setActualHoursSpent(ServerUtils.getTimeSpentHM(taskCostAndTimeSpent[Constants.TASK_HOUR_SPENT] != null
                                ? taskCostAndTimeSpent[Constants.TASK_HOUR_SPENT].intValue() : 0));
                    }
                    chartData.getGanttSerieData().add((GanttSerieData) data);
                } else {
                    chartData.getSeries().add(data);
                }
                if (sprintStart == null || sprintStart.after(start)) sprintStart = start;
                if (sprintEnd == null || sprintEnd.before(end)) sprintEnd = end;
            }

            if (sprint != null) {
                String percentValue = String.valueOf(sprintPercent.doubleValue() / entry.getValue().size());
                sprint.setPercent(BigDecimal.valueOf(percentValue.length() > 4 ? Double.valueOf(percentValue.substring(0, 4)) : Double.valueOf(percentValue)));
                sprint.setStart(sprintStart);
                sprint.setEnd(sprintEnd);
            }
        }
        String overAllPercentValue = String.valueOf(overallPercent.doubleValue() / countOfTasks);
        overAllStatus.setPercent(BigDecimal.valueOf(overAllPercentValue.length() > 4 ? Double.valueOf(overAllPercentValue.substring(0, 4)) : Double.valueOf(overAllPercentValue)));
        if (!isMultiColumn) chartData.getSeries().add(0, overAllStatus);
        return chartData;
    }

    @Override
    public ChartData getGanttChartDataLeave(Integer userId, HashSet<String> columns) {
        boolean isMultiColumn = columns.size() > 0;
        BigDecimal hundred = new BigDecimal(100);
        ChartData chartData = new ChartData();

        Date from = new Date(new Date().getYear(), 0, 1, 0, 0, 0);
        Date to = new Date(new Date().getYear(), 11, 31, 0, 0, 0);
        ListingFilterParameter leaveFilterParam = new ListingFilterParameter();
        leaveFilterParam.setStart(0);
        leaveFilterParam.setStartDate(from);
        leaveFilterParam.setReasonCode("Annual");
        leaveFilterParam.setLimit(400);


        List<EdsSickRequest> leaveRequestList = sickRequestManager.getLeaveRequestList(leaveFilterParam);
        chartData.setGanttMinDate(DateUtil.addDays(from, -1));
        chartData.setGanttMaxDate(DateUtil.addDays(to, 1));

        ChartConfItem confItem = new ChartConfItem();
        confItem.setShowLabel(false);

        chartData.setConf(confItem);

        SerieData overAllStatus = new SerieData();
        overAllStatus.setName("Name");
        overAllStatus.setColor("#333");
//        BigDecimal overallPercent = new BigDecimal(0.0).divide(hundred, 2, BigDecimal.ROUND_HALF_UP);
//        ;
        Date sprintStart = null;
        Date sprintEnd = null;
        LinkedHashMap<Integer, DepartmentData> departmentMap = new LinkedHashMap<>();
        for (EdsSickRequest edsSickRequest : leaveRequestList) {
            if (from.getYear() <= edsSickRequest.getStartDate().getYear()) {
                SerieData data = isMultiColumn ? new GanttSerieData() : new SerieData();
                data.setId(String.valueOf(edsSickRequest.getObjectID()));
                data.setName(edsSickRequest.getEmployee().getFullName());
                data.setDependency(edsSickRequest.getLeaveReason().getLeaveDays().toString());
                EdsDepartment edsDepartment = edsSickRequest.getEmployee().getTeam();
                addToDepartmentMap(departmentMap, edsDepartment);
                data.setParent(String.valueOf(edsDepartment.getObjectID()));


                Date start = edsSickRequest.getStartDate() != null ? edsSickRequest.getStartDate() : from;
                Date end = edsSickRequest.getEndDate() != null ? edsSickRequest.getEndDate() : to;

                data.setStart(start);
                data.setEnd(end);
                data.setMilestone(DateUtil.areOnTheSameDay(start, end));

//            if (overAllStatus.getStart() == null) overAllStatus.setStart(start);
////            else if (start.before(overAllStatus.getStart())) overAllStatus.setStart(start);
////
////            if (overAllStatus.getEnd() == null) overAllStatus.setEnd(end);
////            else if (end.after(overAllStatus.getEnd())) overAllStatus.setEnd(end);

                if (isMultiColumn) {
                    chartData.getGanttSerieData().add((GanttSerieData) data);
                } else {
                    chartData.getSeries().add(data);
                }
//
//            if (sprintStart == null || sprintStart.after(start)) sprintStart = start;
//            if (sprintEnd == null || sprintEnd.before(end)) sprintEnd = end;
//
//
//            if (sprint != null) {
//                String percentValue = String.valueOf(sprintPercent.doubleValue());
//                sprint.setPercent(new BigDecimal(percentValue.length() > 4 ? Double.valueOf(percentValue.substring(0, 4)) : Double.valueOf(percentValue)));
//                sprint.setStart(sprintStart);
//                sprint.setEnd(sprintEnd);
//            }

            }
        }
        for (Map.Entry<Integer, DepartmentData> entry : departmentMap.entrySet()) {
            EdsDepartment department = entry.getValue().getDepartment();
            GanttSeriesDataWithChild ganttSerie = new GanttSeriesDataWithChild();
            ganttSerie.setId(String.valueOf(department.getObjectID()));
            ganttSerie.setName(department.getName());
            ganttSerie.setCollapsed(true);
            ganttSerie.setChildCount(entry.getValue().getChildCount());
            ganttSerie.setMember(entry.getValue().getMember());
            chartData.getSeries().add(ganttSerie);
        }
        // chartData.setObject(integerMap);
        // String overAllPercentValue = String.valueOf(overallPercent.doubleValue());
        // overAllStatus.setPercent(new BigDecimal(overAllPercentValue.length() > 4 ? Double.valueOf(overAllPercentValue.substring(0, 4)) : Double.valueOf(overAllPercentValue)));
        if (!isMultiColumn) chartData.getSeries().add(0, overAllStatus);
        return chartData;
    }

    private void addToDepartmentMap(LinkedHashMap<Integer, DepartmentData> departmentMap, EdsDepartment department) {
        if (departmentMap.containsKey(department.getObjectID())) {
            DepartmentData departmentData = departmentMap.get(department.getObjectID());
            departmentData.setChildCount(departmentData.getChildCount() + 1);
            departmentData.setMember(department.getMembers().size());
        } else {
            departmentMap.put(department.getObjectID(), new DepartmentData(department, 0, 0));
        }
    }

    static class DepartmentData {
        private EdsDepartment department;
        private Integer childCount;
        private Integer member;

        public DepartmentData(EdsDepartment department, Integer childCount, Integer member) {
            this.department = department;
            this.childCount = childCount;
            this.member = member;
        }

        public Integer getMember() {
            return member;
        }

        public void setMember(Integer member) {
            this.member = member;
        }

        public EdsDepartment getDepartment() {
            return department;
        }

        public void setDepartment(EdsDepartment department) {
            this.department = department;
        }

        public Integer getChildCount() {
            return childCount;
        }

        public void setChildCount(Integer childCount) {
            this.childCount = childCount;
        }
    }
}
