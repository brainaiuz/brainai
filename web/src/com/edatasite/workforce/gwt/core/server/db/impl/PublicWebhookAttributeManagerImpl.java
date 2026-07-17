package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebhookAttribute;
import com.edatasite.workforce.gwt.core.server.db.PublicWebhookAttributeManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 13.03.2025
 */
@Repository("publicWebhookAttributeManager")
public class PublicWebhookAttributeManagerImpl extends BaseManager<EdsPublicWebhookAttribute> implements PublicWebhookAttributeManager {
    public PublicWebhookAttributeManagerImpl() {
        super(EdsPublicWebhookAttribute.class);
    }

    @Override
    public void deleteAllAttributeByWebHook(EdsPublicWebHook webHook) {
        update("delete from EdsPublicWebhookAttribute a where a.webHook = ?", webHook);
    }
}
