package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Dilsh0d Madrahimov
 * Date: 01.07.2010
 * Time: 19:13:39
 */
@Transactional
public class ApiEmployeeProfilePicUploadEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsEmployee> TYPE = new WfmType<>(EventTypes.apiEmployeeProfilePicUploadEventListener);
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        try {
            documentsServiceLocal.saveEmployeeProfilePicture(event.getCompanyId(), event.getSourceID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (RuntimeException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
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