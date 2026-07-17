package com.edatasite.workforce.gwt.core.server.db.emailfetching;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailAttachment;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 10/25/11
 * Time: 11:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailAttachmentManager extends Manager<EdsEmailAttachment> {

    List<Integer> getTrackerIDsWithAttachments(List<Integer> trackerIDs);

    List<EdsEmailAttachment> getTrackerAttachments(Integer... trackerIDs);

    List<EdsEmailAttachment> getEmailAttachments(String emailID);

    void removeAttachments(Integer emailID);
}
