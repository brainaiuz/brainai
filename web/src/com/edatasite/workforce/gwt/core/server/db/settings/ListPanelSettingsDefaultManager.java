package com.edatasite.workforce.gwt.core.server.db.settings;

import com.edatasite.workforce.core.domain.settings.EdsListPanelSettingsDefault;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

public interface ListPanelSettingsDefaultManager extends Manager<EdsListPanelSettingsDefault> {

    EdsListPanelSettingsDefault getUserListPanelSettingsDefault(String type);

    List<EdsListPanelSettingsDefault> getPanelListDefaultSettings();

}
