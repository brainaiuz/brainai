package com.edatasite.workforce.gwt.core.server.db.impl.rbac;

import com.edatasite.workforce.core.domain.EdsAttachment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.rbac.EdsAttachmentIndexRbac;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.AttachmentIndexRbacManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: Oct 22, 2009
 * Time: 3:22:50 PM
 */
@Repository("attachmentIndexRbacManager")
public class AttachmentIndexRbacManagerImpl extends BaseManager<EdsAttachmentIndexRbac> implements AttachmentIndexRbacManager {
    public AttachmentIndexRbacManagerImpl() {
        super(EdsAttachment.class);
    }

    @Autowired
    private UploadManager uploadManager;

    /**
     * creates new Rbac index for attachment for given user
     *
     * @param attachment
     * @param user
     * @param permission
     * @return
     */
    public EdsAttachmentIndexRbac createAttachmentIndex(EdsAttachment attachment, EdsUser user, int permission) {
        EdsAttachmentIndexRbac attachmentIndex = new EdsAttachmentIndexRbac();
        attachmentIndex.setAttachment(attachment);
        attachmentIndex.setUser(user);
        attachmentIndex.setPermission(permission);
        create(attachmentIndex);
        return attachmentIndex;
    }

    /**
     * sets special permission for user for given attachment
     *
     * @param attachment
     * @param user
     * @param permission
     * @return
     */
    public EdsAttachmentIndexRbac updateAttachmentIndex(EdsAttachment attachment, EdsUser user, int permission) {
        EdsAttachmentIndexRbac attachmentIndex = getAttachmentIndex(attachment, user);
        if (attachmentIndex != null) {
            attachmentIndex.setPermission(permission);
            return attachmentIndex;
        } else {
            return createAttachmentIndex(attachment, user, permission);
        }
    }

    /**
     * gets Attachment index for given user
     *
     * @param attachment
     * @param user
     * @return
     */
    public EdsAttachmentIndexRbac getAttachmentIndex(EdsAttachment attachment, EdsUser user) {
        return (EdsAttachmentIndexRbac) findSingle("SELECT at FROM EdsAttachmentIndexRbac at WHERE at.user = ? AND at.attachment = ?", user, attachment);
    }

    /**
     * Gets list of attachments Rbac indexes
     *
     * @param attachment
     * @return
     */
    public List<EdsAttachmentIndexRbac> getAttachmentIndexes(EdsAttachment attachment) {
        return (List<EdsAttachmentIndexRbac>) find("SELECT at FROM EdsAttachmentIndexRbac at WHERE at.attachment = ?", attachment);
    }

    /**
     * Removes attachment's Rbac index
     *
     * @param attachment
     */
    public void removeIndex(EdsAttachment attachment) {
        List<EdsAttachmentIndexRbac> attachments = getAttachmentIndexes(attachment);
        for (EdsAttachmentIndexRbac at : attachments) {
            delete(at);
        }
    }

    /**
     * Adds attachment to Rbac index according to user permissions
     *
     * @param attachment
     * @param userPermissions
     */
    public void indexAttachment(EdsAttachment attachment, Map<EdsUser, Integer> userPermissions) {
        removeIndex(attachment);
//        InputStream inputStream = uploadManager.getInputStream(attachment);
//        if (inputStream != null) {
        for (Map.Entry<EdsUser, Integer> entry : userPermissions.entrySet()) {
            updateAttachmentIndex(attachment, entry.getKey(), entry.getValue());
        }
        updateAttachmentIndex(attachment, attachment.getCreator(), EdsAttachmentIndexRbac.DELETE);
//        }

    }

}
