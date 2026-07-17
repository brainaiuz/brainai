package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.rbac.EdsTaskRbac;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TaskRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * User: Abdulaziz
 * Date: Jul 31, 2010
 * Time: 3:40:57 PM
 */
@Transactional
public class TaskRbacToSolrEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsTaskRbac> TYPE = new WfmType<>(EventTypes.taskRbacToSolrEventListener);

    public static String EVENT_ADD = "TASK_RBAC_ENTRY_ADD";
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskRbacManager taskRbacManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_ADD.equals(event.getEventType())) {
            onRbacEntryAdd(event);
        }
    }

    private void onRbacEntryAdd(EdsBusinessEvent event) {
        EdsTaskRbac tEntry = taskRbacManager.get(event.getEntityID());

        try {
            solrManager.addTaskRbacEntryToSolr(tEntry, tEntry.getProject().getCompany());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

}
