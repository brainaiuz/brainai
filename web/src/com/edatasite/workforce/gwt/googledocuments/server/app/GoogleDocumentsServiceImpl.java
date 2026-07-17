/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/8 4:24:42                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.googledocuments.server.app;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsAttachmentIndexRbac;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.rpc.Attachments;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.AttachmentIndexRbacManager;
import com.edatasite.workforce.gwt.googledocuments.client.rpc.GoogleDocumentsService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 14.11.2008
 * Time: 11:45:48
 */
@Transactional
@Service("googleDocumentsService")
public class GoogleDocumentsServiceImpl implements GoogleDocumentsService, Constants, CommandConstants {

    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private AttachmentManager attachmentManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    protected InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private AttachmentIndexRbacManager attachmentIndexRbacManager;

    public void saveToken(String token) throws Exception {
        EdsUser user = googleDocumentsManager.getUser();
        EdsSinxDocuments googleDocuments = googleDocumentsManager.getGoogleDocuments(user, false);
        if (googleDocuments == null) {
            googleDocuments = new EdsSinxDocuments();
            googleDocuments.setUser(user);
        }

        if (token != null) {
            googleDocuments.setToken(token);
            //String userGoogleId = googleDocumentsManager.getGoogleId(googleDocuments, token);
            //googleDocuments.setGoogleID(userGoogleId);
        }
        googleDocuments.setActive(true);
        googleDocuments.setAttempts(0);
        googleDocuments.setReason(null);
        if (googleDocuments.getObjectID() == null) {
            googleDocumentsManager.create(googleDocuments);
        } else {
            googleDocumentsManager.update(googleDocuments);
        }
    }

    public void saveAttachments(Attachments attachments) {
        if (attachments.isEmploymentAtt()) {
            EdsReference employmentAtt = referenceManager.findReference(EdsAttachment._ATTACHMENT_TYPE, EdsAttachment.EMPLOYMENT_ATTACHMENTS);
            for (FileItem item : attachments.getAttachments()) {
                EdsAttachment edsAttachment = attachmentManager.get(item.getId());
                edsAttachment.setDescription(ADD_DESCRIPTION.equals(item.getDescription()) ? "" : item.getDescription());
                edsAttachment.setAttachmentId(attachments.getObjectID());
                edsAttachment.setCategoryType(employmentAtt);
                edsAttachment.setCreator(attachmentManager.getUser());
                setDefaultPermissions(edsAttachment);
            }
        }
    }

    private void setDefaultPermissions(EdsAttachment attachment) {
        Map<EdsUser, Integer> permissions = new HashMap<>();
        EdsUser user = attachmentManager.getUser();
        permissions.put(user, EdsAttachmentIndexRbac.DELETE);
        attachmentIndexRbacManager.indexAttachment(attachment, permissions);
    }


}
