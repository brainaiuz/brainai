package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectEmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 6, 2010
 * Time: 2:18:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ProjectEmployeeEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsProjectEmployee> TYPE = new WfmType<>(EventTypes.projectEmployeeEventListener);
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private ProjectEmployeeManager projectEmployeeManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;


    public void onAddEvent(EdsBusinessEvent event) {
        EdsProjectEmployee projectEmployee = projectEmployeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isSendMail1()) {
            try {
                messageManager.sendProjectAssignNotification(projectEmployee, user);
                event.setSendMail1(true);
            } catch (EdsDbException e) {
                event.setSendMail1(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }

        // Register to myUpdates
        EdsUser receiver = projectEmployee.getEmployeeDepartment().getEmployee();
        if (!event.isMyUpdatesItemAdd()) {
            try {
                if (receiver != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerProjectMemberAddUpdate(projectEmployee.getProject(), receiver, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                }
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

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    public void onEditEvent(EdsBusinessEvent event) {

    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsProjectEmployee projectEmployee = projectEmployeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        // Register to myUpdates
        EdsUser receiver = projectEmployee.getEmployeeDepartment().getEmployee();
        if (!event.isMyUpdatesItemDelete()) {
            try {
                if (receiver != null) {
                    EdsMyUpdate myUpdate = myUpdateManager.registerProjectMemberDeleteUpdate(projectEmployee.getProject(), receiver, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
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
