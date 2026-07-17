package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsTaxillaCredentials;

public interface TaxillaCredentialsManager extends Manager<EdsTaxillaCredentials> {
    EdsTaxillaCredentials getTaxillaCredential();

    String getTaxillaAccesstoken();

}
