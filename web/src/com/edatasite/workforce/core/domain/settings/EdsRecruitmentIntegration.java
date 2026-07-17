package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "recruitment_integration")
public class EdsRecruitmentIntegration extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "hh_client_id")
    private String hhClientId;

    @Column(name = "hh_client_secret")
    private String hhClientSecret;

    @Column(name = "hh_code")
    private String hhCode;

    @Column(name = "hh_access_token")
    private String hhAccessToken;

    @Column(name = "hh_refresh_token")
    private String hhRefreshToken;

    @Column(name = "hh_token_expire_at")
    private Date hhTokenExpireAt;

    @Column(name = "zoom_client_id")
    private String zoomClientId;

    @Column(name = "zoom_client_secret")
    private String zoomClientSecret;

    @Column(name = "zoom_code")
    private String zoomCode;

    @Column(name = "zoom_access_token", length = 1000)
    private String zoomAccessToken;

    @Column(name = "zoom_refresh_token", length = 1000)
    private String zoomRefreshToken;

    @Column(name = "telegram_bot_token")
    private String telegramBotToken;

    @Column(name = "telegram_bot_username")
    private String telegramBotUserName;

    @Override
    public Integer getObjectID() {
        return objectID;
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

    public String getHhCode() {
        return hhCode;
    }

    public void setHhCode(String hhCode) {
        this.hhCode = hhCode;
    }

    public String getHhAccessToken() {
        return hhAccessToken;
    }

    public void setHhAccessToken(String hhAccessToken) {
        this.hhAccessToken = hhAccessToken;
    }

    public String getHhRefreshToken() {
        return hhRefreshToken;
    }

    public void setHhRefreshToken(String hhRefreshToken) {
        this.hhRefreshToken = hhRefreshToken;
    }

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

    public String getZoomCode() {
        return zoomCode;
    }

    public void setZoomCode(String zoomCode) {
        this.zoomCode = zoomCode;
    }

    public String getZoomAccessToken() {
        return zoomAccessToken;
    }

    public void setZoomAccessToken(String zoomAccessToken) {
        this.zoomAccessToken = zoomAccessToken;
    }

    public String getZoomRefreshToken() {
        return zoomRefreshToken;
    }

    public void setZoomRefreshToken(String zoomRefreshToken) {
        this.zoomRefreshToken = zoomRefreshToken;
    }

    public Date getHhTokenExpireAt() {
        return hhTokenExpireAt;
    }

    public void setHhTokenExpireAt(Date hhTokenExpireAt) {
        this.hhTokenExpireAt = hhTokenExpireAt;
    }

    public String getTelegramBotToken() {
        return telegramBotToken;
    }

    public void setTelegramBotToken(String telegramBotToken) {
        this.telegramBotToken = telegramBotToken;
    }

    public String getTelegramBotUsername() {
        return telegramBotUserName;
    }
    public void setTelegramBotUsername(String telegramBotUsername) {
        this.telegramBotUserName = telegramBotUsername;
    }
}
