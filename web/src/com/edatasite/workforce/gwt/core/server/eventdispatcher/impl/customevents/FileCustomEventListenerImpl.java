package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.solr.component.FolderSolrComponent;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * User: Abdulaziz
 * Date: Jul 30, 2010
 * Time: 3:23:49 PM
 */
@Transactional
public class FileCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsFileHeader> TYPE = new WfmType<>(EventTypes.fileCustomEventListener);

    public static String EVENT_ADD = "FILE_ADD";
    public static String EVENT_DELETE = "FILE_DELETE";
    public static String EVENT_EMPLOYEE_PROFILE = "EMPLOYEE_PROFILE";
    public static String EVENT_EMPLOYEE_DELETED = "EVENT_EMPLOYEE_DELETED";

    @Autowired
    private SolrManager solrManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private FolderSolrComponent folderSolrComponent;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_ADD.equals(event.getEventType())) {
            onAdd(event);
        } else if (EVENT_DELETE.equals(event.getEventType())) {
            onDelete(event);
        } else if (EVENT_EMPLOYEE_PROFILE.equals(event.getEventType())) {
            customIndex(event);
        } else if (EVENT_EMPLOYEE_DELETED.equals(event.getEventType())) {
            indexDocuments(event);
        }

    }

    private void indexDocuments(EdsBusinessEvent event) {
        if (event.getSourceID() != null) {
            EdsUser user = userManager.get(event.getEntityID());
            List<EdsFileHeader> list = fileHeaderManager.getEmployeeDocuments(event.getSourceID(), Constants.F_EMPLOYEE_PROFILE);
            if (list != null && !list.isEmpty()) {
                for (EdsFileHeader fileHeader : list) {
                    fileHeader.setDeleted(true);
                    fileHeader.getAuditInfo().setModificationDate(new Date());
                    fileHeader.getAuditInfo().setModifiedBy(user);
                    folderRbacManager.removeIndexFileEntries(fileHeader.getObjectID());
                }
            }
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    private void onAdd(EdsBusinessEvent event) {
        EdsFileHeader file = fileHeaderManager.get(event.getEntityID());
        try {
            folderSolrComponent.index(file);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onDelete(EdsBusinessEvent event) {
        try {
            solrManager.removeFile(event.getEntityID());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (SolrServerException | IOException e) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    private void customIndex(EdsBusinessEvent event) {
        EdsFileHeader file = fileHeaderManager.get(event.getEntityID());
        folderRbacManager.addFileRbacEntries(file);
        try {
            folderSolrComponent.index(file);
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (InterruptedException e) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }
}
