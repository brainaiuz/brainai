package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorkStream;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.solr.component.TaskSolrComponent;
import com.edatasite.workforce.gwt.core.server.db.TaskManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullo
 * Date: May 5, 2011
 * Time: 5:41:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class WorkstreamEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsWorkStream> TYPE = new WfmType<>(EventTypes.workstreamEventListener);

    @Autowired
    private TaskManager taskManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskSolrComponent taskSolrComponent;

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        List<EdsTask> tasks = taskManager.listByParentId(event.getEntityID());
        EdsUser updater = userManager.get(event.getSourceID());
        if (!event.isSolrIndexed()) {
            try {
                taskSolrComponent.indexes(tasks);
                event.setSolrIndexed(true);
            } catch (Exception e) {
                e.printStackTrace();
                event.setSolrIndexed(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isSolrIndexed()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

}
