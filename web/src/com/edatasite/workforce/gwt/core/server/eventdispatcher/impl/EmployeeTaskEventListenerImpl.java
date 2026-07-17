package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTaskManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Abdulaziz
 * Date: Jan 4, 2010
 * Time: 5:25:12 PM
 */
@Transactional
public class EmployeeTaskEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsEmployeeTask> TYPE = new WfmType<>(EventTypes.employeeTaskEventListener);
    @Autowired
    private EmployeeTaskManager employeeTaskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsEmployeeTask employeeTask = employeeTaskManager.get(event.getEntityID());

        if (employeeTask.getEstimatedTime() == null || employeeTask.getEstimatedTime() == 0) {
            employeeTask.setPercent(0f);
        } else {
            employeeTask.setPercent(employeeTaskManager.getEmployeeTaskActualPercentCompleted(employeeTask.getObjectID()));
        }

        EdsTask task = employeeTask.getTask();
        task.setPreviousPercent(employeeTask.getTask().getPercent());

        if (task.getEstimatedTime() == null || task.getEstimatedTime() == 0) {
            task.setPercent(0f);
        } else {
            task.setPercent(taskManager.getTaskActualPercentCompleted(task.getObjectID()));
        }

        EdsProject project = task.getProject();
        project.setPercent(projectManager.getProjectActualPercentCompleted(project.getObjectID()));
        event.setStatus(EventStatus.COMPLETED.name());
        employeeTaskManager.update(employeeTask);
        projectManager.update(project);

        try {
            taskSolrComponent.index(task);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

        try {
            projectSolrComponent.index(project);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void onAddEvent(EdsBusinessEvent event) {
        EdsEmployeeTask employeetask = employeeTaskManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        EdsUser receiver = employeetask.getProjectEmployee().getEmployeeDepartment().getEmployee();
        if (receiver != null) {
            notificationMsgManager.createTaskAssigneeNotificationEvent(employeetask.getTask().getObjectID(), user.getObjectID(), receiver.getObjectID(), ActionOnEntityEnum.ASSIGNED);
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                if (receiver != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeTaskAssignUpdate(employeetask.getTask(), receiver, user, event.getTime());
                    if (myUpdate != null) {
                        myUpdate.setSuperUser(event.isSuperUser());
                    }
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsEmployeeTask employeetask = employeeTaskManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeTaskStatusChangeUpdate(employeetask, user, event.getTime());
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsEmployeeTask employeetask = employeeTaskManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        EdsUser receiver = employeetask.getProjectEmployee().getEmployeeDepartment().getEmployee();
        if (!event.isMyUpdatesItemDelete()) {
            try {
                if (receiver != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerEmployeeTaskAssigneeDeleteUpdate(employeetask.getTask(), receiver, user, event.getTime());
                    if (myUpdate != null) {
                        myUpdate.setSuperUser(event.isSuperUser());
                    }
                }
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
