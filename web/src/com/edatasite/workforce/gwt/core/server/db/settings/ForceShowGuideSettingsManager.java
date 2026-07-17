package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsForceShowGuidePanelSettings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface ForceShowGuideSettingsManager extends Manager<EdsForceShowGuidePanelSettings> {
    Boolean getForceItToShowByType(ListPanelType panelType);
}
