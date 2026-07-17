package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: Oct 9, 2014
 * Time: 11:23:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class RecurringBgTaskEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsTask> TYPE = new WfmType<>(EventTypes.recurringBgTaskEventListener);
    @Autowired
    private TaskServiceLocal taskService;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        SecurityContext.getInstance().setStaticUserID(event.getSourceID());
        taskService.createTaskRecurringInstancesBg(event.getEntityID(), event.getSourceID());
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
