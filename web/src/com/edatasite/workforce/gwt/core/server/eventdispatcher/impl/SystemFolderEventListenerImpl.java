package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: Ilhombek
 * Date: 01.07.2010
 * Time: 19:13:39
 */
@Transactional
public class SystemFolderEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsCompany> TYPE = new WfmType<>(EventTypes.systemFolderCreateEventListener);
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        Integer companyId = event.getEntityID();
        try {
            documentsServiceLocal.createSystemFolders(companyId);
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