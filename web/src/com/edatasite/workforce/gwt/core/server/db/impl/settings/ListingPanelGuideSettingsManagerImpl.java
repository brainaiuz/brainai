package com.edatasite.workforce.gwt.core.server.db.impl.settings;


import com.edatasite.workforce.core.domain.settings.EdsListPanelGuideSettings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelGuideSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("listPanelGuideSettingsManager")
public class ListingPanelGuideSettingsManagerImpl extends BaseManager<EdsListPanelGuideSettings> implements ListPanelGuideSettingsManager {
    public ListingPanelGuideSettingsManagerImpl() {
        super(EdsListPanelGuideSettings.class);
    }

    @Override
    public EdsListPanelGuideSettings getUserListPanelSettingsByType(ListPanelType type) {
        return (EdsListPanelGuideSettings) findSingle("select lpgs from EdsListPanelGuideSettings lpgs where lpgs.panelType=?", type.name());
    }
}
