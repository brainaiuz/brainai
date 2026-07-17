package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.GlobalAuthManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: sherali
 * Date: 3/5/11
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class UserAuthEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsUser> TYPE = new WfmType<>(EventTypes.userAuthEventListener);

    @Autowired
    private GlobalAuthManager globalAuthManager;
    @Autowired
    private UserManager userManager;

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getEntityID());
        globalAuthJdbcSpringManager.updateUserEmail(user);
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
