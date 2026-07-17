package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsMessengersIntegration;
import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.ContactItem;
import com.edatasite.workforce.gwt.contact.client.rpc.MessageTo;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsAppService;
import com.edatasite.workforce.gwt.core.client.rpc.WhatsappCredentialsItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.config.WhatsAppApiConfig;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.AudioMessage;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.DocumentMessage;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.ImageMessage;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.Message;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.TextMessage;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.VideoMessage;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.response.MessageResponse;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.MessageType;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.MessengersIntegrationManager;
import com.edatasite.workforce.gwt.core.server.db.WhatsAppMessageManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Service("whatsappService")
public class WhatsAppServiceImpl implements WhatsAppService {

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private WhatsAppMessageManager whatsAppMessageManager;
    @Autowired
    private MessengersIntegrationManager messengersIntegrationManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private AttachmentUtilsManager attachmentUtilsManager;
    /**
     * Send message call.
     *
     * @param phoneNumberId the phone number id
     * @param message       the message
     * @return the call
     */

    @Override
    @Transactional
    public Boolean sendMessage(MessageTo messageTo) {
        String apiUrl = constructApiUrl(messageTo);

        HttpHeaders headers = createHeaders();
        Message message = createMessage(messageTo);

        HttpEntity<Message> requestEntity = new HttpEntity<>(message, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<MessageResponse> responseEntity = restTemplate.postForEntity(apiUrl, requestEntity, MessageResponse.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            handleSuccessfulResponse(messageTo, responseEntity);
            return true;
        } else {
            return false;
        }
    }


    @Override
    public LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> getAllWhatsappMessages(Integer currentContactId, String contactType) {
        ArrayList<EdsWhatsAppMessage> allMessagesList = whatsAppMessageManager.getAllMessagesList();
        LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> messagesByContactIdAndDate = new LinkedHashMap<>();
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
        // Group messages by contactId and date
        for (EdsWhatsAppMessage message : allMessagesList) {
            MessageTo messageTo = new MessageTo();
            messageTo.setObjectId(message.getMessageId());
            messageTo.setContactFullName(message.getContactName());
            Integer contactId = null;
            String contactFullName = null;
            String contactPhoneNumber = null;
            if (message.getCrmContact() != null) {
                EdsCrmContact crmContact = message.getCrmContact();
                contactId = crmContact.getObjectID();
                contactFullName = crmContact.getFullName();
                contactPhoneNumber = crmContact.getPrimaryPhone();
            } else if (message.getCrmAccount() != null) {
                EdsCrmAccount crmAccount = message.getCrmAccount();
                contactId = crmAccount.getObjectID();
                contactFullName = crmAccount.getName();
                contactPhoneNumber = crmAccount.getPhone();
            } else if (message.getEmployee() != null) {
                EdsEmployee employee = message.getEmployee();
                contactId = employee.getObjectID();
                contactFullName = employee.getFullName();
                contactPhoneNumber = employee.getPrimaryPhone();
            }

            messageTo.setContactId(contactId != null ? contactId.toString() : null);
            messageTo.setContactFullName(contactFullName);
            messageTo.setCompanyMessage(!companyCredentials.getPhoneNumberId().equals(message.getReceiverPhoneNumberId()));
            messageTo.setCreatedDate(message.getMessageDate() != null ? hourFormat.format(message.getMessageDate()) : "");
            messageTo.setMessage(message.getText());
            messageTo.setPhoneNumber(contactPhoneNumber);


            // Get or create the map associated with the contactId
            LinkedHashMap<String, ArrayList<MessageTo>> messagesByDate = messagesByContactIdAndDate.computeIfAbsent(contactId, k -> new LinkedHashMap<>());

            // Format the message date
            String formattedDate = dateFormat.format(message.getMessageDate());

            // Get or create the list associated with the formatted message date
            ArrayList<MessageTo> messages = messagesByDate.getOrDefault(formattedDate, new ArrayList<>());

            // Add the message to the list
            messages.add(messageTo);
            // Sort the messages by date

            // Put the sorted list back into the map
            messagesByDate.put(formattedDate, messages);
        }
        if (!messagesByContactIdAndDate.containsKey(currentContactId)) {
            putMessageToNewContact(messagesByContactIdAndDate, currentContactId, contactType);
        }

        return messagesByContactIdAndDate;
    }


    @Override
    public HashMap<Integer, ContactItem> getContactDataMap(Integer contactId, String type) {
        HashMap<Integer, ContactItem> contactDataMap = new HashMap<>();
        HashMap<String, ArrayList<Integer>> messageOwnersDataMap = whatsAppMessageManager.getMessageOwnersDataMap();

        messageOwnersDataMap.forEach((k, v) -> {
            for (Integer id : v) {
                ContactItem item = createContactItem(id, k);
                contactDataMap.putIfAbsent(id, item);
            }
        });

        if (!contactDataMap.containsKey(contactId)) {
            ContactItem item = createContactItem(contactId, type);
            contactDataMap.put(contactId, item);
        }

        return contactDataMap;

    }

    @Override
    public WhatsappCredentialsItem getWhatsappCredentials() {
        return messengersIntegrationManager.getCompanyCredentials().toRpc();
    }

    @Override
    @Transactional
    public void saveWhatsappCredentials(WhatsappCredentialsItem item) {
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        EdsMessengersIntegration messengersIntegration = companyCredentials != null ? companyCredentials : new EdsMessengersIntegration();
        messengersIntegration.setWhatsappNumber(item.getPhoneNumber());
        messengersIntegrationManager.createOrUpdate(messengersIntegration);
    }


    private void putMessageToNewContact(LinkedHashMap<Integer, LinkedHashMap<String, ArrayList<MessageTo>>> map, Integer currentContactId, String contactType) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

        String fullName = null;
        String phoneNumber = null;
        switch (contactType) {
            case "lead":
            case "contact":
                EdsCrmContact contact = crmContactManager.get(currentContactId);
                fullName = contact.getFullName();
                phoneNumber = contact.getPrimaryPhone();
                break;
            case "account":
                EdsCrmAccount account = crmAccountManager.get(currentContactId);
                fullName = account.getName();
                phoneNumber = account.getPhone();
                break;
            case "employee":
                EdsEmployee employee = employeeManager.get(currentContactId);
                phoneNumber = employee.getPrimaryPhone();
                fullName = employee.getFullName();
                break;
        }

        if (fullName != null && phoneNumber != null) {
            ArrayList<MessageTo> messageTos = new ArrayList<>();
            MessageTo message = new MessageTo();
            message.setPhoneNumber(phoneNumber);
            message.setContactFullName(fullName);
            message.setContactType(contactType);
            message.setContactId(String.valueOf(currentContactId));
            messageTos.add(message);

            LinkedHashMap<String, ArrayList<MessageTo>> messages = new LinkedHashMap<>();
            messages.put(dateFormat.format(new Date()), messageTos);
            map.put(currentContactId, messages);
        }
    }


