package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebhookAttribute;

public interface PublicWebhookAttributeManager extends Manager<EdsPublicWebhookAttribute> {
    void deleteAllAttributeByWebHook(EdsPublicWebHook webHook);
}
