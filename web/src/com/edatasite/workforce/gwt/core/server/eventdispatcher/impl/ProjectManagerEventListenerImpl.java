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
 * User: admin
 * Date: Jan 6, 2010
 * Time: 2:42:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ProjectManagerEventListenerImpl implements BusinessEventListener {

    public static WfmType<EdsProject> TYPE = new WfmType<>(EventTypes.projectManagerEventListener);
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;

    public void onAddEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        if (project.getManager() != null) {
            // Register to myUpdates
            EdsUser manager = project.getManager();
            if (!event.isMyUpdatesItemAdd()) {
                try {
                    EdsMyUpdate myUpdate = myUpdateManager.registerProjectManagerAssignUpdate(project, manager, user, event.getTime());
                    myUpdate.setSuperUser(event.isSuperUser());
                    event.setMyUpdatesItemAdd(true);
                } catch (Exception e) {
                    event.setMyUpdatesItemAdd(false);
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onEditEvent(EdsBusinessEvent event) {
        onAddEvent(event);
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
    }

    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsProject project = projectManager.get(event.getEntityID());
        EdsUser deleter = userManager.get(event.getSourceID());
        if (!project.getManager().getObjectID().equals(deleter.getObjectID())) {
            EdsUser manager = project.getManager();
            if (!event.isMyUpdatesItemDelete()) {
                try {
                    EdsMyUpdate upd = myUpdateManager.registerProjectDeleteForManagersUpdate(project, manager, event.getTime());
                    upd.setPrivateUpdate(true);
                    upd.setSuperUser(event.isSuperUser());
                    event.setMyUpdatesItemDelete(true);
                } catch (Exception e) {
                    event.setMyUpdatesItemDelete(false);
                    event.setStatus(EventStatus.FAILED.name());
                }
            }
            if (event.isMyUpdatesItemDelete()) {
                event.setStatus(EventStatus.COMPLETED.name());
            }
        } else {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
