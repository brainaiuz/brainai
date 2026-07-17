package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Jan 6, 2010
 * Time: 6:16:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ExistingEmployeeEventListenerImpl implements BusinessEventListener, Constants {

    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.existingEmployeeEventListener);
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    public void onAddEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());

        if (!event.isSendMail1()) {
            try {
                messageManager.sendEmployeeAddNotificationForExistingUserName(employee, user);
                event.setSendMail1(true);
            } catch (EdsDbException e) {
                event.setSendMail1(false);
            }
//        }
        }
        if (event.isSendMail1()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
