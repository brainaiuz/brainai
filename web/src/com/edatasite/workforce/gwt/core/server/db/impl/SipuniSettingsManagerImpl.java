package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSipuniSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.SipuniSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("sipuniSettingsManager")
public class SipuniSettingsManagerImpl extends BaseManager<EdsSipuniSettings> implements SipuniSettingsManager, Constants {

    public SipuniSettingsManagerImpl() {
        super(EdsSipuniSettings.class);
    }


    public String getQuery(ListingFilterParameter fp, boolean counting) {
        StringBuilder s = new StringBuilder();
        String companyId = getCompanyId().replaceAll("\"", "");
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" AND (");
            s.append("LOWER(sip_number) LIKE '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        if (!counting) {
            s.append(" ORDER BY sip_number");
            if (fp.isAscending()) {
                s.append(" DESC ");
            }
        }
        return "SELECT " + (counting ? "count( distinct t.objectID)" : "t") + " FROM EdsSipuniSettings t WHERE (t.deleted is null or t.deleted<>true) and companyId = " + companyId + s;
    }

    @Override
    public List<EdsSipuniSettings> list(ListingFilterParameter fp) {
        return find(getQuery(fp, false));
    }

    @Override
    public int listCount(ListingFilterParameter filterParameter) {
        Long count = (Long) findSingle(getQuery(filterParameter, true));
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    @Override
    public EdsSipuniSettings getSipuniSettingsByUser(Integer userId) {
        return (EdsSipuniSettings) findNativeSingle("select * from  sipuni_settings where operator_id = " + userId, EdsSipuniSettings.class);
    }

    @Override
    public EdsSipuniSettings getSipuniSettingsBySipNumber(String sipNumber) {
        Integer companyId = SecurityContext.getCompanyID();
        return (EdsSipuniSettings) findNativeSingle("select * from sipuni_settings where sip_number = '" + sipNumber + "' and companyId = " + companyId, EdsSipuniSettings.class);
    }
}
