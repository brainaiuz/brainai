package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.EmailNotificationSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
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
 * Time: 5:29:59 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class EmployeeDepartmentEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEmployeeDepartment> TYPE = new WfmType<>(EventTypes.employeeDepartmentEventListener);
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private EmailNotificationSettingsManager emailNotificationSettingsManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    public void onAddEvent(EdsBusinessEvent event) {

        EdsEmployeeDepartment emplDep = employeeDepartmentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        //Create new employee project, employee task for newly assigned department
        EdsEmployeeDepartment oldEmplDep = employeeDepartmentManager.get(event.getAdditionalSourceID());
        projectEmployeeManager.deleteAndCreateProjectEmployee(oldEmplDep, emplDep);

        boolean emailNotificationSettings = emailNotificationSettingsManager.hasEmailNotification(
                emplDep.getEmployee().getObjectID(), EmailNotificationConstants.DEPARTMENT_EMPLOYEE_ASSIGN_NOTIFICATION);
        if (!event.isSendMail1()) {
            try {
                if (emailNotificationSettings) {
                    try {
                        messageManager.sendTeamAssignNotification(emplDep, user);
                    } catch (EdsDbException e) {
                        event.setSendMail1(false);
                    }
                }
                event.setSendMail1(true);
            } catch (Exception ex) {
                event.setSendMail1(false);
            }
        }
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentEmployeeAddUpdate(emplDep.getTeam(), emplDep.getEmployee(), user, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSendMail1() && event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {

    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsEmployeeDepartment emplDep = employeeDepartmentManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerDepartmentEmployeeDeleteUpdate(emplDep.getTeam(), emplDep.getEmployee(), user, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
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
