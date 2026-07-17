package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365BaseList;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Message;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365Recipient;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
public interface Office365MessageService {
    Office365BaseList<Office365Message> getMessageCollection(Office365AccessTokenDTO token);

    Office365BaseList<Office365Message> getMessageCollection(Office365AccessTokenDTO token, String folderId);

    Office365Message getMessage(Office365AccessTokenDTO token, String messageId);

    Office365Message createNewDraft(Office365AccessTokenDTO token, Office365Message message);

    Office365Message createNewDraft(Office365AccessTokenDTO token, Office365Message message, String folderId);

    void sendDraftMessage(Office365AccessTokenDTO token, String messageId);

    void sendMessageOnTheFly(Office365AccessTokenDTO token, Office365Message message);

    void sendMessageOnTheFly(Office365AccessTokenDTO token, Office365Message message, boolean savetoSentItems);

    void replyToSender(Office365AccessTokenDTO token, String messageId, String comment);

    void replyAll(Office365AccessTokenDTO token, String messageId, String comment);

    Office365Message createReplyAllDraft(Office365AccessTokenDTO token, String messageId);

    void forwardDirectly(
            Office365AccessTokenDTO token, String messageId,
            String comment, ArrayList<Office365Recipient> toRecipients
    );

    Office365Message createForwardDraft(Office365AccessTokenDTO token, String messageId);

    Office365Message updateAMessage(Office365AccessTokenDTO token, Office365Message message);

    void deleteMessage(Office365AccessTokenDTO token, String messageId);

    Office365Message moveMessage(Office365AccessTokenDTO token, String messageId, String destinationId);

    Office365Message copyMessage(Office365AccessTokenDTO token, String messageId, String destinationId);
}
