package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Ilhombek
 * Date: 27.01.2011
 * Time: 20:25:48
 */
@Transactional
public class TaskRegisterUpdatesEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsTask> TYPE = new WfmType<>(EventTypes.taskRegisterUpdatesEventListener);
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private EmployeeManager employeeManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, creator, event.getTime(), EdsMyUpdate.ADD);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        if (!event.isSendMail2()) {
            ArrayList<Integer> taskProjectManagersIds = new ArrayList<>();
            if (!task.getProject().getManager().getObjectID().equals(creator.getObjectID())) {
                taskProjectManagersIds.add(task.getProject().getManager().getObjectID());
            }
            for (EdsEmployee backupManager : task.getProject().getBackupManagers()) {
                if (backupManager.getObjectID().equals(creator.getObjectID())) {
                    continue;
                }
                if (!taskProjectManagersIds.contains(backupManager.getObjectID())) {
                    taskProjectManagersIds.add(backupManager.getObjectID());
                }
            }
            boolean notTaskAssignees = task.getUnDeletedAssignments().size() == 0;
            boolean enableSendAddTaskEmailNotificationToDLs = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.ENABLE_SEND_ADD_TASK_EMAIL_NOTIFICATION_TO_DEPARTMENT_LEADERS);
            //send email notification to company department leaders too, for 31733 company (custom request, - for Mirahmedov's client)
            /*Integer.valueOf(31733).equals(creator.getCompany().getObjectID()) || Integer.valueOf(35444).equals(creator.getCompany().getObjectID())*/
            if (enableSendAddTaskEmailNotificationToDLs && notTaskAssignees) {
                try {
                    List<EdsEmployee> departmentLeaders = employeeManager.getEmployeeByRole(EdsRole.TL);
                    for (EdsEmployee dLeader : departmentLeaders) {
                        if (!taskProjectManagersIds.contains(dLeader.getObjectID())) {
                            /*boolean isEnableNotification = emailNotificationSettingsManager.hasEmailNotification(dLeader.getObjectID(), EmailNotificationConstants.TASK_ADD_NOTIFICATION);*/
                            /*if (isEnableNotification) {*/
                            messageManager.sendTaskAddNotification(task, creator, dLeader, true);
                            /*}*/
                        }
                    }
                    event.setSendMail2(true);
                } catch (EdsDbException e) {
                    event.setSendMail2(false);
                    event.setStatus(EventStatus.FAILED.name());
                }
            } else {
                event.setSendMail2(true);
            }
        }

        if (event.isMyUpdatesItemAdd() && event.isSendMail2()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser deleter = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, deleter, event.getTime(), EdsMyUpdate.DELETE);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemDelete(true);
            } catch (Exception e) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemDelete()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser updater = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = taskManager.registerTaskAllUpdates(task, updater, event.getTime(), EdsMyUpdate.EDIT);
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

}
