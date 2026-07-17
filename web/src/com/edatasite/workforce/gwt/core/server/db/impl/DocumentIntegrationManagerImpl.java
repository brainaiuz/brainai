package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsDocumentIntegration;
import com.edatasite.workforce.gwt.core.server.db.DocumentIntegrationManager;
import org.springframework.stereotype.Repository;

@Repository("documentIntegrationManager")
public class DocumentIntegrationManagerImpl extends BaseManager<EdsDocumentIntegration> implements DocumentIntegrationManager {
    public DocumentIntegrationManagerImpl() {
        super(EdsDocumentIntegration.class);
    }

    @Override
    public EdsDocumentIntegration getCompanyCredentials() {
        return (EdsDocumentIntegration) findSingle("select r from EdsDocumentIntegration r");
    }
}
