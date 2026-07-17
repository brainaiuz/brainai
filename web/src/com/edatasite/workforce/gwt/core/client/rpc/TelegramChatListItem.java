package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 31.07.2017 21:03
 */
public class TelegramChatListItem implements IsSerializable {

    public static final String CHAT_NAME = "chatName";
    public static final String CHAT_TYPE = "chatType";
    public static final String CREATOR = "creator";
    public static final String ACTIVE = "active";

    private Integer objectId;
    private Long chatId;
    private String chatName;
    private String chatType;
    private SelectItem creator;
    private boolean active;
    private Integer telegramBotId;
    private String telegramBotToken;

    private boolean sendCaseCreate;

    public TelegramChatListItem() {
    }

    public TelegramChatListItem(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getChatType() {
        return chatType;
    }

    public void setChatType(String chatType) {
        this.chatType = chatType;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSendCaseCreate() {
        return sendCaseCreate;
    }

    public void setSendCaseCreate(boolean sendCaseCreate) {
        this.sendCaseCreate = sendCaseCreate;
    }

    public Integer getTelegramBotId() {
        return telegramBotId;
    }

    public void setTelegramBotId(Integer telegramBotId) {
        this.telegramBotId = telegramBotId;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public void setTelegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }
}
