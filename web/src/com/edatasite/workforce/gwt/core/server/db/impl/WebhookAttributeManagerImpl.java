package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWebhookAttribute;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;
import com.edatasite.workforce.gwt.core.server.db.WebhookAttributeManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 11.07.2023
 */
@Repository("webhookAttributeManager")
public class WebhookAttributeManagerImpl extends BaseManager<EdsWebhookAttribute> implements WebhookAttributeManager {
    public WebhookAttributeManagerImpl() {
        super(EdsWebhookAttribute.class);
    }
    @Override
    public void deleteAllAttributeByWebHook(EdsWorkflowWebHook webHook) {
        update("delete from EdsWebhookAttribute a where a.webHook = ?", webHook);
    }
}
