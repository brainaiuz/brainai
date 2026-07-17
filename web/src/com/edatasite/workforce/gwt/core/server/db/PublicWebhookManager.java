package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User : Akhror
 * Date : 12.03.2025
 */
public interface PublicWebhookManager extends Manager<EdsPublicWebHook> {
    List<EdsPublicWebHook> list(ListingFilterParameter fp);
}
