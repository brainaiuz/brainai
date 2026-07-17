package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
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
 * Time: 1:47:25 PM
 */
@Transactional
public class FolderCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsFolder> TYPE = new WfmType<>(EventTypes.folderCustomEventListener);

    public static String EVENT_DELETE = "FOLDER_DELETE";
    public static String EVENT_ADD = "FOLDER_ADD";

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private UserManager userManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_DELETE.equals(event.getEventType())) {
            onDelete(event);
        } else if (EVENT_ADD.equals(event.getEventType())) {
            onAdd(event);
        }
    }

    private void onDelete(EdsBusinessEvent event) {
        try {
            solrManager.removeFolder(event.getEntityID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());

        }
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsFolder folder = folderManager.get(event.getEntityID());
        EdsUser user = userManager.get(event.getSourceID());
        try {
            solrManager.addFolderToIndex(folder, user.getCompany());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

}
