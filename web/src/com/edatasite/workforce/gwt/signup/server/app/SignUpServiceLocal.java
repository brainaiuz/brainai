package com.edatasite.workforce.gwt.signup.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;

import java.util.HashSet;

/**
 * User: admin
 * Date: Oct 5, 2009
 * Time: 7:38:11 PM
 */
public interface SignUpServiceLocal {

    void createFreeTrialUsagePlan(Integer companyId, boolean isCurrencyGBP, int usersCount, String hostName, String pricingPackageNAME);

    void createOneYearUsagePlan(Integer companyId, String hostName);

    SelectItem[] getCountries();

    CreatedCompany createCompany(NewCompany company);

    CreatedCompany createSampleCompany(NewCompany company, TemplateSchema templateSchema);

    DomainInfo findByGoogleAppDomain(String domainName, String googleAppDomain, String email);

    String preProcessSampleCompanyData(Integer scheme, TemplateSchema templateSchema);

    String preProcessCompanyData(Integer scheme, HashSet<String> activeModules);

    Integer getCompany();

    void sendCompanyRegistrationNotification(NewCompany company) throws EdsDbException;

    void getParamsFromMarketPlace(StringBuffer stringBuffer, String message);

    boolean validateForSecurity(NewCompany company);
}
