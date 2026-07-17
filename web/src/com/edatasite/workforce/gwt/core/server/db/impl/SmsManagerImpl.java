package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSmsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.SmsManager;
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
@Repository("smsManager")
public class SmsManagerImpl extends BaseManager<EdsSmsSettings> implements SmsManager, Constants {

    public SmsManagerImpl() {
        super(EdsSmsSettings.class);
    }

    @Override
    public List<EdsSmsSettings> list(ListingFilterParameter fp) {
        Integer companyID = fp.getCompanyID() == null ? SecurityContext.getCompanyID() : fp.getCompanyID();
        StringBuilder s = new StringBuilder();
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            s.append(" and (");
            s.append("lower(name) like '" + fp.getSqlSearchKey() + "' ");
            s.append(") ");
        }
        s.append(" AND company.objectID = ").append(companyID);
        s.append(" ORDER BY name");
        if (fp.isAscending()) {
            s.append(" DESC ");
        }
        return find("SELECT t FROM EdsSmsSettings t WHERE (t.deleted is null or t.deleted<>true)" + s);
    }

    @Override
    public EdsSmsSettings getDefault() {
        return (EdsSmsSettings) findSingle("select distinct sms from EdsSmsSettings sms");
    }
}
