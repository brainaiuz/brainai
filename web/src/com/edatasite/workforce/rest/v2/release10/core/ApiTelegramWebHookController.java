package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.core.domain.EdsMessengersIntegration;
import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.core.domain.EdsWhatsAppMessage;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.MessageTo;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.RedisSocketObject;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.config.WhatsAppApiConfig;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.media.Media;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.messages.type.MessageType;
import com.edatasite.workforce.gwt.core.server.app.social.whatsapp.dto.webhook.WebHookEvent;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_WHATSAPP_MEDIA;

@RestController
public class ApiTelegramWebHookController extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiTelegramWebHookController.class);
    @Autowired
    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    TelegramChatService telegramChatService;
    @Autowired
    TelegramChatManager telegramChatManager;
    @Autowired
    WhatsAppMessageManager whatsAppMessageManager;
    @Autowired
    AttachmentUtilsManager attachmentUtilsManager;
    @Autowired
    CrmContactManager crmContactManager;
    @Autowired
    CrmAccountManager crmAccountManager;
    @Autowired
    NotificationMsgManager notificationMsgManager;
    @Autowired
    MessengersIntegrationManager messengersIntegrationManager;
    @Autowired
    ProfileManager profileManager;

    @Operation(summary = "Update From Telegram")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Update received"))
    @RequestMapping(path = "/telegram/updates/{token}", method = {RequestMethod.POST}, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object updateTelegram(@PathVariable final String token, @RequestBody String updateJson) {

        log.info("Token: {},  Telegram Update: {}", token, updateJson);
        List<TelegramSettingsItem> telegramSettingsItems = globalAuthJdbcSpringManager.getTelegramSettingsItemByToken(token);

        if (telegramSettingsItems != null) {

            for (TelegramSettingsItem telegramSettingsItem : telegramSettingsItems) {
                String databaseName = null;
                if (telegramSettingsItem.getCompanyId() == null || (databaseName = globalAuthJdbcSpringManager.getCompanyDatabaseName(telegramSettingsItem.getCompanyId())) == null) continue;
                ServerSecurityContext.getInstance().setDatabase(databaseName);
                ServerSecurityContext.getInstance().setCompanyId(telegramSettingsItem.getCompanyId());
                Update update = BotUtils.parseUpdate(updateJson);

                if (update != null && update.message() != null && update.message().chat() != null) {
                    Chat chat = update.message().chat();
                    EdsTelegramChat chatListItem = telegramChatManager.getByChatIdAndBotToken(chat.id(), telegramSettingsItem.getToken());

                    if (chatListItem == null) {
                        TelegramChatListItem telegramChatListItem = new TelegramChatListItem();
                        telegramChatListItem.setChatId(chat.id());

                        if (chat.firstName() != null) {
                            telegramChatListItem.setChatName(chat.firstName());
                        } else {
                            telegramChatListItem.setChatName(chat.title());
                        }

                        telegramChatListItem.setTelegramBotId(telegramSettingsItem.getId());
                        telegramChatListItem.setTelegramBotToken(telegramSettingsItem.getToken());
                        telegramChatService.saveTelegramChat(telegramChatListItem);

                        try {
                            TelegramBot telegramBot = new TelegramBot(token);
                            telegramBot.execute(new SendMessage(chat.id(), " ✅ Hi <b>" + telegramChatListItem.getChatName() + "</b>, your telegram account has successfully been synced with <b>kpi.com</b>").parseMode(ParseMode.HTML));
                        } catch (Exception e) {
                            log.error("Failed To Send Message", e);
                        }
                    }
                }
                ServerSecurityContext.getInstance().removeCompanyId();
                ServerSecurityContext.getInstance().setDatabase("");
            }
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Webhook Verification")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Webhook verified"))
    @RequestMapping(value = "/whatsapp/updates/{companyId}", method = RequestMethod.GET)
    public String verifyWhatsAppWebhook(@PathVariable final String companyId, @RequestParam("hub.mode") String mode, @RequestParam("hub.verify_token") String verifyToken,
                                        @RequestParam("hub.challenge") String challenge) {
        if (mode != null && verifyToken != null) {
            if (mode.equals("subscribe") && verifyToken.equals("token_2001")) {
                System.out.println("Webhook Verified");
                return challenge;
            }
        }
        return null;
    }

    @Operation(summary = "Messages From WhatsApp")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Message received"))
    @RequestMapping(value = "/whatsapp/updates/{companyId}", method = RequestMethod.POST)
    @Transactional
    public void handleWhatsAppWebhook(@PathVariable final String companyId,@RequestBody WebHookEvent request) throws Exception {
        SecurityContext.getInstance().setCompanyId(companyId);
        SecurityContext.getInstance().setDatabase(globalAuthJdbcSpringManager.getCompanyDatabaseName(Integer.valueOf(companyId)));
        SecurityContext.getInstance().setStaticUserID(userManager.getAdmin(Integer.valueOf(companyId)).getObjectID());
        System.out.println(request);




        EdsWhatsAppMessage whatsAppMessage = new EdsWhatsAppMessage();
        var value = request.entry().get(0).changes().get(0).value();
        if (value.messages() != null && value.messages().size() > 0){
            var messageChanges = value.messages().get(0);
            whatsAppMessage.setMessageId(messageChanges.id());
            whatsAppMessage.setMessageType(messageChanges.type());
            whatsAppMessage.setReceiverPhoneNumber(value.metadata().displayPhoneNumber());
            whatsAppMessage.setReceiverPhoneNumberId(value.metadata().phoneNumberId());
            whatsAppMessage.setReceivedDate(new Date());
            whatsAppMessage.setSenderPhoneNumber(messageChanges.from());
            Long timestamp  = new Long(messageChanges.timestamp());
            whatsAppMessage.setMessageDate(new Date(timestamp * 1000L));

            List<EdsCrmContact> contacts = crmContactManager.getAllByPhone(messageChanges.from());
            List<EdsCrmAccount> accounts = crmAccountManager.getAllByPhone(messageChanges.from());
            if (contacts.isEmpty() && accounts.isEmpty()){
                allInOneServiceLocal.createContactFromCalls(messageChanges.from(),null);
                contacts = crmContactManager.getAllByPhone(messageChanges.from());
            }

            if (contacts != null && contacts.size() > 0){
                whatsAppMessage.setCrmContact(contacts.get(0));
            } else if (accounts != null && accounts.size() > 0) {
                whatsAppMessage.setCrmAccount(accounts.get(0));
            }


            if (value.contacts() != null){
                whatsAppMessage.setContactName(value.contacts().get(0).profile().name());
            }

            switch (whatsAppMessage.getMessageType()) {
                case AUDIO -> whatsAppMessage.setMediaId(messageChanges.audio().id());
                case TEXT -> whatsAppMessage.setText(messageChanges.text().body());
                case IMAGE -> {
                    whatsAppMessage.setMediaId(messageChanges.image().id());
                    whatsAppMessage.setText(messageChanges.image().caption());
                }
                case VIDEO -> {
                    whatsAppMessage.setMediaId(messageChanges.video().id());
                    whatsAppMessage.setText(messageChanges.video().caption());
                }
                case STICKER -> whatsAppMessage.setMediaId(messageChanges.sticker().id());
                case DOCUMENT -> {
                    whatsAppMessage.setMediaId(messageChanges.document().id());
                    whatsAppMessage.setText(messageChanges.document().caption());
                }
            }

            whatsAppMessageManager.create(whatsAppMessage);

            if (!whatsAppMessage.getMessageType().equals(MessageType.TEXT) && whatsAppMessage.getMediaId() != null) {
                EdsMessengersIntegration companyCredentials = messengersIntegrationManager.getCompanyCredentials();
                ResponseEntity<Media> mediaResponse = null;
                mediaResponse =  new RestTemplate().exchange(WhatsAppApiConfig.BASE_DOMAIN + "/" + WhatsAppApiConfig.API_VERSION + "/" +
                        whatsAppMessage.getMediaId() + "?phone_number_id="+WhatsAppApiConfig.SOLUTION_PHONE_NUMBER_ID,
                        HttpMethod.GET,new HttpEntity<>(null, createHeaders(companyCredentials.getWhatsappToken())), Media.class);
                ResponseEntity<byte[]> file = new RestTemplate().exchange(mediaResponse.getBody().url(), HttpMethod.GET, new HttpEntity<>(null, createHeaders(companyCredentials.getWhatsappToken())), byte[].class);
                byte[] fileContent = file.getBody();// Replace with the actual method to get the byte array
                String fileName = "file";  // You can set a desired file name here
                String contentType  = mediaResponse.getBody().mimeType().getType();// The MIME type of the file
                MultipartFile multipartFile = new MockMultipartFile(fileName, mediaResponse.getBody().url(), contentType, fileContent);
                FolderResource folderResource = documentsServiceLocal.getFolderResource(F_WHATSAPP_MEDIA,null);
                documentsServiceLocal.saveDocumentFile(multipartFile, folderResource.getObjectId(), F_WHATSAPP_MEDIA, whatsAppMessage.getId(), null);

            }

            try {
                Integer user = userManager.getUser().getObjectID();
                WebSocketServerObject message = new WebSocketServerObject();
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

                message.setUserId(user);
                MessageTo messageTo = new MessageTo();
                messageTo.setMessage(whatsAppMessage.getText());
                messageTo.setCompanyMessage(false);
                messageTo.setContactId(contacts.get(0).getObjectID().toString());
                messageTo.setContactFullName(contacts.get(0).getFullName());
                messageTo.setCreatedDate(hourFormat.format(whatsAppMessage.getMessageDate()));
                messageTo.setDate(dateFormat.format(whatsAppMessage.getMessageDate()));
                message.setData(new Gson().toJson(messageTo));
                message.setEventType(WfmUiEventType.ON_MESSAGE_RECEIVED);
                RedisSocketObject redisSocketObject = new RedisSocketObject();
                redisSocketObject.setCompanyId(Integer.parseInt(SecurityContext.getInstance().getCompanyId()));
                redisSocketObject.setWebSocketServerObject(message);
                RedisClient.publish(redisSocketObject);
                notificationMsgManager.createWhatsappMessageNotification(whatsAppMessage,userManager.getUser());
            } catch (NumberFormatException e) {
                log.error(e.getMessage(), e);
            }

        }






    }


    private HttpHeaders createHeaders(String secretKey) {
        return new HttpHeaders() {{
            String authHeader = "Bearer " + secretKey;
            set("Authorization", authHeader);
            setContentType(MediaType.APPLICATION_JSON);
        }};
    }
}
