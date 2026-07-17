package com.edatasite.workforce.gwt.messagecenter.server.app;

import com.edatasite.workforce.core.domain.emailfetching.mongo.EdsEmail;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.server.db.EmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.emailfetching.mongo.EmailRepository;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CacheConstants;
import com.edatasite.workforce.utils.redis.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.plugin.core.OrderAwarePluginRegistry;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailServices {

    private final PluginRegistry<MailService, String> registry;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailSettingsManager emailSettingsManager;

    @Autowired
    public MailServices(List<MailService> services) {
        this.registry = OrderAwarePluginRegistry.create(services);
    }

    public MailService getService(String emailId) {
        return registry.getPluginFor(getProviderByEmailId(emailId)).orElseThrow();
    }

    public MailService getService(Integer emailSettingId) {
        return registry.getPluginFor(getProviderbyEmailSettingId(emailSettingId)).orElseThrow();
    }

    public MailService getService(EdsEmailSetting.Provider provider) {
        return registry.getPluginFor(provider.name()).orElseThrow();
    }

    private String getProviderByEmailId(String emailId) {
        EdsEmail email = emailRepository.findById(emailId).get();
        String provider = RedisClient.getKey(getProviderKey(email.getEmailSettingId()));
        if (provider == null) {
            EdsEmailSetting emailSetting = emailSettingsManager.get(email.getEmailSettingId());
            provider = emailSetting.getProvider().name();
            RedisClient.setKey(getProviderKey(email.getEmailSettingId()), provider);
        }
        return provider;
    }

    private String getProviderbyEmailSettingId(Integer emailSettingId) {
        String provider = RedisClient.getKey(getProviderKey(emailSettingId));
        if (provider == null) {
            EdsEmailSetting emailSetting = emailSettingsManager.get(emailSettingId);
            provider = emailSetting.getProvider().name();
            RedisClient.setKey(getProviderKey(emailSettingId), provider);
        }
        return provider;
    }

    private static String getProviderKey(Integer emailSettingId) {
        return CacheConstants.EMAIL_SETTING_PROVIDER + "_" + SecurityContext.getCompanyID() + "_" + emailSettingId;
    }
}
