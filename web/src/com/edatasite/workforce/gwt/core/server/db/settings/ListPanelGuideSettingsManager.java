package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsListPanelGuideSettings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface ListPanelGuideSettingsManager extends Manager<EdsListPanelGuideSettings> {
    EdsListPanelGuideSettings getUserListPanelSettingsByType(ListPanelType type);
}
