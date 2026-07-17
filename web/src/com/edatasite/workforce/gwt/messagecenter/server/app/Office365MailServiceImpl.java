package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.internet.MimeMessage;
import kpi.javax.mail.internet.KPIMimeMessage;

import java.util.Set;

public class Office365MailServiceImpl extends AbstractMailServiceImpl implements Office365MailServiceLocal {
    @Override
    Session initSession(EdsEmailSetting edsEmailSetting) {
        return null;
    }

    @Override
    void send(EdsEmailSetting settings, KPIMimeMessage message) throws MessagingException {

    }

    @Override
    void appendMessage(EdsEmailSetting settings, MimeMessage message, MCFolderType type) throws MessagingException {

    }

    @Override
    public Integer fetchEmails(Integer folderId, Integer companyId, Integer emailSettingsId, Store store) throws Exception {
        return null;
    }

    @Override
    public Email getWithContent(EdsEmail email) {
        return null;
    }

    @Override
    public void createFolders(EdsEmailSetting emailSettings, boolean isFirstTime) {

    }

    @Override
    public void setFlags(Set<Email> emails, Integer emailSettingID, String flag) {

    }

    @Override
    public KPIMimeMessage toMimeMessage(EdsEmail email) {
        return null;
    }

    private String getProvider() {
        return EdsEmailSetting.Provider.OFFICE365.name();
    }

    @Override
    public boolean supports(String provider) {
        return getProvider().equals(provider);
    }
}
