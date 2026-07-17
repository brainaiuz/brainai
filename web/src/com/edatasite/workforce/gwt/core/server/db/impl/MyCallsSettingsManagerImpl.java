package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMyCallsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.MyCallsSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("myCallsSettingsManager")
public class MyCallsSettingsManagerImpl extends BaseManager<EdsMyCallsSettings> implements MyCallsSettingsManager, Constants {
    public MyCallsSettingsManagerImpl() {
        super(EdsMyCallsSettings.class);
    }

    public String getQuery(ListingFilterParameter fp, boolean counting) {
        StringBuilder s = new StringBuilder();
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" AND (");
            s.append("LOWER(user_login) LIKE '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        if(!counting){
            s.append(" ORDER BY id");
            if (fp.isAscending()) {
                s.append(" DESC ");
            }
        }
        return "SELECT " + (counting ? "count( distinct t.objectID)": "t" )+" FROM EdsMyCallsSettings t WHERE (t.deleted is null or t.deleted<>true)" + s;
    }


    @Override
    public List<EdsMyCallsSettings> list(ListingFilterParameter fp) {
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
    public EdsMyCallsSettings getMyCallsSettingsByUser(Integer userId) {
        return (EdsMyCallsSettings) findNativeSingle("select * from  mycalls_settings where operator_id = "  + userId,EdsMyCallsSettings.class);

    }

    @Override
    public EdsMyCallsSettings getMyCallsSettingsBySipNumber(String userLogin) {
        return (EdsMyCallsSettings) findNativeSingle("select * from mycalls_settings where user_login = '" + userLogin + "'",EdsMyCallsSettings.class);

    }
}
