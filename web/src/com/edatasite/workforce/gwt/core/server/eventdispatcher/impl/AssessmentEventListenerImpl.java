package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentService;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 04.02.2011
 * Time: 22:32:30
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class AssessmentEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEmployeeAssessment> TYPE = new WfmType<>(EventTypes.employeeAssessmentEventListener);

    @Autowired
    private AssessmentService assessmentService;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        try {
            assessmentService.sendAssessmentResultToEmployee(event.getEntityID(), event.getSourceID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }
}
