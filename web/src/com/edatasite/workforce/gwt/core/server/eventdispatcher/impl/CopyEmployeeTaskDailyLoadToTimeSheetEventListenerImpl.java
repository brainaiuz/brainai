package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityCircularResolver;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Ilhombek
 * Date: 3/13/13
 * Time: 6:55 PM
 */
@Transactional
public class CopyEmployeeTaskDailyLoadToTimeSheetEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsCompany> TYPE = new WfmType<>(EventTypes.copyEmployeeTaskDailyLoadToTimeSheetEventListener);

    @Autowired
    private AvailabilityCircularResolver availabilityCircularResolver;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        try {
            //create or update daily timeSheet estimated time
            Integer companyID = event.getCompanyId();
            availabilityCircularResolver.copyEmployeeTaskDailyLoadToTimeSheet(companyID);
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

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
    }
}