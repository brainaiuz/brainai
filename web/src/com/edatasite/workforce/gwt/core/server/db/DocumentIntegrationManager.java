package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.settings.EdsDocumentIntegration;

public interface DocumentIntegrationManager extends Manager<EdsDocumentIntegration> {
    EdsDocumentIntegration getCompanyCredentials();
}
