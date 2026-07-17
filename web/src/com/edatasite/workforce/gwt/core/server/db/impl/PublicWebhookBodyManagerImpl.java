package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHookBody;
import com.edatasite.workforce.gwt.core.server.db.PublicWebhookBodyManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 13.03.2025
 */
@Repository("publicWebhookBodyManager")
public class PublicWebhookBodyManagerImpl extends BaseManager<EdsPublicWebHookBody> implements PublicWebhookBodyManager {
    public PublicWebhookBodyManagerImpl() {
        super(EdsPublicWebHookBody.class);
    }
}
