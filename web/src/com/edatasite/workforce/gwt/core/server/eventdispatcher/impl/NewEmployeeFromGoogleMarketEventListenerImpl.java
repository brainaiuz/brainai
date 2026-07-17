package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Ilhombek
 * Date: 22.05.2010
 * Time: 18:56:28
 */
@Transactional
public class NewEmployeeFromGoogleMarketEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.employeeEventFromGoogleMarketListener);
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsEmployee employee = employeeManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (!event.isSendMail1()) {
            try {
                messageManager.sendEmployeeAddNotificationFromGoogleMarket(employee, user);
                event.setSendMail1(true);
            } catch (EdsDbException e) {
                event.setSendMail1(false);
            }
        }
        if (event.isSendMail1()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
