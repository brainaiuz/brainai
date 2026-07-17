package com.edatasite.shared.mail;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;

/**
 * User: Aziz
 * Date: 23.12.11
 */
public class EdsMailParams {
    private String smtpProtocol = "smtp";
    private String smtpHost;
    private String smtpPort;
    private String login;
    private String password;
    private String IsSSL;
    private String isAuth = "true";
    private String email;
    private String from;

    public String getAuth() {
        return isAuth;
    }

    public void setAuth(String auth) {
        isAuth = auth != null && !"".equals(auth) ? auth : isAuth;
    }

    public String getSmtpProtocol() {
        return smtpProtocol;
    }

    public void setSmtpProtocol(String smtpProtocol) {
        this.smtpProtocol = smtpProtocol;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public String getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(String smtpPort) {
        this.smtpPort = smtpPort;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsSSL() {
        return IsSSL;
    }

    public void setIsSSL(String isSSL) {
        IsSSL = isSSL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public EdsEmailSetting asEdsEmailSetting() {
        EdsEmailSetting setting = new EdsEmailSetting();
        setting.setUserName(getLogin());
        setting.setPassword(EncryptionHelper.encrypt(getPassword()));
        setting.setEmail(getEmail());
        setting.setFromName(getFrom());
        setting.setEmailHostSMTP(getSmtpHost());
        try {
            setting.setEmailPortSMTP(Integer.valueOf(getSmtpPort()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        setting.setEmailSmtpProtocol(getSmtpProtocol());
        try {
            setting.setSmtpAuth(Boolean.valueOf(getAuth()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return setting;
    }
}
