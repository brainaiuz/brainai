package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Anvarbek
 * Date: May 10, 2010
 * Time: 12:17:24 PM
 */
@Repository("companySystemSettingsManager")
public class CompanySystemSettingsManagerImpl extends BaseManager<EdsCompanySystemSettings> implements CompanySystemSettingsManager {


    public CompanySystemSettingsManagerImpl() {
        super(EdsCompanySystemSettings.class);
    }

    public EdsCompanySystemSettings findByGoogleAppDomain(String googleAppDomain) {
        return (EdsCompanySystemSettings) findSingle("SELECT css FROM EdsCompanySystemSettings css where css.googleAppDomain =?", googleAppDomain);
    }

    public EdsCompanySystemSettings findByCompanyID(Integer companyID) {
        if (companyID == null) {
            return null;
        }
        return (EdsCompanySystemSettings) findSingle("SELECT css FROM EdsCompanySystemSettings css where css.company.objectID =?", companyID);
    }

    @Override
    public boolean showTaskRelated() {
        Boolean bool = (Boolean) findSingle("SELECT css.showTaskRelated FROM EdsCompanySystemSettings css where css.company =?", getUser().getCompany());
        return bool != null ? bool : false;
    }

    public List<String> getCompanyHosts() {
        return (List<String>) findNative("SELECT DISTINCT css.host FROM companySystemSettings css ORDER BY css.host ASC");
    }

    @Override
    public Integer getReportingCacheTime() {
        return (Integer) findNativeSingle("select report_caching_time from " + getPublic() + ".companySystemSettings where companyid =  " + ServerSecurityContext.getInstance().getCompanyId());
    }

    @Override
    public String getNameFormat() {
        return (String) findNativeSingle("select nameFormat from " + getPublic() + ".companySystemSettings where companyid =  " + ServerSecurityContext.getInstance().getCompanyId());
    }

    @Override
    public void updateNameFormat(String companyIds, String format) {
        StringBuilder query = new StringBuilder()
                .append("UPDATE ").append(getPublic()).append(".companySystemSettings ")
                .append("SET nameFormat = '").append(format)
                .append("' WHERE companyid in (").append(companyIds).append(");");
        updateNative(query.toString());
    }
}
