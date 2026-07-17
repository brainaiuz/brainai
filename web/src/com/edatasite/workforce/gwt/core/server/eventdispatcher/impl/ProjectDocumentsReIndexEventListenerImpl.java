package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 8/18/11
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ProjectDocumentsReIndexEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectDocumentsReIndexEventListener);

    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private ProjectManager projectManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        if (!event.isRbacIndexed()) {
            try {
                commonServiceLocal.reIndexProjectDocument(project.getObjectID());
                event.setRbacIndexed(true);
            } catch (Exception ex) {
                event.setRbacIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isRbacIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }
}
