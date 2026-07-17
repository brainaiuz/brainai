package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHookParameter;

public interface PublicWebhookParameterManager extends Manager<EdsPublicWebHookParameter> {
    void deleteAllParametersByWebHook(EdsPublicWebHook webHook);
}
