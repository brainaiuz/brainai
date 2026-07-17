package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.profile.client.ui.EmailNotificationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 6, 2010
 * Time: 5:22:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class DepartmentEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsDepartment> TYPE = new WfmType<>(EventTypes.departmentEventListener);
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;

    public void onAddEvent(EdsBusinessEvent event) {

        EdsDepartment department = departmentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isSendMail1()) {
            try {
                messageManager.sendTeamAddNotification(department, user);
                event.setSendMail1(true);
            } catch (EdsDbException e) {
                event.setSendMail1(false);
            }
        }

        if (!event.isSendMail2()) {
            if (department.getLeader() != null) {
                boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                        department.getLeader().getObjectID(), EmailNotificationConstants.DEPARTMENT_LEADER_ASSIGN_NOTIFICATION);
                if (emailNotificationSettings) {
                    try {
                        messageManager.sendTeamLeaderAssignNotification(department, user);
                        event.setSendMail2(true);
                    } catch (EdsDbException e) {
                        event.setSendMail2(false);
                    }
                } else {
                    event.setSendMail2(true);
                }
            } else {
                event.setSendMail2(true);
            }
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentAddUpdate(department, user, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd() && event.isSendMail1() && event.isSendMail2()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    public void onEditEvent(EdsBusinessEvent event) {
        EdsDepartment department = departmentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemEdit()) {
            try {
                if (user.getObjectID().equals(department.getLeader().getObjectID())) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentEditUpdate(department, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                } else {
                    EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentEditUpdate(department, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());

                    EdsMyUpdate myUpdate1 = myUpdateManager.registerDepartmentLeaderEditUpdate(department, department.getLeader(), user, event.getTime());
                    myUpdate1.setPrivateUpdate(true);
                    myUpdate1.setSuperUser(event.isSuperUser());
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
        EdsDepartment department = departmentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                if (user.getObjectID().equals(department.getLeader().getObjectID())) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentDeleteUpdate(department, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                } else {
                    EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentDeleteUpdate(department, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());

                    EdsMyUpdate myUpdate1 = myUpdateManager.registerDepartmentLeaderDeleteUpdate(department, department.getLeader(), user, event.getTime());
                    myUpdate1.setPrivateUpdate(true);
                    myUpdate1.setSuperUser(event.isSuperUser());
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
