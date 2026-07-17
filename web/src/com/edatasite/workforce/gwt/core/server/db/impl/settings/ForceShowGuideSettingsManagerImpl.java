package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsForceShowGuidePanelSettings;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ForceShowGuideSettingsManager;
import org.springframework.stereotype.Repository;

@Repository("forceShowGuideSettingsManager")
public class ForceShowGuideSettingsManagerImpl extends BaseManager<EdsForceShowGuidePanelSettings> implements ForceShowGuideSettingsManager {
    public ForceShowGuideSettingsManagerImpl() {
        super(EdsForceShowGuidePanelSettings.class);
    }

    @Override
    public Boolean getForceItToShowByType(ListPanelType panelType) {
        return (Boolean) findSingle("select fsgps.forceItToShow from EdsForceShowGuidePanelSettings fsgps where fsgps.panelType = ?", panelType.name());
    }
}
