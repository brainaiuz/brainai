package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;

public class TelegramRecurrenceMessage implements IsSerializable {

    private Integer ruleId;
    private String ruleName;
    private TelegramSettingsItem telegramSettingsItem;
    private List<TelegramChatListItem> telegramChatListItems;
    private boolean status;
    private String content;
    private RecurrenceJobItem recurrenceJobItem;
    private Integer reportId;
    private String reportType;

    public TelegramRecurrenceMessage() {
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<TelegramChatListItem> getTelegramChatListItems() {
        return telegramChatListItems;
    }

    public void setTelegramChatListItems(List<TelegramChatListItem> telegramChatListItems) {
        this.telegramChatListItems = telegramChatListItems;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public TelegramSettingsItem getTelegramSettingsItem() {
        return telegramSettingsItem;
    }

    public void setTelegramSettingsItem(TelegramSettingsItem telegramSettingsItem) {
        this.telegramSettingsItem = telegramSettingsItem;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }
}
