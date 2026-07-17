package com.edatasite.workforce.gwt.core.server.db.impl.settings;

import com.edatasite.workforce.core.domain.settings.EdsListPanelSettingsDefault;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.ListPanelSettingsDefaultManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("listPanelSettingsDefaultManager")
public class ListPanelSettingsDefaultManagerImpl extends BaseManager<EdsListPanelSettingsDefault> implements ListPanelSettingsDefaultManager {
    public ListPanelSettingsDefaultManagerImpl() {
        super(EdsListPanelSettingsDefault.class);
    }

    @Override
    public EdsListPanelSettingsDefault getUserListPanelSettingsDefault(String type) {
        if (type == null || type.equals("")) {
            return null;
        }
        return (EdsListPanelSettingsDefault) findSingle("select lp from EdsListPanelSettingsDefault lp where lp.panelType=?", type);
    }

    @Override
    public List<EdsListPanelSettingsDefault> getPanelListDefaultSettings() {
        return find("select adp from EdsListPanelSettingsDefault adp");
    }
}
