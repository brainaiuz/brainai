package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsMessengersIntegration;
import com.edatasite.workforce.gwt.core.server.db.MessengersIntegrationManager;
import org.springframework.stereotype.Repository;

@Repository("messengersIntegrationManager")
public class MessengersIntegrationManagerImpl extends BaseManager<EdsMessengersIntegration> implements MessengersIntegrationManager {
    public MessengersIntegrationManagerImpl() {
        super(EdsMessengersIntegration.class);
    }

    @Override
    public EdsMessengersIntegration getCompanyCredentials() {
        return (EdsMessengersIntegration) findSingle("select m from EdsMessengersIntegration m");
    }
}
