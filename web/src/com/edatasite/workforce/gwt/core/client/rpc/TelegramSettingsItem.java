package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TelegramSettingsItem implements IsSerializable {
    public static final String NUMBER = "TOKEN";
    public static final String BOT_NAME = "BOT_NAME";
    public static final String AUTOMATION_RULE = "AUTOMATION_RULE";

    private Integer id;
    private String token;
    private String botName;
    private Integer companyId;

    public TelegramSettingsItem() {
    }

    public TelegramSettingsItem(Integer id, String token, String botName) {
        this.id = id;
        this.token = token;
        this.botName = botName;
    }

    public TelegramSettingsItem(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
