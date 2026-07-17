package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsSMSTemplates;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by Azazello on 4/21/15.
 */
public interface SMSTemplateManager extends Manager<EdsSMSTemplates> {
    List<EdsSMSTemplates> getSMSTemplates(ListingFilterParameter fp);
}
