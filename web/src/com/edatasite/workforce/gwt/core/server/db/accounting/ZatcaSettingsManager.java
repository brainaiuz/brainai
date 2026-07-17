package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsZatcaSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface ZatcaSettingsManager extends Manager<EdsZatcaSettings> {
    EdsZatcaSettings getZatcaSettings();
}
