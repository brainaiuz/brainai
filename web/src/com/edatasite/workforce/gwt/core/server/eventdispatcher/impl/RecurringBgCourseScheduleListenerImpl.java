package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourseSchedule;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import org.springframework.beans.factory.annotation.Autowired;

public class RecurringBgCourseScheduleListenerImpl implements BusinessEventListener {

    public static WfmType<EdsCourseSchedule> TYPE = new WfmType<>(EventTypes.recurringBgCourseScheduleEventListener);
    @Autowired
    private TCService tcService;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        tcService.saveCourseScheduleInstance(event.getEntityID());
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }
}
