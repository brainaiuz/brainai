package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsTimeSheet;
import com.edatasite.workforce.core.domain.EdsTimeSheetApprovalSession;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSheetApprovalSessionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: admin
 * Date: Jan 22, 2010
 * Time: 9:13:20 PM
 */
@Transactional
public class TimeSheetEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsTimeSheetApprovalSession> TYPE = new WfmType<>(EventTypes.timesheetEventListener);
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TimeSheetApprovalSessionManager timeSheetApprovalSessionManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ReferenceManager referenceManager;
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
    }

    public void onAddEvent(EdsBusinessEvent event) {
        EdsTimeSheetApprovalSession timesheetSession = timeSheetApprovalSessionManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        EdsProject project = timesheetSession.getProject();
        EdsEmployee approver = null;
        if (event.getAdditionalSourceID() != null && event.getAdditionalSourceID() > 0) {
            approver = employeeManager.get(event.getAdditionalSourceID());
        }

        boolean sendMail2 = true;
        boolean tshManWaitUpdate = true;
        boolean tshBManWaitUpdate = true;
        EdsEmployee projectManager = project.getManager();
        if (!event.isSendMail1()) {
            try {
                messageManager.sendTimeSheetForApprovalToManager(timesheetSession, (approver != null ? approver : projectManager));
                event.setSendMail1(true);
            } catch (EdsDbException e) {
                event.setSendMail1(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        notificationMsgManager.createTimeSheetApprovalNotification(ActionOnEntityEnum.WAIT_APPROVAL, timesheetSession, (approver != null ? approver.getObjectID() : projectManager.getObjectID()));

        if (approver == null) {
            for (EdsEmployee backupManager : project.getBackupManagers()) {
                if (!event.isSendMail2()) {
                    try {
                        messageManager.sendTimeSheetForApprovalToManager(timesheetSession, backupManager);
                        event.setSendMail2(true);
                    } catch (EdsDbException e) {
                        event.setSendMail2(false);
                        sendMail2 = false;
                        event.setStatus(EventStatus.FAILED.name());
                    }
                }
                //event for backup
                if (!backupManager.getObjectID().equals(user.getObjectID())) {
                    if (!event.isMyUpdatesItemEdit()) {
                        try {
                            EdsMyUpdate myUpdate = myUpdateManager.registerTimesheetManagerWaitingUpdate(timesheetSession, backupManager, user, event.getTime());
                            myUpdate.setPrivateUpdate(true);
                            myUpdate.setSuperUser(event.isSuperUser());
                            event.setMyUpdatesItemEdit(true);
                        } catch (Exception e) {
                            event.setMyUpdatesItemEdit(false);
                            tshBManWaitUpdate = false;
                        }
                    }
                }
            }
        }

        // event for employee
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerTimesheetWaitingUpdate(timesheetSession, user, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        // event for manager
        Integer timeSheetApproverID = approver != null ? approver.getObjectID() : projectManager.getObjectID();
        if (!timeSheetApproverID.equals(user.getObjectID())) {
            if (!event.isMyUpdatesItemEdit()) {
                try {
                    EdsMyUpdate myUpdate = myUpdateManager.registerTimesheetManagerWaitingUpdate(timesheetSession, (approver != null ? approver : projectManager), user, event.getTime());
                    myUpdate.setPrivateUpdate(true);
                    myUpdate.setSuperUser(event.isSuperUser());
                    event.setMyUpdatesItemEdit(true);
                } catch (Exception e) {
                    event.setMyUpdatesItemEdit(false);
                    tshManWaitUpdate = false;
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
        }
        if (event.isSendMail1() && sendMail2 && tshBManWaitUpdate && tshManWaitUpdate && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsTimeSheetApprovalSession timesheetSession = timeSheetApprovalSessionManager.get(event.getEntityID());
        EdsUser manager = userManager.get(event.getSourceID());

        EdsProject project = timesheetSession.getProject();
        EdsReference status = timesheetSession.getStatus();

        EdsReference reject = referenceManager.findReference("_TIME_SHEET_ENTRY_STATUS", "_REJECT");
        Set<EdsTimeSheet> rejecteds = new HashSet<>();
        for (EdsTimeSheet titem : timesheetSession.getTimeentries()) {
            if (reject.equals(titem.getStatus())) {
                rejecteds.add(titem);
            }
        }
        timesheetSession.getTimeentries().removeAll(rejecteds);

        if (!event.isSendMail1()) {
            try {
                messageManager.sendTimeSheetProceededNotification(timesheetSession, rejecteds, manager);
                event.setSendMail1(true);
            } catch (EdsDbException ex) {
                event.setSendMail1(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        boolean rejected = status.getCode().equals("_REJECTED");
        boolean approved = status.getCode().equals("_APPROVED");

        if (approved) {
            // event to employee
            if (!event.isMyUpdatesItemEdit()) {
                try {
                    EdsMyUpdate myUpdate = myUpdateManager.registerTimesheetEmployeeApprovedUpdate(timesheetSession, timesheetSession.getEmployee(), manager, event.getTime());
                    myUpdate.setPrivateUpdate(true);
                    myUpdate.setSuperUser(event.isSuperUser());
                    // event to manager
                    if (!manager.getObjectID().equals(timesheetSession.getEmployee().getObjectID())) {
                        EdsMyUpdate myUpdate1 = myUpdateManager.registerTimesheetApprovedUpdate(timesheetSession, manager, event.getTime());
                        myUpdate1.setSuperUser(event.isSuperUser());
                    }
                    event.setMyUpdatesItemEdit(true);
                } catch (Exception e) {
                    event.setMyUpdatesItemEdit(false);
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
            notificationMsgManager.updateClickedNotificationEvent(timesheetSession.getObjectID(), NotificationTypeEnum.TimeSheetApproval, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createTimeSheetApprovalNotification(ActionOnEntityEnum.APPROVED, timesheetSession, manager.getObjectID());
        }
        if (rejected) {
            // event to employee
            if (!event.isMyUpdatesItemEdit()) {
                try {
                    EdsMyUpdate myUpdate = myUpdateManager.registerTimesheetEmployeeRejectedUpdate(timesheetSession, timesheetSession.getEmployee(), manager, event.getTime());
                    myUpdate.setPrivateUpdate(true);
                    myUpdate.setSuperUser(event.isSuperUser());
                    // event to manager
                    if (!manager.getObjectID().equals(timesheetSession.getEmployee().getObjectID())) {
                        EdsMyUpdate myUpdate1 = myUpdateManager.registerTimesheetRejectedUpdate(timesheetSession, manager, event.getTime());
                        myUpdate1.setSuperUser(event.isSuperUser());
                    }
                    event.setMyUpdatesItemEdit(true);
                } catch (Exception e) {
                    event.setMyUpdatesItemEdit(false);
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
            notificationMsgManager.updateClickedNotificationEvent(timesheetSession.getObjectID(), NotificationTypeEnum.TimeSheetApproval, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createTimeSheetApprovalNotification(ActionOnEntityEnum.REJECTED, timesheetSession, manager.getObjectID());
        }
        if (event.isSendMail1() && event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        try {
            projectSolrComponent.index(project);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
        List<EdsTask> tasks = new ArrayList<>();
        for (EdsTimeSheet edsTimeSheet : timesheetSession.getTimeentries()) {
            tasks.add(edsTimeSheet.getEmployeeTask().getTask());
        }
        //update task to solr
        try {
            taskSolrComponent.indexes(tasks);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }

    }

    public void onDeleteEvent(EdsBusinessEvent event) {
    }
}