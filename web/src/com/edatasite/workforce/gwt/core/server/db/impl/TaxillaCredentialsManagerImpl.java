package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsTaxillaCredentials;
import com.edatasite.workforce.gwt.core.server.db.TaxillaCredentialsManager;
import org.springframework.stereotype.Repository;

@Repository("taxillaCredentialsManager")
public class TaxillaCredentialsManagerImpl extends BaseManager<EdsTaxillaCredentials> implements TaxillaCredentialsManager {
    public TaxillaCredentialsManagerImpl() {
        super(EdsTaxillaCredentials.class);
    }

    @Override
    public EdsTaxillaCredentials getTaxillaCredential() {
        return (EdsTaxillaCredentials) findNativeSingle("select * from " + getCompanyId() + ".taxilla_credentials", EdsTaxillaCredentials.class);
    }

    public String getTaxillaAccesstoken() {
        String sqlQuery = "select access_token from " + getCompanyId() + ".taxilla_credentials";
        return (String) findNativeSingle(sqlQuery, String.class);
    }

}