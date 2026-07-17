package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.magento.EdsMagentoApiSettings;
import com.edatasite.workforce.gwt.core.server.db.MagentoApiSettingsManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Shohruh on 06 Dec 2016.
 */
@Repository("magentoApiSettingsManager")
public class MagentoApiSettingsManagerImpl extends BaseManager<EdsMagentoApiSettings> implements MagentoApiSettingsManager{
    public MagentoApiSettingsManagerImpl() {
        super(EdsMagentoApiSettings.class);
    }

    @Override
    public EdsMagentoApiSettings getSettings() {
        return (EdsMagentoApiSettings) findSingle("select ms from EdsMagentoApiSettings ms");
    }
}
