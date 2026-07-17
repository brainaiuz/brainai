package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsSMSTemplates;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.SMSTemplateManager;
import com.edatasite.workforce.gwt.profile.client.rpc.SMSTemplateItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Azazello on 4/21/15.
 */
@Repository("smsTemplateManager")
public class SMSTemplateManagerImpl extends BaseManager<EdsSMSTemplates> implements SMSTemplateManager {
    public SMSTemplateManagerImpl() {
        super(EdsSMSTemplates.class);
    }

    @Override
    public List<EdsSMSTemplates> getSMSTemplates(ListingFilterParameter fp) {
        StringBuilder s = new StringBuilder();
        if (fp.isValidSearchKey()) {
            s.append(" and (");
            s.append("lower(name) like '" + fp.getSqlSearchKey() + "' ");
            s.append(" or lower(module.name) like '" + fp.getSqlSearchKey() + "'");
            s.append(") ");
        }
        //get by module
        if (fp.getModule() != null) {
            s.append(" and module.code = '" + fp.getModule() + "' ");
        }
        s.append(" ORDER BY ");
        if (fp.getSortField() == null && "".equals(fp.getSortField())) {
            s.append("name");
        } else if (SMSTemplateItem.NAME.equals(fp.getSortField())) {
            s.append("name");
        } else if (SMSTemplateItem.MODULE.equals(fp.getSortField())) {
            s.append("module.name");
        } else if (SMSTemplateItem.PROVIDER.equals(fp.getSortField())) {
            s.append("settings.providerName");
        } else if (SMSTemplateItem.DEFAULT.equals(fp.getSortField())) {
            s.append("isDefault");
        } else {
            s.append("name");
        }
        if (fp.isAscending()) {
            s.append(" DESC ");
        }
        return find("SELECT t FROM EdsSMSTemplates t WHERE (t.deleted is null or t.deleted<>true)" + s);
    }
}
