package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.ProjectSolrComponent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 27.01.2011
 * Time: 15:20:31
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class TaskSolrEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsTask> TYPE = new WfmType<>(EventTypes.taskSolrEventListener);

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private TaskRbacManager taskRbacManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.index(task);
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.index(task);
                taskRbacManager.addRbacEntries(task);
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.CHANGED_PROJECT_PERCENT)) {
                    projectSolrComponent.index(task.getProject());
                }
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        EdsUser deleter = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                solrManager.removeTask(task, deleter.getCompany());
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsTask task = taskManager.get(event.getEntityID());
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.deleteByTaskId(task.getObjectID());
                taskSolrComponent.index(task);
                event.setSolrIndexed(true);
            } catch (Exception ex) {
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

}
