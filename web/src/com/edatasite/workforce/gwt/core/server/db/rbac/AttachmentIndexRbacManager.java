package com.edatasite.workforce.gwt.core.server.db.rbac;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsAttachmentIndexRbac;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 22, 2009
 * Time: 3:15:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface AttachmentIndexRbacManager extends Manager<EdsAttachmentIndexRbac> {
    EdsAttachmentIndexRbac createAttachmentIndex(EdsAttachment attachment, EdsUser user, int permission);

    EdsAttachmentIndexRbac updateAttachmentIndex(EdsAttachment attachment, EdsUser user, int permission);

    EdsAttachmentIndexRbac getAttachmentIndex(EdsAttachment attachment, EdsUser user);

    List<EdsAttachmentIndexRbac> getAttachmentIndexes(EdsAttachment attachment);

    void removeIndex(EdsAttachment attachment);

    void indexAttachment(EdsAttachment attachment, Map<EdsUser, Integer> userPermissions);

}
