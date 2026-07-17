package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAsteriskSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.AsteriskSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/4/2020
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("asteriskSettingsManager")
public class AsteriskSettingsManagerImpl extends BaseManager<EdsAsteriskSettings> implements AsteriskSettingsManager, Constants {

    public AsteriskSettingsManagerImpl() {
        super(EdsAsteriskSettings.class);
    }

    @Override
    public List<EdsAsteriskSettings> list(ListingFilterParameter fp) {
        return find(getQuery(fp, false));
    }

    public String getQuery(ListingFilterParameter fp, boolean counting) {
        Integer companyID = fp.getCompanyID() == null ? SecurityContext.getCompanyID() : fp.getCompanyID();
        StringBuilder s = new StringBuilder();
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" AND (");
            s.append("LOWER(number) LIKE '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        if(!counting){
            s.append(" ORDER BY number");
            if (fp.isAscending()) {
                s.append(" DESC ");
            }
        }
        return "SELECT " + (counting ? "count( distinct t.objectID)": "t" )+" FROM EdsAsteriskSettings t WHERE (t.deleted is null or t.deleted<>true)" + s;
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
    public EdsAsteriskSettings getByNumber(String number) {
        return (EdsAsteriskSettings) findSingle("SELECT t FROM EdsAsteriskSettings t WHERE t.deleted is null OR t.deleted<> true AND t.number = ?", number);
    }
}
