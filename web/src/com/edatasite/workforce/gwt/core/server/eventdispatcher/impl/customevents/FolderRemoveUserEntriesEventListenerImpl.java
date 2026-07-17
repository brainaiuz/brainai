package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * User: Abdulaziz
 * Date: Jul 30, 2010
 * Time: 8:16:11 PM
 */
@Transactional
public class FolderRemoveUserEntriesEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsUser> TYPE = new WfmType<>(EventTypes.folderRemoveUserEntriesEventListener);

    public static String EVENT_FOLDER_REMOVE_USER_ENTRIES = "FOLDER_REMOVE_USER_ENTRIES";

    @Audited
    private SolrManager solrManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_FOLDER_REMOVE_USER_ENTRIES.equals(event.getEventType())) {

            try {
                solrManager.removeUserEntries(event.getEntityID());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (SolrServerException | IOException e) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }
}
