package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 27.01.2011
 * Time: 15:23:18
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ImportTasksFromMPPFileEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.importTasksFromMPPFileEventListener);
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerProjectImportTasksFromMSProject(project, creator, event.getTime());
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }
}
