package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Attachment;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;

/**
 * Created by umakarimov on 9/30/15.
 */
public interface Office365MessageAttachmentService {
    Office365BaseList<Office365Attachment> getAttachmentCollection(Office365AccessTokenDTO token, String messageId);

    Office365Attachment getAttachment(Office365AccessTokenDTO token, String messageId, String attachmentId);

    Office365Attachment createFileAttachment(Office365AccessTokenDTO token, String messageId, Office365Attachment attachment);

    void deleteAttachment(Office365AccessTokenDTO token, String messageId, String attachmentId);
}
