package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.shared.mail.MailMessage;
import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import jakarta.mail.Store;
import kpi.javax.mail.internet.KPIMimeMessage;
import org.springframework.plugin.core.Plugin;

import java.util.Set;

public interface MailService extends Plugin<String> {

    Integer fetchEmails(Integer folderId, Integer companyId, Integer emailSettingsId, Store store) throws Exception;

    // @return trackerId
    Integer sendMessage(EdsEmailSetting settings, MailMessage mail);

    Email getWithContent(EdsEmail email);

    void createFolders(EdsEmailSetting emailSettings, boolean isFirstTime);

    void setFlags(Set<Email> emails, Integer emailSettingID, String flag);

    KPIMimeMessage toMimeMessage(EdsEmail email);
}
