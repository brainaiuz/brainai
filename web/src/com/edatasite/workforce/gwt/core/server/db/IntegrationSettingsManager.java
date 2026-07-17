package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsIntegrationSettings;

import java.util.List;

/**
 * Created by Shohruh on 02-Feb-17.
 */
public interface IntegrationSettingsManager extends Manager<EdsIntegrationSettings> {
    List<EdsIntegrationSettings> getIntegrationSettings();
}
