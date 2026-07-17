package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsIntegrationSettings;
import com.edatasite.workforce.gwt.core.server.db.IntegrationSettingsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shohruh on 02-Feb-17.
 */
@Repository("integrationSettingsManager")
public class IntegrationSettingsManagerImpl extends BaseManager<EdsIntegrationSettings> implements IntegrationSettingsManager {

    public IntegrationSettingsManagerImpl() {
        super(EdsIntegrationSettings.class);
    }

    @Override
    public List<EdsIntegrationSettings> getIntegrationSettings() {
        return find("from EdsIntegrationSettings");
    }
}
