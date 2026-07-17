package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Attachment;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365HttpResponse;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365MessageAttachmentService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by umakarimov on 9/30/15.
 */
@Service("office365MessageAttachmentService")
public class Office365MessageAttachmentImpl implements Office365MessageAttachmentService, Office365Constants {
    /**
     * @param token
     * @param messageId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#GetAttachmentCollection
     */
    @Override
    public Office365BaseList<Office365Attachment> getAttachmentCollection(Office365AccessTokenDTO token, String messageId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_ATTACHMENT_LIST_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365Attachment>() {
            @Override
            public Office365Attachment map(Object item) {
                return new Office365Attachment((JSONObject) item);
            }
        });
    }

    /**
     * @param token
     * @param messageId
     * @param attachmentId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#GetAttachment
     */
    @Override
    public Office365Attachment getAttachment(Office365AccessTokenDTO token, String messageId, String attachmentId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_ATTACHMENT_ITEM_URL, messageId, attachmentId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Attachment(data);
    }

    /**
     * @param token
     * @param messageId
     * @param attachment
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateItemAttachment
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateFileAttachment
     */
    @Override
    public Office365Attachment createFileAttachment(Office365AccessTokenDTO token, String messageId, Office365Attachment attachment) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_ATTACHMENT_LIST_URL, messageId);
        Office365HttpResponse data = Office365HttpClient.doPost(url, attachment.toJSON(), token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Attachment(data);
    }


    /**
     * @param token
     * @param messageId
     * @param attachmentId
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Deleteattachments
     */
    @Override
    public void deleteAttachment(Office365AccessTokenDTO token, String messageId, String attachmentId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_ATTACHMENT_ITEM_URL, messageId, attachmentId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {

        }
    }
}
