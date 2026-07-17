package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

/*
 * User: Abdullo
 * Date: Apr 11, 2011
 * Time: 3:18:20 PM
 */
@Transactional
public class StatusTaskEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsTask> TYPE = new WfmType<>("statusTaskEventListenerString");
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsTask completedTask = taskManager.get(event.getEntityID());
        EdsUser updater = userManager.get(event.getSourceID());
        //send completed predecessor task email notification
        ArrayList<Integer> userIdsSentToEmailNotification = new ArrayList<>();
        if (!event.isSendMail1()) {
            if (completedTask.getSuccessors() != null && !completedTask.getSuccessors().isEmpty()) {
                for (EdsTask successorTask : completedTask.getSuccessors()) {
                    for (EdsEmployeeTask employee : successorTask.getUnDeletedAssignments()) {
                        try {
                            EdsUser userId = employee.getProjectEmployee().getEmployeeDepartment().getEmployee();
                            boolean isEnableNotification = emailNotificationSettingsManager.hasEmailNotification(userId.getObjectID(), EmailNotificationConstants.SUCCESSOR_TASK_COMPLETED_NOTIFICATION);
                            if (isEnableNotification) {
                                if (!userIdsSentToEmailNotification.contains(userId.getObjectID())) {
                                    userIdsSentToEmailNotification.add(userId.getObjectID());
                                }
                                messageManager.sendCompletedPredTaskNotification(employee, updater, completedTask.getName());
                            }
                            event.setSendMail1(true);
                            event.setStatus(EventStatus.COMPLETED.name());
                        } catch (EdsDbException e) {
                            event.setSendMail1(false);
                        }
                    }
                }
            } else {
                event.setSendMail1(true);
                event.setStatus(EventStatus.COMPLETED.name());
            }
        }


        if (completedTask.getSuccessors() != null && !completedTask.getSuccessors().isEmpty()) {
            for (EdsTask task : completedTask.getSuccessors()) {
                try {
                    taskSolrComponent.index(task);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (!(event.isSendMail1())) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

}