    private String constructApiUrl(MessageTo messageTo) {
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        String url = WhatsAppApiConfig.BASE_DOMAIN + "/" + WhatsAppApiConfig.API_VERSION + "/{Phone-Number-ID}/messages";
        return url.replace("{Phone-Number-ID}", companyCredentials.getPhoneNumberId());
    }

    private HttpHeaders createHeaders() {
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + companyCredentials.getWhatsappToken());
        return headers;
    }

    private Message createMessage(MessageTo messageTo) {
        String phoneNumber = getPhoneNumber(messageTo);
        if (!"text".equals(messageTo.getMessageType())) {
            return createFileMessage(messageTo, phoneNumber);
        } else {
            return createTextMessage(messageTo, phoneNumber);
        }
    }

    private String getPhoneNumber(MessageTo messageTo) {
        String phoneNumber = "";
        int contactId = Integer.valueOf(messageTo.getContactId());

        switch (messageTo.getContactType()) {
            case "contact", "lead":
                EdsCrmContact contact = crmContactManager.get(contactId);
                phoneNumber = contact != null ? contact.getPrimaryPhone() : "";
                break;
            case "account":
                EdsCrmAccount account = crmAccountManager.get(contactId);
                phoneNumber = account != null ? account.getPhone() : "";
                break;
            case "employee":
                EdsEmployee employee = employeeManager.get(contactId);
                phoneNumber = employee != null ? employee.getPrimaryPhone() : "";
                break;
        }

//        if (phoneNumber != null && (phoneNumber.startsWith("998") || phoneNumber.startsWith("+998"))) {
//            phoneNumber = phoneNumber.replace("998", "9988");
//        }

        return phoneNumber;
    }

    private Message createFileMessage(MessageTo messageTo, String phoneNumber) {
        FileResource fileResource = messageTo.getFiles() != null ? messageTo.getFiles().get(0) : null;
        if (fileResource == null) return null;

        String contentType = getFileContentType(fileResource.getFileName());
        if (contentType == null) return null;

        switch (contentType) {
            case "image":
                return createImageMessage(fileResource, phoneNumber);
            case "audio":
                return createAudioMessage(fileResource, phoneNumber);
            case "document":
                return createDocumentMessage(fileResource, messageTo.getNote(), phoneNumber);
            case "video":
                return createVideoMessage(fileResource, phoneNumber);
            default:
                return null;
        }
    }

    private String getFileContentType(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        switch (extension) {
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
            case "svg":
                return "image";
            case "mp3":
            case "wav":
            case "ogg":
            case "aac":
            case "flac":
                return "audio";
            case "pdf":
            case "doc":
            case "docx":
            case "txt":
            case "rtf":
            case "xls":
            case "xlsx":
            case "ppt":
            case "pptx":
            case "csv":
            case "odt":
            case "ods":
            case "odp":
                return "document";
            case "mp4":
            case "avi":
            case "mkv":
            case "mov":
            case "wmv":
            case "webm": // Added support for webm
                return "video";
            default:
                return null;
        }
    }

    private Message createImageMessage(FileResource fileResource, String phoneNumber) {
        ImageMessage imageMessage = new ImageMessage().setLink(fileResource.getAmazonLink());
        return Message.MessageBuilder.builder().setTo(phoneNumber).buildImageMessage(imageMessage);
    }

    private Message createAudioMessage(FileResource fileResource, String phoneNumber) {
        AudioMessage audioMessage = new AudioMessage().setLink(fileResource.getAmazonLink());
        return Message.MessageBuilder.builder().setTo(phoneNumber).buildAudioMessage(audioMessage);
    }

    private Message createVideoMessage(FileResource fileResource, String phoneNumber) {
        VideoMessage videoMessage = new VideoMessage().setLink(fileResource.getAmazonLink());
        return Message.MessageBuilder.builder().setTo(phoneNumber).buildVideoMessage(videoMessage);
    }

    private Message createDocumentMessage(FileResource fileResource, String caption, String phoneNumber) {
        DocumentMessage documentMessage = new DocumentMessage()
                .setLink(fileResource.getAmazonLink())
                .setCaption(caption)
                .setFileName(fileResource.getFileName());
        return Message.MessageBuilder.builder().setTo(phoneNumber).buildDocumentMessage(documentMessage);
    }

    private Message createTextMessage(MessageTo messageTo, String phoneNumber) {
        TextMessage textMessage = new TextMessage().setBody(messageTo.getMessage()).setPreviewUrl(false);
        return Message.MessageBuilder.builder().setTo(phoneNumber).buildTextMessage(textMessage);
    }

    private void handleSuccessfulResponse(MessageTo messageTo, ResponseEntity<MessageResponse> responseEntity) {
        EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
        EdsWhatsAppMessage whatsAppMessage = new EdsWhatsAppMessage();
        whatsAppMessage.setMessageDate(new Date());
        whatsAppMessage.setText(messageTo.getMessage());
//        if (messageTo.getTemplateId() != null) {
//            EdsWhatsAppTemplate template = whatsAppTemplateManager.get(Integer.valueOf(messageTo.getTemplateId()));
//            whatsAppMessage.setText(template.getContent());
//        }
        whatsAppMessage.setCreator(whatsAppMessageManager.getUser());
        whatsAppMessage.setMessageId(responseEntity.getBody().messages().get(0).id());
        whatsAppMessage.setReceiverPhoneNumberId(responseEntity.getBody().contacts().get(0).input());
        whatsAppMessage.setReceiverPhoneNumber(responseEntity.getBody().contacts().get(0).waId());
        whatsAppMessage.setSenderPhoneNumber(companyCredentials.getWhatsappNumber());
        whatsAppMessage.setSenderPhoneNumberId(companyCredentials.getPhoneNumberId());
        whatsAppMessage.setMessageType(messageTo.getMessageType().equals("TEXT") ? MessageType.TEXT : MessageType.DOCUMENT);
        if ("contact".equals(messageTo.getContactType()) || "lead".equals(messageTo.getContactType())) {
            whatsAppMessage.setCrmContact(crmContactManager.get(Integer.valueOf(messageTo.getContactId())));
        } else if ("account".equals(messageTo.getContactType())) {
            whatsAppMessage.setCrmAccount(crmAccountManager.get(Integer.valueOf(messageTo.getContactId())));
        } else if ("employee".equals(messageTo.getContactType())) {
            whatsAppMessage.setEmployee(employeeManager.get(Integer.valueOf(messageTo.getContactId())));
        }
        whatsAppMessageManager.create(whatsAppMessage);

        if (!"TEXT".equals(messageTo.getMessageType())) {
            ArrayList<FileItem> fileItems = getFileItems(messageTo);
            attachmentUtilsManager.saveAttachments(Constants.F_WHATSAPP_MEDIA, whatsAppMessage.getObjectID(), whatsAppMessage.getObjectID(), fileItems.toArray(new FileItem[0]));
        }
    }

    private ArrayList<FileItem> getFileItems(MessageTo messageTo) {
        ArrayList<FileItem> fileItems = new ArrayList<>();
        FileResource fileResource = messageTo.getFiles() != null ? messageTo.getFiles().get(0) : null;
        if (fileResource != null) {
            FileItem fileItem = new FileItem();
            fileItem.setId(fileResource.getObjectId());
            fileItem.setFileName(fileResource.getFileName());
            fileItem.setUploadType(fileResource.getUploadType());
            fileItems.add(fileItem);
        }
        return fileItems;
    }

    private ContactItem createContactItem(Integer id, String type) {
        ContactItem item = new ContactItem();
        switch (type) {
            case "contact":
                EdsCrmContact contact = crmContactManager.get(id);
                item.setId(id);
                item.setFullName(contact.getFirstName() + " " + (contact.getLastName() != null ? contact.getLastName() : ""));
                item.setPhone(contact.getPrimaryPhone());
                break;
            case "account":
                EdsCrmAccount account = crmAccountManager.get(id);
                item.setId(id);
                item.setFullName(account.getName());
                item.setPhone(account.getPhone());
                break;
            case "employee":
                EdsEmployee employee = employeeManager.get(id);
                item.setId(id);
                item.setFullName(employee.getFullName());
                item.setPhone(employee.getPrimaryPhone());
                break;
        }
        return item;
    }



}
