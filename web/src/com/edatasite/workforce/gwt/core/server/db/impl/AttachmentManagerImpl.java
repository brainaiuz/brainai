/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/4/29 8:56:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AttachmentManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 01-Jun-2009
 * Time: 18:11:42
 * To change this template use File | Settings | File Templates.
 */
@Repository("attachmentManager")
public class AttachmentManagerImpl extends UploadManagerImpl<EdsAttachment> implements AttachmentManager, Constants {

    public AttachmentManagerImpl() {
        super(EdsAttachment.class);
    }

    public List<EdsAttachment> getAttachmentsByAttachmentId(Integer attachmentId){
        String sql = "select a from EdsAttachment a where a.deleted<>true and (a.attachmentId=? or a.id=?) and a.size > 0";
        return find(sql, attachmentId, attachmentId);
    }

    public List<EdsAttachment> getEmploymentAttachmentsById(Integer employmentId) {
        EdsReference empAtt = referenceManager.findReference(EdsAttachment._ATTACHMENT_TYPE, EdsAttachment.EMPLOYMENT_ATTACHMENTS);
        if (empAtt != null) {
            return find("from EdsAttachment a where a.attachmentId=? and a.deleted<>true and a.size > 0 and a.categoryType=?", employmentId, empAtt);
        } else {
            return new ArrayList<>();
        }
    }

    public List<EdsAttachment> getCaseAttachments(Integer caseTrackerID) {
        EdsReference emailAtt = referenceManager.findReference(EdsAttachment._ATTACHMENT_TYPE, EdsAttachment.EMAILS_ATTACHMENT);
        String query = "select a from EdsAttachment a where a.attachmentId = ? and " + ServerUtils.checkForDeleted("a.deleted") + " and a.size > 0 and a.categoryType = ?";
        return find(query, caseTrackerID, emailAtt);
    }

    public List<EdsAttachment> getAttachments(Integer id, EdsReference categoryType) {
        return find("from EdsAttachment a where a.attachmentId = ? and a.deleted <> true and a.size > 0 " +
                "and a.categoryType = ? order by a.objectID desc", id, categoryType);
    }

    public List<EdsAttachment> getCompanyCaseAttachments() {
        EdsReference categoryType = referenceManager.findReference(EdsAttachment._ATTACHMENT_TYPE, EdsAttachment.EMAILS_ATTACHMENT);
        return find("from EdsAttachment a where a.attachmentId is not null and a.deleted <> true and a.size > 0 " +
                " and a.categoryType = ? order by a.objectID desc", categoryType);
    }
}
