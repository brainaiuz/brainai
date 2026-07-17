package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsTaxillaSettings;
import com.edatasite.workforce.gwt.core.server.TaxillaSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("taxillaSettingsManager")
public class TaxillaSettingsManagerImpl extends BaseManager<EdsTaxillaSettings> implements TaxillaSettingsManager {
    public TaxillaSettingsManagerImpl() {
        super(EdsTaxillaSettings.class);
    }

    @Override
    public EdsTaxillaSettings getTaxillaSettings() {
        return (EdsTaxillaSettings) findNativeSingle("select * from " + getCompanyId() + ".taxilla_settings", EdsTaxillaSettings.class);
    }
}