package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
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
 * Date: Jul 30, 2010
 * Time: 7:45:30 PM
 */
@Transactional
public class FolderRemoveGroupEntriesEventListener extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsGroup> TYPE = new WfmType<>(EventTypes.folderRemoveGroupEntriesEventListener);
    public static String EVENT_FOLDER_REMOVE_GROUP_ENTRIES = "FOLDER_REMOVE_GROUP_ENTRIES";

    @Autowired
    private SolrManager solrManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
       if(EVENT_FOLDER_REMOVE_GROUP_ENTRIES.equals(event.getEventType())){
           try {
               solrManager.removeGroupEntries(event.getEntityID());
               event.setStatus(EventStatus.COMPLETED.name());
           } catch (SolrServerException | IOException e) {
               event.setStatus(EventStatus.FAILED.name());
           }
       }
    }
}
