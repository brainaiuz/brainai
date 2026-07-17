package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.issue.EdsIssue;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.IssueManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: admin
 * Date: Jan 20, 2010
 * Time: 8:42:08 PM
 */
@Transactional
public class IssueEventListenerImpl implements BusinessEventListener, Constants {
    public static WfmType<EdsIssue> TYPE = new WfmType<>(EventTypes.issueEventListener);
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ClientContactManager clientContactManager;

    public void onAddEvent(EdsBusinessEvent event) {
        EdsIssue issueDomain = issueManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        String issueType = null;

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpd = myUpdateManager.registerIssueAddUpdate(issueDomain, creator, event.getTime(), issueType);
                myUpd.setPrivateUpdate(true);
                myUpd.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception e) {
                event.setMyUpdatesItemAdd(false);
            }
        }
        if (!event.isSendMail1()) {
            if (issueDomain.getResolver() != null && creator != null &&
                    !issueDomain.getResolver().getObjectID().equals(creator.getObjectID())) {
                try {
                    messageManager.sendIssueAddNotification(issueDomain, issueDomain.getResolver(), creator);
                    event.setSendMail1(true);
                } catch (EdsDbException e) {
                    event.setSendMail1(false);
                }
            } else {
                event.setSendMail1(true);
            }
        }
        if (!event.isSendMail2()) {
            if (issueDomain.getUnDeletedAssignments() != null && issueDomain.getUnDeletedAssignments().size() > 0) {
                for (EdsEmployeeTask employeeTask : issueDomain.getUnDeletedAssignments()) {
                    EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                    if (employee != null && creator != null && (issueDomain.getResolver() == null || !employee.getObjectID().equals(issueDomain.getResolver().getObjectID())) &&
                            !employee.getObjectID().equals(creator.getObjectID())) {
                        try {
                            messageManager.sendIssueAssignNotification(issueDomain, employee, creator);
                            event.setSendMail2(true);
                        } catch (EdsDbException e) {
                            event.setSendMail2(false);
                        }
                    }
                }
            } else {
                event.setSendMail2(true);
            }
        }
        boolean sendEmailToProjectClient = false;
        if (!sendEmailToProjectClient) {
            if (issueDomain.getProject() != null) {
                EdsCrmAccount client = issueDomain.getProject().getClient();
                if (client != null && client.isClient()) {
                    List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(client);
                    for (EdsClientContact clientContact : clientContactList) {
                        try {
                            messageManager.sendIssueAddNotificationToClient(issueDomain, clientContact, creator);
                            sendEmailToProjectClient = true;
                        } catch (EdsDbException e) {
                            sendEmailToProjectClient = true;
                        }
                    }
                } else {
                    sendEmailToProjectClient = true;
                }
            } else {
                sendEmailToProjectClient = true;
            }
        }

        if (event.isMyUpdatesItemAdd() && event.isSendMail1() && event.isSendMail2() && sendEmailToProjectClient) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsIssue issueDomain = issueManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        String issueType = null;

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpd = myUpdateManager.registerIssueEditUpdate(issueDomain, creator, event.getTime(), issueType);
                myUpd.setPrivateUpdate(true);
                myUpd.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception e) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        if (!event.isSendMail1()) {
            if (issueDomain.getResolver() != null && creator != null &&
                    !issueDomain.getResolver().getObjectID().equals(creator.getObjectID())) {
                try {
                    messageManager.sendIssueUpdateNotification(issueDomain, issueDomain.getResolver(), creator);
                    event.setSendMail1(true);
                } catch (EdsDbException e) {
                    event.setSendMail1(false);
                }
            } else {
                event.setSendMail1(true);
            }
        }
        if (!event.isSendMail2()) {
            if (issueDomain.getUnDeletedAssignments() != null && issueDomain.getUnDeletedAssignments().size() > 0) {
                for (EdsEmployeeTask employeeTask : issueDomain.getUnDeletedAssignments()) {
                    EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                    if (employee != null && creator != null && (issueDomain.getResolver() == null || !employee.getObjectID().equals(issueDomain.getResolver().getObjectID())) &&
                            !employee.getObjectID().equals(creator.getObjectID())) {
                        try {
                            messageManager.sendIssueUpdateNotification(issueDomain, employee, creator);
                            event.setSendMail2(true);
                        } catch (EdsDbException e) {
                            event.setSendMail2(false);
                        }
                    }
                }
            } else {
                event.setSendMail2(true);
            }
        }
        boolean sendEmailToProjectClient = false;
        if (!sendEmailToProjectClient) {
            if (issueDomain.getProject() != null) {
                EdsCrmAccount client = issueDomain.getProject().getClient();
                if (client != null && client.isClient()) {
                    List<EdsClientContact> clientContactList = clientContactManager.getAccessEnabledContacts(client);
                    for (EdsClientContact clientContact : clientContactList) {
                        try {
                            messageManager.sendIssueUpdateNotificationToClient(issueDomain, clientContact, creator);
                            sendEmailToProjectClient = true;
                        } catch (EdsDbException e) {
                            sendEmailToProjectClient = false;
                        }
                    }
                } else {
                    sendEmailToProjectClient = true;
                }
            } else {
                sendEmailToProjectClient = true;
            }
        }

        if (event.isMyUpdatesItemEdit() && event.isSendMail1() && event.isSendMail2() && sendEmailToProjectClient) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsIssue issueDomain = issueManager.get(event.getEntityID());
        EdsUser deleter = userManager.get(event.getSourceID());
        if (!event.isSendMail1()) {
            if (issueDomain.getResolver() != null && deleter != null &&
                    !issueDomain.getResolver().getObjectID().equals(deleter.getObjectID())) {
                try {
                    messageManager.sendIssueDeleteNotification(issueDomain, issueDomain.getResolver(), deleter);
                    event.setSendMail1(true);
                } catch (EdsDbException e) {
                    event.setSendMail1(false);
                }
            } else {
                event.setSendMail1(true);
            }
        }
        if (!event.isSendMail2()) {
            if (issueDomain.getUnDeletedAssignments() != null && issueDomain.getUnDeletedAssignments().size() > 0) {
                for (EdsEmployeeTask employeeTask : issueDomain.getUnDeletedAssignments()) {
                    EdsEmployee employee = employeeTask.getProjectEmployee().getEmployeeDepartment().getEmployee();
                    if (employee != null && deleter != null && (issueDomain.getResolver() == null || !employee.getObjectID().equals(issueDomain.getResolver().getObjectID())) &&
                            !employee.getObjectID().equals(deleter.getObjectID())) {
                        try {
                            messageManager.sendIssueDeleteNotification(issueDomain, employee, deleter);
                            event.setSendMail2(true);
                        } catch (EdsDbException e) {
                            event.setSendMail2(false);
                        }
                    }
                }
            } else {
                event.setSendMail2(true);
            }
        }
        if (event.isSendMail1() && event.isSendMail2()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}