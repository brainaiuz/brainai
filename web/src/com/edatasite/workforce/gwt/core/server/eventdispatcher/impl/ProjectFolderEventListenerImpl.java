package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 20-Oct-2010
 * Time: 14:16:26
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ProjectFolderEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectFolderEventListener);
    @Autowired
    private CommonServiceLocal commonServiceLocal;

    public void onAddEvent(EdsBusinessEvent event) {
        commonServiceLocal.createProjectFolder(event.getEntityID());
        event.setStatus(EventStatus.COMPLETED.name());
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }
}
