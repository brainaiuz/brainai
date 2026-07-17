package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;

import java.util.List;

/**
 * User: Anvarbek
 * Date: May 10, 2010
 * Time: 12:15:02 PM
 */
public interface CompanySystemSettingsManager extends Manager<EdsCompanySystemSettings> {

    EdsCompanySystemSettings findByGoogleAppDomain(String googleAppDomain);

    EdsCompanySystemSettings findByCompanyID(Integer companyID);

    boolean showTaskRelated();

    List<String> getCompanyHosts();

    Integer getReportingCacheTime();

    String getNameFormat();

    void updateNameFormat(String ids,String name);
}
