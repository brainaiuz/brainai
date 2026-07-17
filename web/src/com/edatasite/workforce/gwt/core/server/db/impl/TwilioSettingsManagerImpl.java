package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTwilioSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.TwilioSettingsManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Virus
 * Date: 7/15/11
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("twilioSettingsManager")
public class TwilioSettingsManagerImpl extends BaseManager<EdsTwilioSettings> implements TwilioSettingsManager, Constants {

    public TwilioSettingsManagerImpl() {
        super(EdsTwilioSettings.class);
    }

    @Override
    public List<EdsTwilioSettings> list(ListingFilterParameter fp) {
        return find(getQuery(fp, false));
    }

    public String getQuery(ListingFilterParameter fp, boolean counting) {
        Integer companyID = fp.getCompanyID() == null ? SecurityContext.getCompanyID() : fp.getCompanyID();
        StringBuilder s = new StringBuilder();
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" and (");
            s.append("lower(number) like '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        if(!counting){
            s.append(" ORDER BY number");
            if (fp.isAscending()) {
                s.append(" DESC ");
            }
        }
        return "SELECT " + (counting ? "count( distinct t.objectID)": "t" )+" FROM EdsTwilioSettings t WHERE (t.deleted is null or t.deleted<>true)" + s;
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
    public EdsTwilioSettings getByNumber(String number) {
        return (EdsTwilioSettings) findSingle("select t from EdsTwilioSettings t where t.deleted is null or t.deleted<> true and t.number = ?", number);
    }
}
