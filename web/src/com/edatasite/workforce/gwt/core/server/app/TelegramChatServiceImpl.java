package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.enums.TelegramChatTypeEnum;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.CaseManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.TelegramChatManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.WhatsAppMessageManager;
import com.edatasite.workforce.gwt.core.server.rpc.TelegramMessageRequestObject;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetWebhook;
import com.pengrad.telegrambot.response.BaseResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 18.05.2017 4:04
 */
@Service("telegramChatService")
public class TelegramChatServiceImpl implements TelegramChatService, Constants {

    Logger logger = LoggerFactory.getLogger(TelegramChatServiceImpl.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private TelegramChatManager telegramChatManager;
    @Autowired
    private TelegramBlackListService telegramBlackListService;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private WhatsAppMessageManager whatsAppMessageManager;

    @Override
    @Transactional(readOnly = true)
    public ListResult<TelegramChatListItem> getChatList(ListingFilterParameter fp) {
        ArrayList<TelegramChatListItem> result = Lists.newArrayList();

        Integer count = telegramChatManager.getListCount(fp);
        if (count != null && count > 0) {
            List<EdsTelegramChat> edsChats = telegramChatManager.getList(fp);
            result.addAll(edsChats.stream().map(EdsTelegramChat::getRPC).toList());
        }
        return new ListResult<>(result, count);
    }

    @Override
    @Transactional(readOnly = true)
    public SelectItem[] getChatListAsSelectItem(ListingFilterParameter fp) {
        List<EdsTelegramChat> edsChats = telegramChatManager.getList(fp);
        return edsChats.stream().map(x -> new SelectItem(x.getObjectID(), x.getChatName())).toArray(SelectItem[]::new);
    }

    @Override
    @Transactional(readOnly = true)
    public TelegramChatListItem getChat(Integer objectId) {
        TelegramChatListItem result = null;
        if (objectId != null) {
            EdsTelegramChat edsChat = telegramChatManager.get(objectId);
            if (edsChat != null) {
                result = edsChat.getRPC();
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void createChat(Long chatId, String chatName) {
        if (chatId != null) {
            if (telegramBlackListService.validate(chatId)) {
                telegramBlackListService.deleteByChatId(chatId);
            }
            EdsModule telegramModule = moduleManager.getModuleByCode(PermissionConstants.TELEGRAM_CHATS);// TODO: 08.08.2017 send message if the module not enabled
            try {
                String botToken = EdsContextParams.getTelegramBotToken();
                if (!StringUtils.isEmpty(botToken)) {
                    EdsTelegramChat edsTelegramChat = telegramChatManager.getByChatId(chatId);
                    if (edsTelegramChat == null) {
                        edsTelegramChat = new EdsTelegramChat();
                        edsTelegramChat.setChatId(chatId);
                    }
                    edsTelegramChat.setChatName(chatName);
                    edsTelegramChat.setActive(false);
                    edsTelegramChat.setChatType(TelegramChatTypeEnum.GROUP_TYPE);
                    edsTelegramChat.setCreator(userManager.getUser());
                    telegramChatManager.createOrUpdate(edsTelegramChat);

                    sendMessage(chatId, chatName, "Chat successfully registered");

                    logger.info("--------------" + chatId + " Telegram chat successfully registered--------------");
                }
            } catch (Exception e) {
                logger.info("--------------" + chatId + " Error creating telegram chat--------------");
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void saveTelegramChat(TelegramChatListItem telegramChatListItem) {
        if (telegramChatListItem.getChatId() != null) {
            EdsTelegramChat edsTelegramChat = new EdsTelegramChat();
            edsTelegramChat.setChatId(telegramChatListItem.getChatId());
            edsTelegramChat.setChatName(telegramChatListItem.getChatName());
            edsTelegramChat.setTelegramBotId(telegramChatListItem.getTelegramBotId());
            edsTelegramChat.setTelegramBotToken(telegramChatListItem.getTelegramBotToken());
            telegramChatManager.createOrUpdate(edsTelegramChat);
        }
    }

    @Override
    @Transactional
    public Integer updateChat(TelegramChatListItem item) {
        if (item != null && item.getObjectId() != null) {
            EdsTelegramChat edsChat = telegramChatManager.get(item.getObjectId());
            if (edsChat != null && !edsChat.isNew()) {
                edsChat.setChatName(item.getChatName());
                edsChat.setActive(item.isActive());
                edsChat.setSendCaseCreate(item.isSendCaseCreate());
                telegramChatManager.update(edsChat);
                return edsChat.getObjectID();
            }
        }
        return null;
    }

    @Override
    @Transactional
    public Boolean deleteChat(Integer objectId) {
        if (objectId != null) {
            EdsTelegramChat edsChat = telegramChatManager.get(objectId);
            if (edsChat != null) {
                edsChat.setActive(false);
                edsChat.setDeleted(true);
                telegramChatManager.update(edsChat);
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public void sendMessage(Long chatId, String chatName, String text) {
        if (chatId != null && !StringUtils.isEmpty(text)) {
            try {
                String botToken = EdsContextParams.getTelegramBotToken();
                if (!StringUtils.isEmpty(botToken)) {
                    String url = TELEGRAM_BASE_URL + botToken + "/sendMessage";
                    TelegramMessageRequestObject request = new TelegramMessageRequestObject();
                    request.setChat_id(chatId);
                    request.setText(text);

                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);

                    RestTemplate restTemplate = new RestTemplate();
                    HttpEntity<TelegramMessageRequestObject> httpRequest = new HttpEntity<>(request, httpHeaders);
                    restTemplate.postForObject(url, httpRequest, Object.class);

                    try {
                        Thread.sleep(1000);                 // ONE DAY WE WILL REMOVE IT
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info("--------------" + chatId + " " + chatName + " Telegram message sent successfully--------------");
                }
            } catch (RestClientException e) {
                telegramBlackListService.saveChat(chatId, chatName);

                logger.info("--------------" + chatId + " Error sending text to telegram chat--------------");
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional
    public void sendCaseCreateMessage(Integer entityId, String messageType, Integer userId) {
        if (entityId != null) {
            EdsCase edsCase = caseManager.get(entityId);
            EdsUser edsUser = userManager.get(userId);
            if (edsCase != null) {
                StringBuilder messageText = new StringBuilder();
                messageText.append("CRM Case Created Notification \n");
                messageText.append("Case Number: ").append(edsCase.getCaseNumberString()).append("\n");
                if (edsCase.getCrmAccount() != null && !StringUtils.isEmpty(edsCase.getCrmAccount().getName())) {
                    messageText.append("Company: ").append(edsCase.getCrmAccount().getName()).append("\n");
                } else if (edsCase.getCrmContact() != null && edsCase.getCrmContact().getCrmAccount() != null
                        && !StringUtils.isEmpty(edsCase.getCrmContact().getCrmAccount().getName())) {
                    messageText.append("Company: ").append(edsCase.getCrmContact().getCrmAccount().getName()).append("\n");
                } else if (edsCase.getLead() != null && edsCase.getLead().getCrmAccount() != null
                        && !StringUtils.isEmpty(edsCase.getLead().getCrmAccount().getName())) {
                    messageText.append("Company: ").append(edsCase.getLead().getCrmAccount().getName()).append("\n");
                }
                messageText.append("Case Subject: ").append(edsCase.getSubject()).append("\n");
                if (!StringUtils.isEmpty(edsCase.getReportedBy())) {
                    messageText.append("Reported by: ").append(edsCase.getReportedBy()).append("\n");
                }
                if (edsUser != null) {
                    messageText.append("Reported Date: ").append(ServerUtils.shortDateFormat(edsCase.getAuditInfo().getCreationDate(), edsUser));
                }

                List<EdsTelegramChat> edsChats = telegramChatManager.getActiveChatsByType(messageType);

                edsChats.forEach(edsChat -> {
                    if (!telegramBlackListService.validate(edsChat.getChatId())) {
                        sendMessage(edsChat.getChatId(), edsChat.getChatName(), messageText.toString());
                    }
                });
            }
        }
    }

    @Override
    @Transactional
    public ListResult<TelegramSettingsItem> getTelegramSettingsList(ListingFilterParameter fp) {
        return globalAuthJdbcSpringManager.getTelegramSettingItems(fp);
    }

    @Override
    public SelectItem[] getTelegramSettingsAsSelectItems() {
        return globalAuthJdbcSpringManager.getTelegramSettingItems(new ListingFilterParameter())
                .getList()
                .stream()
                .map(x -> new SelectItem(x.getId(), x.getBotName(), x.getToken()))
                .toArray(SelectItem[]::new);
    }

    @Override
    @Transactional
    public Boolean deleteTelegramSettingsItem(Integer id) {
        return globalAuthJdbcSpringManager.deleteTelegramSettingsItem(id);
    }

    @Override
    @Transactional
    public Integer saveTelegramSettingsItem(TelegramSettingsItem settingsItem) {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setAccessToken(settingsItem.getToken());
        filterParameter.setName(settingsItem.getBotName());
        filterParameter.setObjectId(settingsItem.getId());

        ListResult<TelegramSettingsItem> telegramSettingItems = globalAuthJdbcSpringManager.getTelegramSettingItems(filterParameter);
        if (telegramSettingItems.getTotal() > 0) {
            return 2;
        }
        TelegramBot telegramBot = new TelegramBot(settingsItem.getToken());
        SetWebhook webhook = new SetWebhook();
        webhook.url(EdsContextParams.getHost() + "/services/api/v2/telegram/updates/" + settingsItem.getToken());

        BaseResponse response = telegramBot.execute(webhook);
        boolean ok = response.isOk();
        if (ok) {
            settingsItem.setCompanyId(SecurityContext.getCompanyID());
            globalAuthJdbcSpringManager.saveTelegramSettingsItem(settingsItem);
            return 1;
        }
        return 3;
    }

    @Override
    @Transactional
    public TelegramSettingsItem getTelegramSettingsItem(Integer id) {
        return globalAuthJdbcSpringManager.getTelegramSettingsItem(id);
    }
}
