package com.edatasite.workforce.gwt.core.server.db.impl.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.EmailAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 6/13/11
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("emailAttachmentManager")
public class EmailAttachmentManagerImpl extends BaseManager<EdsEmailAttachment> implements EmailAttachmentManager {

    public EmailAttachmentManagerImpl() {
        super(EdsEmailAttachment.class);
    }

    @Override
    public List<Integer> getTrackerIDsWithAttachments(List<Integer> trackerIDs) {
        return (List<Integer>) findNative("select ea.emailtracker_id from " + getCompanyId() + ".emailattachments ea where ea.emailtracker_id in (" + ServerUtils.getAsCommoDelimited(trackerIDs, "0", ",") + ")");
    }

    @Override
    public List<EdsEmailAttachment> getTrackerAttachments(Integer... trackerIDs) {
        if (trackerIDs == null || trackerIDs.length == 0) {
            return new ArrayList<>();
        }
        return find("select a from EdsEmailAttachment a where a.emailTracker.objectID in (" + ServerUtils.getAsCommoDelimited(Arrays.asList(trackerIDs), "0") + " )");
    }

    @Override
    public List<EdsEmailAttachment> getEmailAttachments(String emailID) {
        return slaveEntityManager.createQuery("select a from EdsEmailAttachment a where a.emailId = :emailID ").setParameter("emailID", emailID).getResultList();
    }

    @Override
    public void removeAttachments(Integer emailID) {
        masterEntityManager.createQuery("update EdsEmailAttachment set emailId=null,emailTracker=null where emailId=:emailID").setParameter("emailID", emailID).executeUpdate();
    }
}
