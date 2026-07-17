package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsQuickAddSettings;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface QuickAddSettingsManager extends Manager<EdsQuickAddSettings> {
    EdsQuickAddSettings getByForm(QuickAddSettingsForm form);
}
