package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsHRSettings;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface HRSettingsManager extends Manager<EdsHRSettings> {
    EdsHRSettings findOne();
}
