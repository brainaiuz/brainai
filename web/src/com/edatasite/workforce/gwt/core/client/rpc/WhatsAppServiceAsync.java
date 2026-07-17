package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.MessageTo;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface WhatsAppServiceAsync {

    void sendMessage (MessageTo message, AsyncCallback<Boolean> async);

    void getAllWhatsappMessages(Integer currentContactId,String contactType,AsyncCallback<LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>>> async);

    void getContactDataMap(Integer contactId,String type,AsyncCallback<HashMap<Integer, ContactItem>> async);


    void getWhatsappCredentials(AsyncCallback<WhatsappCredentialsItem> async);

    void saveWhatsappCredentials(WhatsappCredentialsItem item,AsyncCallback<Void> async);
}
