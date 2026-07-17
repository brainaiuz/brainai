package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.webhook.EdsPublicWebHook;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.PublicWebhookManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User : Akhror
 * Date : 12.03.2025
 */
@Repository("publicWebHookManager")
public class PublicWebhookManagerImpl extends BaseManager<EdsPublicWebHook> implements PublicWebhookManager {
    public PublicWebhookManagerImpl() {
        super(EdsPublicWebHook.class);
    }

    @Override
    public List<EdsPublicWebHook> list(ListingFilterParameter fp) {
        String query = "from EdsPublicWebHook w ";
        if (fp != null && fp.getSearchKey() != null) {
            query += " where LOWER(w.name) LIKE '%" + fp.getSearchKey() + "%' ";
        }
        return find(query + " ORDER BY w.id ASC");
    }
}
