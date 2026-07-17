package com.edatasite.workforce.gwt.core.server.rabbitmq.receiver;

import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.server.db.EmailFolderManager;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.DataMQ;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.messagecenter.server.MessageCenterServiceLocal;
import com.edatasite.workforce.gwt.messagecenter.server.app.DefaultMailServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Store;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.eclipse.angus.mail.util.MailConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EmailFetchListener extends BaseAmqpListener<Integer> {

    private static final Logger log = LoggerFactory.getLogger(EmailFetchListener.class);
    private static final Map<String, Boolean> runningEmailSettings = new PassiveExpiringMap<>(600000);//key = "companyid_emailsettingid"

    @Autowired
    private EmailFolderManager emailFolderManager;
    @Autowired
    private EmailSettingsManager emailSettingsManager;
    @Autowired
    private MessageCenterServiceLocal messageCenterService;

    @Override
    protected void receiveMessage(Integer emailSettingId) {
        String key = getKey(emailSettingId);
        if (runningEmailSettings.containsKey(key)) {
            log.info("EmailSettings[{}] already exists in Queue", key);
            return;
        }

        EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingId);
        if (emailSetting == null || !emailSetting.isActive()) {
            return;
        }

        runningEmailSettings.put(key, true);

        Date fetchingStartDate = new Date();
        Store store = null;
        final String uuid = UUID.randomUUID().toString();

        try {
            final List<Integer> folderIds = emailFolderManager.getFolderIdsForFetch(emailSettingId, false);
            if (folderIds.isEmpty()) {
                log.info("UUID[{}], No folders to fetch for emailSetting[{}]", uuid, emailSettingId);
                return;
            }

            if (emailSetting.isDefaultProvider()) {
                store = DefaultMailServiceImpl.connectAndGetEmailStore(emailSetting);
                if (store == null || !store.isConnected()) {
                    log.error("UUID[{}], Company[{}], email[{}] — Store is not connected", uuid, SecurityContext.getCompanyID(), emailSetting.getEmail());
                    return;
                }
            }
            for (Integer folderId : folderIds) {
                try {
                    Integer count = messageCenterService.fetchEmail(folderId, SecurityContext.getCompanyID(), emailSetting.getObjectID(), store);
                    if (count != null && count > 0) {
                        log.info("UUID[{}], Company[{}], email[{}], folder[{}] — {} emails fetched", uuid, SecurityContext.getCompanyID(), emailSetting.getEmail(), folderId, count);
                    }
                } catch (Exception e) {
                    log.error("UUID[{}], Company[{}], email[{}], folderId[{}]", uuid, SecurityContext.getInstance().getCompanyId(), emailSetting.getEmail(), folderId, e);
                    processException(e, emailSettingId);
                } catch (Throwable e) {
                    log.error("Unexpected error UUID[{}]: {}", uuid, e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            log.error("Email fetch failed — UUID[{}], Company[{}], email[{}]", uuid, SecurityContext.getInstance().getCompanyId(), emailSetting.getEmail(), e);
            processException(e, emailSettingId);
        } finally {
            runningEmailSettings.remove(key);
            emailSettingsManager.updateFetchingTimes(emailSetting.getObjectID(), fetchingStartDate, new Date());
            closeStoreQuietly(store, emailSettingId);
        }
    }

    private void closeStoreQuietly(Store store, Integer emailSettingId) {
        if (store != null && store.isConnected()) {
            try {
                store.close();
            } catch (MessagingException e) {
                log.error("Store yopishda xato, settings[{}]: {}", emailSettingId, e.getMessage());
            }
        }
    }

    private void processException(Exception e, Integer id) {
        if (e instanceof AuthenticationFailedException || e instanceof NoSuchProviderException) {
            messageCenterService.updateEmailSettingsAfterException(id, "Eror message 1 - " + e.getMessage());
        } else if (e instanceof MailConnectException ex) {
            if (ex.getNextException() instanceof ConnectException) {
                //todo postpone fetch feature
            }
        } else if (e instanceof MessagingException ex) {
            if (ex.getNextException() instanceof UnknownHostException) {
                messageCenterService.updateEmailSettingsAfterException(id, "Eror message 2 - " + e.getMessage());
            } else if (ex.getNextException() instanceof ConnectException) {
                if (ex.getNextException().getMessage().contains("refused")) {
                    messageCenterService.updateEmailSettingsAfterException(id, "Eror message 3 - " + e.getMessage());
                } else if (ex.getNextException().getMessage().contains("timed out")) {
                    messageCenterService.updateEmailSettingsAfterException(id, "Eror message 4 - " + e.getMessage());
                }
            }
        }
    }

    @Override
    protected DataMQ<Integer> convertMessage(String message) {
        return new Gson().fromJson(message, new TypeToken<DataMQ<Integer>>() {
        }.getType());
    }

    private String getKey(Integer key) {
        return SecurityContext.getInstance().getCompanyId() + "@" + key;
    }
}
