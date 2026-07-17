package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHookParameter;
import com.edatasite.workforce.gwt.core.server.db.PublicWebhookParameterManager;
import org.springframework.stereotype.Repository;

/**
 * User : Akhror
 * Date : 13.03.2025
 */
@Repository("publicWebhookParameterManager")
public class PublicWebhookParameterManagerImpl extends BaseManager<EdsPublicWebHookParameter> implements PublicWebhookParameterManager {
    public PublicWebhookParameterManagerImpl() {
        super(EdsPublicWebHookParameter.class);
    }

    @Override
    public void deleteAllParametersByWebHook(EdsPublicWebHook webHook) {
        update("delete from EdsPublicWebHookParameter p where p.webHook = ? or p.webHookBody = ? ", webHook, webHook.getBody());
    }
}
