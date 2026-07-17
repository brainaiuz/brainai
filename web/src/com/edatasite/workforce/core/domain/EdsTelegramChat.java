package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.TelegramChatTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * User: Abror Abdukadirov
 * Date: 18.05.2017 3:52
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "telegram_chat")
public class EdsTelegramChat extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Long chatId;

    private String chatName;

    @Column(name = "chatType")
    @Enumerated(EnumType.STRING)
    private TelegramChatTypeEnum chatType;

    @Column(name = "active", columnDefinition = " boolean default false")
    private boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @Column(name = "sendCaseCreate", columnDefinition = " boolean default false")
    private boolean sendCaseCreate = false;

    @Column
    private Integer telegramBotId;

    @Column
    private String telegramBotToken;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public TelegramChatTypeEnum getChatType() {
        return chatType;
    }

    public void setChatType(TelegramChatTypeEnum chatType) {
        this.chatType = chatType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public TelegramChatListItem getRPC() {
        TelegramChatListItem item = new TelegramChatListItem();
        item.setObjectId(this.getObjectID());
        item.setChatId(this.getChatId());
        item.setChatName(this.getChatName());
        if (this.getChatType() != null) {
            item.setChatType(this.getChatType().name());
        }
        item.setActive(this.isActive());
        if (this.getCreator() != null) {
            item.setCreator(this.getCreator().getAsSelectItem());
        }
        item.setSendCaseCreate(this.isSendCaseCreate());
        return item;
    }
}
