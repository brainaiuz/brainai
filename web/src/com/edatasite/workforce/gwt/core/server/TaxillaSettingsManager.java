package com.edatasite.workforce.gwt.core.server;

import com.edatasite.workforce.core.domain.accounting.EdsTaxillaSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface TaxillaSettingsManager extends Manager<EdsTaxillaSettings> {
    EdsTaxillaSettings getTaxillaSettings();

}
