package com.edatasite.workforce.gwt.profile.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class WorkflowTelegramAlert implements IsSerializable {
    public static final String TELEGRAM_BOT = "TELEGRAM_BOT";
    public static final String RECEIVER = "RECEIVER";
    public static final String MESSAGE = "MESSAGE";

    private Integer objectId;
    private Integer workflowId;
    private TelegramSettingsItem telegramBot;
    private List<TelegramChatListItem> telegramChatListItems;
    private String message;
    private String receiverAttributes;
    private WorkflowRule workflowRule;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private SelectItem[] telegramBots;
    private FileItem[] attachments;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public TelegramSettingsItem getTelegramBot() {
        return telegramBot;
    }

    public void setTelegramBot(TelegramSettingsItem telegramBot) {
        this.telegramBot = telegramBot;
    }

    public List<TelegramChatListItem> getTelegramChatListItems() {
        return telegramChatListItems;
    }

    public void setTelegramChatListItems(List<TelegramChatListItem> telegramChatListItems) {
        this.telegramChatListItems = telegramChatListItems;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WorkflowRule getWorkflowRule() {
        return workflowRule;
    }

    public void setWorkflowRule(WorkflowRule workflowRule) {
        this.workflowRule = workflowRule;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public SelectItem[] getTelegramBots() {
        return telegramBots;
    }

    public void setTelegramBots(SelectItem[] telegramBots) {
        this.telegramBots = telegramBots;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getReceiverAttributes() {
        return receiverAttributes;
    }

    public void setReceiverAttributes(String receiverAttributes) {
        this.receiverAttributes = receiverAttributes;
    }
}
