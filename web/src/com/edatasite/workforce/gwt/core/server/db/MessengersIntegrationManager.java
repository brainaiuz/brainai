package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsMessengersIntegration;

public interface MessengersIntegrationManager extends Manager<EdsMessengersIntegration>{

    EdsMessengersIntegration getCompanyCredentials();
}
