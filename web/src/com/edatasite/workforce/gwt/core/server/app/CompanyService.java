package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.server.controllers.dto.AccountingSetupItem;
import com.edatasite.workforce.rest.v3.release10.auth.dto.AccountingInitTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyInitTO;
import com.google.gwt.user.client.rpc.RemoteService;

public interface CompanyService extends RemoteService {

    String resetCompany();

    boolean saveCompanyInit(String companyName, String language, String orgType, Integer timeZoneID, String selectedApps);

    boolean saveCompanyAccountingSettings(AccountingSetupItem setupItem);

    boolean saveCompanyCrmSettings( Integer industryId, Address companyBillingAddress);

    void gymCompanyInit(CompanyInitTO companyInitTO);

    void gymAccountingInit(AccountingInitTO accountingInitTO);
}
