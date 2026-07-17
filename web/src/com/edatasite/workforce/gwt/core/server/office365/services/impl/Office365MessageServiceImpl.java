package com.edatasite.workforce.gwt.core.server.office365.services.impl;

import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.resources.*;
import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseItem;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365MessageService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365HttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
@Service("office365MessageService")
public class Office365MessageServiceImpl implements Office365MessageService, Office365Constants {
    /**
     * @param token
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#GetMessageCollection
     */
    @Override
    public Office365BaseList<Office365Message> getMessageCollection(Office365AccessTokenDTO token) {
        return this.getMessageCollection(token, null);
    }

    /**
     * @param token
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#GetMessageCollection
     */
    @Override
    public Office365BaseList<Office365Message> getMessageCollection(Office365AccessTokenDTO token, String folderId) {
        String url = folderId == null ?
                OUTLOOK_MESSAGE_LIST_URL :
                String.format(OUTLOOK_MESSAGE_FOLDER_ITEM_MESSAGE_LIST_URL, folderId);

        Office365HttpResponse data = Office365HttpClient.doGet(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365BaseList<>(data, new Office365BaseItem.FieldMapper<Office365Message>() {
            @Override
            public Office365Message map(Object item) {
                return new Office365Message((JSONObject) item);
            }
        });
    }

    /**
     * @param token
     * @param messageId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#GetMessage
     */
    @Override
    public Office365Message getMessage(Office365AccessTokenDTO token, String messageId) {
        String messageUrl = String.format(OUTLOOK_MESSAGE_ITEM_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doGet(messageUrl, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }

    /**
     * @param token
     * @param message
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateNewDraft
     */
    @Override
    public Office365Message createNewDraft(Office365AccessTokenDTO token, Office365Message message) {
        return this.createNewDraft(token, message, null);
    }

    /**
     * @param token
     * @param message
     * @param folderId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateNewDraft
     */
    @Override
    public Office365Message createNewDraft(Office365AccessTokenDTO token, Office365Message message, String folderId) {
        String url = OUTLOOK_MESSAGE_LIST_URL;

        if (folderId != null) {
            url = String.format(OUTLOOK_MESSAGE_FOLDER_ITEM_MESSAGE_LIST_URL, folderId);
        }

        Office365HttpResponse data = Office365HttpClient.doPost(url, message.toJSON(), token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }

    /**
     * @param token
     * @param messageId
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#SendDraftMessages
     */
    @Override
    public void sendDraftMessage(Office365AccessTokenDTO token, String messageId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_SEND_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, null, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @param message
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#SendMessageOnTheFly
     */
    @Override
    public void sendMessageOnTheFly(Office365AccessTokenDTO token, Office365Message message) {
        this.sendMessageOnTheFly(token, message, true);
    }

    /**
     * @param token
     * @param message
     * @param savetoSentItems
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#SendMessageOnTheFly
     */
    @Override
    public void sendMessageOnTheFly(Office365AccessTokenDTO token, final Office365Message message, final boolean savetoSentItems) {
        Office365HttpResponse data = Office365HttpClient.doPost(OUTLOOK_MESSAGE_SEND_ON_THE_FLY_URL, new JSONObject() {{
            this.put("Message", message.toJSON());
            this.put("SavetoSentItems", savetoSentItems);
        }}, token);

        if (data.hasError()) {

        }
    }

    /***
     * @param token
     * @param messageId
     * @param comment
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#ReplyToSender
     */
    @Override
    public void replyToSender(Office365AccessTokenDTO token, String messageId, final String comment) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_REPLY_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, new JSONObject() {{
            this.put("Comment", comment);
        }}, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @param messageId
     * @param comment
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#ReplyAll
     */
    @Override
    public void replyAll(Office365AccessTokenDTO token, String messageId, final String comment) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_REPLY_ALL_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, new JSONObject() {{
            this.put("Comment", comment);
        }}, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @param messageId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateReplyAllDraft
     */
    @Override
    public Office365Message createReplyAllDraft(Office365AccessTokenDTO token, String messageId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_CREATE_REPLY_ALL_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }

    /**
     * @param token
     * @param messageId
     * @param comment
     * @param toRecipients
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#ForwardDirectly
     */
    @Override
    public void forwardDirectly(
            Office365AccessTokenDTO token, String messageId,
            final String comment, final ArrayList<Office365Recipient> toRecipients
    ) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_FORWARD_URL, messageId);
        Office365HttpResponse data = Office365HttpClient.doPost(url, new JSONObject() {{
            this.put("Comment", comment);

            this.put("ToRecipients", new JSONArray() {{
                for (Office365Recipient toRecipient : toRecipients) {
                    this.add(toRecipient.toJSON());
                }
            }});

        }}, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @param messageId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CreateForwardDraft
     */
    @Override
    public Office365Message createForwardDraft(Office365AccessTokenDTO token, String messageId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_FORWARD_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, null, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }

    /**
     * @param token
     * @param message
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#UpdateAMessage
     */
    @Override
    public Office365Message updateAMessage(Office365AccessTokenDTO token, Office365Message message) {
        Office365HttpResponse data = Office365HttpClient.doPatch(OUTLOOK_MESSAGE_LIST_URL, message.toJSON(), token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }

    /**
     * @param token
     * @param messageId
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#Deletemessages
     */
    @Override
    public void deleteMessage(Office365AccessTokenDTO token, String messageId) {
        String url = String.format(OUTLOOK_MESSAGE_ITEM_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doDelete(url, null, token);

        if (data.hasError()) {

        }
    }

    /**
     * @param token
     * @param messageId
     * @param destinationId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#MoveMessage
     */
    @Override
    public Office365Message moveMessage(Office365AccessTokenDTO token, String messageId, final String destinationId) {
        return this.moveOrCopyMessage(token, messageId, destinationId, true);
    }

    /**
     * @param token
     * @param messageId
     * @param destinationId
     * @return
     * @see https://msdn.microsoft.com/office/office365/APi/mail-rest-operations#CopyMessage
     */
    @Override
    public Office365Message copyMessage(Office365AccessTokenDTO token, String messageId, final String destinationId) {
        return this.moveOrCopyMessage(token, messageId, destinationId, false);
    }

    private Office365Message moveOrCopyMessage(Office365AccessTokenDTO token, String messageId, final String destinationId, boolean move) {
        String url = String.format(move ? OUTLOOK_MESSAGE_ITEM_MOVE_URL : OUTLOOK_MESSAGE_ITEM_COPY_URL, messageId);

        Office365HttpResponse data = Office365HttpClient.doPost(url, new JSONObject() {{
            this.put("DestinationId", destinationId);
        }}, token);

        if (data.hasError()) {
            return null;
        }

        return new Office365Message(data);
    }
}
