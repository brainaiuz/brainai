package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RecruitmentIntegrationItem implements IsSerializable {
    private String hhClientId;
    private String hhClientSecret;

    private String zoomClientId;
    private String zoomClientSecret;

    private String botToken;
    private String botUsername;

    public String getZoomClientId() {
        return zoomClientId;
    }

    public void setZoomClientId(String zoomClientId) {
        this.zoomClientId = zoomClientId;
    }

    public String getZoomClientSecret() {
        return zoomClientSecret;
    }

    public void setZoomClientSecret(String zoomClientSecret) {
        this.zoomClientSecret = zoomClientSecret;
    }

    public String getHhClientId() {
        return hhClientId;
    }

    public void setHhClientId(String hhClientId) {
        this.hhClientId = hhClientId;
    }

    public String getHhClientSecret() {
        return hhClientSecret;
    }

    public void setHhClientSecret(String hhClientSecret) {
        this.hhClientSecret = hhClientSecret;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }
}
