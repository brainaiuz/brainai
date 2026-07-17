package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.workflow.EdsWebhookAttribute;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowWebHook;

/**
 * User : Akhror
 * Date : 11.07.2023
 */
public interface WebhookAttributeManager extends Manager<EdsWebhookAttribute> {
    void deleteAllAttributeByWebHook(EdsWorkflowWebHook webHook);
}
