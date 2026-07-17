package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.MessageTo;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public interface WhatsAppService extends RemoteService {

    Boolean sendMessage(MessageTo message);

    LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> getAllWhatsappMessages(Integer currentContactId,String contactType);

    HashMap<Integer, ContactItem> getContactDataMap(Integer contactId,String type);


    WhatsappCredentialsItem getWhatsappCredentials();

    void saveWhatsappCredentials(WhatsappCredentialsItem item);

//    UploadResponse uploadMedia(String phoneNumberId);
//
//    Media retrieveMediaUrl(String mediaId);
//
//    ResponseBody downloadMediaFile(String url);
//
//    Response deleteMedia(String mediaId);
//
//    Response markMessageAsRead(String phoneNumberId);
//
//    Response twoStepVerification(String phoneNumberId);

    class App {
        public static WhatsAppServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/whatsapp");
            return (WhatsAppServiceAsync) target;
        }
    }

}
