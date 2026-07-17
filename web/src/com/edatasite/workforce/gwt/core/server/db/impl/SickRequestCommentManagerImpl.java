package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSickRequestComment;
import com.edatasite.workforce.gwt.core.server.db.SickRequestCommentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: unni
 * Date: Aug 25, 2009
 * Time: 3:01:37 PM
 */
@Repository("sickRequestCommentManager")
public class SickRequestCommentManagerImpl extends AttachmentSupportManager<EdsSickRequestComment> implements SickRequestCommentManager {

    public SickRequestCommentManagerImpl() {
        super(EdsSickRequestComment.class);
    }

    public List<EdsSickRequestComment> getComments(Integer sickRequestId) {
        if (sickRequestId == null) {
            return null;
        }
        return find("select src from EdsSickRequestComment src where src.sickRequest.objectID =? order by src.creationDate desc", sickRequestId);
    }

    @Override
    public void delete(EdsSickRequestComment obj) {
        if (obj != null) {
            updateNative("delete from " + getCompanyId() + ".SickRequestComment  where id = " + obj.getObjectID());
        }
    }
}