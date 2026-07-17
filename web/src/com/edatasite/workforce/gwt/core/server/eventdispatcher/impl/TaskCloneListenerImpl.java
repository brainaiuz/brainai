package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.customfields.EdsTaskCustomFields;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;
import com.edatasite.workforce.gwt.core.client.rpc.task.CloneTaskItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WorkStreamManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.TaskCFManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.project.client.rpc.CloneProjectItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.TransactionCustomEventListenerImpl.EVENT_SALES_INVOICE_TRANSACTION;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Mar 4, 2019
 * Time: 2:04:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class TaskCloneListenerImpl extends CustomBusinessEventListenerAdapter {

    public static final String EVENT_TASK_ADD = "ADD";

    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.taskCloneListener);
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;
    @Autowired
    private WorkStreamManager workStreamManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private CommonServiceLocal commonService;
    @Autowired
    private TaskCFManager taskCFManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    ;

    public void onAddEvent(EdsBusinessEvent event) {
        long begin = System.currentTimeMillis();
        if (StringUtils.isNotBlank(event.getCustomStringField())) {
            try {
                CloneProjectItem cloneProjectItem = new Gson().fromJson(event.getCustomStringField(), CloneProjectItem.class);
                EdsProject project = projectManager.get(cloneProjectItem.getProjectId());
                EdsProject cloneProject = projectManager.get(event.getEntityID());
                EdsUser user = userManager.get(event.getSourceID());

                Map<EdsTask, EdsTask> clonedTasks = new HashMap<>();

                if (cloneProjectItem.isCopyTasks() || cloneProjectItem.isCopyAssignmentsToAllProjectMembers()) {

                    ProjectMember[] pMembers = cloneProjectItem.getMembers();
                    List<Integer> allEmployees = new ArrayList<>();
                    for (ProjectMember mem : pMembers) {
                        allEmployees.add(mem.getId());
                    }
                    List<EdsProjectEmployee> clonedProjectMembers = projectEmployeeManager.getProjectEmployees(project);

                    //Start Cloning Tasks
                    List<EdsTask> taskIDs = taskManager.getProjectTasksOrderBySDate(project);
                    EdsReference taskStatus = getTaskStatus(cloneProjectItem);
                    int i = 0;
                    EdsTask task;
                    Map<Integer, Integer> defaultTimeslot = availabilityCircularResolver.getCompanyTimeSlot();
                    for (EdsTask tsk : taskIDs) {
                        task = taskManager.get(tsk.getObjectID());
                        Calendar tsd = ServerUtils.convertDateIntoCalendar(task.getStartDate());
                        tsd.set(Calendar.MILLISECOND, 0);
                        Calendar psd = ServerUtils.convertDateIntoCalendar(project.getStartDate());
                        psd.set(Calendar.MILLISECOND, 0);
                        long daysCount = tsd.getTimeInMillis() - psd.getTimeInMillis();
                        EdsTask clonedTask = null;
                        if (cloneProjectItem.isCopyTasks()) {
                            clonedTask = cloneTask(cloneProject, task, cloneProjectItem.getTaskItem(), allEmployees, taskStatus, user, daysCount, defaultTimeslot);
                        } else if (cloneProjectItem.isCopyAssignmentsToAllProjectMembers()) {
                            clonedTask = cloneTaskToAllProjectMembers(cloneProject, task, cloneProjectItem.getTaskItem(), allEmployees, taskStatus, user, clonedProjectMembers, daysCount);
                        }
                        clonedTasks.put(task, clonedTask);
                        if (task.getPredecessors() != null && !task.getPredecessors().isEmpty()) {
                            for (EdsTask predTask : task.getPredecessors()) {
                                if (clonedTasks.get(predTask) != null) {
                                    EdsTask tmpPredTask = taskManager.get(clonedTasks.get(predTask).getObjectID());
                                    if (tmpPredTask != null) {
                                        clonedTask.getPredecessors().add(tmpPredTask);
                                    }
                                } else {
                                    System.out.println("PREDESSESSOR IS NOT SET FOR TASK, ID: " + task.getObjectID() + " - " + task.getName() + " PRED, ID: " + predTask.getObjectID() + " - " + predTask.getName());
                                }
                            }
                        }
                        if (i++ % 30 == 0) {
                            taskManager.flushAndClear();
                            taskStatus = getTaskStatus(cloneProjectItem);
                        }
                    }
                }
                //End Of Cloning Tasks

                //Start Cloning Workstreams
                if (cloneProjectItem.isCopyWorkstream()) {
                    ListingFilterParameter filterParameter = new ListingFilterParameter();
//                    filterParameter.setWorkstreamID(cloneProjectItem.getProjectId());
                    filterParameter.setProjectId(cloneProjectItem.getProjectId());
                    List<EdsWorkStream> workstreams = workStreamManager.findOrphanWorkstreams(filterParameter);
                    Map<EdsWorkStream, EdsWorkStream> clonedWs = new HashMap<>();
                    boolean isFirstWorkStream = true;
                    Date firstWorkStreamStartDate = null;
                    long daysCount = 0;
                    for (EdsWorkStream worksteam : workstreams) {
                        if (clonedWs.get(worksteam) == null) {
                            if (isFirstWorkStream) {
                                isFirstWorkStream = false;
                                firstWorkStreamStartDate = worksteam.getStartDate();
                            } else {
                                if (firstWorkStreamStartDate != null && worksteam.getStartDate() != null) {
                                    daysCount = worksteam.getStartDate().getTime() - firstWorkStreamStartDate.getTime();
                                }
                            }
                            EdsWorkStream cloned = cloneWorkstream(cloneProject, worksteam, cloneProjectItem.getTaskItem(), clonedWs, clonedTasks, user, daysCount);
                            clonedWs.put(worksteam, cloned);
                        }
                    }

                    for (EdsWorkStream workStream : clonedWs.values()) {
                        adjustParentWSDates(workStream);
                    }

                }
                //End Of Cloning Workstreams
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (JsonSyntaxException e) {
                event.setStatus(EventStatus.FAILED.name());
                e.printStackTrace();
            }
        }

        System.out.println("CLONE PROJECT TASKS/WORKSTREAMS TOOK: " + (System.currentTimeMillis() - begin));


    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_TASK_ADD.equals(event.getEventType())) {
            this.onAddEvent(event);
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {

    }

    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    private EdsReference getTaskStatus(CloneProjectItem cloneProjectItem) {
        if (cloneProjectItem.getTaskItem().getStatus() == null) {
            return referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        } else {
            return referenceManager.get(cloneProjectItem.getTaskItem().getStatus());
        }
    }

    private EdsTask cloneTask(EdsProject cloneProject, EdsTask task, CloneTaskItem cloneTaskItem, List<Integer> employees, EdsReference taskStatus, EdsUser user, long daysCount, Map<Integer, Integer> defaultTimeslot) {
        EdsTask cloneTask = task.cloneShallow();
        cloneTask.clear();
        cloneTask.setParentWS(null);
//        cloneTask.setTaskCustomFields(null);
        cloneTask.setActualStartDate(null);
        cloneTask.setActualEndDate(null);
        cloneTask.setTaskGanttOrder(task.getTaskGanttOrder());
        cloneTask.setProject(cloneProject);
        if (cloneProject.getCreator() != null && employees.contains(cloneProject.getCreator().getObjectID())) {
            cloneTask.setCreator(cloneProject.getCreator());
        } else {
            cloneTask.setCreator(user);
        }
        cloneTask.setCreationTime(new Date());
        cloneTask.setLastUpdateTime(new Date());
        cloneTask.setTimespent(null);
        cloneTask.setActualWageAmount(0d);
        cloneTask.setActualClientChargeAmount(0d);
        cloneTask.setPercent(null);
        if (task.getIntNumber() != null) {
            cloneTask.setIntNumber(task.getIntNumber());
        }
        if (task.getNumber() != null) {
            cloneTask.setNumber(task.getNumber());
        }

        //Clone Custom Fields
        EdsTaskCustomFields edsTaskCustomFields = task.getTaskCustomFields();
        if (edsTaskCustomFields != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task));
            customFieldItems.forEach(cf -> cf.setObjectId(null));
            EdsTaskCustomFields edsTaskCusromFields = null;
            if (!genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_COPY_CUSTOM_FIELD_WITHOUT_VALUE)) {
                edsTaskCusromFields = createTaskCustomFields(customFieldItems);
            }
            cloneTask.setTaskCustomFields(edsTaskCusromFields);
        }

        if (cloneTaskItem.isAdjustByProjectStartDate()) {
            long days = 0;
            if (task.getDueDate() != null) {
                Calendar sd = ServerUtils.convertDateIntoCalendar(task.getStartDate());
                sd.set(Calendar.MILLISECOND, 0);
                Calendar ed = ServerUtils.convertDateIntoCalendar(task.getDueDate());
                ed.set(Calendar.MILLISECOND, 0);
                days = ed.getTimeInMillis() - sd.getTimeInMillis();
            }
            long startDate = cloneProject.getStartDate().getTime() + daysCount;
            //If task's start date is on weekend, we shift it to next day
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(startDate);
            while (defaultTimeslot != null && defaultTimeslot.get(start.get(Calendar.DAY_OF_WEEK) - 1) == 0) {
                start.add(Calendar.DAY_OF_WEEK, 1);
            }
            long endDate = start.getTime().getTime() + days;
            cloneTask.setStartAndDueDates(start.getTime(), new Date(endDate));
        }
        cloneTask.setLastUpdateTime(new Date());
        cloneTask.setDeleted(false);
        cloneTask.setStatus(taskStatus);
        EdsTask clonedTask = taskManager.merge(cloneTask);
        clonedTask.setNewItem(true);
        baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, clonedTask, user);
        if (cloneTaskItem.isCopyTaskAssignments()) {
            Set<EdsEmployeeTask> tasks = task.getUnDeletedAssignments();
            if (tasks.size() != 0) {
                Integer sumEstimates = 0;
                for (EdsEmployeeTask etask : tasks) {
                    if (employees.contains(etask.getProjectEmployee().getEmployeeDepartment().getEmployee().getObjectID())) {
                        EdsEmployeeTask cloneEmpTask = etask.cloneShallow(); //new EdsEmployeeTask();
                        cloneEmpTask.setTask(clonedTask);

                        cloneEmpTask.setProjectEmployee(projectEmployeeManager.getProjectEmployee(etask.getProjectEmployee().getEmployeeDepartment().getEmployee(), cloneProject));
                        cloneEmpTask.setDeleted(false);

                        cloneEmpTask.setNewTask(true);

                        if (cloneTaskItem.isAdjustByProjectStartDate()) {
                            cloneEmpTask.setStartDate(cloneProject.getStartDate());
                        } else {
                            cloneEmpTask.setStartDate(etask.getStartDate());
                        }
                        cloneEmpTask.setTimeSpent(null);
                        cloneEmpTask.setActualWageAmmount(0d);
                        cloneEmpTask.setActualClientChargeAmmount(0d);
                        cloneEmpTask.setPercent(null);
                        sumEstimates += cloneEmpTask.getEstimatedTime();

                        cloneEmpTask.setStatus(taskStatus);

                        EdsEmployeeTask clonedEmpTask = employeeTaskManager.merge(cloneEmpTask);
                        clonedTask.getAssignments().add(clonedEmpTask);

                        baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, clonedEmpTask, user);
                    }
                }
                clonedTask.setEstimatedTime(sumEstimates);

            } else {
                EdsEmployeeTask employeeTask = new EdsEmployeeTask();
                employeeTask.setTask(clonedTask);
                employeeTask.setProjectEmployee(projectEmployeeManager.getProjectEmployee(cloneProject.getManager(), cloneProject));
                employeeTask.setDeleted(true);
                employeeTask.setStatus(taskStatus);
                employeeTask.setStartDate(clonedTask.getStartDate());
                employeeTask.setEndDate(clonedTask.getDueDate());
                employeeTask.setNewTask(true);
                employeeTaskManager.create(employeeTask);

                clonedTask.setEstimatedTime(null);
            }
        } else {
            EdsEmployeeTask employeeTask = new EdsEmployeeTask();
            employeeTask.setTask(clonedTask);
            employeeTask.setProjectEmployee(projectEmployeeManager.getProjectEmployee(cloneProject.getManager(), cloneProject));
            employeeTask.setDeleted(true);
            employeeTask.setStatus(taskStatus);
            employeeTask.setStartDate(clonedTask.getStartDate());
            employeeTask.setEndDate(clonedTask.getDueDate());
            employeeTask.setNewTask(true);
            employeeTaskManager.create(employeeTask);

            clonedTask.setEstimatedTime(null);
        }
        clonedTask.setCalculated(false);
        clonedTask.setChangedCalculationFields(true);
        taskRbacManager.addRbacEntries(clonedTask);
        return clonedTask;
    }


    private EdsTask cloneTaskToAllProjectMembers(EdsProject cloneProject, EdsTask task, CloneTaskItem cloneTaskItem, List<Integer> employees, EdsReference taskStatus, EdsUser user, List<EdsProjectEmployee> clonedProjectMembers, long daysCount) {
        EdsTask cloneTask = task.cloneShallow();
        cloneTask.clear();
        cloneTask.setParentWS(null);
//        cloneTask.setTaskCustomFields(null);
        cloneTask.setProject(cloneProject);
        if (/*task*/cloneProject.getCreator() != null && employees.contains(/*task*/cloneProject.getCreator().getObjectID())) {
            cloneTask.setCreator(cloneProject.getCreator());
        } else {
            cloneTask.setCreator(user);
        }
        cloneTask.setCreationTime(new Date());
        cloneTask.setLastUpdateTime(new Date());
        cloneTask.setTimespent(null);
        cloneTask.setActualWageAmount(0d);
        cloneTask.setActualClientChargeAmount(0d);
        cloneTask.setPercent(null);
        if (task.getIntNumber() != null) {
            cloneTask.setIntNumber(task.getIntNumber());
        }
        if (task.getNumber() != null) {
            cloneTask.setNumber(task.getNumber());
        }

        if (cloneTaskItem.isAdjustByProjectStartDate()) {
            long days = 0;
            if (task.getDueDate() != null) {
                days = task.getDueDate().getTime() - task.getStartDate().getTime();
            }
            long startDate = cloneProject.getStartDate().getTime() + daysCount;
            long endDate = cloneProject.getStartDate().getTime() + days + daysCount;
            cloneTask.setStartAndDueDates(new Date(startDate), new Date(endDate));

        }
        cloneTask.setDeleted(false);
        cloneTask.setStatus(taskStatus);
        //Clone Custom Fields
        EdsTaskCustomFields edsTaskCustomFields = task.getTaskCustomFields();
        if (edsTaskCustomFields != null) {
            List<CompanyCustomFieldItem> customFieldItems = CustomFieldsUtils.setRPCCustomFieldItems(task.getTaskCustomFields(), commonService.getCompanyCustomFields(ViewName.Task));
            customFieldItems.forEach(cf -> cf.setObjectId(null));
            EdsTaskCustomFields edsTaskCusromFields = createTaskCustomFields(customFieldItems);
            cloneTask.setTaskCustomFields(edsTaskCusromFields);
        }

        EdsTask clonedTask = taskManager.merge(cloneTask);
        clonedTask.setNewItem(true);
        baseEventPostProcessor.registerEvent(TaskSolrEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, clonedTask, user);

        if (clonedProjectMembers != null && clonedProjectMembers.size() > 0) {
            for (EdsProjectEmployee pEmp : clonedProjectMembers) {
                EdsEmployeeTask empTask = new EdsEmployeeTask(clonedTask, pEmp);
                if (cloneTaskItem.isAdjustByProjectStartDate()) {
                    empTask.setStartDate(cloneProject.getStartDate());
                } else {
                    empTask.setStartDate(clonedTask.getStartDate());
                }
                empTask.setTimeSpent(null);
                empTask.setActualWageAmmount(0d);
                empTask.setActualClientChargeAmmount(0d);
                empTask.setPercent(null);
                empTask.setDeleted(false);
                empTask.setStatus(taskStatus);
                empTask.setEndDate(clonedTask.getDueDate());
                empTask.setNewTask(true);
                EdsEmployeeTask clonedEmpTask = employeeTaskManager.merge(empTask);
                clonedTask.getAssignments().add(clonedEmpTask);

                baseEventPostProcessor.registerEvent(EmployeeTaskEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, clonedEmpTask, user);
            }
        } else {
            EdsEmployeeTask employeeTask = new EdsEmployeeTask();
            employeeTask.setTask(clonedTask);
            employeeTask.setProjectEmployee(projectEmployeeManager.getProjectEmployee(cloneProject.getManager(), cloneProject));
            employeeTask.setDeleted(true);
            employeeTask.setStatus(taskStatus);
            employeeTask.setStartDate(clonedTask.getStartDate());
            employeeTask.setEndDate(clonedTask.getDueDate());
            employeeTask.setNewTask(true);
            employeeTaskManager.create(employeeTask);

            clonedTask.setEstimatedTime(null);
        }
        clonedTask.setLastUpdateTime(new Date());
        clonedTask.setCalculated(false);
        clonedTask.setChangedCalculationFields(true);
        taskRbacManager.addRbacEntries(clonedTask);
        return clonedTask;
    }

    private EdsWorkStream cloneWorkstream(EdsProject cloneProject, EdsWorkStream workstream, CloneTaskItem cloneTaskItem, Map<EdsWorkStream, EdsWorkStream> clonedWs, Map<EdsTask, EdsTask> clonedTasks, EdsUser user, long daysCount) {
        EdsWorkStream cloneStream = workstream.cloneShallow();
        cloneStream.setProject(cloneProject);
        if (cloneProject.getCreator() != null) {
            cloneStream.setCreator(cloneProject.getCreator());
        } else {
            cloneStream.setCreator(user);
        }
        cloneStream.setCreationTime(new Date());
        cloneStream.setLastUpdateTime(new Date());
        cloneStream.setActualTime(0);
        cloneStream.setActualWageAmount(0d);
        cloneStream.setActualClientChargeAmount(0d);
        cloneStream.setPercent(null);
        //
        cloneStream.setWageAmmount(0d);
        cloneStream.setClientChargeAmmount(0d);
        //
        if (cloneTaskItem.isAdjustByProjectStartDate()) {

            long startDate = cloneProject.getStartDate().getTime() + (daysCount > 0 ? daysCount : 0);
            cloneStream.setStartDate(new Date(startDate));
            long days = 0;
            if (workstream.getEndDate() != null) {
                days = workstream.getEndDate().getTime() - workstream.getStartDate().getTime();
            }
            long endDate = cloneProject.getStartDate().getTime() + days + (daysCount > 0 ? daysCount : 0);
            cloneStream.setEndDate(new Date(endDate));
        }
        cloneStream = workStreamManager.merge(cloneStream);

        cloneStream.setEstimatedTime(0);
        cloneStream.setPlannedWageAmount(0.0);
        cloneStream.setPlannedClientChargeAmount(0.0);

        for (EdsTask task : workstream.getTasks()) {

            if (clonedTasks.get(task) != null) {
                EdsTask cTask = taskManager.get(clonedTasks.get(task).getObjectID());
                if (cTask != null) {
                    cloneStream.getTasks().add(cTask);
                    cTask.setParent(cloneStream);
                    cTask.setLastUpdateTime(new Date());
                    taskManager.update(cTask);
                }
            }

        }

        boolean isFirstSubWorkStream = true;
        Date firstSubWorkStreamStartDate = null;
        long subWorkStreamDaysCount = 0;
        for (EdsWorkStream subWs : workstream.getSubWorkStreams()) {
            if (!subWs.isDeleted()) {
                EdsWorkStream clonedSub = clonedWs.get(subWs);
                if (clonedSub != null) {
                    cloneStream.getSubWorkStreams().add(clonedWs.get(subWs));
                } else {
                    if (isFirstSubWorkStream) {
                        isFirstSubWorkStream = false;
                        firstSubWorkStreamStartDate = subWs.getStartDate();
                    } else {
                        if (firstSubWorkStreamStartDate != null && subWs.getStartDate() != null) {
                            subWorkStreamDaysCount = subWs.getStartDate().getTime() - firstSubWorkStreamStartDate.getTime();
                        }
                    }
                    clonedSub = cloneWorkstream(cloneProject, subWs, cloneTaskItem, clonedWs, clonedTasks, user, subWorkStreamDaysCount);
                    clonedWs.put(subWs, clonedSub);
                    cloneStream.getSubWorkStreams().add(clonedSub);
                }
            }
        }
        return cloneStream;
    }

    private void adjustParentWSDates(EdsWorkStream workstream) {
        if (workstream.getParentWS() != null) {
            if (workstream.getParentWS().getStartDate().getTime() > workstream.getStartDate().getTime()) {
                workstream.getParentWS().setStartDate(workstream.getStartDate());
            }
            if (workstream.getParentWS().getEndDate().getTime() < workstream.getEndDate().getTime()) {
                workstream.getParentWS().setEndDate(workstream.getEndDate());
            }
            adjustParentWSDates(workstream.getParentWS());
        }
    }

    @Transactional
    public EdsTaskCustomFields createTaskCustomFields(List<CompanyCustomFieldItem> customFieldItems) {
        if (customFieldItems != null && customFieldItems.size() != 0) {
            EdsTaskCustomFields edsTaskCustomFields;
            if (customFieldItems.get(0).getObjectId() != null) {
                edsTaskCustomFields = taskCFManager.get(customFieldItems.get(0).getObjectId());
            } else {
                boolean isEmpty = true;
                for (CompanyCustomFieldItem fieldItem : customFieldItems) {
                    if ((fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue()))
                            || fieldItem.getFieldDateNonConvertedValue() != null || (fieldItem.getAttachments() != null && fieldItem.getAttachments().length > 0)
                            || (fieldItem.getSelectItems() != null && fieldItem.getSelectItems().size() > 0)) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    return null;
                }
                edsTaskCustomFields = new EdsTaskCustomFields();
                taskCFManager.create(edsTaskCustomFields);
            }
            CustomFieldsUtils.setDomenObjectCustomFields(edsTaskCustomFields, customFieldItems);
            return edsTaskCustomFields;
        }
        return null;
    }
}
