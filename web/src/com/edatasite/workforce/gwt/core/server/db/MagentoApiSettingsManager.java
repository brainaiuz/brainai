package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.magento.EdsMagentoApiSettings;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
public interface MagentoApiSettingsManager extends Manager<EdsMagentoApiSettings> {

    EdsMagentoApiSettings getSettings();
}
